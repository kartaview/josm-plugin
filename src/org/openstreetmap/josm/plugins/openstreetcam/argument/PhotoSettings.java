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
 * Defines the user configurable photo settings.
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoSettings {

    private final boolean highQualityFlag;
    private final boolean displayTrackFlag;
    private final boolean mouseHoverFlag;
    private final int mouseHoverDelay;


    /**
     * Builds a new object with the given arguments.
     *
     * @param highQualityFlag specifies if the high quality should e loaded or not
     * @param displayTrackFlag specifies if also the track associated with the selected photo should be displayed
     * @param mouseHoverFlag specifies if photos should be loaded on mouse hover event
     * @param mouseHoverDelay specifies the delay between selecting a photo on mouse hover event
     */
    public PhotoSettings(final boolean highQualityFlag, final boolean displayTrackFlag, final boolean mouseHoverFlag,
            final int mouseHoverDelay) {
        this.highQualityFlag = highQualityFlag;
        this.displayTrackFlag = displayTrackFlag;
        this.mouseHoverFlag = mouseHoverFlag;
        this.mouseHoverDelay = mouseHoverDelay;
    }


    public boolean isHighQualityFlag() {
        return highQualityFlag;
    }

    public boolean isDisplayTrackFlag() {
        return displayTrackFlag;
    }

    public boolean isMouseHoverFlag() {
        return mouseHoverFlag;
    }

    public int getMouseHoverDelay() {
        return mouseHoverDelay;
    }
}