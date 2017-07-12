/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import java.awt.event.ActionEvent;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.preferences.PreferenceDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;


/**
 * Opens the preference settings dialog and selects the OpenStreetCam plugin related settings tab.
 *
 * @author beataj
 * @version $Revision$
 */
class OpenPreferenceDialogAction extends JosmAction {

    private static final long serialVersionUID = -5408882391844055637L;


    OpenPreferenceDialogAction() {
        super(GuiConfig.getInstance().getLayerPreferenceMenuItemLbl(), IconConfig.getInstance().getPreferenceIconName(),
                GuiConfig.getInstance().getLayerPreferenceMenuItemLbl(), null, true);
    }


    @Override
    public void actionPerformed(final ActionEvent event) {
        final PreferenceDialog dialog = new PreferenceDialog(Main.parent);
        dialog.selectPreferencesTabByClass(PreferenceEditor.class);
        dialog.setVisible(true);
    }
}