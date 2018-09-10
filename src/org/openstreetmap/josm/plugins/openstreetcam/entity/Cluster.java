/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class Cluster {

    private final Long id;
    private final Long latestChangeTimestamp;
    private final LatLon point;
    private final Double facing;
    private final Sign sign;
    private final Double confidenceLevel;
    private final List<Long> detectionIds;
    private final String osmComparison;
    private final OsmElement osmElement;
    private List<Detection> detections;
    private List<Photo> photos;


    public Cluster(final Long id, final Long latestChangeTimestamp, final LatLon point, final Double facing,
            final Sign sign, final Double confidenceLevel, final List<Long> detectionIds, final String osmComparison,
            final OsmElement osmElement) {
        this.id = id;
        this.latestChangeTimestamp = latestChangeTimestamp;
        this.point = point;
        this.facing = facing;
        this.sign = sign;
        this.confidenceLevel = confidenceLevel;
        this.detectionIds = detectionIds;
        this.osmComparison = osmComparison;
        this.osmElement = osmElement;
    }


    public Long getId() {
        return id;
    }

    public Long getLatestChangeTimestamp() {
        return latestChangeTimestamp;
    }

    public LatLon getPoint() {
        return point;
    }

    public Double getFacing() {
        return facing;
    }

    public Sign getSign() {
        return sign;
    }

    public Double getConfidenceLevel() {
        return confidenceLevel;
    }

    public List<Long> getDetectionIds() {
        return detectionIds;
    }

    public String getOsmComparison() {
        return osmComparison;
    }

    public OsmElement getOsmElement() {
        return osmElement;
    }

    public List<Detection> getDetections() {
        return detections;
    }

    public void setDetections(final List<Detection> detections) {
        this.detections = detections;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(final List<Photo> photos) {
        this.photos = photos;
    }
}