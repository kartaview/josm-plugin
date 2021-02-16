/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.pref;


/**
 * Holds preference keys.
 *
 * @author Beata
 * @version $Revision$
 */
final class Keys {

    static final String SUPPRESS_PHOTOS_ERROR = "kartaview.error.photos.suppress";
    static final String SUPPRESS_SEGMENTS_ERROR = "kartaview.error.segments.suppress";
    static final String SUPPRESS_SEQUENCE_ERROR = "kartaview.error.sequence.suppress";

    static final String SUPPRESS_PHOTOS_SEARCH_ERROR = "kartaview.error.photos.search.suppress";

    static final String SUPPRESS_DETECTIONS_SEARCH_ERROR = "kartaview.error.detections.search.suppress";

    static final String SUPPRESS_CLUSTERS_SEARCH_ERROR = "kartaview.error.clusters.search.suppress";


    static final String SUPPRESS_SEQUENCE_DETECTIONS_ERROR = "kartaview.error.sequence.detections.suppress";
    static final String SUPPRESS_PHOTO_DETECTIONS_ERROR = "kartaview.error.photo.detections.suppress";
    static final String SUPPRESS_DETECTION_UPDATE_ERROR = "kartaview.error.detection.update.suppress";

    static final String SUPPRESS_LIST_SIGNS_ERROR = "kartaview.error.sign.list.suppress";
    static final String SUPPRESS_LIST_SIGN_REGIONS_ERROR = "kartaview.error.sign.region.list.suppress";

    static final String FILTER_CHANGED = "kartaview.filter.changed";
    static final String FILTER_DATE = "kartaview.filter.date";
    static final String FILTER_ONLY_USER_FLAG = "kartaview.filter.onlyUserFlag";
    static final String FILTER_SEARCH_PHOTO_TYPE = "kartaview.filter.search.photoType";
    static final String FILTER_SEARCH_MODE = "kartaview.filter.search.mode";
    static final String FILTER_SEARCH_EDIT_STATUS = "kartaview.filter.search.editStatus";
    static final String FILTER_SEARCH_REGION = "kartaview.filter.search.region";
    static final String FILTER_SEARCH_SIGN_TYPE = "kartaview.filter.search.signType";
    static final String FILTER_SEARCH_SPECIFIC_SIGN = "kartaview.filter.search.specificSign";
    static final String FILTER_SEARCH_MIN_CONFIDENCE_LEVEL = "kartaview.filter.search.minConfidenceLvl";
    static final String FILTER_SEARCH_MAX_CONFIDENCE_LEVEL = "kartaview.filter.search.maxConfidenceLvl";
    static final String FILTER_SEARCH_EMPTY = "EMPTY";

    static final String LAYER_OPENED = "kartaview.layerOpened";
    static final String PHOTO_PANEL_OPENED = "kartaview.panelOpened";
    static final String DETECTION_PANEL_OPENED = "kartaview.detection.panelOpened";
    static final String PHOTO_PANEL_ICON_VISIBILITY = "kartaview_logo_25x25.svg.visible";
    static final String DETECTION_PANEL_ICON_VISIBILITY = "kartaview_detection.svg.visible";
    static final String PLUGIN_LOCAL_VERSION = "kartaview.localVersion";

    static final String JOSM_AUTH_METHOD = "osm-server.auth-method";
    static final String JOSM_OAUTH_SECRET = "oauth.access-token.secret";
    static final String JOSM_BASIC_VAL = "basic";

    /* photo related user preference settings */
    static final String HIGH_QUALITY_PHOTO_FLAG = "kartaview.preferences.highQuality";
    static final String MOUSE_HOVER_FLAG = "kartaview.preferences.mouseHover";
    static final String MOUSE_HOVER_DELAY = "kartaview.preferences.mouseHover.delay";
    static final String DISPLAY_FRONT_FACING_FLAG = "kartaview.preferences.display.front.facing";

    /* aggregated detections user preference settings */
    static final String DISPLAY_DETECTION_LOCATIONS = "kartaview.preferences.aggregated.displayDetectionLoc";
    static final String DISPLAY_TAGS = "kartaview.preferences.aggregated.displayTags";
    static final String DISPLAY_COLOR_CODED = "kartaview.preferences.aggregated.displayColorCoded";

    /* track related user preference settings */
    static final String DISPLAY_TRACK_FLAG = "kartaview.preferences.trackLoading";
    static final String AUTOPLAY_LENGTH = "kartaview.preferences.autoplay.length";
    static final String FILTER_SEARCH_OSM_COMPARISON = "kartaview.filter.search.osmComparison";
    static final String AUTOPLAY_DELAY = "kartaview.preferences.autoplay.delay";
    static final String AUTOPLAY_STARTED = "kartaview.preferences.autoplay.started";

    /* cache related user preference settings */
    static final String CACHE_MEMORY_COUNT = "kartaview.preferences.cache.memory";
    static final String CACHE_DISK_COUNT = "kartaview.preferences.cache.disk";
    static final String CACHE_PREV_NEXT_COUNT = "kartaview.preferences.cache.prevNext";
    static final String CACHE_NEARBY_COUNT = "kartaview.preferences.cache.nearby";

    /* map view related user preference settings */
    static final String MAP_VIEW_PHOTO_ZOOM = "kartaview.preferences.mapView.photoZoom";
    static final String MAP_VIEW_MANUAL_SWITCH = "kartaview.preferences.mapView.manualSwitch";
    static final String MAP_VIEW_DATA_LOAD = "kartaview.preferences.mapView.dataLoad";
    static final String MAP_VIEW_TYPE = "kartaview.dataType";


    private Keys() {}
}