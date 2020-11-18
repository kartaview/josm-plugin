/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;


/**
 * @author nicoleta.viregan
 */

public class DetectionShape {

    public static final int FIRST_POINT_INDEX = 0;

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

    public boolean isPointInEquirectangularPolygon(final Point2D point) {
        final List<PixelPoint> polygonPoints = (List<PixelPoint>) equirectangularPolygon;
        final Path2D path = new Path2D.Double();
        path.moveTo(polygonPoints.get(FIRST_POINT_INDEX).getX(), polygonPoints.get(FIRST_POINT_INDEX).getY());
        for (int i = 1; i < polygonPoints.size(); ++i) {
            path.lineTo(polygonPoints.get(i).getX(), polygonPoints.get(i).getY());
        }

        return path.contains(point);
    }
}