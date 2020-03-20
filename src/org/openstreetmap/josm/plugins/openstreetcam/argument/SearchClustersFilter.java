package org.openstreetmap.josm.plugins.openstreetcam.argument;

import org.openstreetmap.josm.plugins.openstreetcam.entity.ConfidenceLevelFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class containing the builder for creating the filter specific for the searchClusters method.
 *
 * @author nicoleta.viregan
 */

public class SearchClustersFilter{

    private final Date date;
    private final Double minConfidenceLvl;
    private final Double maxConfidenceLvl;
    private final Collection<OsmComparison> osmComparisons;
    private final SignFilter signFilter;

    public SearchClustersFilter(final Builder builder) {
        this.date = builder.date;
        this.minConfidenceLvl = builder.minConfidenceLvl;
        this.maxConfidenceLvl = builder.maxConfidenceLvl;
        this.osmComparisons = builder.osmComparisons;
        this.signFilter = builder.signFilter;
    }

    public static class Builder {

        private Date date;
        private Double minConfidenceLvl;
        private Double maxConfidenceLvl;
        private Collection<OsmComparison> osmComparisons;
        private SignFilter signFilter;

        public Builder date(final Date date) {
            this.date = date;
            return this;
        }

        public Builder minConfidenceLevel(final ConfidenceLevelFilter filter) {
            this.minConfidenceLvl = filter.getMinConfidenceLevel();
            return this;
        }

        public Builder maxConfidenceLevel(final ConfidenceLevelFilter filter) {
            this.maxConfidenceLvl = filter.getMaxConfidenceLevel();
            return this;
        }

        public Builder osmComparisons(final Collection<OsmComparison> osmComparisons) {
            this.osmComparisons = osmComparisons;
            return this;
        }

        public Builder signFilter(final String region, final List<String> includedTypes,
                final List<Sign> includedSigns) {
            Collection<String> includedNames = null;
            if (includedSigns != null) {
                includedNames = includedSigns.stream().map(Sign::getInternalName).collect(Collectors.toList());
            }
            this.signFilter = new SignFilter(region, includedTypes, includedNames);
            return this;
        }

        public SearchClustersFilter build() {
            return new SearchClustersFilter(this);
        }
    }
}