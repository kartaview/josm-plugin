/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

/**
 * The observable interface for the {@code NearbyPhotoObserver}.
 *
 * @author ioanao
 * @version $Revision$
 */
public interface NearbyPhotoObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code ClosestImageObserver}
     */
    void registerObserver(NearbyPhotoObserver observer);

    /**
     * Notifies the observers listening.
     */
    void notifyNearbyPhotoObserver();
}