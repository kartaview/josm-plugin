/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;


/**
 * Defines the user configurable photo settings.
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoSettings {

    private final boolean highQualityFlag;
    private final boolean mouseHoverFlag;
    private final int mouseHoverDelay;
    private final boolean displayFrontFacingFlag;


    /**
     * Builds a new object with the given arguments.
     *
     * @param highQualityFlag specifies if the high quality should e loaded or not
     * @param mouseHoverFlag specifies if photos should be loaded on mouse hover event
     * @param mouseHoverDelay specifies the delay between selecting a photo on mouse hover event
     */
    public PhotoSettings(final boolean highQualityFlag, final boolean mouseHoverFlag, final int mouseHoverDelay,
            final boolean displayFrontFacingFlag) {
        this.highQualityFlag = highQualityFlag;
        this.mouseHoverFlag = mouseHoverFlag;
        this.mouseHoverDelay = mouseHoverDelay;
        this.displayFrontFacingFlag = displayFrontFacingFlag;
    }


    public boolean isHighQualityFlag() {
        return highQualityFlag;
    }

    public boolean isMouseHoverFlag() {
        return mouseHoverFlag;
    }

    public int getMouseHoverDelay() {
        return mouseHoverDelay;
    }

    public boolean isDisplayFrontFacingFlag() {
        return displayFrontFacingFlag;
    }
}