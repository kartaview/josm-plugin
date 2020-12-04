/*
 *
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class ClusterConfidenceLevel {

    private final Double overallConfidence;
    private final Double ocrConfidence;


    public ClusterConfidenceLevel(final Double overallConfidence, final Double ocrConfidence) {
        super();
        this.overallConfidence = overallConfidence;
        this.ocrConfidence = ocrConfidence;
    }

    public Double getOverallConfidence() {
        return overallConfidence;
    }

    public Double getOcrConfidence() {
        return ocrConfidence;
    }
}