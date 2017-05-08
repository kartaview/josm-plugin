/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref;

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
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEQUENCE_ERROR;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;


/**
 * Helper class, manages the save operations of the plugin preference variables. The preference variables are saved into
 * a global preference file.
 *
 * @author beataj
 * @version $Revision$
 */
final class SaveManager {

    void savePhotosErrorSuppressFlag(final boolean flag) {
        Main.pref.put(SUPPRESS_PHOTOS_ERROR, flag);
    }

    void saveSequenceErrorSuppressFlag(final boolean flag) {
        Main.pref.put(SUPPRESS_SEQUENCE_ERROR, flag);
    }

    void saveFiltersChangedFlag(final boolean changed) {
        Main.pref.put(FILTERS_CHANGED, "");
        Main.pref.put(FILTERS_CHANGED, Boolean.toString(changed));
    }

    void saveListFilter(final ListFilter filter) {
        if (filter != null) {
            final String dateStr = filter.getDate() != null ? Long.toString(filter.getDate().getTime()) : "";
            Main.pref.put(FILTER_DATE, dateStr);
            Main.pref.put(FILTER_ONLY_USER_FLAG, filter.isOnlyUserFlag());
        }
    }

    void saveMapViewSettings(final MapViewSettings mapViewSettings) {
        Main.pref.putInteger(MAP_VIEW_PHOTO_ZOOM, mapViewSettings.getPhotoZoom());
        Main.pref.put(MAP_VIEW_MANUAL_SWITCH, mapViewSettings.isManualSwitchFlag());
    }

    void savePhotoSettings(final PhotoSettings photoSettings) {
        Main.pref.put(HIGH_QUALITY_PHOTO_FLAG, photoSettings.isHighQualityFlag());
        Main.pref.put(DISPLAY_TRACK_FLAG, photoSettings.isDisplayTrackFlag());
        Main.pref.put(MOUSE_HOVER_FLAG, photoSettings.isMouseHoverFlag());
        Main.pref.putInteger(MOUSE_HOVER_DELAY, photoSettings.getMouseHoverDelay());
    }

    void saveCacheSettings(final CacheSettings cacheSettings) {
        Main.pref.putInteger(CACHE_MEMORY_COUNT, cacheSettings.getMemoryCount());
        Main.pref.putInteger(CACHE_DISK_COUNT, cacheSettings.getDiskCount());
        Main.pref.putInteger(CACHE_PREV_NEXT_COUNT, cacheSettings.getPrevNextCount());
        Main.pref.putInteger(CACHE_NEARBY_COUNT, cacheSettings.getNearbyCount());
    }

    void saveLayerOpenedFlag(final boolean isLayerOpened) {
        Main.pref.put(LAYER_OPENED, isLayerOpened);
    }

    void savePanelOpenedFlag(final boolean isPanelOpened) {
        Main.pref.put(PANEL_OPENED, isPanelOpened);
    }

    void saveDataType(final DataType dataType) {
        final String value = dataType != null ? dataType.name() : "";
        Main.pref.put(DATA_TYPE, value);
    }

    void savePluginLocalVersion(final String localVersion) {
        Main.pref.put(PLUGIN_LOCAL_VERSION, localVersion);
    }
}