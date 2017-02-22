/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref;

import java.util.Date;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.CacheConfig;


/**
 * Helper class, manages the load operations of the preference variables. The preference variables are loaded from the
 * global preference file.
 *
 * @author beataj
 * @version $Revision$
 */
final class LoadManager {

    boolean loadPhotosErrorSuppressFlag() {
        return Main.pref.getBoolean(Keys.SUPPRESS_PHOTOS_ERROR);
    }

    boolean loadSequenceErrorSuppressFlag() {
        return Main.pref.getBoolean(Keys.SUPPRESS_SEQUENCE_ERROR);
    }


    ListFilter loadListFilter() {
        final String dateStr = Main.pref.get(Keys.DATE);
        Date date = null;
        if (!dateStr.isEmpty()) {
            date = new Date(Long.parseLong(dateStr));
        }
        final String onlyUserFlagStr = Main.pref.get(Keys.ONLY_USER_FLAG);
        final boolean onlyUserFlag =
                onlyUserFlagStr.isEmpty() ? ListFilter.DEFAULT.isOnlyUserFlag() : Boolean.parseBoolean(onlyUserFlagStr);
                return new ListFilter(date, onlyUserFlag);
    }

    PhotoSettings loadPhotoSettings() {
        final boolean highQualityFlag = Main.pref.getBoolean(Keys.HIGH_QUALITY_PHOTO_FLAG);
        final String displayTrackFlagVal = Main.pref.get(Keys.DISPLAY_TRACK_FLAG);
        final boolean displayTrackFlag = displayTrackFlagVal.isEmpty() ? true : Boolean.valueOf(displayTrackFlagVal);
        return new PhotoSettings(highQualityFlag, displayTrackFlag);
    }

    CacheSettings loadCacheSettings() {
        final String memoryCountVal = Main.pref.get(Keys.CACHE_MEMORY_COUNT);
        final int memoryCount = (memoryCountVal != null && !memoryCountVal.isEmpty()) ? Integer.valueOf(memoryCountVal)
                : CacheConfig.getInstance().getDefaultMemoryCount();
        final String diskCountVal = Main.pref.get(Keys.CACHE_DISK_COUNT);
        final int diskCount = (diskCountVal != null && !diskCountVal.isEmpty()) ? Integer.valueOf(diskCountVal)
                : CacheConfig.getInstance().getDefaultDiskCount();
        final String prevNextCountVal = Main.pref.get(Keys.CACHE_PREV_NEXT_COUNT);
        final int prevNextCount = (prevNextCountVal != null && !prevNextCountVal.isEmpty())
                ? Integer.valueOf(prevNextCountVal) : CacheConfig.getInstance().getDefaultPrevNextCount();
                final String nearbyCountVal = Main.pref.get(Keys.CACHE_NEARBY_COUNT);
                final int nearbyCount = (nearbyCountVal != null && !nearbyCountVal.isEmpty()) ? Integer.valueOf(nearbyCountVal)
                        : CacheConfig.getInstance().getDefaultNearbyCount();
                return new CacheSettings(memoryCount, diskCount, prevNextCount, nearbyCount);
    }

    boolean loadLayerOpenedFlag() {
        final String layerOpened = Main.pref.get(Keys.LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadPanelOpenedFlag() {
        final String layerOpened = Main.pref.get(Keys.PANEL_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

}