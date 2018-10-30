/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity;

import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.BaseResponse;
import com.telenav.josm.common.entity.Status;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class Response extends BaseResponse {

    // setters and constructors are not required,since GSON sets the fields directly using reflection.
    private Detection detection;
    private Cluster cluster;
    private List<Detection> detections;
    private List<Cluster> clusters;
    private List<Photo> photos;
    private List<Sign> signs;


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

    public List<Cluster> getClusters() {
        return clusters;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public List<Sign> getSigns() {
        return signs;
    }
}