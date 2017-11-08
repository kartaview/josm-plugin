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


/**
 * Defines the data set business entity. A data set holds the photos/segments from the current view.
 *
 * @author beataj
 * @version $Revision$
 */
public class DataSet {

    private final List<Segment> segments;
    private final PhotoDataSet photoDataSet;
    private final List<Detection> detections;


    /**
     * Builds a new object with the given arguments.
     *
     * @param segments represents the data for small zoom levels
     * @param photoDataSet represents the data for bigger zoom levels
     */
    public DataSet(final List<Segment> segments, final PhotoDataSet photoDataSet, final List<Detection> detections) {
        this.segments = segments;
        this.photoDataSet = photoDataSet;
        this.detections = detections;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public PhotoDataSet getPhotoDataSet() {
        return photoDataSet;
    }

    /**
     * Returns a list of individual photo locations. The method returns an empty list if the photo data set does not
     * contain any photo locations.
     *
     * @return a list of {@code Photo}s
     */
    public List<Photo> getPhotos() {
        return photoDataSet != null ? photoDataSet.getPhotos() : new ArrayList<>();
    }

    public List<Detection> getDetections() {
        return detections;
    }
}