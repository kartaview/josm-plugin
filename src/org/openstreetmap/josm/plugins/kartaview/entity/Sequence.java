/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Defines the sequence business entity.
 *
 * @author beataj
 * @version $Revision$
 */
public class Sequence {

    private final Long id;
    private final List<Photo> photos;
    private final Set<Detection> detections;

    public Sequence(final Long id, final List<Photo> photos, final List<Detection> detections) {
        this.id = id;
        this.photos = photos;
        this.detections = new HashSet<>(detections);
    }

    public Long getId() {
        return id;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void addDetections(List<Detection> detections) {
        this.detections.addAll(detections);
    }

    public Set<Detection> getDetections() {
        return detections;
    }

    public boolean hasPhotos() {
        return photos != null && !photos.isEmpty();
    }

    public boolean hasDetections() {
        return detections != null && !detections.isEmpty();
    }

    public boolean hasData() {
        return hasPhotos() || hasDetections();
    }
}