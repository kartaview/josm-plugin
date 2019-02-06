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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplaySettings;
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
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.CacheConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.DetectionModeEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.EditStatusEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.ImageDataTypeEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.OsmComparisonEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.SignEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.SignTypeEntry;


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

    boolean loadClustersSearchErrorSuppressFlag() {
        return Preferences.main().getBoolean(SUPPRESS_CLUSTERS_SEARCH_ERROR);
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
        final String onlyUserFlagStr = Preferences.main().get(FILTER_ONLY_USER_FLAG);
        final boolean onlyUserFlag = !onlyUserFlagStr.isEmpty() && Boolean.parseBoolean(onlyUserFlagStr);
        final List<DataType> dataType = loadDataTypeFilter();

        final List<OsmComparison> osmComparisons = loadOsmComparisonFilter();
        final List<EditStatus> editStatuses = loadEditStatusFilter();
        final List<Sign> signInternalNames = loadSpecificSignFilter();
        final List<String> signTypes = loadSignTypeFilter();
        final List<DetectionMode> modes = loadModes();
        final String region = Preferences.main().get(FILTER_SEARCH_REGION);
        return new SearchFilter(date, onlyUserFlag, dataType,
                new DetectionFilter(osmComparisons, editStatuses, signTypes, signInternalNames, modes, region));
    }

    private List<DataType> loadDataTypeFilter() {
        final String dataTypeVal = Preferences.main().get(FILTER_SEARCH_PHOTO_TYPE);
        final List<ImageDataTypeEntry> entries =
                StructUtils.getListOfStructs(Preferences.main(), FILTER_SEARCH_PHOTO_TYPE, ImageDataTypeEntry.class);
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

    private List<DetectionMode> loadModes() {
        final String dataTypeVal = Preferences.main().get(FILTER_SEARCH_MODE);
        final List<DetectionModeEntry> entries =
                StructUtils.getListOfStructs(Preferences.main(), FILTER_SEARCH_MODE, DetectionModeEntry.class);
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
        final List<EditStatusEntry> entries =
                StructUtils.getListOfStructs(Preferences.main(), FILTER_SEARCH_EDIT_STATUS, EditStatusEntry.class);
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

    private List<String> loadSignTypeFilter() {
        final List<SignTypeEntry> entries =
                StructUtils.getListOfStructs(Preferences.main(), FILTER_SEARCH_SIGN_TYPE, SignTypeEntry.class);
        List<String> list = null;
        if (entries != null && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final SignTypeEntry entry : entries) {
                list.add(entry.getName());
            }
        }
        return list;
    }

    private List<Sign> loadSpecificSignFilter() {
        final List<SignEntry> entries =
                StructUtils.getListOfStructs(Preferences.main(), FILTER_SEARCH_SPECIFIC_SIGN, SignEntry.class);
        List<Sign> list = null;
        if (entries != null && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final SignEntry entry : entries) {
                list.add(entry.getSign());
            }
        }
        return list;
    }

    MapViewSettings loadMapViewSettings() {
        final int photoZoom = loadIntValue(MAP_VIEW_PHOTO_ZOOM, Config.getInstance().getMapPhotoZoom(),
                Config.getInstance().getPreferencesMaxZoom());
        final boolean manualSwitchFlag = Preferences.main().getBoolean(MAP_VIEW_MANUAL_SWITCH);
        final boolean dataLoadFlag = Preferences.main().getBoolean(MAP_VIEW_DATA_LOAD, true);
        return new MapViewSettings(photoZoom, manualSwitchFlag, dataLoadFlag);
    }

    PhotoSettings loadPhotoSettings() {
        final boolean highQualityFlag = Preferences.main().getBoolean(HIGH_QUALITY_PHOTO_FLAG);
        final boolean mouseHoverFlag = Preferences.main().getBoolean(MOUSE_HOVER_FLAG);
        final int mouseHoverDelay = loadIntValue(MOUSE_HOVER_DELAY, Config.getInstance().getMouseHoverMinDelay(),
                Config.getInstance().getMouseHoverMaxDelay());
        return new PhotoSettings(highQualityFlag, mouseHoverFlag, mouseHoverDelay);
    }

    ClusterSettings loadClusterSettings() {
        final boolean displayDetectionLocations = Preferences.main().getBoolean(DISPLAY_DETECTION_LOCATIONS);
        return new ClusterSettings(displayDetectionLocations);
    }

    SequenceSettings loadTrackSettings() {
        final String displayTrackFlagVal = Preferences.main().get(DISPLAY_TRACK_FLAG);
        final boolean displayTrackFlag =
                displayTrackFlagVal.isEmpty() ? Boolean.TRUE : Boolean.valueOf(displayTrackFlagVal);
        return new SequenceSettings(displayTrackFlag, loadAutoplaySettings());
    }

    AutoplaySettings loadAutoplaySettings() {
        final Integer length = loadIntValue(AUTOPLAY_LENGTH, null, null);
        final int delay = loadIntValue(AUTOPLAY_DELAY, Config.getInstance().getAutoplayMinDelay(),
                Config.getInstance().getAutoplayMaxDelay());
        return new AutoplaySettings(length, delay);
    }

    CacheSettings loadCacheSettings() {
        final int memoryCount = loadIntValue(CACHE_MEMORY_COUNT, CacheConfig.getInstance().getDefaultMemoryCount(),
                CacheConfig.getInstance().getMaxMemoryCount());
        final int diskCount = loadIntValue(CACHE_DISK_COUNT, CacheConfig.getInstance().getDefaultDiskCount(),
                CacheConfig.getInstance().getMaxDiskCount());
        final int prevNextCount = loadIntValue(CACHE_PREV_NEXT_COUNT,
                CacheConfig.getInstance().getDefaultPrevNextCount(), CacheConfig.getInstance().getMaxPrevNextCount());
        final int nearbyCount = loadIntValue(CACHE_NEARBY_COUNT, CacheConfig.getInstance().getMaxNearbyCount(),
                CacheConfig.getInstance().getDefaultNearbyCount());
        return new CacheSettings(memoryCount, diskCount, prevNextCount, nearbyCount);
    }

    boolean loadLayerOpenedFlag() {
        final String layerOpened = Preferences.main().get(LAYER_OPENED);
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

    MapViewType loadMapViewType() {
        final String value = Preferences.main().get(MAP_VIEW_TYPE);
        MapViewType dataType;
        try {
            dataType = MapViewType.valueOf(value);
        } catch (final RuntimeException e) {
            dataType = null;
        }
        return dataType;
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