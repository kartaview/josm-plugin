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
 * Builder for the {@code Segment} business entity.
 *
 * @author beataj
 * @version $Revision$
 */
public class SegmentBuilder {

    private String id;
    private Long from;
    private Long to;
    private Long wayId;
    private Integer coverage;
    private List<LatLon> geometry;


    public SegmentBuilder() {}

    public void id(final String id) {
        this.id = id;
    }

    public void from(final Long from) {
        this.from = from;
    }

    public void to(final Long to) {
        this.to = to;
    }

    public void wayId(final Long wayId) {
        this.wayId = wayId;
    }

    public void coverage(final Integer coverage) {
        this.coverage = coverage;
    }

    public void geometry(final List<LatLon> geometry) {
        this.geometry = geometry;
    }

    String getId() {
        return id;
    }

    Long getFrom() {
        return from;
    }

    Long getTo() {
        return to;
    }

    Long getWayId() {
        return wayId;
    }

    Integer getCoverage() {
        return coverage;
    }

    List<LatLon> getGeometry() {
        return geometry;
    }

    public Segment build() {
        return new Segment(this);
    }
}