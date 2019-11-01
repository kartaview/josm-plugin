/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils.StructEntry;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class SignTypeEntry {

    // preference entities must be declared public, otherwise JOSM preference loading does not work!
    @StructEntry
    private String name;

    public SignTypeEntry() {}


    public SignTypeEntry(final String signType) {
        this.name = signType;
    }

    public String getName() {
        return name;
    }
}