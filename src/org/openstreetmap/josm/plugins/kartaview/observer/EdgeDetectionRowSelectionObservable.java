/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;


/**
 * The observable interface for the {@code EdgeDetectionRowSelectionObserver}.
 *
 * @author maria.mitisor
 */
public interface EdgeDetectionRowSelectionObservable {

    /**
     * Registers the given observer.
     *
     * @param observer an {@code EdgeDetectionRowSelectionObserver}
     */
    void registerObserver(final EdgeDetectionRowSelectionObserver observer);

    /**
     * Notifies the observers listening.
     *
     * @param edgeDetection corresponding to the selected row
     */
    void notifyRowSelectionObserver(final EdgeDetection edgeDetection);
}