/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.preferences;


import static org.openstreetmap.josm.plugins.kartaview.gui.preferences.Constraints.TABLE_DISPLAY_COLOR_LEGEND;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;

import org.openstreetmap.josm.plugins.kartaview.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.kartaview.argument.CacheSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.ClusterSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.PreferenceSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.SequenceSettings;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.CacheConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;
import com.grab.josm.common.gui.builder.CheckBoxBuilder;
import com.grab.josm.common.gui.builder.LabelBuilder;
import com.grab.josm.common.gui.builder.TextComponentBuilder;


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
    private JCheckBox cbDataLoad;
    private JCheckBox cbHighQualityPhoto;
    private JCheckBox agDisplayDetection;
    private JRadioButton rbFrontFacingDisplay;
    private JCheckBox cbDisplayTags;
    private JCheckBox cbDisplayColorBased;
    private JCheckBox cbDisplayTrack;
    private JCheckBox cbMouseHover;
    private JSpinner spMouseHoverDelay;
    private JFormattedTextField txtAutoplayLength;
    private JSpinner spAutoplayDelay;
    private JSpinner spMemoryCount;
    private JSpinner spDiskCount;
    private JSpinner spPrevNextCount;
    private JSpinner spNearbyCount;


    PreferencePanel() {
        super(new GridBagLayout());
        final PreferenceSettings preferenceSettings = PreferenceManager.getInstance().loadPreferenceSettings();
        createMapViewSettings(preferenceSettings.getMapViewSettings());
        createPhotoSettingsComponents(preferenceSettings);
        createWrappedPhotoSettingsComponent(preferenceSettings);
        createClusterSettingsComponents(preferenceSettings.getClusterSettings());
        createTrackVisualizationSettings(preferenceSettings);
        createCacheSettingsComponents(preferenceSettings.getCacheSettings());
    }

    private void createMapViewSettings(final MapViewSettings mapViewSettings) {
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefMapViewLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_MAP_VIEW);
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefPhotoZoomLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_PHOTO_ZOOM);
        spPhotoZoom = TextComponentBuilder.buildPositiveNumberSpinner(mapViewSettings.getPhotoZoom(),
                Config.getInstance().getMapPhotoZoom(), Config.getInstance().getPreferencesMaxZoom(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, false, true);
        add(spPhotoZoom, Constraints.SP_PHOTO_ZOOM);

        cbDataLoad = CheckBoxBuilder.build(GuiConfig.getInstance().getPrefDataLoadLbl(), null, Font.PLAIN,
                getBackground(), mapViewSettings.isDataLoadFlag());
        add(cbDataLoad, Constraints.CB_DATA_LOAD);
    }

    private void createPhotoSettingsComponents(final PreferenceSettings settings) {
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefImageLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_IMAGE);
        final boolean enabled = !settings.getPhotoSettings().isMouseHoverFlag();
        cbHighQualityPhoto = CheckBoxBuilder.build(GuiConfig.getInstance().getPrefImageHighQualityLbl(),
                new SelectionListener(), Font.PLAIN, settings.getPhotoSettings().isHighQualityFlag(), enabled);
        add(cbHighQualityPhoto, Constraints.CB_HIGH_QUALITY);

        final boolean selectedMouseHoverFlag = !settings.getTrackSettings().isDisplayTrack()
                && !settings.getPhotoSettings().isHighQualityFlag() && settings.getPhotoSettings().isMouseHoverFlag();
        final boolean enabledMouseHoverFlag =
                !(settings.getTrackSettings().isDisplayTrack() || settings.getPhotoSettings().isHighQualityFlag());
        cbMouseHover = CheckBoxBuilder.build(GuiConfig.getInstance().getPrefMouseHoverLbl(), new SelectionListener(),
                Font.PLAIN, selectedMouseHoverFlag, enabledMouseHoverFlag);
        add(cbMouseHover, Constraints.CB_MOUSE_HOVER);

        add(LabelBuilder.build(GuiConfig.getInstance().getPrefMouseHoverDelayLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_MOUSE_HOVER_DELAY);
        spMouseHoverDelay =
                TextComponentBuilder.buildPositiveNumberSpinner(settings.getPhotoSettings().getMouseHoverDelay(),
                        Config.getInstance().getMouseHoverMinDelay(), Config.getInstance().getMouseHoverMaxDelay(),
                        Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, false, enabledMouseHoverFlag);
        add(spMouseHoverDelay, Constraints.SP_MOUSE_HOVER_DELAY);
    }

    private void createWrappedPhotoSettingsComponent(final PreferenceSettings settings) {
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefWrappedPhotoLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_360_IMAGE);
        final boolean displayFrontFacingEnabled = settings.getPhotoSettings().isDisplayFrontFacingFlag();
        rbFrontFacingDisplay = new JRadioButton(GuiConfig.getInstance().getPrefPhotoDisplayFrontFacingLbl(),
                displayFrontFacingEnabled);
        final JRadioButton rbWrappedDisplay =
                new JRadioButton(GuiConfig.getInstance().getPrefPhotoDisplayWrappedLbl(), !displayFrontFacingEnabled);
        final ButtonGroup photoOptionsGroup = new ButtonGroup();
        photoOptionsGroup.add(rbFrontFacingDisplay);
        photoOptionsGroup.add(rbWrappedDisplay);

        add(rbFrontFacingDisplay, Constraints.RB_FRONT_FACING);
        add(rbWrappedDisplay, Constraints.RB_WRAPPED);
    }

    private void createClusterSettingsComponents(final ClusterSettings settings) {
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefClusterLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_CLUSTER);
        agDisplayDetection = CheckBoxBuilder.build(GuiConfig.getInstance().getPrefClusterDisplayDetectionLbl(),
                new SelectionListener(), Font.PLAIN, settings.isDisplayDetectionLocations(), true);
        add(agDisplayDetection, Constraints.CB_DISPLAY_DETECTION);
        cbDisplayTags = CheckBoxBuilder.build(GuiConfig.getInstance().getPrefClusterDisplayTagsLbl(),
                new SelectionListener(), Font.PLAIN, settings.isDisplayTags(), true);
        add(cbDisplayTags, Constraints.CB_DISPLAY_TAGS);
        cbDisplayColorBased = CheckBoxBuilder.build(GuiConfig.getInstance().getPrefClusterDisplayColorCodedLbl(),
                new SelectionListener(), Font.PLAIN, settings.isDisplayColorCoded(), true);
        add(cbDisplayColorBased, Constraints.CB_DISPLAY_COLOR_CODED);
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefClusterLegendLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_DISPLAY_COLOR_LEGEND);
        final LegendTable legendTable = new LegendTable();
        add(legendTable.getComponent(), TABLE_DISPLAY_COLOR_LEGEND);
    }

    private void createTrackVisualizationSettings(final PreferenceSettings settings) {
        final boolean enabled = !settings.getPhotoSettings().isMouseHoverFlag();
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefTrackLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_TRACK);
        cbDisplayTrack = CheckBoxBuilder.build(GuiConfig.getInstance().getPrefDisplayTrackLbl(),
                new SelectionListener(), Font.PLAIN, settings.getTrackSettings().isDisplayTrack(), enabled);
        add(cbDisplayTrack, Constraints.CB_TRACK_LOADING);
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefAutoplayLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_AUTOPLAY);
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefAutoplayLengthLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_AUTOPLAY_LENGTH);
        txtAutoplayLength = TextComponentBuilder.buildIntegerTextField(
                settings.getTrackSettings().getAutoplaySettings().getLength(), 0, null, Font.PLAIN, Color.WHITE, true);
        add(txtAutoplayLength, Constraints.TXT_AUTOPLAY_LENGTH);
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefAutoplayDelayLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_AUTOPLAY_DELAY);
        final int autoplayDelay = settings.getTrackSettings().getAutoplaySettings().getDelay() != null && settings
                .getTrackSettings().getAutoplaySettings().getDelay() > Config.getInstance().getAutoplayMinDelay()
                ? settings.getTrackSettings().getAutoplaySettings().getDelay()
                : Config.getInstance().getAutoplayMinDelay();
        spAutoplayDelay = TextComponentBuilder.buildPositiveNumberSpinner(autoplayDelay,
                Config.getInstance().getAutoplayMinDelay(), Config.getInstance().getAutoplayMaxDelay(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, true, enabled);
        add(spAutoplayDelay, Constraints.SP_AUTOPLAY_DELAY);
    }

    private void createCacheSettingsComponents(final CacheSettings settings) {
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefCacheLbl(), Font.PLAIN,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_CACHE);
        add(LabelBuilder.build(GuiConfig.getInstance().getPrefMemoryLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_MEMORY_COUNT);
        spMemoryCount = TextComponentBuilder.buildPositiveNumberSpinner(settings.getMemoryCount(), null,
                CacheConfig.getInstance().getMaxMemoryCount(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, false,
                true);
        add(spMemoryCount, Constraints.SP_MEMORY_COUNT);

        add(LabelBuilder.build(GuiConfig.getInstance().getPrefDiskLbl(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT,
                SwingConstants.LEFT, SwingConstants.TOP), Constraints.LBL_DISK_COUNT);
        spDiskCount = TextComponentBuilder.buildPositiveNumberSpinner(settings.getDiskCount(), null,
                CacheConfig.getInstance().getMaxDiskCount(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, false,
                true);
        add(spDiskCount, Constraints.SP_DISK_COUNT);

        add(LabelBuilder.build(GuiConfig.getInstance().getPrefPrevNextLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_PREV_NEXT_COUNT);
        spPrevNextCount = TextComponentBuilder.buildPositiveNumberSpinner(settings.getPrevNextCount(), null,
                CacheConfig.getInstance().getMaxPrevNextCount(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, false,
                true);
        add(spPrevNextCount, Constraints.SP_PREV_NEXT_COUNT);

        add(LabelBuilder.build(GuiConfig.getInstance().getPrefNearbyLbl(), Font.PLAIN,
                        ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_NEARBY_COUNT);
        spNearbyCount = TextComponentBuilder.buildPositiveNumberSpinner(settings.getNearbyCount(), null,
                CacheConfig.getInstance().getMaxNearbyCount(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, false,
                true);
        add(spNearbyCount, Constraints.SP_NEARBY_COUNT);
    }

    PreferenceSettings getSelectedSettings() {
        final MapViewSettings mapViewSettings =
                new MapViewSettings((int) spPhotoZoom.getValue(), cbDataLoad.isSelected());
        final PhotoSettings photoSettings = new PhotoSettings(cbHighQualityPhoto.isSelected(),
                cbMouseHover.isSelected(), (int) spMouseHoverDelay.getValue(), rbFrontFacingDisplay.isSelected());
        final ClusterSettings clusterSettings = new ClusterSettings(agDisplayDetection.isSelected(),
                cbDisplayTags.isSelected(), cbDisplayColorBased.isSelected());

        final String lengthValue = txtAutoplayLength.getText().trim();
        final Integer length = lengthValue.isEmpty() ? null : Integer.parseInt(lengthValue);
        final SequenceSettings trackSettings = new SequenceSettings(cbDisplayTrack.isSelected(),
                new AutoplaySettings(length, (int) spAutoplayDelay.getValue()));
        final CacheSettings cacheSettings = new CacheSettings((int) spMemoryCount.getValue(),
                (int) spDiskCount.getValue(), (int) spPrevNextCount.getValue(), (int) spNearbyCount.getValue());
        return new PreferenceSettings(mapViewSettings, photoSettings, clusterSettings, trackSettings, cacheSettings);
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