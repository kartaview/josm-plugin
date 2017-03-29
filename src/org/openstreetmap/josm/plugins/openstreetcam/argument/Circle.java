/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import com.telenav.josm.common.util.EntityUtil;


/**
 * Defines the attributes of a circle. A circle represents a searching map area and it is represented by a center
 * coordinate and radius.
 *
 * @author Beata
 * @version $Revision$
 */
public class Circle {

    private final LatLon center;
    private final int radius;


    /**
     * Builds a new circle from the given bounds.
     *
     * @param bounds a {@code Bounds} represents the JOSM map view bounds
     */
    public Circle(final Bounds bounds) {
        this.center = bounds.toBBox().getCenter();
        final int distance = (int) this.center.greatCircleDistance(bounds.toBBox().getBottomRight());

        if (distance > Config.getInstance().getNearbyPhotosMaxRadius()) {
            this.radius = Config.getInstance().getNearbyPhotosMaxRadius();
        } else {
            if (distance < Config.getInstance().getNearbyPhotosMinRadius()) {
                this.radius = Config.getInstance().getNearbyPhotosMinRadius();
            } else {
                this.radius = distance;
            }
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