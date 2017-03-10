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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PreferenceSettings;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.CacheConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Defines the UI components for the preference settings.
 *
 * @author beataj
 * @version $Revision$
 */
class PreferencePanel extends JPanel {

    private static final long serialVersionUID = -8056772228573127238L;

    /* photo preference settings */
    private JSpinner spPhotoZoom;
    private JCheckBox cbManualSwitch;
    private JCheckBox cbHighQualityPhoto;
    private JCheckBox cbDisplayTrack;
    private JSpinner spMemoryCount;
    private JSpinner spDiskCount;
    private JSpinner spPrevNextCount;
    private JSpinner spNearbyCount;


    PreferencePanel() {
        super(new GridBagLayout());
        final PreferenceSettings preferenceSettings = PreferenceManager.getInstance().loadPreferenceSettings();
        createMapViewSettings(preferenceSettings.getMapViewSettings());
        createPhotoSettingsComponents(preferenceSettings.getPhotoSettings());
        createCacheSettingsComponents(preferenceSettings.getCacheSettings());
    }

    private void createMapViewSettings(final MapViewSettings mapViewSettings) {
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefMapViewLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_MAP_VIEW);
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefPhotoZoomLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_PHOTO_ZOOM);
        spPhotoZoom = GuiBuilder.buildPositiveNumberSpinner(mapViewSettings.getPhotoZoom(),
                Config.getInstance().getMaxZoom(), false, getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT);
        ((SpinnerNumberModel) spPhotoZoom.getModel()).setMinimum(Config.getInstance().getPhotoZoom());
        add(spPhotoZoom, Constraints.SP_PHOTO_ZOOM);
        cbManualSwitch = GuiBuilder.buildCheckBox(GuiConfig.getInstance().getPrefManualSwitchLbl(),
                new JCheckBox().getFont().deriveFont(Font.PLAIN), mapViewSettings.isManualSwitchFlag(),
                getBackground());
        add(cbManualSwitch, Constraints.CB_MANUAL_SWITCH);
    }

    private void createPhotoSettingsComponents(final PhotoSettings settings) {
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefImageLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_IMAGE);
        cbHighQualityPhoto = GuiBuilder.buildCheckBox(GuiConfig.getInstance().getPrefImageHighQualityLbl(),
                new JCheckBox().getFont().deriveFont(Font.PLAIN), settings.isHighQualityFlag(), getBackground());
        add(cbHighQualityPhoto, Constraints.CB_HIGHG_QUALITY);

        cbDisplayTrack = GuiBuilder.buildCheckBox(GuiConfig.getInstance().getPrefDisplayTrackLbl(),
                new JCheckBox().getFont().deriveFont(Font.PLAIN), settings.isDisplayTrackFlag(), getBackground());
        add(cbDisplayTrack, Constraints.CB_TRACK_LOADING);
    }

    private void createCacheSettingsComponents(final CacheSettings settings) {
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefCacheLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_CACHE);
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefMemoryLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_MEMORY_COUNT);
        spMemoryCount = GuiBuilder.buildPositiveNumberSpinner(settings.getMemoryCount(),
                CacheConfig.getInstance().getMaxMemoryCount(), false, getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT);
        add(spMemoryCount, Constraints.SP_MEMORY_COUNT);

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefDiskLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_DISK_COUNT);
        spDiskCount = GuiBuilder.buildPositiveNumberSpinner(settings.getDiskCount(),
                CacheConfig.getInstance().getMaxDiskCount(), false, getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT);
        add(spDiskCount, Constraints.SP_DISK_COUNT);

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefPrevNextLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_PREV_NEXT_COUNT);
        spPrevNextCount = GuiBuilder.buildPositiveNumberSpinner(settings.getPrevNextCount(),
                CacheConfig.getInstance().getMaxPrevNextCount(), false, getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT);
        add(spPrevNextCount, Constraints.SP_PREV_NEXT_COUNT);

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefNearbyLbl(), getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_NEARBY_COUNT);
        spNearbyCount = GuiBuilder.buildPositiveNumberSpinner(settings.getNearbyCount(),
                CacheConfig.getInstance().getMaxNearbyCount(), false, getFont().deriveFont(Font.PLAIN),
                ComponentOrientation.LEFT_TO_RIGHT);
        add(spNearbyCount, Constraints.SP_NEARBY_COUNT);
    }

    PreferenceSettings getSelectedSettings() {
        final MapViewSettings mapViewSettings =
                new MapViewSettings((int) spPhotoZoom.getValue(), cbManualSwitch.isSelected());
        final PhotoSettings photoSettings =
                new PhotoSettings(cbHighQualityPhoto.isSelected(), cbDisplayTrack.isSelected());
        final CacheSettings cacheSettings = new CacheSettings((int) spMemoryCount.getValue(),
                (int) spDiskCount.getValue(), (int) spPrevNextCount.getValue(), (int) spNearbyCount.getValue());
        return new PreferenceSettings(mapViewSettings, photoSettings, cacheSettings);
    }
}