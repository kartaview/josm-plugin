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
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.IconConfig;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Defines the {@code OpenStreetViewDetailsDialog} action panel. The user can perform the following actions: filter,
 * jump to photo location and open photo web page.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel {

    private static final long serialVersionUID = -2909078640977666884L;
    private static final int ROWS = 1;
    private static final int COLS = 5;
    private static final Dimension DIM = new Dimension(200, 23);

    private final JButton btnFilter;
    private final JButton btnLocation;
    private final JButton btnWebPage;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();
        btnFilter = GuiBuilder.buildButton(null, iconConfig.getFilterIcon(), guiConfig.getBtnFilterTlt(), true);
        btnLocation = GuiBuilder.buildButton(null, iconConfig.getLocationIcon(), guiConfig.getBtnLocationTlt(), true);
        btnWebPage = GuiBuilder.buildButton(null, iconConfig.getWebPageIcon(), guiConfig.getBtnWebPageTlt(), true);

        add(btnFilter);
        add(btnLocation);
        add(btnWebPage);
        setPreferredSize(DIM);
    }

}