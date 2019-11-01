/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.Objects;


public class ConfidenceLevelFilter {

    private final Double minConfidenceLevel;
    private final Double maxConfidenceLevel;

    public ConfidenceLevelFilter(final Double minConfidenceLevel, final Double maxConfidenceLevel) {
        this.minConfidenceLevel = minConfidenceLevel;
        this.maxConfidenceLevel = maxConfidenceLevel;
    }

    public Double getMinConfidenceLevel() {
        return minConfidenceLevel;
    }

    public Double getMaxConfidenceLevel() {
        return maxConfidenceLevel;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConfidenceLevelFilter that = (ConfidenceLevelFilter) o;
        return Objects.equals(minConfidenceLevel, that.minConfidenceLevel)
                && Objects.equals(maxConfidenceLevel, that.maxConfidenceLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minConfidenceLevel, maxConfidenceLevel);
    }
}