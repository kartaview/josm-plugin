/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license. 
 *  https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved             
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.preferences;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.preferences.DefaultTabPreferenceSetting;
import org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


/**
 * 
 * @author beataj
 * @version $Revision$
 */
public class PreferenceEditor extends DefaultTabPreferenceSetting {

    private final PreferencePanel pnlPreference;


    public PreferenceEditor() {
        super(IconConfig.getInstance().getPluginIconName(), GuiConfig.getInstance().getPluginLongName(),
                GuiConfig.getInstance().getPluginTlt());
        pnlPreference = new PreferencePanel();
    }

    
    @Override
    public void addGui(PreferenceTabbedPane pnlParent) {
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(pnlPreference, BorderLayout.NORTH);
        createPreferenceTabWithScrollPane(pnlParent, mainPanel);
    }

    @Override
    public boolean ok() {
        boolean highQualityFlag = pnlPreference.getHighQualityFlag();
        PreferenceManager.getInstance().saveHighQualityPhotoFlag(highQualityFlag);
        boolean displayTrackFlag = pnlPreference.getDisplayTrackFlag();
        PreferenceManager.getInstance().saveDisplayTrackFlag(displayTrackFlag);
        return false;
    }
}