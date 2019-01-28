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
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.DETECTION_PANEL_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.DISPLAY_DETECTION_LOCATIONS;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.DISPLAY_TRACK_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_CHANGED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_DATE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_ONLY_USER_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_EDIT_STATUS;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_EMPTY;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_MODE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_OSM_COMPARISON;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_PHOTO_TYPE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_REGION;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_SIGN_TYPE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_SPECIFIC_SIGN;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.HIGH_QUALITY_PHOTO_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.LAYER_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_DATA_LOAD;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_MANUAL_SWITCH;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_PHOTO_ZOOM;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_TYPE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_DELAY;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.PHOTO_PANEL_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.PLUGIN_LOCAL_VERSION;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_CLUSTERS_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_DETECTIONS_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_DETECTION_UPDATE_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_LIST_SIGNS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_LIST_SIGN_REGIONS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_PHOTOS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_PHOTOS_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_PHOTO_DETECTIONS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEGMENTS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEQUENCE_DETECTIONS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEQUENCE_ERROR;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ClusterSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SequenceSettings;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.DetectionFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.DetectionModeEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.EditStatusEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.ImageDataTypeEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.OsmComparisonEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.SignEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.SignTypeEntry;


/**
 * Helper class, manages the save operations of the plugin preference variables. The preference variables are saved into
 * a global preference file.
 *
 * @author beataj
 * @version $Revision$
 */
final class SaveManager {

    void savePhotosSearchErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_PHOTOS_SEARCH_ERROR, flag);
    }

    void saveDetectionsSearchErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_DETECTIONS_SEARCH_ERROR, flag);
    }

    void saveClustersSearchErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_CLUSTERS_SEARCH_ERROR, flag);
    }

    void savePhotosErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_PHOTOS_ERROR, flag);
    }

    void saveSequenceErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_SEQUENCE_ERROR, flag);
    }

    void saveSegmentsErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_SEGMENTS_ERROR, flag);
    }

    void saveSequenceDetectionsErrorFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_SEQUENCE_DETECTIONS_ERROR, flag);
    }

    void savePhotoDetectionsErrorFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_PHOTO_DETECTIONS_ERROR, flag);
    }

    void saveDetectionUpdateErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_DETECTION_UPDATE_ERROR, flag);
    }

    void saveListSignErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_LIST_SIGNS_ERROR, flag);
    }

    void saveListSignRegionErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_LIST_SIGN_REGIONS_ERROR, flag);
    }

    void saveFiltersChangedFlag(final boolean changed) {
        Preferences.main().put(FILTER_CHANGED, "");
        Preferences.main().put(FILTER_CHANGED, Boolean.toString(changed));
    }

    void saveSearchFilter(final SearchFilter filter) {
        if (filter != null) {
            final String dateStr = filter.getDate() != null ? Long.toString(filter.getDate().getTime()) : "";
            Preferences.main().put(FILTER_DATE, dateStr);
            Preferences.main().putBoolean(FILTER_ONLY_USER_FLAG, filter.isOlnyUserData());
            saveDataTypeFilter(filter.getDataTypes());
            saveDetectionFilter(filter.getDetectionFilter());
        }
    }

    private void saveDataTypeFilter(final List<DataType> types) {
        if (types == null || types.isEmpty()) {
            Preferences.main().put(FILTER_SEARCH_PHOTO_TYPE, FILTER_SEARCH_EMPTY);
        } else {
            final List<ImageDataTypeEntry> entries =
                    types.stream().map(ImageDataTypeEntry::new).collect(Collectors.toList());
            StructUtils.putListOfStructs(Preferences.main(), FILTER_SEARCH_PHOTO_TYPE, entries,
                    ImageDataTypeEntry.class);
        }
    }

    private void saveDetectionFilter(final DetectionFilter filter) {
        List<OsmComparison> osmComparions = null;
        List<EditStatus> editStatuses = null;
        List<Sign> specificSigns = null;
        List<String> signTypes = null;
        List<DetectionMode> detectionModes = null;
        String region = null;
        if (filter != null) {
            osmComparions = filter.getOsmComparisons();
            editStatuses = filter.getEditStatuses();
            specificSigns = filter.getSpecificSigns();
            signTypes = filter.getSignTypes();
            detectionModes = filter.getModes();
            region = filter.getRegion();
        }
        saveOsmComparisonFilter(osmComparions);
        saveEditStatusFilter(editStatuses);
        saveSignTypeFilter(signTypes);
        saveSpecificSignsFilter(specificSigns);
        saveModesFilter(detectionModes);
        if (region != null) {
            Preferences.main().put(FILTER_SEARCH_REGION, region);
        }
    }

    private void saveOsmComparisonFilter(final List<OsmComparison> osmComparisons) {
        final List<OsmComparisonEntry> entries = new ArrayList<>();
        if (osmComparisons != null) {
            for (final OsmComparison osmComparison : osmComparisons) {
                entries.add(new OsmComparisonEntry(osmComparison));
            }
        }
        StructUtils.putListOfStructs(Preferences.main(), FILTER_SEARCH_OSM_COMPARISON, entries,
                OsmComparisonEntry.class);
    }

    private void saveEditStatusFilter(final List<EditStatus> editStatuses) {
        final List<EditStatusEntry> entries = new ArrayList<>();
        if (editStatuses != null) {
            for (final EditStatus editStatus : editStatuses) {
                entries.add(new EditStatusEntry(editStatus));
            }
        }
        StructUtils.putListOfStructs(Preferences.main(), FILTER_SEARCH_EDIT_STATUS, entries, EditStatusEntry.class);
    }

    private void saveSignTypeFilter(final List<String> signTypes) {
        final List<SignTypeEntry> entries = new ArrayList<>();
        if (signTypes != null) {
            for (final String sign : signTypes) {
                entries.add(new SignTypeEntry(sign));
            }
        }
        StructUtils.putListOfStructs(Preferences.main(), FILTER_SEARCH_SIGN_TYPE, entries, SignTypeEntry.class);
    }

    private void saveSpecificSignsFilter(final List<Sign> specificSigns) {
        final List<SignEntry> entries = new ArrayList<>();
        if (specificSigns != null) {
            for (final Sign sign : specificSigns) {
                entries.add(new SignEntry(sign));
            }
        }
        StructUtils.putListOfStructs(Preferences.main(), FILTER_SEARCH_SPECIFIC_SIGN, entries, SignEntry.class);
    }

    private void saveModesFilter(final List<DetectionMode> modes) {
        if (modes == null || modes.isEmpty()) {
            Preferences.main().put(FILTER_SEARCH_MODE, FILTER_SEARCH_EMPTY);
        } else {
            final List<DetectionModeEntry> entries =
                    modes.stream().map(DetectionModeEntry::new).collect(Collectors.toList());
            StructUtils.putListOfStructs(Preferences.main(), FILTER_SEARCH_MODE, entries, DetectionModeEntry.class);
        }
    }

    void saveMapViewSettings(final MapViewSettings mapViewSettings) {
        Preferences.main().putInt(MAP_VIEW_PHOTO_ZOOM, mapViewSettings.getPhotoZoom());
        Preferences.main().putBoolean(MAP_VIEW_MANUAL_SWITCH, mapViewSettings.isManualSwitchFlag());
        Preferences.main().putBoolean(MAP_VIEW_DATA_LOAD, mapViewSettings.isDataLoadFlag());
    }

    void savePhotoSettings(final PhotoSettings photoSettings) {
        Preferences.main().putBoolean(HIGH_QUALITY_PHOTO_FLAG, photoSettings.isHighQualityFlag());
        Preferences.main().putBoolean(MOUSE_HOVER_FLAG, photoSettings.isMouseHoverFlag());
        Preferences.main().putInt(MOUSE_HOVER_DELAY, photoSettings.getMouseHoverDelay());
    }

    void saveClusterSettings(final ClusterSettings aggregatedSettings) {
        Preferences.main().putBoolean(DISPLAY_DETECTION_LOCATIONS, aggregatedSettings.isDisplayDetectionLocations());
    }

    void saveTrackSettings(final SequenceSettings trackSettings) {
        Preferences.main().putBoolean(DISPLAY_TRACK_FLAG, trackSettings.isDisplayTrack());
        if (trackSettings.getAutoplaySettings() != null) {
            final String length = trackSettings.getAutoplaySettings().getLength() != null
                    ? Integer.toString(trackSettings.getAutoplaySettings().getLength()) : "";
                    Preferences.main().put(AUTOPLAY_LENGTH, length);
                    Preferences.main().putInt(AUTOPLAY_DELAY, trackSettings.getAutoplaySettings().getDelay());
        }
    }

    void saveAutoplayStartedFlag(final boolean flag) {
        Preferences.main().putBoolean(AUTOPLAY_STARTED, flag);
    }

    void saveCacheSettings(final CacheSettings cacheSettings) {
        Preferences.main().putInt(CACHE_MEMORY_COUNT, cacheSettings.getMemoryCount());
        Preferences.main().putInt(CACHE_DISK_COUNT, cacheSettings.getDiskCount());
        Preferences.main().putInt(CACHE_PREV_NEXT_COUNT, cacheSettings.getPrevNextCount());
        Preferences.main().putInt(CACHE_NEARBY_COUNT, cacheSettings.getNearbyCount());
    }

    void saveLayerOpenedFlag(final boolean isLayerOpened) {
        Preferences.main().putBoolean(LAYER_OPENED, isLayerOpened);
    }

    void savePhotoPanelOpenedFlag(final boolean isPanelOpened) {
        Preferences.main().putBoolean(PHOTO_PANEL_OPENED, isPanelOpened);
    }

    void saveDetectionPanelOpenedFlag(final boolean isPanelOpened) {
        Preferences.main().putBoolean(DETECTION_PANEL_OPENED, isPanelOpened);
    }

    void saveMapViewType(final MapViewType dataType) {
        final String value = dataType != null ? dataType.name() : "";
        Preferences.main().put(MAP_VIEW_TYPE, value);
    }

    void savePluginLocalVersion(final String localVersion) {
        Preferences.main().put(PLUGIN_LOCAL_VERSION, localVersion);
    }
}