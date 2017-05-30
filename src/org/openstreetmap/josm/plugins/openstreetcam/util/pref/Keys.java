/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
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
    static final String SUPPRESS_SEQUENCE_ERROR = "openstreetcam.error.sequence.suppress";
    static final String FILTERS_CHANGED = "openstreetcam.filter.changed";
    static final String FILTER_DATE = "openstreetcam.filter.date";
    static final String FILTER_ONLY_USER_FLAG = "openstreetcam.filter.onlyUserFlag";
    static final String LAYER_OPENED = "openstreetcam.layerOpened";
    static final String PANEL_OPENED = "openstreetcam.panelOpened";
    static final String PANEL_ICON_VISIBILITY = "openstreetcam_logo_25x25.png.visible";
    static final String PLUGIN_LOCAL_VERSION = "openstreetcam.localVersion";

    static final String JOSM_AUTH_METHOD = "osm-server.auth-method";
    static final String JOSM_OAUTH_SECRET = "oauth.access-token.secret";
    static final String JOSM_BASIC_VAL = "basic";

    /* photo related user preference settings */
    static final String HIGH_QUALITY_PHOTO_FLAG = "openstreetcam.preferences.highQuality";
    static final String DISPLAY_TRACK_FLAG = "openstreetcam.preferences.trackLoading";
    static final String MOUSE_HOVER_FLAG = "openstreetcam.preferences.mouseHover";
    static final String MOUSE_HOVER_DELAY = "openstreetcam.preferences.mouseHover.delay";

    /* cache related user preference settings */
    static final String CACHE_MEMORY_COUNT = "openstreetcam.preferences.cache.memory";
    static final String CACHE_DISK_COUNT = "openstreetcam.preferences.cache.disk";
    static final String CACHE_PREV_NEXT_COUNT = "openstreetcam.preferences.cache.prevNext";
    static final String CACHE_NEARBY_COUNT = "openstreetcam.preferences.cache.nearby";

    /* map view related user preference settings */
    static final String MAP_VIEW_PHOTO_ZOOM = "openstreetcam.preferences.mapView.photoZoom";
    static final String MAP_VIEW_MANUAL_SWITCH = "openstreetcam.preferences.mapView.manualSwitch";
    static final String DATA_TYPE = "openstreetcam.dataType";

    private Keys() {}
}