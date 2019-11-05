/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewType;


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