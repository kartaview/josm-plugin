/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the segment business entity.
 *
 * @author beataj
 * @version $Revision$
 */
public class Segment {

    private final String id;
    private final Long from;
    private final Long to;
    private final Long wayId;
    private final Integer coverage;
    private final List<LatLon> geometry;


    public Segment(final SegmentBuilder builder) {
        this.id = builder.getId();
        this.from = builder.getFrom();
        this.to = builder.getTo();
        this.wayId = builder.getWayId();
        this.coverage = builder.getCoverage();
        this.geometry = builder.getGeometry();
    }


    public String getId() {
        return id;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public Long getWayId() {
        return wayId;
    }

    public Integer getCoverage() {
        return coverage;
    }

    public List<LatLon> getGeometry() {
        return geometry;
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
            final Segment other = (Segment) obj;
            result = EntityUtil.bothNullOrEqual(id, other.getId());
        }
        return result;
    }
}