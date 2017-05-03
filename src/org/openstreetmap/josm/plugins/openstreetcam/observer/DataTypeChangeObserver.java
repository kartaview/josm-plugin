/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;


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
public interface DataTypeChangeObserver {

    /**
     * Updates the map view with the given data type.
     *
     * @param dataType the new {@code DataType} that will be displayed
     */
    void update(DataType dataType);
}