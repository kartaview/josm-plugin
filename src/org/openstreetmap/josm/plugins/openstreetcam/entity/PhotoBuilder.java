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


/**
 * Builder for the {@code Photo} business entity.
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoBuilder {

    private Long id;
    private Long sequenceId;
    private Integer sequenceIndex;
    private LatLon location;
    private String name;
    private String largeThumbnailName;
    private String thumbnailName;
    private Long timestamp;
    private Double heading;
    private String username;
    private Long wayId;
    private String shotDate;


    public PhotoBuilder() {}

    public void id(final Long id) {
        this.id = id;
    }

    public void sequenceId(final Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    public void sequenceIndex(final Integer sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
    }

    public void location(final Double latitude, final Double longitude) {
        this.location = new LatLon(latitude, longitude);
    }

    public void name(final String name) {
        this.name = name;
    }

    public void largeThumbnailName(final String largeThumbnailName) {
        this.largeThumbnailName = largeThumbnailName;
    }

    public void thumbnailName(final String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public void timestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    public void heading(final Double heading) {
        this.heading = heading;
    }

    public void username(final String username) {
        this.username = username;
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

    public Long getWayId() {
        return wayId;
    }

    public void wayId(final Long wayId) {
        this.wayId = wayId;
    }

    public String getShotDate() {
        return shotDate;
    }

    public void shotDate(final String shotDate) {
        this.shotDate = shotDate;
    }

    public Photo build() {
        return new Photo(this);
    }
}