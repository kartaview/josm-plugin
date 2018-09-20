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
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the cluster entity.
 *
 * @author beataj
 */
public class Cluster {

    private final Long id;
    private final Long latestChangeTimestamp;
    private final LatLon point;
    private final Double facing;
    private final Sign sign;
    private final Double confidenceLevel;
    private final List<Long> detectionIds;
    private final OsmComparison osmComparison;
    private final OsmElement osmElement;
    private final List<Detection> detections;
    private final List<Photo> photos;


    Cluster(final ClusterBuilder builder) {
        this.id = builder.getId();
        this.latestChangeTimestamp = builder.getLatestChangeTimestamp();
        this.point = builder.getPoint();
        this.facing = builder.getFacing();
        this.sign = builder.getSign();
        this.confidenceLevel = builder.getConfidenceLevel();
        this.detectionIds = builder.getDetectionIds();
        this.osmComparison = builder.getOsmComparison();
        this.osmElement = builder.getOsmElement();
        this.detections = builder.getDetections();
        this.photos = builder.getPhotos();
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

    public OsmComparison getOsmComparison() {
        return osmComparison;
    }

    public OsmElement getOsmElement() {
        return osmElement;
    }

    public List<Detection> getDetections() {
        return detections;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public boolean hasPhotos() {
        return photos != null && !photos.isEmpty();
    }

    public boolean hasDetections() {
        return detections != null && !detections.isEmpty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(id);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final Cluster other = (Cluster) obj;
            result = EntityUtil.bothNullOrEqual(id, other.getId());
        }
        return result;
    }
}