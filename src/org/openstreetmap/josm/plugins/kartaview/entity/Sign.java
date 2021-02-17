/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;


import java.util.Objects;


/**
 * Defines the detection entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Sign {

    private final String name;
    private final String internalName;
    private final String iconName;
    private final String region;
    private final String type;


    public Sign(final String name, final String internalName, final String iconName, final String region,
            final String type) {
        this.name = name;
        this.internalName = internalName;
        this.iconName = iconName;
        this.region = region;
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getIconName() {
        return iconName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Sign sign = (Sign) o;
        return Objects.equals(name, sign.name) && Objects.equals(internalName, sign.internalName)
                && Objects.equals(iconName, sign.iconName) && Objects.equals(region, sign.region)
                && Objects.equals(type, sign.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, internalName, iconName, region, type);
    }
}