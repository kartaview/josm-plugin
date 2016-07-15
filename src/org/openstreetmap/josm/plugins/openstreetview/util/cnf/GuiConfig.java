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
public class GuiConfig extends BaseConfig {

    private static final String CONFIG_FILE = "openstreetview_gui.properties";
    private static final GuiConfig INSTANCE = new GuiConfig();

    private final String pluginShortName;
    private final String pluginLongName;
    private final String pluginTlt;

    private final String btnFilterTlt;
    private final String btnLocationTlt;
    private final String btnWebPageTlt;
    private final String btnFeedbackTlt;

    private final String photoErrorTxt;
    private final String dlgFilterTitle;
    private final String dlgFilterDateLbl;
    private final String dlgFilterUserLbl;
    private final String dlgFilterLoginWarning;
    private final String btnOkLbl;
    private final String btnClearLbl;
    private final String btnCancelLbl;


    public GuiConfig() {
        super(CONFIG_FILE);

        pluginShortName = readProperty("plugin.name.short");
        pluginLongName = readProperty("plugin.name.long");
        pluginTlt = readProperty("plugin.tlt");
        btnFilterTlt = readProperty("btn.filter.tlt");
        btnLocationTlt = readProperty("btn.location.tlt");
        btnWebPageTlt = readProperty("btn.webPage.tlt");
        btnFeedbackTlt = readProperty("btn.feedback.tlt");
        photoErrorTxt = readProperty("photo.error.txt");
        dlgFilterTitle = readProperty("filter.title");
        dlgFilterDateLbl = readProperty("filter.date");
        dlgFilterUserLbl = readProperty("filter.user");
        dlgFilterLoginWarning = readProperty("filter.login.warning");
        btnOkLbl = readProperty("btn.ok.lbl");
        btnClearLbl = readProperty("btn.clear.lbl");
        btnCancelLbl = readProperty("btn.cancel.lbl");
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

    public String getPhotoErrorTxt() {
        return photoErrorTxt;
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
}