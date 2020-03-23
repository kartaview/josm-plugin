package org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity;

import org.openstreetmap.josm.plugins.openstreetcam.argument.SignFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;


import java.util.Collection;
import java.util.Date;


/**
 * Builder for the SearchClustersFilter objects.
 *
 * @author nicoleta.viregan
 */
public class SearchClustersFilterBuilder {

    private Date date;
    private Double minConfidenceLvl;
    private Double maxConfidenceLvl;
    private Collection<OsmComparison> osmComparisons;
    private SignFilter signFilter;

    public SearchClustersFilterBuilder() {
    }

    public SearchClustersFilterBuilder(final SearchClustersFilter filter) {
        date(filter.getDate());
        minConfidenceLevel(filter.getMaxConfidenceLvl());
        maxConfidenceLevel(filter.getMaxConfidenceLvl());
        osmComparisons(filter.getOsmComparisons());
        signFilter(filter.getSignFilter().getRegion(), filter.getSignFilter().getIncludedTypes(),
                filter.getSignFilter().getInternalNames());
    }

    public void date(final Date date) {
        this.date = date;
    }

    public void minConfidenceLevel(final Double minConfidenceLvl) {
        this.minConfidenceLvl = minConfidenceLvl;
    }

    public void maxConfidenceLevel(final Double maxConfidenceLvl) {
        this.maxConfidenceLvl = maxConfidenceLvl;
    }

    public void osmComparisons(final Collection<OsmComparison> osmComparisons) {
        this.osmComparisons = osmComparisons;
    }

    public void signFilter(final String region, final Collection<String> includedTypes,
            final Collection<String> includedSigns) {
        this.signFilter = new SignFilter(region, includedTypes, includedSigns);
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

    public SearchClustersFilter build() {
        return new SearchClustersFilter(this);
    }
}