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
import java.awt.FontMetrics;
import java.awt.Rectangle;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
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


    public DetectionDetailsPanel() {
        super();
    }


    @Override
    public void createComponents(final Detection detection) {
        final GuiConfig cnf = GuiConfig.getInstance();
        final FontMetrics fontMetrics = getFontMetrics(getFont().deriveFont(Font.BOLD));
        final int widthLbl = getMaxWidth(fontMetrics, "Detected:", "OSM:", "Status:");

        // addSign(detection.getSign(), widthLbl);
        addSimpleLine("Detected:", detection.getSign().getName(), widthLbl);
        addSimpleLine("OSM:", detection.getOsmComparison().toString().trim(), widthLbl);
        addSimpleLine("Status:", detection.getEditStatus().toString().trim(), widthLbl);
        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, pnlHeight));
    }

    /*
     * private void addSign(final Sign sign, final int widthLbl) { if (sign != null) {
     * add(LabelBuilder.build(GuiConfig.getInstance().getLblStatus(), Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT,
     * SwingConstants.LEFT, SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT))); final int
     * widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(type.toString());
     * add(LabelBuilder.build(type.toString(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
     * SwingConstants.TOP, new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT))); setPnlWidth(widthLbl +
     * widthVal); incrementPnlY(); } }
     */

    private void addSimpleLine(final String label, final String value, final int widthLbl) {
        add(LabelBuilder.build(label, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
        final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(value);
        add(LabelBuilder.build(value, Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT,
                SwingConstants.LEFT, SwingConstants.TOP, new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
        setPnlWidth(widthLbl + widthVal);
        incrementPnlY();
    }
}