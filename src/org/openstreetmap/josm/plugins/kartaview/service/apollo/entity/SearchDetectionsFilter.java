package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import org.openstreetmap.josm.plugins.kartaview.argument.SignFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.Author;
import org.openstreetmap.josm.plugins.kartaview.entity.DetectionMode;
import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmComparison;
import org.openstreetmap.josm.plugins.kartaview.entity.ValidationStatus;

import java.util.Collection;
import java.util.Date;


/**
 * Class containing the filter specific for the searchDetections method.
 *
 * @author nicoleta.viregan
 */

class SearchDetectionsFilter {

    private final Author author;
    private final Date date;
    private final Collection<EditStatus> editStatuses;
    private final Collection<ValidationStatus> validationStatuses;
    private final Collection<OsmComparison> osmComparisons;
    private final Collection<DetectionMode> detectionModes;
    private final SignFilter signFilter;

    SearchDetectionsFilter(final SearchDetectionsFilterBuilder builder) {
        this.author = builder.getAuthor();
        this.date = builder.getDate();
        this.editStatuses = builder.getEditStatuses();
        this.validationStatuses = builder.getValidationStatuses();
        this.osmComparisons = builder.getOsmComparisons();
        this.detectionModes = builder.getDetectionModes();
        this.signFilter = builder.getSignFilter();
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

    Collection<DetectionMode> getDetectionModes() {
        return detectionModes;
    }

    SignFilter getSignFilter() {
        return signFilter;
    }
}