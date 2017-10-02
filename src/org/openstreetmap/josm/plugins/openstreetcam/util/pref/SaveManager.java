/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref;

import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.AUTOPLAY_DELAY;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.AUTOPLAY_LENGTH;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.AUTOPLAY_STARTED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.CACHE_DISK_COUNT;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.CACHE_MEMORY_COUNT;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.CACHE_NEARBY_COUNT;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.CACHE_PREV_NEXT_COUNT;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.DATA_TYPE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.DISPLAY_TRACK_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTERS_CHANGED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_DATE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_ONLY_USER_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.HIGH_QUALITY_PHOTO_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.LAYER_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_MANUAL_SWITCH;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_PHOTO_ZOOM;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_DELAY;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.PANEL_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.PLUGIN_LOCAL_VERSION;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_PHOTOS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEGMENTS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEQUENCE_ERROR;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.TrackSettings;
import org.openstreetmap.josm.spi.preferences.Config;


/**
 * Helper class, manages the save operations of the plugin preference variables. The preference variables are saved into
 * a global preference file.
 *
 * @author beataj
 * @version $Revision$
 */
final class SaveManager {

    void savePhotosErrorSuppressFlag(final boolean flag) {
        Config.getPref().putBoolean(SUPPRESS_PHOTOS_ERROR, flag);
    }

    void saveSequenceErrorSuppressFlag(final boolean flag) {
        Config.getPref().putBoolean(SUPPRESS_SEQUENCE_ERROR, flag);
    }

    void saveSegmentsErrorSuppressFlag(final boolean flag) {
        Config.getPref().putBoolean(SUPPRESS_SEGMENTS_ERROR, flag);
    }

    void saveFiltersChangedFlag(final boolean changed) {
        Config.getPref().put(FILTERS_CHANGED, "");
        Config.getPref().put(FILTERS_CHANGED, Boolean.toString(changed));
    }

    void saveListFilter(final ListFilter filter) {
        if (filter != null) {
            final String dateStr = filter.getDate() != null ? Long.toString(filter.getDate().getTime()) : "";
            Config.getPref().put(FILTER_DATE, dateStr);
            Config.getPref().putBoolean(FILTER_ONLY_USER_FLAG, filter.isOnlyUserFlag());
        }
    }

    void saveMapViewSettings(final MapViewSettings mapViewSettings) {
        Config.getPref().putInt(MAP_VIEW_PHOTO_ZOOM, mapViewSettings.getPhotoZoom());
        Config.getPref().putBoolean(MAP_VIEW_MANUAL_SWITCH, mapViewSettings.isManualSwitchFlag());
    }

    void savePhotoSettings(final PhotoSettings photoSettings) {
        Config.getPref().putBoolean(HIGH_QUALITY_PHOTO_FLAG, photoSettings.isHighQualityFlag());
        Config.getPref().putBoolean(MOUSE_HOVER_FLAG, photoSettings.isMouseHoverFlag());
        Config.getPref().putInt(MOUSE_HOVER_DELAY, photoSettings.getMouseHoverDelay());
    }

    void saveTrackSettings(final TrackSettings trackSettings) {
        Config.getPref().putBoolean(DISPLAY_TRACK_FLAG, trackSettings.isDisplayTrack());
        if (trackSettings.getAutoplaySettings() != null) {
            final String length = trackSettings.getAutoplaySettings().getLength() != null
                    ? Integer.toString(trackSettings.getAutoplaySettings().getLength()) : "";
                    Config.getPref().put(AUTOPLAY_LENGTH, length);
                    Config.getPref().putInt(AUTOPLAY_DELAY, trackSettings.getAutoplaySettings().getDelay());
        }
    }

    void saveAutoplayStartedFlag(final boolean flag) {
        Config.getPref().putBoolean(AUTOPLAY_STARTED, flag);
    }

    void saveCacheSettings(final CacheSettings cacheSettings) {
        Config.getPref().putInt(CACHE_MEMORY_COUNT, cacheSettings.getMemoryCount());
        Config.getPref().putInt(CACHE_DISK_COUNT, cacheSettings.getDiskCount());
        Config.getPref().putInt(CACHE_PREV_NEXT_COUNT, cacheSettings.getPrevNextCount());
        Config.getPref().putInt(CACHE_NEARBY_COUNT, cacheSettings.getNearbyCount());
    }

    void saveLayerOpenedFlag(final boolean isLayerOpened) {
        Config.getPref().putBoolean(LAYER_OPENED, isLayerOpened);
    }

    void savePanelOpenedFlag(final boolean isPanelOpened) {
        Config.getPref().putBoolean(PANEL_OPENED, isPanelOpened);
    }

    void saveDataType(final DataType dataType) {
        final String value = dataType != null ? dataType.name() : "";
        Config.getPref().put(DATA_TYPE, value);
    }

    void savePluginLocalVersion(final String localVersion) {
        Config.getPref().put(PLUGIN_LOCAL_VERSION, localVersion);
    }
}