/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.plugins.kartaview.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;

import com.grab.josm.common.gui.ModalDialog;


/**
 * Action displaying the filter dialog window.
 *
 * @author beata.tautan
 * @version $Revision$
 */
final class DisplayFilterDialogAction extends JosmAction {

    private static final long serialVersionUID = -4179121968166563158L;

    private final ModalDialog filterDialog;

    DisplayFilterDialogAction(ModalDialog filterDialog) {
        super(GuiConfig.getInstance().getDlgFilterTitle(), IconConfig.getInstance().getFilterIconName(), GuiConfig
                .getInstance().getDlgFilterTitle(), ShortcutFactory.getInstance().getShortcut(GuiConfig.getInstance()
                        .getDlgFilterShortcutText()), true);
        this.filterDialog = filterDialog;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        SwingUtilities.invokeLater(() -> filterDialog.setVisible(true));
    }
}