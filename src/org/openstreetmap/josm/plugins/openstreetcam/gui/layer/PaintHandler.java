/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.ARROW_LENGTH;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.MIN_ARROW_ZOOM;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.OPAQUE_COMPOSITE;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.SEGMENT_COLOR;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.SEGMENT_STROKE;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.SEQUENCE_LINE;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.TRANSPARENT_COMPOSITE;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ClusterSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.entity.Coordinate;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.gui.PaintManager;
import com.telenav.josm.common.util.GeometryUtil;


/**
 * Defines custom drawing methods.
 *
 * @author Beata
 * @version $Revision$
 */
class PaintHandler {

    /**
     * Draws a list of segments to the map.
     *
     * @param graphics a {@code Graphics2D} used to draw elements to the map
     * @param mapView a {@code MapView} represents the current map view
     * @param segments a list of {@code Segment}s
     */
    void drawSegments(final Graphics2D graphics, final MapView mapView, final List<Segment> segments) {
        graphics.setColor(SEGMENT_COLOR);
        graphics.setStroke(SEGMENT_STROKE);
        final SortedMap<Integer, Float> transparencyMap = PaintUtil.generateSegmentTransparencyMap(segments);
        final AlphaComposite originalComposite = (AlphaComposite) graphics.getComposite();
        for (final Segment segment : segments) {
            final Float val =
                    PaintUtil.segmentTransparency(transparencyMap, segment.getCoverage(), originalComposite.getAlpha());
            graphics.setComposite(originalComposite.derive(val));
            PaintManager.drawSegment(graphics, toPoints(mapView, segment.getGeometry()));
        }
    }

    /**
     * Draws a list of photo locations to the map. The photo locations are rotated based on heading if available.
     *
     * @param graphics a {@code Graphics2D} used to draw elements to the map
     * @param mapView a {@code MapView} represents the current map view
     * @param photos a list of {@code Photo}s
     * @param selectedPhoto
     * @param isTransparent
     */
    void drawPhotos(final Graphics2D graphics, final MapView mapView, final List<Photo> photos,
            final Photo selectedPhoto, final boolean isTransparent) {
        final Composite composite = isTransparent ? TRANSPARENT_COMPOSITE : graphics.getComposite();

        // draw photo locations
        if (photos != null) {
            graphics.setComposite(composite);
            for (final Photo photo : photos) {
                if (!photo.equals(selectedPhoto)) {
                    drawPhoto(graphics, mapView, photo, false);
                }
            }
        }

        if (selectedPhoto != null) {
            drawPhoto(graphics, mapView, selectedPhoto, !isTransparent);
        }
    }

    void drawSequence(final Graphics2D graphics, final MapView mapView, final Sequence sequence,
            final Photo selectedPhoto, final Detection selectedDetection) {
        graphics.setComposite(OPAQUE_COMPOSITE);
        graphics.setStroke(SEQUENCE_LINE);

        final boolean drawPhotos =
                PreferenceManager.getInstance().loadSearchFilter().getDataTypes().contains(DataType.PHOTO);
        final boolean drawDetections =
                PreferenceManager.getInstance().loadSearchFilter().getDataTypes().contains(DataType.DETECTION);
        if (sequence != null) {
            if (sequence.hasPhotos()) {
                drawSequencePhotos(graphics, mapView, sequence.getPhotos(), drawPhotos);
            }

            if (sequence.hasDetections() && drawDetections) {
                drawDetections(graphics, mapView, sequence.getDetections(), selectedDetection, false);
            }
        }
        if (selectedPhoto != null && drawPhotos) {
            drawPhoto(graphics, mapView, selectedPhoto, true);
        }
        if (selectedDetection != null && drawDetections) {
            drawDetection(graphics, mapView, selectedDetection, true);
        }
    }

    private void drawSequencePhotos(final Graphics2D graphics, final MapView mapView, final List<Photo> photos,
            final boolean drawPhotos) {
        final Double arrowLength =
                Util.zoom(mapView.getRealBounds()) > MIN_ARROW_ZOOM ? ARROW_LENGTH * mapView.getScale() : null;
                graphics.setColor(PaintUtil.lineColor(mapView, Constants.SEQUENCE_LINE_COLOR));

                Photo prevPhoto = photos.get(0);
                for (int i = 1; i <= photos.size() - 1; i++) {
                    final Photo currentPhoto = photos.get(i);
                    // at least one of the photos is in current view draw line
                    drawLine(graphics, mapView, prevPhoto.getPoint(), currentPhoto.getPoint(), arrowLength);

                    if (drawPhotos) {
                        drawPhoto(graphics, mapView, prevPhoto, false);
                    }
                    prevPhoto = currentPhoto;
                }
                if (drawPhotos) {
                    drawPhoto(graphics, mapView, prevPhoto, false);
                }
    }

    void drawDetections(final Graphics2D graphics, final MapView mapView, final List<Detection> detections,
            final Detection selectedDetection, final boolean isTransparent) {
        final Composite composite = isTransparent ? TRANSPARENT_COMPOSITE : graphics.getComposite();
        graphics.setComposite(composite);

        // draw map detections
        for (final Detection detection : detections) {
            if (selectedDetection == null || (!detection.equals(selectedDetection))) {
                drawDetection(graphics, mapView, detection, false);
            }
        }

        if (selectedDetection != null) {
            graphics.setComposite(OPAQUE_COMPOSITE);
            drawDetection(graphics, mapView, selectedDetection, true);
        }
    }

    void drawClusters(final Graphics2D graphics, final MapView mapView, final List<Cluster> clusters,
            final Cluster selectedCluster, final Photo selectedPhoto, final Detection selectedDetection) {
        final Composite composite = selectedCluster != null ? TRANSPARENT_COMPOSITE : graphics.getComposite();
        graphics.setComposite(composite);
        for (final Cluster cluster : clusters) {
            if (selectedCluster == null || !cluster.equals(selectedCluster)) {
                drawCluster(graphics, mapView, cluster, selectedPhoto, false);
            }
        }
        if (selectedCluster != null) {
            graphics.setComposite(OPAQUE_COMPOSITE);
            drawCluster(graphics, mapView, selectedCluster, selectedPhoto, true);
            if (selectedPhoto != null && selectedDetection != null) {
                graphics.setComposite(OPAQUE_COMPOSITE);
                drawPhoto(graphics, mapView, selectedPhoto, true);
            } else {
                if (selectedPhoto != null && !selectedCluster.getPoint().equals(selectedPhoto.getPoint())) {
                    drawPhoto(graphics, mapView, selectedPhoto, true);
                }
                if (selectedDetection != null && !selectedCluster.getPoint().equals(selectedDetection.getPoint())) {
                    drawDetection(graphics, mapView, selectedDetection, true);
                }
            }
        }
    }

    private void drawPhoto(final Graphics2D graphics, final MapView mapView, final Photo photo,
            final boolean isSelected) {
        if (Util.containsLatLon(mapView, photo.getPoint())) {
            final Point point = mapView.getPoint(photo.getPoint());
            if (photo.getHeading() != null) {
                final ImageIcon icon = isSelected ? IconConfig.getInstance().getPhotoSelectedIcon()
                        : IconConfig.getInstance().getPhotoIcon();
                PaintManager.drawIcon(graphics, icon, point, photo.getHeading());
            } else {
                final ImageIcon icon = isSelected ? IconConfig.getInstance().getPhotoNoHeadingSelectedIcon()
                        : IconConfig.getInstance().getPhotoNoHeadingIcon();
                PaintManager.drawIcon(graphics, icon, point);
            }
        }
    }


    private void drawLine(final Graphics2D graphics, final MapView mapView, final LatLon start, final LatLon end,
            final Double arrowLength) {
        if (Util.containsLatLon(mapView, start) || Util.containsLatLon(mapView, end)) {
            final Pair<Point, Point> lineGeometry = new Pair<>(mapView.getPoint(start), mapView.getPoint(end));
            if (arrowLength == null) {
                PaintManager.drawLine(graphics, lineGeometry);
            } else {
                final Pair<Pair<Point, Point>, Pair<Point, Point>> arrowGeometry =
                        getArrowGeometry(mapView, start, end, arrowLength);
                PaintManager.drawDirectedLine(graphics, lineGeometry, arrowGeometry);
            }
        }
    }

    private Pair<Pair<Point, Point>, Pair<Point, Point>> getArrowGeometry(final MapView mapView, final LatLon start,
            final LatLon end, final double length) {
        final LatLon midPoint = new LatLon((start.lat() + end.lat()) / 2, (start.lon() + end.lon()) / 2);
        final double bearing = Math.toDegrees(start.bearing(midPoint));
        final Pair<Coordinate, Coordinate> arrowEndCoordinates =
                GeometryUtil.arrowEndPoints(new Coordinate(midPoint.lat(), midPoint.lon()), bearing, -length);
        final Pair<Point, Point> arrowLine1 = new Pair<>(mapView.getPoint(midPoint), mapView.getPoint(
                new LatLon(arrowEndCoordinates.getFirst().getLat(), arrowEndCoordinates.getFirst().getLon())));
        final Pair<Point, Point> arrowLine2 = new Pair<>(mapView.getPoint(midPoint), mapView.getPoint(
                new LatLon(arrowEndCoordinates.getSecond().getLat(), arrowEndCoordinates.getSecond().getLon())));
        return new Pair<>(arrowLine1, arrowLine2);
    }

    private void drawDetection(final Graphics2D graphics, final MapView mapView, final Detection detection,
            final boolean isSelected) {
        final Point point = mapView.getPoint(detection.getPoint());
        final ImageIcon icon = DetectionIconFactory.INSTANCE.getIcon(detection.getSign(), isSelected);
        PaintManager.drawIcon(graphics, icon, point);
    }

    private void drawCluster(final Graphics2D graphics, final MapView mapView, final Cluster cluster,
            final Photo selectedPhoto, final boolean isSelected) {
        if (isSelected) {
            final ClusterSettings clusterSettings = PreferenceManager.getInstance().loadClusterSettings();
            if (clusterSettings != null && clusterSettings.isDisplayDetectionLocations()) {
                if (cluster.getDetections() != null && cluster.getPhotos() != null) {
                    drawClusterData(graphics, mapView, cluster, selectedPhoto);
                } else if (cluster.getPhotos() != null) {
                    drawPhotos(graphics, mapView, cluster.getPhotos(), selectedPhoto, false);
                }
            } else {
                drawPhotos(graphics, mapView, cluster.getPhotos(), selectedPhoto, false);
            }
        }
        drawClusterIcon(graphics, mapView, cluster, isSelected);
    }

    private void drawClusterData(final Graphics2D graphics, final MapView mapView, final Cluster cluster,
            final Photo selectedPhoto) {
        final Map<Photo, List<Detection>> metadata = new HashMap<>();
        for (final Photo photo : cluster.getPhotos()) {
            final List<Detection> photoDetections =
                    cluster.getDetections().stream()
                    .filter(d -> d.getSequenceId().equals(photo.getSequenceId())
                            && d.getSequenceIndex().equals(photo.getSequenceIndex()))
                    .collect(Collectors.toList());
            metadata.put(photo, photoDetections);
        }
        graphics.setColor(PaintUtil.lineColor(mapView, Constants.CLUSTER_DATA_LINE_COLOR));
        graphics.setStroke(Constants.CLUSTER_DATA_LINE);
        for (final Entry<Photo, List<Detection>> entry : metadata.entrySet()) {
            // draw line
            final Point photoPoint = mapView.getPoint(entry.getKey().getPoint());
            final Composite origComposite = graphics.getComposite();
            final boolean isPhotoSelected = selectedPhoto != null && selectedPhoto.equals(entry.getKey());
            final Composite composite = isPhotoSelected ? Constants.OPAQUE_COMPOSITE : Constants.TRANSPARENT_COMPOSITE;
            graphics.setComposite(composite);
            for (final Detection d : entry.getValue()) {
                final Point detectionPoint = mapView.getPoint(d.getPoint());
                if (!photoPoint.equals(detectionPoint)) {
                    final Pair<Point, Point> lineGeometry = new Pair<>(photoPoint, detectionPoint);
                    PaintManager.drawLine(graphics, lineGeometry);
                }
            }
            graphics.setComposite(origComposite);
            drawPhoto(graphics, mapView, entry.getKey(), false);
            for (final Detection detection : entry.getValue()) {
                drawDetection(graphics, mapView, detection, isPhotoSelected);
            }
        }
    }

    private void drawClusterIcon(final Graphics2D graphics, final MapView mapView, final Cluster cluster,
            final boolean isSelected) {
        final ImageIcon backgroundIcon = isSelected ? IconConfig.getInstance().getClusterBackgroundSelectedIcon()
                : IconConfig.getInstance().getClusterBackgroundIcon();
        final ImageIcon icon = DetectionIconFactory.INSTANCE.getIcon(cluster.getSign(), false);
        double bearing = 0;
        final Point point = mapView.getPoint(cluster.getPoint());
        if (cluster.getFacing() != null) {
            bearing = cluster.getFacing();
            PaintManager.drawIcon(graphics, backgroundIcon, point, cluster.getFacing());
        } else {
            PaintManager.drawIcon(graphics, backgroundIcon, point);
        }
        final Coordinate coord =
                GeometryUtil.extrapolate(new Coordinate(cluster.getPoint().lat(), cluster.getPoint().lon()), bearing,
                        mapView.getDist100Pixel() * Constants.CLUSTER_EXTRAPOLATE_DISTANCE);
        PaintManager.drawIcon(graphics, icon, mapView.getPoint(new LatLon(coord.getLat(), coord.getLon())));
    }

    private List<Point> toPoints(final MapView mapView, final List<LatLon> geometry) {
        final List<Point> points = new ArrayList<>();
        for (final LatLon latLon : geometry) {
            points.add(mapView.getPoint(latLon));
        }
        return points;
    }
}