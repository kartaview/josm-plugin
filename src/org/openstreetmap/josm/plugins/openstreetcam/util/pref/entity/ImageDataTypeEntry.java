/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils.StructEntry;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ImageDataType;


/**
*
* @author ioanao
* @version $Revision$
*/
public class ImageDataTypeEntry {

    @StructEntry
    private String name;

    public ImageDataTypeEntry() {}


    public ImageDataTypeEntry(final ImageDataType type) {
        this.name = type.name();
    }

    public String getName() {
        return name;
    }
}