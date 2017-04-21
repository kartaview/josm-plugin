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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PreferenceSettings;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.CacheConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
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
    private JCheckBox cbMouseHover;
    private JSpinner spMouseHoverDelay;
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
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefMapViewLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_MAP_VIEW);
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefPhotoZoomLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_PHOTO_ZOOM);
        final boolean enabled = !mapViewSettings.isManualSwitchFlag();
        spPhotoZoom = GuiBuilder.buildPositiveNumberSpinner(mapViewSettings.getPhotoZoom(),
                Config.getInstance().getMapPhotoZoom(), Config.getInstance().getPreferencesMaxZoom(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, false, enabled);
        add(spPhotoZoom, Constraints.SP_PHOTO_ZOOM);
        final ActionListener listener = event -> {
            final JCheckBox source = (JCheckBox) event.getSource();
            final boolean spPhotoZoomEnabled = !source.isSelected();
            spPhotoZoom.setEnabled(spPhotoZoomEnabled);
        };
        cbManualSwitch = GuiBuilder.buildCheckBox(GuiConfig.getInstance().getPrefManualSwitchLbl(), listener,
                Font.PLAIN, getBackground(), mapViewSettings.isManualSwitchFlag());
        add(cbManualSwitch, Constraints.CB_MANUAL_SWITCH);

    }

    private void createPhotoSettingsComponents(final PhotoSettings settings) {
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefImageLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_IMAGE);
        final boolean enabled = !settings.isMouseHoverFlag();
        cbHighQualityPhoto = GuiBuilder.buildCheckBox(GuiConfig.getInstance().getPrefImageHighQualityLbl(),
                new SelectionListener(), Font.PLAIN, settings.isHighQualityFlag(), enabled);
        add(cbHighQualityPhoto, Constraints.CB_HIGHG_QUALITY);

        cbDisplayTrack = GuiBuilder.buildCheckBox(GuiConfig.getInstance().getPrefDisplayTrackLbl(),
                new SelectionListener(), Font.PLAIN, settings.isDisplayTrackFlag(), enabled);
        add(cbDisplayTrack, Constraints.CB_TRACK_LOADING);

        final boolean selectedMouseHoverFlag =
                settings.isDisplayTrackFlag() || settings.isHighQualityFlag() ? false : settings.isMouseHoverFlag();
        cbMouseHover = GuiBuilder.buildCheckBox(GuiConfig.getInstance().getPrefMouseHoverLbl(), new SelectionListener(),
                Font.PLAIN, selectedMouseHoverFlag, selectedMouseHoverFlag);
        add(cbMouseHover, Constraints.CB_MOUSE_HOVER);

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefMouseHoverDelayLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_MOUSE_HOVER_DELAY);
        spMouseHoverDelay = GuiBuilder.buildPositiveNumberSpinner(settings.getMouseHoverDelay(),
                Config.getInstance().getMouseHoverMinDelay(), Config.getInstance().getMouseHoverMaxDelay(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, false, selectedMouseHoverFlag);
        add(spMouseHoverDelay, Constraints.SP_MOUSE_HOVER_DELAY);
    }

    private void createCacheSettingsComponents(final CacheSettings settings) {
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefCacheLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_CACHE);
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefMemoryLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_MEMORY_COUNT);
        spMemoryCount = GuiBuilder.buildPositiveNumberSpinner(settings.getMemoryCount(), null,
                CacheConfig.getInstance().getMaxMemoryCount(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, false,
                true);
        add(spMemoryCount, Constraints.SP_MEMORY_COUNT);

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefDiskLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_DISK_COUNT);
        spDiskCount = GuiBuilder.buildPositiveNumberSpinner(settings.getDiskCount(), null,
                CacheConfig.getInstance().getMaxDiskCount(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, false,
                true);
        add(spDiskCount, Constraints.SP_DISK_COUNT);

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefPrevNextLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_PREV_NEXT_COUNT);
        spPrevNextCount = GuiBuilder.buildPositiveNumberSpinner(settings.getPrevNextCount(), null,
                CacheConfig.getInstance().getMaxPrevNextCount(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, false,
                true);
        add(spPrevNextCount, Constraints.SP_PREV_NEXT_COUNT);

        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPrefNearbyLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_NEARBY_COUNT);
        spNearbyCount = GuiBuilder.buildPositiveNumberSpinner(settings.getNearbyCount(), null,
                CacheConfig.getInstance().getMaxNearbyCount(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, false,
                true);
        add(spNearbyCount, Constraints.SP_NEARBY_COUNT);
    }

    PreferenceSettings getSelectedSettings() {
        final MapViewSettings mapViewSettings =
                new MapViewSettings((int) spPhotoZoom.getValue(), cbManualSwitch.isSelected());
        final PhotoSettings photoSettings = new PhotoSettings(cbHighQualityPhoto.isSelected(),
                cbDisplayTrack.isSelected(), cbMouseHover.isSelected(), (int) spMouseHoverDelay.getValue());
        final CacheSettings cacheSettings = new CacheSettings((int) spMemoryCount.getValue(),
                (int) spDiskCount.getValue(), (int) spPrevNextCount.getValue(), (int) spNearbyCount.getValue());
        return new PreferenceSettings(mapViewSettings, photoSettings, cacheSettings);
    }


    private final class SelectionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            final JCheckBox cb = (JCheckBox) event.getSource();
            final String text = cb.getText();
            if (text.equals(GuiConfig.getInstance().getPrefDisplayTrackLbl())
                    || text.equals(GuiConfig.getInstance().getPrefImageHighQualityLbl())) {
                final boolean enabled =
                        !cb.isSelected() && (!cbDisplayTrack.isSelected() && !cbHighQualityPhoto.isSelected());
                cbMouseHover.setEnabled(enabled);
                spMouseHoverDelay.setEnabled(enabled);
            } else if (text.equals(GuiConfig.getInstance().getPrefMouseHoverLbl())) {
                final boolean enabled = !cb.isSelected();
                cbDisplayTrack.setEnabled(enabled);
                cbHighQualityPhoto.setEnabled(enabled);
            }
        }
    }
}