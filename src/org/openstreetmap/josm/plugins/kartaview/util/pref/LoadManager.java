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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.plugins.kartaview.argument.AutoplaySettings;
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
import org.openstreetmap.josm.plugins.kartaview.util.Util;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.CacheConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.ConfidenceCategoryEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.DetectionModeEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.EditStatusEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.ImageDataTypeEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.OsmComparisonEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.SignEntry;
import org.openstreetmap.josm.plugins.kartaview.util.pref.entity.SignTypeEntry;


/**
 * Helper class, manages the load operations of the preference variables. The preference variables are loaded from the
 * global preference file.
 *
 * @author beataj
 * @version $Revision$
 */
final class LoadManager {


    boolean loadPhotosSearchErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_PHOTOS_SEARCH_ERROR);
    }

    boolean loadDetectionsSearchErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_DETECTIONS_SEARCH_ERROR);
    }

    boolean loadEdgeDetectionsSearchErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_EDGE_DETECTIONS_SEARCH_ERROR);
    }

    boolean loadClustersSearchErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_CLUSTERS_SEARCH_ERROR);
    }

    boolean loadEdgeClusterSearchErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_EDGE_CLUSTERS_SEARCH_ERROR);
    }

    boolean loadEdgeDataOperationErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_EDGE_DATA_OPERATION_ERROR);
    }

    boolean loadPhotosErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_PHOTOS_ERROR);
    }

    boolean loadSegmentsErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_SEGMENTS_ERROR);
    }

    boolean loadSequenceErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_SEQUENCE_ERROR);
    }

    boolean loadSequenceDetectionsErrorFlag() {
        return Preferences.main().getBoolean(SUPPRESS_SEQUENCE_DETECTIONS_ERROR);
    }

    boolean loadPhotoDetectionsErrorFlag() {
        return Preferences.main().getBoolean(SUPPRESS_PHOTO_DETECTIONS_ERROR);
    }

    boolean loadDetectionUpdateErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_DETECTION_UPDATE_ERROR);
    }

    boolean loadListSignErrorFlag() {
        return Preferences.main().getBoolean(SUPPRESS_LIST_SIGNS_ERROR);
    }

    boolean loadListSignRegionErrorFlag() {
        return Preferences.main().getBoolean(SUPPRESS_LIST_SIGN_REGIONS_ERROR);
    }

    boolean loadAutoplayStartedFlag() {
        return Preferences.main().getBoolean(AUTOPLAY_STARTED);
    }

    SearchFilter loadSearchFilter() {
        final String dateStr = Preferences.main().get(FILTER_DATE);
        final Date date = !dateStr.isEmpty() ? new Date(Long.parseLong(dateStr)) : null;
        final List<DataType> dataType = loadDataTypeFilter();

        final List<OsmComparison> osmComparisons = loadOsmComparisonFilter();
        final List<EditStatus> editStatuses = loadEditStatusFilter();
        final List<Sign> signInternalNames = loadSpecificSignFilter(FILTER_SEARCH_SPECIFIC_SIGN);
        final List<String> signTypes = loadSignTypeFilter(FILTER_SEARCH_SIGN_TYPE);
        final List<DetectionMode> modes = loadModes();
        final String region = Preferences.main().get(FILTER_SEARCH_REGION);
        final List<ConfidenceLevelCategory> confidenceCategories = loadConfidenceCategoriesFilter();
        return new SearchFilter(date, dataType, new DetectionFilter(osmComparisons, editStatuses, signTypes,
                signInternalNames, modes, region, confidenceCategories));
    }

    EdgeSearchFilter loadEdgeSearchFilter() {
        final List<DataType> dataType = loadEdgeDataTypeFilter();
        final List<Sign> signInternalNames = loadSpecificSignFilter(FILTER_EDGE_SEARCH_SPECIFIC_SIGN);
        final List<String> signTypes = loadSignTypeFilter(FILTER_EDGE_SEARCH_SIGN_TYPE);
        final String region = Preferences.main().get(FILTER_EDGE_SEARCH_REGION);
        final List<ConfidenceLevelCategory> confidenceCategories = loadEdgeConfidenceCategoriesFilter();
        final List<OsmComparison> osmComparisons = loadEdgeOsmComparisonFilter();
        return new EdgeSearchFilter(dataType, osmComparisons, confidenceCategories, region, signTypes,
                signInternalNames);
    }

    public static String getOsmUserId() {
        final Long id = Util.getOsmUserId();
        return id != null ? id.toString() : null;
    }

    private List<DataType> loadDataTypeFilter() {
        final String dataTypeVal = Preferences.main().get(FILTER_SEARCH_PHOTO_TYPE);
        final List<ImageDataTypeEntry> entries = StructUtils.getListOfStructs(Preferences.main(),
                FILTER_SEARCH_PHOTO_TYPE, ImageDataTypeEntry.class);
        List<DataType> list;
        if (dataTypeVal.isEmpty() && entries.isEmpty()) {
            list = SearchFilter.DEFAULT.getDataTypes();
        } else if (dataTypeVal.equals(FILTER_SEARCH_EMPTY)) {
            list = new ArrayList<>();
        } else {
            list = entries.stream().map(entry -> DataType.getDataType(entry.getName())).collect(Collectors.toList());
        }
        return list;
    }

    private List<DataType> loadEdgeDataTypeFilter() {
        final String dataTypeVal = Preferences.main().get(FILTER_EDGE_SEARCH_DATA_TYPE);
        final List<ImageDataTypeEntry> entries = StructUtils.getListOfStructs(Preferences.main(),
                FILTER_EDGE_SEARCH_DATA_TYPE, ImageDataTypeEntry.class);
        List<DataType> list;
        if (dataTypeVal.isEmpty() && entries.isEmpty()) {
            list = EdgeSearchFilter.DEFAULT.getDataTypes();
        } else if (dataTypeVal.equals(FILTER_SEARCH_EMPTY)) {
            list = new ArrayList<>();
        } else {
            list = entries.stream().map(entry -> DataType.getDataType(entry.getName())).collect(Collectors.toList());
        }
        return list;
    }

    private List<OsmComparison> loadOsmComparisonFilter() {
        final List<OsmComparisonEntry> entries = StructUtils.getListOfStructs(Preferences.main(),
                FILTER_SEARCH_OSM_COMPARISON, OsmComparisonEntry.class);
        List<OsmComparison> list;
        if (entries != null && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final OsmComparisonEntry entry : entries) {
                list.add(OsmComparison.valueOf(entry.getName()));
            }
        } else {
            list = SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons();
        }
        return list;
    }

    private List<OsmComparison> loadEdgeOsmComparisonFilter() {
        final List<OsmComparisonEntry> entries = StructUtils.getListOfStructs(Preferences.main(),
                FILTER_EDGE_SEARCH_OSM_COMPARISON, OsmComparisonEntry.class);
        List<OsmComparison> list;
        if (Objects.nonNull(entries) && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final OsmComparisonEntry entry : entries) {
                list.add(OsmComparison.valueOf(entry.getName()));
            }
        } else {
            list = EdgeSearchFilter.DEFAULT.getOsmComparisons();
        }
        return list;
    }

    private List<DetectionMode> loadModes() {
        final String dataTypeVal = Preferences.main().get(FILTER_SEARCH_MODE);
        final List<DetectionModeEntry> entries = StructUtils.getListOfStructs(Preferences.main(), FILTER_SEARCH_MODE,
                DetectionModeEntry.class);
        List<DetectionMode> list;
        if (dataTypeVal.isEmpty() && entries.isEmpty()) {
            list = Arrays.asList(DetectionMode.values());
        } else if (dataTypeVal.equals(FILTER_SEARCH_EMPTY)) {
            list = null;
        } else {
            list = entries.stream().map(entry -> DetectionMode.valueOf(entry.getName())).collect(Collectors.toList());
        }
        return list;
    }

    private List<EditStatus> loadEditStatusFilter() {
        final List<EditStatusEntry> entries = StructUtils.getListOfStructs(Preferences.main(),
                FILTER_SEARCH_EDIT_STATUS, EditStatusEntry.class);
        List<EditStatus> list;
        if (entries != null && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final EditStatusEntry entry : entries) {
                list.add(EditStatus.valueOf(entry.getName()));
            }
        } else {
            list = SearchFilter.DEFAULT.getDetectionFilter().getEditStatuses();
        }
        return list;
    }

    private List<String> loadSignTypeFilter(final String filterKey) {
        final List<SignTypeEntry> entries = StructUtils.getListOfStructs(Preferences.main(), filterKey,
                SignTypeEntry.class);
        List<String> list = null;
        if (entries != null && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final SignTypeEntry entry : entries) {
                list.add(entry.getName());
            }
        }
        return list;
    }

    private List<Sign> loadSpecificSignFilter(final String filterKey) {
        final List<SignEntry> entries = StructUtils.getListOfStructs(Preferences.main(), filterKey, SignEntry.class);
        List<Sign> list = null;
        if (Objects.nonNull(entries) && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final SignEntry entry : entries) {
                list.add(entry.getSign());
            }
        }
        return list;
    }

    private List<ConfidenceLevelCategory> loadConfidenceCategoriesFilter() {
        final List<ConfidenceCategoryEntry> entries = StructUtils.getListOfStructs(Preferences.main(),
                FILTER_SEARCH_CONFIDENCE_CATEGORY, ConfidenceCategoryEntry.class);
        List<ConfidenceLevelCategory> list;
        if (entries != null && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final ConfidenceCategoryEntry entry : entries) {
                list.add(ConfidenceLevelCategory.valueOf(entry.getName()));
            }
        } else {
            list = SearchFilter.DEFAULT.getDetectionFilter().getConfidenceCategories();
        }
        return list;
    }

    private List<ConfidenceLevelCategory> loadEdgeConfidenceCategoriesFilter() {
        final List<ConfidenceCategoryEntry> entries = StructUtils.getListOfStructs(Preferences.main(),
                FILTER_EDGE_SEARCH_CONFIDENCE_CATEGORY, ConfidenceCategoryEntry.class);
        List<ConfidenceLevelCategory> list;
        if (Objects.nonNull(entries) && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final ConfidenceCategoryEntry entry : entries) {
                list.add(ConfidenceLevelCategory.valueOf(entry.getName()));
            }
        } else {
            list = EdgeSearchFilter.DEFAULT.getConfidenceCategories();
        }
        return list;
    }

    MapViewSettings loadMapViewSettings() {
        final int photoZoom = loadIntValue(MAP_VIEW_PHOTO_ZOOM, Config.getInstance().getMapPhotoZoom(), Config
                .getInstance().getPreferencesMaxZoom());
        final boolean dataLoadFlag = Preferences.main().getBoolean(MAP_VIEW_DATA_LOAD, true);
        return new MapViewSettings(photoZoom, dataLoadFlag);
    }

    PhotoSettings loadPhotoSettings() {
        final boolean highQualityFlag = Preferences.main().getBoolean(HIGH_QUALITY_PHOTO_FLAG);
        final boolean mouseHoverFlag = Preferences.main().getBoolean(MOUSE_HOVER_FLAG);
        final int mouseHoverDelay = loadIntValue(MOUSE_HOVER_DELAY, Config.getInstance().getMouseHoverMinDelay(), Config
                .getInstance().getMouseHoverMaxDelay());
        final boolean displayFrontFacingFlag = Preferences.main().getBoolean(DISPLAY_FRONT_FACING_FLAG, true);
        return new PhotoSettings(highQualityFlag, mouseHoverFlag, mouseHoverDelay, displayFrontFacingFlag);
    }

    ClusterSettings loadClusterSettings() {
        final boolean displayDetectionLocations = Preferences.main().getBoolean(DISPLAY_DETECTION_LOCATIONS);
        final boolean displayTags = Preferences.main().getBoolean(DISPLAY_TAGS);
        final boolean displayColorCoded = Preferences.main().getBoolean(DISPLAY_COLOR_CODED);
        return new ClusterSettings(displayDetectionLocations, displayTags, displayColorCoded);
    }

    SequenceSettings loadTrackSettings() {
        final String displayTrackFlagVal = Preferences.main().get(DISPLAY_TRACK_FLAG);
        final boolean displayTrackFlag = displayTrackFlagVal.isEmpty() ? Boolean.TRUE : Boolean.valueOf(
                displayTrackFlagVal);
        return new SequenceSettings(displayTrackFlag, loadAutoplaySettings());
    }

    AutoplaySettings loadAutoplaySettings() {
        final Integer length = loadIntValue(AUTOPLAY_LENGTH, null, null);
        final int delay = loadIntValue(AUTOPLAY_DELAY, Config.getInstance().getAutoplayMinDelay(), Config.getInstance()
                .getAutoplayMaxDelay());
        return new AutoplaySettings(length, delay);
    }

    CacheSettings loadCacheSettings() {
        final int memoryCount = loadIntValue(CACHE_MEMORY_COUNT, CacheConfig.getInstance().getDefaultMemoryCount(),
                CacheConfig.getInstance().getMaxMemoryCount());
        final int diskCount = loadIntValue(CACHE_DISK_COUNT, CacheConfig.getInstance().getDefaultDiskCount(),
                CacheConfig.getInstance().getMaxDiskCount());
        final int prevNextCount = loadIntValue(CACHE_PREV_NEXT_COUNT, CacheConfig.getInstance()
                .getDefaultPrevNextCount(), CacheConfig.getInstance().getMaxPrevNextCount());
        final int nearbyCount = loadIntValue(CACHE_NEARBY_COUNT, CacheConfig.getInstance().getMaxNearbyCount(),
                CacheConfig.getInstance().getDefaultNearbyCount());
        return new CacheSettings(memoryCount, diskCount, prevNextCount, nearbyCount);
    }

    boolean loadKartaViewLayerOpenedFlag() {
        final String layerOpened = Preferences.main().get(KARTAVIEW_LAYER_OPENED);
        return layerOpened.isEmpty() ? Boolean.FALSE : Boolean.valueOf(layerOpened);
    }

    boolean loadEdgeLayerOpenedFlag() {
        final String layerOpened = Preferences.main().get(EDGE_LAYER_OPENED);
        return layerOpened.isEmpty() ? Boolean.FALSE : Boolean.valueOf(layerOpened);
    }

    boolean loadPhotoPanelOpenedFlag() {
        final String layerOpened = Preferences.main().get(PHOTO_PANEL_OPENED);
        return layerOpened.isEmpty() ? Boolean.FALSE : Boolean.valueOf(layerOpened);
    }

    boolean loadDetectionPanelOpenedFlag() {
        final String layerOpened = Preferences.main().get(DETECTION_PANEL_OPENED);
        return layerOpened.isEmpty() ? Boolean.FALSE : Boolean.valueOf(layerOpened);
    }

    boolean loadEdgeDetectionPanelOpenedFlag() {
        final String layerOpened = Preferences.main().get(EDGE_DETECTION_PANEL_OPENED);
        return layerOpened.isEmpty() ? Boolean.FALSE : Boolean.valueOf(layerOpened);
    }

    String loadPluginLocalVersion() {
        return Preferences.main().get(PLUGIN_LOCAL_VERSION);
    }

    private Integer loadIntValue(final String key, final Integer defaultValue, final Integer maxValue) {
        final String valueStr = Preferences.main().get(key);
        final Integer value = (valueStr != null && !valueStr.isEmpty()) ? Integer.valueOf(valueStr) : defaultValue;
        return maxValue != null ? (value > maxValue ? maxValue : value) : value;
    }
}