/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

/**
 * Observable interface for SwitchPhotoFormatObserver object.
 *
 * @author nicoleta.viregan
 */
public interface SwitchPhotoFormatObservable {

    /**
     * Register the observer.
     *
     * @param observer - SwitchPhotoFormatObserver object
     */
    void registerObserver(final SwitchPhotoFormatObserver observer);

    /**
     * Notifies the observer listening.
     */
    void notifySwitchPhotoFormatObserver();
}