/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import java.awt.geom.Point2D;


/**
 * Defines the rectangle business entity; represents the detection position in a photo.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Rectangle {

    private final Double x;
    private final Double y;
    private final Double width;
    private final Double height;


    public Rectangle(final Double x, final Double y, final Double width, final Double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public boolean contains(final Point2D point) {
        return point.getX() >= x && point.getX() <= x + width && point.getY() >= y && point.getY() <= y + height;
    }

    public Double surface() {
        return width * height;
    }
}