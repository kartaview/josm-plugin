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
 *
 * @author beataj
 * @version $Revision$
 */
public class Cluster {

    private Long id;
    private Long latestChangeTimestamp;
    private LatLon point;
    private Double facing;
    private Sign sign;
    private Double confidenceLevel;
    private List<Long> detectionIds;
    private OsmComparison osmComparison;
    private OsmElement osmElement;
    private List<Detection> detections;
    private List<Photo> photos;


    public Cluster() {}

    public Cluster(final Long id, final Long latestChangeTimestamp, final LatLon point, final Double facing,
            final Sign sign, final Double confidenceLevel, final List<Long> detectionIds,
            final OsmComparison osmComparison, final OsmElement osmElement) {
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

    public OsmComparison getOsmComparison() {
        return osmComparison;
    }

    public OsmElement getOsmElement() {
        return osmElement;
    }

    public void setOsmElement(final OsmElement osmElement) {
        this.osmElement = osmElement;
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