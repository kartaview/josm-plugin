/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref;

import java.util.Date;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ImageSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PreferenceSettings;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.CacheConfig;


/**
 * Utility class, manages save and load (put & get) operations of the preference variables. The preference variables are
 * saved into a global preference file. Preference variables are static variables which can be accessed from any plugin
 * class. Values saved in this global file, can be accessed also after a JOSM restart.
 *
 * @author Beata
 * @version $Revision$
 */
public final class PreferenceManager {

    private static final PreferenceManager INSTANCE = new PreferenceManager();

    private PreferenceManager() {}

    public static PreferenceManager getInstance() {
        return INSTANCE;
    }

    /**
     * Loads the photo search error suppress flag. If this value is true, then all the future photo search service
     * errors will be suppressed.
     *
     * @return a boolean value
     */
    public boolean loadPhotosErrorSuppressFlag() {
        return Main.pref.getBoolean(Keys.SUPPRESS_PHOTOS_ERROR);
    }

    /**
     * Saves the photo search error suppress flag.
     *
     * @param flag a boolean value
     */
    public void savePhotosErrorSuppressFlag(final boolean flag) {
        Main.pref.put(Keys.SUPPRESS_PHOTOS_ERROR, flag);
    }

    /**
     * Loads the sequence error suppress flag. If this value is true, then all the future sequence retrieve errors will
     * be suppressed.
     *
     * @return a boolean value
     */
    public boolean loadSequenceErrorSuppressFlag() {
        return Main.pref.getBoolean(Keys.SUPPRESS_SEQUENCE_ERROR);
    }

    /**
     * Saves the sequence error suppress flag.
     *
     * @param flag a boolean value
     */
    public void saveSequenceErrorSuppressFlag(final boolean flag) {
        Main.pref.put(Keys.SUPPRESS_SEQUENCE_ERROR, flag);
    }

    /**
     * Saves the 'filtersChanged' flag to the preference file.
     *
     * @param changed a boolean value
     */
    public void saveFiltersChangedFlag(final boolean changed) {
        Main.pref.put(Keys.FILTERS_CHANGED, "");
        Main.pref.put(Keys.FILTERS_CHANGED, Boolean.toString(changed));
    }


    /**
     * Loads the list filters from the preference file.
     *
     * @return a {@code ListFilter}
     */
    public ListFilter loadListFilter() {
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

    /**
     * Saves the list filter to the preference file.
     *
     * @param filter a {@code ListFilter} represents the current filter settings
     */
    public void saveListFilter(final ListFilter filter) {
        if (filter != null) {
            final String dateStr = filter.getDate() != null ? Long.toString(filter.getDate().getTime()) : "";
            Main.pref.put(Keys.DATE, dateStr);
            Main.pref.put(Keys.ONLY_USER_FLAG, filter.isOnlyUserFlag());
        }
    }

    public void savePreferenceSettings(final PreferenceSettings preferenceSettings) {
        if (preferenceSettings != null) {
            saveImageSettings(preferenceSettings.getImageSettings());
            saveCacheSettings(preferenceSettings.getCacheSettings());
        }
    }

    private void saveImageSettings(final ImageSettings imageSettings) {
        Main.pref.put(Keys.HIGH_QUALITY_PHOTO_FLAG, imageSettings.isHighQualityFlag());
        Main.pref.put(Keys.DISPLAY_TRACK_FLAG, imageSettings.isDisplayTrackFlag());
    }

    private void saveCacheSettings(final CacheSettings cacheSettings) {
        Main.pref.putInteger(Keys.CACHE_MEMORY_COUNT, cacheSettings.getMemoryCount());
        Main.pref.putInteger(Keys.CACHE_DISK_COUNT, cacheSettings.getDiskCount());
        Main.pref.putInteger(Keys.CACHE_PREV_NEXT_COUNT, cacheSettings.getPrevNextCount());
        Main.pref.putInteger(Keys.CACHE_NEARBY_COUNT, cacheSettings.getNearbyCount());
    }

    public PreferenceSettings loadPreferenceSettings() {
        final ImageSettings imageSettings = loadImageSettings();
        final CacheSettings cacheSettings = loadCacheSettings();
        return new PreferenceSettings(imageSettings, cacheSettings);
    }

    private ImageSettings loadImageSettings() {
        final boolean highQualityFlag = Main.pref.getBoolean(Keys.HIGH_QUALITY_PHOTO_FLAG);
        final String displayTrackFlagVal = Main.pref.get(Keys.DISPLAY_TRACK_FLAG);
        final boolean displayTrackFlag = displayTrackFlagVal.isEmpty() ? true : Boolean.valueOf(displayTrackFlagVal);
        return new ImageSettings(highQualityFlag, displayTrackFlag);
    }

    private CacheSettings loadCacheSettings() {
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

    /**
     * Loads the layer appearance status from the preference file.
     *
     * @return a boolean
     */
    public boolean loadLayerOpenedFlag() {
        final String layerOpened = Main.pref.get(Keys.LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    /**
     * Saves the layer appearance status to the preference file.
     *
     * @param isLayerOpened represents the layer showing/hiding status
     */
    public void saveLayerOpenedFlag(final boolean isLayerOpened) {
        Main.pref.put(Keys.LAYER_OPENED, isLayerOpened);
    }

    /**
     * Loads the panel appearance status from the preference file.
     *
     * @return a boolean
     */
    public boolean loadPanelOpenedFlag() {
        final String layerOpened = Main.pref.get(Keys.PANEL_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    /**
     * Saves the panel appearance status to the preference file
     *
     * @param isPanelOpened represents the panel showing/hiding status
     */
    public void savePanelOpenedFlag(final boolean isPanelOpened) {
        Main.pref.put(Keys.PANEL_OPENED, isPanelOpened);
    }

    public boolean hasAuthMethodChanged(final String key, final String value) {
        return (Keys.JOSM_AUTH_METHOD.equals(key)
                && (Keys.JOSM_AUTH_VAL.equals(value) || Keys.JOSM_BASIC_VAL.equals(value))
                || Keys.JOSM_OAUTH_SECRET.equals(key));
    }
}