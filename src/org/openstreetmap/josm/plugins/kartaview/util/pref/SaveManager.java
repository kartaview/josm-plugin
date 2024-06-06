/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.pref;

import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.AUTOPLAY_DELAY;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.AUTOPLAY_LENGTH;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.AUTOPLAY_STARTED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.CACHE_DISK_COUNT;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.CACHE_MEMORY_COUNT;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.CACHE_NEARBY_COUNT;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.CACHE_PREV_NEXT_COUNT;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DETECTION_PANEL_OPENED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DISPLAY_COLOR_CODED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DISPLAY_DETECTION_LOCATIONS;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DISPLAY_FRONT_FACING_FLAG;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DISPLAY_TAGS;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DISPLAY_TRACK_FLAG;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.EDGE_DETECTION_PANEL_OPENED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.EDGE_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_DATE;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_EDGE_SEARCH_CONFIDENCE_CATEGORY;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_EDGE_SEARCH_DATA_TYPE;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_EDGE_SEARCH_OSM_COMPARISON;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_EDGE_SEARCH_REGION;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_EDGE_SEARCH_SIGN_TYPE;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_EDGE_SEARCH_SPECIFIC_SIGN;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_SEARCH_CONFIDENCE_CATEGORY;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_SEARCH_EDIT_STATUS;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_SEARCH_EMPTY;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_SEARCH_MODE;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_SEARCH_OSM_COMPARISON;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_SEARCH_PHOTO_TYPE;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_SEARCH_REGION;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_SEARCH_SIGN_TYPE;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_SEARCH_SPECIFIC_SIGN;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.HIGH_QUALITY_PHOTO_FLAG;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.KARTAVIEW_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.MAP_VIEW_DATA_LOAD;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.MAP_VIEW_PHOTO_ZOOM;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.MOUSE_HOVER_DELAY;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.MOUSE_HOVER_FLAG;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.PHOTO_PANEL_OPENED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.PLUGIN_LOCAL_VERSION;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_CLUSTERS_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_DETECTIONS_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_DETECTION_UPDATE_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_EDGE_CLUSTERS_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_EDGE_DATA_OPERATION_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_EDGE_DETECTIONS_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_LIST_SIGNS_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_LIST_SIGN_REGIONS_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_PHOTOS_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_PHOTOS_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_PHOTO_DETECTIONS_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_SEGMENTS_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_SEQUENCE_DETECTIONS_ERROR;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.SUPPRESS_SEQUENCE_ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.plugins.kartaview.argument.CacheSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.ClusterSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.DataType;
import org.openstreetmap.josm.plugins.kartaview.argument.DetectionFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.SearchFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.SequenceSettings;
import org.openstreetmap.josm.plugins.kartaview.entity.ConfidenceLevelCategory;
import org.openstreetmap.josm.plugins.kartaview.entity.DetectionMode;
import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmComparison;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.ConfidenceCategoryEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.DetectionModeEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.EditStatusEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.ImageDataTypeEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.OsmComparisonEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.SignEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.SignTypeEntry;


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

    void saveEdgeDetectionsSearchErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_EDGE_DETECTIONS_SEARCH_ERROR, flag);
    }

    void saveClustersSearchErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_CLUSTERS_SEARCH_ERROR, flag);
    }

    void saveEdgeClustersSearchErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_EDGE_CLUSTERS_SEARCH_ERROR, flag);
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

    void saveEdgeDataOperationErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_EDGE_DATA_OPERATION_ERROR, flag);
    }

    void saveListSignErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_LIST_SIGNS_ERROR, flag);
    }

    void saveListSignRegionErrorSuppressFlag(final boolean flag) {
        Preferences.main().putBoolean(SUPPRESS_LIST_SIGN_REGIONS_ERROR, flag);
    }

    void saveFiltersChangedFlag(final boolean changed, final String filterKey) {
        Preferences.main().put(filterKey, "");
        Preferences.main().put(filterKey, Boolean.toString(changed));
    }

    void saveSearchFilter(final SearchFilter filter) {
        if (filter != null) {
            final String dateStr = filter.getDate() != null ? Long.toString(filter.getDate().getTime()) : "";
            Preferences.main().put(FILTER_DATE, dateStr);
            saveDataTypeFilter(filter.getDataTypes(), FILTER_SEARCH_PHOTO_TYPE);
            saveDetectionFilter(filter.getDetectionFilter());
        }
    }

    void saveEdgeSearchFilter(final EdgeSearchFilter edgeSearchFilter) {
        if (Objects.nonNull(edgeSearchFilter)) {
            saveDataTypeFilter(edgeSearchFilter.getDataTypes(), FILTER_EDGE_SEARCH_DATA_TYPE);
            saveSignTypeFilter(edgeSearchFilter.getSignTypes(), FILTER_EDGE_SEARCH_SIGN_TYPE);
            saveSpecificSignsFilter(edgeSearchFilter.getSpecificSigns(), FILTER_EDGE_SEARCH_SPECIFIC_SIGN);
            saveConfidenceCategoryFilter(edgeSearchFilter.getConfidenceCategories(),
                    FILTER_EDGE_SEARCH_CONFIDENCE_CATEGORY);
            saveOsmComparisonFilter(edgeSearchFilter.getOsmComparisons(), FILTER_EDGE_SEARCH_OSM_COMPARISON);
            if (Objects.nonNull(edgeSearchFilter.getRegion())) {
                Preferences.main().put(FILTER_EDGE_SEARCH_REGION, edgeSearchFilter.getRegion());
            }
        }
    }

    private void saveDataTypeFilter(final List<DataType> types, final String filter) {
        if (Objects.isNull(types) || types.isEmpty()) {
            Preferences.main().put(filter, FILTER_SEARCH_EMPTY);
        } else {
            final List<ImageDataTypeEntry> entries = types.stream().map(ImageDataTypeEntry::new).collect(Collectors
                    .toList());
            StructUtils.putListOfStructs(Preferences.main(), filter, entries, ImageDataTypeEntry.class);
        }
    }

    private void saveDetectionFilter(final DetectionFilter filter) {
        List<OsmComparison> osmComparions = null;
        List<EditStatus> editStatuses = null;
        List<Sign> specificSigns = null;
        List<String> signTypes = null;
        List<DetectionMode> detectionModes = null;
        String region = null;
        List<ConfidenceLevelCategory> confidenceCategories = null;

        if (filter != null) {
            osmComparions = filter.getOsmComparisons();
            editStatuses = filter.getEditStatuses();
            specificSigns = filter.getSpecificSigns();
            signTypes = filter.getSignTypes();
            detectionModes = filter.getModes();
            region = filter.getRegion();
            confidenceCategories = filter.getConfidenceCategories();
        }
        saveOsmComparisonFilter(osmComparions, FILTER_SEARCH_OSM_COMPARISON);
        saveEditStatusFilter(editStatuses);
        saveSignTypeFilter(signTypes, FILTER_SEARCH_SIGN_TYPE);
        saveSpecificSignsFilter(specificSigns, FILTER_SEARCH_SPECIFIC_SIGN);
        saveModesFilter(detectionModes);
        saveConfidenceCategoryFilter(confidenceCategories, FILTER_SEARCH_CONFIDENCE_CATEGORY);

        if (region != null) {
            Preferences.main().put(FILTER_SEARCH_REGION, region);
        }
    }

    private void saveOsmComparisonFilter(final List<OsmComparison> osmComparisons, final String filterKey) {
        final List<OsmComparisonEntry> entries = new ArrayList<>();
        if (osmComparisons != null) {
            for (final OsmComparison osmComparison : osmComparisons) {
                entries.add(new OsmComparisonEntry(osmComparison));
            }
        }
        StructUtils.putListOfStructs(Preferences.main(), filterKey, entries, OsmComparisonEntry.class);
    }

    private void saveConfidenceCategoryFilter(final List<ConfidenceLevelCategory> confidenceCategories,
            final String filterKey) {
        final List<ConfidenceCategoryEntry> entries = new ArrayList<>();
        if (Objects.nonNull(confidenceCategories)) {
            for (final ConfidenceLevelCategory confidenceCategory : confidenceCategories) {
                entries.add(new ConfidenceCategoryEntry(confidenceCategory));
            }
        }
        StructUtils.putListOfStructs(Preferences.main(), filterKey, entries, ConfidenceCategoryEntry.class);
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

    private void saveSignTypeFilter(final List<String> signTypes, final String filterKey) {
        final List<SignTypeEntry> entries = new ArrayList<>();
        if (Objects.nonNull(signTypes)) {
            for (final String sign : signTypes) {
                entries.add(new SignTypeEntry(sign));
            }
        }
        StructUtils.putListOfStructs(Preferences.main(), filterKey, entries, SignTypeEntry.class);
    }

    private void saveSpecificSignsFilter(final List<Sign> specificSigns, final String filterKey) {
        final List<SignEntry> entries = new ArrayList<>();
        if (specificSigns != null) {
            for (final Sign sign : specificSigns) {
                entries.add(new SignEntry(sign));
            }
        }
        StructUtils.putListOfStructs(Preferences.main(), filterKey, entries, SignEntry.class);
    }

    private void saveModesFilter(final List<DetectionMode> modes) {
        if (modes == null || modes.isEmpty()) {
            Preferences.main().put(FILTER_SEARCH_MODE, FILTER_SEARCH_EMPTY);
        } else {
            final List<DetectionModeEntry> entries = modes.stream().map(DetectionModeEntry::new).collect(Collectors
                    .toList());
            StructUtils.putListOfStructs(Preferences.main(), FILTER_SEARCH_MODE, entries, DetectionModeEntry.class);
        }
    }

    void saveMapViewSettings(final MapViewSettings mapViewSettings) {
        Preferences.main().putInt(MAP_VIEW_PHOTO_ZOOM, mapViewSettings.getPhotoZoom());
        Preferences.main().putBoolean(MAP_VIEW_DATA_LOAD, mapViewSettings.isDataLoadFlag());
    }

    void savePhotoSettings(final PhotoSettings photoSettings) {
        Preferences.main().putBoolean(HIGH_QUALITY_PHOTO_FLAG, photoSettings.isHighQualityFlag());
        Preferences.main().putBoolean(MOUSE_HOVER_FLAG, photoSettings.isMouseHoverFlag());
        Preferences.main().putInt(MOUSE_HOVER_DELAY, photoSettings.getMouseHoverDelay());
        Preferences.main().putBoolean(DISPLAY_FRONT_FACING_FLAG, photoSettings.isDisplayFrontFacingFlag());
    }


    void saveClusterSettings(final ClusterSettings clusterSettings) {
        Preferences.main().putBoolean(DISPLAY_DETECTION_LOCATIONS, clusterSettings.isDisplayDetectionLocations());
        Preferences.main().putBoolean(DISPLAY_TAGS, clusterSettings.isDisplayTags());
        Preferences.main().putBoolean(DISPLAY_COLOR_CODED, clusterSettings.isDisplayColorCoded());
    }

    void saveTrackSettings(final SequenceSettings trackSettings) {
        Preferences.main().putBoolean(DISPLAY_TRACK_FLAG, trackSettings.isDisplayTrack());
        if (trackSettings.getAutoplaySettings() != null) {
            final String length = trackSettings.getAutoplaySettings().getLength() != null ? Integer.toString(
                    trackSettings.getAutoplaySettings().getLength()) : "";
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

    void saveKartaViewLayerOpenedFlag(final boolean isLayerOpened) {
        Preferences.main().putBoolean(KARTAVIEW_LAYER_OPENED, isLayerOpened);
    }

    void saveEdgeLayerOpenedFlag(final boolean isLayerOpened) {
        Preferences.main().putBoolean(EDGE_LAYER_OPENED, isLayerOpened);
    }

    void saveEdgeDetectionPanelOpenedFlag(final boolean isPanelOpened) {
        Preferences.main().putBoolean(EDGE_DETECTION_PANEL_OPENED, isPanelOpened);
    }

    void savePhotoPanelOpenedFlag(final boolean isPanelOpened) {
        Preferences.main().putBoolean(PHOTO_PANEL_OPENED, isPanelOpened);
    }

    void saveDetectionPanelOpenedFlag(final boolean isPanelOpened) {
        Preferences.main().putBoolean(DETECTION_PANEL_OPENED, isPanelOpened);
    }

    void savePluginLocalVersion(final String localVersion) {
        Preferences.main().put(PLUGIN_LOCAL_VERSION, localVersion);
    }
}