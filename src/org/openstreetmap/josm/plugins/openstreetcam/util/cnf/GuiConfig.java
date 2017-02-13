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

    private final String imagePrefLbl;
    private final String imageHighQualityPrefLbl;
    private final String displayTrackPrefLbl;

    private final String btnFilterTlt;
    private final String btnPreviousTlt;
    private final String btnNextTlt;
    private final String btnLocationTlt;
    private final String btnWebPageTlt;
    private final String btnFeedbackTlt;


    private final String dlgFilterTitle;
    private final String dlgFilterDateLbl;
    private final String dlgFilterUserLbl;
    private final String dlgFilterLoginWarning;
    private final String btnOkLbl;
    private final String btnClearLbl;
    private final String btnCancelLbl;

    private final String errorTitle;
    private final String errorPhotoListTxt;
    private final String errorSequenceTxt;
    private final String errorPhotoLoadingTxt;
    private final String errorPhotoPageTxt;
    private final String errorFeedbackPageTxt;
    private final String incorrectDateFilterTxt;
    private final String unacceptedDateFilterTxt;

    private final String warningHighQualityPhoto;
    private final String warningLoadingPhoto;

    private final String confirmDateFilterTitle;
    private final String confirmDateFilterTxt;

    private final String maxDateFilterTxt;

    private final String deleteLayerMenuItemTxt;
    private final String deleteLayerMenuItemShortDescription;
    private final String deleteLayerMenuItemImageDirectory;
    private final String deleteLayerMenuItemImageName;


    private GuiConfig() {
        super(CONFIG_FILE);

        pluginShortName = readProperty("plugin.name.short");
        pluginLongName = readProperty("plugin.name.long");
        pluginTlt = readProperty("plugin.tlt");

        imagePrefLbl = readProperty("preference.image.lbl");
        imageHighQualityPrefLbl = readProperty("preference.image.highQuality.lbl");
        displayTrackPrefLbl = readProperty("preference.image.displayTrack.lbl");


        btnFilterTlt = readProperty("btn.filter.tlt");
        btnPreviousTlt = readProperty("btn.previous");
        btnNextTlt = readProperty("btn.next");
        btnLocationTlt = readProperty("btn.location.tlt");
        btnWebPageTlt = readProperty("btn.webPage.tlt");
        btnFeedbackTlt = readProperty("btn.feedback.tlt");
        dlgFilterTitle = readProperty("filter.title");
        dlgFilterDateLbl = readProperty("filter.date");
        dlgFilterUserLbl = readProperty("filter.user");
        dlgFilterLoginWarning = readProperty("filter.login.warning");
        btnOkLbl = readProperty("btn.ok.lbl");
        btnClearLbl = readProperty("btn.clear.lbl");
        btnCancelLbl = readProperty("btn.cancel.lbl");

        errorTitle = readProperty("error.title");
        errorPhotoListTxt = readProperty("error.photo.list");
        errorSequenceTxt = readProperty("error.track");
        errorPhotoLoadingTxt = readProperty("error.photo.loading");
        errorPhotoPageTxt = readProperty("error.photo.page");
        errorFeedbackPageTxt = readProperty("error.feedback.page");
        unacceptedDateFilterTxt = readProperty("error.dateFilter.unaccepted");
        incorrectDateFilterTxt = readProperty("error.dateFilter.incorrect");

        warningHighQualityPhoto = readProperty("warning.photo.highQuality");
        warningLoadingPhoto = readProperty("warning.photo.loading");

        confirmDateFilterTitle = readProperty("information.dateFilter.title");
        confirmDateFilterTxt = readProperty("information.dateFilter.text");

        maxDateFilterTxt = readProperty("dateFilter.maxDate");

        deleteLayerMenuItemTxt = readProperty("layer.menu.delete.text");
        deleteLayerMenuItemShortDescription = readProperty("layer.menu.delete.shortDescription");
        deleteLayerMenuItemImageDirectory = readProperty("layer.menu.delete.image.directory");
        deleteLayerMenuItemImageName = readProperty("layer.menu.delete.image.name");
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

    public String getImagePrefLbl() {
        return imagePrefLbl;
    }

    public String getImageHighQualityPrefLbl() {
        return imageHighQualityPrefLbl;
    }

    public String getBtnFilterTlt() {
        return btnFilterTlt;
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

    public String getBtnFeedbackTlt() {
        return btnFeedbackTlt;
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

    public String getDlgFilterLoginWarning() {
        return dlgFilterLoginWarning;
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

    public String getErrorPhotoLoadingTxt() {
        return errorPhotoLoadingTxt;
    }

    public String getErrorPhotoPageTxt() {
        return errorPhotoPageTxt;
    }

    public String getErrorFeedbackPageTxt() {
        return errorFeedbackPageTxt;
    }

    public String getErrorPhotoListTxt() {
        return errorPhotoListTxt;
    }

    public String getErrorSequenceTxt() {
        return errorSequenceTxt;
    }

    public String getUnacceptedDateFilterTxt() {
        return unacceptedDateFilterTxt;
    }

    public String getIncorrectDateFilterTxt() {
        return incorrectDateFilterTxt;
    }

    public String getConfirmDateFilterTitle() {
        return confirmDateFilterTitle;
    }

    public String getConfirmDateFilterTxt() {
        return confirmDateFilterTxt;
    }

    public String getMaxDateFilterTxt() {
        return maxDateFilterTxt;
    }

    public String getDeleteLayerMenuItemTxt() {
        return deleteLayerMenuItemTxt;
    }

    public String getDeleteLayerMenuItemShortDescription() {
        return deleteLayerMenuItemShortDescription;
    }

    public String getDeleteLayerMenuItemImageDirectory() {
        return deleteLayerMenuItemImageDirectory;
    }

    public String getDeleteLayerMenuItemImageName() {
        return deleteLayerMenuItemImageName;
    }

    public String getWarningHighQualityPhoto() {
        return warningHighQualityPhoto;
    }
    
    public String getWarningLoadingPhoto() {
        return warningLoadingPhoto;
    }


    public String getDisplayTrackPrefLbl() {
        return displayTrackPrefLbl;
    }
}