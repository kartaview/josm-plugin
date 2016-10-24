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
package org.openstreetmap.josm.plugins.openstreetview.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;


/**
 * Utility class, holds helper methods.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Util {

    private static final double POZ_DIST_DATA_LAYER = 5.0;
    private static final double POZ_DIST = 10.0;
    private static final int MIN_ZOOM = 0;
    private static final int MAX_ZOOM = 18;
    private static final int TILE_SIZE = 1024;

    private Util() {}


    /**
     * Returns the zoom level based on the given bounds.
     *
     * @param bounds the map bounds
     * @return an integer
     */
    public static int zoom(final Bounds bounds) {
        return ((int) Math.min(MAX_ZOOM, Math.max(MIN_ZOOM,
                Math.round(Math.floor(Math.log(TILE_SIZE / bounds.asRect().height) / Math.log(2))))));
    }

    /**
     * Returns the photo near to the given location. The method returns null if there is no photo nearby.
     *
     * @param photos a list of {@code Photo}s
     * @param point a {@code Point} the location where the user clicked
     * @return a {@code Photo} object
     */
    public static Photo nearbyPhoto(final List<Photo> photos, final Point point) {
        double minDist = Double.MAX_VALUE;
        final double maxDist = Main.getLayerManager().getEditLayer() != null ? POZ_DIST_DATA_LAYER : POZ_DIST;
        Photo result = null;
        for (final Photo photo : photos) {
            final double dist = distance(point, photo.getLocation());
            if (dist <= minDist && dist <= maxDist) {
                minDist = dist;
                result = photo;
            }
        }
        return result;
    }

    private static double distance(final Point2D fromPoint, final LatLon toLatLon) {
        final Point toPoint = Main.map.mapView.getPoint(toLatLon);
        return new Point2D.Double(fromPoint.getX(), fromPoint.getY()).distance(toPoint);
    }
}