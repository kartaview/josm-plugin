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
package org.openstreetmap.josm.plugins.openstreetview.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Loads GUI related texts.
 *
 * @author Beata
 * @version $Revision$
 */
public final class GuiConfig extends BaseConfig {

    private static final String CONFIG_FILE = "openstreetview_gui.properties";
    private static final GuiConfig INSTANCE = new GuiConfig();

    private final String pluginShortName;
    private final String pluginLongName;
    private final String pluginTlt;

    private final String btnFilterTlt;
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
    private final String errorPhotoLoadingTxt;
    private final String errorPhotoPageTxt;
    private final String errorFeedbackPageTxt;
    private final String errorDateFilterTxt;

    private final String confirmDateFilterTitle;
    private final String confirmDateFilterTxt;


    private GuiConfig() {
        super(CONFIG_FILE);

        pluginShortName = readProperty("plugin.name.short");
        pluginLongName = readProperty("plugin.name.long");
        pluginTlt = readProperty("plugin.tlt");
        btnFilterTlt = readProperty("btn.filter.tlt");
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
        errorPhotoLoadingTxt = readProperty("error.photo.loading");
        errorPhotoPageTxt = readProperty("error.photo.page");
        errorFeedbackPageTxt = readProperty("error.feedback.page");
        errorDateFilterTxt = readProperty("error.dateFilter");

        confirmDateFilterTitle = readProperty("information.dateFilter.title");
        confirmDateFilterTxt = readProperty("information.dateFilter.text");
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

    public String getBtnFilterTlt() {
        return btnFilterTlt;
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

    public String getErrorDateFilterTxt() {
        return errorDateFilterTxt;
    }

    public String getConfirmDateFilterTitle() {
        return confirmDateFilterTitle;
    }

    public String getConfirmDateFilterTxt() {
        return confirmDateFilterTxt;
    }
}