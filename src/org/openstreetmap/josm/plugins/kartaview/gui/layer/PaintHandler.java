/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.ARROW_LENGTH;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.MIN_ARROW_ZOOM;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.OPAQUE_COMPOSITE;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.SEGMENT_COLOR;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.SEGMENT_STROKE;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.SEQUENCE_LINE;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.TRANSPARENT_COMPOSITE;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import org.openstreetmap.josm.data.coor.ILatLon;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.argument.ClusterSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.DataType;
import org.openstreetmap.josm.plugins.kartaview.argument.DetectionFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.Projection;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.DownloadedNode;
import org.openstreetmap.josm.plugins.kartaview.entity.DownloadedRelation;
import org.openstreetmap.josm.plugins.kartaview.entity.DownloadedWay;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmElement;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmElementType;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.entity.Segment;
import org.openstreetmap.josm.plugins.kartaview.entity.Sequence;
import org.openstreetmap.josm.plugins.kartaview.gui.ClusterBackgroundIconFactory;
import org.openstreetmap.josm.plugins.kartaview.gui.DetectionIconFactory;
import org.openstreetmap.josm.plugins.kartaview.util.BoundingBoxUtil;
import org.openstreetmap.josm.plugins.kartaview.util.Util;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.ImageProvider;

import com.grab.josm.common.entity.Coordinate;
import com.grab.josm.common.entity.Pair;
import com.grab.josm.common.gui.PaintManager;
import com.grab.josm.common.util.GeometryUtil;


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
            final Float val = PaintUtil.segmentTransparency(transparencyMap, segment.getCoverage(), originalComposite
                    .getAlpha());
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
            final Photo selectedPhoto, final Detection selectedDetection, final boolean isClusterSelected) {
        graphics.setComposite(OPAQUE_COMPOSITE);
        graphics.setStroke(SEQUENCE_LINE);

        final boolean drawPhotos = PreferenceManager.getInstance().loadSearchFilter().getDataTypes().contains(
                DataType.PHOTO);
        final boolean drawDetections = PreferenceManager.getInstance().loadSearchFilter().getDataTypes().contains(
                DataType.DETECTION);
        if (sequence != null) {
            if (Objects.nonNull(selectedDetection) || Objects.nonNull(selectedPhoto)) {
                if (sequence.hasPhotos()) {
                    drawSequencePhotos(graphics, mapView, sequence.getPhotos(), drawPhotos);
                }

                // if we selected a cluster, we should not draw the detections of the sequence
                if (drawDetections && !isClusterSelected) {
                    drawDetections(graphics, mapView, sequence.getDetections(), selectedDetection, false);
                }
            }
        }
        if (Objects.nonNull(selectedPhoto) && drawPhotos) {
            drawPhoto(graphics, mapView, selectedPhoto, true);
        }
        if (selectedDetection != null && drawDetections) {
            drawDetection(graphics, mapView, selectedDetection, true);
        }
    }

    private void drawSequencePhotos(final Graphics2D graphics, final MapView mapView, final List<Photo> photos,
            final boolean drawPhotos) {
        final Double arrowLength = Util.zoom(mapView.getRealBounds()) > MIN_ARROW_ZOOM ? ARROW_LENGTH * mapView
                .getScale() : null;
        graphics.setColor(PaintUtil.lineColor(mapView, Constants.SEQUENCE_LINE_COLOR));

        Photo prevPhoto = photos.get(0);
        for (int i = 1; i < photos.size(); i++) {
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

    void drawSequenceDetections(final Graphics2D graphics, final MapView mapView, final List<Detection> detections,
            final Detection selectedDetection) {
        // filter detections
        final DetectionFilter filter = PreferenceManager.getInstance().loadSearchFilter().getDetectionFilter();
        final List<Detection> filteredDetections = Util.filterDetections(detections, filter);
        // draw map detections
        for (final Detection detection : filteredDetections) {
            if (selectedDetection == null || (!detection.equals(selectedDetection))) {
                drawDetection(graphics, mapView, detection, false);
            }
        }

        if (selectedDetection != null) {
            drawDetection(graphics, mapView, selectedDetection, true);
        }
    }


    void drawDetections(final Graphics2D graphics, final MapView mapView, final Collection<Detection> detections,
            final Detection selectedDetection, final boolean isTransparent) {
        final DetectionFilter filter = PreferenceManager.getInstance().loadSearchFilter().getDetectionFilter();
        final List<Detection> filteredDetections = Util.filterDetections(detections.stream().collect(Collectors
                .toCollection(ArrayList::new)), filter);
        final Composite composite = isTransparent ? TRANSPARENT_COMPOSITE : graphics.getComposite();
        graphics.setComposite(composite);

        // draw map detections
        if (Objects.nonNull(detections)) {
            for (final Detection detection : filteredDetections) {
                if (Objects.isNull(selectedDetection) || (!detection.equals(selectedDetection))) {
                    drawDetection(graphics, mapView, detection, false);
                }
            }
        }

        if (Objects.nonNull(selectedDetection)) {
            graphics.setComposite(OPAQUE_COMPOSITE);
            drawDetection(graphics, mapView, selectedDetection, true);
        }
    }

    void drawEdgeDetections(final Graphics2D graphics, final MapView mapView, final List<EdgeDetection> edgeDetections,
            final EdgeDetection selectedEdgeDetection, final boolean isTransparent) {
        final Composite composite = isTransparent ? TRANSPARENT_COMPOSITE : graphics.getComposite();
        graphics.setComposite(composite);

        for (final EdgeDetection edgeDetection : edgeDetections) {
            if (Objects.isNull(selectedEdgeDetection) || (!edgeDetection.equals(selectedEdgeDetection))) {
                drawEdgeDetection(graphics, mapView, edgeDetection, false);
            }
        }

        if (Objects.nonNull(selectedEdgeDetection)) {
            graphics.setComposite(OPAQUE_COMPOSITE);
            drawEdgeDetection(graphics, mapView, selectedEdgeDetection, true);
        }
    }

    void drawClusters(final Graphics2D graphics, final MapView mapView, final List<Cluster> clusters,
            final Cluster selectedCluster, final Photo selectedPhoto, final Detection selectedDetection) {
        final Composite composite = Objects.nonNull(selectedCluster) ? TRANSPARENT_COMPOSITE : graphics.getComposite();
        graphics.setComposite(composite);
        if (Objects.nonNull(clusters)) {
            for (final Cluster cluster : clusters) {
                if (Objects.nonNull(selectedCluster) || !cluster.equals(selectedCluster)) {
                    drawCluster(graphics, mapView, cluster, selectedPhoto, selectedDetection, false);
                    if (Objects.nonNull(selectedDetection)) {
                        graphics.setComposite(TRANSPARENT_COMPOSITE);
                    }
                }
            }
        }
        if (Objects.nonNull(selectedCluster)) {
            graphics.setComposite(OPAQUE_COMPOSITE);
            drawCluster(graphics, mapView, selectedCluster, selectedPhoto, selectedDetection, true);
            if (selectedPhoto != null && selectedDetection != null) {
                graphics.setComposite(OPAQUE_COMPOSITE);
                drawPhoto(graphics, mapView, selectedPhoto, true);
                drawDetection(graphics, mapView, selectedDetection, true);
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

    void drawEdgeClusters(final Graphics2D graphics, final MapView mapView, final List<Cluster> edgeClusters,
            final Cluster selectedEdgeCluster, final EdgeDetection selectedEdgeDetection) {
        final Composite composite = Objects.nonNull(selectedEdgeCluster) ? TRANSPARENT_COMPOSITE : graphics
                .getComposite();
        graphics.setComposite(composite);
        if (Objects.nonNull(edgeClusters)) {
            for (final Cluster cluster : edgeClusters) {
                if (Objects.isNull(selectedEdgeCluster) || !cluster.equals(selectedEdgeCluster)) {
                    drawEdgeCluster(graphics, mapView, cluster, selectedEdgeDetection, false);
                }
            }
        }
        if (Objects.nonNull(selectedEdgeCluster)) {
            graphics.setComposite(OPAQUE_COMPOSITE);
            drawEdgeCluster(graphics, mapView, selectedEdgeCluster, selectedEdgeDetection, true);
        }
    }

    private void drawEdgeCluster(final Graphics2D graphics, final MapView mapView, final Cluster cluster,
            final EdgeDetection selectedEdgeDetection, final boolean isSelected) {
        final ClusterSettings clusterSettings = PreferenceManager.getInstance().loadClusterSettings();
        if (isSelected) {
            if (Objects.nonNull(cluster.getEdgeDetections())) {
                drawEdgeDetections(graphics, mapView, cluster.getEdgeDetections(), selectedEdgeDetection, false);
            }
        }
        if (Util.containsLatLon(mapView, cluster.getPoint())) {
            final boolean isColored = Objects.nonNull(clusterSettings) && clusterSettings.isDisplayColorCoded()
                    && Objects.nonNull(cluster.getConfidenceLevel()) && Objects.nonNull(cluster.getConfidenceLevel()
                            .getConfidenceCategory());
            drawClusterIcon(graphics, mapView, cluster, isSelected, isColored, true);
        }
    }

    void drawMatchedData(final Graphics2D graphics, final MapView mapView, final List<OsmElement> matchedData) {
        for (final OsmElement element : matchedData) {
            switch (element.getType()) {
                case NODE:
                    drawNodeIcon(graphics, mapView, (DownloadedNode) element);
                    break;
                case WAY:
                    drawWay(graphics, mapView, (DownloadedWay) element, Color.RED);
                    break;
                case WAY_SECTION:
                    drawWay(graphics, mapView, (DownloadedWay) element, Color.RED);
                    break;
                case RELATION:
                    final DownloadedRelation relation = (DownloadedRelation) element;
                    relation.translateIdenticalMembers();
                    for (final DownloadedWay member : relation.getDownloadedMembers()) {
                        switch (member.getTag()) {
                            case "FROM":
                                drawWay(graphics, mapView, member, Color.GREEN);
                                break;
                            case "VIA":
                                drawWay(graphics, mapView, member, Color.BLUE);
                                break;
                            case "TO":
                                drawWay(graphics, mapView, member, Color.RED);
                                break;
                            default:
                                drawWay(graphics, mapView, member, Color.RED);
                                break;

                        }
                    }
                    break;
            }
        }
    }

    private void drawPhoto(final Graphics2D graphics, final MapView mapView, final Photo photo,
            final boolean isSelected) {
        if (Util.containsLatLon(mapView, photo.getPoint())) {
            final Point point = mapView.getPoint(photo.getPoint());
            if (DataSet.getInstance().getSelectedCluster() != null && DataSet.getInstance().getSelectedCluster()
                    .getPhotos() != null && DataSet.getInstance().getSelectedCluster().getPhotos().contains(photo)) {
                if (photo.getProjectionType() != null && photo.getProjectionType().equals(Projection.SPHERE)) {
                    if (photo.getHeading() != null) {
                        final ImageIcon icon = isSelected ? IconConfig.getInstance().getPhotoWrappedSelectedIconPurple()
                                : IconConfig.getInstance().getPhotoWrappedUnselectedIconPurple();
                        PaintManager.drawIcon(graphics, icon, point, photo.getHeading());
                    } else {
                        final ImageIcon icon = isSelected ? IconConfig.getInstance()
                                .getPhotoWrappedNoHeadingSelectedIconPurple() : IconConfig.getInstance()
                                        .getPhotoWrappedNoHeadingUnselectedIconPurple();
                        PaintManager.drawIcon(graphics, icon, point);
                    }
                } else {
                    if (photo.getHeading() != null) {
                        final ImageIcon icon = isSelected ? IconConfig.getInstance().getPhotoSelectedIconPurple()
                                : IconConfig.getInstance().getPhotoUnselectedIconPurple();
                        PaintManager.drawIcon(graphics, icon, point, photo.getHeading());
                    } else {
                        final ImageIcon icon = isSelected ? IconConfig.getInstance()
                                .getPhotoNoHeadingSelectedIconPurple() : IconConfig.getInstance()
                                        .getPhotoNoHeadingUnselectedIconPurple();
                        PaintManager.drawIcon(graphics, icon, point);
                    }
                }
            } else {
                if (photo.getProjectionType() != null && photo.getProjectionType().equals(Projection.SPHERE)) {
                    if (photo.getHeading() != null) {
                        final ImageIcon icon = isSelected ? IconConfig.getInstance().getPhotoWrappedSelectedIcon()
                                : IconConfig.getInstance().getPhotoWrappedUnselectedIcon();
                        PaintManager.drawIcon(graphics, icon, point, photo.getHeading());
                    } else {
                        final ImageIcon icon = isSelected ? IconConfig.getInstance()
                                .getPhotoWrappedNoHeadingSelectedIcon() : IconConfig.getInstance()
                                        .getPhotoWrappedNoHeadingUnselectedIcon();
                        PaintManager.drawIcon(graphics, icon, point);
                    }
                } else {
                    if (photo.getHeading() != null) {
                        final ImageIcon icon = isSelected ? IconConfig.getInstance().getPhotoSelectedIcon() : IconConfig
                                .getInstance().getPhotoIcon();
                        PaintManager.drawIcon(graphics, icon, point, photo.getHeading());
                    } else {
                        final ImageIcon icon = isSelected ? IconConfig.getInstance().getPhotoNoHeadingSelectedIcon()
                                : IconConfig.getInstance().getPhotoNoHeadingIcon();
                        PaintManager.drawIcon(graphics, icon, point);
                    }
                }

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
                final Pair<Pair<Point, Point>, Pair<Point, Point>> arrowGeometry = getArrowGeometry(mapView, start, end,
                        arrowLength);
                PaintManager.drawDirectedLine(graphics, lineGeometry, arrowGeometry);
            }
        }
    }

    private Pair<Pair<Point, Point>, Pair<Point, Point>> getArrowGeometry(final MapView mapView, final LatLon start,
            final LatLon end, final double length) {
        final LatLon midPoint = new LatLon((start.lat() + end.lat()) / 2, (start.lon() + end.lon()) / 2);
        final double bearing = Math.toDegrees(start.bearing((ILatLon) midPoint));
        final Pair<Coordinate, Coordinate> arrowEndCoordinates = GeometryUtil.arrowEndPoints(new Coordinate(midPoint
                .lat(), midPoint.lon()), bearing, -length);
        final Pair<Point, Point> arrowLine1 = new Pair<>(mapView.getPoint(midPoint), mapView.getPoint(new LatLon(
                arrowEndCoordinates.getFirst().getLat(), arrowEndCoordinates.getFirst().getLon())));
        final Pair<Point, Point> arrowLine2 = new Pair<>(mapView.getPoint(midPoint), mapView.getPoint(new LatLon(
                arrowEndCoordinates.getSecond().getLat(), arrowEndCoordinates.getSecond().getLon())));
        return new Pair<>(arrowLine1, arrowLine2);
    }

    private void drawDetection(final Graphics2D graphics, final MapView mapView, final Detection detection,
            final boolean isSelected) {
        final Point point = mapView.getPoint(detection.getPoint());
        final ImageIcon icon = DetectionIconFactory.INSTANCE.getIcon(detection.getSign(), isSelected);
        final ImageIcon backgroundIcon = DetectionIconFactory.INSTANCE.detectionBackgroundIcon(isSelected);
        double bearing = 0;
        if (Util.containsLatLon(mapView, detection.getPoint())) {
            if (detection.getFacing() != null) {
                bearing = detection.getFacing();
                PaintManager.drawIcon(graphics, backgroundIcon, point, bearing);
            } else {
                PaintManager.drawIcon(graphics, backgroundIcon, point);
            }
            final Coordinate coord = GeometryUtil.extrapolate(new Coordinate(detection.getPoint().lat(), detection
                    .getPoint().lon()), bearing, mapView.getDist100Pixel() * Constants.EXTRAPOLATE_DISTANCE);
            PaintManager.drawIcon(graphics, icon, mapView.getPoint(new LatLon(coord.getLat(), coord.getLon())));
        }
    }

    private void drawEdgeDetection(final Graphics2D graphics, final MapView mapView, final EdgeDetection edgeDetection,
            final boolean isSelected) {
        final Point point = mapView.getPoint(edgeDetection.getPoint());
        final ImageIcon icon = DetectionIconFactory.INSTANCE.getIcon(edgeDetection.getSign(), isSelected);
        final ImageIcon backgroundIcon = DetectionIconFactory.INSTANCE.edgeDetectionBackgroundIcon(isSelected);
        double bearing = 0;
        if (Util.containsLatLon(mapView, edgeDetection.getPoint())) {
            if (Objects.nonNull(edgeDetection.getFacing())) {
                bearing = edgeDetection.getFacing();
                PaintManager.drawIcon(graphics, backgroundIcon, point, bearing);
            } else {
                PaintManager.drawIcon(graphics, backgroundIcon, point);
            }
            final Coordinate coord = GeometryUtil.extrapolate(new Coordinate(edgeDetection.getPoint().lat(),
                    edgeDetection.getPoint().lon()), bearing, mapView.getDist100Pixel()
                            * Constants.EXTRAPOLATE_DISTANCE);
            PaintManager.drawIcon(graphics, icon, mapView.getPoint(new LatLon(coord.getLat(), coord.getLon())));
        }
    }

    private void drawCluster(final Graphics2D graphics, final MapView mapView, final Cluster cluster,
            final Photo selectedPhoto, final Detection selectedDetection, final boolean isSelected) {
        final ClusterSettings clusterSettings = PreferenceManager.getInstance().loadClusterSettings();
        if (isSelected) {
            if (clusterSettings != null && clusterSettings.isDisplayDetectionLocations()) {
                if (cluster.getDetections() != null && cluster.getPhotos() != null) {
                    drawClusterData(graphics, mapView, cluster, selectedPhoto);
                } else if (cluster.getPhotos() != null) {
                    drawPhotos(graphics, mapView, cluster.getPhotos(), selectedPhoto, false);
                }
            } else {
                drawPhotos(graphics, mapView, cluster.getPhotos(), selectedPhoto, false);
                drawDetections(graphics, mapView, cluster.getDetections(), selectedDetection, false);
            }
        }
        if (Util.containsLatLon(mapView, cluster.getPoint())) {
            final boolean isColored = clusterSettings != null && clusterSettings.isDisplayColorCoded();
            drawClusterIcon(graphics, mapView, cluster, isSelected, isColored, false);
        }
    }

    private void drawClusterData(final Graphics2D graphics, final MapView mapView, final Cluster cluster,
            final Photo selectedPhoto) {
        final Map<Photo, List<Detection>> metadata = new HashMap<>();
        for (final Photo photo : cluster.getPhotos()) {
            final List<Detection> photoDetections = cluster.getDetections().stream().filter(d -> d.getSequenceId()
                    .equals(photo.getSequenceId()) && d.getSequenceIndex().equals(photo.getSequenceIndex())).collect(
                            Collectors.toList());
            metadata.put(photo, photoDetections);
        }
        graphics.setColor(PaintUtil.lineColor(mapView, Constants.CLUSTER_DATA_LINE_COLOR));
        graphics.setStroke(Constants.CLUSTER_DATA_LINE);
        for (final Entry<Photo, List<Detection>> entry : metadata.entrySet()) {
            // draw line
            final Point photoPoint = mapView.getPoint(entry.getKey().getPoint());
            final Composite origComposite = graphics.getComposite();
            final boolean isPhotoSelected = selectedPhoto != null && selectedPhoto.equals(entry.getKey());
            final Composite composite = isPhotoSelected ? OPAQUE_COMPOSITE : TRANSPARENT_COMPOSITE;
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

    // ToDo: refactor this method
    private void drawClusterIcon(final Graphics2D graphics, final MapView mapView, final Cluster cluster,
            final boolean isSelected, final boolean isColorCoded, final boolean isEdgeCluster) {
        final ImageIcon backgroundIcon = ClusterBackgroundIconFactory.INSTANCE.getClusterBackground(cluster, isSelected,
                isColorCoded, isEdgeCluster);
        final ImageIcon icon = DetectionIconFactory.INSTANCE.getIcon(cluster.getSign(), false);
        double bearing = 0;
        final Point point = mapView.getPoint(cluster.getPoint());
        if (cluster.getFacing() != null) {
            bearing = cluster.getFacing();
            PaintManager.drawIcon(graphics, backgroundIcon, point, cluster.getFacing());
        } else {
            PaintManager.drawIcon(graphics, backgroundIcon, point);
        }
        final Coordinate coord = GeometryUtil.extrapolate(new Coordinate(cluster.getPoint().lat(), cluster.getPoint()
                .lon()), bearing, mapView.getDist100Pixel() * Constants.EXTRAPOLATE_DISTANCE);
        PaintManager.drawIcon(graphics, icon, mapView.getPoint(new LatLon(coord.getLat(), coord.getLon())));
    }

    private List<Point> toPoints(final MapView mapView, final List<LatLon> geometry) {
        final List<Point> points = new ArrayList<>();
        for (final LatLon latLon : geometry) {
            points.add(mapView.getPoint(latLon));
        }
        return points;
    }

    private void drawNodeIcon(final Graphics2D graphics, final MapView mapView, final DownloadedNode node) {
        final Point point = mapView.getPoint(new LatLon(node.getMatchedNode().lat(), node.getMatchedNode().lon()));
        final ImageIcon icon = ImageProvider.get("data", "node.svg", ImageProvider.ImageSizes.LARGEICON);
        PaintManager.drawIcon(graphics, icon, point);
    }

    private void drawWay(final Graphics2D graphics, final MapView mapView, final DownloadedWay way, final Color color) {
        final ClusterSettings clusterSettings = PreferenceManager.getInstance().loadClusterSettings();
        if (way.getType() == OsmElementType.WAY) {
            final List<Point> geometry = way.getDownloadedNodes().stream().map(mapView::getPoint).collect(Collectors
                    .toList());
            PaintManager.drawSegment(graphics, geometry, color, SEQUENCE_LINE);
        } else {
            final List<Point> geometry = new ArrayList<>();
            final int startIndex = way.getDownloadedNodes().indexOf(way.getMatchedFromNode());
            final int endIndex = way.getDownloadedNodes().indexOf(way.getMatchedToNode());
            for (int i = Math.min(startIndex, endIndex); i <= Math.max(startIndex, endIndex); i++) {
                geometry.add(mapView.getPoint(way.getDownloadedNodes().get(i)));
            }
            PaintManager.drawSegment(graphics, geometry, color, SEQUENCE_LINE);
            if (clusterSettings.isDisplayTags() && way.getTag() != null) {
                drawTag(graphics, mapView, way);
            }
        }
    }

    private void drawTag(final Graphics2D graphics, final MapView mapView, final DownloadedWay way) {
        final LatLon fromPoint = new LatLon(way.getMatchedFromNode().lat(), way.getMatchedFromNode().lon());
        final LatLon toPoint = new LatLon(way.getMatchedToNode().lat(), way.getMatchedToNode().lon());
        Optional<LatLon> middlePoint;
        if (way.isStraight()) {
            middlePoint = BoundingBoxUtil.middlePointOfLineInMapViewBounds(fromPoint, toPoint);
        } else {
            final int middleIndex = (way.getDownloadedNodes().indexOf(way.getMatchedFromNode()) + way
                    .getDownloadedNodes().indexOf(way.getMatchedToNode())) / 2;
            final Node middleNode = way.getDownloadedNodes().get(middleIndex);
            middlePoint = Optional.of(new LatLon(middleNode.lat(), middleNode.lon()));
        }
        if (middlePoint.isPresent()) {
            final Point textPoint = mapView.getPoint(middlePoint.get());
            final int textWidth = graphics.getFontMetrics().stringWidth(way.getTag());
            if (way.getTag().equals("FROM")) {
                textPoint.translate(-textWidth, 0);
            } else if (way.getTag().equals("TO")) {
                textPoint.translate(textWidth, 0);
            }
            PaintManager.drawText(graphics, way.getTag(), textPoint, mapView.getFont().deriveFont(Font.BOLD),
                    Color.WHITE, Color.BLACK, OPAQUE_COMPOSITE);
        }
    }
}