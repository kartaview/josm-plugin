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
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.DETECTION_PANEL_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.DISPLAY_TRACK_FLAG;
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
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.PHOTO_PANEL_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.PLUGIN_LOCAL_VERSION;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_DETECTION_SEARCH_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_DETECTION_UPDATE_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_PHOTOS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_PHOTO_DETECTIONS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEGMENTS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEQUENCE_DETECTIONS_ERROR;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.SUPPRESS_SEQUENCE_ERROR;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ImageDataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.TrackSettings;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.SignType;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.DetectionFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.CacheConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.DetectionModeEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.EditStatusEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.ImageDataTypeEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.OsmComparisonEntry;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.SignTypeEntry;


/**
 * Helper class, manages the load operations of the preference variables. The preference variables are loaded from the
 * global preference file.
 *
 * @author beataj
 * @version $Revision$
 */
final class LoadManager {

    boolean loadPhotosErrorSuppressFlag() {
        return Main.pref.getBoolean(SUPPRESS_PHOTOS_ERROR);
    }

    boolean loadSegmentsErrorSuppressFlag() {
        return Main.pref.getBoolean(SUPPRESS_SEGMENTS_ERROR);
    }

    boolean loadSequenceErrorSuppressFlag() {
        return Main.pref.getBoolean(SUPPRESS_SEQUENCE_ERROR);
    }

    boolean loadDetectionSearchErrorSuppressFlag() {
        return Main.pref.getBoolean(SUPPRESS_DETECTION_SEARCH_ERROR);
    }

    boolean loadSequenceDetectionsErrorFlag() {
        return Main.pref.getBoolean(SUPPRESS_SEQUENCE_DETECTIONS_ERROR);
    }

    boolean loadPhotoDetectionsErrorFlag() {
        return Main.pref.getBoolean(SUPPRESS_PHOTO_DETECTIONS_ERROR);
    }

    boolean loadDetectionUpdateErrorSuppressFlag() {
        return Main.pref.getBoolean(SUPPRESS_DETECTION_UPDATE_ERROR);
    }

    boolean loadOnlyDetectionFilterChangedFlag() {
        return Main.pref.getBoolean(ONLY_DETECTION_FILTER_CHANGED);
    }

    boolean loadAutoplayStartedFlag() {
        return Main.pref.getBoolean(AUTOPLAY_STARTED);
    }

    SearchFilter loadSearchFilter() {
        final String dateStr = Main.pref.get(FILTER_DATE);
        final Date date = !dateStr.isEmpty() ? new Date(Long.parseLong(dateStr)) : null;
        final String onlyUserFlagStr = Main.pref.get(FILTER_ONLY_USER_FLAG);
        final boolean onlyUserFlag = onlyUserFlagStr.isEmpty() ? false : Boolean.parseBoolean(onlyUserFlagStr);
        final List<ImageDataType> dataType = loadDataTypeFilter();
        final DetectionFilter detectionFilter = loadDetectionFilter();
        return new SearchFilter(date, onlyUserFlag, dataType, detectionFilter);
    }

    private List<ImageDataType> loadDataTypeFilter() {
        final String dataTypeVal = Main.pref.get(FILTER_SEARCH_PHOTO_TYPE);
        final List<ImageDataTypeEntry> entries =
                StructUtils.getListOfStructs(Main.pref, FILTER_SEARCH_PHOTO_TYPE, ImageDataTypeEntry.class);
        List<ImageDataType> list;
        if (dataTypeVal.isEmpty() && entries.isEmpty()) {
            list = Arrays.asList(ImageDataType.values());
        } else if (dataTypeVal.equals(FILTER_SEARCH_EMPTY)) {
            list = new ArrayList<>();
        } else {
            list = entries.stream().map(entry -> ImageDataType.valueOf(entry.getName())).collect(Collectors.toList());
        }
        return list;
    }

    private DetectionFilter loadDetectionFilter() {
        final List<OsmComparison> osmComparisons = loadOsmComparisonFilter();
        final List<EditStatus> editStatuses = loadEditStatusFilter();
        final List<SignType> signTypes = loadSignTypeFilter();
        final List<DetectionMode> modes = loadModes();
        return new DetectionFilter(osmComparisons, editStatuses, signTypes, modes);
    }

    private List<OsmComparison> loadOsmComparisonFilter() {
        final List<OsmComparisonEntry> entries =
                StructUtils.getListOfStructs(Main.pref, FILTER_SEARCH_OSM_COMPARISON, OsmComparisonEntry.class);
        List<OsmComparison> list = null;
        if (entries != null && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final OsmComparisonEntry entry : entries) {
                list.add(OsmComparison.valueOf(entry.getName()));
            }
        }
        return list;
    }

    private List<DetectionMode> loadModes() {
        final String dataTypeVal = Main.pref.get(FILTER_SEARCH_MODE);
        final List<DetectionModeEntry> entries =
                StructUtils.getListOfStructs(Main.pref, FILTER_SEARCH_MODE, DetectionModeEntry.class);
        List<DetectionMode> list;
        if (dataTypeVal.isEmpty() && entries.isEmpty()) {
            list = Arrays.asList(DetectionMode.values());
        } else if (dataTypeVal.equals(FILTER_SEARCH_EMPTY)) {
            list = new ArrayList<>();
        } else {
            list = entries.stream().map(entry -> DetectionMode.valueOf(entry.getName())).collect(Collectors.toList());
        }
        return list;
    }

    private List<EditStatus> loadEditStatusFilter() {
        final List<EditStatusEntry> entries =
                StructUtils.getListOfStructs(Main.pref, FILTER_SEARCH_EDIT_STATUS, EditStatusEntry.class);
        List<EditStatus> list = null;
        if (entries != null && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final EditStatusEntry entry : entries) {
                list.add(EditStatus.valueOf(entry.getName()));
            }
        }
        return list;
    }

    private List<SignType> loadSignTypeFilter() {
        final List<SignTypeEntry> entries =
                StructUtils.getListOfStructs(Main.pref, FILTER_SEARCH_SIGN_TYPE, SignTypeEntry.class);
        List<SignType> list = null;
        if (entries != null && !entries.isEmpty()) {
            list = new ArrayList<>();
            for (final SignTypeEntry entry : entries) {
                list.add(SignType.valueOf(entry.getName()));
            }
        }
        return list;
    }

    MapViewSettings loadMapViewSettings() {
        final int photoZoom = loadIntValue(MAP_VIEW_PHOTO_ZOOM, Config.getInstance().getMapPhotoZoom(),
                Config.getInstance().getPreferencesMaxZoom());
        final boolean manualSwitchFlag = Main.pref.getBoolean(MAP_VIEW_MANUAL_SWITCH);
        return new MapViewSettings(photoZoom, manualSwitchFlag);
    }

    PhotoSettings loadPhotoSettings() {
        final boolean highQualityFlag = Main.pref.getBoolean(HIGH_QUALITY_PHOTO_FLAG);
        final boolean mouseHoverFlag = Main.pref.getBoolean(MOUSE_HOVER_FLAG);
        final int mouseHoverDelay = loadIntValue(MOUSE_HOVER_DELAY, Config.getInstance().getMouseHoverMinDelay(),
                Config.getInstance().getMouseHoverMaxDelay());
        return new PhotoSettings(highQualityFlag, mouseHoverFlag, mouseHoverDelay);
    }

    TrackSettings loadTrackSettings() {
        final String displayTrackFlagVal = Main.pref.get(DISPLAY_TRACK_FLAG);
        final boolean displayTrackFlag = displayTrackFlagVal.isEmpty() ? true : Boolean.valueOf(displayTrackFlagVal);
        return new TrackSettings(displayTrackFlag, loadAutoplaySettings());
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
        final String layerOpened = Main.pref.get(LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadPhotoPanelOpenedFlag() {
        final String layerOpened = Main.pref.get(PHOTO_PANEL_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadDetectionPanelOpenedFlag() {
        final String layerOpened = Main.pref.get(DETECTION_PANEL_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    DataType loadDataType() {
        final String value = Main.pref.get(DATA_TYPE);
        DataType dataType;
        try {
            dataType = DataType.valueOf(value);
        } catch (final RuntimeException e) {
            dataType = null;
        }
        return dataType;
    }

    String loadPluginLocalVersion() {
        return Main.pref.get(PLUGIN_LOCAL_VERSION);
    }

    private Integer loadIntValue(final String key, final Integer defaultValue, final Integer maxValue) {
        final String valueStr = Main.pref.get(key);
        final Integer value = (valueStr != null && !valueStr.isEmpty()) ? Integer.valueOf(valueStr) : defaultValue;
        return maxValue != null ? (value > maxValue ? maxValue : value) : value;
    }
}