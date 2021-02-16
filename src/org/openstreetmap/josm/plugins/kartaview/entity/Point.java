/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;


/**
 * Defines the point entity which represents a gps position.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Point {

    private final Double lat;
    private final Double lon;

    public Point(final Double lat, final Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }
}