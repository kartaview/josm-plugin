/*
 *
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import com.grab.josm.common.formatter.DecimalPattern;
import com.grab.josm.common.formatter.EntityFormatter;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class ClusterConfidenceLevel {

    private final Double ocrConfidence;
    private final Double matchingConfidence;
    private final ConfidenceLevelCategory confidenceCategory;


    public ClusterConfidenceLevel(final Double ocrConfidence, final Double matchingConfidence,
            final ConfidenceLevelCategory confidenceCategory) {
        super();
        this.ocrConfidence = ocrConfidence;
        this.matchingConfidence = matchingConfidence;
        this.confidenceCategory = confidenceCategory;
    }

    public Double getOcrConfidence() {
        return ocrConfidence;
    }

    public Double getMatchingConfidence() {
        return matchingConfidence;
    }

    public ConfidenceLevelCategory getConfidenceCategory() {
        return confidenceCategory;
    }


    @Override
    public String toString() {
        String result = GuiConfig.getInstance().getClusterOcrValueLbl()
                + EntityFormatter.formatDouble(ocrConfidence, false, DecimalPattern.MEDIUM);
        result += ", " + GuiConfig.getInstance().getClusterMatchingConfidenceShortLbl()
                + EntityFormatter.formatDouble(matchingConfidence, false, DecimalPattern.MEDIUM);
        result += ", " + GuiConfig.getInstance().getClusterConfidenceCategoryShortLbl() + confidenceCategory;
        return result;
    }
}