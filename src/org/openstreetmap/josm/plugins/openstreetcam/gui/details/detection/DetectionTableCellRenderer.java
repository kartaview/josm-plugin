/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionConfidenceLevel;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ValidationStatus;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.telenav.josm.common.formatter.DateFormatter;
import com.telenav.josm.common.formatter.DecimalPattern;
import com.telenav.josm.common.formatter.EntityFormatter;


/**
 * Defines a custom table cell renderer for {@code Detection} objects.
 *
 * @author nicoletav
 */
class DetectionTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    private static final String ZERO = "0";
    private static final String NULL_CONFIDENCE_LEVEL_TEXT = "-/-/-/-/-/-";
    private static final String DELIMITER = "/";
    private static final int IDX_ID = 0;
    private static final int IDX_TRACKING_ID = 2;
    private static final double APPROXIMATED_ZERO_DOUBLE = 0.0;
    private static final float APPROXIMATED_ZERO_FLOAT = 0.0f;
    private static final Color HEADER_GRAY = new Color(235, 237, 239);
    private static final MatteBorder HEADER_BORDER = new MatteBorder(0, 0, 1, 1, Color.gray);

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setFont(MainApplication.getMap().getFont().deriveFont(Font.PLAIN));
        String txt = "-";
        if (value != null) {
            final ArrayList<String> headerList =
                    new ArrayList<>(Arrays.asList(GuiConfig.getInstance().getClusterTableHeader()));
            if (value instanceof String && headerList.contains(value)) {
                txt = value.toString();
                setBackground(HEADER_GRAY);
                setBorder(HEADER_BORDER);
                setFont(MainApplication.getMap().getFont().deriveFont(Font.BOLD));
            } else if (value instanceof ValidationStatus || value instanceof EditStatus || column == IDX_TRACKING_ID ||
                    column == IDX_ID) {
                txt = value.toString();
            } else if (value instanceof DetectionConfidenceLevel) {
                if (((DetectionConfidenceLevel) value).isNotNull()) {
                    final DetectionConfidenceLevel confidenceLevel = (DetectionConfidenceLevel) value;
                    txt = createConfidenceText(confidenceLevel.getDetectionConfidence()) + DELIMITER +
                            createConfidenceText(confidenceLevel.getFacingConfidence()) + DELIMITER +
                            createConfidenceText(confidenceLevel.getPositioningConfidence()) + DELIMITER +
                            createConfidenceText(confidenceLevel.getKeyPointsConfidence()) + DELIMITER +
                            createConfidenceText(confidenceLevel.getTrackingConfidence()) + DELIMITER +
                            createConfidenceText(confidenceLevel.getOcrConfidence());
                } else {
                    txt = NULL_CONFIDENCE_LEVEL_TEXT;
                }
            } else if (value instanceof Long) {
                txt = DateFormatter.formatTimestamp((Long) value);
            } else if (value instanceof Double) {
                if ((double) value != APPROXIMATED_ZERO_DOUBLE) {
                    txt = EntityFormatter.formatDouble((double) value, false, DecimalPattern.SHORT);
                } else {
                    txt = ZERO;
                }
            } else if (value instanceof Float) {
                if ((float) value != APPROXIMATED_ZERO_FLOAT) {
                    final double convertedValue = (float) value;
                    txt = EntityFormatter.formatDouble(convertedValue, false, DecimalPattern.SHORT);
                } else {
                    txt = ZERO;
                }
            }
        }
        setText(txt);
        return this;
    }

    private String createConfidenceText(final Double confidence) {
        String confidenceText = "-";
        if (confidence != null) {
            if (confidence > APPROXIMATED_ZERO_DOUBLE) {
                confidenceText = EntityFormatter.formatDouble(confidence, false, DecimalPattern.MEDIUM);
            } else {
                confidenceText = ZERO;
            }
        }
        return confidenceText;
    }
}