/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.edge.detection;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.openstreetmap.josm.gui.dialogs.properties.PropertiesCellRenderer;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.gui.details.common.TableHeaderCellRenderer;
import org.openstreetmap.josm.plugins.kartaview.observer.EdgeDetectionRowSelectionObservable;
import org.openstreetmap.josm.plugins.kartaview.observer.EdgeDetectionRowSelectionObserver;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;


/**
 * Table containing the Edge detections belonging to an Edge cluster.
 *
 * @author maria.mitisor
 */
public class EdgeDetectionsTable extends JTable implements EdgeDetectionRowSelectionObservable {

    private static final long serialVersionUID = -5126108981152454480L;

    private static final int ROW_HEIGHT = 15;
    private static final int TABLE_COLUMNS_EXTRA_WIDTH = 12;
    private static final int ID_COLUMN = 0;
    private static final int UPPER_ROW_MOVEMENT = -1;
    private static final int LOWER_ROW_MOVEMENT = 1;
    private static final int FIRST_DETECTION = 0;

    private int tableWidth = 0;
    private final Cluster edgeCluster;
    private transient EdgeDetectionRowSelectionObserver observer;


    EdgeDetectionsTable(final Cluster edgeCluster) {
        super(new EdgeDetectionsTableModel(edgeCluster.getEdgeDetections()));
        this.edgeCluster = edgeCluster;

        final String[] header = GuiConfig.getInstance().getEdgeDetectionsTableHeader();
        this.setRowHeight(ROW_HEIGHT);

        getTableHeader().setDefaultRenderer(new TableHeaderCellRenderer());
        getTableHeader().setMinimumSize(new Dimension(0, ROW_HEIGHT));
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);
        setRowSelectionAllowed(true);

        for (int i = 0; i < header.length; i++) {
            final TableColumn column = getColumnModel().getColumn(i);
            column.setCellRenderer(new PropertiesCellRenderer());
            column.setResizable(false);
        }

        if (Objects.nonNull(edgeCluster.getEdgeDetections()) && !edgeCluster.getEdgeDetections().isEmpty()) {
            adjustColumnSizes();
        }
        this.setTableKeyStrokes();
        this.addMouseListener(new EdgeDetectionsTable.MouseActionsListener());
    }

    private void setTableKeyStrokes() {
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), GuiConfig.getInstance()
                .getNextRowSelection());
        this.getActionMap().put(GuiConfig.getInstance().getNextRowSelection(), new EdgeDetectionsTable.SelectRowAction(
                LOWER_ROW_MOVEMENT));
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), GuiConfig.getInstance()
                .getPrevRowSelection());
        this.getActionMap().put(GuiConfig.getInstance().getPrevRowSelection(), new EdgeDetectionsTable.SelectRowAction(
                UPPER_ROW_MOVEMENT));
    }

    private void adjustColumnSizes() {
        final DefaultTableColumnModel colModel = (DefaultTableColumnModel) getColumnModel();
        for (int i = 0; i < getColumnCount(); i++) {
            final TableColumn col = colModel.getColumn(i);
            int width;

            TableCellRenderer renderer = col.getHeaderRenderer();
            if (Objects.isNull(renderer)) {
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

    @Override
    public void registerObserver(final EdgeDetectionRowSelectionObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyRowSelectionObserver(final EdgeDetection edgeDetection) {
        observer.selectEdgeDetectionFromTable(edgeDetection);
    }

    public Cluster getEdgeCluster() {
        return edgeCluster;
    }

    int getTableWidth() {
        return tableWidth;
    }

    private EdgeDetection getSelectedEdgeDetection() {
        final Long selectedDetectionId = Long.valueOf(this.getValueAt(this.getSelectedRow(), ID_COLUMN).toString());
        return edgeCluster.getEdgeDetections().stream().filter(edgeDetection -> edgeDetection.getId().equals(
                selectedDetectionId)).collect(Collectors.toList()).get(0);
    }


    /**
     * Action listening for the detection table selection actions from the keyboard. When a detection is selected from
     * the table the selected state should be illustrated accordingly on the map.
     */
    private class SelectRowAction extends AbstractAction {

        private static final long serialVersionUID = 683092478683984266L;
        private final int direction;

        private SelectRowAction(final int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            int index = 0;
            if ((getSelectedRow() < edgeCluster.getDetectionIds().size() - 1 && direction == LOWER_ROW_MOVEMENT)
                    || (getSelectedRow() > FIRST_DETECTION && direction == UPPER_ROW_MOVEMENT)) {
                index = getSelectedRow() + direction;
            }
            if (getSelectedRow() == edgeCluster.getDetectionIds().size() - 1 && direction == LOWER_ROW_MOVEMENT) {
                index = FIRST_DETECTION;
            }
            if (getSelectedRow() == FIRST_DETECTION && direction == UPPER_ROW_MOVEMENT) {
                index = edgeCluster.getDetectionIds().size() - 1;
            }
            index = index < edgeCluster.getEdgeDetections().size() ? index : 0;
            final EdgeDetection selectedTableDetection = edgeCluster.getEdgeDetections().get(index);
            notifyRowSelectionObserver(selectedTableDetection);
        }
    }


    /**
     * Action listening for the detection table selection actions from the mouse. When a detection is selected from the
     * table the selected state should be illustrated accordingly on the map.
     */
    private class MouseActionsListener extends MouseAdapter {

        @Override
        public void mouseClicked(final MouseEvent e) {
            final EdgeDetection selectedDetection = getSelectedEdgeDetection();
            notifyRowSelectionObserver(selectedDetection);
        }
    }
}