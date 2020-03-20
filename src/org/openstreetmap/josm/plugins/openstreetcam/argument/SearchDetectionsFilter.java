package org.openstreetmap.josm.plugins.openstreetcam.argument;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Author;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ValidationStatus;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class containing the builder for creating the filter specific for the searchDetections method.
 *
 * @author nicoleta.viregan
 */

public class SearchDetectionsFilter {

    private Author author;
    private Date date;
    private Collection<EditStatus> editStatuses;
    private Collection<ValidationStatus> validationStatuses;
    private Collection<OsmComparison> osmComparisons;
    private Collection<DetectionMode> detectionsModes;
    private SignFilter signFilter;

    public SearchDetectionsFilter(final Builder builder) {
        this.author = builder.author;
        this.date = builder.date;
        this.editStatuses = builder.editStatuses;
        this.validationStatuses = builder.validationStatuses;
        this.osmComparisons = builder.osmComparisons;
        this.detectionsModes = builder.detectionsModes;
        this.signFilter = builder.signFilter;
    }

    public static class Builder {
        private Author author;
        private Date date;
        private Collection<EditStatus> editStatuses;
        private Collection<ValidationStatus> validationStatuses;
        private Collection<OsmComparison> osmComparisons;
        private Collection<DetectionMode> detectionsModes;
        private SignFilter signFilter;

        public Builder author(final Author author) {
            this.author = author;
            return this;
        }

        public Builder date(final Date date) {
            this.date = date;
            return this;
        }

        public Builder editStatuses(final Collection<EditStatus> editStatuses) {
            this.editStatuses = editStatuses;
            return this;
        }

        public Builder validationStatuses(final Collection<ValidationStatus> validationStatuses) {
            this.validationStatuses = validationStatuses;
            return this;
        }

        public Builder osmComparisons(final Collection<OsmComparison> osmComparisons) {
            this.osmComparisons = osmComparisons;
            return this;
        }

        public Builder detectionModes(final Collection<DetectionMode> detectionsModes) {
            this.detectionsModes = detectionsModes;
            return this;
        }

        public Builder signFilter(final String region, final List<String> includedTypes, final List<Sign> includedSigns) {
            Collection<String> includedNames = null;
            if (includedSigns != null) {
                includedNames = includedSigns.stream().map(Sign::getInternalName).collect(Collectors.toList());
            }
            this.signFilter = new SignFilter(region, includedTypes, includedNames);
            return this;
        }

        public SearchDetectionsFilter build() {
            return new SearchDetectionsFilter(this);
        }
    }
}