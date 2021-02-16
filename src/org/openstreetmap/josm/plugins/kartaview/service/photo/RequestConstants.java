/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.photo;


/**
 * Holds paramters and method names.
 *
 * @author Beata
 * @version $Revision$
 */
final class RequestConstants {

    static final String LIST_NEARBY_PHOTOS = "/list/nearby-photos/";
    static final String SEQUENCE_PHOTO_LIST = "/sequence/photo-list/";
    static final String LIST_MATCHED_TRACKS = "tracks";
    static final String PHOTO_DETAILS = "/photo/full-details/";
    static final String COORDINATE = "coordinate";
    static final String RADIUS = "radius";
    static final String PAGE = "page";
    static final String PAGE_ITEMS = "ipp";
    static final String DATE = "date";
    static final String SEQUENCE_ID = "sequenceId";
    static final String SEQUENCE_INDEX = "sequenceIndex";
    static final String BBOX_TOP_LEFT = "bbTopLeft";
    static final String BBOX_BOTTOM_RIGHT = "bbBottomRight";
    static final String ZOOM = "zoom";
    static final String MY_TRACKS = "myTracks";
    static final String MY_TRACKS_VAL = "true";
    static final String USER_TYPE = "filterUserType";
    static final String USER_TYPE_OSM = "osm";
    static final String EXTERNAL_USER_ID = "filterExternalUserId";

    private RequestConstants() {}
}