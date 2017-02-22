/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

/**
 * Defines the user configurable preference settings attributes. The plugin's preference settings can be modified by the
 * users via JOSM->Preferences->OSC preference settings.
 *
 * @author beataj
 * @version $Revision$
 */
public class PreferenceSettings {

    private final ImageSettings imageSettings;
    private final CacheSettings cacheSettings;


    public PreferenceSettings(final ImageSettings imageSettings, final CacheSettings cacheSettings) {
        this.imageSettings = imageSettings;
        this.cacheSettings = cacheSettings;
    }


    public ImageSettings getImageSettings() {
        return imageSettings;
    }

    public CacheSettings getCacheSettings() {
        return cacheSettings;
    }
}