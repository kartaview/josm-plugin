/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.argument.MapViewType;


/**
 * The observable interface for the {@code DataTypeChangeObserver} object.
 *
 * @author beataj
 * @version $Revision$
 */
public interface MapViewTypeChangeObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code DataTypeChangeObserver}
     */
    void registerObserver(MapViewTypeChangeObserver observer);

    /**
     * Notifies the observer that is listening.
     *
     * @param mapViewType the new {@code MapViewType} that will be displayed
     */
    void notifyDataUpdateObserver(MapViewType mapViewType);
}