/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Dimension;
import java.awt.Font;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ConfidenceLevel;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.telenav.josm.common.formatter.DateFormatter;
import com.telenav.josm.common.formatter.DecimalPattern;
import com.telenav.josm.common.formatter.EntityFormatter;


/**
 * Displays the information of a detection {@code Detection}
 *
 * @author ioanao
 * @version $Revision$
 */
class DetectionDetailsPanel extends BaseDetailsPanel<Detection> {

    private static final long serialVersionUID = 5842933383198993565L;

    @Override
    public void createComponents(final Detection detection) {
        final int widthLbl = getMaxWidth(getFontMetrics(getFont().deriveFont(Font.BOLD)),
                GuiConfig.getInstance().getDetectedDetectionText(), GuiConfig.getInstance().getDetectionOnOsmText(),
                GuiConfig.getInstance().getDetectionModeText(), GuiConfig.getInstance().getDetectionTaskStatusText(),
                GuiConfig.getInstance().getDetectionValidationStatusText(), GuiConfig.getInstance().getDetectionIdLbl(),
                GuiConfig.getInstance().getDetectionTrackingIdLbl(),
                GuiConfig.getInstance().getDetectionAutomaticOcrValueLbl(),
                GuiConfig.getInstance().getDetectionManualOcrValueLbl(),
                GuiConfig.getInstance().getDetectionConfidenceLbl(), GuiConfig.getInstance().getFacingConfidenceLbl(),
                GuiConfig.getInstance().getPositioningConfidenceLbl(),
                GuiConfig.getInstance().getKeypointsConfidenceLbl(), GuiConfig.getInstance().getTrackingConfidenceLbl(),
                GuiConfig.getInstance().getOcrConfidenceLbl());
        addSignType(GuiConfig.getInstance().getDetectedDetectionText(), detection.getSign(), widthLbl);
        addInformation(GuiConfig.getInstance().getDetectionOnOsmText(), detection.getOsmComparison(), widthLbl);
        addInformation(GuiConfig.getInstance().getDetectionModeText(), detection.getMode(), widthLbl);
        addInformation(GuiConfig.getInstance().getDetectionValidationStatusText(), detection.getValidationStatus(),
                widthLbl);
        if (detection.getOsmComparison() != null && (!detection.getOsmComparison().equals(OsmComparison.SAME))) {
            addInformation(GuiConfig.getInstance().getDetectionTaskStatusText(), detection.getEditStatus(), widthLbl);
        }
        if (detection.getCreationTimestamp() != null) {
            addInformation(GuiConfig.getInstance().getDetectionCreatedDate(),
                    DateFormatter.formatTimestamp(detection.getCreationTimestamp()), widthLbl);
        }
        if (detection.getLatestChangeTimestamp() != null) {
            addInformation(GuiConfig.getInstance().getDetectionUpdatedDate(),
                    DateFormatter.formatTimestamp(detection.getLatestChangeTimestamp()), widthLbl);
        }
        if (detection.getFacing() != null) {
            addInformation(GuiConfig.getInstance().getDetectionFacingText(),
                    EntityFormatter.formatDouble(detection.getFacing(), true, DecimalPattern.SHORT), widthLbl);
        }

        addInformation(GuiConfig.getInstance().getDetectionIdLbl(), detection.getId(), widthLbl);
        if (detection.getTrackingId() != null && !detection.getTrackingId().isEmpty()) {
            addInformation(GuiConfig.getInstance().getDetectionTrackingIdLbl(), detection.getTrackingId(), widthLbl);
        }
        if (detection.getAutomaticOcrValue() != null && !detection.getAutomaticOcrValue().isEmpty()) {
            addInformation(GuiConfig.getInstance().getDetectionAutomaticOcrValueLbl(), detection.getAutomaticOcrValue(),
                    widthLbl);
        }
        if (detection.getManualOcrValue() != null && !detection.getManualOcrValue().isEmpty()) {
            addInformation(GuiConfig.getInstance().getDetectionManualOcrValueLbl(), detection.getManualOcrValue(),
                    widthLbl);
        }
        addConfidenceLevel(detection.getConfidenceLevel(), widthLbl);
        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, pnlHeight));
    }

    private void addConfidenceLevel(final ConfidenceLevel confidenceLevel, final int widthLbl) {
        if (confidenceLevel != null && confidenceLevel.isNotNull()) {
            if (confidenceLevel.getDetectionConfidence() != null) {
                addInformation(GuiConfig.getInstance().getDetectionConfidenceLbl(), EntityFormatter.formatDouble(
                        confidenceLevel.getDetectionConfidence(), false, DecimalPattern.MEDIUM), widthLbl);
            }
            if (confidenceLevel.getFacingConfidence() != null) {
                addInformation(GuiConfig.getInstance().getFacingConfidenceLbl(), EntityFormatter
                        .formatDouble(confidenceLevel.getFacingConfidence(), false, DecimalPattern.MEDIUM), widthLbl);
            }
            if (confidenceLevel.getPositioningConfidence() != null) {
                addInformation(GuiConfig.getInstance().getPositioningConfidenceLbl(), EntityFormatter.formatDouble(
                        confidenceLevel.getPositioningConfidence(), false, DecimalPattern.MEDIUM), widthLbl);
            }
            if (confidenceLevel.getKeyPointsConfidence() != null) {
                addInformation(GuiConfig.getInstance().getKeypointsConfidenceLbl(), EntityFormatter.formatDouble(
                        confidenceLevel.getKeyPointsConfidence(), false, DecimalPattern.MEDIUM), widthLbl);
            }
            if (confidenceLevel.getTrackingConfidence() != null) {
                addInformation(GuiConfig.getInstance().getTrackingConfidenceLbl(), EntityFormatter
                        .formatDouble(confidenceLevel.getTrackingConfidence(), false, DecimalPattern.MEDIUM), widthLbl);
            }
            if (confidenceLevel.getOcrConfidence() != null) {
                addInformation(GuiConfig.getInstance().getOcrConfidenceLbl(),
                        EntityFormatter.formatDouble(confidenceLevel.getOcrConfidence(), false, DecimalPattern.MEDIUM),
                        widthLbl);
            }
        }
    }
}