/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;


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