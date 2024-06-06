/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import org.openstreetmap.josm.gui.MainApplication;


/**
 * Custom cell renderer for the headers of tables in the Edge detection panel.
 *
 * @author maria.mitisor
 */
public class TableHeaderCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1955687871723617683L;

    private static final Color HEADER_GRAY = new Color(235, 237, 239);
    private static final MatteBorder HEADER_BORDER = new MatteBorder(0, 0, 1, 1, Color.gray);

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        setBackground(HEADER_GRAY);
        setBorder(HEADER_BORDER);
        setFont(MainApplication.getMap().getFont().deriveFont(Font.BOLD));
        setText(value.toString());
        return this;
    }
}