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
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_DATE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_ONLY_USER_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_EDIT_STATUS;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_OSM_COMPARISON;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_PHOTO_TYPE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_SEARCH_SIGN_TYPE;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.HIGH_QUALITY_PHOTO_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.LAYER_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_MANUAL_SWITCH;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_PHOTO_ZOOM;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_DELAY;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_FLAG;
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
import java.util.Date;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoDataTypeFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.TrackSettings;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.SignType;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.CacheConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity.EditStatusEntry;
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

    SearchFilter loadSearchFilter() {
        final String dateStr = Main.pref.get(FILTER_DATE);
        Date date = null;
        if (!dateStr.isEmpty()) {
            date = new Date(Long.parseLong(dateStr));
        }
        final String onlyUserFlagStr = Main.pref.get(FILTER_ONLY_USER_FLAG);
        final boolean onlyUserFlag = onlyUserFlagStr.isEmpty() ? false : Boolean.parseBoolean(onlyUserFlagStr);
     //   final String photoTypeVal = Main.pref.get(FILTER_SEARCH_PHOTO_TYPE);
        final String photoTypeVal = "";
        final PhotoDataTypeFilter photoType = photoTypeVal != null && !photoTypeVal.isEmpty()
                ? PhotoDataTypeFilter.valueOf(photoTypeVal) : PhotoDataTypeFilter.ALL;
        final List<OsmComparison> osmComparisons = loadOsmComparisonFilter();
        final List<EditStatus> editStatuses = loadEditStatusFilter();
        final List<SignType> signTypes = loadSignTypeFilter();
        return new SearchFilter(date, onlyUserFlag, photoType, osmComparisons, editStatuses, signTypes);
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
        final String photoZoomVal = Main.pref.get(MAP_VIEW_PHOTO_ZOOM);
        int photoZoom = (photoZoomVal != null && !photoZoomVal.isEmpty()) ? Integer.valueOf(photoZoomVal)
                : org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config.getInstance().getMapPhotoZoom();
        final int maxZoom =
                org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config.getInstance().getPreferencesMaxZoom();
        photoZoom = photoZoom > maxZoom ? maxZoom : photoZoom;
        final boolean manualSwitchFlag = Main.pref.getBoolean(MAP_VIEW_MANUAL_SWITCH);
        return new MapViewSettings(photoZoom, manualSwitchFlag);

    }


    PhotoSettings loadPhotoSettings() {
        final boolean highQualityFlag = Main.pref.getBoolean(HIGH_QUALITY_PHOTO_FLAG);
        final boolean mouseHoverFlag = Main.pref.getBoolean(MOUSE_HOVER_FLAG);
        final String mouseHoverDelayValue = Main.pref.get(MOUSE_HOVER_DELAY);
        int mouseHoverDelay = (mouseHoverDelayValue != null && !mouseHoverDelayValue.isEmpty())
                ? Integer.valueOf(mouseHoverDelayValue)
                : org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config.getInstance().getMouseHoverMinDelay();
        final int maxDelay =
                org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config.getInstance().getMouseHoverMaxDelay();
        mouseHoverDelay = mouseHoverDelay > maxDelay ? maxDelay : mouseHoverDelay;
        return new PhotoSettings(highQualityFlag, mouseHoverFlag, mouseHoverDelay);
    }

    TrackSettings loadTrackSettings() {
        final String displayTrackFlagVal = Main.pref.get(DISPLAY_TRACK_FLAG);
        final boolean displayTrackFlag = displayTrackFlagVal.isEmpty() ? true : Boolean.valueOf(displayTrackFlagVal);
        return new TrackSettings(displayTrackFlag, loadAutoplaySettings());
    }

    AutoplaySettings loadAutoplaySettings() {
        final String lengthValue = Main.pref.get(AUTOPLAY_LENGTH);
        final Integer length = lengthValue == null || lengthValue.isEmpty() ? null : Integer.valueOf(lengthValue);
        final String delayValue = Main.pref.get(AUTOPLAY_DELAY);
        Integer delay = delayValue == null || delayValue.isEmpty()
                ? org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config.getInstance().getAutoplayMinDelay()
                : Integer.valueOf(delayValue);
        delay = delay > org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config.getInstance().getAutoplayMaxDelay()
                ? org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config.getInstance().getAutoplayMaxDelay()
                : delay;
        return new AutoplaySettings(length, delay);
    }

    boolean loadAutoplayStartedFlag() {
        return Main.pref.getBoolean(AUTOPLAY_STARTED);
    }

    CacheSettings loadCacheSettings() {
        final String memoryCountVal = Main.pref.get(CACHE_MEMORY_COUNT);
        int memoryCount = (memoryCountVal != null && !memoryCountVal.isEmpty()) ? Integer.valueOf(memoryCountVal)
                : CacheConfig.getInstance().getDefaultMemoryCount();
        memoryCount = memoryCount > CacheConfig.getInstance().getMaxMemoryCount()
                ? CacheConfig.getInstance().getMaxMemoryCount() : memoryCount;
        final String diskCountVal = Main.pref.get(CACHE_DISK_COUNT);
        int diskCount = (diskCountVal != null && !diskCountVal.isEmpty()) ? Integer.valueOf(diskCountVal)
                : CacheConfig.getInstance().getDefaultDiskCount();
        diskCount = diskCount > CacheConfig.getInstance().getMaxDiskCount()
                ? CacheConfig.getInstance().getMaxDiskCount() : diskCount;
        final String prevNextCountVal = Main.pref.get(CACHE_PREV_NEXT_COUNT);
        int prevNextCount = (prevNextCountVal != null && !prevNextCountVal.isEmpty())
                ? Integer.valueOf(prevNextCountVal) : CacheConfig.getInstance().getDefaultPrevNextCount();
        prevNextCount = prevNextCount > CacheConfig.getInstance().getMaxNearbyCount()
                ? CacheConfig.getInstance().getMaxNearbyCount() : prevNextCount;
        final String nearbyCountVal = Main.pref.get(CACHE_NEARBY_COUNT);
        int nearbyCount = (nearbyCountVal != null && !nearbyCountVal.isEmpty()) ? Integer.valueOf(nearbyCountVal)
                : CacheConfig.getInstance().getDefaultNearbyCount();
        nearbyCount = nearbyCount > CacheConfig.getInstance().getMaxNearbyCount()
                ? CacheConfig.getInstance().getMaxNearbyCount() : nearbyCount;
        return new CacheSettings(memoryCount, diskCount, prevNextCount, nearbyCount);
    }

    boolean loadLayerOpenedFlag() {
        final String layerOpened = Main.pref.get(LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadPanelOpenedFlag() {
        final String layerOpened = Main.pref.get(PANEL_OPENED);
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
}