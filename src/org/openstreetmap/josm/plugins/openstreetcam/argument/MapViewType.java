/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;


/**
 * Defines the data types that can be displayed on the map.
 *
 * @author beataj
 * @version $Revision$
 */
public enum MapViewType {

    COVERAGE, ELEMENT;

    public static MapViewType getDataType(final String value) {
        return COVERAGE.name().equals(value) ? COVERAGE : ELEMENT.name().equals(value) ? ELEMENT : null;
    }
}