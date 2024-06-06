/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import com.grab.josm.common.entity.EntityUtil;


/**
 * POJO class for storing edge detection information.
 *
 * @author nicoleta.viregan
 */
public class EdgeDetection {

    private Long id;
    private Long generationTimestamp;
    private Long creationTimestamp;
    private Long latestChangeTimestamp;
    private EdgePhoto photoMetadata;
    private LatLon point;
    private Sign sign;
    private DetectionShape shapeOnPhoto;
    private Algorithm algorithm;
    private DetectionConfidenceLevel confidenceLevel;
    private OcrValue ocrValue;
    private Double facing;
    private String trackingId;
    private List<Attribute> customAttributes;
    private String extendedId;

    public Long getId() {
        return id;
    }

    public Long getGenerationTimestamp() {
        return generationTimestamp;
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Long getLatestChangeTimestamp() {
        return latestChangeTimestamp;
    }

    public EdgePhoto getPhotoMetadata() {
        return photoMetadata;
    }

    public LatLon getPoint() {
        return point;
    }

    public Sign getSign() {
        return sign;
    }

    public DetectionShape getShapeOnPhoto() {
        return shapeOnPhoto;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public DetectionConfidenceLevel getConfidenceLevel() {
        return confidenceLevel;
    }

    public OcrValue getOcrValue() {
        return ocrValue;
    }

    public Double getFacing() {
        return facing;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public List<Attribute> getCustomAttributes() {
        return customAttributes;
    }

    public String getExtendedId() {
        return extendedId;
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
            final EdgeDetection other = (EdgeDetection) obj;
            result = EntityUtil.bothNullOrEqual(id, other.getId());
        }
        return result;
    }
}