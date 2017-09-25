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


/**
 * Defines the sequence business entity.
 *
 * @author beataj
 * @version $Revision$
 */
public class Sequence {

    private final Long id;
    private final List<Photo> photos;


    public Sequence(final Long id, final List<Photo> photos) {
        this.id = id;
        this.photos = photos;
    }


    public Long getId() {
        return id;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public boolean hasPhotos() {
        return photos != null && !photos.isEmpty();
    }
}