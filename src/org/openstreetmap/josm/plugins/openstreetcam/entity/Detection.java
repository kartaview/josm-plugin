package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 * Defines the detection entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Detection {

    private final Long id;
    private final Long sequenceId;
    private final Long sequenceIndex;
    private final Long creationTimestamp;
    private final Long latestChangeTimestamp;
    private final Point point;
    private final Sign sign;
    private final Rectangle locationOnPhoto;
    private final Double confidenceLevel;
    private final ValidationStatus validationStatus;
    private final EditStatus editStatus;
    private final OsmComparison osmComparison;
    private final OsmElement osmElement;
    private final DetectionMode mode;
    private final Author author;

    
    public Detection(final Long id, final Long sequenceId, final Long sequenceIndex, final Long creationTimestamp, final Long latestChangeTimestamp,
            final Point point, final Sign sign, final Rectangle locationOnPhoto, final Double confidenceLevel,
            final ValidationStatus validationStatus, final EditStatus editStatus, final OsmComparison osmComparison,
            final OsmElement osmElement, final DetectionMode mode, final Author author) {
        super();
        this.id = id;
        this.sequenceId = sequenceId;
        this.sequenceIndex = sequenceIndex;
        this.creationTimestamp = creationTimestamp;
        this.latestChangeTimestamp = latestChangeTimestamp;
        this.point = point;
        this.sign = sign;
        this.locationOnPhoto = locationOnPhoto;
        this.confidenceLevel = confidenceLevel;
        this.validationStatus = validationStatus;
        this.editStatus = editStatus;
        this.osmComparison = osmComparison;
        this.osmElement = osmElement;
        this.mode = mode;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public Long getSequenceId() {
        return sequenceId;
    }

    public Long getSequenceIndex() {
        return sequenceIndex;
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Long getLatestChangeTimestamp() {
        return latestChangeTimestamp;
    }

    public Point getPoint() {
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
}