/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;


/**
 * Observes the "Next Photo/Previous Photo" user click action. If the user clicks on the next/previous photo action the
 * next/previous photo belonging to a selected cluster relative to the selected photo is selected.
 *
 * @author beataj
 * @version $Revision$
 */
public interface ClusterObserver {

    /**
     * Selects a photo belonging to a selected cluster. The photo to be selected is computed relative to the currently
     * selected photo.
     *
     * @param isNext specifies if the next or previous photo relative to the selected photo is selected
     */
    void selectPhoto(boolean isNext);
}