/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.Collection;


/**
 * @author nicoleta.viregan
 */

public class DetectionShape {

    private final Collection<PixelPoint> planePolygon;
    private final Collection<PixelPoint> equirectangularPolygon;
    private final Collection<PixelPoint> spherePolygon;

    public DetectionShape(final Collection<PixelPoint> planePolygon,
            final Collection<PixelPoint> equirectangularPolygon, final Collection<PixelPoint> spherePolygon) {
        this.planePolygon = planePolygon;
        this.equirectangularPolygon = equirectangularPolygon;
        this.spherePolygon = spherePolygon;
    }

    public Collection<PixelPoint> getPlanePolygon() {
        return planePolygon;
    }

    public Collection<PixelPoint> getEquirectangularPolygon() {
        return equirectangularPolygon;
    }

    public Collection<PixelPoint> getSpherePolygon() {
        return spherePolygon;
    }
}