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
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo.PhotoDetailsDialog;
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

    /**
     * Updates the PhotoPanel elements according to the selectedPhoto and to the filters set in
     * the preference panel.
     *
     * It has to be called whenever there is a change in order to update the text associated with the image.
     */
    private void updatePhotoPanel() {
        final Photo selectedPhoto = DataSet.getInstance().getSelectedPhoto();
        final boolean preferencePanelValue =
                PreferenceManager.getInstance().loadPhotoSettings().isDisplayFrontFacingFlag();
        if (selectedPhoto != null && selectedPhoto.getProjectionType().equals(Projection.SPHERE)) {
            final SelectionHandler handler = new SelectionHandler();
            DataSet.getInstance().setFrontFacingDisplayed(preferencePanelValue);
            PhotoDetailsDialog.getInstance().updateSwitchImageFormatButton(true, preferencePanelValue);
            handler.handleDataSelection(selectedPhoto, DataSet.getInstance().getSelectedDetection(),
                    DataSet.getInstance().getSelectedCluster(), true);
        } else if (selectedPhoto != null && !selectedPhoto.getProjectionType().equals(Projection.SPHERE)) {
            PhotoDetailsDialog.getInstance().updateSwitchImageFormatButton(false, true);
        } else if (selectedPhoto == null) {
            PhotoDetailsDialog.getInstance().updateSwitchImageFormatButton(false, preferencePanelValue);
        }
    }
}