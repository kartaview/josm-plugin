/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetview.gui.details;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.Shortcut;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetViewDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = -8089399825436744652L;
    private static final Dimension DIM = new Dimension(150, 110);
    private static final int DLG_HEIGHT = 100;
    private static Shortcut shortcut = Shortcut.registerShortcut(GuiConfig.getInstance().getPluginShortName(),
            GuiConfig.getInstance().getPluginLongName(), KeyEvent.VK_F10, Shortcut.NONE);


    public OpenStreetViewDetailsDialog() {
        super(GuiConfig.getInstance().getPluginShortName(), IconConfig.getInstance().getDialogShortcutName(),
                GuiConfig.getInstance().getPluginLongName(), shortcut, DLG_HEIGHT, true);
        add(createLayout(new JPanel(), false, null));
        setPreferredSize(DIM);
    }
}