/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.edge.detection;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.openstreetmap.josm.gui.dialogs.properties.PropertiesCellRenderer;
import org.openstreetmap.josm.plugins.kartaview.entity.Attribute;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;


/**
 * Table containing the custom attributes of an Edge detection.
 *
 * @author maria.mitisor
 */
class AttributeTable extends JTable {

    private static final long serialVersionUID = -5126108981152454480L;

    private static final int ROW_HEIGHT = 15;
    private static final int TABLE_COLUMNS_EXTRA_WIDTH = 12;

    private int tableWidth = 0;

    AttributeTable(final List<Attribute> attributes) {
        super(new AttributeTableModel(attributes));

        final String[] header = GuiConfig.getInstance().getAttributeTableHeader();
        this.setRowHeight(ROW_HEIGHT);

        getTableHeader().setDefaultRenderer(new AttributeTableHeaderCellRenderer());
        getTableHeader().setMinimumSize(new Dimension(0, ROW_HEIGHT));
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);
        setRowSelectionAllowed(true);

        for (int i = 0; i < header.length; i++) {
            final TableColumn column = getColumnModel().getColumn(i);
            column.setCellRenderer(new PropertiesCellRenderer());
            column.setResizable(false);
        }

        if (attributes != null && !attributes.isEmpty()) {
            adjustColumnSizes();
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


    int getTableWidth() {
        return tableWidth;
    }
}