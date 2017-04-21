/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.List;


/**
 * Defines the data set business entity. A data set holds the photos/segments from the current view.
 *
 * @author beataj
 * @version $Revision$
 */
public class DataSet {

    private final List<Segment> segments;
    private final List<Photo> photos;


    /**
     * Builds a new object with the given arguments.
     *
     * @param segments a list of {@code Segment}s.
     * @param photos a list of {@code Photo}s
     */
    public DataSet(final List<Segment> segments, final List<Photo> photos) {
        this.segments = segments;
        this.photos = photos;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}