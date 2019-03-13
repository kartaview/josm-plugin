/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */

package org.openstreetmap.josm.plugins.openstreetcam.gui.preferences;


import javax.swing.table.AbstractTableModel;
import java.awt.Color;


public class LegendTableModel extends AbstractTableModel {

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

    private String[] columnNames =
            { "Confidence interval", "0-0.1", "0.1-0.2", "0.2-0.3", "0.3-0.4", "0.4-0.5", "0.5-0.6", "0.6-0.7",
                    "0.7-0.8", "0.8-0.9", "0.9-1" };

    private Object[][] data = { { "Color", "", "", "", "", "", "", "", "", "", } };


    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
