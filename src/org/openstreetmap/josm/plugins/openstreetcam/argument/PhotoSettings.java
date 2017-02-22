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
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoSettings {

    private final boolean highQualityFlag;
    private final boolean displayTrackFlag;


    public PhotoSettings(final boolean highQualityFlag, final boolean displayTrackFlag) {
        this.highQualityFlag = highQualityFlag;
        this.displayTrackFlag = displayTrackFlag;
    }


    public boolean isHighQualityFlag() {
        return highQualityFlag;
    }

    public boolean isDisplayTrackFlag() {
        return displayTrackFlag;
    }
}