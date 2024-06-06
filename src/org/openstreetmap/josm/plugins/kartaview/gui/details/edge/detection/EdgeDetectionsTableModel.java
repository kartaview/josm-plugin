/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.edge.detection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.swing.table.AbstractTableModel;
import org.openstreetmap.josm.plugins.kartaview.entity.Attribute;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import com.grab.josm.common.formatter.DateFormatter;
import com.grab.josm.common.formatter.DecimalPattern;
import com.grab.josm.common.formatter.EntityFormatter;


/**
 * Defines a custom model for the {@code EdgeDetection} objects.
 *
 * @author maria.mitisor
 */
class EdgeDetectionsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -7958153336003891606L;

    private static final int IDX_ID = 0;
    private static final int IDX_GENERATED_AT = 1;
    private static final int IDX_SAVED_AT = 2;
    private static final int IDX_DEVICE = 3;
    private static final int IDX_CONFIDENCE_LEVEL = 4;
    private static final int IDX_FACING = 5;
    private static final int IDX_TRACKING_ID = 6;
    private static final int IDX_EXTENDED_ID = 7;
    private static final int IDX_CUSTOM_ATTRIBUTES = 8;

    private static final String EMPTY_STRING = "";
    private static final String SEMICOLON = ";";
    private static final String DASH = "-";

    private List<EdgeDetection> data;


    EdgeDetectionsTableModel(final List<EdgeDetection> data) {
        super();
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return Objects.nonNull(data) ? data.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return GuiConfig.getInstance().getEdgeDetectionsTableHeader().length;
    }

    @Override
    public String getColumnName(final int colIdx) {
        return GuiConfig.getInstance().getEdgeDetectionsTableHeader()[colIdx];
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Object value = null;
        if (rowIndex > -1 && rowIndex < data.size()) {
            final EdgeDetection edgeDetection = data.get(rowIndex);
            switch (columnIndex) {
                case IDX_ID:
                    value = edgeDetection.getId().toString();
                    break;
                case IDX_GENERATED_AT:
                    value = DateFormatter.formatMillisecondTimestamp(edgeDetection.getGenerationTimestamp());
                    value = value.equals(EMPTY_STRING) ? DASH : value;
                    break;
                case IDX_SAVED_AT:
                    value = DateFormatter.formatTimestamp(edgeDetection.getCreationTimestamp());
                    value = value.equals(EMPTY_STRING) ? DASH : value;
                    break;
                case IDX_DEVICE:
                    value = edgeDetection.getPhotoMetadata().getDevice().toString();
                    break;
                case IDX_CONFIDENCE_LEVEL:
                    value = edgeDetection.getConfidenceLevel().toString();
                    break;
                case IDX_FACING:
                    value = EntityFormatter.formatDouble(edgeDetection.getFacing(), false, DecimalPattern.MEDIUM);
                    break;
                case IDX_TRACKING_ID:
                    value = Objects.nonNull(edgeDetection.getTrackingId()) ? edgeDetection.getTrackingId() : DASH;
                    break;
                case IDX_EXTENDED_ID:
                    value = Objects.nonNull(edgeDetection.getExtendedId()) ? edgeDetection.getExtendedId() : DASH;
                    break;
                case IDX_CUSTOM_ATTRIBUTES:
                    value = formatCustomAttributes(edgeDetection.getCustomAttributes());
                    break;
                default:
                    value = rowIndex;
                    break;
            }
        }
        return value;
    }

    private String formatCustomAttributes(Collection<Attribute> attributes) {
        String result = DASH;
        if (Objects.nonNull(attributes) && !attributes.isEmpty()) {
            result = EMPTY_STRING;
            for (final Attribute attribute : attributes) {
                result += attribute.toString() + SEMICOLON;
            }
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    void setData(final List<EdgeDetection> data) {
        this.data = Objects.nonNull(data) ? new ArrayList<>(data) : null;
    }

    List<EdgeDetection> getData() {
        return data;
    }
}