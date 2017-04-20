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
 * Defines the photo types supported by OpenStreetCam.
 *
 * @author beataj
 * @version $Revision$
 */
public enum PhotoType {
    /** small image */
    THUMBNAIL,

    /** normal sized image */
    LARGE_THUMBNAIL,

    /** high quality image */
    HIGH_QUALITY;


    /**
     * Returns the photo type based on the given user configured photo settings.
     *
     * @param settings a {@code PhotoSettings} represents the photo settings
     * @return a {@code PhotoType}
     */
    public static PhotoType getPhotoType(final PhotoSettings settings) {
        PhotoType result = LARGE_THUMBNAIL;
        if (settings.isMouseHoverFlag()) {
            result = THUMBNAIL;
        } else if (settings.isHighQualityFlag()) {
            result = HIGH_QUALITY;
        }
        return result;
    }
}