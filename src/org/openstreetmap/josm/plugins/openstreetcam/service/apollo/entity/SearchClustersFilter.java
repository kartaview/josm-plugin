package org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity;

import org.openstreetmap.josm.plugins.openstreetcam.argument.SignFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;

import java.util.Collection;
import java.util.Date;


/**
 * Class containing the filter specific for the searchClusters method.
 *
 * @author nicoleta.viregan
 */

class SearchClustersFilter {

    private final Date date;
    private final Double minConfidenceLvl;
    private final Double maxConfidenceLvl;
    private final Collection<OsmComparison> osmComparisons;
    private final SignFilter signFilter;

    SearchClustersFilter(final SearchClustersFilterBuilder builder) {
        this.date = builder.getDate();
        this.minConfidenceLvl = builder.getMinConfidenceLevel();
        this.maxConfidenceLvl = builder.getMaxConfidenceLevel();
        this.osmComparisons = builder.getOsmComparisons();
        this.signFilter = builder.getSignFilter();
    }

    Date getDate() {
        return date;
    }

    Double getMinConfidenceLvl() {
        return minConfidenceLvl;
    }

    Double getMaxConfidenceLvl() {
        return maxConfidenceLvl;
    }

    Collection<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    SignFilter getSignFilter() {
        return signFilter;
    }
}