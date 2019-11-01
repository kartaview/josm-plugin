/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.data.coor.LatLon;


/**
 * Defines the matching entity.
 *
 * @author beataj
 * @version $Revision$
 */
public class Matching {

    private final Long wayId;
    private final Long from;
    private final Long to;
    private final LatLon point;


    public Matching(final Long wayId, final Long from, final Long to, final LatLon point) {
        this.wayId = wayId;
        this.from = from;
        this.to = to;
        this.point = point;
    }

    public Long getWayId() {
        return wayId;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public LatLon getPoint() {
        return point;
    }
}