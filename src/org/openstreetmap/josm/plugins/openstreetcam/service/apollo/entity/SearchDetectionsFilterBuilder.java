package org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity;

import org.openstreetmap.josm.plugins.openstreetcam.argument.SignFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Author;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ValidationStatus;

import java.util.Collection;
import java.util.Date;


/**
 * Builder for the SearchDetectionsFilter objects.
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

    public SearchDetectionsFilterBuilder() {
    }

    public SearchDetectionsFilterBuilder(final SearchDetectionsFilter filter) {
        author(filter.getAuthor());
        date(filter.getDate());
        editStatuses(filter.getEditStatuses());
        validationStatuses(filter.getValidationStatuses());
        osmComparisons(filter.getOsmComparisons());
        detectionModes(filter.getDetectionsModes());
        signFilter(filter.getSignFilter().getRegion(), filter.getSignFilter().getIncludedTypes(),
                filter.getSignFilter().getInternalNames());
    }

    public void author(final Author author) {
        this.author = author;
    }

    public void date(final Date date) {
        this.date = date;
    }

    public void editStatuses(final Collection<EditStatus> editStatuses) {
        this.editStatuses = editStatuses;
    }

    public void validationStatuses(final Collection<ValidationStatus> validationStatuses) {
        this.validationStatuses = validationStatuses;
    }

    public void osmComparisons(final Collection<OsmComparison> osmComparisons) {
        this.osmComparisons = osmComparisons;
    }

    public void detectionModes(final Collection<DetectionMode> detectionsModes) {
        this.detectionsModes = detectionsModes;
    }

    public void signFilter(final String region, final Collection<String> includedTypes,
            final Collection<String> includedSigns) {
        this.signFilter = new SignFilter(region, includedTypes, includedSigns);
    }

    Author getAuthor() {
        return author;
    }

    Date getDate() {
        return date;
    }

    Collection<EditStatus> getEditStatuses() {
        return editStatuses;
    }

    Collection<ValidationStatus> getValidationStatuses() {
        return validationStatuses;
    }

    Collection<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    Collection<DetectionMode> getDetectionsModes() {
        return detectionsModes;
    }

    SignFilter getSignFilter() {
        return signFilter;
    }

    public SearchDetectionsFilter build() {
        return new SearchDetectionsFilter(this);
    }
}