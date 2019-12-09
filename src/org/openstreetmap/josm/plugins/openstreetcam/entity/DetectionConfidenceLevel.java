/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

/**
 * Defines the detection confidence level business entity. A confidence level represents a set of values that measures
 * how valid is a detection.
 *
 * @author beataj
 */
public class DetectionConfidenceLevel {

    private final Double detectionConfidence;
    private final Double facingConfidence;
    private final Double positioningConfidence;
    private final Double keyPointsConfidence;
    private final Double trackingConfidence;
    private final Double ocrConfidence;


    public DetectionConfidenceLevel(final Double detectionConfidence, final Double facingConfidence,
            final Double positioningConfidence, final Double keyPointsConfidence, final Double trackingConfidence,
            final Double ocrConfidence) {
        this.detectionConfidence = detectionConfidence;
        this.facingConfidence = facingConfidence;
        this.positioningConfidence = positioningConfidence;
        this.keyPointsConfidence = keyPointsConfidence;
        this.trackingConfidence = trackingConfidence;
        this.ocrConfidence = ocrConfidence;
    }

    public Double getDetectionConfidence() {
        return detectionConfidence;
    }

    public Double getFacingConfidence() {
        return facingConfidence;
    }

    public Double getPositioningConfidence() {
        return positioningConfidence;
    }

    public Double getKeyPointsConfidence() {
        return keyPointsConfidence;
    }

    public Double getTrackingConfidence() {
        return trackingConfidence;
    }

    public Double getOcrConfidence() {
        return ocrConfidence;
    }

    public boolean isNotNull() {
        return detectionConfidence != null || facingConfidence != null || positioningConfidence != null
                || keyPointsConfidence != null || trackingConfidence != null || ocrConfidence != null;
    }
}
