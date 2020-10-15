/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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
    private List<Photo> photosToBeDrawn;
    private Integer page;
    private Integer totalItems;


    public PhotoDataSet() {
        this.photos = new ArrayList<>();
        this.photosToBeDrawn = new ArrayList<>();
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

    public List<Photo> getPhotosToBeDrawn() {
        return photosToBeDrawn;
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

    public void revalidatePhotosToBeDrawn(final List<Photo> photos) {
        this.photosToBeDrawn = new ArrayList<>();
        if (photos != null && !photos.isEmpty()) {
            this.photosToBeDrawn.addAll(photos);
        }
    }
}