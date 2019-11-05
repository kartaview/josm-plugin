/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Loads various run-time properties.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Config extends BaseConfig {

    private static final String CONFIG_FILE = "openstreetcam.properties";
    private static final Config INSTANCE = new Config();

    private static final int MAX_CLOSEST_ITEMS = 10;
    private static final int SEGMENT_ZOOM = 10;
    private static final int PHOTO_ZOOM = 15;
    private static final int MAX_ZOOM = 18;
    private static final int MOUSE_HOVER_MIN_DELAY = 500;
    private static final int MOUSE_HOVER_MAX_DELAY = 30000;
    private static final int AUTOPLAY_MIN_DELAY = 200;
    private static final int AUTOPLAY_MAX_DELAY = 2000;
    private static final double FACING_THRESHOLD = 30.0;
    private static final double DISTANCE_THRESHOLD = 30.0;


    private final int closestPhotosMaxItems;
    private final int tracksMaxZoom;
    private final int preferencesMaxZoom;
    private final int mapPhotoZoom;
    private final int mapSegmentZoom;
    private final int mouseHoverMinDelay;
    private final int mouseHoverMaxDelay;
    private final int autoplayMinDelay;
    private final int autoplayMaxDelay;
    private final double clusterFacingThreshold;
    private final double clusterDistanceThreshold;


    private Config() {
        super(CONFIG_FILE);

        closestPhotosMaxItems = readIntegerProperty("closestPhotos.maxNumber", MAX_CLOSEST_ITEMS);
        tracksMaxZoom = readIntegerProperty("tracks.maxZoom", MAX_ZOOM);
        preferencesMaxZoom = readIntegerProperty("preferences.maxZoom", MAX_ZOOM);
        mapPhotoZoom = readIntegerProperty("map.photoZoom", PHOTO_ZOOM);
        mapSegmentZoom = readIntegerProperty("map.segmentZoom", SEGMENT_ZOOM);
        mouseHoverMinDelay = readIntegerProperty("mouseHover.minDelay", MOUSE_HOVER_MIN_DELAY);
        mouseHoverMaxDelay = readIntegerProperty("mouseHover.maxDelay", MOUSE_HOVER_MAX_DELAY);
        autoplayMinDelay = readIntegerProperty("autoplay.minDelay", AUTOPLAY_MIN_DELAY);
        autoplayMaxDelay = readIntegerProperty("autoplay.maxDelay", AUTOPLAY_MAX_DELAY);

        clusterFacingThreshold = readDoubleProperty("cluster.facing.threshold", FACING_THRESHOLD);
        clusterDistanceThreshold = readDoubleProperty("cluster.distance.threshold", DISTANCE_THRESHOLD);
    }


    public static Config getInstance() {
        return INSTANCE;
    }


    public int getTracksMaxZoom() {
        return tracksMaxZoom;
    }

    public int getPreferencesMaxZoom() {
        return preferencesMaxZoom;
    }

    public int getMapPhotoZoom() {
        return mapPhotoZoom;
    }

    public int getMapSegmentZoom() {
        return mapSegmentZoom;
    }

    public int getClosestPhotosMaxItems() {
        return closestPhotosMaxItems;
    }

    public int getMouseHoverMinDelay() {
        return mouseHoverMinDelay;
    }

    public int getMouseHoverMaxDelay() {
        return mouseHoverMaxDelay;
    }

    public int getAutoplayMinDelay() {
        return autoplayMinDelay;
    }

    public int getAutoplayMaxDelay() {
        return autoplayMaxDelay;
    }

    public double getClusterFacingThreshold() {
        return clusterFacingThreshold;
    }

    public double getClusterDistanceThreshold() {
        return clusterDistanceThreshold;
    }
}