/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Projection;


/**
 * Builder for the {@code Photo} business entity.
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoBuilder {

    /** photo type  */
    private static final String WRAPPED = "wrapped_proc";
    private static final String WRAPPED_LARGE = "wrapped_lth";
    private static final String PROC = "proc";
    private static final String LARGE = "lth";

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
    private PhotoSize size;
    private PhotoSize realSize;
    private Double horizontalFieldOfView;
    private UprightOrientation uprightOrientation;
    private Projection projection;


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

    public void point(final LatLon point) {
        this.point = point;
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

    public void oriName(final String oriName) {
        this.oriName = oriName;
    }

    public void size(final PhotoSize size) {
        this.size = size;
    }

    public void projection(final Projection projection) {
        this.projection = projection;
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

    String getOriName() {
        return oriName;
    }

    public PhotoSize getSize() {
        return size;
    }

    public PhotoSize getRealSize() {
        return realSize;
    }

    public Double getHorizontalFieldOfView() {
        return horizontalFieldOfView;
    }

    public UprightOrientation getUprightOrientation() {
        return uprightOrientation;
    }

    Projection getProjection() {
        return projection;
    }

    public Photo build() {
        if (this.getProjection().equals(Projection.SPHERE)) {
            visualiseWrappedPhoto(this);
        }
        // any photo should have a coordinate
        return point != null ? new Photo(this) : null;
    }

    private void visualiseWrappedPhoto(final PhotoBuilder builder) {
        final String currentName = builder.getName();
        final String currentLargeThumbnailName = builder.getLargeThumbnailName();
        builder.name(currentName.replace(PROC, WRAPPED));
        builder.largeThumbnailName(currentLargeThumbnailName.replace(LARGE, WRAPPED_LARGE));
    }
}