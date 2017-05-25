/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;


/**
 * Defines the data types that can be displayed on the map.
 *
 * @author beataj
 * @version $Revision$
 */
public enum DataType {
    SEGMENT, PHOTO;

    public static DataType getDataType(final String value) {
        return SEGMENT.name().equals(value) ? SEGMENT : PHOTO.name().equals(value) ? PHOTO : null;
    }
}