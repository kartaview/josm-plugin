/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;


/**
 * Defines the track display user configurable settings.
 *
 * @author beataj
 * @version $Revision$
 */
public class SequenceSettings {

    private final boolean displayTrack;
    private final AutoplaySettings autoplaySettings;


    /**
     * Builds a new object with the given arguments.
     *
     * @param displayTrack is true then when selecting an image also the track will be displayed
     * @param autoplaySettings the track auto-play settings
     */
    public SequenceSettings(final boolean displayTrack, final AutoplaySettings autoplaySettings) {
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