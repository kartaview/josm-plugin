/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import com.grab.josm.common.entity.EntityUtil;
import org.openstreetmap.josm.data.coor.LatLon;

import java.util.Collection;


/**
 * Defines the detection entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Detection implements Comparable<Detection> {

    // setters are not required,since GSON sets the fields directly using reflection.

    private final Long id;
    private Long sequenceId;
    private Integer sequenceIndex;
    private Long creationTimestamp;
    private Long latestChangeTimestamp;
    private LatLon point;
    private Sign sign;
    private Rectangle locationOnPhoto;
    private ValidationStatus validationStatus;
    private final EditStatus editStatus;
    private OsmComparison osmComparison;
    private Collection<OsmElement> osmElements;
    private DetectionMode mode;
    private Author author;
    private Double facing;
    private Double distance;
    private Float angleFromCenter;
    private Float orientation;
    private DetectionConfidenceLevel confidenceLevel;
    private OcrValue ocrValue;
    private DetectionShape shapeOnPhoto;
    private String trackingId;
    private String automaticOcrValue;
    private String manualOcrValue;

    public Detection(final Long id, final EditStatus editStatus) {
        this.id = id;
        this.editStatus = editStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getSequenceId() {
        return sequenceId;
    }

    public Integer getSequenceIndex() {
        return sequenceIndex;
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Long getLatestChangeTimestamp() {
        return latestChangeTimestamp;
    }

    public LatLon getPoint() {
        return point;
    }

    public Sign getSign() {
        return sign;
    }

    public Rectangle getLocationOnPhoto() {
        return locationOnPhoto;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public EditStatus getEditStatus() {
        return editStatus;
    }

    public OsmComparison getOsmComparison() {
        return osmComparison;
    }

    public Collection<OsmElement> getOsmElements() {
        return osmElements;
    }

    public DetectionMode getMode() {
        return mode;
    }

    public Author getAuthor() {
        return author;
    }

    public Double getFacing() {
        return facing;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(final Double distance) {
        this.distance = distance;
    }

    public Float getAngleFromCenter() {
        return angleFromCenter;
    }

    public Float getOrientation() {
        return orientation;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public DetectionConfidenceLevel getConfidenceLevel() {
        return confidenceLevel;
    }

    public String getAutomaticOcrValue() {
        return automaticOcrValue;
    }

    public String getManualOcrValue() {
        return manualOcrValue;
    }

    public OcrValue getOcrValue() {
        return ocrValue;
    }

    public DetectionShape getShapeOnPhoto() {
        return shapeOnPhoto;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(id);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final Detection other = (Detection) obj;
            result = EntityUtil.bothNullOrEqual(id, other.getId());
        }
        return result;
    }

    @Override
    public int compareTo(final Detection detection) {
        return detection.getLocationOnPhoto().surface().compareTo(locationOnPhoto.surface());
    }

    /**
     *  Checks if the detection has only the information needed to be marked on a cropped(front-facing) photo.
     *
     * @return true if the detection contains only information for representation on cropped image; false otherwise
     */
    public boolean containsOnlyFrontFacingCoordinates() {
        return (this.getShapeOnPhoto() == null || !this.getShapeOnPhoto().containsEquirectangularPolygon())
                && this.getLocationOnPhoto() != null;
    }

    public boolean containsEquirectangularPolygonCoordinates() {
        return this.getShapeOnPhoto() != null && this.getShapeOnPhoto().containsEquirectangularPolygon();
    }
}