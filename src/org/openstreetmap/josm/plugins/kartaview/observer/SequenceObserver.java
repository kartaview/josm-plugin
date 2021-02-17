/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;


/**
 * Observes the 'Next Photo/Previous Photo' user click action. If the user clicks on the 'Next Photo/Previous Photo'
 * buttons then the previous/next photo needs to be displayed and the map needs to be centered around the selected
 * element.
 *
 * @author beataj
 * @version $Revision$
 */
public interface SequenceObserver {

    /**
     * Selects the photo that is located at the given index in the selected sequence.
     *
     * @param index the photo index
     */
    void selectSequencePhoto(int index);
}