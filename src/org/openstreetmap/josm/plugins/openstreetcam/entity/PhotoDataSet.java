/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.OpenStreetCamServiceConfig;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoDataSet {

    private final List<Photo> photos;
    private Integer page;
    private Integer totalItems;


    public PhotoDataSet() {
        this.photos = new ArrayList<>();
    }

    public PhotoDataSet(final List<Photo> photos) {
        this.photos = photos;
    }


    public PhotoDataSet(final List<Photo> photos, final Integer page, final Integer totalItems) {
        this.photos = photos;
        this.page = page;
        this.totalItems = totalItems;
    }

    public List<Photo> getPhotos() {
        return photos;
    }


    public Integer getPage() {
        return page;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public boolean hasPreviousItems() {
        return page != null && page > 1;
    }

    public boolean hasNextItems() {
        return page != null && totalItems > page * OpenStreetCamServiceConfig.getInstance().getNearbyPhotosMaxItems();
    }

    public boolean hasItems() {
        return photos != null && !photos.isEmpty();
    }

    public void addPhotos(final List<Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            this.photos.addAll(photos);
        }
    }
}