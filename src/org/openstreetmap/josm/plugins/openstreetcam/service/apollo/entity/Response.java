/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity;

import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.BaseResponse;
import com.grab.josm.common.entity.Status;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class Response extends BaseResponse {

    // setters and constructors are not required,since GSON sets the fields directly using reflection.
    private Detection detection;
    private Cluster cluster;
    private Photo photo;
    private List<Detection> detections;
    private List<Cluster> clusters;
    private List<Photo> photos;
    private List<Sign> signs;
    private List<String> regions;


    public Response(final Status status) {
        super(status);
    }

    public List<Detection> getDetections() {
        return detections;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public Detection getDetection() {
        return detection;
    }

    public Photo getPhoto() {
        return photo;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public List<Sign> getSigns() {
        return signs;
    }

    public List<String> getRegions() {
        return regions;
    }
}