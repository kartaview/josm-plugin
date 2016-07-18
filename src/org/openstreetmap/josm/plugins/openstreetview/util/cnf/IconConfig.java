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
    private static final String CONFIG_FILE = "openstreetview_icon.properties";

    private final String pluginIconName;
    private final String dialogShortcutName;
    private final Icon layerIcon;
    private final ImageIcon photoIcon;
    private final ImageIcon photoSelectedIcon;
    private final ImageIcon filterIcon;
    private final Icon filterSelectedIcon;
    private final Icon feedbackIcon;
    private final Icon locationIcon;
    private final Icon webPageIcon;


    private IconConfig() {
        super(CONFIG_FILE);

        pluginIconName = readProperty("plugin.icon");
        layerIcon = ImageProvider.get(readProperty("layer.icon"));
        dialogShortcutName = readProperty("dialog.shortcut");
        photoIcon = ImageProvider.get(readProperty("photo.icon"));
        photoSelectedIcon = ImageProvider.get(readProperty("photo.sel.icon"));
        filterIcon = ImageProvider.get(readProperty("filter.icon"));
        filterSelectedIcon = ImageProvider.get(readProperty("filter.sel.icon"));
        locationIcon = ImageProvider.get(readProperty("location.icon"));
        feedbackIcon = ImageProvider.get(readProperty("feedback.icon"));
        webPageIcon = ImageProvider.get(readProperty("webPage.icon"));
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

    public Icon getLayerIcon() {
        return layerIcon;
    }

    public ImageIcon getPhotoIcon() {
        return photoIcon;
    }

    public ImageIcon getPhotoSelectedIcon() {
        return photoSelectedIcon;
    }

    public ImageIcon getFilterIcon() {
        return filterIcon;
    }

    public Icon getFilterSelectedIcon() {
        return filterSelectedIcon;
    }

    public Icon getLocationIcon() {
        return locationIcon;
    }

    public Icon getFeedbackIcon() {
        return feedbackIcon;
    }


    public Icon getWebPageIcon() {
        return webPageIcon;
    }
}