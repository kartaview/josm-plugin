/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.common;

import java.awt.event.ActionEvent;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.plugins.openstreetcam.DownloadWayTask;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;


/**
 * Downloads a matched OSM element (way, node or relation).
 *
 * @author beataj
 * @version $Revision$
 */
public abstract class DownloadMatchedOsmElement extends JosmAction {

    private static final long serialVersionUID = -1073602145436061511L;


    public DownloadMatchedOsmElement(final String name, final String tooltip) {
        super(name, null, tooltip,
                ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnMatchedWayShortcutTlt()), true);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final PrimitiveId osmId = getPrimitiveId();
        if (osmId != null) {
            final boolean downloaded = Util.editLayerContainsWay(osmId);
            if (downloaded) {
                GuiHelper.runInEDT(() -> MainApplication.getLayerManager().getEditDataSet().setSelected(osmId));
            } else {
                final DownloadWayTask task = new DownloadWayTask(osmId);
                MainApplication.worker.submit(task);
            }
        }

    }

    protected abstract PrimitiveId getPrimitiveId();
}