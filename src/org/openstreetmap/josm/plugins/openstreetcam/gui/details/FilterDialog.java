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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.gui.CancelAction;
import com.telenav.josm.common.gui.GuiBuilder;
import com.telenav.josm.common.gui.ModalDialog;


/**
 * Dialog window that displays the {@code ListFilter}s that can be applied to the OpenStreetCam layer.
 *
 * @author Beata
 * @version $Revision$
 */
public class FilterDialog extends ModalDialog {

    private static final long serialVersionUID = -8822903239223085640L;
    private static final Dimension DIM = new Dimension(350, 150);

    private FilterPanel pnlFilter;


    public FilterDialog() {
        super(GuiConfig.getInstance().getDlgFilterTitle(), IconConfig.getInstance().getFilterIcon().getImage());
        createComponents();
        setLocationRelativeTo(Main.map.mapView);
        setSize(DIM);
        setMinimumSize(DIM);
        setPreferredSize(DIM);
    }

    @Override
    protected void createComponents() {
        pnlFilter = new FilterPanel();
        final JButton btnOk = GuiBuilder.buildButton(new OkAction(), GuiConfig.getInstance().getBtnOkLbl());
        final JButton btnClear = GuiBuilder.buildButton(new ClearAction(), GuiConfig.getInstance().getBtnClearLbl());
        final JButton btnCancel =
                GuiBuilder.buildButton(new CancelAction(this), GuiConfig.getInstance().getBtnCancelLbl());
        final JPanel pnlButton = GuiBuilder.buildFlowLayoutPanel(FlowLayout.RIGHT, btnOk, btnClear, btnCancel);
        add(pnlFilter, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);
    }


    /**
     * Applies the selected filters.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class OkAction extends AbstractAction {

        private static final long serialVersionUID = -1573801969230451122L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            final ListFilter result = pnlFilter.selectedFilters();
            if (result != null) {
                final PreferenceManager prefManager = PreferenceManager.getInstance();
                final ListFilter oldFilter = prefManager.loadListFilter();
                if (result.equals(oldFilter)) {
                    prefManager.saveFiltersChangedFlag(false);
                } else {
                    prefManager.saveListFilter(result);
                    prefManager.saveFiltersChangedFlag(true);
                }
                dispose();
            }
        }
    }


    /**
     * Clears the selected filters
     *
     * @author beataj
     * @version $Revision$
     */
    private final class ClearAction extends AbstractAction {

        private static final long serialVersionUID = -8589369992232950474L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            pnlFilter.clearFilters();
        }
    }
}