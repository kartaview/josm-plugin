/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
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
import java.util.Date;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.TrackSettings;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.CacheConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;


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

    boolean loadSequenceErrorSuppressFlag() {
        return Main.pref.getBoolean(SUPPRESS_SEQUENCE_ERROR);
    }

    ListFilter loadListFilter() {
        final String dateStr = Main.pref.get(FILTER_DATE);
        Date date = null;
        if (!dateStr.isEmpty()) {
            date = new Date(Long.parseLong(dateStr));
        }
        final String onlyUserFlagStr = Main.pref.get(FILTER_ONLY_USER_FLAG);
        final boolean onlyUserFlag =
                onlyUserFlagStr.isEmpty() ? ListFilter.DEFAULT.isOnlyUserFlag() : Boolean.parseBoolean(onlyUserFlagStr);
        return new ListFilter(date, onlyUserFlag);
    }

    MapViewSettings loadMapViewSettings() {
        final String photoZoomVal = Main.pref.get(MAP_VIEW_PHOTO_ZOOM);
        final int photoZoom = (photoZoomVal != null && !photoZoomVal.isEmpty()) ? Integer.valueOf(photoZoomVal)
                : Config.getInstance().getMapPhotoZoom();
        final boolean manualSwitchFlag = Main.pref.getBoolean(MAP_VIEW_MANUAL_SWITCH);
        return new MapViewSettings(photoZoom, manualSwitchFlag);

    }

    PhotoSettings loadPhotoSettings() {
        final boolean highQualityFlag = Main.pref.getBoolean(HIGH_QUALITY_PHOTO_FLAG);
        final boolean mouseHoverFlag = Main.pref.getBoolean(MOUSE_HOVER_FLAG);
        final String mouseHoverDelayValue = Main.pref.get(MOUSE_HOVER_DELAY);
        final int mouseHoverDelay = (mouseHoverDelayValue != null && !mouseHoverDelayValue.isEmpty())
                ? Integer.valueOf(mouseHoverDelayValue) : Config.getInstance().getMouseHoverMinDelay();
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
        final Integer delay = delayValue == null || delayValue.isEmpty() ? Config.getInstance().getAutoplayMinDelay()
                : Integer.valueOf(delayValue);
        return new AutoplaySettings(length, delay);
    }

    boolean loadAutoplayStartedFlag() {
        return Main.pref.getBoolean(AUTOPLAY_STARTED);
    }

    CacheSettings loadCacheSettings() {
        final String memoryCountVal = Main.pref.get(CACHE_MEMORY_COUNT);
        final int memoryCount = (memoryCountVal != null && !memoryCountVal.isEmpty()) ? Integer.valueOf(memoryCountVal)
                : CacheConfig.getInstance().getDefaultMemoryCount();
        final String diskCountVal = Main.pref.get(CACHE_DISK_COUNT);
        final int diskCount = (diskCountVal != null && !diskCountVal.isEmpty()) ? Integer.valueOf(diskCountVal)
                : CacheConfig.getInstance().getDefaultDiskCount();
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