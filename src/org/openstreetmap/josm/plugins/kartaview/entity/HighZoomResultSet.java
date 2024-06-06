/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import java.util.List;


/**
 * Represents the result set for the high zoom level.
 *
 * @author beataj
 * @version $Revision$
 */
public class HighZoomResultSet {

    private PhotoDataSet photoDataSet;
    private List<Detection> detections;
    private List<Cluster> clusters;

    public HighZoomResultSet() {
    }

    public HighZoomResultSet(final PhotoDataSet photoDataSet, final List<Detection> detections,
            final List<Cluster> clusters) {
        this.photoDataSet = photoDataSet;
        this.detections = detections;
        this.clusters = clusters;
    }

    public PhotoDataSet getPhotoDataSet() {
        return photoDataSet;
    }

    public List<Detection> getDetections() {
        return detections;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }
}