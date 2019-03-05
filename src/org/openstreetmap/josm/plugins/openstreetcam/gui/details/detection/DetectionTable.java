/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.observer.RowSelectionObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.RowSelectionObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;


/**
 * Builds a table with information about the selected cluster's detections.
 *
 * @author nicoletav
 */
class DetectionTable extends JTable implements RowSelectionObservable {

    private static final long serialVersionUID = 1L;
    private static final int ROW_HEIGHT = 15;
    private static final int TABLE_COLUMNS_EXTRA_WIDTH = 12;
    private static final int ID_COLUMN = 0;
    private static final int UPPER_ROW_MOVEMENT = -1;
    private static final int LOWER_ROW_MOVEMENT = 1;

    private Cluster cluster;
    private int tableWidth = 0;
    private RowSelectionObserver observer;

    /**
     * @param cluster represents the selected cluster from the map
     */

    DetectionTable(Cluster cluster) {
        super(new DetectionsTableModel(cluster.getDetections()));
        this.cluster = cluster;

        final String[] header = GuiConfig.getInstance().getClusterTableHeader();
        this.setRowHeight(ROW_HEIGHT);

        getTableHeader().setDefaultRenderer(new DetectionTableCellRenderer());

        for (int i = 0; i < header.length; i++) {
            final TableColumn column = getColumnModel().getColumn(i);
            column.setCellRenderer(new DetectionTableCellRenderer());
            column.setResizable(false);
        }

        this.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                Detection selectedDetection = getSelectedDetection();
                notifyRowSelectionObserver(selectedDetection);
            }
        });

        if (cluster.getDetections() != null && !cluster.getDetections().isEmpty()) {
            adjustColumnSizes();
        }

        this.getInputMap()
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), GuiConfig.getInstance().getNextRowSelection());
        this.getActionMap().put(GuiConfig.getInstance().getNextRowSelection(), new SelectRowAction(LOWER_ROW_MOVEMENT));
        this.getInputMap()
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), GuiConfig.getInstance().getPrevRowSelection());
        this.getActionMap().put(GuiConfig.getInstance().getPrevRowSelection(), new SelectRowAction(UPPER_ROW_MOVEMENT));

        adjustColumnSizes();
    }

    private class SelectRowAction extends AbstractAction {

        private int direction;

        SelectRowAction(final int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            Detection selectedTableDetection = cluster.getDetections().get(getSelectedRow());
            if ((getSelectedRow() < cluster.getDetectionIds().size() - 1 && direction == LOWER_ROW_MOVEMENT) ||
                    (getSelectedRow() > 0 && direction == UPPER_ROW_MOVEMENT)) {
                selectedTableDetection = cluster.getDetections().get(getSelectedRow() + direction);
            }
            notifyRowSelectionObserver(selectedTableDetection);
        }
    }

    private void adjustColumnSizes() {
        final DefaultTableColumnModel colModel = (DefaultTableColumnModel) getColumnModel();
        for (int i = 0; i < getColumnCount(); i++) {
            final TableColumn col = colModel.getColumn(i);
            int width;

            TableCellRenderer renderer = col.getHeaderRenderer();
            if (renderer == null) {
                renderer = getTableHeader().getDefaultRenderer();
            }
            Component comp = renderer.getTableCellRendererComponent(this, col.getHeaderValue(), false, false, 0, 0);
            width = comp.getPreferredSize().width;

            for (int r = 0; r < getRowCount(); r++) {
                renderer = getCellRenderer(r, i);
                comp = renderer.getTableCellRendererComponent(this, this.getValueAt(r, i), false, false, r, i);
                final int currentWidth = comp.getPreferredSize().width;
                width = Math.max(width, currentWidth);
            }

            width += TABLE_COLUMNS_EXTRA_WIDTH;
            tableWidth += width;

            col.setPreferredWidth(width);
            col.setWidth(width);
            col.setMinWidth(width);
        }
    }

    private Detection getSelectedDetection() {
        Detection selectedDetection = null;
        final Long selectedDetectionId = Long.valueOf(this.getValueAt(this.getSelectedRow(), ID_COLUMN).toString());
        final List<Detection> detectionsList = cluster.getDetections();
        int index = 0;
        while (index < detectionsList.size()) {
            long currentDetectionId = detectionsList.get(index).getId();
            if (currentDetectionId == selectedDetectionId) {
                selectedDetection = detectionsList.get(index);
            }
            ++index;
        }

        return selectedDetection;
    }

    Cluster getCluster() {
        return cluster;
    }


    int getTableWidth() {
        return tableWidth;
    }

    @Override
    public void registerObserver(final RowSelectionObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyRowSelectionObserver(final Detection detection) {
        observer.selectDetectionFromTable(detection);
    }
}