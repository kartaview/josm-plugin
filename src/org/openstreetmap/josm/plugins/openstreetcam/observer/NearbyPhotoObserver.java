/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;


/**
 * Observes the closest image user click action.
 *
 * @author ioanao
 * @version $Revision$
 */
public interface NearbyPhotoObserver {

    /**
     * Selects the nearby photo to the selected photo.
     */
    void selectNearbyPhoto();
}