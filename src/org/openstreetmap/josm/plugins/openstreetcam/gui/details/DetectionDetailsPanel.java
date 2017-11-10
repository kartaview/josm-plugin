/*
 *  Copyright 2015 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.telenav.josm.common.gui.BasicInfoPanel;
import com.telenav.josm.common.gui.builder.LabelBuilder;


/**
 * Displays the information of a detection {@code Detection}
 *
 * @author ioanao
 * @version $Revision$
 */
public class DetectionDetailsPanel extends BasicInfoPanel<Detection> {

    private static final long serialVersionUID = 5842933383198993565L;

    private static final int SIGN_VALUE_EXTRA_WIDTH = 10;


    @Override
    public void createComponents(final Detection detection) {
        final int widthLbl = getMaxWidth(getFontMetrics(getFont().deriveFont(Font.BOLD)),
                GuiConfig.getInstance().getDetectedDetectionText(), GuiConfig.getInstance().getDetectionOnOsmText(),
                GuiConfig.getInstance().getDetectionModeText(),
                GuiConfig.getInstance().getDetectionValidationStatusText());

        if (detection != null) {
            addSign(GuiConfig.getInstance().getDetectedDetectionText(), detection.getSign().getName(), widthLbl);
            addDetectionInformation(GuiConfig.getInstance().getDetectionOnOsmText(), detection.getOsmComparison(),
                    widthLbl);
            addDetectionInformation(GuiConfig.getInstance().getDetectionModeText(), detection.getMode(), widthLbl);
            addDetectionInformation(GuiConfig.getInstance().getDetectionValidationStatusText(),
                    detection.getValidationStatus(), widthLbl);
        }

        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, pnlHeight));
    }

    private void addSign(final String label, final String sign, final int widthLbl) {
        final ImageIcon icon = DetectionIconFactory.INSTANCE.getIcon(sign, true);

        add(LabelBuilder.build(label, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, icon.getIconHeight())));

        final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(sign) + icon.getIconWidth()
                + SIGN_VALUE_EXTRA_WIDTH;
        add(LabelBuilder.build(sign, icon, Font.PLAIN,
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