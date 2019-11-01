/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import java.awt.event.ActionEvent;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter.FilterDialog;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;


/**
 * Displays the filter dialog window.
 *
 * @author beataj
 * @version $Revision$
 */
class DisplayFilterDialogAction extends JosmAction {

    private static final long serialVersionUID = 8325126526750975651L;


    DisplayFilterDialogAction() {
        super(GuiConfig.getInstance().getDlgFilterTitle(), IconConfig.getInstance().getFilterIconName(),
                GuiConfig.getInstance().getDlgFilterTitle(),
                ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getDlgFilterShortcutText()), true);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final FilterDialog filterDialog = new FilterDialog(IconConfig.getInstance().getFilterIcon());
        filterDialog.setVisible(true);
    }
}