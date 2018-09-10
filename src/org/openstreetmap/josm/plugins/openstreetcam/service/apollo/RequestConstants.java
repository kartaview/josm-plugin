/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;


/**
 *
 * @author beataj
 * @version $Revision$
 */
final class RequestConstants {

    static final String SEARCH_DETECTIONS = "searchDetections";
    static final String UPDATE_DETECTION = "updateDetection";
    static final String RETRIEVE_SEQUENCE_DETECTIONS = "retrieveSequenceDetections";
    static final String RETRIEVE_PHOTO_DETECTIONS = "retrievePhotoDetections";
    static final String RETRIVE_DETECTION = "retrieveDetection";
    static final String SEARCH_CLUSTERS = "searchClusters";
    static final String RETRIEVE_CLUSTER_DETECTIONS = "retrieveClusterDetections";
    static final String RETRIEVE_CLUSTER_PHOTOS = "retrieveClusterPhotos";

    static final String NORTH = "north";
    static final String SOUTH = "south";
    static final String EAST = "east";
    static final String WEST = "west";

    static final String FORMAT = "format";
    static final String FORMAT_VAL = "json";
    static final String EXTERNAL_ID = "externalId";
    static final String AUTHOR_TYPE = "authorType";
    static final String AUTHOR_TYPE_VAL = "OSM";
    static final String DATE = "date";
    static final String OSM_COMPARISONS = "osmComparisons";
    static final String EDIT_STATUSES = "editStatuses";
    static final String MODES = "detectionModes";
    static final String VALIDATION_STATUSES = "validationStatuses";
    static final String INCLUDED_SIGN_TYPES = "includedSignTypes";
    static final String EXCLUDED_SIGN_TYPES = "excludedSignTypes";
    static final String SEQUENCE_ID = "sequenceId";
    static final String SEQUENCE_INDEX = "sequenceIndex";
    static final String EDIT_STATUS_FIXED = "FIXED";
    static final String EDIT_STATUS_ALREADY_FIXED = "ALREADY_FIXED";
    static final String ID = "id";

    static final String BLURRING_TYPE = "BLURRING";

    private RequestConstants() {}
}