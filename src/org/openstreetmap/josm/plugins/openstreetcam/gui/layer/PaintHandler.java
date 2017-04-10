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
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.ARROW_LENGTH;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.BING_LAYER_NAME;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.MAPBOX_LAYER_NAME;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.MIN_ARROW_ZOOM;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.OPAQUE_ALPHA;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.OPAQUE_COMPOSITE;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.SEGMENT_COLOR;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.SEGMENT_STROKE;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.SEGMENT_TRANSPARENCY;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.SEQUENCE_LINE;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.SEQUENCE_LINE_COLOR;
import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.TRANSPARENT_COMPOSITE;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.layer.ImageryLayer;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import com.telenav.josm.common.entity.Coordinate;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.gui.PaintUtil;
import com.telenav.josm.common.util.GeometryUtil;


/**
 * Defines custom drawing methods.
 *
 * @author Beata
 * @version $Revision$
 */
class PaintHandler {


    /**
     * Draws a list of photos to the map. A photo is represented by an icon on the map.
     *
     * @param graphics a {@code Graphics2D} used to draw elements to the map
     * @param mapView a {@code MapView} represents the current map view
     * @param photos a list of {@code Photo}s
     * @param selectedPhoto the currently selected {@code Photo}
     */
    void drawPhotos(final Graphics2D graphics, final MapView mapView, final List<Photo> photos,
            final Photo selectedPhoto, final Sequence selectedSequence) {
        final Composite composite = selectedSequence != null && !selectedSequence.getPhotos().isEmpty()
                ? TRANSPARENT_COMPOSITE : graphics.getComposite();

        // draw photo locations
        graphics.setComposite(composite);
        for (final Photo photo : photos) {
            if (!photo.equals(selectedPhoto)) {
                drawPhoto(graphics, mapView, photo, false);
            }
        }

        if (selectedSequence != null) {
            graphics.setComposite(OPAQUE_COMPOSITE);
            graphics.setStroke(SEQUENCE_LINE);
            drawSequence(graphics, mapView, selectedSequence);
        }

        if (selectedPhoto != null) {
            drawPhoto(graphics, mapView, selectedPhoto, true);
        }
    }

    private void drawPhoto(final Graphics2D graphics, final MapView mapView, final Photo photo,
            final boolean isSelected) {
        if (Util.containsLatLon(mapView, photo.getLocation())) {
            final Point point = mapView.getPoint(photo.getLocation());
            if (photo.getHeading() != null) {
                final ImageIcon icon = isSelected ? IconConfig.getInstance().getPhotoSelectedIcon()
                        : IconConfig.getInstance().getPhotoIcon();
                PaintUtil.drawIcon(graphics, icon, point, photo.getHeading());
            } else {
                final ImageIcon icon = isSelected ? IconConfig.getInstance().getPhotoNoHeadingSelectedIcon()
                        : IconConfig.getInstance().getPhotoNoHeadingIcon();
                PaintUtil.drawIcon(graphics, icon, point);
            }
        }
    }

    private void drawSequence(final Graphics2D graphics, final MapView mapView, final Sequence sequence) {
        final Double length =
                Util.zoom(mapView.getRealBounds()) > MIN_ARROW_ZOOM ? ARROW_LENGTH * mapView.getScale() : null;
        graphics.setColor(getSequenceColor(mapView));

        Photo prevPhoto = sequence.getPhotos().get(0);
        for (int i = 1; i <= sequence.getPhotos().size() - 1; i++) {
            final Photo currentPhoto = sequence.getPhotos().get(i);

            // at least one of the photos is in current view draw line
            if (Util.containsLatLon(mapView, prevPhoto.getLocation())
                    || Util.containsLatLon(mapView, currentPhoto.getLocation())) {
                final Pair<Point, Point> lineGeometry = new Pair<>(mapView.getPoint(prevPhoto.getLocation()),
                        mapView.getPoint(currentPhoto.getLocation()));
                if (length == null) {
                    PaintUtil.drawLine(graphics, lineGeometry);
                } else {
                    final Pair<Pair<Point, Point>, Pair<Point, Point>> arrowGeometry =
                            getArrowGeometry(mapView, prevPhoto.getLocation(), currentPhoto.getLocation(), length);
                    PaintUtil.drawDirectedLine(graphics, lineGeometry, arrowGeometry);
                }
            }

            drawPhoto(graphics, mapView, prevPhoto, false);
            prevPhoto = currentPhoto;
        }

        drawPhoto(graphics, mapView, prevPhoto, false);
    }

    private Color getSequenceColor(final MapView mapView) {
        String mapLayerName = "";
        if (mapView.getLayerManager().getActiveLayer() instanceof ImageryLayer) {
            mapLayerName = ((ImageryLayer) mapView.getLayerManager().getActiveLayer()).getInfo().getName();
        } else {
            for (final Layer layer : mapView.getLayerManager().getLayers()) {
                if (layer.isVisible() && layer instanceof ImageryLayer) {
                    mapLayerName = ((ImageryLayer) layer).getInfo().getName();
                    break;
                }
            }
        }
        return mapLayerName.equals(BING_LAYER_NAME) || mapLayerName.equals(MAPBOX_LAYER_NAME)
                ? SEQUENCE_LINE_COLOR.brighter() : SEQUENCE_LINE_COLOR.darker();
    }

    private Pair<Pair<Point, Point>, Pair<Point, Point>> getArrowGeometry(final MapView mapView, final LatLon start,
            final LatLon end, final double length) {
        final LatLon midPoint = new LatLon((start.lat() + end.lat()) / 2, (start.lon() + end.lon()) / 2);
        final double bearing = Math.toDegrees(start.bearing(midPoint));
        final Pair<Coordinate, Coordinate> arrowEndCoordinates =
                GeometryUtil.arrowEndPoints(new Coordinate(start.lat(), start.lon()), bearing, -length);
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
        final SortedMap<Integer, Float> transparencyMap = generateSegmentTransparencyMap(segments);
        final AlphaComposite originalComposite = (AlphaComposite) graphics.getComposite();
        for (final Segment segment : segments) {
            final Float val = segmentTransparency(transparencyMap, segment.getCoverage(), originalComposite.getAlpha());
            graphics.setComposite(originalComposite.derive(val));
            PaintUtil.drawSegment(graphics, toPoints(mapView, segment.getGeometry()));
        }
    }

    private List<Point> toPoints(final MapView mapView, final List<LatLon> geometry) {
        final List<Point> points = new ArrayList<>();
        for (final LatLon latLon : geometry) {
            points.add(mapView.getPoint(latLon));
        }
        return points;
    }
    private SortedMap<Integer, Float> generateSegmentTransparencyMap(final List<Segment> segments) {
        final SortedMap<Integer, Float> map = new TreeMap<>();
        if (!segments.isEmpty()) {
            final SortedSet<Integer> coverages = new TreeSet<>();
            for (final Segment segment : segments) {
                coverages.add(segment.getCoverage());
            }
            map.put(coverages.first(), SEGMENT_TRANSPARENCY[0]);
            map.put(coverages.last(), SEGMENT_TRANSPARENCY[SEGMENT_TRANSPARENCY.length - 1]);

            final Integer[] list = coverages.toArray(new Integer[0]);
            final int count = coverages.size() / SEGMENT_TRANSPARENCY.length;
            int index = 0;
            for (int i = 0; i < SEGMENT_TRANSPARENCY.length - 1; i++) {
                index += count;
                map.put(list[index], SEGMENT_TRANSPARENCY[i]);

            }
        }
        return map;
    }

    private float segmentTransparency(final SortedMap<Integer, Float> map, final Integer coverage,
            final float originalTransparency) {
        float transparency = SEGMENT_TRANSPARENCY[0];
        if (map.size() > 1) {
            for (final Entry<Integer, Float> entry : map.entrySet()) {
                if (coverage <= entry.getKey()) {
                    transparency = entry.getValue();
                    break;
                }
            }

        } else {
            transparency = map.get(coverage);
        }
        if (originalTransparency < OPAQUE_ALPHA) {
            // take into account global JOSM transparency setting
            transparency =
                    OPAQUE_ALPHA.equals(transparency) ? originalTransparency : (originalTransparency * transparency);
        }
        return transparency;
    }
}