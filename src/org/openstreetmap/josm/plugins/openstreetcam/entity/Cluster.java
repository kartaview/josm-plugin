/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.Collection;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import com.grab.josm.common.entity.EntityUtil;


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
    private final ClusterConfidenceLevel confidenceLevel;
    private final List<Long> detectionIds;
    private final OsmComparison osmComparison;
    private final Collection<OsmElement> osmElements;
    private final List<Detection> detections;
    private final List<Photo> photos;
    private final String ocrValue;
    private final Short laneCount;


    Cluster(final ClusterBuilder builder) {
        this.id = builder.getId();
        this.latestChangeTimestamp = builder.getLatestChangeTimestamp();
        this.point = builder.getPoint();
        this.facing = builder.getFacing();
        this.sign = builder.getSign();
        this.confidenceLevel = builder.getConfidenceLevel();
        this.detectionIds = builder.getDetectionIds();
        this.osmComparison = builder.getOsmComparison();
        this.osmElements = builder.getOsmElements();
        this.detections = builder.getDetections();
        this.photos = builder.getPhotos();
        this.ocrValue = builder.getComponentValue();
        this.laneCount = builder.getLaneCount();
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

    public ClusterConfidenceLevel getConfidenceLevel() {
        return confidenceLevel;
    }

    public List<Long> getDetectionIds() {
        return detectionIds;
    }

    public OsmComparison getOsmComparison() {
        return osmComparison;
    }

    public Collection<OsmElement> getOsmElements() {
        return osmElements;
    }

    public List<Detection> getDetections() {
        return detections;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getOcrValue() {
        return ocrValue;
    }

    public Short getLaneCount() {
        return laneCount;
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

    @Override
    public String toString() {
        return point.lat() + ";" + point.lon() + ";" + facing + ";" + sign.getInternalName();
    }
}