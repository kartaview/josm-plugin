/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
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
import com.telenav.josm.common.gui.CancelAction;
import com.telenav.josm.common.gui.ModalDialog;
import com.telenav.josm.common.gui.builder.ButtonBuilder;
import com.telenav.josm.common.gui.builder.ContainerBuilder;
import org.openstreetmap.josm.tools.GuiSizesHelper;


/**
 * Dialog window that displays the {@code ListFilter}s that can be applied to the OpenStreetCam layer.
 *
 * @author Beata
 * @version $Revision$
 */
public class FilterDialog extends ModalDialog {

    private static final long serialVersionUID = -8822903239223085640L;
    private static final Dimension HIGH_ZOOM_DIM = new Dimension(700, 550);
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

        setSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
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
                if (!result.equals(oldFilter)) {
                    prefManager.saveListFilter(result);
                    prefManager.saveFiltersChangedFlag(true);

                } else {
                    prefManager.saveFiltersChangedFlag(false);
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
    private final class ResetAction extends AbstractAction {

        private static final long serialVersionUID = -8589369992232950474L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            pnlFilter.resetFilters();
        }
    }
}