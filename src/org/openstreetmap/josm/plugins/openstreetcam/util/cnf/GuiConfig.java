/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
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

    private final String prefMapViewLbl;
    private final String prefPhotoZoomLbl;
    private final String prefManualSwitchLbl;
    private final String prefImageLbl;
    private final String prefImageHighQualityLbl;
    private final String prefDisplayTrackLbl;
    private final String prefMouseHoverLbl;
    private final String prefMouseHoverDelayLbl;
    private final String prefCacheLbl;
    private final String prefMemoryLbl;
    private final String prefDiskLbl;
    private final String prefPrevNextLbl;
    private final String prefNearbyLbl;

    private final String btnPreviousTlt;
    private final String btnNextTlt;
    private final String btnLocationTlt;
    private final String btnWebPageTlt;
    private final String btnClosestImageTlt;
    private final String btnDataSwitchImageTlt;
    private final String btnDataSwitchSegmentTlt;

    private final String dlgFilterTitle;
    private final String dlgFilterDateLbl;
    private final String dlgFilterUserLbl;
    private final String dlgFilterLoginWarningLbl;
    private final String btnOkLbl;
    private final String btnClearLbl;
    private final String btnCancelLbl;

    private final String errorTitle;
    private final String errorPhotoListTxt;
    private final String errorSequenceTxt;
    private final String errorPhotoLoadingTxt;
    private final String errorPhotoPageTxt;
    private final String errorUserPageTxt;
    private final String errorFeedbackPageTxt;
    private final String incorrectDateFilterTxt;
    private final String unacceptedDateFilterTxt;

    private final String warningHighQualityPhoto;
    private final String warningLoadingPhoto;

    private final String confirmDateFilterTitle;
    private final String confirmDateFilterTxt;

    private final String layerDeleteMenuItemLbl;
    private final String layerDeleteMenuItemTlt;
    private final String layerFeedbackMenuItemLbl;


    private GuiConfig() {
        super(CONFIG_FILE);

        pluginShortName = readProperty("plugin.name.short");
        pluginLongName = readProperty("plugin.name.long");
        pluginTlt = readProperty("plugin.tlt");

        prefMapViewLbl = readProperty("preferences.mapView.lbl");
        prefPhotoZoomLbl = readProperty("preferences.mapView.zoom.lbl");
        prefManualSwitchLbl = readProperty("preferences.mapView.switch.lbl");
        prefImageLbl = readProperty("preference.photo.lbl");
        prefImageHighQualityLbl = readProperty("preference.photo.highQuality.lbl");
        prefDisplayTrackLbl = readProperty("preference.photo.displayTrack.lbl");
        prefMouseHoverLbl = readProperty("preference.photo.mouseHover.lbl");
        prefMouseHoverDelayLbl = readProperty("preference.photo.mouseHover.delay.lbl");
        prefCacheLbl = readProperty("preference.cache.lbl");
        prefMemoryLbl = readProperty("preference.cache.memory.lbl");
        prefDiskLbl = readProperty("preference.cache.disk.lbl");
        prefPrevNextLbl = readProperty("preference.cache.prevNext.lbl");
        prefNearbyLbl = readProperty("preference.cache.nearby.lbl");

        btnPreviousTlt = readProperty("btn.previous.tlt");
        btnNextTlt = readProperty("btn.next.tlt");
        btnLocationTlt = readProperty("btn.location.tlt");
        btnWebPageTlt = readProperty("btn.webPage.tlt");
        btnClosestImageTlt = readProperty("btn.closestImage.tlt");
        btnDataSwitchImageTlt = readProperty("btn.switch.image.tlt");
        btnDataSwitchSegmentTlt = readProperty("btn.switch.segment.tlt");

        dlgFilterTitle = readProperty("filter.title");
        dlgFilterDateLbl = readProperty("filter.date.lbl");
        dlgFilterUserLbl = readProperty("filter.user.lbl");
        dlgFilterLoginWarningLbl = readProperty("filter.login.warning.lbl");

        btnOkLbl = readProperty("btn.ok.lbl");
        btnClearLbl = readProperty("btn.clear.lbl");
        btnCancelLbl = readProperty("btn.cancel.lbl");

        errorTitle = readProperty("error.title");
        errorPhotoListTxt = readProperty("error.photo.list");
        errorSequenceTxt = readProperty("error.track");
        errorPhotoLoadingTxt = readProperty("error.photo.loading");
        errorPhotoPageTxt = readProperty("error.photo.page");
        errorUserPageTxt = readProperty("error.user.page");
        errorFeedbackPageTxt = readProperty("error.feedback.page");
        unacceptedDateFilterTxt = readProperty("error.dateFilter.unaccepted");
        incorrectDateFilterTxt = readProperty("error.dateFilter.incorrect");

        warningHighQualityPhoto = readProperty("warning.photo.highQuality");
        warningLoadingPhoto = readProperty("warning.photo.loading");

        confirmDateFilterTitle = readProperty("information.dateFilter.title");
        confirmDateFilterTxt = readProperty("information.dateFilter.text");

        layerDeleteMenuItemLbl = readProperty("layer.menu.delete.lbl");
        layerDeleteMenuItemTlt = readProperty("layer.menu.delete.tlt");
        layerFeedbackMenuItemLbl = readProperty("layer.menu.feedback.lbl");
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

    public String getBtnLocationTlt() {
        return btnLocationTlt;
    }

    public String getBtnWebPageTlt() {
        return btnWebPageTlt;
    }

    public String getBtnClosestImageTlt() {
        return btnClosestImageTlt;
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

    public String getBtnOkLbl() {
        return btnOkLbl;
    }

    public String getBtnClearLbl() {
        return btnClearLbl;
    }

    public String getBtnCancelLbl() {
        return btnCancelLbl;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getErrorPhotoListTxt() {
        return errorPhotoListTxt;
    }

    public String getErrorSequenceTxt() {
        return errorSequenceTxt;
    }

    public String getErrorPhotoLoadingTxt() {
        return errorPhotoLoadingTxt;
    }

    public String getErrorPhotoPageTxt() {
        return errorPhotoPageTxt;
    }

    public String getErrorUserPageTxt() {
        return errorUserPageTxt;
    }

    public String getErrorFeedbackPageTxt() {
        return errorFeedbackPageTxt;
    }

    public String getIncorrectDateFilterTxt() {
        return incorrectDateFilterTxt;
    }

    public String getUnacceptedDateFilterTxt() {
        return unacceptedDateFilterTxt;
    }

    public String getWarningHighQualityPhoto() {
        return warningHighQualityPhoto;
    }

    public String getWarningLoadingPhoto() {
        return warningLoadingPhoto;
    }

    public String getConfirmDateFilterTitle() {
        return confirmDateFilterTitle;
    }

    public String getConfirmDateFilterTxt() {
        return confirmDateFilterTxt;
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
}