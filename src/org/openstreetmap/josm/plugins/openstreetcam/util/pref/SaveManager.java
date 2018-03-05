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
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_CHANGED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_DATE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_ONLY_USER_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_EDIT_STATUS;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_EMPTY;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_MODE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_OSM_COMPARISON;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_PHOTO_TYPE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_SIGN_TYPE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.HIGH_QUALITY_PHOTO_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.LAYER_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_MANUAL_SWITCH;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_PHOTO_ZOOM;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_DELAY;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.ONLY_DETECTION_FILTER_CHANGED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.PANEL_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.PLUGIN_LOCAL_VERSION;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_DETECTION_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_DETECTION_UPDATE_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_PHOTOS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_PHOTO_DETECTIONS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEGMENTS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEQUENCE_DETECTIONS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEQUENCE_ERROR;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DetectionFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ImageDataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.TrackSettings;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.SignType;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.DetectionModeEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.EditStatusEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.ImageDataTypeEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.OsmComparisonEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.SignTypeEntry;


/**
 * Helper class, manages the save operations of the plugin preference variables. The preference variables are saved into
 * a global preference file.
 *
 * @author beataj
 * @version $Revision$
 */
final class SaveManager {

    void savePhotosErrorSuppressFlag(final boolean flag) {
        Main.pref.putBoolean(SUPPRESS_PHOTOS_ERROR, flag);
    }

    void saveSequenceErrorSuppressFlag(final boolean flag) {
        Main.pref.putBoolean(SUPPRESS_SEQUENCE_ERROR, flag);
    }

    void saveSegmentsErrorSuppressFlag(final boolean flag) {
        Main.pref.putBoolean(SUPPRESS_SEGMENTS_ERROR, flag);
    }

    void saveDetectionSearchErrorSuppressFlag(final boolean flag) {
        Main.pref.putBoolean(SUPPRESS_DETECTION_SEARCH_ERROR, flag);
    }

    void saveSequenceDetectionsErrorFlag(final boolean flag) {
        Main.pref.putBoolean(SUPPRESS_SEQUENCE_DETECTIONS_ERROR, flag);
    }

    void savePhotoDetectionsErrorFlag(final boolean flag) {
        Main.pref.putBoolean(SUPPRESS_PHOTO_DETECTIONS_ERROR, flag);
    }

    void saveDetectionUpdateErrorSuppressFlag(final boolean flag) {
        Main.pref.putBoolean(SUPPRESS_DETECTION_UPDATE_ERROR, flag);
    }

    void saveFiltersChangedFlag(final boolean changed) {
        Main.pref.put(FILTER_CHANGED, "");
        Main.pref.put(FILTER_CHANGED, Boolean.toString(changed));
    }

    void saveOnlyDetectionFilterChangedFlag(final boolean flag) {
        Main.pref.putBoolean(ONLY_DETECTION_FILTER_CHANGED, flag);
    }

    void saveSearchFilter(final SearchFilter filter) {
        if (filter != null) {
            final String dateStr = filter.getDate() != null ? Long.toString(filter.getDate().getTime()) : "";
            Main.pref.put(FILTER_DATE, dateStr);
            Main.pref.putBoolean(FILTER_ONLY_USER_FLAG, filter.isOnlyMineFlag());
            saveDataTypeFilter(filter.getDataTypes());
            saveDetectionFilter(filter.getDetectionFilter());
        }
    }

    private void saveDataTypeFilter(final List<ImageDataType> types) {
        if (types == null || types.isEmpty()) {
            Main.pref.put(FILTER_SEARCH_PHOTO_TYPE, FILTER_SEARCH_EMPTY);
        } else {
            final List<ImageDataTypeEntry> entries =
                    types.stream().map(ImageDataTypeEntry::new).collect(Collectors.toList());
            StructUtils.putListOfStructs(Main.pref, FILTER_SEARCH_PHOTO_TYPE, entries, ImageDataTypeEntry.class);
        }
    }

    private void saveDetectionFilter(final DetectionFilter filter) {
        saveOsmComparisonFilter(filter.getOsmComparisons());
        saveEditStatusFilter(filter.getEditStatuses());
        saveSignTypeFilter(filter.getSignTypes());
        saveModesFilter(filter.getModes());
    }

    private void saveOsmComparisonFilter(final List<OsmComparison> osmComparisons) {
        final List<OsmComparisonEntry> entries = new ArrayList<>();
        if (osmComparisons != null) {
            for (final OsmComparison osmComparison : osmComparisons) {
                entries.add(new OsmComparisonEntry(osmComparison));
            }
        }
        StructUtils.putListOfStructs(Main.pref, FILTER_SEARCH_OSM_COMPARISON, entries, OsmComparisonEntry.class);
    }

    private void saveEditStatusFilter(final List<EditStatus> editStatuses) {
        final List<EditStatusEntry> entries = new ArrayList<>();
        if (editStatuses != null) {
            for (final EditStatus editStatus : editStatuses) {
                entries.add(new EditStatusEntry(editStatus));
            }
        }
        StructUtils.putListOfStructs(Main.pref, FILTER_SEARCH_EDIT_STATUS, entries, EditStatusEntry.class);
    }

    private void saveSignTypeFilter(final List<SignType> signTypes) {
        final List<SignTypeEntry> entries = new ArrayList<>();
        if (signTypes != null) {
            for (final SignType signType : signTypes) {
                entries.add(new SignTypeEntry(signType));
            }
        }
        StructUtils.putListOfStructs(Main.pref, FILTER_SEARCH_SIGN_TYPE, entries, SignTypeEntry.class);
    }

    private void saveModesFilter(final List<DetectionMode> modes) {
        if (modes == null || modes.isEmpty()) {
            Main.pref.put(FILTER_SEARCH_MODE, FILTER_SEARCH_EMPTY);
        } else {
            final List<DetectionModeEntry> entries =
                    modes.stream().map(DetectionModeEntry::new).collect(Collectors.toList());
            StructUtils.putListOfStructs(Main.pref, FILTER_SEARCH_MODE, entries, DetectionModeEntry.class);
        }
    }

    void saveMapViewSettings(final MapViewSettings mapViewSettings) {
        Main.pref.putInt(MAP_VIEW_PHOTO_ZOOM, mapViewSettings.getPhotoZoom());
        Main.pref.putBoolean(MAP_VIEW_MANUAL_SWITCH, mapViewSettings.isManualSwitchFlag());
    }

    void savePhotoSettings(final PhotoSettings photoSettings) {
        Main.pref.putBoolean(HIGH_QUALITY_PHOTO_FLAG, photoSettings.isHighQualityFlag());
        Main.pref.putBoolean(MOUSE_HOVER_FLAG, photoSettings.isMouseHoverFlag());
        Main.pref.putInt(MOUSE_HOVER_DELAY, photoSettings.getMouseHoverDelay());
    }

    void saveTrackSettings(final TrackSettings trackSettings) {
        Main.pref.putBoolean(DISPLAY_TRACK_FLAG, trackSettings.isDisplayTrack());
        if (trackSettings.getAutoplaySettings() != null) {
            final String length = trackSettings.getAutoplaySettings().getLength() != null
                    ? Integer.toString(trackSettings.getAutoplaySettings().getLength()) : "";
                    Main.pref.put(AUTOPLAY_LENGTH, length);
                    Main.pref.putInt(AUTOPLAY_DELAY, trackSettings.getAutoplaySettings().getDelay());
        }
    }

    void saveAutoplayStartedFlag(final boolean flag) {
        Main.pref.putBoolean(AUTOPLAY_STARTED, flag);
    }

    void saveCacheSettings(final CacheSettings cacheSettings) {
        Main.pref.putInt(CACHE_MEMORY_COUNT, cacheSettings.getMemoryCount());
        Main.pref.putInt(CACHE_DISK_COUNT, cacheSettings.getDiskCount());
        Main.pref.putInt(CACHE_PREV_NEXT_COUNT, cacheSettings.getPrevNextCount());
        Main.pref.putInt(CACHE_NEARBY_COUNT, cacheSettings.getNearbyCount());
    }

    void saveLayerOpenedFlag(final boolean isLayerOpened) {
        Main.pref.putBoolean(LAYER_OPENED, isLayerOpened);
    }

    void savePanelOpenedFlag(final boolean isPanelOpened) {
        Main.pref.putBoolean(PANEL_OPENED, isPanelOpened);
    }

    void saveDataType(final DataType dataType) {
        final String value = dataType != null ? dataType.name() : "";
        Main.pref.put(DATA_TYPE, value);
    }

    void savePluginLocalVersion(final String localVersion) {
        Main.pref.put(PLUGIN_LOCAL_VERSION, localVersion);
    }
}