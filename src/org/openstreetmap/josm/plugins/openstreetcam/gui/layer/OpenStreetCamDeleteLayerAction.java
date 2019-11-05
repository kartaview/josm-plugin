/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import java.awt.event.ActionEvent;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.layer.DeleteLayerAction;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


/**
 * Deletes the OpenStreetCam layer.
 *
 * @author ioanao
 * @version $Revision$
 */
final class OpenStreetCamDeleteLayerAction extends JosmAction {

    private static final long serialVersionUID = 1569467764140753112L;
    private final DeleteLayerAction deleteAction = LayerListDialog.getInstance().createDeleteLayerAction();

    OpenStreetCamDeleteLayerAction() {
        super(GuiConfig.getInstance().getLayerDeleteMenuItemLbl(), IconConfig.getInstance().getDeleteIconName(),
                GuiConfig.getInstance().getLayerDeleteMenuItemTlt(),
                ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getLayerDeleteMenuItemLbl()), true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        PreferenceManager.getInstance().saveLayerOpenedFlag(false);
        deleteAction.actionPerformed(e);
    }
}