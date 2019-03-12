/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;

import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.progress.NullProgressMonitor;
import org.openstreetmap.josm.io.MultiFetchServerObjectReader;
import org.openstreetmap.josm.io.OsmTransferException;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmElement;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.Collection;
import java.util.Optional;


/**
 * Handles data download from the OSM service.
 *
 * @author laurad
 */
public class OsmDataHandler {

    private OsmDataHandler() {
    }

    /**
     * Retrieves the given OsmElements from the server.
     *
     * @param elements - List of OsmElements to be retrieved
     * @return DataSet containing the OsmPrimitives
     */
    public static Optional<DataSet> retrieveServerObjects(final Collection<OsmElement> elements) {
        DataSet result = null;
        if (elements != null) {
            MultiFetchServerObjectReader reader = MultiFetchServerObjectReader.create();
            for (OsmElement element : elements) {
                appendOsmPrimitive(reader, element);
                if (element.getMembers() != null) {
                    for (OsmElement member : element.getMembers()) {
                        appendOsmPrimitive(reader, member);
                    }
                }
            }
            try {
                result = reader.parseOsm(NullProgressMonitor.INSTANCE);
            } catch (OsmTransferException e1) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(MainApplication.getMainPanel(),
                        "Error retrieving OSM members from the OSM service", GuiConfig.getInstance().getWarningTitle(),
                        JOptionPane.WARNING_MESSAGE));
            }
        }
        return Optional.ofNullable(result);
    }

    private static void appendOsmPrimitive(final MultiFetchServerObjectReader reader, final OsmElement element) {
        switch (element.getType()) {
            case NODE:
                if (element.getOsmId() != null && element.getOsmId() > 0) {
                    reader.append(new Node(element.getOsmId()));
                }
                break;
            case WAY:
                if (element.getOsmId() != null && element.getOsmId() > 0) {
                    reader.append(new Way(element.getOsmId()));
                }
                break;
            case WAY_SECTION:
                if (element.getFromId() != null && element.getToId() != null) {
                    reader.append(new Node(element.getFromId()));
                    reader.append(new Node(element.getToId()));
                }
                if (element.getOsmId() != null && element.getOsmId() > 0) {
                    reader.append(new Way(element.getOsmId()));
                }
                break;
            case RELATION:
                if (element.getOsmId() != null && element.getOsmId() > 0) {
                    reader.append(new Relation(element.getOsmId()));
                }
                break;
        }
    }
}