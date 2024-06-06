/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.SignFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.ConfidenceLevelCategory;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmComparison;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;


/**
 * Class containing the filter specific for the searchEdgeClusters method.
 *
 * @author maria.mitisor
 */
public class SearchEdgeClustersFilter {

    private Collection<OsmComparison> osmComparisons;
    private SignFilter signFilter;
    private Collection<ConfidenceLevelCategory> confidenceCategories;


    public SearchEdgeClustersFilter(final EdgeSearchFilter filter) {
        if (Objects.nonNull(filter)) {
            this.osmComparisons = filter.getOsmComparisons();
            final Collection<String> includedNames = Objects.nonNull(filter.getSpecificSigns()) ? filter
                    .getSpecificSigns().stream().map(Sign::getInternalName).collect(Collectors.toList()) : null;
            final String region = Objects.nonNull(filter.getRegion()) ? filter.getRegion() : null;
            final List<String> signTypes = Objects.nonNull(filter.getSignTypes()) ? filter.getSignTypes() : null;
            this.signFilter = Objects.nonNull(includedNames) || Objects.nonNull(region) || Objects.nonNull(signTypes)
                    ? new SignFilter(region, signTypes, null, includedNames) : null;
            this.confidenceCategories = filter.getConfidenceCategories();
        }
    }

    public Collection<ConfidenceLevelCategory> getConfidenceCategories() {
        return confidenceCategories;
    }

    public Collection<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    public SignFilter getSignFilter() {
        return signFilter;
    }
}