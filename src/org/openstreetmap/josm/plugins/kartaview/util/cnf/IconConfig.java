/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.cnf;

import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.openstreetmap.josm.plugins.kartaview.entity.ConfidenceLevelCategory;
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
    private static final int CLUSTER_BACKGROUND_SELECTED_SIZE = 65;

    private static final int DETECTION_BACKGROUND_SIZE = 62;
    private static final int DETECTION_BACKGROUND_SELECTED_SIZE = 85;
    private static final int SELECTED_ICON_SIZE = 28;
    private static final int UNSELECTED_ICON_SIZE = 14;
    private static final int SELECTED_WRAPPED_ICON_SIZE = 37;
    private static final int UNSELECTED_WRAPPED_ICON_SIZE = 20;


    private final String pluginIconName;
    private final String dialogShortcutName;
    private final String detectionDialogShortcutName;
    private final String edgeDetectionDialogShortcutName;
    private final String deleteIconName;
    private final String kartaViewLayerIconName;
    private final String edgeLayerIconName;
    private final String downloadIconName;
    private final String saveIconName;
    private final Icon kartaViewLayerIcon;
    private final Icon edgeLayerIcon;
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
    private final Icon downloadIcon;
    private final ImageIcon clusterBackgroundUnselectedIconColorless;
    private final ImageIcon clusterBackgroundSelectedIconColorless;
    private final ImageIcon clusterBackgroundUnselectedIconColorC3;
    private final ImageIcon clusterBackgroundSelectedIconColorC3;
    private final ImageIcon clusterBackgroundUnselectedIconColorC2;
    private final ImageIcon clusterBackgroundSelectedIconColorC2;
    private final ImageIcon clusterBackgroundIconUnselectedColorC1;
    private final ImageIcon clusterBackgroundSelectedIconColorC1;
    private final ImageIcon detectionHeadingUnselectedIcon;
    private final ImageIcon detectionHeadingSelectedIcon;

    private final ImageIcon edgeDetectionHeadingUnselectedIcon;
    private final ImageIcon edgeClusterBackgroundUnselectedIconColorless;
    private final ImageIcon edgeClusterBackgroundSelectedIconColorless;
    private final ImageIcon edgeClusterBackgroundUnselectedIconColorC1;
    private final ImageIcon edgeClusterBackgroundSelectedIconColorC1;
    private final ImageIcon edgeClusterBackgroundUnselectedIconColorC2;
    private final ImageIcon edgeClusterBackgroundSelectedIconColorC2;
    private final ImageIcon edgeClusterBackgroundUnselectedIconColorC3;
    private final ImageIcon edgeClusterBackgroundSelectedIconColorC3;
    private final ImageIcon edgeDetectionHeadingSelectedIcon;
    private final String preferenceIconName;
    private final String detectionIconsLongPath;
    private final String detectionIconsPath;
    private final String edgeDetectionIconsPath;
    private final Icon mappedIcon;
    private final Icon badDetectionIcon;
    private final Icon otherIcon;

    private IconConfig() {
        super(CONFIG_FILE);

        pluginIconName = readProperty("plugin.icon");
        dialogShortcutName = readProperty("dialog.shortcut");
        detectionDialogShortcutName = readProperty("dialog.detection.shortcut");
        edgeDetectionDialogShortcutName = readProperty("dialog.edge.detection.shortcut");
        deleteIconName = readProperty("delete.icon");
        kartaViewLayerIconName = readProperty("kartaview.layer.icon");
        edgeLayerIconName = readProperty("edge.layer.icon");
        downloadIconName = readProperty("download.icon");
        saveIconName = readProperty("save.icon");
        kartaViewLayerIcon = ImageProvider.get(kartaViewLayerIconName);
        edgeLayerIcon = ImageProvider.get(edgeLayerIconName);
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
        downloadIcon = ImageProvider.get(readProperty("download.icon"));
        clusterBackgroundUnselectedIconColorless = getUnselectedClusterBackground("cluster.background.icon");
        clusterBackgroundSelectedIconColorless = getSelectedClusterBackground("cluster.background.icon");
        clusterBackgroundUnselectedIconColorC3 = getUnselectedClusterBackground("cluster.background.icon.colorC3");
        clusterBackgroundSelectedIconColorC3 = getSelectedClusterBackground("cluster.background.icon.colorC3");
        clusterBackgroundUnselectedIconColorC2 = getUnselectedClusterBackground("cluster.background.icon.colorC2");
        clusterBackgroundSelectedIconColorC2 = getSelectedClusterBackground("cluster.background.icon.colorC2");
        clusterBackgroundIconUnselectedColorC1 = getUnselectedClusterBackground("cluster.background.icon.colorC1");
        clusterBackgroundSelectedIconColorC1 = getSelectedClusterBackground("cluster.background.icon.colorC1");
        detectionHeadingUnselectedIcon = getUnselectedDetectionBackground("detection.heading.icon");
        detectionHeadingSelectedIcon = getSelectedDetectionBackground("detection.heading.icon");
        edgeDetectionHeadingUnselectedIcon = getUnselectedDetectionBackground("edge.detection.heading.icon");
        edgeClusterBackgroundUnselectedIconColorless = getUnselectedClusterBackground("edge.cluster.background.icon");
        edgeClusterBackgroundSelectedIconColorless = getSelectedClusterBackground("edge.cluster.background.icon");
        edgeClusterBackgroundUnselectedIconColorC1 =
                getUnselectedClusterBackground("edge.cluster.background.icon.colorC1");
        edgeClusterBackgroundSelectedIconColorC1 = getSelectedClusterBackground("edge.cluster.background.icon.colorC1");
        edgeClusterBackgroundUnselectedIconColorC2 =
                getUnselectedClusterBackground("edge.cluster.background.icon.colorC2");
        edgeClusterBackgroundSelectedIconColorC2 = getSelectedClusterBackground("edge.cluster.background.icon.colorC2");
        edgeClusterBackgroundUnselectedIconColorC3 =
                getUnselectedClusterBackground("edge.cluster.background.icon.colorC3");
        edgeClusterBackgroundSelectedIconColorC3 = getSelectedClusterBackground("edge.cluster.background.icon.colorC3");
        edgeDetectionHeadingSelectedIcon = getSelectedDetectionBackground("edge.detection.heading.icon");
        preferenceIconName = readProperty("preference.icon");
        detectionIconsLongPath = readProperty("detection.icons.longPath");
        detectionIconsPath = readProperty("detection.icons.path");
        edgeDetectionIconsPath = readProperty("edge.detection.icons.path");
        mappedIcon = ImageProvider.get(readProperty("mapped.icon"));
        badDetectionIcon = ImageProvider.get(readProperty("bad.detection.icon"));
        otherIcon = ImageProvider.get(readProperty("other.icon"));
    }

    public static IconConfig getInstance() {
        return INSTANCE;
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

    private ImageIcon getUnselectedDetectionBackground(final String key) {
        final ImageProvider imageProvider = new ImageProvider(readProperty(key));
        imageProvider.setSize(DETECTION_BACKGROUND_SIZE, DETECTION_BACKGROUND_SIZE);
        return imageProvider.get();
    }

    private ImageIcon getSelectedDetectionBackground(final String key) {
        final ImageProvider imageProvider = new ImageProvider(readProperty(key));
        imageProvider.setSize(DETECTION_BACKGROUND_SELECTED_SIZE, DETECTION_BACKGROUND_SELECTED_SIZE);
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

    public ImageIcon getEdgeDetectionHeadingUnselectedIcon() {
        return edgeDetectionHeadingUnselectedIcon;
    }

    public ImageIcon getEdgeDetectionHeadingSelectedIcon() {
        return edgeDetectionHeadingSelectedIcon;
    }

    public ImageIcon getEdgeClusterBackgroundUnselectedIconColorless() {
        return edgeClusterBackgroundUnselectedIconColorless;
    }

    public ImageIcon getEdgeClusterBackgroundSelectedIconColorless() {
        return edgeClusterBackgroundSelectedIconColorless;
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

    public String getEdgeDetectionDialogShortcutName() {
        return edgeDetectionDialogShortcutName;
    }

    public String getDeleteIconName() {
        return deleteIconName;
    }

    public String getKartaViewLayerIconName() {
        return kartaViewLayerIconName;
    }

    public String getEdgeLayerIconName() {
        return edgeLayerIconName;
    }

    public String getDownloadIconName() {
        return downloadIconName;
    }

    public String getSaveIconName() {
        return saveIconName;
    }

    public Icon getKartaViewLayerIcon() {
        return kartaViewLayerIcon;
    }

    public Icon getEdgeLayerIcon() {
        return edgeLayerIcon;
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

    public String getFilterIconName() {
        return filterIconName;
    }

    public Icon getMatchedWayIcon() {
        return matchedWayIcon;
    }

    public Icon getDownloadIcon() {
        return downloadIcon;
    }

    public ImageIcon getClusterBackgroundUnselectedIconColorless() {
        return clusterBackgroundUnselectedIconColorless;
    }

    public ImageIcon getClusterBackgroundSelectedIconColorless() {
        return clusterBackgroundSelectedIconColorless;
    }

    public Map<ConfidenceLevelCategory, ImageIcon> getUnselectedClusterBordersColored() {
        final Map<ConfidenceLevelCategory, ImageIcon> clusterBordersColored = new HashMap<>();
        clusterBordersColored.put(ConfidenceLevelCategory.C1, clusterBackgroundIconUnselectedColorC1);
        clusterBordersColored.put(ConfidenceLevelCategory.C2, clusterBackgroundUnselectedIconColorC2);
        clusterBordersColored.put(ConfidenceLevelCategory.C3, clusterBackgroundUnselectedIconColorC3);
        return clusterBordersColored;
    }

    public Map<ConfidenceLevelCategory, ImageIcon> getSelectedClusterBordersColored() {
        final Map<ConfidenceLevelCategory, ImageIcon> selectedClusterBordersColored = new HashMap<>();
        selectedClusterBordersColored.put(ConfidenceLevelCategory.C1, clusterBackgroundSelectedIconColorC1);
        selectedClusterBordersColored.put(ConfidenceLevelCategory.C2, clusterBackgroundSelectedIconColorC2);
        selectedClusterBordersColored.put(ConfidenceLevelCategory.C3, clusterBackgroundSelectedIconColorC3);
        return selectedClusterBordersColored;
    }

    public Map<ConfidenceLevelCategory, ImageIcon> getUnselectedEdgeClusterBordersColored() {
        final Map<ConfidenceLevelCategory, ImageIcon> clusterBordersColored = new HashMap<>();
        clusterBordersColored.put(ConfidenceLevelCategory.C1, edgeClusterBackgroundUnselectedIconColorC1);
        clusterBordersColored.put(ConfidenceLevelCategory.C2, edgeClusterBackgroundUnselectedIconColorC2);
        clusterBordersColored.put(ConfidenceLevelCategory.C3, edgeClusterBackgroundUnselectedIconColorC3);
        return clusterBordersColored;
    }

    public Map<ConfidenceLevelCategory, ImageIcon> getSelectedEdgeClusterBordersColored() {
        final Map<ConfidenceLevelCategory, ImageIcon> selectedClusterBordersColored = new HashMap<>();
        selectedClusterBordersColored.put(ConfidenceLevelCategory.C1, edgeClusterBackgroundSelectedIconColorC1);
        selectedClusterBordersColored.put(ConfidenceLevelCategory.C2, edgeClusterBackgroundSelectedIconColorC2);
        selectedClusterBordersColored.put(ConfidenceLevelCategory.C3, edgeClusterBackgroundSelectedIconColorC3);
        return selectedClusterBordersColored;
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

    public String getEdgeDetectionIconsPath() {
        return edgeDetectionIconsPath;
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

    public ImageIcon getDetectionHeadingUnselectedIcon() {
        return detectionHeadingUnselectedIcon;
    }

    public ImageIcon getDetectionHeadingSelectedIcon() {
        return detectionHeadingSelectedIcon;
    }
}