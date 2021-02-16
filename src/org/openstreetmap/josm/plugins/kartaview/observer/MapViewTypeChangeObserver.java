/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.argument.MapViewType;


/**
 * Observers the 'data type change'user click action. If the user selected 'manual data switch' option from preferences
 * and clicks on the 'data switch' button then, if previously:
 * <ul>
 * <li>segments were displayed, the map view should display photo locations</li>
 * <li>photo locations were displayed, the map view should display segments</li>
 * </ul>
 *
 * @author beataj
 * @version $Revision$
 */
public interface MapViewTypeChangeObserver {

    /**
     * Updates the map view with the given data type.
     *
     * @param mapViewType the new {@code MapViewType} that will be displayed
     */
    void update(MapViewType mapViewType);
}