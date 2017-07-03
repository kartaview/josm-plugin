/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the attributes of a circle. A circle represents a searching map area and it is represented by a center
 * coordinate and radius.
 *
 * @author Beata
 * @version $Revision$
 */
public class Circle {

    private static final int BUFFER = 4;
    private final LatLon center;
    private final int radius;


    /**
     * Builds a new circle from the given bounds.
     *
     * @param bounds a {@code Bounds} represents the JOSM map view bounds
     */
    public Circle(final Bounds bounds) {
        this.center = bounds.toBBox().getCenter();
        int distance = (int) this.center.greatCircleDistance(bounds.toBBox().getBottomRight());

        // add a buffer to the circle, otherwise elements will be centered in the middle
        distance += distance / BUFFER;

        if (distance > Config.getInstance().getNearbyPhotosMaxRadius()) {
            this.radius = Config.getInstance().getNearbyPhotosMaxRadius();
        } else {
            this.radius = distance < Config.getInstance().getNearbyPhotosMinRadius()
                    ? Config.getInstance().getNearbyPhotosMinRadius() : distance;
        }
    }

    public LatLon getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(radius);
        result = prime * result + EntityUtil.hashCode(center);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final Circle other = (Circle) obj;
            result = EntityUtil.bothNullOrEqual(radius, other.getRadius());
            result = result && EntityUtil.bothNullOrEqual(center, other.getCenter());
        }
        return result;
    }

    @Override
    public String toString() {
        return "center:" + center.toDisplayString() + " radius:" + radius;
    }
}