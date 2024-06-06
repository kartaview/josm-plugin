/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.detection;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.observer.RowSelectionObservable;
import org.openstreetmap.josm.plugins.kartaview.observer.RowSelectionObserver;


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
    private static final int FIRST_DETECTION = 0;

    private final transient Cluster cluster;
    private int tableWidth = 0;
    private transient RowSelectionObserver observer;

    /**
     * @param cluster represents the selected cluster from the map
     */
    DetectionTable(final Cluster cluster) {
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

        this.addMouseListener(new MouseActionsListener());
        this.setTableKeyStrokes();
        if (cluster.getDetections() != null && !cluster.getDetections().isEmpty()) {
            adjustColumnSizes();
        }
    }

    private void setTableKeyStrokes() {
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                GuiConfig.getInstance().getNextRowSelection());
        this.getActionMap().put(GuiConfig.getInstance().getNextRowSelection(), new SelectRowAction(LOWER_ROW_MOVEMENT));
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                GuiConfig.getInstance().getPrevRowSelection());
        this.getActionMap().put(GuiConfig.getInstance().getPrevRowSelection(), new SelectRowAction(UPPER_ROW_MOVEMENT));
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
            final long currentDetectionId = detectionsList.get(index).getId();
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
        observer.selectDetectionFromTable(detection, true);
    }

    private class SelectRowAction extends AbstractAction {

        private static final long serialVersionUID = -303889953263348330L;
        private final int direction;

        private SelectRowAction(final int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            int index = 0;
            if ((getSelectedRow() < cluster.getDetectionIds().size() - 1 && direction == LOWER_ROW_MOVEMENT)
                    || (getSelectedRow() > FIRST_DETECTION && direction == UPPER_ROW_MOVEMENT)) {
                index = getSelectedRow() + direction;
            }
            if (getSelectedRow() == cluster.getDetectionIds().size() - 1 && direction == LOWER_ROW_MOVEMENT) {
                index = FIRST_DETECTION;
            }
            if (getSelectedRow() == FIRST_DETECTION && direction == UPPER_ROW_MOVEMENT) {
                index = cluster.getDetectionIds().size() - 1;
            }
            index = index < cluster.getDetections().size() ? index : 0;
            final Detection selectedTableDetection = cluster.getDetections().get(index);
            notifyRowSelectionObserver(selectedTableDetection);
        }
    }

    private class MouseActionsListener extends MouseAdapter {

        @Override
        public void mouseClicked(final MouseEvent e) {
            final Detection selectedDetection = getSelectedDetection();
            notifyRowSelectionObserver(selectedDetection);
        }
    }
}