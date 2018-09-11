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
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.DETECTION_PANEL_ICON_VISIBILITY;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.DISPLAY_TRACK_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.FILTER_CHANGED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.HIGH_QUALITY_PHOTO_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.JOSM_AUTH_METHOD;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.JOSM_BASIC_VAL;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.JOSM_OAUTH_SECRET;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.LAYER_OPENED;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_MANUAL_SWITCH;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MAP_VIEW_PHOTO_ZOOM;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_DELAY;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.MOUSE_HOVER_FLAG;
import static org.openstreetmap.josm.plugins.openstreetcam.util.pref.Keys.PHOTO_PANEL_ICON_VISIBILITY;

import org.openstreetmap.josm.plugins.openstreetcam.argument.*;


/**
 * Utility class, manages save and load operations of the preference variables. The preference variables are saved into
 * a global preference file. Preference variables are static variables which can be accessed from any plugin class.
 * Values saved in this global file, can be accessed also after a JOSM restart.
 *
 * @author Beata
 * @version $Revision$
 */
public final class PreferenceManager {

    private static final PreferenceManager INSTANCE = new PreferenceManager();

    private final LoadManager loadManager = new LoadManager();
    private final SaveManager saveManager = new SaveManager();

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
        return loadManager.loadPhotosErrorSuppressFlag();
    }

    /**
     * Saves the photo search error suppress flag.
     *
     * @param flag a boolean value
     */
    public void savePhotosErrorSuppressFlag(final boolean flag) {
        saveManager.savePhotosErrorSuppressFlag(flag);
    }

    public void saveDetectionSearchErrorSuppressFlag(final boolean flag) {
        saveManager.saveDetectionSearchErrorSuppressFlag(flag);
    }

    public void saveSequenceDetectionsErrorFlag(final boolean flag) {
        saveManager.saveSequenceDetectionsErrorFlag(flag);
    }

    public void savePhotoDetectionsErrorFlag(final boolean flag) {
        saveManager.savePhotoDetectionsErrorFlag(flag);
    }

    public void saveDetectionUpdateErrorSuppressFlag(final boolean flag) {
        saveManager.saveDetectionUpdateErrorSuppressFlag(flag);
    }

    public boolean loadDetectionSearchErrorSuppressFlag() {
        return loadManager.loadDetectionSearchErrorSuppressFlag();
    }

    public boolean loadSequenceDetectionsErrorFlag() {
        return loadManager.loadSequenceDetectionsErrorFlag();
    }

    public boolean loadPhotoDetectionsErrorFlag() {
        return loadManager.loadPhotoDetectionsErrorFlag();
    }

    public boolean loadDetectionUpdateErrorSuppressFlag() {
        return loadManager.loadDetectionUpdateErrorSuppressFlag();
    }

    /**
     * Loads the sequence error suppress flag. If this value is true, then all the future sequence retrieve errors will
     * be suppressed.
     *
     * @return a boolean value
     */
    public boolean loadSequenceErrorSuppressFlag() {
        return loadManager.loadSequenceErrorSuppressFlag();
    }

    /**
     * Saves the sequence error suppress flag.
     *
     * @param flag a boolean value
     */
    public void saveSequenceErrorSuppressFlag(final boolean flag) {
        saveManager.saveSequenceErrorSuppressFlag(flag);
    }

    public boolean loadSegmentsErrorSuppressFlag() {
        return loadManager.loadSegmentsErrorSuppressFlag();
    }

    public void saveSegmentsErrorSuppressFlag(final boolean flag) {
        saveManager.saveSegmentsErrorSuppressFlag(flag);
    }

    /**
     * Saves the 'filtersChanged' flag to the preference file.
     *
     * @param changed a boolean value
     */
    public void saveFiltersChangedFlag(final boolean changed) {
        saveManager.saveFiltersChangedFlag(changed);
    }

    /**
     * Loads the list filters from the preference file.
     *
     * @return a {@code ListFilter}
     */
    public SearchFilter loadSearchFilter() {
        return loadManager.loadSearchFilter();
    }

    /**
     * Saves the list filter to the preference file.
     *
     * @param filter a {@code ListFilter} represents the current filter settings
     */
    public void saveListFilter(final SearchFilter filter) {
        saveManager.saveSearchFilter(filter);
    }

    /**
     * Loads the user's OpenStreetCam plugin related preference settings from the preference file.
     *
     * @return a {@code PreferenceSettings} object
     */
    public PreferenceSettings loadPreferenceSettings() {
        return new PreferenceSettings(loadMapViewSettings(), loadPhotoSettings(), loadAggregatedSettings(), loadTrackSettings(),
                loadCacheSettings());
    }

    /**
     * Loads the the user's map view related preference settings from the preference file.
     *
     * @return a {@code MapViewSettings} object
     */
    public MapViewSettings loadMapViewSettings() {
        return loadManager.loadMapViewSettings();
    }

    public boolean loadOnlyDetectionFilterChangedFlag() {
        return loadManager.loadOnlyDetectionFilterChangedFlag();
    }

    public void saveOnlyDetectionFilterChangedFlag(final boolean flag) {
        saveManager.saveOnlyDetectionFilterChangedFlag(flag);
    }

    /**
     * Loads the photo settings from the preference file.
     *
     * @return a {@code PhotoSettings}
     */
    public PhotoSettings loadPhotoSettings() {
        return loadManager.loadPhotoSettings();
    }

    private AggregatedDetectionSettings loadAggregatedSettings() {
        return loadManager.loadAggregatedSettings();
    }

    public SequenceSettings loadTrackSettings() {
        return loadManager.loadTrackSettings();
    }

    public AutoplaySettings loadAutoplaySettings() {
        return loadManager.loadAutoplaySettings();
    }

    public boolean loadAutoplayStartedFlag() {
        return loadManager.loadAutoplayStartedFlag();
    }

    public void saveAutoplayStartedFlag(final boolean flag) {
        saveManager.saveAutoplayStartedFlag(flag);
    }

    /**
     * Loads the cache preference settings.
     *
     * @return a {@code CacheSettings}
     */
    public CacheSettings loadCacheSettings() {
        return loadManager.loadCacheSettings();
    }

    /**
     * Saves the user's preference settings to the preference file.
     *
     * @param preferenceSettings a {@code PreferenceSettings} represents the newly set preference settings
     */
    public void savePreferenceSettings(final PreferenceSettings preferenceSettings) {
        if (preferenceSettings != null) {
            saveManager.saveMapViewSettings(preferenceSettings.getMapViewSettings());
            saveManager.savePhotoSettings(preferenceSettings.getPhotoSettings());
            saveManager.saveAggregatedSettings(preferenceSettings.getAggregatedSettings());
            saveManager.saveTrackSettings(preferenceSettings.getTrackSettings());
            saveManager.saveCacheSettings(preferenceSettings.getCacheSettings());
        }
    }

    /**
     * Loads the layer appearance status from the preference file.
     *
     * @return a boolean value
     */
    public boolean loadLayerOpenedFlag() {
        return loadManager.loadLayerOpenedFlag();
    }

    /**
     * Saves the layer appearance status to the preference file.
     *
     * @param isLayerOpened represents the layer showing/hiding status
     */
    public void saveLayerOpenedFlag(final boolean isLayerOpened) {
        saveManager.saveLayerOpenedFlag(isLayerOpened);
    }

    /**
     * Loads the panel appearance status flag from the preference file.
     *
     * @return a boolean a boolean value
     */
    public boolean loadPhotoPanelOpenedFlag() {
        return loadManager.loadPhotoPanelOpenedFlag();
    }

    public boolean loadDetectionPanelOpenedFlag() {
        return loadManager.loadDetectionPanelOpenedFlag();
    }

    /**
     * Saves the panel appearance status flag to the preference file.
     *
     * @param value true/false in string format
     */
    public void savePhotoPanelOpenedFlag(final String value) {
        final Boolean panelOpened = Boolean.parseBoolean(value);
        saveManager.savePhotoPanelOpenedFlag(panelOpened);
    }

    public void saveDetectionPanelOpenedFlag(final String value) {
        final Boolean panelOpened = Boolean.parseBoolean(value);
        saveManager.saveDetectionPanelOpenedFlag(panelOpened);
    }

    /**
     * Verifies if the data download related preference settings has changed or not.
     *
     * @param key a {@code String} represents the key associated with the authentication preference change event
     * @param newValue a {@code String} represents the new value associated with the authentication preference change
     * event
     * @return true if the data download preference settings has been changed; false otherwise
     */
    public boolean dataDownloadPreferencesChanged(final String key, final String newValue) {
        return isFiltersChangedKey(key, newValue) || isMapViewZoomKey(key) || hasAuthMethodChanged(key, newValue)
                || isLayerOpenedFlag(key, newValue);
    }

    private boolean hasAuthMethodChanged(final String key, final String value) {
        return (JOSM_AUTH_METHOD.equals(key) && JOSM_BASIC_VAL.equals(value)) || JOSM_OAUTH_SECRET.equals(key);
    }

    private boolean isFiltersChangedKey(final String value, final String newValue) {
        return FILTER_CHANGED.equals(value) && Boolean.TRUE.toString().equals(newValue);
    }

    private boolean isMapViewZoomKey(final String value) {
        return MAP_VIEW_PHOTO_ZOOM.equals(value);
    }

    private boolean isLayerOpenedFlag(final String key, final String newValue) {
        return LAYER_OPENED.equals(key) && Boolean.TRUE.toString().equals(newValue);
    }

    /**
     * Verifies if the high quality photo user preference settings flag has been selected or not.
     *
     * @param key a {@code String} represents the key associated with the high quality flag
     * @param newValue a {@code String} represents the new value associated with the high quality preference change
     * event
     * @return true if the flag has been selected, false otherwise
     */
    public boolean hasHighQualityPhotoFlagChanged(final String key, final String newValue) {
        return HIGH_QUALITY_PHOTO_FLAG.equals(key) && Boolean.TRUE.toString().equals(newValue);
    }

    /**
     * Verifies if the mouse hover user preference settings flag has been selected or not.
     *
     * @param key a {@code String} represents the key associated with the mouse hover flag
     * @param newValue a {@code String} represents the new value associated with the flag
     * @return true if the flag has been selected, false otherwise
     */
    public boolean hasMouseHoverFlagChanged(final String key, final String newValue) {
        return MOUSE_HOVER_FLAG.equals(key) && Boolean.TRUE.toString().equals(newValue);
    }

    /**
     * Verifies if the given key represents the display track flag or not.
     *
     * @param key a {@code String} value
     * @return boolean value
     */
    public boolean isDisplayTrackFlag(final String key) {
        return DISPLAY_TRACK_FLAG.equals(key);
    }

    /**
     * Verifies if the manual data switch type has been changed or not.
     *
     * @param key a {@code String} represents the key associated with the manual data switch type
     * @param newValue a {@code String} represents the new value associated with the manual data switch typee change
     * event
     * @return boolean value
     */
    public boolean hasManualSwitchDataTypeChanged(final String key, final String newValue) {
        return MAP_VIEW_MANUAL_SWITCH.equals(key) && newValue != null;
    }

    /**
     * Saves the selected map view type to the preference file.
     *
     * @param mapViewType the currently selected {@code MapViewType}
     */
    public void saveMapViewType(final MapViewType mapViewType) {
        saveManager.saveMapViewType(mapViewType);
    }

    /**
     * Loads the map view type.
     *
     * @return a {@code MapViewType} object
     */
    public MapViewType loadMapViewType() {
        return loadManager.loadMapViewType();
    }

    /**
     * Verifies if the given key represents the panel visibility icon key.
     *
     * @param key a {@code String} value
     * @return a boolean value
     */
    public boolean isPhotoPanelIconVisibilityKey(final String key) {
        return PHOTO_PANEL_ICON_VISIBILITY.equals(key);
    }

    public boolean isDetectionPanelIconVisibilityKey(final String key) {
        return DETECTION_PANEL_ICON_VISIBILITY.equals(key);
    }

    /**
     * Verifies if the given key represents the mouse hover delay key.
     *
     * @param key a {@code String} value
     * @return a boolean value
     */
    public boolean isMouseHoverDelayKey(final String key) {
        return MOUSE_HOVER_DELAY.equals(key);
    }

    public boolean isAutoplayDelayKey(final String key) {
        return AUTOPLAY_DELAY.equals(key);
    }

    /**
     * Saves the plugin's local version to the preference file.
     *
     * @param localVersion the plugin's local version
     */
    public void savePluginLocalVersion(final String localVersion) {
        saveManager.savePluginLocalVersion(localVersion);
    }

    /**
     * Loads the plugin's local version from the preference file.
     *
     * @return a {@code String} value
     */
    public String loadPluginLocalVersion() {
        return loadManager.loadPluginLocalVersion();
    }
}