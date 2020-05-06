/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

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
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.GuiSizesHelper;
import com.grab.josm.common.gui.CancelAction;
import com.grab.josm.common.gui.ModalDialog;
import com.grab.josm.common.gui.builder.ButtonBuilder;
import com.grab.josm.common.gui.builder.ContainerBuilder;


/**
 * Dialog window that displays the {@code ListFilter}s that can be applied to the OpenStreetCam layer.
 *
 * @author Beata
 * @version $Revision$
 */
public class FilterDialog extends ModalDialog {

    private static final long serialVersionUID = -8822903239223085640L;
    private static final Dimension HIGH_ZOOM_DIM = new Dimension(700, 600);
    private static final Dimension DIM = new Dimension(380, 150);

    private FilterPanel pnlFilter;
    private boolean isHighLevelZoom = false;

    public FilterDialog(final ImageIcon icon) {
        super(GuiConfig.getInstance().getDlgFilterTitle(), icon.getImage());
        final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
        final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
        Dimension dimension = GuiSizesHelper.getDimensionDpiAdjusted(DIM);
        if (zoom >= mapViewSettings.getPhotoZoom()) {
            isHighLevelZoom = true;
            dimension = GuiSizesHelper.getDimensionDpiAdjusted(HIGH_ZOOM_DIM);
        }
        createComponents();
        setLocationRelativeTo(MainApplication.getMap().mapView);

        setMinimumSize(dimension);
        setMaximumSize(dimension);
        setPreferredSize(dimension);
        this.setResizable(false);
    }

    @Override
    protected void createComponents() {
        pnlFilter = new FilterPanel(isHighLevelZoom);
        final JButton btnOk = ButtonBuilder.build(new OkAction(), GuiConfig.getInstance().getBtnOkLbl());
        final JButton btnClear = ButtonBuilder.build(new ResetAction(), GuiConfig.getInstance().getBtnResetLbl());
        final JButton btnCancel =
                ButtonBuilder.build(new CancelAction(this), GuiConfig.getInstance().getBtnCancelLbl());
        final JPanel pnlButton = ContainerBuilder.buildFlowLayoutPanel(FlowLayout.RIGHT, btnOk, btnClear, btnCancel);
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
            final SearchFilter result = pnlFilter.selectedFilters();
            if (result != null) {
                final PreferenceManager prefManager = PreferenceManager.getInstance();
                final SearchFilter oldFilter = prefManager.loadSearchFilter();
                if (checkFilterChanged(result, oldFilter, isHighLevelZoom)) {
                    prefManager.saveListFilter(result, isHighLevelZoom);
                    prefManager.saveFiltersChangedFlag(true);

                } else {
                    prefManager.saveFiltersChangedFlag(false);
                }
                dispose();
            }
        }
    }

    private boolean checkFilterChanged(final SearchFilter newFilter, final SearchFilter oldFilter,
            final boolean isHighLevelZoom) {
        return (isHighLevelZoom && !newFilter.equals(oldFilter)) || (!isHighLevelZoom && (
                newFilter.isOlnyUserData() != oldFilter.isOlnyUserData() || !Objects
                        .equals(newFilter.getDate(), oldFilter.getDate())));
    }


    /**
     * Clears the selected filters
     *
     * @author beataj
     * @version $Revision$
     */
    private final class ResetAction extends AbstractAction {

        private static final long serialVersionUID = -8589369992232950474L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            pnlFilter.resetFilters();
        }
    }
}