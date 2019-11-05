/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;


/**
 * Interface that describes the functionality of a class responsible of handling the detection selection event.
 *
 *
 * @author ioanao
 * @version $Revision$
 */
public interface DetectionSelectionObserver {

    /**
     * Handle detection selection event.
     *
     * @param detection the selected detection
     */
    void selectPhotoDetection(final Detection detection);
}