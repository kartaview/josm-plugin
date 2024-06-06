/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.pref;


import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.ACCESS_TOKEN;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.AUTOPLAY_DELAY;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DETECTION_PANEL_ICON_VISIBILITY;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DISPLAY_DETECTION_LOCATIONS;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DISPLAY_FRONT_FACING_FLAG;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.DISPLAY_TRACK_FLAG;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.EDGE_DETECTION_PANEL_ICON_VISIBILITY;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.EDGE_FILTER_CHANGED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.EDGE_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.FILTER_CHANGED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.HIGH_QUALITY_PHOTO_FLAG;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.KARTAVIEW_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.MAP_VIEW_DATA_LOAD;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.MAP_VIEW_PHOTO_ZOOM;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.MOUSE_HOVER_DELAY;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.MOUSE_HOVER_FLAG;
import static org.openstreetmap.josm.plugins.kartaview.util.pref.Keys.PHOTO_PANEL_ICON_VISIBILITY;

import org.openstreetmap.josm.plugins.kartaview.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.kartaview.argument.CacheSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.ClusterSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.PreferenceSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.SearchFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.SequenceSettings;


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

    private PreferenceManager() {
    }

    public static PreferenceManager getInstance() {
        return INSTANCE;
    }

    public void savePhotosSearchErrorSuppressFlag(final boolean flag) {
        saveManager.savePhotosSearchErrorSuppressFlag(flag);
    }

    public boolean loadPhotosSearchErrorSuppressFlag() {
        return loadManager.loadPhotosSearchErrorSuppressFlag();
    }

    public void saveDetectionsSearchErrorSuppressFlag(final boolean flag) {
        saveManager.saveDetectionsSearchErrorSuppressFlag(flag);
    }

    public void saveEdgeDetectionsSearchErrorSuppressFlag(final boolean flag) {
        saveManager.saveEdgeDetectionsSearchErrorSuppressFlag(flag);
    }

    public boolean loadDetectionsSearchErrorSuppressFlag() {
        return loadManager.loadDetectionsSearchErrorSuppressFlag();
    }

    public boolean loadEdgeDetectionsSearchErrorSuppressFlag() {
        return loadManager.loadEdgeDetectionsSearchErrorSuppressFlag();
    }

    public boolean loadClustersSearchErrorSuppressFlag() {
        return loadManager.loadClustersSearchErrorSuppressFlag();
    }

    public boolean loadEdgeClustersSearchErrorSuppressFlag() {
        return loadManager.loadEdgeClusterSearchErrorSuppressFlag();
    }

    public boolean loadEdgeDataOperationErrorSuppressFlag() {
        return loadManager.loadEdgeDataOperationErrorSuppressFlag();
    }

    public void saveClustersSearchErrorSuppressFlag(final boolean flag) {
        saveManager.saveClustersSearchErrorSuppressFlag(flag);
    }

    public void saveEdgeClustersSearchErrorSuppressFlag(final boolean flag) {
        saveManager.saveEdgeClustersSearchErrorSuppressFlag(flag);
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

    public void saveSequenceDetectionsErrorFlag(final boolean flag) {
        saveManager.saveSequenceDetectionsErrorFlag(flag);
    }

    public void savePhotoDetectionsErrorFlag(final boolean flag) {
        saveManager.savePhotoDetectionsErrorFlag(flag);
    }

    public void saveDetectionUpdateErrorSuppressFlag(final boolean flag) {
        saveManager.saveDetectionUpdateErrorSuppressFlag(flag);
    }

    public void saveEdgeDataOperationErrorSuppressFlag(final boolean flag) {
        saveManager.saveEdgeDataOperationErrorSuppressFlag(flag);
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

    public boolean loadListSignsErrorSuppressFlag() {
        return loadManager.loadListSignErrorFlag();
    }

    public void saveListSignsErrorSuppressFlag(final boolean flag) {
        saveManager.saveListSignErrorSuppressFlag(flag);
    }

    public boolean loadListSignRegionsSuppressFlag() {
        return loadManager.loadListSignRegionErrorFlag();
    }

    public void saveListSignRegionErrorSuppressFlag(final boolean flag) {
        saveManager.saveListSignRegionErrorSuppressFlag(flag);
    }

    /**
     * Saves the 'filtersChanged' flag to the preference file.
     *
     * @param changed a boolean value
     */
    public void saveFiltersChangedFlag(final boolean changed) {
        saveManager.saveFiltersChangedFlag(changed, FILTER_CHANGED);
    }

    /**
     * Saves the 'edgeFiltersChanged' flag to the preference file.
     *
     * @param edgeFiltersChanged a boolean value
     */
    public void saveEdgeFiltersChangedFlag(final boolean edgeFiltersChanged) {
        saveManager.saveFiltersChangedFlag(edgeFiltersChanged, EDGE_FILTER_CHANGED);
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
     * Loads the edge filters from the preference file.
     *
     * @return a {@code EdgeSearchFilter}
     */
    public EdgeSearchFilter loadEdgeSearchFilter() {
        return loadManager.loadEdgeSearchFilter();
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
     * Saves the list edge filter to the preference file.
     *
     * @param edgeSearchFilter a {@code EdgeSearchFilter} represents the current edge filter settings
     */
    public void saveEdgeSearchFilter(final EdgeSearchFilter edgeSearchFilter) {
        saveManager.saveEdgeSearchFilter(edgeSearchFilter);
    }

    /**
     * Loads the user's KartaView plugin related preference settings from the preference file.
     *
     * @return a {@code PreferenceSettings} object
     */
    public PreferenceSettings loadPreferenceSettings() {
        return new PreferenceSettings(loadMapViewSettings(), loadPhotoSettings(), loadClusterSettings(),
                loadTrackSettings(), loadCacheSettings());
    }

    /**
     * Loads the user's map view related preference settings from the preference file.
     *
     * @return a {@code MapViewSettings} object
     */
    public MapViewSettings loadMapViewSettings() {
        return loadManager.loadMapViewSettings();
    }

    /**
     * Loads the photo settings from the preference file.
     *
     * @return a {@code PhotoSettings}
     */
    public PhotoSettings loadPhotoSettings() {
        return loadManager.loadPhotoSettings();
    }

    public ClusterSettings loadClusterSettings() {
        return loadManager.loadClusterSettings();
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
            saveManager.saveClusterSettings(preferenceSettings.getClusterSettings());
            saveManager.saveTrackSettings(preferenceSettings.getTrackSettings());
            saveManager.saveCacheSettings(preferenceSettings.getCacheSettings());
        }
    }

    /**
     * Loads the KartaView layer appearance status from the preference file.
     *
     * @return a boolean value
     */
    public boolean loadKartaViewLayerOpenedFlag() {
        return loadManager.loadKartaViewLayerOpenedFlag();
    }

    /**
     * Saves the KartaView layer appearance status to the preference file.
     *
     * @param isLayerOpened represents the layer showing/hiding status
     */
    public void saveKartaViewLayerOpenedFlag(final boolean isLayerOpened) {
        saveManager.saveKartaViewLayerOpenedFlag(isLayerOpened);
    }

    /**
     * Loads the Edge layer appearance status from the preference file.
     *
     * @return a boolean value representing the status of the layer
     */
    public boolean loadEdgeLayerOpenedFlag() {
        return loadManager.loadEdgeLayerOpenedFlag();
    }

    /**
     * Saves the Edge layer appearance status to the preference file.
     *
     * @param isLayerOpened represents the layer showing/hiding status
     */
    public void saveEdgeLayerOpenedFlag(final boolean isLayerOpened) {
        saveManager.saveEdgeLayerOpenedFlag(isLayerOpened);
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
     * Loads the edge detection panel appearance status flag from the preference file.
     *
     * @return a boolean a boolean value
     */
    public boolean loadEdgeDetectionPanelOpenedFlag() {
        return loadManager.loadEdgeDetectionPanelOpenedFlag();
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
     * Saves the edge detection panel appearance status flag to the preference file.
     *
     * @param value true/false in string format
     */
    public void saveEdgeDetectionPanelOpenedFlag(final String value) {
        final Boolean panelOpened = Boolean.parseBoolean(value);
        saveManager.saveEdgeDetectionPanelOpenedFlag(panelOpened);
    }

    /**
     * Verifies if the KartaView layer data download related preference settings has changed or not.
     *
     * @param key a {@code String} represents the key associated with the authentication preference change event
     * @param newValue a {@code String} represents the new value associated with the authentication preference change
     * event
     * @return true if the data download preference settings has been changed; false otherwise
     */
    public boolean kartaViewLayerDataDownloadPreferencesChanged(final String key, final String newValue) {
        return isFiltersChangedKey(key, newValue, FILTER_CHANGED) || isMapViewZoomKey(key)
                || isKartaViewLayerOpenedFlag(key, newValue) || isDataDownloadedFlag(key);
    }

    /**
     * Verifies if the Edge layer data download related preference settings has changed or not.
     *
     * @param key a {@code String} represents the key associated with the authentication preference change event
     * @param newValue a {@code String} represents the new value associated with the authentication preference change
     * event
     * @return true if the data download preference settings has been changed; false otherwise
     */
    public boolean edgeLayerDataDownloadPreferencesChanged(final String key, final String newValue) {
        return isFiltersChangedKey(key, newValue, EDGE_FILTER_CHANGED) || isMapViewZoomKey(key)
                || isEdgeLayerOpenedFlag(key, newValue) || isDataDownloadedFlag(key);
    }

    private boolean isFiltersChangedKey(final String value, final String newValue, final String filterKey) {
        return filterKey.equals(value) && Boolean.TRUE.toString().equals(newValue);
    }

    private boolean isMapViewZoomKey(final String value) {
        return MAP_VIEW_PHOTO_ZOOM.equals(value);
    }

    private boolean isKartaViewLayerOpenedFlag(final String key, final String newValue) {
        return KARTAVIEW_LAYER_OPENED.equals(key) && Boolean.TRUE.toString().equals(newValue);
    }

    private boolean isEdgeLayerOpenedFlag(final String key, final String newValue) {
        return EDGE_LAYER_OPENED.equals(key) && Boolean.TRUE.toString().equals(newValue);
    }

    private boolean isDataDownloadedFlag(final String key) {
        return MAP_VIEW_DATA_LOAD.equals(key);
    }

    public boolean isDisplayDetectionLocationFlag(final String key) {
        return DISPLAY_DETECTION_LOCATIONS.equals(key);
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
     * Verifies if the photo format flag from preference settings has been changed or not.
     *
     * @param key a {@code String} represents the key associated with the photo format.
     * @return true if the flag has been selected, false otherwise
     */
    public boolean hasPhotoFormatFlagChanged(final String key) {
        return DISPLAY_FRONT_FACING_FLAG.equals(key);
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
     * Verifies if the access token flag has been changed or not
     *
     * @param key Represents the key associated with the access token
     * @return boolean value
     */
    public boolean hasAccessTokenFlagChanged(final String key) {
        return ACCESS_TOKEN.equals(key);
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
     * Verifies if the given key represents the edge detection panel visibility icon key.
     *
     * @param key a {@code String} value
     * @return a boolean value
     */
    public boolean isEdgeDetectionPanelIconVisibilityKey(final String key) {
        return EDGE_DETECTION_PANEL_ICON_VISIBILITY.equals(key);
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