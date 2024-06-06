/*
 * Copyright 2019 privatetaxi Holdings PTE LTE (private), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.pref;


import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;


/**
 * Holds preference keys.
 *
 * @author Beata
 * @version $Revision$
 */
final class Keys {

    final static boolean isPrivate = GuiConfig.getInstance().isPrivateBuild();

    static final String SUPPRESS_PHOTOS_ERROR = isPrivate ? "kartaview.private.error.photos.suppress"
            : "kartaview.error.photos.suppress";
    static final String SUPPRESS_SEGMENTS_ERROR = isPrivate ? "kartaview.private.error.segments.suppress"
            : "kartaview.error.segments.suppress";
    static final String SUPPRESS_SEQUENCE_ERROR = isPrivate ? "kartaview.private.error.sequence.suppress"
            : "kartaview.error.sequence.suppress";

    static final String SUPPRESS_PHOTOS_SEARCH_ERROR = isPrivate ? "kartaview.private.error.photos.search.suppress"
            : "kartaview.error.photos.search.suppress";

    static final String SUPPRESS_DETECTIONS_SEARCH_ERROR = isPrivate
            ? "kartaview.private.error.detections.search.suppress" : "kartaview.error.detections.search.suppress";
    static final String SUPPRESS_EDGE_DETECTIONS_SEARCH_ERROR = isPrivate
            ? "kartaview.private.error.edge.detections.search.suppress"
            : "kartaview.error.edge.detections.search.suppress";

    static final String SUPPRESS_CLUSTERS_SEARCH_ERROR = isPrivate ? "kartaview.private.error.clusters.search.suppress"
            : "kartaview.error.clusters.search.suppress";
    static final String SUPPRESS_EDGE_CLUSTERS_SEARCH_ERROR = isPrivate
            ? "kartaview.private.error.edge.clusters.search.suppress" : "kartaview.error.edge.clusters.search.suppress";


    static final String SUPPRESS_SEQUENCE_DETECTIONS_ERROR = isPrivate
            ? "kartaview.private.error.sequence.detections.suppress" : "kartaview.error.sequence.detections.suppress";
    static final String SUPPRESS_PHOTO_DETECTIONS_ERROR = isPrivate
            ? "kartaview.private.error.photo.detections.suppress" : "kartaview.error.photo.detections.suppress";
    static final String SUPPRESS_DETECTION_UPDATE_ERROR = isPrivate
            ? "kartaview.private.error.detection.update.suppress" : "kartaview.error.detection.update.suppress";
    static final String SUPPRESS_EDGE_DATA_OPERATION_ERROR = isPrivate ? "kartaview.private.error.edge.data.suppress"
            : "kartaview.error.edge.data.suppress";

    static final String SUPPRESS_LIST_SIGNS_ERROR = isPrivate ? "kartaview.private.error.sign.list.suppress"
            : "kartaview.error.sign.list.suppress";
    static final String SUPPRESS_LIST_SIGN_REGIONS_ERROR = isPrivate
            ? "kartaview.private.error.sign.region.list.suppress" : "kartaview.error.sign.region.list.suppress";

    static final String FILTER_CHANGED = isPrivate ? "kartaview.private.filter.changed" : "kartaview.filter.changed";
    static final String EDGE_FILTER_CHANGED = isPrivate ? "kartaview.private.edge.filter.changed"
            : "kartaview.edge.filter.changed";
    static final String FILTER_DATE = isPrivate ? "kartaview.private.filter.date" : "kartaview.filter.date";
    static final String FILTER_ONLY_USER_FLAG = isPrivate ? "kartaview.private.filter.onlyUserFlag"
            : "kartaview.filter.onlyUserFlag";
    static final String FILTER_SEARCH_PHOTO_TYPE = isPrivate ? "kartaview.private.filter.search.photoType"
            : "kartaview.filter.search.photoType";
    static final String FILTER_EDGE_SEARCH_DATA_TYPE = isPrivate ? "kartaview.private.filter.edge.search.dataType"
            : "kartaview.filter.edge.search.dataType";
    static final String FILTER_SEARCH_MODE = isPrivate ? "kartaview.private.filter.search.mode"
            : "kartaview.filter.search.mode";
    static final String FILTER_SEARCH_EDIT_STATUS = isPrivate ? "kartaview.private.filter.search.editStatus"
            : "kartaview.filter.search.editStatus";
    static final String FILTER_SEARCH_REGION = isPrivate ? "kartaview.private.filter.search.region"
            : "kartaview.filter.search.region";
    static final String FILTER_EDGE_SEARCH_REGION = isPrivate ? "kartaview.private.filter.edge.search.region"
            : "kartaview.filter.edge.search.region";
    static final String FILTER_SEARCH_SIGN_TYPE = isPrivate ? "kartaview.private.filter.search.signType"
            : "kartaview.filter.search.signType";
    static final String FILTER_EDGE_SEARCH_SIGN_TYPE = isPrivate ? "kartaview.private.filter.edge.search.signType"
            : "kartaview.filter.edge.search.signType";
    static final String FILTER_SEARCH_SPECIFIC_SIGN = isPrivate ? "kartaview.private.filter.search.specificSign"
            : "kartaview.filter.search.specificSign";
    static final String FILTER_EDGE_SEARCH_SPECIFIC_SIGN = isPrivate
            ? "kartaview.private.filter.edge.search.specificSign" : "kartaview.filter.edge.search.specificSign";
    static final String FILTER_SEARCH_MIN_CONFIDENCE_LEVEL = isPrivate
            ? "kartaview.private.filter.search.minConfidenceLvl" : "kartaview.filter.search.minConfidenceLvl";
    static final String FILTER_SEARCH_MAX_CONFIDENCE_LEVEL = isPrivate
            ? "kartaview.private.filter.search.maxConfidenceLvl" : "kartaview.filter.search.maxConfidenceLvl";
    static final String FILTER_SEARCH_EMPTY = "EMPTY";

    static final String FILTER_SEARCH_CONFIDENCE_CATEGORY = isPrivate
            ? "kartaview.private.filter.search.confidenceCategory" : "kartaview.filter.search.confidenceCategory";
    static final String FILTER_EDGE_SEARCH_CONFIDENCE_CATEGORY = isPrivate
            ? "kartaview.private.filter.edge.search.confidenceCategory"
            : "kartaview.filter.edge.search.confidenceCategory";
    static final String KARTAVIEW_LAYER_OPENED = isPrivate ? "kartaview.private.layerOpened" : "kartaview.layerOpened";
    static final String EDGE_LAYER_OPENED = "edge.layerOpened";
    static final String PHOTO_PANEL_OPENED = isPrivate ? "kartaview.private.panelOpened" : "kartaview.panelOpened";
    static final String DETECTION_PANEL_OPENED = isPrivate ? "kartaview.private.detection.panelOpened"
            : "kartaview.detection.panelOpened";
    static final String EDGE_DETECTION_PANEL_OPENED = "edge.detection.panelOpened";
    static final String PHOTO_PANEL_ICON_VISIBILITY = "kartaview_logo_25x25.svg.visible";
    static final String DETECTION_PANEL_ICON_VISIBILITY = "kartaview_detection.svg.visible";
    static final String EDGE_DETECTION_PANEL_ICON_VISIBILITY = "kartaview_edge_detection.svg.visible";
    static final String PLUGIN_LOCAL_VERSION = isPrivate ? "kartaview.private.localVersion" : "kartaview.localVersion";

    static final String JOSM_AUTH_METHOD = "osm-server.auth-method";
    static final String JOSM_OAUTH_SECRET = "oauth.access-token.secret";
    static final String JOSM_BASIC_VAL = "basic";

    static final String ACCESS_TOKEN = isPrivate ? "kartaview.private.preferences.accessToken"
            : "kartaview.preferences.accessToken";

    /* photo related user preference settings */
    static final String HIGH_QUALITY_PHOTO_FLAG = isPrivate ? "kartaview.private.preferences.highQuality"
            : "kartaview.preferences.highQuality";
    static final String MOUSE_HOVER_FLAG = isPrivate ? "kartaview.private.preferences.mouseHover"
            : "kartaview.preferences.mouseHover";
    static final String MOUSE_HOVER_DELAY = isPrivate ? "kartaview.private.preferences.mouseHover.delay"
            : "kartaview.preferences.mouseHover.delay";
    static final String DISPLAY_FRONT_FACING_FLAG = isPrivate ? "kartaview.private.preferences.display.front.facing"
            : "kartaview.preferences.display.front.facing";

    /* cluster user preference settings */
    static final String DISPLAY_DETECTION_LOCATIONS = isPrivate
            ? "kartaview.private.preferences.cluster.displayDetectionLoc"
            : "kartaview.preferences.cluster.displayDetectionLoc";
    static final String DISPLAY_TAGS = isPrivate ? "kartaview.private.preferences.cluster.displayTags"
            : "kartaview.preferences.cluster.displayTags";
    static final String DISPLAY_COLOR_CODED = isPrivate ? "kartaview.private.preferences.cluster.displayColorCoded"
            : "kartaview.preferences.cluster.displayColorCoded";

    /* track related user preference settings */
    static final String DISPLAY_TRACK_FLAG = isPrivate ? "kartaview.private.preferences.trackLoading"
            : "kartaview.preferences.trackLoading";
    static final String AUTOPLAY_LENGTH = isPrivate ? "kartaview.private.preferences.autoplay.length"
            : "kartaview.preferences.autoplay.length";
    static final String FILTER_SEARCH_OSM_COMPARISON = isPrivate ? "kartaview.private.filter.search.osmComparison"
            : "kartaview.filter.search.osmComparison";
    static final String FILTER_EDGE_SEARCH_OSM_COMPARISON = isPrivate
            ? "kartaview.private.filter.edge.search.osmComparison" : "kartaview.filter.edge.search.osmComparison";
    static final String AUTOPLAY_DELAY = isPrivate ? "kartaview.private.preferences.autoplay.delay"
            : "kartaview.preferences.autoplay.delay";
    static final String AUTOPLAY_STARTED = isPrivate ? "kartaview.private.preferences.autoplay.started"
            : "kartaview.preferences.autoplay.started";

    /* cache related user preference settings */
    static final String CACHE_MEMORY_COUNT = isPrivate ? "kartaview.private.preferences.cache.memory"
            : "kartaview.preferences.cache.memory";
    static final String CACHE_DISK_COUNT = isPrivate ? "kartaview.private.preferences.cache.disk"
            : "kartaview.preferences.cache.disk";
    static final String CACHE_PREV_NEXT_COUNT = isPrivate ? "kartaview.private.preferences.cache.prevNext"
            : "kartaview.preferences.cache.prevNext";
    static final String CACHE_NEARBY_COUNT = isPrivate ? "kartaview.private.preferences.cache.nearby"
            : "kartaview.preferences.cache.nearby";

    /* map view related user preference settings */
    static final String MAP_VIEW_PHOTO_ZOOM = isPrivate ? "kartaview.private.preferences.mapView.photoZoom"
            : "kartaview.preferences.mapView.photoZoom";
    static final String MAP_VIEW_DATA_LOAD = isPrivate ? "kartaview.private.preferences.mapView.dataLoad"
            : "kartaview.preferences.mapView.dataLoad";

    private Keys() {
    }
}