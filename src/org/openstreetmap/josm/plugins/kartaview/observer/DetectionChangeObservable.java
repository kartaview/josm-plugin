/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;

/**
 * The observable interface for the {@code DetectionChangeObserver}.
 *
 * @author ioanao
 * @version $Revision$
 */
public interface DetectionChangeObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code DetectionChangeObserver}
     */
    void registerObserver(final DetectionChangeObserver observer);

    /**
     * Notifies the observers listening.
     *
     * @param status the new status to be set
     * @param text an addition comment to be added to a detection
     */
    void notifyDetectionChangeObserver(final EditStatus status, final String text);
}