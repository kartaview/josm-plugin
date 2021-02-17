/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.observer.LocationObserver;


/**
 * The observable interface for the {@code LocationObserver} object.
 *
 * @author Beata
 * @version $Revision$
 */
public interface LocationObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code LocationObserver}
     */
    void registerObserver(LocationObserver observer);

    /**
     * Notifies the observers listening.
     */
    void notifyObserver();
}