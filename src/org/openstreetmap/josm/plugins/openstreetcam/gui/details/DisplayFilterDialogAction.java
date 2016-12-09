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
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;


/**
 * Displays the filter dialog window.
 *
 * @author Beata
 * @version $Revision$
 */
public class DisplayFilterDialogAction extends AbstractAction {

    private static final long serialVersionUID = 8325126526750975651L;


    public DisplayFilterDialogAction() {
        super(GuiConfig.getInstance().getDlgFilterTitle(), IconConfig.getInstance().getFilterIcon());
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final FilterDialog filterDialog = new FilterDialog();
        filterDialog.setVisible(true);
    }
}