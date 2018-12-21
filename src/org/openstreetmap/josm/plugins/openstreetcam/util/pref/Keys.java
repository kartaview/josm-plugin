/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref;


/**
 * Holds preference keys.
 *
 * @author Beata
 * @version $Revision$
 */
final class Keys {

    static final String SUPPRESS_PHOTOS_ERROR = "openstreetcam.error.photos.suppress";
    static final String SUPPRESS_SEGMENTS_ERROR = "openstreetcam.error.segments.suppress";
    static final String SUPPRESS_SEQUENCE_ERROR = "openstreetcam.error.sequence.suppress";

    static final String SUPPRESS_PHOTOS_SEARCH_ERROR = "openstreetcam.error.photos.search.suppress";

    static final String SUPPRESS_DETECTIONS_SEARCH_ERROR = "openstreetcam.error.detections.search.suppress";

    static final String SUPPRESS_CLUSTERS_SEARCH_ERROR = "openstreetcam.error.clusters.search.suppress";


    static final String SUPPRESS_SEQUENCE_DETECTIONS_ERROR = "openstreetcam.error.sequence.detections.suppress";
    static final String SUPPRESS_PHOTO_DETECTIONS_ERROR = "openstreetcam.error.photo.detections.suppress";
    static final String SUPPRESS_DETECTION_UPDATE_ERROR = "openstreetcam.error.detection.update.suppress";

    static final String SUPPRESS_LIST_SIGNS_ERROR = "openstreetcam.error.sign.list.suppress";

    static final String FILTER_CHANGED = "openstreetcam.filter.changed";
    static final String FILTER_DATE = "openstreetcam.filter.date";
    static final String FILTER_ONLY_USER_FLAG = "openstreetcam.filter.onlyUserFlag";
    static final String FILTER_SEARCH_PHOTO_TYPE = "openstreetcam.filter.search.photoType";
    static final String FILTER_SEARCH_MODE = "openstreetcam.filter.search.mode";
    static final String FILTER_SEARCH_EDIT_STATUS = "openstreetcam.filter.search.editStatus";
    static final String FILTER_SEARCH_SIGN_TYPE = "openstreetcam.filter.search.signType";
    static final String FILTER_SEARCH_SPECIFIC_SIGN = "openstreetcam.filter.search.specificSign";
    static final String FILTER_SEARCH_EMPTY = "EMPTY";

    static final String LAYER_OPENED = "openstreetcam.layerOpened";
    static final String PHOTO_PANEL_OPENED = "openstreetcam.panelOpened";
    static final String DETECTION_PANEL_OPENED = "openstreetcam.detection.panelOpened";
    static final String PHOTO_PANEL_ICON_VISIBILITY = "openstreetcam_logo_25x25.png.visible";
    static final String DETECTION_PANEL_ICON_VISIBILITY = "openstreetcam_detection.png.visible";
    static final String PLUGIN_LOCAL_VERSION = "openstreetcam.localVersion";

    static final String JOSM_AUTH_METHOD = "osm-server.auth-method";
    static final String JOSM_OAUTH_SECRET = "oauth.access-token.secret";
    static final String JOSM_BASIC_VAL = "basic";

    /* photo related user preference settings */
    static final String HIGH_QUALITY_PHOTO_FLAG = "openstreetcam.preferences.highQuality";
    static final String MOUSE_HOVER_FLAG = "openstreetcam.preferences.mouseHover";
    static final String MOUSE_HOVER_DELAY = "openstreetcam.preferences.mouseHover.delay";

    /* aggregated detections user preference settings */
    static final String DISPLAY_DETECTION_LOCATIONS = "openstreetcam.preferences.aggregated.displayDetectionLoc";

    /* track related user preference settings */
    static final String DISPLAY_TRACK_FLAG = "openstreetcam.preferences.trackLoading";
    static final String AUTOPLAY_LENGTH = "openstreetcam.preferences.autoplay.length";
    static final String FILTER_SEARCH_OSM_COMPARISON = "openstreetcam.filter.search.osmComparison";
    static final String AUTOPLAY_DELAY = "openstreetcam.preferences.autoplay.delay";
    static final String AUTOPLAY_STARTED = "opensteetcam.preferences.autoplay.started";

    /* cache related user preference settings */
    static final String CACHE_MEMORY_COUNT = "openstreetcam.preferences.cache.memory";
    static final String CACHE_DISK_COUNT = "openstreetcam.preferences.cache.disk";
    static final String CACHE_PREV_NEXT_COUNT = "openstreetcam.preferences.cache.prevNext";
    static final String CACHE_NEARBY_COUNT = "openstreetcam.preferences.cache.nearby";

    /* map view related user preference settings */
    static final String MAP_VIEW_PHOTO_ZOOM = "openstreetcam.preferences.mapView.photoZoom";
    static final String MAP_VIEW_MANUAL_SWITCH = "openstreetcam.preferences.mapView.manualSwitch";
    static final String MAP_VIEW_TYPE = "openstreetcam.dataType";


    private Keys() {}
}