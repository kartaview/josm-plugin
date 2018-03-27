/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.telenav.josm.common.formatter.DateFormatter;
import com.telenav.josm.common.gui.BasicInfoPanel;
import com.telenav.josm.common.gui.builder.LabelBuilder;


/**
 * Displays the information of a detection {@code Detection}
 *
 * @author ioanao
 * @version $Revision$
 */
class DetailsPanel extends BasicInfoPanel<Detection> {

    private static final long serialVersionUID = 5842933383198993565L;

    private static final int SIGN_VALUE_EXTRA_WIDTH = 10;


    @Override
    public void createComponents(final Detection detection) {
        final int widthLbl = getMaxWidth(getFontMetrics(getFont().deriveFont(Font.BOLD)),
                GuiConfig.getInstance().getDetectedDetectionText(), GuiConfig.getInstance().getDetectionOnOsmText(),
                GuiConfig.getInstance().getDetectionModeText(), GuiConfig.getInstance().getDetectionTaskStatusText(),
                GuiConfig.getInstance().getDetectionValidationStatusText());

        addSign(GuiConfig.getInstance().getDetectedDetectionText(), detection.getSign(), widthLbl);
        addDetectionInformation(GuiConfig.getInstance().getDetectionOnOsmText(), detection.getOsmComparison(),
                widthLbl);
        addDetectionInformation(GuiConfig.getInstance().getDetectionModeText(), detection.getMode(), widthLbl);
        addDetectionInformation(GuiConfig.getInstance().getDetectionValidationStatusText(),
                detection.getValidationStatus(), widthLbl);
        if (detection.getOsmComparison() != null && (detection.getOsmComparison().equals(OsmComparison.NEW)
                || (detection.getOsmComparison().equals(OsmComparison.CHANGED)))) {
            addDetectionInformation(GuiConfig.getInstance().getDetectionTaskStatusText(), detection.getEditStatus(),
                    widthLbl);
        }
        if (detection.getCreationTimestamp() != null) {
            addDetectionInformation(GuiConfig.getInstance().getDetectionCreatedDate(),
                    DateFormatter.formatTimestamp(detection.getCreationTimestamp()), widthLbl);
        }
        if (detection.getLatestChangeTimestamp() != null) {
            addDetectionInformation(GuiConfig.getInstance().getDetectionUpdatedDate(),
                    DateFormatter.formatTimestamp(detection.getLatestChangeTimestamp()), widthLbl);
        }

        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, pnlHeight));
    }

    private void addSign(final String label, final Sign sign, final int widthLbl) {
        final ImageIcon icon = DetectionIconFactory.INSTANCE.getIcon(sign, true);

        add(LabelBuilder.build(label, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, icon.getIconHeight())));

        final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(sign.getName())
                + icon.getIconWidth() + SIGN_VALUE_EXTRA_WIDTH;
        add(LabelBuilder.build(sign.getName(), icon, Font.PLAIN,
                new Rectangle(widthLbl, getPnlY(), widthVal, Math.max(LINE_HEIGHT, icon.getIconHeight()))));

        setPnlWidth(widthLbl + widthVal);
        setPnlY(Math.max(LINE_HEIGHT, icon.getIconHeight()));
    }

    private void addDetectionInformation(final String label, final Object value, final int widthLbl) {
        if (value != null) {
            add(LabelBuilder.build(label, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(value.toString());
            add(LabelBuilder.build(value.toString(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }
}