/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;

/**
 * Defines a custom model for the {@code Detection} objects.
 *
 * @author nicoletav
 */
class DetectionsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private static final int IDX_ID = 0;
    private static final int IDX_DISTANCE = 1;
    private static final int IDX_TRACKING_ID = 2;
    private static final int IDX_FACING = 3;
    private static final int IDX_ORIENTATION = 4;
    private static final int IDX_CONFIDENCE_LEVEL = 5;
    private static final int IDX_VALIDATION_STATUS = 6;
    private static final int IDX_CREATION_DATE = 7;
    private static final int IDX_UPDATE_ON_DATE = 8;
    private static final int MILLIMETER_METER_SCALE = 1000;

    private transient List<Detection> data;

    /**
     * @param data detections list corresponding to the selected cluster
     */
    public DetectionsTableModel(final List<Detection> data) {
        super();
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return GuiConfig.getInstance().getClusterTableHeader().length;
    }

    @Override
    public String getColumnName(final int colIdx) {
        return GuiConfig.getInstance().getClusterTableHeader()[colIdx];
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Object value = null;
        if (rowIndex > -1 && rowIndex < data.size()) {
            final Detection detection = data.get(rowIndex);
            switch (columnIndex) {
                case IDX_ID:
                    value = detection.getId();
                    break;
                case IDX_CREATION_DATE:
                    value = detection.getCreationTimestamp();
                    break;
                case IDX_UPDATE_ON_DATE:
                    value = detection.getLatestChangeTimestamp();
                    break;
                case IDX_VALIDATION_STATUS:
                    value = detection.getValidationStatus();
                    break;
                case IDX_CONFIDENCE_LEVEL:
                    value = detection.getConfidenceLevel();
                    break;
                case IDX_FACING:
                    value = detection.getFacing();
                    break;
                case IDX_DISTANCE:
                    if (detection.getDistance() != null) {
                        value = detection.getDistance() / MILLIMETER_METER_SCALE;
                    }
                    break;
                case IDX_ORIENTATION:
                    value = detection.getOrientation();
                    break;
                case IDX_TRACKING_ID:
                    value = detection.getTrackingId();
                    break;
                default:
                    value = rowIndex;
                    break;
            }
        }
        return value;
    }

    void setData(final List<Detection> data) {
        this.data = data != null ? new ArrayList<>(data) : null;
    }

    List<Detection> getData() {
        return data;
    }
}