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

    private final Double x;
    private final Double y;
    private final Double z;
    private final Double w;

    public UprightOrientation(final Double x, final Double y, final Double z, final Double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    public Double getW() {
        return w;
    }
}