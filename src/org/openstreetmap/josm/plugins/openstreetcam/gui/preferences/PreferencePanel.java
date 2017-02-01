/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license. 
 *  https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved             
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.preferences;


import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * 
 * @author beataj
 * @version $Revision$
 */
class PreferencePanel extends JPanel {

    private static final long serialVersionUID = -8056772228573127238L;
    /* photo preference settings */
    private JCheckBox cbHighQualityPhoto;


    PreferencePanel() {
        super(new GridBagLayout());
        createComponents();
    }

    private void createComponents() {
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getImagePrefLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_IMAGE);
        boolean selected = PreferenceManager.getInstance().loadHighQualityPhotoFlag();
        cbHighQualityPhoto = GuiBuilder.buildCheckBox("display high quality image",
                new JCheckBox().getFont().deriveFont(Font.PLAIN), null, selected, false);
        cbHighQualityPhoto.setBackground(getBackground());
        add(cbHighQualityPhoto, Constraints.CB_HIGHG_QUALITY);
    }

    boolean getHighQualityFlag() {
        return cbHighQualityPhoto.isSelected();
    }
}