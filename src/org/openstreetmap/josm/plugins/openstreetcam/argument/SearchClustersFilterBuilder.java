package org.openstreetmap.josm.plugins.openstreetcam.argument;

import org.openstreetmap.josm.plugins.openstreetcam.entity.ConfidenceLevelFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Builder for creating the filter specific for the searchClusters method.
 *
 * @author nicoleta.viregan
 */
public class SearchClustersFilterBuilder {

    private Date date;
    private Double minConfidenceLvl;
    private Double maxConfidenceLvl;
    private Collection<OsmComparison> osmComparisons;
    private SignFilter signFilter;

    public SearchClustersFilterBuilder date(final Date date) {
        this.date = date;
        return this;
    }

    public SearchClustersFilterBuilder minConfidenceLevel(final ConfidenceLevelFilter filter) {
        this.minConfidenceLvl = filter.getMinConfidenceLevel();
        return this;
    }

    public SearchClustersFilterBuilder maxConfidenceLevel(final ConfidenceLevelFilter filter) {
        this.maxConfidenceLvl = filter.getMaxConfidenceLevel();
        return this;
    }

    public SearchClustersFilterBuilder osmComparisons(final Collection<OsmComparison> osmComparisons) {
        this.osmComparisons = osmComparisons;
        return this;
    }

    public SearchClustersFilterBuilder signFilter(final String region, final List<String> includedTypes,
            final List<Sign> includedSigns) {
        Collection<String> includedNames = null;
        if (includedSigns != null) {
            includedNames = includedSigns.stream().map(Sign::getInternalName).collect(Collectors.toList());
        }
        this.signFilter = new SignFilter(region, includedTypes, includedNames);
        return this;
    }

    public SearchClustersFilterBuilder() {

    }

    public SearchClustersFilterBuilder build() {
        final SearchClustersFilterBuilder filterBuilder = new SearchClustersFilterBuilder();
        filterBuilder.date = this.date;
        filterBuilder.maxConfidenceLvl = this.maxConfidenceLvl;
        filterBuilder.minConfidenceLvl = this.minConfidenceLvl;
        filterBuilder.osmComparisons = this.osmComparisons;
        filterBuilder.signFilter = this.signFilter;

        return filterBuilder;
    }
}