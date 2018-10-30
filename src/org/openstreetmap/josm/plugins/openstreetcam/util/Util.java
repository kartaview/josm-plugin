/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.BBox;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;


/**
 * Utility class, holds helper methods.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Util {

    private static final double POZ_DIST_DATA_LAYER = 6.0;
    private static final double POZ_DIST = 12.0;
    private static final double CLUSTER_POZ_DIST = 14.0;

    private static final int MIN_ZOOM = 0;
    private static final int MAX_ZOOM = 22;
    private static final int TILE_SIZE = 1024;
    private static final int ZOOM1_SCALE = 78206;
    private static final int ZOOM_CONST = 2;
    private static final double RADIUS = 0.0003;
    private static final double MAX_DISTANCE = 2.0;


    private Util() {}


    /**
     * Returns the zoom level based on the given bounds.
     *
     * @param bounds the map bounds
     * @return an integer
     */
    public static int zoom(final Bounds bounds) {
        return MainApplication.getMap().mapView.getScale() >= ZOOM1_SCALE ? 1 : (int) Math.min(MAX_ZOOM,
                Math.max(MIN_ZOOM, Math.round(Math.log(TILE_SIZE / bounds.asRect().height) / Math.log(ZOOM_CONST))));
    }

    /**
     * Returns the photo near to the given location. The method returns null if there is no photo nearby.
     *
     * @param photos a list of {@code Photo}s
     * @param point a {@code Point} the location where the user clicked
     * @return a {@code Photo} object
     */
    public static Photo nearbyPhoto(final List<Photo> photos, final Point point) {
        final double maxDist = MainApplication.getLayerManager().getEditLayer() != null && MainApplication
                .getLayerManager().getActiveLayer().equals(MainApplication.getLayerManager().getEditLayer())
                ? POZ_DIST_DATA_LAYER : POZ_DIST;
        Photo result = null;
        for (final Photo photo : photos) {
            final double dist = new Point2D.Double(point.getX(), point.getY())
                    .distance(MainApplication.getMap().mapView.getPoint(photo.getPoint()));
            if (dist <= maxDist) {
                result = photo;
                break;
            }
        }
        return result;
    }

    public static Detection nearbyDetection(final List<Detection> detections, final Point point) {
        final double maxDist = MainApplication.getLayerManager().getEditLayer() != null && MainApplication
                .getLayerManager().getActiveLayer().equals(MainApplication.getLayerManager().getEditLayer())
                ? POZ_DIST_DATA_LAYER : POZ_DIST;
        Detection result = null;
        for (final Detection detection : detections) {
            final double dist = new Point2D.Double(point.getX(), point.getY())
                    .distance(MainApplication.getMap().mapView.getPoint(detection.getPoint()));
            if (dist <= maxDist) {
                result = detection;
                break;
            }
        }
        return result;
    }

    public static Cluster nearbyCluster(final List<Cluster> clusters, final Point point) {
        Cluster result = null;
        for (final Cluster cluster : clusters) {
            final double dist = new Point2D.Double(point.getX(), point.getY())
                    .distance(MainApplication.getMap().mapView.getPoint(cluster.getPoint()));
            if (dist <= CLUSTER_POZ_DIST) {
                result = cluster;
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
        final BBox bbox = new BBox(selectedPhoto.getPoint().getX() - RADIUS, selectedPhoto.getPoint().getY() - RADIUS,
                selectedPhoto.getPoint().getX() + RADIUS, selectedPhoto.getPoint().getY() + RADIUS);
        final Map<Double, Photo> candidateMap = new TreeMap<>();
        for (final Photo photo : photos) {
            if (!photo.equals(selectedPhoto) && bbox.bounds(photo.getPoint())) {
                final double dist = selectedPhoto.getPoint().distance(photo.getPoint());
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
        final OsmDataLayer osmDataLayer = MainApplication.getLayerManager().getEditLayer();
        if ((MainApplication.getLayerManager().getActiveLayer() instanceof OsmDataLayer) && osmDataLayer != null
                && !osmDataLayer.data.getDataSourceBounds().isEmpty() && osmDataLayer.isVisible()) {
            for (final Bounds bounds : MainApplication.getLayerManager().getEditLayer().data.getDataSourceBounds()) {
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
     * Checks if the edit layer contains or not the given way. A way is complete if the edit layer contains the way
     * nodes and references.
     *
     * @param wayId represents the identifier of the way
     * @return a boolean value
     */
    public static boolean editLayerContainsWay(final PrimitiveId wayId) {
        boolean contains = false;
        if (MainApplication.getLayerManager().getEditDataSet() != null) {
            for (final Way way : MainApplication.getLayerManager().getEditDataSet().getWays()) {
                if (wayId.equals(way.getPrimitiveId()) && way.getNodesCount() > 0) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    public static Long getOsmUserId() {
        return UserIdentityManager.getInstance().isFullyIdentified()
                && UserIdentityManager.getInstance().asUser().getId() > 0
                ? UserIdentityManager.getInstance().asUser().getId() : null;
    }

    /**
     * This method compares two unordered lists and returns if they are equal based on contained values.
     * A null list is considered equal to an empty list.
     * The method checks if each list contains all the elements from the other.
     *
     * @param one - The first list to compare
     * @param two - The second list to compare
     * @param <T> - The type of the elements contained in the lists
     * @return true if the lists contain the same values or false otherwise
     */
    public static <T> boolean equalUnorderedPreferenceLists(final List<T> one, final List<T> two) {
        if ((one == null && two == null) || (one == null && two.isEmpty()) || (two == null && one.isEmpty())) {
            return true;
        }
        if (one == null || two == null || one.size() != two.size()) {
            return false;
        }
        return one.containsAll(two) && two.containsAll(one);
    }
}