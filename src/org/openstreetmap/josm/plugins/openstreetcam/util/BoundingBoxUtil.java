/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.ProjectionBounds;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Projection;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.Geometry;
import com.grab.josm.common.argument.BoundingBox;


/**
 * Utility class, contains methods for obtaining the current bounding box.
 *
 * @author beataj
 * @version $Revision$
 */
public final class BoundingBoxUtil {

    private static final Bounds WORLD_BOUNDS = new Bounds(-90, -180, 90, 180);

    private BoundingBoxUtil() {}

    /**
     * Returns a bounding box that represents the current search area. The method takes into consideration also the edit
     * layer bounds. If there are more edit layer bounds the method returns the bounds of the current MapView. Use this
     * method for photo locations.
     *
     * @return list of {@code Circle}s
     */
    public static BoundingBox currentBoundingBox() {
        final List<Bounds> osmDataLayerBounds = editLayerDataBounds();
        Bounds bounds;
        if (osmDataLayerBounds != null && !osmDataLayerBounds.isEmpty()) {
            if (osmDataLayerBounds.size() > 1) {
                bounds = MainApplication.getMap().mapView.getRealBounds();
            } else {
                bounds = osmDataLayerBounds.get(0);
            }
        } else {
            bounds = MainApplication.getMap().mapView.getRealBounds();
        }
        return new BoundingBox(bounds.getMax().lat(), bounds.getMin().lat(), bounds.getMax().lon(),
                bounds.getMin().lon());
    }

    /**
     * Returns a list of bounding boxes, representing the current search area. The method takes into consideration also
     * the edit layer bounds. Use this method for segments.
     *
     * @param mapViewDataLoad if true only the data inside the current bounding box is loaded
     * @return a list of {@code BoundingBox}
     */
    public static List<BoundingBox> currentBoundingBoxes(final boolean mapViewDataLoad) {
        final List<BoundingBox> result = new ArrayList<>();
        if (!mapViewDataLoad) {
            result.add(mapViewBounds());
        } else {
            final List<Bounds> osmDataLayerBounds = editLayerDataBounds();
            if (osmDataLayerBounds != null && !osmDataLayerBounds.isEmpty()) {
                for (final Bounds osmBounds : osmDataLayerBounds) {
                    if (MainApplication.getMap().mapView.getRealBounds().intersects(osmBounds)) {
                        result.add(new BoundingBox(osmBounds.getMax().lat(), osmBounds.getMin().lat(),
                                osmBounds.getMax().lon(), osmBounds.getMin().lon()));
                    }
                }
            } else {
                result.add(mapViewBounds());
            }
        }
        return result;
    }

    /**
     * Returns a list of Bounds objects, representing the current active area. If no area is active, The whole map is
     * returned.
     *
     * @return a list of {@code Bounds}
     */
    public static List<Bounds> currentBounds() {
        final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
        List<Bounds> result = new ArrayList<>();
        final List<Bounds> osmDataLayerBounds = editLayerDataBounds();
        if (!mapViewSettings.isDataLoadFlag() || osmDataLayerBounds == null || osmDataLayerBounds.isEmpty()) {
            result.add(WORLD_BOUNDS);
        } else {
            result = osmDataLayerBounds;
        }
        return result;
    }

    /**
     * The method returns the middle point of the visible part of the line represented by the given points inside the
     * mapview area.
     *
     * @param fromPoint - the starting point of the segment
     * @param toPoint - the ending point of the segment
     * @return Optional containing the LatLon coordinates for the middle point
     */
    public static Optional<LatLon> middlePointOfLineInMapViewBounds(final LatLon fromPoint, final LatLon toPoint) {
        Optional<LatLon> result = Optional.empty();
        final ProjectionBounds visibleBounds = MainApplication.getMap().mapView.getProjectionBounds();
        final Projection projection = MainApplication.getMap().mapView.getProjection();

        EastNorth fromEN = projection.latlon2eastNorth(fromPoint);
        EastNorth toEN = projection.latlon2eastNorth(toPoint);
        if (!visibleBounds.contains(fromEN)) {
            fromEN = moveFirstPointInBounds(fromEN, toEN, visibleBounds);
        }
        if (!visibleBounds.contains(toEN)) {
            toEN = moveFirstPointInBounds(toEN, fromEN, visibleBounds);
        }
        if (fromEN != null && toEN != null) {
            final LatLon from = projection.eastNorth2latlon(fromEN);
            final LatLon to = projection.eastNorth2latlon(toEN);
            result = Optional.of(from.getCenter(to));
        }
        return result;
    }

    private static EastNorth moveFirstPointInBounds(final EastNorth fromPoint, final EastNorth toPoint,
            final ProjectionBounds bounds) {
        EastNorth intersectionPoint = null;
        if (fromPoint != null && toPoint != null) {
            if (fromPoint.north() < bounds.minNorth) {
                intersectionPoint = Geometry.getSegmentSegmentIntersection(fromPoint, toPoint,
                        new EastNorth(bounds.minEast, bounds.minNorth), new EastNorth(bounds.maxEast, bounds.minNorth));
                if (intersectionPoint != null) {
                    return intersectionPoint;
                }
            }
            if (fromPoint.east() > bounds.maxEast) {
                intersectionPoint = Geometry.getSegmentSegmentIntersection(fromPoint, toPoint,
                        new EastNorth(bounds.maxEast, bounds.minNorth), new EastNorth(bounds.maxEast, bounds.maxNorth));
                if (intersectionPoint != null) {
                    return intersectionPoint;
                }
            }
            if (fromPoint.north() > bounds.maxNorth) {
                intersectionPoint = Geometry.getSegmentSegmentIntersection(fromPoint, toPoint,
                        new EastNorth(bounds.maxEast, bounds.maxNorth), new EastNorth(bounds.minEast, bounds.maxNorth));
                if (intersectionPoint != null) {
                    return intersectionPoint;
                }
            }
            if (fromPoint.east() < bounds.minEast) {
                intersectionPoint = Geometry.getSegmentSegmentIntersection(fromPoint, toPoint,
                        new EastNorth(bounds.minEast, bounds.maxNorth), new EastNorth(bounds.minEast, bounds.minNorth));
                if (intersectionPoint != null) {
                    return intersectionPoint;
                }
            }
        }
        return intersectionPoint;
    }

    private static BoundingBox mapViewBounds() {
        final Bounds bounds = MainApplication.getMap().mapView.getRealBounds();
        return new BoundingBox(bounds.getMax().lat(), bounds.getMin().lat(), bounds.getMax().lon(),
                bounds.getMin().lon());
    }

    private static List<Bounds> editLayerDataBounds() {
        List<Bounds> osmDataLayerBounds = null;
        if (MainApplication.getLayerManager().getEditLayer() != null
                && (MainApplication.getLayerManager().getActiveLayer() instanceof OsmDataLayer)
                && MainApplication.getLayerManager().getActiveLayer().isVisible()) {
            osmDataLayerBounds = MainApplication.getLayerManager().getEditLayer().data.getDataSourceBounds();
        }
        return osmDataLayerBounds;
    }
}