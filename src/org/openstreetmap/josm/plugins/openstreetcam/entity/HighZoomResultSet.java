/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

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

    public HighZoomResultSet() {}

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