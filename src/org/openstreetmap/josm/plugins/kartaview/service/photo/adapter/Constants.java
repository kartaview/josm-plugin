/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.photo.adapter;


/**
 * Holds object field names.
 *
 * @author beataj
 * @version $Revision$
 */
final class Constants {

    /* photo object fields */
    static final String PHOTO_ID = "id";
    static final String PHOTO_SEQUENCE_ID = "sequence_id";
    static final String PHOTO_SEQUENCE_IDX = "sequence_index";
    static final String PHOTO_LATITUDE = "lat";
    static final String PHOTO_LONGITUDE = "lng";
    static final String PHOTO_NAME = "name";
    static final String PHOTO_LTH_NAME = "lth_name";
    static final String PHOTO_TH_NAME = "th_name";
    static final String PHOTO_ORI_NAME = "ori_name";
    static final String PHOTO_TIMESTAMP = "timestamp";
    static final String PHOTO_HEADING = "heading";
    static final String PHOTO_USERNAME = "username";
    static final String PHOTO_SHOT_DATE = "shot_date";
    static final String PROJECTION = "projection";

    /* segment object fields */
    static final String SEGMENT_ID = "element_id";
    static final String SEGMENT_FROM = "from";
    static final String SEGMENT_TO = "to";
    static final String SEGMENT_COVERAGE = "coverage";
    static final String SEGMENT_GEOMETRY = "track";


    /* commonly used fields */
    static final String WAY_ID = "way_id";

    private Constants() {}
}