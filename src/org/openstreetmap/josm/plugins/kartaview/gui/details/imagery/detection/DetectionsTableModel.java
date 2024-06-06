/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.detection;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;


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
    private static final int IDX_CONFIDENCE_CATEGORY = 5;
    private static final int IDX_DETECTION_CONFIDENCE = 6;
    private static final int IDX_CONFIDENCE_FACING = 7;
    private static final int IDX_CONFIDENCE_POSITIONING = 8;
    private static final int IDX_CONFIDENCE_KEY_POINTS = 9;
    private static final int IDX_CONFIDENCE_TRACKING = 10;
    private static final int IDX_CONFIDENCE_OCR = 11;
    private static final int IDX_VALIDATION_STATUS = 12;
    private static final int IDX_CREATION_DATE = 13;
    private static final int IDX_UPDATE_ON_DATE = 14;

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
                case IDX_DETECTION_CONFIDENCE:
                    value = detection.getConfidenceLevel().getDetectionConfidence();
                    break;
                case IDX_CONFIDENCE_CATEGORY:
                    value = detection.getConfidenceLevel().getConfidenceCategory();
                    break;
                case IDX_CONFIDENCE_FACING:
                    value = detection.getConfidenceLevel().getFacingConfidence();
                    break;
                case IDX_CONFIDENCE_KEY_POINTS:
                    value = detection.getConfidenceLevel().getKeyPointsConfidence();
                    break;
                case IDX_CONFIDENCE_OCR:
                    value = detection.getConfidenceLevel().getOcrConfidence();
                    break;
                case IDX_CONFIDENCE_POSITIONING:
                    value = detection.getConfidenceLevel().getPositioningConfidence();
                    break;
                case IDX_CONFIDENCE_TRACKING:
                    value = detection.getConfidenceLevel().getTrackingConfidence();
                    break;
                case IDX_FACING:
                    value = detection.getFacing();
                    break;
                case IDX_DISTANCE:
                    if (detection.getDistance() != null) {
                        value = detection.getDistance();
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