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
        this.minConfidenceLvl = builder.getMinConfidenceLvl();
        this.maxConfidenceLvl = builder.getMaxConfidenceLvl();
        this.osmComparisons = builder.getOsmComparisons();
        this.signFilter = builder.getSignFilter();
    }

    public Date getDate() {
        return date;
    }

    public Double getMinConfidenceLvl() {
        return minConfidenceLvl;
    }

    public Double getMaxConfidenceLvl() {
        return maxConfidenceLvl;
    }

    public Collection<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    public SignFilter getSignFilter() {
        return signFilter;
    }
}