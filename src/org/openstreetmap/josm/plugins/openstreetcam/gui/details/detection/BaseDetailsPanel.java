/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;
import org.openstreetmap.josm.tools.Platform;
import com.telenav.josm.common.gui.BasicInfoPanel;
import com.telenav.josm.common.gui.builder.LabelBuilder;
import com.telenav.josm.common.gui.builder.TextComponentBuilder;


/**
 * Defines common functionality used by the {@code Detection} and {@code Cluster} detail panels.
 *
 * @author beataj
 * @version $Revision$
 */
abstract class BaseDetailsPanel<T> extends BasicInfoPanel<T> {

    private static final long serialVersionUID = -5651455515267347282L;
    private static final int SIGN_VALUE_EXTRA_WIDTH = 10;
    private static final int LINE_HEIGHT = 25;
    private static final int EXTRA_Y = 4;

    void addSignType(final String label, final Sign sign, final int widthLbl) {
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

    void addInformation(final String label, final Object value, final int widthLbl) {
        if (value != null) {
            add(LabelBuilder.build(label, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final Platform platform = Platform.determinePlatform();
            int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(value.toString());
            widthVal = platform.equals(Platform.WINDOWS) ? widthVal * 2 : widthVal;
            final int yVal = platform.equals(Platform.OSX) ? getPnlY() + EXTRA_Y : getPnlY();
            add(TextComponentBuilder.buildTextArea(value.toString(), Font.PLAIN, Color.white, false,
                    new Rectangle(widthLbl, yVal, widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }
}