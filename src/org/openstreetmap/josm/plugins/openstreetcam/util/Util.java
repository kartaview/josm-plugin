/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.DetectionFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.OpenStreetCamServiceConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


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
        Collection<Photo> result = Collections.emptyList();
        if (selectedPhoto != null) {
            final BBox bbox =
                    new BBox(selectedPhoto.getPoint().getX() - RADIUS, selectedPhoto.getPoint().getY() - RADIUS,
                            selectedPhoto.getPoint().getX() + RADIUS, selectedPhoto.getPoint().getY() + RADIUS);
            final Map<Double, Photo> candidateMap = new TreeMap<>();
            for (final Photo photo : photos) {
                if (!photo.equals(selectedPhoto) && bbox.bounds(photo.getPoint()) && isPointInActiveArea(
                        photo.getPoint())) {
                    final double dist = selectedPhoto.getPoint().distance(photo.getPoint());
                    if (dist <= MAX_DISTANCE) {
                        candidateMap.put(dist, photo);
                    }
                }
            }
            result = size < candidateMap.size() ? new ArrayList<>(candidateMap.values()).subList(0, size) :
                candidateMap.values();
        }
        return result;
    }

    /**
     * Checks if the given point is inside the active areas of the data layer.
     *
     * @param point - {@code LatLon} point to be checked
     * @return true if the point is in the active area or false otherwise
     */
    public static boolean isPointInActiveArea(final LatLon point) {
        final List<Bounds> activeAreas = BoundingBoxUtil.currentBounds();
        return activeAreas.stream().anyMatch(area -> area.contains(point));
    }

    /**
     * Verifies if the given mapView contains or not the given coordinate. If the preference for loading data only
     * inside the active area is selected and {@code OsmDataLayer} is active and has data, then the coordinate is search
     * in the available bounds.
     *
     * @param mapView the current {@code MapView}
     * @param latLon the coordinate to be checked
     * @return boolean
     */
    public static boolean containsLatLon(final MapView mapView, final LatLon latLon) {
        boolean contains = false;
        final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
        final OsmDataLayer osmDataLayer = MainApplication.getLayerManager().getEditLayer();
        if (mapViewSettings.isDataLoadFlag() && (MainApplication.getLayerManager().getActiveLayer() instanceof OsmDataLayer) && osmDataLayer != null
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
     * Filters the given detection list and return a new list containing the filtered detections.
     * @param detections - list of detections to be filtered
     * @param filter - the DetectionFilter to apply
     * @return a new list of detections remained after filtering
     */
    public static List<Detection> filterDetections(final List<Detection> detections, final DetectionFilter filter) {
        final List<Detection> filteredDetections = new ArrayList<>();
        for (final Detection detection : detections) {
            final boolean osmComparisons = filter.getOsmComparisons() == null || filter.getOsmComparisons()
                    .contains(detection.getOsmComparison());
            final boolean editStatus =
                    filter.getEditStatuses() == null || filter.getEditStatuses().contains(detection.getEditStatus());
            final boolean signType =
                    filter.getSignTypes() != null && filter.getSignTypes().contains(detection.getSign().getType());
            final boolean specificSigns =
                    filter.getSpecificSigns() != null && filter.getSpecificSigns().contains(detection.getSign());
            final boolean allDetections = filter.getSignTypes() == null && filter.getSpecificSigns() == null;
            final boolean modes = filter.getModes() == null || filter.getModes().contains(detection.getMode());
            if (osmComparisons && editStatus && (allDetections || signType || specificSigns) && modes) {
                filteredDetections.add(detection);
            }
        }
        return filteredDetections;
    }

    public static boolean shouldDisplayImage(final Photo photo) {
        boolean display = false;
        if (photo != null && photo.getUsername() != null && !photo.getUsername().isEmpty()) {
            display = BordersFactory.getInstance().isPhotoInAvailableZone(photo.getPoint()) && isVendorAccepted(photo);
        }
        return display;
    }

    public static boolean isVendorAccepted(final Photo photo) {
        final List<String> acceptedVendors = Arrays.asList(OpenStreetCamServiceConfig.getInstance().getVendorsList());
        return acceptedVendors.contains(photo.getUsername());
    }
}