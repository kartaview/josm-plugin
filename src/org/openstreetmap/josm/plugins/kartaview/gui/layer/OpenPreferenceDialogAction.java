/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import java.awt.event.ActionEvent;
import org.openstreetmap.josm.plugins.kartaview.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.preferences.PreferenceDialog;


/**
 * Opens the preference settings dialog and selects the KartaView plugin related settings tab.
 *
 * @author beataj
 * @version $Revision$
 */
final class OpenPreferenceDialogAction extends JosmAction {

    private static final long serialVersionUID = -5408882391844055637L;


    OpenPreferenceDialogAction() {
        super(GuiConfig.getInstance().getLayerPreferenceMenuItemLbl(), IconConfig.getInstance().getPreferenceIconName(),
                GuiConfig.getInstance().getLayerPreferenceMenuItemLbl(), null, true);
    }


    @Override
    public void actionPerformed(final ActionEvent event) {
        final PreferenceDialog dialog = new PreferenceDialog(MainApplication.getMainFrame());
        dialog.selectPreferencesTabByClass(PreferenceEditor.class);
        dialog.setVisible(true);
    }
}