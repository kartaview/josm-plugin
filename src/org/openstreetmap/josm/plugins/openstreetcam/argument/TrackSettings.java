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
 * Defines the track display user configurable settings.
 *
 * @author beataj
 * @version $Revision$
 */
public class TrackSettings {

    private final boolean displayTrack;
    private final AutoplaySettings autoplaySettings;


    /**
     * Builds a new object with the given arguments.
     *
     * @param displayTrack
     * @param autoplaySettings
     */
    public TrackSettings(final boolean displayTrack, final AutoplaySettings autoplaySettings) {
        this.displayTrack = displayTrack;
        this.autoplaySettings = autoplaySettings;
    }

    public boolean isDisplayTrack() {
        return displayTrack;
    }

    public AutoplaySettings getAutoplaySettings() {
        return autoplaySettings;
    }
}