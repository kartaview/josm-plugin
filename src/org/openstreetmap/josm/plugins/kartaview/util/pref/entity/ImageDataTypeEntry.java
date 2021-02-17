/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils.StructEntry;
import org.openstreetmap.josm.plugins.kartaview.argument.DataType;


/**
 *
 * @author ioanao
 * @version $Revision$
 */
public class ImageDataTypeEntry {

    @StructEntry
    private String name;

    public ImageDataTypeEntry() {}


    public ImageDataTypeEntry(final DataType type) {
        this.name = type.name();
    }

    public String getName() {
        return name;
    }
}