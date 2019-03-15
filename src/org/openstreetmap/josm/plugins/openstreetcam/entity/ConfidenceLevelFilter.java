/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
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