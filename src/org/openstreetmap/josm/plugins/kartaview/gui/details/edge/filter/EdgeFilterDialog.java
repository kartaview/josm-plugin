/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.edge.filter;

import com.grab.josm.common.gui.CancelAction;
import com.grab.josm.common.gui.ModalDialog;
import com.grab.josm.common.gui.builder.ButtonBuilder;
import com.grab.josm.common.gui.builder.ContainerBuilder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.GuiSizesHelper;


/**
 * Dialog window that displays the filters that can be applied to the Edge detections layer.
 *
 * @author maria.mitisor
 */
public class EdgeFilterDialog extends ModalDialog {

    private static final long serialVersionUID = -8822903239223085640L;
    private static final Dimension DIM = new Dimension(650, 550);

    private EdgeFilterPanel pnlFilter;

    public EdgeFilterDialog(final ImageIcon icon) {
        super(GuiConfig.getInstance().getDlgFilterTitle(), icon.getImage());
        final Dimension dimension = GuiSizesHelper.getDimensionDpiAdjusted(DIM);

        createComponents();
        setLocationRelativeTo(MainApplication.getMap().mapView);

        setMinimumSize(dimension);
        setMaximumSize(dimension);
        setPreferredSize(dimension);
        this.setResizable(false);
    }

    @Override
    protected void createComponents() {
        pnlFilter = new EdgeFilterPanel();
        final JButton btnOk =
                ButtonBuilder.build(new EdgeFilterDialog.OkAction(), GuiConfig.getInstance().getBtnOkLbl());
        final JButton btnClear =
                ButtonBuilder.build(new EdgeFilterDialog.ResetAction(), GuiConfig.getInstance().getBtnResetLbl());
        final JButton btnCancel =
                ButtonBuilder.build(new CancelAction(this), GuiConfig.getInstance().getBtnCancelLbl());
        final JPanel pnlButton = ContainerBuilder.buildFlowLayoutPanel(FlowLayout.RIGHT, btnOk, btnClear, btnCancel);
        add(pnlFilter, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);
    }

    /**
     * It applies the selected edge filters.
     *
     * @author adina.misaras
     */
    private final class OkAction extends AbstractAction {

        private static final long serialVersionUID = 1773050424908204520L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            final EdgeSearchFilter newFilter = pnlFilter.selectedEdgeFilters();
            if (Objects.nonNull(newFilter)) {
                final PreferenceManager prefManager = PreferenceManager.getInstance();
                final EdgeSearchFilter oldFilter = prefManager.loadEdgeSearchFilter();
                if (!newFilter.equals(oldFilter)) {
                    prefManager.saveEdgeSearchFilter(newFilter);
                    prefManager.saveEdgeFiltersChangedFlag(true);
                } else {
                    prefManager.saveEdgeFiltersChangedFlag(false);
                }
                dispose();
            }
        }
    }


    /**
     * It resets the selected edge filters.
     *
     * @author adina.misaras
     */
    private final class ResetAction extends AbstractAction {

        private static final long serialVersionUID = 8966136663900718778L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            pnlFilter.resetFilters();
        }
    }
}