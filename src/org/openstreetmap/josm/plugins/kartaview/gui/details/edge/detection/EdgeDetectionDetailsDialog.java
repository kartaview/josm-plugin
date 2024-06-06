/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.edge.detection;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Objects;

import javax.swing.JScrollPane;

import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.kartaview.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.kartaview.observer.EdgeDetectionRowSelectionObserver;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;

import com.grab.josm.common.gui.builder.ContainerBuilder;


/**
 * Dialog containing information related to the selected Edge detection.
 *
 * @author maria.mitisor
 */
public final class EdgeDetectionDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = -8182753382630961923L;

    private static final Dimension DIM = new Dimension(150, 120);
    private static final int DLG_HEIGHT = 120;
    private static final int SCROLL_BAR_UNIT = 100;

    private static EdgeDetectionDetailsDialog instance = new EdgeDetectionDetailsDialog();

    private JScrollPane cmpInfo;
    private EdgeDetectionDetailsPanel pnlDetection;
    private final EdgeClusterDetailsPanel pnlCluster;
    private boolean isDetached = false;


    private EdgeDetectionDetailsDialog() {
        super(GuiConfig.getInstance().getEdgeDataName(), IconConfig.getInstance().getEdgeDetectionDialogShortcutName(),
                GuiConfig.getInstance().getEdgeDetectionShortcutLongText(), ShortcutFactory.getInstance().getShortcut(
                        GuiConfig.getInstance().getEdgeDetectionShortcutText()), DLG_HEIGHT, true,
                PreferenceEditor.class);

        pnlDetection = new EdgeDetectionDetailsPanel();
        pnlCluster = new EdgeClusterDetailsPanel();
        cmpInfo = ContainerBuilder.buildScrollPane(ContainerBuilder.buildEmptyPanel(Color.WHITE), null, Color.white,
                null, SCROLL_BAR_UNIT, false, DIM);
        add(createLayout(ContainerBuilder.buildBorderLayoutPanel(null, cmpInfo, null, null), false, null));

        setPreferredSize(DIM);
    }

    /**
     * Returns the unique instance of the Edge detection details dialog window.
     *
     * @return a {@code EdgeDetectionDetailsDialog}
     */
    public static synchronized EdgeDetectionDetailsDialog getInstance() {
        if (Objects.isNull(instance)) {
            instance = new EdgeDetectionDetailsDialog();
        }
        return instance;
    }

    /**
     * Destroys the instance of the dialog.
     */
    public static synchronized void destroyInstance() {
        if (Objects.nonNull(instance)) {
            instance.pnlDetection = null;
            instance.cmpInfo = null;
            instance = null;
        }
    }

    @Override
    public void destroy() {
        destroyInstance();
    }

    /**
     * Registers the observers to the corresponding UI components.
     *
     * @param rowSelectionObserver a {@code RowSelectionObserver} listens for row selection in the Edge detections table
     */
    public void registerObservers(final EdgeDetectionRowSelectionObserver rowSelectionObserver) {
        pnlCluster.registerObserver(rowSelectionObserver);
    }

    /**
     * Updates the dialog with edgeDetection information.
     *
     * @param edgeDetection an {@code EdgeDetection} object
     */
    public void updateEdgeDetectionDetails(final EdgeDetection edgeDetection) {
        setTitle(GuiConfig.getInstance().getEdgeDataName());
        pnlDetection.updateData(edgeDetection);
        cmpInfo.setViewportView(pnlDetection);
        cmpInfo.getViewport().revalidate();
        cmpInfo.revalidate();
        revalidate();
        repaint();
    }

    /**
     * Updates the dialog with the information of the selected Edge cluster.
     *
     * @param edgeCluster a {@code Cluster} object
     */
    public void updateEdgeClusterDetails(final Cluster edgeCluster) {
        if (Objects.nonNull(edgeCluster) && !edgeCluster.equals(pnlCluster.getCluster())) {
            setTitle(GuiConfig.getInstance().getEdgeDataName());
            pnlCluster.updateData(edgeCluster);
            pnlCluster.revalidate();
            cmpInfo.setViewportView(pnlCluster);
            cmpInfo.getViewport().revalidate();
            cmpInfo.revalidate();
            revalidate();
            repaint();
        }
    }

    /**
     * Clears the content of the panel.
     */
    public void clearDetailsDialog() {
        pnlCluster.setCluster(null);
        pnlDetection.updateData(null);
        pnlCluster.updateData(null);
        cmpInfo.setViewportView(pnlDetection);
        cmpInfo.getViewport().revalidate();
        cmpInfo.revalidate();
        revalidate();
        repaint();
    }

    /**
     * Selects the given detection from the table.
     *
     * @param edgeDetection {@code EdgeDetection} to be selected from the table
     */
    public void selectDetectionFromTable(EdgeDetection edgeDetection) {
        pnlCluster.addSelectedEdgeDetectionToTable(edgeDetection);
        pnlCluster.invalidate();
        cmpInfo.getViewport().revalidate();
        cmpInfo.revalidate();
        revalidate();
        repaint();
    }

    @Override
    public void expand() {
        showDialog();
        if (getButton() != null && !getButton().isSelected()) {
            getButton().setSelected(true);
        }
        getInstance().getButton().setSelected(true);
        if (isCollapsed) {
            super.expand();
        }
    }

    /**
     * It is called when the detached dialog is opened.
     */
    @Override
    protected void detach() {
        if (!isDetached) {
            super.detach();
            isDetached = true;
        }
    }

    @Override
    public void hideDialog() {
        super.hideDialog();
        isDetached = false;
    }
}