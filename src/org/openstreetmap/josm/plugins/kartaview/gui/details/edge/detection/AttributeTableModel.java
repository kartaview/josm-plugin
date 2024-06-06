/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.edge.detection;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openstreetmap.josm.plugins.kartaview.entity.Attribute;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;


/**
 * Defines a custom model for the {@code Attribute} objects.
 *
 * @author maria.mitisor
 */
class AttributeTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1897706173209857701L;
    private static final int IDX_KEY = 0;
    private static final int IDX_VALUE = 1;

    private List<Attribute> data;

    public AttributeTableModel(final List<Attribute> data) {
        super();
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return GuiConfig.getInstance().getAttributeTableHeader().length;
    }

    @Override
    public String getColumnName(final int colIdx) {
        return GuiConfig.getInstance().getAttributeTableHeader()[colIdx];
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Object value = null;
        if (rowIndex > -1 && rowIndex < data.size()) {
            final Attribute attribute = data.get(rowIndex);
            switch (columnIndex) {
                case IDX_KEY:
                    value = attribute.getKey();
                    break;
                case IDX_VALUE:
                    value = attribute.getValue();
                    break;
                default:
                    value = rowIndex;
                    break;
            }
        }
        return value;
    }

    void setData(final List<Attribute> data) {
        this.data = data != null ? new ArrayList<>(data) : null;
    }

    List<Attribute> getData() {
        return data;
    }
}