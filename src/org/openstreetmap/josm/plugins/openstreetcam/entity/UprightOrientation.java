/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

/**
 * @author nicoleta.viregan
 */
public class UprightOrientation {
    private final Long x;
    private final Long y;
    private final Long z;
    private final Long w;

    public UprightOrientation(final Long x, final Long y, final Long z, final Long w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Long getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    public Long getZ() {
        return z;
    }

    public Long getW() {
        return w;
    }
}