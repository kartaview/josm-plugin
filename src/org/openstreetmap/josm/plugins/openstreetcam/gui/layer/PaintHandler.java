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
import java.util.List;
import java.util.SortedMap;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
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
            final boolean isSelected = isTransparent ? false : true;
            drawPhoto(graphics, mapView, selectedPhoto, isSelected);
        }
    }


    void drawSequence(final Graphics2D graphics, final MapView mapView, final Pair<Sequence, List<Detection>> sequence,
            final Photo selectedPhoto, final Detection selectedDetection) {
        graphics.setComposite(OPAQUE_COMPOSITE);
        graphics.setStroke(SEQUENCE_LINE);

        if (sequence.getFirst() != null) {
            drawSequence(graphics, mapView, sequence.getFirst());
        }
        if (sequence.getSecond() != null) {
            for (final Detection detection : sequence.getSecond()) {
                if (selectedDetection == null || !selectedDetection.equals(detection)) {
                    drawDetection(graphics, mapView, detection, false);
                }
            }
        }
        if (selectedPhoto != null) {
            drawPhoto(graphics, mapView, selectedPhoto, true);
        }
        if (selectedDetection != null) {
            drawDetection(graphics, mapView, selectedDetection, true);
        }
    }

    private void drawPhoto(final Graphics2D graphics, final MapView mapView, final Photo photo,
            final boolean isSelected) {
        if (Util.containsLatLon(mapView, photo.getLocation())) {
            final Point point = mapView.getPoint(photo.getLocation());
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

    private void drawSequence(final Graphics2D graphics, final MapView mapView, final Sequence sequence) {
        final Double length =
                Util.zoom(mapView.getRealBounds()) > MIN_ARROW_ZOOM ? ARROW_LENGTH * mapView.getScale() : null;
        graphics.setColor(PaintUtil.sequenceColor(mapView));

        Photo prevPhoto = sequence.getPhotos().get(0);
        for (int i = 1; i <= sequence.getPhotos().size() - 1; i++) {
            final Photo currentPhoto = sequence.getPhotos().get(i);

            // at least one of the photos is in current view draw line
            if (Util.containsLatLon(mapView, prevPhoto.getLocation())
                    || Util.containsLatLon(mapView, currentPhoto.getLocation())) {
                final Pair<Point, Point> lineGeometry = new Pair<>(mapView.getPoint(prevPhoto.getLocation()),
                        mapView.getPoint(currentPhoto.getLocation()));
                if (length == null) {
                    PaintManager.drawLine(graphics, lineGeometry);
                } else {
                    final Pair<Pair<Point, Point>, Pair<Point, Point>> arrowGeometry =
                            getArrowGeometry(mapView, prevPhoto.getLocation(), currentPhoto.getLocation(), length);
                    PaintManager.drawDirectedLine(graphics, lineGeometry, arrowGeometry);
                }
            }

            drawPhoto(graphics, mapView, prevPhoto, false);
            prevPhoto = currentPhoto;
        }
        drawPhoto(graphics, mapView, prevPhoto, false);
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

    private void drawDetection(final Graphics2D graphics, final MapView mapView, final Detection detection,
            final boolean isSelected) {
        final Point point = mapView.getPoint(detection.getPoint());
        final ImageIcon icon = DetectionIconFactory.INSTANCE.getIcon(detection.getSign().getName(), isSelected);
        PaintManager.drawIcon(graphics, icon, point);
    }

    private List<Point> toPoints(final MapView mapView, final List<LatLon> geometry) {
        final List<Point> points = new ArrayList<>();
        for (final LatLon latLon : geometry) {
            points.add(mapView.getPoint(latLon));
        }
        return points;
    }
}