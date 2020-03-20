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
 * Builder for creating the filter specific for the searchDetections method.
 *
 * @author nicoleta.viregan
 */
public class SearchDetectionsFilterBuilder {

    private Author author;
    private Date date;
    private Collection<EditStatus> editStatuses;
    private Collection<ValidationStatus> validationStatuses;
    private Collection<OsmComparison> osmComparisons;
    private Collection<DetectionMode> detectionsModes;
    private SignFilter signFilter;

    public SearchDetectionsFilterBuilder author(final Author author) {
        this.author = author;
        return this;
    }

    public SearchDetectionsFilterBuilder date(final Date date) {
        this.date = date;
        return this;
    }

    public SearchDetectionsFilterBuilder editStatuses(final Collection<EditStatus> editStatuses) {
        this.editStatuses = editStatuses;
        return this;
    }

    public SearchDetectionsFilterBuilder validationStatuses(final Collection<ValidationStatus> validationStatuses) {
        this.validationStatuses = validationStatuses;
        return this;
    }

    public SearchDetectionsFilterBuilder osmComparisons(final Collection<OsmComparison> osmComparisons) {
        this.osmComparisons = osmComparisons;
        return this;
    }

    public SearchDetectionsFilterBuilder detectionModes(final Collection<DetectionMode> detectionsModes) {
        this.detectionsModes = detectionsModes;
        return this;
    }

    public SearchDetectionsFilterBuilder signFilter(final String region, final List<String> includedTypes,
            final List<Sign> includedSigns) {
        Collection<String> includedNames = null;
        if (includedSigns != null) {
            includedNames = includedSigns.stream().map(Sign::getInternalName).collect(Collectors.toList());
        }
        this.signFilter = new SignFilter(region, includedTypes, includedNames);
        return this;
    }

    public SearchDetectionsFilterBuilder() {

    }

    public SearchDetectionsFilterBuilder build() {
        final SearchDetectionsFilterBuilder filterBuilder = new SearchDetectionsFilterBuilder();
        filterBuilder.author = this.author;
        filterBuilder.date = this.date;
        filterBuilder.editStatuses = this.editStatuses;
        filterBuilder.validationStatuses = this.validationStatuses;
        filterBuilder.osmComparisons = this.osmComparisons;
        filterBuilder.detectionsModes = this.detectionsModes;
        filterBuilder.signFilter = this.signFilter;

        return filterBuilder;
    }
}