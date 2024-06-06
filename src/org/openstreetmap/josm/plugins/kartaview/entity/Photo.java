/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import com.grab.josm.common.entity.EntityUtil;
import org.openstreetmap.josm.plugins.kartaview.argument.Projection;


/**
 * Defines the photo business entity. The field names originated from the OSC API and should be kept in order to avoid
 * transformation between notions.
 *
 * @author Beata
 * @version $Revision$
 */
public class Photo {

    private final Long id;
    private final Long sequenceId;
    private final Integer sequenceIndex;
    private final LatLon point;
    private final String name;
    private final String largeThumbnailName;
    private final String thumbnailName;
    private final String oriName;
    private final String wrappedName;
    private final String largeThumbnailWrappedName;
    private final Long timestamp;
    private Double heading;
    private Double gpsAccuracy;
    private String username;
    private String shotDate;
    private List<Detection> detections;
    private Matching matching;
    private PhotoSize size;
    private PhotoSize realSize;
    private final Double horizontalFieldOfView;
    private final UprightOrientation uprightOrientation;
    private final Projection projectionType;


    Photo(final PhotoBuilder builder) {
        this.id = builder.getId();
        this.sequenceId = builder.getSequenceId();
        this.sequenceIndex = builder.getSequenceIndex();
        this.point = builder.getPoint();
        this.name = builder.getName();
        this.largeThumbnailName = builder.getLargeThumbnailName();
        this.thumbnailName = builder.getThumbnailName();
        this.oriName = builder.getOriName();
        this.wrappedName = builder.getWrappedName();
        this.largeThumbnailWrappedName = builder.getLargeThumbnailWrappedName();
        this.timestamp = builder.getTimestamp();
        this.heading = builder.getHeading();
        this.username = builder.getUsername();
        this.shotDate = builder.getShotDate();
        this.size = builder.getSize();
        this.realSize = builder.getRealSize();
        this.horizontalFieldOfView = builder.getHorizontalFieldOfView();
        this.uprightOrientation = builder.getUprightOrientation();
        this.projectionType = builder.getProjection();
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

    public LatLon getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    public String getLargeThumbnailName() {
        return largeThumbnailName;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public String getWrappedName() {
        return wrappedName;
    }

    public String getLargeThumbnailWrappedName() {
        return largeThumbnailWrappedName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Double getHeading() {
        return heading;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Long getWayId() {
        return matching != null ? matching.getWayId() : null;
    }

    public String getShotDate() {
        return shotDate;
    }

    public void setShotDate(final String shotDate) {
        this.shotDate = shotDate;
    }

    public void setDetections(final List<Detection> detections) {
        this.detections = detections;
    }

    public List<Detection> getDetections() {
        return detections;
    }

    public String getOriName() {
        return oriName;
    }

    public Double getGpsAccuracy() {
        return gpsAccuracy;
    }

    public void setHeading(final Double heading) {
        this.heading = heading;
    }

    public Matching getMatching() {
        return matching;
    }

    public void setMatching(final Matching matching) {
        this.matching = matching;
    }

    public PhotoSize getSize() {
        return size;
    }

    public void setSize(final PhotoSize size) {
        this.size = size;
    }

    public PhotoSize getRealSize() {
        return realSize;
    }

    public void setRealSize(final PhotoSize realSize) {
        this.realSize = realSize;
    }

    public Projection getProjectionType() {
        return projectionType;
    }

    public Double getHorizontalFieldOfView() {
        return horizontalFieldOfView;
    }

    public UprightOrientation getUprightOrientation() {
        return uprightOrientation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(sequenceIndex);
        result = prime * result + EntityUtil.hashCode(sequenceId);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final Photo other = (Photo) obj;
            result = EntityUtil.bothNullOrEqual(sequenceId, other.getSequenceId());
            result = result && EntityUtil.bothNullOrEqual(sequenceIndex, other.getSequenceIndex());
        }
        return result;
    }
}