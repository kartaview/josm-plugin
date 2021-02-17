/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.cnf;

import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.tools.ImageProvider;
import com.grab.josm.common.cnf.BaseConfig;


/**
 * Loads icons and icon names.
 *
 * @author Beata
 * @version $Revision$
 */
public final class IconConfig extends BaseConfig {

    private static final IconConfig INSTANCE = new IconConfig();
    private static final String CONFIG_FILE = "kartaview_icon.properties";
    private static final int CLUSTER_BACKGROUND_SIZE = 49;
    private static final int CLUSTER_BACKGROUND_SELECTED_SIZE = 60;
    private static final int SELECTED_ICON_SIZE = 28;
    private static final int UNSELECTED_ICON_SIZE = 14;
    private static final int SELECTED_WRAPPED_ICON_SIZE = 37;
    private static final int UNSELECTED_WRAPPED_ICON_SIZE = 20;


    private final String pluginIconName;
    private final String dialogShortcutName;
    private final String detectionDialogShortcutName;
    private final String deleteIconName;
    private final String layerIconName;
    private final String downloadIconName;
    private final String saveIconName;
    private final Icon layerIcon;
    private final Icon layerIconFiltered;
    private final ImageIcon photoIcon;
    private final ImageIcon photoSelectedIcon;
    private final ImageIcon photoNoHeadingIcon;
    private final ImageIcon photoNoHeadingSelectedIcon;
    private final ImageIcon photoSelectedIconPurple;
    private final ImageIcon photoUnselectedIconPurple;
    private final ImageIcon photoNoHeadingSelectedIconPurple;
    private final ImageIcon photoNoHeadingUnselectedIconPurple;
    private final ImageIcon photoWrappedSelectedIcon;
    private final ImageIcon photoWrappedUnselectedIcon;
    private final ImageIcon photoWrappedNoHeadingSelectedIcon;
    private final ImageIcon photoWrappedNoHeadingUnselectedIcon;
    private final ImageIcon photoWrappedSelectedIconPurple;
    private final ImageIcon photoWrappedUnselectedIconPurple;
    private final ImageIcon photoWrappedNoHeadingSelectedIconPurple;
    private final ImageIcon photoWrappedNoHeadingUnselectedIconPurple;
    private final ImageIcon filterIcon;
    private final String filterIconName;
    private final Icon previousIcon;
    private final Icon nextIcon;
    private final Icon playIcon;
    private final Icon stopIcon;
    private final String feedbackIconName;
    private final Icon locationIcon;
    private final Icon wrappedImageFormatIcon;
    private final Icon frontFacingImageFormatIcon;
    private final Icon webPageIcon;
    private final Icon closestImageIcon;
    private final Icon matchedWayIcon;
    private final Icon warningIcon;
    private final Icon warningImageFormatIcon;
    private final Icon manualSwitchSegmentIcon;
    private final Icon manualSwitchImageIcon;
    private final Icon downloadIcon;
    private final ImageIcon clusterBackgroundIconColorless;
    private final ImageIcon clusterBackgroundSelectedIconColorless;
    private final ImageIcon clusterBackgroundIconColor1;
    private final ImageIcon clusterBackgroundSelectedIconColor1;
    private final ImageIcon clusterBackgroundIconColor2;
    private final ImageIcon clusterBackgroundSelectedIconColor2;
    private final ImageIcon clusterBackgroundIconColor3;
    private final ImageIcon clusterBackgroundSelectedIconColor3;
    private final ImageIcon clusterBackgroundIconColor4;
    private final ImageIcon clusterBackgroundSelectedIconColor4;
    private final ImageIcon clusterBackgroundIconColor5;
    private final ImageIcon clusterBackgroundSelectedIconColor5;
    private final ImageIcon clusterBackgroundIconColor6;
    private final ImageIcon clusterBackgroundSelectedIconColor6;
    private final ImageIcon clusterBackgroundIconColor7;
    private final ImageIcon clusterBackgroundSelectedIconColor7;
    private final ImageIcon clusterBackgroundIconColor8;
    private final ImageIcon clusterBackgroundSelectedIconColor8;
    private final ImageIcon clusterBackgroundIconColor9;
    private final ImageIcon clusterBackgroundSelectedIconColor9;
    private final ImageIcon clusterBackgroundIconColor10;
    private final ImageIcon clusterBackgroundSelectedIconColor10;
    private final String preferenceIconName;
    private final String detectionIconsLongPath;
    private final String detectionIconsPath;
    private final Icon mappedIcon;
    private final Icon badDetectionIcon;
    private final Icon otherIcon;

    private IconConfig() {
        super(CONFIG_FILE);

        pluginIconName = readProperty("plugin.icon");
        dialogShortcutName = readProperty("dialog.shortcut");
        detectionDialogShortcutName = readProperty("dialog.detection.shortcut");
        deleteIconName = readProperty("delete.icon");
        layerIconName = readProperty("layer.icon");
        downloadIconName = readProperty("download.icon");
        saveIconName = readProperty("save.icon");
        layerIcon = ImageProvider.get(layerIconName);
        layerIconFiltered = ImageProvider.get(readProperty("layer.icon.filtered"));
        photoIcon = getUnselectedPhotoIcon("photo.icon");
        photoSelectedIcon = getSelectedPhotoIcon("photo.icon");
        photoNoHeadingIcon = getUnselectedPhotoIcon("photo.noHeading.icon");
        photoNoHeadingSelectedIcon = getSelectedPhotoIcon("photo.noHeading.icon");
        photoSelectedIconPurple = getSelectedPhotoIcon("photo.icon.purple");
        photoUnselectedIconPurple = getUnselectedPhotoIcon("photo.icon.purple");
        photoNoHeadingSelectedIconPurple = getSelectedPhotoIcon("photo.noHeading.icon.purple");
        photoNoHeadingUnselectedIconPurple = getUnselectedPhotoIcon("photo.noHeading.icon.purple");
        photoWrappedSelectedIcon = getSelectedWrappedPhotoIcon("photo.360.icon");
        photoWrappedUnselectedIcon = getUnselectedWrappedPhotoIcon("photo.360.icon");
        photoWrappedNoHeadingSelectedIcon = getSelectedWrappedPhotoIcon("photo.360.noHeading.icon");
        photoWrappedNoHeadingUnselectedIcon = getUnselectedWrappedPhotoIcon("photo.360.noHeading.icon");
        photoWrappedSelectedIconPurple = getSelectedWrappedPhotoIcon("photo.360.icon.purple");
        photoWrappedUnselectedIconPurple = getUnselectedWrappedPhotoIcon("photo.360.icon.purple");
        photoWrappedNoHeadingSelectedIconPurple = getSelectedWrappedPhotoIcon("photo.360.noHeading.icon.purple");
        photoWrappedNoHeadingUnselectedIconPurple = getUnselectedWrappedPhotoIcon("photo.360.noHeading.icon.purple");
        filterIconName = readProperty("filter.icon");
        filterIcon = ImageProvider.get(filterIconName);
        previousIcon = ImageProvider.get(readProperty("previous.icon"));
        nextIcon = ImageProvider.get(readProperty("next.icon"));
        playIcon = ImageProvider.get(readProperty("play.icon"));
        stopIcon = ImageProvider.get(readProperty("stop.icon"));
        locationIcon = ImageProvider.get(readProperty("location.icon"));
        wrappedImageFormatIcon = ImageProvider.get(readProperty("image.format.wrapped.icon"));
        frontFacingImageFormatIcon = ImageProvider.get(readProperty("image.format.cropped.icon"));
        feedbackIconName = readProperty("feedback.icon");
        webPageIcon = ImageProvider.get(readProperty("webPage.icon"));
        closestImageIcon = ImageProvider.get(readProperty("closestImage.icon"));
        matchedWayIcon = ImageProvider.get(readProperty("matchedWay.icon"));
        warningIcon = ImageProvider.get(readProperty("warning.icon"));
        warningImageFormatIcon = ImageProvider.get(readProperty("warning.image.format.icon"));
        manualSwitchSegmentIcon = ImageProvider.get(readProperty("manualSwitch.segment.icon"));
        manualSwitchImageIcon = ImageProvider.get(readProperty("manualSwitch.image.icon"));
        downloadIcon = ImageProvider.get(readProperty("download.icon"));
        clusterBackgroundIconColorless = getUnselectedClusterBackground("cluster.background.icon");
        clusterBackgroundSelectedIconColorless = getSelectedClusterBackground("cluster.background.icon");
        clusterBackgroundIconColor1 = getUnselectedClusterBackground("cluster.background.icon.color1");
        clusterBackgroundSelectedIconColor1 = getSelectedClusterBackground("cluster.background.icon.color1");
        clusterBackgroundIconColor2 = getUnselectedClusterBackground("cluster.background.icon.color2");
        clusterBackgroundSelectedIconColor2 = getSelectedClusterBackground("cluster.background.icon.color2");
        clusterBackgroundIconColor3 = getUnselectedClusterBackground("cluster.background.icon.color3");
        clusterBackgroundSelectedIconColor3 = getSelectedClusterBackground("cluster.background.icon.color3");
        clusterBackgroundIconColor4 = getUnselectedClusterBackground("cluster.background.icon.color4");
        clusterBackgroundSelectedIconColor4 = getSelectedClusterBackground("cluster.background.icon.color4");
        clusterBackgroundIconColor5 = getUnselectedClusterBackground("cluster.background.icon.color5");
        clusterBackgroundSelectedIconColor5 = getSelectedClusterBackground("cluster.background.icon.color5");
        clusterBackgroundIconColor6 = getUnselectedClusterBackground("cluster.background.icon.color6");
        clusterBackgroundSelectedIconColor6 = getSelectedClusterBackground("cluster.background.icon.color6");
        clusterBackgroundIconColor7 = getUnselectedClusterBackground("cluster.background.icon.color7");
        clusterBackgroundSelectedIconColor7 = getSelectedClusterBackground("cluster.background.icon.color7");
        clusterBackgroundIconColor8 = getUnselectedClusterBackground("cluster.background.icon.color8");
        clusterBackgroundSelectedIconColor8 = getSelectedClusterBackground("cluster.background.icon.color8");
        clusterBackgroundIconColor9 = getUnselectedClusterBackground("cluster.background.icon.color9");
        clusterBackgroundSelectedIconColor9 = getSelectedClusterBackground("cluster.background.icon.color9");
        clusterBackgroundIconColor10 = getUnselectedClusterBackground("cluster.background.icon.color10");
        clusterBackgroundSelectedIconColor10 = getSelectedClusterBackground("cluster.background.icon.color10");
        preferenceIconName = readProperty("preference.icon");
        detectionIconsLongPath = readProperty("detection.icons.longPath");
        detectionIconsPath = readProperty("detection.icons.path");
        mappedIcon = ImageProvider.get(readProperty("mapped.icon"));
        badDetectionIcon = ImageProvider.get(readProperty("bad.detection.icon"));
        otherIcon = ImageProvider.get(readProperty("other.icon"));
    }

    private ImageIcon getUnselectedClusterBackground(final String key) {
        final ImageProvider imageProvider = new ImageProvider(readProperty(key));
        imageProvider.setSize(CLUSTER_BACKGROUND_SIZE, CLUSTER_BACKGROUND_SIZE);
        return imageProvider.get();
    }

    private ImageIcon getSelectedClusterBackground(final String key) {
        final ImageProvider imageProvider = new ImageProvider(readProperty(key));
        imageProvider.setSize(CLUSTER_BACKGROUND_SELECTED_SIZE, CLUSTER_BACKGROUND_SELECTED_SIZE);
        return imageProvider.get();
    }

    private ImageIcon getUnselectedPhotoIcon(final String key) {
        final ImageProvider imageProvider = new ImageProvider(readProperty(key));
        imageProvider.setSize(UNSELECTED_ICON_SIZE, UNSELECTED_ICON_SIZE);
        return imageProvider.get();
    }

    private ImageIcon getSelectedPhotoIcon(final String key) {
        final ImageProvider imageProvider = new ImageProvider(readProperty(key));
        imageProvider.setSize(SELECTED_ICON_SIZE, SELECTED_ICON_SIZE);
        return imageProvider.get();
    }

    private ImageIcon getUnselectedWrappedPhotoIcon(final String key) {
        final ImageProvider imageProvider = new ImageProvider(readProperty(key));
        imageProvider.setSize(UNSELECTED_WRAPPED_ICON_SIZE, UNSELECTED_WRAPPED_ICON_SIZE);
        return imageProvider.get();
    }

    private ImageIcon getSelectedWrappedPhotoIcon(final String key) {
        final ImageProvider imageProvider = new ImageProvider(readProperty(key));
        imageProvider.setSize(SELECTED_WRAPPED_ICON_SIZE, SELECTED_WRAPPED_ICON_SIZE);
        return imageProvider.get();
    }

    public static IconConfig getInstance() {
        return INSTANCE;
    }


    public String getPluginIconName() {
        return pluginIconName;
    }

    public String getDialogShortcutName() {
        return dialogShortcutName;
    }

    public String getDetectionDialogShortcutName() {
        return detectionDialogShortcutName;
    }


    public String getDeleteIconName() {
        return deleteIconName;
    }

    public String getLayerIconName() {
        return layerIconName;
    }

    public String getDownloadIconName() {
        return downloadIconName;
    }

    public String getSaveIconName() {
        return saveIconName;
    }

    public Icon getLayerIcon() {
        return layerIcon;
    }

    public Icon getLayerIconFiltered(){
        return layerIconFiltered;
    }

    public ImageIcon getPhotoIcon() {
        return photoIcon;
    }

    public ImageIcon getPhotoSelectedIcon() {
        return photoSelectedIcon;
    }

    public ImageIcon getPhotoNoHeadingIcon() {
        return photoNoHeadingIcon;
    }

    public ImageIcon getPhotoNoHeadingSelectedIcon() {
        return photoNoHeadingSelectedIcon;
    }

    public ImageIcon getPhotoSelectedIconPurple() {
        return photoSelectedIconPurple;
    }

    public ImageIcon getPhotoUnselectedIconPurple() {
        return photoUnselectedIconPurple;
    }

    public ImageIcon getPhotoNoHeadingSelectedIconPurple() {
        return photoNoHeadingSelectedIconPurple;
    }

    public ImageIcon getPhotoNoHeadingUnselectedIconPurple() {
        return photoNoHeadingUnselectedIconPurple;
    }

    public ImageIcon getPhotoWrappedSelectedIcon() {
        return photoWrappedSelectedIcon;
    }

    public ImageIcon getPhotoWrappedUnselectedIcon() {
        return photoWrappedUnselectedIcon;
    }

    public ImageIcon getPhotoWrappedNoHeadingSelectedIcon() {
        return photoWrappedNoHeadingSelectedIcon;
    }

    public ImageIcon getPhotoWrappedNoHeadingUnselectedIcon() {
        return photoWrappedNoHeadingUnselectedIcon;
    }

    public ImageIcon getPhotoWrappedSelectedIconPurple() {
        return photoWrappedSelectedIconPurple;
    }

    public ImageIcon getPhotoWrappedUnselectedIconPurple() {
        return photoWrappedUnselectedIconPurple;
    }

    public ImageIcon getPhotoWrappedNoHeadingSelectedIconPurple() {
        return photoWrappedNoHeadingSelectedIconPurple;
    }

    public ImageIcon getPhotoWrappedNoHeadingUnselectedIconPurple() {
        return photoWrappedNoHeadingUnselectedIconPurple;
    }

    public ImageIcon getFilterIcon() {
        return filterIcon;
    }

    public Icon getPreviousIcon() {
        return previousIcon;
    }

    public Icon getNextIcon() {
        return nextIcon;
    }

    public Icon getPlayIcon() {
        return playIcon;
    }

    public Icon getStopIcon() {
        return stopIcon;
    }

    public String getFeedbackIconName() {
        return feedbackIconName;
    }

    public Icon getLocationIcon() {
        return locationIcon;
    }

    public Icon getWrappedImageFormatIcon() {
        return wrappedImageFormatIcon;
    }

    public Icon getFrontFacingImageFormatIcon() {
        return frontFacingImageFormatIcon;
    }

    public Icon getWebPageIcon() {
        return webPageIcon;
    }

    public Icon getClosestImageIcon() {
        return closestImageIcon;
    }

    public Icon getWarningIcon() {
        return warningIcon;
    }

    public Icon getWarningImageFormatIcon() {
        return warningImageFormatIcon;
    }

    public Icon getManualSwitchSegmentIcon() {
        return manualSwitchSegmentIcon;
    }

    public Icon getManualSwitchImageIcon() {
        return manualSwitchImageIcon;
    }

    public String getFilterIconName() {
        return filterIconName;
    }

    public Icon getMatchedWayIcon() {
        return matchedWayIcon;
    }

    public Icon getDownloadIcon() {
        return downloadIcon;
    }

    public ImageIcon getClusterBackgroundIconColorless() {
        return clusterBackgroundIconColorless;
    }

    public ImageIcon getClusterBackgroundSelectedIconColorless() {
        return clusterBackgroundSelectedIconColorless;
    }

    public List<ImageIcon> getClusterBordersColored() {
        return Arrays.asList(clusterBackgroundIconColor1, clusterBackgroundIconColor2, clusterBackgroundIconColor3,
                clusterBackgroundIconColor4, clusterBackgroundIconColor5, clusterBackgroundIconColor6,
                clusterBackgroundIconColor7, clusterBackgroundIconColor8, clusterBackgroundIconColor9,
                clusterBackgroundIconColor10);
    }

    public List<ImageIcon> getSelectedClusterBordersColored() {
        return Arrays.asList(clusterBackgroundSelectedIconColor1, clusterBackgroundSelectedIconColor2,
                clusterBackgroundSelectedIconColor3, clusterBackgroundSelectedIconColor4,
                clusterBackgroundSelectedIconColor5, clusterBackgroundSelectedIconColor6,
                clusterBackgroundSelectedIconColor7, clusterBackgroundSelectedIconColor8,
                clusterBackgroundSelectedIconColor9, clusterBackgroundSelectedIconColor10);
    }

    public String getPreferenceIconName() {
        return preferenceIconName;
    }

    public String getDetectionIconsLongPath() {
        return detectionIconsLongPath;
    }

    public String getDetectionIconsPath() {
        return detectionIconsPath;
    }

    public Icon getMappedIcon() {
        return mappedIcon;
    }

    public Icon getBadDetectionIcon() {
        return badDetectionIcon;
    }

    public Icon getOtherIcon() {
        return otherIcon;
    }
}