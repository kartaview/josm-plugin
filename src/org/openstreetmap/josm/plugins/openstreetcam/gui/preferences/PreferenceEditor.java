/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.preferences;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.preferences.DefaultTabPreferenceSetting;
import org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PreferenceSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Projection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.handler.SelectionHandler;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


/**
 * Defines the preference editor settings.
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
    public void addGui(final PreferenceTabbedPane pnlParent) {
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(pnlPreference, BorderLayout.NORTH);
        createPreferenceTabWithScrollPane(pnlParent, mainPanel);
    }

    @Override
    public boolean ok() {
        final PreferenceSettings settings = pnlPreference.getSelectedSettings();
        final PreferenceSettings oldPreferenceSettings = PreferenceManager.getInstance().loadPreferenceSettings();
        PreferenceManager.getInstance().savePreferenceSettings(settings);
        if (oldPreferenceSettings.getPhotoSettings().isDisplayFrontFacingFlag() != settings.getPhotoSettings()
                .isDisplayFrontFacingFlag()) {
            updatePhotoPanel();
        }
        return !settings.getCacheSettings().equals(oldPreferenceSettings.getCacheSettings());
    }

    private void updatePhotoPanel() {
        final Photo selectedPhoto = DataSet.getInstance().getSelectedPhoto();
        if (selectedPhoto != null && selectedPhoto.getProjectionType().equals(Projection.SPHERE)) {
            final SelectionHandler handler = new SelectionHandler();
            DataSet.getInstance().setFrontFacingDisplayed(
                    PreferenceManager.getInstance().loadPhotoSettings().isDisplayFrontFacingFlag());
            handler.handleDataSelection(selectedPhoto, DataSet.getInstance().getSelectedDetection(),
                    DataSet.getInstance().getSelectedCluster(), true);
        }
    }
}