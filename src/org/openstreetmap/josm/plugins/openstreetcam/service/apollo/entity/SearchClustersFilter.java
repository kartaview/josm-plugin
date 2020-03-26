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
    private final Double minConfidenceLevel;
    private final Double maxConfidenceLevel;
    private final Collection<OsmComparison> osmComparisons;
    private final SignFilter signFilter;

    SearchClustersFilter(final SearchClustersFilterBuilder builder) {
        this.date = builder.getDate();
        this.minConfidenceLevel = builder.getMinConfidenceLevel();
        this.maxConfidenceLevel = builder.getMaxConfidenceLevel();
        this.osmComparisons = builder.getOsmComparisons();
        this.signFilter = builder.getSignFilter();
    }

    Date getDate() {
        return date;
    }

    Double getMinConfidenceLevel() {
        return minConfidenceLevel;
    }

    Double getMaxConfidenceLevel() {
        return maxConfidenceLevel;
    }

    Collection<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    SignFilter getSignFilter() {
        return signFilter;
    }
}