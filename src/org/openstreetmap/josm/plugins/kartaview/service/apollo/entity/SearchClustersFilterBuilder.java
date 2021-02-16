package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import org.openstreetmap.josm.plugins.kartaview.argument.SignFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmComparison;


import java.util.Collection;
import java.util.Date;


/**
 * Builder for the SearchClustersFilter objects.
 *
 * @author nicoleta.viregan
 */
public class SearchClustersFilterBuilder {

    private Date date;
    private Double minConfidenceLevel;
    private Double maxConfidenceLevel;
    private Collection<OsmComparison> osmComparisons;
    private SignFilter signFilter;

    public SearchClustersFilterBuilder() {
    }

    public SearchClustersFilterBuilder(final SearchClustersFilter filter) {
        date(filter.getDate());
        minConfidenceLevel(filter.getMinConfidenceLevel());
        maxConfidenceLevel(filter.getMaxConfidenceLevel());
        osmComparisons(filter.getOsmComparisons());
        signFilter(filter.getSignFilter().getSignRegion(), filter.getSignFilter().getIncludedSignTypes(),
                filter.getSignFilter().getSignInternalNames());
    }

    public void date(final Date date) {
        this.date = date;
    }

    public void minConfidenceLevel(final Double minConfidenceLvl) {
        this.minConfidenceLevel = minConfidenceLvl;
    }

    public void maxConfidenceLevel(final Double maxConfidenceLvl) {
        this.maxConfidenceLevel = maxConfidenceLvl;
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

    public SearchClustersFilter build() {
        return new SearchClustersFilter(this);
    }
}