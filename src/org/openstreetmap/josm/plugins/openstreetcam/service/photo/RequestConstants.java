/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.photo;


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
    static final String PHOTO_DETAILS = "/photo/details/";
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