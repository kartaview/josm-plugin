/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.argument;


/**
 * Defines the user configurable auto-play settings.
 *
 * @author beataj
 * @version $Revision$
 */
public class AutoplaySettings {

    private final Integer length;
    private final Integer delay;


    /**
     * Builds a new object with the given arguments.
     *
     * @param length the length of the track that should be auto-played
     * @param delay the number of milliseconds to wait between displaying track photos
     */
    public AutoplaySettings(final Integer length, final Integer delay) {
        this.length = length;
        this.delay = delay;
    }

    public Integer getLength() {
        return length;
    }

    public Integer getDelay() {
        return delay;
    }
}