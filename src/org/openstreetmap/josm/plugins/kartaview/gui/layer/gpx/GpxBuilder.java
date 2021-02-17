/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer.gpx;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.data.gpx.GpxConstants;
import org.openstreetmap.josm.data.gpx.GpxData;
import org.openstreetmap.josm.data.gpx.GpxTrack;
import org.openstreetmap.josm.data.gpx.WayPoint;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.entity.Sequence;


/**
 * Converts business entities to GPX format.
 *
 * @author beataj
 * @version $Revision$
 */
final class GpxBuilder {
    private static final String EXTENSIONS = "extensions";
    private static final String HEADING = "heading";
    private static final String TIME = "time";
    private static final String UTC = "UTC";
    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String EMPTY_DATE = "0000-00-00 00:00:00";

    private GpxBuilder() {}


    /**
     * Builds a GPX structure for the sequence business entity. A sequence represents the GPS track associated with the
     * selected photo location.
     *
     * @param sequence a {@code Sequence} object
     * @return a {@code GpxData} object
     */
    static GpxData buildSequenceGpx(final Sequence sequence) {
        final Collection<Collection<WayPoint>> trk = new ArrayList<>();
        final List<WayPoint> wayPoints = new ArrayList<>();
        for (final Photo photo : sequence.getPhotos()) {
            wayPoints.add(createWayPoint(photo));
        }
        trk.add(wayPoints);
        final Map<String, Object> trkAttr = new HashMap<>();
        trkAttr.put(EXTENSIONS, HEADING);
        trkAttr.put(GpxConstants.GPX_DESC, GuiConfig.getInstance().getGpxTrackDescription());
        final GpxTrack gpsTrack = new GpxTrack(trk, trkAttr);
        final GpxData gpxData = new GpxData();
        // need to set fromServer=true, otherwise GpxWriter will not add the necessary extension URI
        gpxData.fromServer = true;
        gpxData.addTrack(gpsTrack);
        return gpxData;
    }


    private static WayPoint createWayPoint(final Photo photo) {
        final WayPoint wayPoint = new WayPoint(photo.getPoint());
        if (photo.getShotDate() != null && !photo.getShotDate().isEmpty() && !photo.getShotDate().equals(EMPTY_DATE)) {
            final LocalDateTime dateTime =
                    LocalDateTime.parse(photo.getShotDate(), DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT));
            final ZonedDateTime utcZoned = dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(UTC));
            final String formattedDate = DateTimeFormatter.ofPattern(UTC_DATE_FORMAT).format(utcZoned);
            wayPoint.put(TIME, formattedDate);
        }
        if (photo.getHeading() != null) {
            wayPoint.put(HEADING, Double.toString(photo.getHeading()));
        }
        return wayPoint;
    }
}