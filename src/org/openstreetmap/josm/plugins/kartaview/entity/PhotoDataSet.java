/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import org.openstreetmap.josm.plugins.kartaview.util.cnf.KartaViewServiceConfig;
import java.util.ArrayList;
import java.util.List;


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
        return page != null && totalItems > page * KartaViewServiceConfig.getInstance().getNearbyPhotosMaxItems();
    }

    public boolean hasItems() {
        return photos != null && !photos.isEmpty();
    }

    public void addPhotos(final List<Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            this.photos.addAll(photos);
        }
    }

    public void removePhotos(final List<Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            this.photos.removeAll(photos);
        }
    }
}