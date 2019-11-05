/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
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