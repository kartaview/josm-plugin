/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
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
        Main.pref.put(Keys.SUPPRESS_PHOTOS_ERROR, flag);
    }

    void saveSequenceErrorSuppressFlag(final boolean flag) {
        Main.pref.put(Keys.SUPPRESS_SEQUENCE_ERROR, flag);
    }

    void saveFiltersChangedFlag(final boolean changed) {
        Main.pref.put(Keys.FILTERS_CHANGED, "");
        Main.pref.put(Keys.FILTERS_CHANGED, Boolean.toString(changed));
    }

    void saveListFilter(final ListFilter filter) {
        if (filter != null) {
            final String dateStr = filter.getDate() != null ? Long.toString(filter.getDate().getTime()) : "";
            Main.pref.put(Keys.DATE, dateStr);
            Main.pref.put(Keys.ONLY_USER_FLAG, filter.isOnlyUserFlag());
        }
    }

    void savePhotoSettings(final PhotoSettings settings) {
        Main.pref.put(Keys.HIGH_QUALITY_PHOTO_FLAG, settings.isHighQualityFlag());
        Main.pref.put(Keys.DISPLAY_TRACK_FLAG, settings.isDisplayTrackFlag());
    }

    void saveCacheSettings(final CacheSettings cacheSettings) {
        Main.pref.putInteger(Keys.CACHE_MEMORY_COUNT, cacheSettings.getMemoryCount());
        Main.pref.putInteger(Keys.CACHE_DISK_COUNT, cacheSettings.getDiskCount());
        Main.pref.putInteger(Keys.CACHE_PREV_NEXT_COUNT, cacheSettings.getPrevNextCount());
        Main.pref.putInteger(Keys.CACHE_NEARBY_COUNT, cacheSettings.getNearbyCount());
    }

    void saveLayerOpenedFlag(final boolean isLayerOpened) {
        Main.pref.put(Keys.LAYER_OPENED, isLayerOpened);
    }

    void savePanelOpenedFlag(final boolean isPanelOpened) {
        Main.pref.put(Keys.PANEL_OPENED, isPanelOpened);
    }
}