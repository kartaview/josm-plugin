/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.preferences;


import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 * This table models the color legend from plugin preferences for the cluster confidence level display.
 *
 * @author laurad
 */
class LegendTable {

    private final JScrollPane tablePane;
    private final JTable legendTable;
    private final LegendTableModel model;
    private static final Color R3 = new Color(255, 0, 0);
    private static final Color R2 = new Color(254, 255, 0);
    private static final Color R1 = new Color(0, 139, 0);
    private static final List<Color> COLORS = Arrays.asList(R1, R2, R3);
    private static final int ROW_HEIGHT = 15;
    private static final int TABLE_WIDTH = 13;
    private static final String FONT_NAME = "Tahoma";
    private static final Font FONT = new Font(FONT_NAME, Font.PLAIN, 12);

    LegendTable() {
        model = new LegendTableModel();
        legendTable = new JTable(model);
        legendTable.setRowHeight(ROW_HEIGHT);
        initColumnSizes();
        legendTable.setDefaultRenderer(String.class, new LegendTableRenderer());
        legendTable.getTableHeader().setBackground(Color.WHITE);
        legendTable.getTableHeader().setFont(FONT);
        legendTable.getTableHeader().setReorderingAllowed(false);
        legendTable.setPreferredScrollableViewportSize(new Dimension(TABLE_WIDTH, ROW_HEIGHT));

        tablePane = new JScrollPane(legendTable);
        tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    JScrollPane getComponent() {
        return tablePane;
    }

    private void initColumnSizes() {
        final FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        for (int i = 0; i < model.getColumnCount(); i++) {
            final TableColumn column = legendTable.getColumnModel().getColumn(i);
            final int textWidth = (int) (FONT.getStringBounds(model.getColumnName(i), frc).getWidth());
            column.setPreferredWidth(textWidth);
        }

    }

    private class LegendTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        private final String[] columnNames = GuiConfig.getInstance().getPrefClusterLegendHeaders();

        private final transient Object[][] data =
                { { GuiConfig.getInstance().getPrefClusterLegendHeaderColor(), "", "", "" } };


        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(final int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public Class<String> getColumnClass(final int c) {
            return String.class;
        }
    }


    private class LegendTableRenderer extends JLabel implements TableCellRenderer {

        private static final long serialVersionUID = 1L;

        private LegendTableRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                final boolean hasFocus, final int row, final int column) {
            if (column > 0) {
                setBackground(COLORS.get(column - 1));
            } else {
                final Component firstCell = table.getTableHeader().getDefaultRenderer()
                        .getTableCellRendererComponent(table, value, isSelected, hasFocus, 0, 0);
                firstCell.setBackground(Color.WHITE);
                firstCell.setFont(FONT);
                return firstCell;
            }
            setText((String) model.getValueAt(row, column));
            return this;
        }
    }
}