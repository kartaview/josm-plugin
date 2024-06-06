/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.plugins.kartaview.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;


/**
 * Deletes a plugin layer.
 *
 * @author maria.mitisor
 */
abstract class BaseDeleteLayerAction extends JosmAction {

    private static final long serialVersionUID = -8155780238532750943L;

    BaseDeleteLayerAction() {
        super(GuiConfig.getInstance().getLayerDeleteMenuItemLbl(), IconConfig.getInstance().getDeleteIconName(),
                GuiConfig.getInstance().getLayerDeleteMenuItemTlt(),
                ShortcutFactory.getInstance().getShortcut(GuiConfig.getInstance().getLayerDeleteMenuItemLbl()), true);
    }
}