/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

/**
 * Defines the user configurable preference settings attributes. The plugin's preference settings can be modified by the
 * users via JOSM OSC reference settings.
 *
 * @author beataj
 * @version $Revision$
 */
public class PreferenceSettings {

    private final MapViewSettings mapViewSettings;
    private final PhotoSettings photoSettings;
    private final ClusterSettings clusterSettings;
    private final SequenceSettings trackSettings;
    private final CacheSettings cacheSettings;


    /**
     * Builds a new object with the given arguments.
     *
     * @param mapViewSettings specifies the map view user configurable settings
     * @param photoSettings specifies the photo loading user configurable settings
     * @param clusterSettings specifies the selected cluster user configurable settings
     * @param trackSettings specifies the track display user configurable settings
     * @param cacheSettings specifies the cache user configurable settings
     */
    public PreferenceSettings(final MapViewSettings mapViewSettings, final PhotoSettings photoSettings,
            final ClusterSettings clusterSettings, final SequenceSettings trackSettings,
            final CacheSettings cacheSettings) {
        this.mapViewSettings = mapViewSettings;
        this.photoSettings = photoSettings;
        this.clusterSettings = clusterSettings;
        this.trackSettings = trackSettings;
        this.cacheSettings = cacheSettings;
    }


    public MapViewSettings getMapViewSettings() {
        return mapViewSettings;
    }

    public PhotoSettings getPhotoSettings() {
        return photoSettings;
    }

    public ClusterSettings getClusterSettings() {
        return clusterSettings;
    }

    public SequenceSettings getTrackSettings() {
        return trackSettings;
    }

    public CacheSettings getCacheSettings() {
        return cacheSettings;
    }
}