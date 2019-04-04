/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */

package org.openstreetmap.josm.plugins.openstreetcam.entity;

/**
 * Defines the detection confidence level business entity. A confidence level represents a set of values that measures
 * how valid is a detection.
 *
 * @author beataj
 */
public class ConfidenceLevel {

    private final Double detectionConfidence;
    private final Double facingConfidence;
    private final Double positioningConfidence;
    private final Double keyPointsConfidence;
    private final Double trackingConfidence;
    private final Double ocrConfidence;


    public ConfidenceLevel(final Double detectionConfidence, final Double facingConfidence,
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
