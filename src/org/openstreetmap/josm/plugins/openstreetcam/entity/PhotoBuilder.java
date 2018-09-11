/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.List;
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
    private LatLon point;
    private String name;
    private String largeThumbnailName;
    private String thumbnailName;
    private String oriName;
    private Long timestamp;
    private Double heading;
    private String username;
    private Long wayId;
    private String shotDate;
    private List<Detection> detections;


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

    public void point(final Double latitude, final Double longitude) {
        this.point = new LatLon(latitude, longitude);
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

    public void wayId(final Long wayId) {
        this.wayId = wayId;
    }

    public void shotDate(final String shotDate) {
        this.shotDate = shotDate;
    }

    public void detections(final List<Detection> detections) {
        this.detections = detections;
    }

    public void oriName(final String oriName) {
        this.oriName = oriName;
    }

    Long getId() {
        return id;
    }

    Long getSequenceId() {
        return sequenceId;
    }

    Integer getSequenceIndex() {
        return sequenceIndex;
    }

    LatLon getPoint() {
        return point;
    }

    String getName() {
        return name;
    }

    String getLargeThumbnailName() {
        return largeThumbnailName;
    }

    String getThumbnailName() {
        return thumbnailName;
    }

    Long getTimestamp() {
        return timestamp;
    }

    Double getHeading() {
        return heading;
    }

    String getUsername() {
        return username;
    }

    Long getWayId() {
        return wayId;
    }

    String getShotDate() {
        return shotDate;
    }

    List<Detection> getDetections() {
        return detections;
    }

    String getOriName() {
        return oriName;
    }

    public Photo build() {
        return new Photo(this);
    }
}