/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.cnf;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.tools.ImageProvider;
import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Loads icons and icon names.
 *
 * @author Beata
 * @version $Revision$
 */
public final class IconConfig extends BaseConfig {

    private static final IconConfig INSTANCE = new IconConfig();
    private static final String CONFIG_FILE = "openstreetcam_icon.properties";

    private final String pluginIconName;
    private final String dialogShortcutName;
    private final String detectionDialogShortcutName;
    private final String deleteIconName;
    private final String layerIconName;
    private final String downloadIconName;
    private final String saveIconName;
    private final Icon layerIcon;
    private final ImageIcon photoIcon;
    private final ImageIcon photoSelectedIcon;
    private final ImageIcon photoNoHeadingIcon;
    private final ImageIcon photoNoHeadingSelectedIcon;
    private final ImageIcon filterIcon;
    private final String filterIconName;
    private final Icon previousIcon;
    private final Icon nextIcon;
    private final Icon playIcon;
    private final Icon stopIcon;
    private final String feedbackIconName;
    private final Icon locationIcon;
    private final Icon webPageIcon;
    private final Icon closestImageIcon;
    private final Icon matchedWayIcon;
    private final Icon warningIcon;
    private final Icon manualSwitchSegmentIcon;
    private final Icon manualSwitchImageIcon;
    private final Icon downloadIcon;
    private final ImageIcon clusterBackgroundIcon;
    private final ImageIcon clusterBackgroundSelectedIcon;
    private final String preferenceIconName;
    private final String detectionIconsLongPath;
    private final String detectionIconsPath;


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
        photoIcon = ImageProvider.get(readProperty("photo.icon"));
        photoSelectedIcon = ImageProvider.get(readProperty("photo.sel.icon"));
        photoNoHeadingIcon = ImageProvider.get(readProperty("photo.noHeading.icon"));
        photoNoHeadingSelectedIcon = ImageProvider.get(readProperty("photo.noHeading.sel.icon"));
        filterIconName = readProperty("filter.icon");
        filterIcon = ImageProvider.get(filterIconName);
        previousIcon = ImageProvider.get(readProperty("previous.icon"));
        nextIcon = ImageProvider.get(readProperty("next.icon"));
        playIcon = ImageProvider.get(readProperty("play.icon"));
        stopIcon = ImageProvider.get(readProperty("stop.icon"));
        locationIcon = ImageProvider.get(readProperty("location.icon"));
        feedbackIconName = readProperty("feedback.icon");
        webPageIcon = ImageProvider.get(readProperty("webPage.icon"));
        closestImageIcon = ImageProvider.get(readProperty("closestImage.icon"));
        matchedWayIcon = ImageProvider.get(readProperty("matchedWay.icon"));
        warningIcon = ImageProvider.get(readProperty("warning.icon"));
        manualSwitchSegmentIcon = ImageProvider.get(readProperty("manualSwitch.segment.icon"));
        manualSwitchImageIcon = ImageProvider.get(readProperty("manualSwitch.image.icon"));
        downloadIcon = ImageProvider.get(readProperty("download.icon"));

        final ImageProvider imageProvider = new ImageProvider(readProperty("cluster.background.icon"));
        imageProvider.setSize(42, 42);
        clusterBackgroundIcon = imageProvider.get();

        imageProvider.setSize(55, 55);
        clusterBackgroundSelectedIcon = imageProvider.get();
        preferenceIconName = readProperty("preference.icon");
        detectionIconsLongPath = readProperty("detection.icons.longPath");
        detectionIconsPath = readProperty("detection.icons.path");
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

    public Icon getWebPageIcon() {
        return webPageIcon;
    }

    public Icon getClosestImageIcon() {
        return closestImageIcon;
    }

    public Icon getWarningIcon() {
        return warningIcon;
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

    public ImageIcon getClusterBackgroundIcon() {
        return clusterBackgroundIcon;
    }

    public ImageIcon getClusterBackgroundSelectedIcon() {
        return clusterBackgroundSelectedIcon;
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
}