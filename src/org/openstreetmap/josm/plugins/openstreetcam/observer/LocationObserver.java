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
 * Observes the location user click action. If the user clicks on the location button, then the map needs to be zoomed
 * to the selected photo's location.
 *
 * @author Beata
 * @version $Revision$
 */
public interface LocationObserver {

    /**
     * Zooms the map to the selected photo's location.
     */
    void zoomToSelectedPhoto();
}