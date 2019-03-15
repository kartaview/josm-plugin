/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */

package org.openstreetmap.josm.plugins.openstreetcam.gui.preferences;


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
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;


/**
 * This table models the color legend from plugin preferences for the cluster confidence level display.
 *
 * @author laurad
 */
class LegendTable {

    private final JScrollPane tablePane;
    private final JTable legendTable;
    private final LegendTableModel model;
    private final Color R1 = new Color(255, 0, 0);
    private final Color R2 = new Color(255, 42, 0);
    private final Color R3 = new Color(254, 91, 0);
    private final Color R4 = new Color(255, 144, 0);
    private final Color R5 = new Color(255, 198, 2);
    private final Color R6 = new Color(254, 255, 0);
    private final Color R7 = new Color(198, 255, 0);
    private final Color R8 = new Color(144, 255, 3);
    private final Color R9 = new Color(91, 255, 0);
    private final Color R10 = new Color(0, 139, 0);
    private final List<Color> colors = Arrays.asList(R1, R2, R3, R4, R5, R6, R7, R8, R9, R10);
    private final int ROW_HEIGHT = 17;
    private static final Dimension LEGEND_TABLE_DIM = new Dimension(13, 38);
    private static final String FONT_NAME = "Tahoma";
    final private Font font = new Font(FONT_NAME, Font.PLAIN, 12);

    LegendTable() {
        model = new LegendTableModel();
        legendTable = new JTable(model);
        legendTable.setRowHeight(ROW_HEIGHT);
        initColumnSizes();
        legendTable.setDefaultRenderer(String.class, new LegendTableRenderer());
        legendTable.getTableHeader().setBackground(Color.WHITE);
        legendTable.getTableHeader().setFont(font);
        legendTable.getTableHeader().setReorderingAllowed(false);

        tablePane = new JScrollPane(legendTable);
        tablePane.setPreferredSize(LEGEND_TABLE_DIM);
    }

    JScrollPane getComponent() {
        return tablePane;
    }

    private void initColumnSizes() {
        final FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        for (int i = 0; i < model.getColumnCount(); i++) {
            final TableColumn column = legendTable.getColumnModel().getColumn(i);
            final int textWidth = (int) (font.getStringBounds(model.getColumnName(i), frc).getWidth());
            column.setPreferredWidth(textWidth);
        }

    }

    private class LegendTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        private final String[] columnNames = GuiConfig.getInstance().getPrefAggregatedLegendHeaders();

        private final Object[][] data =
            { { GuiConfig.getInstance().getPregAggregatedLegendHeaderColor(), "", "", "", "", "", "", "", "", "",
            "" } };


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
                setBackground(colors.get(column - 1));
            } else {
                final Component firstCell = table.getTableHeader().getDefaultRenderer()
                        .getTableCellRendererComponent(table, value, isSelected, hasFocus, 0, 0);
                firstCell.setBackground(Color.WHITE);
                firstCell.setFont(font);
                return firstCell;
            }
            setText((String) model.getValueAt(row, column));
            return this;
        }
    }
}
