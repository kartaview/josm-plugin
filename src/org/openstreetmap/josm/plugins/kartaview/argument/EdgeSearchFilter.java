/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.argument;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.openstreetmap.josm.plugins.kartaview.entity.ConfidenceLevelCategory;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmComparison;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;


/**
 * Entity mapping the filters available in filters panel of the edge layer.
 *
 * @author nicoleta.viregan
 */
public class EdgeSearchFilter {

    public static final EdgeSearchFilter DEFAULT = new EdgeSearchFilter(Arrays.asList(DataType.DETECTION,
            DataType.CLUSTER), null, null, null, null, null);

    private final List<DataType> dataTypes;
    private final List<OsmComparison> osmComparisons;
    private final List<ConfidenceLevelCategory> confidenceCategories;
    private final String region;
    private final List<String> signTypes;
    private final List<Sign> specificSigns;


    public EdgeSearchFilter(final List<DataType> dataTypes, final List<OsmComparison> osmComparisons,
            final List<ConfidenceLevelCategory> confidenceCategories, final String region, final List<String> signTypes,
            final List<Sign> specificSigns) {
        this.dataTypes = dataTypes;
        this.osmComparisons = osmComparisons;
        this.confidenceCategories = confidenceCategories;
        this.region = region;
        this.signTypes = signTypes;
        this.specificSigns = specificSigns;
    }


    public List<DataType> getDataTypes() {
        return dataTypes;
    }

    public List<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    public List<ConfidenceLevelCategory> getConfidenceCategories() {
        return confidenceCategories;
    }

    public String getRegion() {
        return region;
    }

    public List<String> getSignTypes() {
        return signTypes;
    }

    public List<Sign> getSpecificSigns() {
        return specificSigns;
    }

    public boolean hasDetectionFilters() {
        return Objects.nonNull(signTypes) || Objects.nonNull(region) || Objects.nonNull(specificSigns);
    }

    public boolean hasClusterFilters() {
        return Objects.nonNull(osmComparisons) || Objects.nonNull(confidenceCategories) || hasDetectionFilters();
    }
}