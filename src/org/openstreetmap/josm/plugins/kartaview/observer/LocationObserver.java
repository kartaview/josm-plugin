/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

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