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


    public GuiConfig() {
        super(CONFIG_FILE);

        pluginShortName = readProperty("plugin.name.short");
        pluginLongName = readProperty("plugin.name.long");
        pluginTlt = readProperty("plugin.tlt");
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
}