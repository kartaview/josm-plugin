/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.entity.Detection;


/**
 * Interface that describes the functionality of a class responsible with sending notifications related to detection
 * selection.
 *
 * @author ioanao
 * @version $Revision$
 */
public interface DetectionSelectionObservable {

    /**
     * Registers the observer responsible of handling the notification.
     *
     * @param observer a {@code DetectionSelectionObserver}
     */
    void registerObserver(final DetectionSelectionObserver observer);

    /**
     * Notifies the observers entity when a detection is selected.
     *
     * @param detection the {@code Detection} to be selected
     */
    void notifyDetectionSelectionObserver(final Detection detection);
}