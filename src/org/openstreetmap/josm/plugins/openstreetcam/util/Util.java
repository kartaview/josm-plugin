/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.BBox;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import com.telenav.josm.common.argument.BoundingBox;


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
    private static final int MAX_ZOOM = 22;
    private static final int TILE_SIZE = 1024;
    private static final int ZOOM1_SCALE = 78206;
    private static final int ZOOM_CONST = 2;
    private static final double RADIUS = 0.0003;
    private static final double MAX_DISTANCE = 2.0;
    private static final int EXTENSION_DISTANCE = 500;


    private Util() {}


    /**
     * Returns the zoom level based on the given bounds.
     *
     * @param bounds the map bounds
     * @return an integer
     */
    public static int zoom(final Bounds bounds) {
        return Main.map.mapView.getScale() >= ZOOM1_SCALE ? 1 : (int) Math.min(MAX_ZOOM,
                Math.max(MIN_ZOOM,
                        Math.round(Math.floor(Math.log(TILE_SIZE / bounds.asRect().height) / Math.log(ZOOM_CONST)))));
    }

    /**
     * Returns the photo near to the given location. The method returns null if there is no photo nearby.
     *
     * @param photos a list of {@code Photo}s
     * @param point a {@code Point} the location where the user clicked
     * @return a {@code Photo} object
     */
    public static Photo nearbyPhoto(final List<Photo> photos, final Point point) {
        final double maxDist = Main.getLayerManager().getEditLayer() != null ? POZ_DIST_DATA_LAYER : POZ_DIST;
        Photo result = null;
        for (final Photo photo : photos) {
            final double dist = new Point2D.Double(point.getX(), point.getY())
                    .distance(Main.map.mapView.getPoint(photo.getLocation()));
            if (dist <= maxDist) {
                result = photo;
                break;
            }
        }
        return result;
    }

    /**
     * Returns the photos that are near to the selected photo.
     *
     * @param photos a list of {@code Photo}s
     * @param selectedPhoto the currently selected {@code Photo}
     * @param size the number of nearby photos to return
     * @return a set of {@code Photo}
     */
    public static Collection<Photo> nearbyPhotos(final List<Photo> photos, final Photo selectedPhoto, final int size) {
        final BBox bbox = selectedPhoto.getLocation().toBBox(RADIUS);
        final Map<Double, Photo> candidateMap = new TreeMap<>();
        for (final Photo photo : photos) {
            if (!photo.getSequenceId().equals(selectedPhoto.getSequenceId()) && bbox.bounds(photo.getLocation())) {
                final double dist = selectedPhoto.getLocation().distance(photo.getLocation());
                if (dist <= MAX_DISTANCE) {
                    candidateMap.put(dist, photo);
                }
            }
        }
        return size < candidateMap.size() ? new ArrayList<>(candidateMap.values()).subList(0, size)
                : candidateMap.values();
    }

    /**
     * Verifies if the given mapView contains or not the given coordinate. If the {@code OsmDataLayer} is active and has
     * data, then the coordinate is search in the available bounds.
     *
     * @param mapView the current {@code MapView}
     * @param latLon the coordinate to be checked
     * @return boolean
     */
    public static boolean containsLatLon(final MapView mapView, final LatLon latLon) {
        boolean contains = false;
        if ((Main.getLayerManager().getActiveLayer() instanceof OsmDataLayer)
                && Main.getLayerManager().getEditLayer() != null
                && !mapView.getLayerManager().getEditLayer().data.getDataSourceBounds().isEmpty()) {
            for (final Bounds bounds : Main.getLayerManager().getEditLayer().data.getDataSourceBounds()) {
                if (bounds.contains(latLon)) {
                    contains = true;
                    break;
                }
            }
        } else {
            final Point point = mapView.getPoint(latLon);
            contains = mapView.contains(point);
        }
        return contains;
    }

    /**
     * Returns a circle that represents the current search area. The method takes into consideration also the edit layer
     * bounds. If there are more edit layer bounds the method returns the bounds of the current MapView.
     *
     * @return list of {@code Circle}s
     */
    public static Circle currentCircle() {
        final Circle circle;
        final List<Bounds> osmDataLayerBounds = editLayerDataBounds();
        if (osmDataLayerBounds != null && !osmDataLayerBounds.isEmpty()) {
            if (osmDataLayerBounds.size() > 1) {
                circle = new Circle(Main.map.mapView.getRealBounds());
            } else {
                circle = new Circle(osmDataLayerBounds.get(0));
            }
        } else {
            circle = new Circle(Main.map.mapView.getRealBounds());
        }
        return circle;
    }

    /**
     * Returns a list of bounding boxes, representing the current search area. The method takes into consideration also
     * the edit layer bounds.
     *
     * @return a list of {@code BoundingBox}
     */
    public static List<BoundingBox> currentBoundingBoxes() {
        final List<BoundingBox> result = new ArrayList<>();
        final List<Bounds> osmDataLayerBounds = editLayerDataBounds();
        if (osmDataLayerBounds != null && !osmDataLayerBounds.isEmpty()) {
            for (final Bounds osmBounds : osmDataLayerBounds) {
                if (Main.map.mapView.getRealBounds().intersects(osmBounds)) {
                    result.add(new BoundingBox(osmBounds.getMax().lat(), osmBounds.getMin().lat(),
                            osmBounds.getMax().lon(), osmBounds.getMin().lon()));
                }
            }
        } else {
            final Bounds bounds = Main.map.mapView.getRealBounds();
            final BoundingBox bbox = new BoundingBox(bounds.getMax().lat(), bounds.getMin().lat(),
                    bounds.getMax().lon(), bounds.getMin().lon());
            final double distance = EXTENSION_DISTANCE * Main.map.mapView.getScale();
            result.add(bbox.extendBoundingBox((int) distance));
        }
        return result;
    }

    private static List<Bounds> editLayerDataBounds() {
        List<Bounds> osmDataLayerBounds = null;
        if (Main.getLayerManager().getEditLayer() != null
                && (Main.getLayerManager().getActiveLayer() instanceof OsmDataLayer)) {
            osmDataLayerBounds = Main.getLayerManager().getEditLayer().data.getDataSourceBounds();
        }
        return osmDataLayerBounds;
    }

    /**
     * Checks if the edit layer contains or not the given way. A way is complete if the edit layer contains the way
     * nodes and references.
     *
     * @param wayId represents the identifier of the way
     * @return a boolean value
     */
    public static boolean editLayerContainsWay(final PrimitiveId wayId) {
        boolean contains = false;
        if (Main.getLayerManager().getEditDataSet() != null) {
            for (final Way way : Main.getLayerManager().getEditDataSet().getWays()) {
                if (wayId.equals(way.getPrimitiveId()) && way.getNodesCount() > 0) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }
}