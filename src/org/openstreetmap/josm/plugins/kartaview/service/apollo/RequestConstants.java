/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public final class RequestConstants {

    static final String SEARCH_DETECTIONS = "searchDetections";
    static final String UPDATE_DETECTION = "updateDetection";
    static final String RETRIEVE_SEQUENCE_DETECTIONS = "retrieveSequenceDetections";
    static final String RETRIEVE_PHOTO_DETECTIONS = "retrievePhotoDetections";
    static final String RETRIEVE_DETECTION = "retrieveDetection";
    static final String SEARCH_CLUSTERS = "searchClusters";
    static final String RETRIEVE_CLUSTER = "retrieveCluster";
    static final String RETRIEVE_CLUSTER_DETECTIONS = "retrieveClusterDetections";
    static final String RETRIEVE_CLUSTER_PHOTOS = "retrieveClusterPhotos";
    static final String RETRIEVE_PHOTO = "retrievePhoto";
    static final String LIST_SIGNS = "listSigns";
    static final String LIST_REGIONS = "listSignRegions";

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
    static final String REGION = "signRegion";
    static final String SEQUENCE_ID = "sequenceId";
    static final String SEQUENCE_INDEX = "sequenceIndex";
    static final String EDIT_STATUS_FIXED = "FIXED";
    static final String EDIT_STATUS_ALREADY_FIXED = "ALREADY_FIXED";
    static final String ID = "id";
    static final String SIGN_INTERNAL_NAMES = "signInternalNames";
    static final String MIN_CONFIDENCE_LEVEL = "minConfidenceLevel";
    static final String MAX_CONFIDENCE_LEVEL = "maxConfidenceLevel";

    public static final String BLURRING_TYPE = "BLURRING";
    public static final String PHOTO_QUALITY_TYPE = "PHOTO_QUALITY";
    public static final String POI = "POI";

    public RequestConstants() {}
}