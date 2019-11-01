/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;


public class SignEntry {
    // preference entities must be declared public, otherwise JOSM preference loading does not work!

    @StructUtils.StructEntry
    private String name;
    @StructUtils.StructEntry
    private String internalName;
    @StructUtils.StructEntry
    private String iconName;
    @StructUtils.StructEntry
    private String region;
    @StructUtils.StructEntry
    private String type;

    public SignEntry() {
    }

    public SignEntry(final Sign sign) {
        this.name = sign.getName();
        this.internalName = sign.getInternalName();
        this.iconName = sign.getIconName();
        this.region = sign.getRegion();
        this.type = sign.getType();
    }

    public Sign getSign() {
        return new Sign(name, internalName, iconName, region, type);
    }
}