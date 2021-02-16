/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;


/**
 * The observable interface for the {@code ClusterObserver} object.
 *
 * @author beataj
 * @version $Revision$
 */
public interface ClusterObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code ClusterObserver} to be registered
     */
    void registerObserver(ClusterObserver observer);

    /**
     * Notifies the observer listening.
     *
     * @param isNext specifies if the next or previous photo is selected
     */
    void notifyObserver(boolean isNext);
}