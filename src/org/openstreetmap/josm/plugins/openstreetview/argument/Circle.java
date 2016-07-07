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
package org.openstreetmap.josm.plugins.openstreetview.argument;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.BBox;
import org.openstreetmap.josm.gui.MapView;


/**
 * Defines the attributes of a circle. A circle represents a searching map area and it is represented by a center
 * coordinate and radius.
 *
 * @author Beata
 * @version $Revision$
 */
public class Circle {

    private static final int MAX_RADIUS = 5000;
    private final LatLon center;
    private final int radius;

    /**
     * Builds a new circle based on the given map view.
     *
     * @param mapView represents the currently visible map
     */
    public Circle(final MapView mapView) {
        final BBox bbox =
                new Bounds(mapView.getLatLon(0, mapView.getHeight()), mapView.getLatLon(mapView.getWidth(), 0))
                .toBBox();
        this.center = bbox.getCenter();
        final int distance = (int) this.center.greatCircleDistance(bbox.getBottomRight());
        this.radius = distance > MAX_RADIUS ? MAX_RADIUS : distance;
    }


    public LatLon getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}