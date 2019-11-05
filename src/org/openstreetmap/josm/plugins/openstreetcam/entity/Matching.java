/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
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