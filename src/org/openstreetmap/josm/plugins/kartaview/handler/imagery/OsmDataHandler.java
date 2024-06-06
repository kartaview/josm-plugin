/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler.imagery;

import java.util.Collection;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.progress.NullProgressMonitor;
import org.openstreetmap.josm.io.MultiFetchServerObjectReader;
import org.openstreetmap.josm.io.OsmTransferException;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmElement;


/**
 * Handles data download from the OSM service.
 *
 * @author laurad
 */
public class OsmDataHandler {

    public static final int INVALID_ID_VALUE = 0;

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
            final MultiFetchServerObjectReader reader = MultiFetchServerObjectReader.create();
            for (final OsmElement element : elements) {
                appendOsmPrimitive(reader, element);
                if (element.getMembers() != null) {
                    for (final OsmElement member : element.getMembers()) {
                        appendOsmPrimitive(reader, member);
                    }
                }
            }
            try {
                result = reader.parseOsm(NullProgressMonitor.INSTANCE);
            } catch (final OsmTransferException e1) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(MainApplication.getMainPanel(),
                        GuiConfig.getInstance().getErrorDownloadOsmData(), GuiConfig.getInstance().getWarningTitle(),
                        JOptionPane.WARNING_MESSAGE));
            }
        }
        return Optional.ofNullable(result);
    }

    private static void appendOsmPrimitive(final MultiFetchServerObjectReader reader, final OsmElement element) {
        switch (element.getType()) {
            case NODE:
                if (element.getOsmId() != null && element.getOsmId() > INVALID_ID_VALUE) {
                    reader.append(new Node(element.getOsmId()));
                }
                break;
            case WAY:
                if (element.getOsmId() != null && element.getOsmId() > INVALID_ID_VALUE) {
                    reader.append(new Way(element.getOsmId()));
                }
                break;
            case WAY_SECTION:
                if (element.getFromId() != null && element.getToId() != null) {
                    reader.append(new Node(element.getFromId()));
                    reader.append(new Node(element.getToId()));
                }
                if (element.getOsmId() != null && element.getOsmId() > INVALID_ID_VALUE) {
                    reader.append(new Way(element.getOsmId()));
                }
                break;
            case RELATION:
                if (element.getOsmId() != null && element.getOsmId() > INVALID_ID_VALUE) {
                    reader.append(new Relation(element.getOsmId()));
                }
                break;
        }
    }
}