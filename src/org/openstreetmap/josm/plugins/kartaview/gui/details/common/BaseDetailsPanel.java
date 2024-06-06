/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.common;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.kartaview.entity.OcrValue;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.gui.DetectionIconFactory;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.tools.Platform;
import com.grab.josm.common.gui.BasicInfoPanel;
import com.grab.josm.common.gui.builder.LabelBuilder;
import com.grab.josm.common.gui.builder.TextComponentBuilder;


/**
 * Defines common functionality used by the {@code Detection}, {@code EdgeDetection} and {@code Cluster} detail panels.
 *
 * @author beataj
 * @version $Revision$
 */
public abstract class BaseDetailsPanel<T> extends BasicInfoPanel<T> {

    private static final long serialVersionUID = -5651455515267347282L;

    private static final int SIGN_VALUE_EXTRA_WIDTH = 10;
    private static final int LINE_HEIGHT = 25;
    private static final int EXTRA_Y = 4;
    private static final int EXTRA_Y_SIGN_ICON = 15;
    protected static final String EMPTY_STRING = "";

    protected void addSignType(final String label, final Sign sign, final int widthLbl) {
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

    protected void addSignIcon(final String label, final Sign sign, final int widthLbl) {
        final ImageIcon icon = DetectionIconFactory.INSTANCE.getIcon(sign, false);

        add(LabelBuilder.build(label, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, icon.getIconHeight())));

        add(LabelBuilder.build(EMPTY_STRING, icon, Font.PLAIN, new Rectangle(widthLbl, getPnlY(), icon.getIconWidth(),
                Math.max(LINE_HEIGHT, icon.getIconHeight()) + EXTRA_Y_SIGN_ICON)));

        setPnlWidth(widthLbl + icon.getIconWidth());
        setPnlY(getPnlY() + Math.max(LINE_HEIGHT, icon.getIconHeight()) + EXTRA_Y_SIGN_ICON);
    }

    protected void addInformation(final String label, final Object value, final int widthLbl) {
        addInformation(label, value, RECT_X, widthLbl);
    }

    protected void addInformation(final String label, final Object value, int xLbl, final int widthLbl) {
        if (Objects.nonNull(value)) {
            add(LabelBuilder.build(label, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(xLbl, getPnlY(), widthLbl, LINE_HEIGHT)));
            final Platform platform = Platform.determinePlatform();
            int widthVal = xLbl + getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(value.toString());
            widthVal = platform.equals(Platform.WINDOWS) ? widthVal * 2 : widthVal;
            final int yVal = platform.equals(Platform.OSX) ? getPnlY() + EXTRA_Y : getPnlY();
            add(TextComponentBuilder.buildTextArea(value.toString(), Font.PLAIN, Color.white, false,
                    new Rectangle(widthLbl, yVal, widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    protected void addLabel(final String label, final int widthLbl) {
        add(LabelBuilder.build(label, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
        setPnlWidth(widthLbl);
        incrementPnlY();
    }

    protected void addOcrValue(final OcrValue ocrValue, final int widthLbl) {
        if (Objects.nonNull(ocrValue) && ocrValue.isNotNull()) {
            if (Objects.nonNull(ocrValue.getText()) && !ocrValue.getText().isEmpty()) {
                addInformation(GuiConfig.getInstance().getDetectionOcrValueLbl(), ocrValue.formatOCRValueText(),
                        widthLbl);
            }
            if (Objects.nonNull(ocrValue.getLanguage()) && !ocrValue.getLanguage().isEmpty()) {
                addInformation(GuiConfig.getInstance().getDetectionOcrValueLanguageLbl(), ocrValue.getLanguage(),
                        widthLbl);
            }
            if (Objects.nonNull(ocrValue.getCharacterSet()) && !ocrValue.getCharacterSet().isEmpty()) {
                addInformation(GuiConfig.getInstance().getDetectionOcrValueCharacterSetLbl(),
                        ocrValue.getCharacterSet(), widthLbl);
            }
        }
    }
}