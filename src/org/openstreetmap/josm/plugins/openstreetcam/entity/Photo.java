/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.data.coor.LatLon;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the photo business entity.
 *
 * @author Beata
 * @version $Revision$
 */
public class Photo {

    private final Long id;
    private final Long sequenceId;
    private final Integer sequenceIndex;
    private final LatLon location;
    private final String name;
    private final String largeThumbnailName;
    private final String thumbnailName;
    private final Long timestamp;
    private final Double heading;
    private String username;
    private Long wayId;
    private String shotDate;


    Photo(final PhotoBuilder builder) {
        this.id = builder.getId();
        this.sequenceId = builder.getSequenceId();
        this.sequenceIndex = builder.getSequenceIndex();
        this.location = builder.getLocation();
        this.name = builder.getName();
        this.largeThumbnailName = builder.getLargeThumbnailName();
        this.thumbnailName = builder.getThumbnailName();
        this.timestamp = builder.getTimestamp();
        this.heading = builder.getHeading();
        this.username = builder.getUsername();
        this.wayId = builder.getWayId();
        this.shotDate = builder.getShotDate();
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

    public LatLon getLocation() {
        return location;
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
        return wayId;
    }

    public void setWayId(final Long wayId) {
        this.wayId = wayId;
    }

    public String getShotDate() {
        return shotDate;
    }

    public void setShotDate(final String shotDate) {
        this.shotDate = shotDate;
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
            final Photo other = (Photo) obj;
            result = EntityUtil.bothNullOrEqual(id, other.getId());
        }
        return result;
    }
}