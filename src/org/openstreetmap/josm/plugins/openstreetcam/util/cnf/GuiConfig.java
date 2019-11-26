/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Loads GUI related texts.
 *
 * @author Beata
 * @version $Revision$
 */
public final class GuiConfig extends BaseConfig {

    private static final String CONFIG_FILE = "openstreetcam_gui.properties";
    private static final GuiConfig INSTANCE = new GuiConfig();

    private final String pluginShortName;
    private final String pluginLongName;
    private final String pluginTlt;
    private final String pluginShortcutText;
    private final String pluginShortcutLongText;
    private final String pluginDetectionShortcutText;
    private final String pluginDetectionShortcutLongText;

    private final String prefMapViewLbl;
    private final String prefPhotoZoomLbl;
    private final String prefManualSwitchLbl;
    private final String prefDataLoadLbl;
    private final String prefImageLbl;
    private final String prefImageHighQualityLbl;
    private final String prefMouseHoverLbl;
    private final String prefMouseHoverDelayLbl;
    private final String prefAggregatedLbl;
    private final String prefAggregatedDisplayDetectionLbl;
    private final String prefAggregatedDisplayTagsLbl;
    private final String prefAggregatedDisplayColorCodedLbl;
    private final String prefAggregatedLegendLbl;
    private final String[] prefAggregatedLegendHeader;
    private final String pregAggregatedLegendHeaderColor;

    private final String prefTrackLbl;
    private final String prefDisplayTrackLbl;
    private final String prefAutoplayLbl;
    private final String prefAutoplayLengthLbl;
    private final String prefAutoplayDelayLbl;

    private final String prefCacheLbl;
    private final String prefMemoryLbl;
    private final String prefDiskLbl;
    private final String prefPrevNextLbl;
    private final String prefNearbyLbl;

    private final String btnPreviousTlt;
    private final String btnPreviousShortcutText;
    private final String btnNextTlt;
    private final String btnNextShortcutText;

    private final String btnClosestTlt;
    private final String btnClosestShortcutText;
    private final String btnPlayTlt;
    private final String btnPlayShortcutText;
    private final String btnStopTlt;
    private final String btnStopShortcutText;
    private final String btnLocationTlt;
    private final String btnLocationShortcutText;
    private final String btnWebPageTlt;
    private final String btnWebPageShortcutTlt;
    private final String btnDataSwitchImageTlt;
    private final String btnDataSwitchSegmentTlt;
    private final String btnDataSwitchShortcutTlt;
    private final String btnMatchedWayTlt;
    private final String btnMatchedWayShortcutTlt;

    private final String dlgFilterTitle;
    private final String dlgFilterShortcutText;
    private final String dlgFilterDateLbl;
    private final String dlgFilterUserLbl;
    private final String dlgFilterLoginWarningLbl;
    private final String dlgFilterDataTypeLbl;
    private final String dlgFilterDetectionLbl;
    private final String dlgFilterOsmComparisonLbl;
    private final String dlgFilterModeLbl;
    private final String dlgFilterEditStatusLbl;
    private final String dlgFilterDetectionTypeLbl;
    private final String dlgFilterDataTypeImageTxt;
    private final String dlgFilterDataTypeDetectionsTxt;
    private final String dlgFilterDataTypeAggregatedDetectionsTxt;
    private final String dlgFilterDataRegionLbl;
    private final String dlgFilterSearchDataLbl;
    private final String dlgFilterConfidenceLbl;
    private final String dlgFilterConfidenceMinLbl;
    private final String dlgFilterConfidenceMaxLbl;

    private final String btnOkLbl;
    private final String btnClearLbl;
    private final String btnCancelLbl;
    private final String btnSelectLbl;
    private final String btnResetLbl;

    private final String errorTitle;
    private final String errorPhotoListText;
    private final String errorSequenceText;
    private final String errorPhotoLoadingText;
    private final String errorPhotoPageText;
    private final String errorUserPageText;
    private final String errorFeedbackPageText;
    private final String incorrectDateFilterText;
    private final String unacceptedDateFilterText;
    private final String errorSequenceSaveText;
    private final String errorSegmentListText;
    private final String errorDetectionRetrieveText;
    private final String errorDetectionUpdateText;
    private final String errorClusterRetrieveText;
    private final String errorListSignsText;
    private final String errorListRegionsText;

    private final String incorrectConfidenceFilterText;
    private final String unacceptedConfidenceFilterText;

    private final String errorDownloadOsmData;
    private final String warningTitle;
    private final String warningHighQualityPhoto;
    private final String warningLoadingPhoto;

    private final String layerDeleteMenuItemLbl;
    private final String layerDeleteMenuItemTlt;
    private final String layerFeedbackMenuItemLbl;
    private final String layerFeedbackShortcutText;
    private final String layerPreviousMenuItemLbl;
    private final String layerNextMenuItemLbl;
    private final String layerPreferenceMenuItemLbl;
    private final String layerSaveSequenceMenuItemLbl;

    private final String infoMatchedWayTitle;
    private final String infoDownloadNextPhotosTitle;
    private final String infoDownloadPreviousPhotosTitle;
    private final String infoFileExistsTitle;
    private final String infoFileExistsText;
    private final String gpxTrackDescription;

    private final String detectionDialogTitleName;
    private final String clusterDialogTitleName;
    private final String btnFixDetectionShortcutText;
    private final String btnFixDetectionTlt;
    private final String btnAlreadyFixedDetectionShortcutText;
    private final String btnAlreadyFixedDetectionTlt;
    private final String btnCouldntFixDetection;
    private final String btnBadDetection;
    private final String btnBadDetectionShortcutText;
    private final String btnBadDetectionTlt;
    private final String btnOtherActionOnDetection;
    private final String btnOtherActionOnDetectionShortcutText;
    private final String btnOtherActionOnDetectionTlt;
    private final String btnMatchedData;
    private final String btnMatchedDataShortcutText;
    private final String btnMatchedDataTlt;
    private final String btnClusterNextShortcutText;
    private final String btnClusterPreviousShortcutText;

    private final String detectedDetectionText;
    private final String detectionOnOsmText;
    private final String detectionModeText;
    private final String detectionEditStatusMappedText;
    private final String detectionValidationStatusText;
    private final String detectionChangedValidationStatusText;
    private final String detectionConfirmedValidationStatusText;
    private final String detectionRemovedValidationStatusText;
    private final String detectionToBeCheckedValidationStatusText;
    private final String detectionTaskStatusText;
    private final String detectionManualModeText;
    private final String detectionAutomaticModeText;
    private final String detectionNewOnOsmText;
    private final String detectionChangedOnOsmText;
    private final String detectionSameOnOsmText;
    private final String detectionUnknownOnOsmText;
    private final String detectionFacingText;
    private final String dialogAddCommentText;
    private final String authenticationNeededErrorMessage;
    private final String detectionCreatedDate;
    private final String detectionUpdatedDate;
    private final String clusterDetectionsLbl;
    private final String clusterOcrValueLbl;
    private final String detectionIdLbl;
    private final String detectionConfidenceLbl;
    private final String facingConfidenceLbl;
    private final String positioningConfidenceLbl;
    private final String keypointsConfidenceLbl;
    private final String trackingConfidenceLbl;
    private final String ocrConfidenceLbl;
    private final String detectionTrackingIdLbl;
    private final String detectionAutomaticOcrValueLbl;
    private final String detectionManualOcrValueLbl;
    private final String clusterLaneCountText;
    private final String clusterConfidenceLevelText;

    private final String[] clusterTableHeader;

    private final String nextRowSelection;
    private final String prevRowSelection;

    private GuiConfig() {
        super(CONFIG_FILE);

        pluginShortName = readProperty("plugin.name.short");
        pluginLongName = readProperty("plugin.name.long");
        pluginTlt = readProperty("plugin.tlt");
        pluginShortcutText = readProperty("plugin.shortcut.text");
        pluginShortcutLongText = readProperty("plugin.shortcut.longText");
        pluginDetectionShortcutText = readProperty("plugin.detection.shortcut.text");
        pluginDetectionShortcutLongText = readProperty("plugin.detection.shortcut.longText");

        prefMapViewLbl = readProperty("preferences.mapView.lbl");
        prefPhotoZoomLbl = readProperty("preferences.mapView.zoom.lbl");
        prefManualSwitchLbl = readProperty("preferences.mapView.switch.lbl");
        prefDataLoadLbl = readProperty("preferences.mapView.load.lbl");
        prefImageLbl = readProperty("preference.photo.lbl");
        prefImageHighQualityLbl = readProperty("preference.photo.highQuality.lbl");
        prefMouseHoverLbl = readProperty("preference.photo.mouseHover.lbl");
        prefMouseHoverDelayLbl = readProperty("preference.photo.mouseHover.delay.lbl");
        prefAggregatedLbl = readProperty("preference.aggregated.lbl");
        prefAggregatedDisplayDetectionLbl = readProperty("preference.aggregated.detection");
        prefAggregatedDisplayTagsLbl = readProperty("preference.aggregated.tags");
        prefAggregatedDisplayColorCodedLbl = readProperty("preference.aggregated.colorCoded");
        prefAggregatedLegendLbl = readProperty("preference.aggregated.legend");
        prefAggregatedLegendHeader = readPropertiesArray("preference.aggregated.legend.header");
        pregAggregatedLegendHeaderColor = readProperty("preference.aggregated.legend.header.color");
        prefTrackLbl = readProperty("preference.track.lbl");
        prefDisplayTrackLbl = readProperty("preference.track.displayTrack.lbl");
        prefAutoplayLbl = readProperty("preference.track.autoplay.lbl");
        prefAutoplayLengthLbl = readProperty("preference.track.autoplay.length.lbl");
        prefAutoplayDelayLbl = readProperty("preference.track.autoplay.delay.lbl");

        prefCacheLbl = readProperty("preference.cache.lbl");
        prefMemoryLbl = readProperty("preference.cache.memory.lbl");
        prefDiskLbl = readProperty("preference.cache.disk.lbl");
        prefPrevNextLbl = readProperty("preference.cache.prevNext.lbl");
        prefNearbyLbl = readProperty("preference.cache.nearby.lbl");

        btnPreviousTlt = readProperty("btn.previous.tlt");
        btnPreviousShortcutText = readProperty("btn.previous.shortcut.text");
        btnNextTlt = readProperty("btn.next.tlt");
        btnNextShortcutText = readProperty("btn.next.shortcut.text");
        btnPlayTlt = readProperty("btn.play.tlt");
        btnPlayShortcutText = readProperty("btn.play.shortcut.text");
        btnStopTlt = readProperty("btn.stop.tlt");
        btnStopShortcutText = readProperty("btn.stop.shortcut.text");
        btnLocationTlt = readProperty("btn.location.tlt");
        btnLocationShortcutText = readProperty("btn.location.shortcut.text");
        btnWebPageTlt = readProperty("btn.webPage.tlt");
        btnWebPageShortcutTlt = readProperty("btn.webPage.shortcut.text");
        btnClosestTlt = readProperty("btn.closest.tlt");
        btnClosestShortcutText = readProperty("btn.closest.shortcut.text");
        btnDataSwitchImageTlt = readProperty("btn.switch.image.tlt");
        btnDataSwitchSegmentTlt = readProperty("btn.switch.segment.tlt");
        btnDataSwitchShortcutTlt = readProperty("btn.switch.shortcut.text");
        btnMatchedWayShortcutTlt = readProperty("btn.matchedWay.shortcut.text");
        btnMatchedWayTlt = readProperty("btn.matchedWay.tlt");

        dlgFilterTitle = readProperty("filter.title");
        dlgFilterShortcutText = readProperty("filter.shortcut.text");
        dlgFilterDateLbl = readProperty("filter.date.lbl");
        dlgFilterUserLbl = readProperty("filter.user.lbl");
        dlgFilterLoginWarningLbl = readProperty("filter.login.warning.lbl");
        dlgFilterDetectionLbl = readProperty("filter.detection.lbl");
        dlgFilterModeLbl = readProperty("filter.mode.lbl");
        dlgFilterDataTypeLbl = readProperty("filter.dataType.lbl");
        dlgFilterOsmComparisonLbl = readProperty("filter.osmComparison.lbl");
        dlgFilterEditStatusLbl = readProperty("filter.editStatus.lbl");
        dlgFilterDetectionTypeLbl = readProperty("filter.detectionType.lbl");
        dlgFilterDataTypeImageTxt = readProperty("filter.dataType.photos");
        dlgFilterDataTypeDetectionsTxt = readProperty("filter.dataType.detections");
        dlgFilterDataTypeAggregatedDetectionsTxt = readProperty("filter.dataType.aggregatedDetections");
        dlgFilterDataRegionLbl = readProperty("filter.detectionRegion.lbl");
        dlgFilterSearchDataLbl = readProperty("filter.searchData.lbl");
        dlgFilterConfidenceLbl = readProperty("filter.confidenceLevel.lbl");
        dlgFilterConfidenceMinLbl = readProperty("filter.confidence.min.lbl");
        dlgFilterConfidenceMaxLbl = readProperty("filter.confidence.max.lbl");

        btnOkLbl = readProperty("btn.ok.lbl");
        btnClearLbl = readProperty("btn.clear.lbl");
        btnCancelLbl = readProperty("btn.cancel.lbl");
        btnSelectLbl = readProperty("btn.select.lbl");
        btnResetLbl = readProperty("btn.reset.lbl");

        errorTitle = readProperty("error.title");
        errorPhotoListText = readProperty("error.photo.list");
        errorSequenceText = readProperty("error.track");
        errorPhotoLoadingText = readProperty("error.photo.loading");
        errorPhotoPageText = readProperty("error.photo.page");
        errorUserPageText = readProperty("error.user.page");
        errorFeedbackPageText = readProperty("error.feedback.page");
        errorSegmentListText = readProperty("error.segment.list");
        errorSequenceSaveText = readProperty("error.track.save");
        errorDetectionRetrieveText = readProperty("error.detection.retrieve");
        errorDetectionUpdateText = readProperty("error.detection.update");
        errorClusterRetrieveText = readProperty("error.cluster.retrieve");
        errorListSignsText = readProperty("error.sign.list");
        errorListRegionsText = readProperty("error.sign.region.list");
        errorDownloadOsmData = readProperty("error.osmData.download");
        unacceptedDateFilterText = readProperty("error.dateFilter.unaccepted");
        incorrectDateFilterText = readProperty("error.dateFilter.incorrect");
        incorrectConfidenceFilterText = readProperty("error.confidenceFilter.incorrect");
        unacceptedConfidenceFilterText = readProperty("error.confidenceFilter.unaccepted");
        warningTitle = readProperty("warning.title");
        warningHighQualityPhoto = readProperty("warning.photo.highQuality");
        warningLoadingPhoto = readProperty("warning.photo.loading");

        layerDeleteMenuItemLbl = readProperty("layer.menu.delete.lbl");
        layerDeleteMenuItemTlt = readProperty("layer.menu.delete.tlt");
        layerFeedbackMenuItemLbl = readProperty("layer.menu.feedback.lbl");
        layerFeedbackShortcutText = readProperty("layer.menu.feedback.shortcut.text");
        layerPreviousMenuItemLbl = readProperty("layer.menu.previous.lbl");
        layerNextMenuItemLbl = readProperty("layer.menu.next.lbl");
        layerPreferenceMenuItemLbl = readProperty("layer.menu.preference.lbl");
        layerSaveSequenceMenuItemLbl = readProperty("layer.menu.sequence.save");

        infoMatchedWayTitle = readProperty("info.matchedWay.title");
        infoDownloadNextPhotosTitle = readProperty("info.download.next.title");
        infoDownloadPreviousPhotosTitle = readProperty("info.download.previous.title");
        infoFileExistsTitle = readProperty("info.file.exists.title");
        infoFileExistsText = readProperty("info.file.exists.text");

        gpxTrackDescription = readProperty("gpx.track.description");

        btnFixDetectionShortcutText = readProperty("btn.detection.fix.shortcut.text");
        btnFixDetectionTlt = readProperty("btn.detection.fix.tlt");
        btnAlreadyFixedDetectionShortcutText = readProperty("btn.detection.alreadyFixed.shortcut.text");
        btnAlreadyFixedDetectionTlt = readProperty("btn.detection.alreadyFixed.tlt");
        btnCouldntFixDetection = readProperty("btn.detection.couldntFixed");
        btnBadDetection = readProperty("btn.detection.couldntFixed.bad");
        btnBadDetectionShortcutText = readProperty("btn.detection.couldntFixed.bad.shortcut.text");
        btnBadDetectionTlt = readProperty("btn.detection.couldntFixed.bad.tlt");
        btnOtherActionOnDetection = readProperty("btn.detection.couldntFixed.other");
        btnOtherActionOnDetectionShortcutText = readProperty("btn.detection.couldntFixed.other.shortcut.text");
        btnOtherActionOnDetectionTlt = readProperty("btn.detection.couldntFixed.other.tlt");
        btnMatchedData = readProperty("btn.matchedData.lbl");
        btnClusterNextShortcutText = readProperty("btn.cluster.next.shortcut.text");
        btnClusterPreviousShortcutText = readProperty("btn.cluster.previous.shortcut.text");
        btnMatchedDataTlt = readProperty("btn.matchedData.tlt");
        btnMatchedDataShortcutText = readProperty("btn.matchedData.shortcut.text");

        detectionDialogTitleName = readProperty("detection.dialog.title");
        clusterDialogTitleName = readProperty("cluster.dialog.title");
        detectedDetectionText = readProperty("detection.detected.text");
        detectionOnOsmText = readProperty("detection.osm.text");
        detectionModeText = readProperty("detection.mode.text");

        detectionEditStatusMappedText = readProperty("detection.edit.status.value.mapped");
        detectionValidationStatusText = readProperty("detection.validation.status.text");
        detectionChangedValidationStatusText = readProperty("detection.validation.status.value.changed");
        detectionConfirmedValidationStatusText = readProperty("detection.validation.status.value.confirmed");
        detectionRemovedValidationStatusText = readProperty("detection.validation.status.value.removed");
        detectionToBeCheckedValidationStatusText = readProperty("detection.validation.status.value.toBeChecked");
        detectionTaskStatusText = readProperty("detection.task.status.text");
        detectionManualModeText = readProperty("detection.mode.value.manual");
        detectionAutomaticModeText = readProperty("detection.mode.value.automatic");
        detectionNewOnOsmText = readProperty("detection.osm.new.value");
        detectionChangedOnOsmText = readProperty("detection.osm.changed.value");
        detectionSameOnOsmText = readProperty("detection.osm.same.value");
        detectionUnknownOnOsmText = readProperty("detection.osm.unknown.value");
        detectionFacingText = readProperty("detection.facing.text");
        dialogAddCommentText = readProperty("detection.dialog.comment.text");
        authenticationNeededErrorMessage = readProperty("error.detection.authentication");
        detectionCreatedDate = readProperty("detection.date.created");
        detectionUpdatedDate = readProperty("detection.date.updated");
        clusterDetectionsLbl = readProperty("cluster.detections.lbl");
        clusterOcrValueLbl = readProperty("cluster.ocrValue.lbl");
        detectionIdLbl = readProperty("detection.id");
        detectionTrackingIdLbl = readProperty("detection.tracking.id");
        detectionConfidenceLbl = readProperty("detection.confidence.detection");
        detectionAutomaticOcrValueLbl = readProperty("detection.automaticOcrValue.lbl");
        detectionManualOcrValueLbl = readProperty("detection.manualOcrValue.lbl");
        facingConfidenceLbl = readProperty("detection.confidence.facing");
        positioningConfidenceLbl = readProperty("detection.confidece.positioning");
        keypointsConfidenceLbl = readProperty("detection.confidence.keypoints");
        trackingConfidenceLbl = readProperty("detection.confidence.tracking");
        ocrConfidenceLbl = readProperty("detection.confidence.ocr");
        clusterLaneCountText = readProperty("cluster.lane.count.text");
        clusterConfidenceLevelText = readProperty("cluster.confidence.level.text");

        clusterTableHeader = readPropertiesArray("cluster.table.header");

        nextRowSelection = readProperty("table.next.row.selection");
        prevRowSelection = readProperty("table.prev.row.selection");
    }

    public static GuiConfig getInstance() {
        return INSTANCE;
    }

    public String getPluginShortName() {
        return pluginShortName;
    }

    public String getPluginLongName() {
        return pluginLongName;
    }

    public String getPluginDetectionShortcutText() {
        return pluginDetectionShortcutText;
    }

    public String getPluginDetectionShortcutLongText() {
        return pluginDetectionShortcutLongText;
    }

    public String getPluginTlt() {
        return pluginTlt;
    }

    public String getPrefMapViewLbl() {
        return prefMapViewLbl;
    }

    public String getPrefPhotoZoomLbl() {
        return prefPhotoZoomLbl;
    }

    public String getPrefManualSwitchLbl() {
        return prefManualSwitchLbl;
    }

    public String getPrefDataLoadLbl() {
        return prefDataLoadLbl;
    }

    public String getPrefImageLbl() {
        return prefImageLbl;
    }

    public String getPrefImageHighQualityLbl() {
        return prefImageHighQualityLbl;
    }

    public String getPrefDisplayTrackLbl() {
        return prefDisplayTrackLbl;
    }

    public String getPrefMouseHoverLbl() {
        return prefMouseHoverLbl;
    }

    public String getPrefMouseHoverDelayLbl() {
        return prefMouseHoverDelayLbl;
    }

    public String getPrefAggregatedLbl() {
        return prefAggregatedLbl;
    }

    public String getPrefAggregatedDisplayDetectionLbl() {
        return prefAggregatedDisplayDetectionLbl;
    }

    public String getPrefAggregatedDisplayTagsLbl() {
        return prefAggregatedDisplayTagsLbl;
    }

    public String getPrefAggregatedDisplayColorCodedLbl() {
        return prefAggregatedDisplayColorCodedLbl;
    }

    public String getPrefAggregatedLegendLbl() {
        return prefAggregatedLegendLbl;
    }

    public String[] getPrefAggregatedLegendHeaders() {
        return prefAggregatedLegendHeader;
    }

    public String getPregAggregatedLegendHeaderColor() {
        return pregAggregatedLegendHeaderColor;
    }

    public String getPrefTrackLbl() {
        return prefTrackLbl;
    }

    public String getPrefAutoplayLbl() {
        return prefAutoplayLbl;
    }

    public String getPrefAutoplayLengthLbl() {
        return prefAutoplayLengthLbl;
    }

    public String getPrefAutoplayDelayLbl() {
        return prefAutoplayDelayLbl;
    }

    public String getPrefCacheLbl() {
        return prefCacheLbl;
    }

    public String getPrefMemoryLbl() {
        return prefMemoryLbl;
    }

    public String getPrefDiskLbl() {
        return prefDiskLbl;
    }

    public String getPrefPrevNextLbl() {
        return prefPrevNextLbl;
    }

    public String getPrefNearbyLbl() {
        return prefNearbyLbl;
    }

    public String getBtnPreviousTlt() {
        return btnPreviousTlt;
    }

    public String getBtnNextTlt() {
        return btnNextTlt;
    }

    public String getBtnPlayTlt() {
        return btnPlayTlt;
    }

    public String getBtnStopTlt() {
        return btnStopTlt;
    }

    public String getBtnLocationTlt() {
        return btnLocationTlt;
    }

    public String getBtnWebPageTlt() {
        return btnWebPageTlt;
    }

    public String getBtnDataSwitchImageTlt() {
        return btnDataSwitchImageTlt;
    }

    public String getBtnDataSwitchSegmentTlt() {
        return btnDataSwitchSegmentTlt;
    }

    public String getDlgFilterTitle() {
        return dlgFilterTitle;
    }

    public String getDlgFilterDateLbl() {
        return dlgFilterDateLbl;
    }

    public String getDlgFilterUserLbl() {
        return dlgFilterUserLbl;
    }

    public String getDlgFilterLoginWarningLbl() {
        return dlgFilterLoginWarningLbl;
    }

    public String getDlgFilterDetectionLbl() {
        return dlgFilterDetectionLbl;
    }

    public String getDlgFilterDataTypeLbl() {
        return dlgFilterDataTypeLbl;
    }

    public String getDlgFilterOsmComparisonLbl() {
        return dlgFilterOsmComparisonLbl;
    }

    public String getDlgFilterConfidenceLbl() {
        return dlgFilterConfidenceLbl;
    }

    public String getDlgFilterConfidenceMinLbl() {
        return dlgFilterConfidenceMinLbl;
    }

    public String getDlgFilterConfidenceMaxLbl() {
        return dlgFilterConfidenceMaxLbl;
    }

    public String getDlgFilterModeLbl() {
        return dlgFilterModeLbl;
    }

    public String getDlgFilterEditStatusLbl() {
        return dlgFilterEditStatusLbl;
    }

    public String getDlgFilterDetectionTypeLbl() {
        return dlgFilterDetectionTypeLbl;
    }

    public String getDlgFilterDataRegionLbl() {
        return dlgFilterDataRegionLbl;
    }

    public String getDlgFilterSearchDataLbl() {
        return dlgFilterSearchDataLbl;
    }

    public String getBtnOkLbl() {
        return btnOkLbl;
    }

    public String getBtnClearLbl() {
        return btnClearLbl;
    }

    public String getBtnCancelLbl() {
        return btnCancelLbl;
    }

    public String getBtnSelectLbl() {
        return btnSelectLbl;
    }

    public String getBtnResetLbl() {
        return btnResetLbl;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getWarningTitle() {
        return warningTitle;
    }

    public String getErrorPhotoListText() {
        return errorPhotoListText;
    }

    public String getErrorSequenceText() {
        return errorSequenceText;
    }

    public String getErrorPhotoLoadingText() {
        return errorPhotoLoadingText;
    }

    public String getErrorPhotoPageText() {
        return errorPhotoPageText;
    }

    public String getErrorUserPageText() {
        return errorUserPageText;
    }

    public String getErrorFeedbackPageText() {
        return errorFeedbackPageText;
    }

    public String getErrorDetectionRetrieveText() {
        return errorDetectionRetrieveText;
    }

    public String getErrorDetectionUpdateText() {
        return errorDetectionUpdateText;
    }

    public String getErrorClusterRetrieveText() {
        return errorClusterRetrieveText;
    }

    public String getErrorListSignsText() {
        return errorListSignsText;
    }

    public String getErrorListRegionsText() {
        return errorListRegionsText;
    }

    public String getIncorrectDateFilterText() {
        return incorrectDateFilterText;
    }

    public String getUnacceptedDateFilterText() {
        return unacceptedDateFilterText;
    }

    public String getIncorrectConfidenceFilterText() {
        return incorrectConfidenceFilterText;
    }

    public String getUnacceptedConfidenceFilterText() {
        return unacceptedConfidenceFilterText;
    }

    public String getErrorSequenceSaveText() {
        return errorSequenceSaveText;
    }

    public String getErrorSegmentListText() {
        return errorSegmentListText;
    }

    public String getErrorDownloadOsmData() {
        return errorDownloadOsmData;
    }

    public String getWarningHighQualityPhoto() {
        return warningHighQualityPhoto;
    }

    public String getWarningLoadingPhoto() {
        return warningLoadingPhoto;
    }

    public String getLayerDeleteMenuItemLbl() {
        return layerDeleteMenuItemLbl;
    }

    public String getLayerDeleteMenuItemTlt() {
        return layerDeleteMenuItemTlt;
    }

    public String getLayerFeedbackMenuItemLbl() {
        return layerFeedbackMenuItemLbl;
    }

    public String getLayerPreviousMenuItemLbl() {
        return layerPreviousMenuItemLbl;
    }

    public String getLayerNextMenuItemLbl() {
        return layerNextMenuItemLbl;
    }

    public String getPluginShortcutText() {
        return pluginShortcutText;
    }

    public String getPluginShortcutLongText() {
        return pluginShortcutLongText;
    }

    public String getBtnPreviousShortcutText() {
        return btnPreviousShortcutText;
    }

    public String getBtnNextShortcutText() {
        return btnNextShortcutText;
    }

    public String getBtnClosestTlt() {
        return btnClosestTlt;
    }

    public String getBtnClosestShortcutText() {
        return btnClosestShortcutText;
    }

    public String getBtnPlayShortcutText() {
        return btnPlayShortcutText;
    }

    public String getBtnStopShortcutText() {
        return btnStopShortcutText;
    }

    public String getBtnLocationShortcutText() {
        return btnLocationShortcutText;
    }

    public String getBtnWebPageShortcutTlt() {
        return btnWebPageShortcutTlt;
    }

    public String getBtnDataSwitchShortcutTlt() {
        return btnDataSwitchShortcutTlt;
    }

    public String getDlgFilterShortcutText() {
        return dlgFilterShortcutText;
    }

    public String getLayerFeedbackShortcutText() {
        return layerFeedbackShortcutText;
    }

    public String getBtnMatchedWayTlt() {
        return btnMatchedWayTlt;
    }

    public String getBtnMatchedWayShortcutTlt() {
        return btnMatchedWayShortcutTlt;
    }

    public String getInfoMatchedWayTitle() {
        return infoMatchedWayTitle;
    }

    public String getInfoDownloadNextPhotosTitle() {
        return infoDownloadNextPhotosTitle;
    }

    public String getInfoDownloadPreviousPhotosTitle() {
        return infoDownloadPreviousPhotosTitle;
    }

    public String getLayerPreferenceMenuItemLbl() {
        return layerPreferenceMenuItemLbl;
    }

    public String getLayerSaveSequenceMenuItemLbl() {
        return layerSaveSequenceMenuItemLbl;
    }

    public String getInfoFileExistsTitle() {
        return infoFileExistsTitle;
    }

    public String getInfoFileExistsText() {
        return infoFileExistsText;
    }

    public String getGpxTrackDescription() {
        return gpxTrackDescription;
    }

    public String getBtnFixDetectionShortcutText() {
        return btnFixDetectionShortcutText;
    }

    public String getBtnFixDetectionTlt() {
        return btnFixDetectionTlt;
    }

    public String getBtnOtherActionOnDetectionShortcutText() {
        return btnOtherActionOnDetectionShortcutText;
    }

    public String getBtnOtherActionOnDetectionTlt() {
        return btnOtherActionOnDetectionTlt;
    }

    public String getBtnClusterNextShortcutText() {
        return btnClusterNextShortcutText;
    }

    public String getBtnClusterPreviousShortcutText() {
        return btnClusterPreviousShortcutText;
    }

    public String getDetectedDetectionText() {
        return detectedDetectionText;
    }

    public String getDetectionOnOsmText() {
        return detectionOnOsmText;
    }

    public String getDetectionModeText() {
        return detectionModeText;
    }

    public String getDetectionValidationStatusText() {
        return detectionValidationStatusText;
    }

    public String getDetectionChangedValidationStatusText() {
        return detectionChangedValidationStatusText;
    }

    public String getDetectionConfirmedValidationStatusText() {
        return detectionConfirmedValidationStatusText;
    }

    public String getDetectionRemovedValidationStatusText() {
        return detectionRemovedValidationStatusText;
    }

    public String getDetectionToBeCheckedValidationStatusText() {
        return detectionToBeCheckedValidationStatusText;
    }

    public String getDetectionManualModeText() {
        return detectionManualModeText;
    }

    public String getDetectionAutomaticModeText() {
        return detectionAutomaticModeText;
    }

    public String getDetectionNewOnOsmText() {
        return detectionNewOnOsmText;
    }

    public String getDetectionChangedOnOsmText() {
        return detectionChangedOnOsmText;
    }

    public String getDetectionSameOnOsmText() {
        return detectionSameOnOsmText;
    }

    public String getDetectionUnknownOnOsmText() {
        return detectionUnknownOnOsmText;
    }

    public String getBtnCouldntFixDetection() {
        return btnCouldntFixDetection;
    }

    public String getBtnBadDetection() {
        return btnBadDetection;
    }

    public String getBtnBadDetectionShortcutText() {
        return btnBadDetectionShortcutText;
    }

    public String getBtnBadDetectionTlt() {
        return btnBadDetectionTlt;
    }

    public String getBtnOtherActionOnDetection() {
        return btnOtherActionOnDetection;
    }

    public String getBtnAlreadyFixedDetectionShortcutText() {
        return btnAlreadyFixedDetectionShortcutText;
    }

    public String getBtnAlreadyFixedDetectionTlt() {
        return btnAlreadyFixedDetectionTlt;
    }

    public String getDetectionDialogTitleName() {
        return detectionDialogTitleName;
    }

    public String getDetectionTaskStatusText() {
        return detectionTaskStatusText;
    }

    public String getAuthenticationNeededErrorMessage() {
        return authenticationNeededErrorMessage;
    }

    public String getDetectionCreatedDate() {
        return detectionCreatedDate;
    }

    public String getDetectionUpdatedDate() {
        return detectionUpdatedDate;
    }

    public String getDetectionEditStatusMappedText() {
        return detectionEditStatusMappedText;
    }

    public String getDlgFilterDataTypeImageTxt() {
        return dlgFilterDataTypeImageTxt;
    }

    public String getDlgFilterDataTypeDetectionsTxt() {
        return dlgFilterDataTypeDetectionsTxt;
    }

    public String getDlgFilterDataTypeAggregatedDetectionsTxt() {
        return dlgFilterDataTypeAggregatedDetectionsTxt;
    }

    public String getDialogAddCommentText() {
        return dialogAddCommentText;
    }

    public String getClusterDetectionsLbl() {
        return clusterDetectionsLbl;
    }

    public String getClusterDialogTitleName() {
        return clusterDialogTitleName;
    }

    public String getDetectionFacingText() {
        return detectionFacingText;
    }

    public String getClusterOcrValueLbl() {
        return clusterOcrValueLbl;
    }

    public String getDetectionIdLbl() {
        return detectionIdLbl;
    }

    public String getDetectionConfidenceLbl() {
        return detectionConfidenceLbl;
    }

    public String getFacingConfidenceLbl() {
        return facingConfidenceLbl;
    }

    public String getPositioningConfidenceLbl() {
        return positioningConfidenceLbl;
    }

    public String getKeypointsConfidenceLbl() {
        return keypointsConfidenceLbl;
    }

    public String getTrackingConfidenceLbl() {
        return trackingConfidenceLbl;
    }

    public String getOcrConfidenceLbl() {
        return ocrConfidenceLbl;
    }

    public String getDetectionTrackingIdLbl() {
        return detectionTrackingIdLbl;
    }

    public String getDetectionAutomaticOcrValueLbl() {
        return detectionAutomaticOcrValueLbl;
    }

    public String getClusterLaneCountText() {
        return clusterLaneCountText;
    }

    public String getClusterConfidenceLevelText() {
        return clusterConfidenceLevelText;
    }

    public String[] getClusterTableHeader() {
        return clusterTableHeader;
    }

    public String getBtnMatchedData() {
        return btnMatchedData;
    }

    public String getBtnMatchedDataShortcutText() {
        return btnMatchedDataShortcutText;
    }

    public String getBtnMatchedDataTlt() {
        return btnMatchedDataTlt;
    }

    public String getNextRowSelection() {
        return nextRowSelection;
    }

    public String getPrevRowSelection() {
        return prevRowSelection;
    }

    public String getDetectionManualOcrValueLbl() {
        return detectionManualOcrValueLbl;
    }
}