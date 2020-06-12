package org.openstreetmap.josm.plugins.openstreetcam.entity;

/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */

/**
 * @author nicoleta.viregan
 */
public class PhotoSize {
    private final Long width;
    private final Long height;

    public PhotoSize(final Long width, final Long height) {
        this.width = width;
        this.height = height;
    }

    public Long getWidth() {
        return width;
    }

    public Long getHeight() {
        return height;
    }
}