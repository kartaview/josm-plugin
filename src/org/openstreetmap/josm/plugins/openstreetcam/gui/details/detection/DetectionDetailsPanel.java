/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Dimension;
import java.awt.Font;
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
                GuiConfig.getInstance().getDetectionComponentValueLbl(),
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
        if (detection.getComponentValue() != null && !detection.getComponentValue().isEmpty()) {
            addInformation(GuiConfig.getInstance().getDetectionComponentValueLbl(), detection.getComponentValue(),
                    widthLbl);
        }
        if (detection.getConfidenceLevel() != null && detection.getConfidenceLevel().isNotNull()) {
            if (detection.getConfidenceLevel().getDetectionConfidence() != null) {
                addInformation(GuiConfig.getInstance().getDetectionConfidenceLbl(), EntityFormatter
                        .formatDouble(detection.getConfidenceLevel().getDetectionConfidence(), false,
                                DecimalPattern.MEDIUM), widthLbl);
            }
            if (detection.getConfidenceLevel().getFacingConfidence() != null) {
                addInformation(GuiConfig.getInstance().getFacingConfidenceLbl(), EntityFormatter
                        .formatDouble(detection.getConfidenceLevel().getFacingConfidence(), false,
                                DecimalPattern.MEDIUM), widthLbl);
            }
            if (detection.getConfidenceLevel().getPositioningConfidence() != null) {
                addInformation(GuiConfig.getInstance().getPositioningConfidenceLbl(), EntityFormatter
                        .formatDouble(detection.getConfidenceLevel().getPositioningConfidence(), false,
                                DecimalPattern.MEDIUM), widthLbl);
            }
            if (detection.getConfidenceLevel().getKeyPointsConfidence() != null) {
                addInformation(GuiConfig.getInstance().getKeypointsConfidenceLbl(), EntityFormatter
                        .formatDouble(detection.getConfidenceLevel().getKeyPointsConfidence(), false,
                                DecimalPattern.MEDIUM), widthLbl);
            }
            if (detection.getConfidenceLevel().getTrackingConfidence() != null) {
                addInformation(GuiConfig.getInstance().getTrackingConfidenceLbl(), EntityFormatter
                        .formatDouble(detection.getConfidenceLevel().getTrackingConfidence(), false,
                                DecimalPattern.MEDIUM), widthLbl);
            }
            if (detection.getConfidenceLevel().getOcrConfidence() != null) {
                addInformation(GuiConfig.getInstance().getOcrConfidenceLbl(), EntityFormatter
                                .formatDouble(detection.getConfidenceLevel().getOcrConfidence(), false, DecimalPattern.MEDIUM),
                        widthLbl);
            }

        }
        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, pnlHeight));
    }
}