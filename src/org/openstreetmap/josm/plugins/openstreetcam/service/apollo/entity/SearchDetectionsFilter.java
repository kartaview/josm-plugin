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

    public Author getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public Collection<EditStatus> getEditStatuses() {
        return editStatuses;
    }

    public Collection<ValidationStatus> getValidationStatuses() {
        return validationStatuses;
    }

    public Collection<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    public Collection<DetectionMode> getDetectionModes() {
        return detectionModes;
    }

    public SignFilter getSignFilter() {
        return signFilter;
    }
}