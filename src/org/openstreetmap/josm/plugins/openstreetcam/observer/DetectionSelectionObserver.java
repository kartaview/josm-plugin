/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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