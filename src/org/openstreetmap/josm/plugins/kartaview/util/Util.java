/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.BBox;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.kartaview.argument.DetectionFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.SearchFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;


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


    private Util() {
    }


    /**
     * Returns the zoom level based on the given bounds.
     *
     * @param bounds the map bounds
     * @return an integer
     */
    public static int zoom(final Bounds bounds) {
        return MainApplication.getMap().mapView.getScale() >= ZOOM1_SCALE ? 1 : (int) Math.min(MAX_ZOOM, Math.max(
                MIN_ZOOM, Math.round(Math.log(TILE_SIZE / bounds.asRect().height) / Math.log(ZOOM_CONST))));
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
            final double dist = new Point2D.Double(point.getX(), point.getY()).distance(MainApplication.getMap().mapView
                    .getPoint(photo.getPoint()));
            if (dist <= maxDist) {
                result = photo;
                break;
            }
        }
        return result;
    }

    public static Detection nearbyDetection(final Collection<Detection> detections, final Point point) {
        final double maxDist = MainApplication.getLayerManager().getEditLayer() != null && MainApplication
                .getLayerManager().getActiveLayer().equals(MainApplication.getLayerManager().getEditLayer())
                        ? POZ_DIST_DATA_LAYER : POZ_DIST;
        Detection result = null;
        for (final Detection detection : detections) {
            final double dist = new Point2D.Double(point.getX(), point.getY()).distance(MainApplication.getMap().mapView
                    .getPoint(detection.getPoint()));
            if (dist <= maxDist) {
                result = detection;
                break;
            }
        }
        return result;
    }

    public static EdgeDetection nearbyEdgeDetection(final List<EdgeDetection> edgeDetections, final Point point) {
        final double maxDist = MainApplication.getLayerManager().getEditLayer() != null && MainApplication
                .getLayerManager().getActiveLayer().equals(MainApplication.getLayerManager().getEditLayer())
                        ? POZ_DIST_DATA_LAYER : POZ_DIST;
        EdgeDetection result = null;
        for (final EdgeDetection edgeDetection : edgeDetections) {
            final double dist = new Point2D.Double(point.getX(), point.getY()).distance(MainApplication.getMap().mapView
                    .getPoint(edgeDetection.getPoint()));
            if (dist <= maxDist) {
                result = edgeDetection;
                break;
            }
        }
        return result;
    }

    public static Cluster nearbyCluster(final List<Cluster> clusters, final Point point) {
        Cluster result = null;
        for (final Cluster cluster : clusters) {
            final double dist = new Point2D.Double(point.getX(), point.getY()).distance(MainApplication.getMap().mapView
                    .getPoint(cluster.getPoint()));
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
            final BBox bbox = new BBox(selectedPhoto.getPoint().getX() - RADIUS, selectedPhoto.getPoint().getY()
                    - RADIUS, selectedPhoto.getPoint().getX() + RADIUS, selectedPhoto.getPoint().getY() + RADIUS);
            final Map<Double, Photo> candidateMap = new TreeMap<>();
            for (final Photo photo : photos) {
                if (!photo.equals(selectedPhoto) && bbox.bounds(photo.getPoint()) && isPointInActiveArea(photo
                        .getPoint())) {
                    final double dist = selectedPhoto.getPoint().distance(photo.getPoint());
                    if (dist <= MAX_DISTANCE) {
                        candidateMap.put(dist, photo);
                    }
                }
            }
            result = size < candidateMap.size() ? new ArrayList<>(candidateMap.values()).subList(0, size) : candidateMap
                    .values();
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
        final List<Bounds> activeAreas = BoundingBoxUtil.currentAreas();
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
        if (mapViewSettings.isDataLoadFlag() && (MainApplication.getLayerManager()
                .getActiveLayer() instanceof OsmDataLayer) && osmDataLayer != null && !osmDataLayer.data
                        .getDataSourceBounds().isEmpty() && osmDataLayer.isVisible()) {
            for (final Bounds bounds : MainApplication.getLayerManager().getEditLayer().data.getDataSourceBounds()) {
                if (latLon != null && bounds.contains(latLon)) {
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
        return UserIdentityManager.getInstance().isFullyIdentified() && UserIdentityManager.getInstance().asUser()
                .getId() > 0 ? UserIdentityManager.getInstance().asUser().getId() : null;
    }

    /**
     * Filters the given detection list and return a new list containing the filtered detections.
     *
     * @param detections - list of detections to be filtered
     * @param filter - the DetectionFilter to apply
     * @return a new list of detections remained after filtering
     */
    public static List<Detection> filterDetections(final List<Detection> detections, final DetectionFilter filter) {
        final List<Detection> filteredDetections = new ArrayList<>();
        for (final Detection detection : detections) {
            final boolean editStatusMatches = Objects.isNull(filter.getEditStatuses()) || filter.getEditStatuses()
                    .contains(detection.getEditStatus());
            final boolean signTypeMatches = Objects.nonNull(filter.getSignTypes()) && filter.getSignTypes()
                    .contains(detection.getSign().getType());
            final boolean specificSignsMatch = Objects.nonNull(filter.getSpecificSigns()) && filter.getSpecificSigns()
                    .contains(detection.getSign());
            final boolean allDetectionsMatch =
                    Objects.isNull(filter.getSignTypes()) && Objects.isNull(filter.getSpecificSigns());
            final boolean modesMatch =
                    Objects.isNull(filter.getModes()) || filter.getModes().contains(detection.getMode());
            if (editStatusMatches && (allDetectionsMatch || signTypeMatches || specificSignsMatch) && modesMatch) {
                filteredDetections.add(detection);
            }
        }
        return filteredDetections;
    }

    public static boolean isDetectionMatchingFilters(final SearchFilter filter, final Detection selectedDetection) {
        boolean isCorresponding = true;
        if (selectedDetection != null) {
            if (filter.getDetectionFilter().getEditStatuses() != null && !filter.getDetectionFilter().getEditStatuses()
                    .isEmpty() && selectedDetection.getEditStatus() != null && !filter.getDetectionFilter()
                            .getEditStatuses().contains(selectedDetection.getEditStatus())) {
                isCorresponding = false;
            } else if (filter.getDetectionFilter().getModes() != null && selectedDetection.getMode() != null && !filter
                    .getDetectionFilter().getModes().isEmpty() && !filter.getDetectionFilter().getModes().contains(
                            selectedDetection.getMode())) {
                isCorresponding = false;
            } else if (filter.getDetectionFilter().getSignTypes() != null) {
                final List<String> matchedSignNames = filter.getDetectionFilter().getSignTypes().stream().filter(s -> s
                        .equals(selectedDetection.getSign().getType())).collect(Collectors.toList());
                if (matchedSignNames.size() != 1) {
                    isCorresponding = false;
                }
            } else if (filter.getDetectionFilter().getSpecificSigns() != null) {
                final List<Sign> matchedSignNames = filter.getDetectionFilter().getSpecificSigns().stream().filter(
                        s -> s.getName().equals(selectedDetection.getSign().getName())).collect(Collectors.toList());
                if (matchedSignNames.isEmpty()) {
                    isCorresponding = false;
                }
            }
        }

        return isCorresponding;
    }

    public static boolean checkFrontFacingDisplay(final Detection detection) {
        boolean displayFrontFacingPhotoFormat = true;
        if (detection != null) {
            if (PreferenceManager.getInstance().loadPhotoSettings().isDisplayFrontFacingFlag()) {
                if (!detection.containsOnlyFrontFacingCoordinates() && detection.getLocationOnPhoto() == null) {
                    displayFrontFacingPhotoFormat = false;
                }
            } else {
                if (!detection.containsOnlyFrontFacingCoordinates()) {
                    displayFrontFacingPhotoFormat = false;
                }
            }
        }
        return displayFrontFacingPhotoFormat;
    }
}