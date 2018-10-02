package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.data.coor.LatLon;
import com.telenav.josm.common.entity.EntityUtil;


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
    private Double confidenceLevel;
    private ValidationStatus validationStatus;
    private final EditStatus editStatus;
    private OsmComparison osmComparison;
    private OsmElement osmElement;
    private DetectionMode mode;
    private Author author;


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

    public Double getConfidenceLevel() {
        return confidenceLevel;
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

    public OsmElement getOsmElement() {
        return osmElement;
    }

    public DetectionMode getMode() {
        return mode;
    }

    public Author getAuthor() {
        return author;
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
}