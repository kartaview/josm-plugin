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
package org.openstreetmap.josm.plugins.openstreetcam.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Loads various run-time properties.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Config extends BaseConfig {

    private static final int MAX_RADIUS = 5000;
    private static final int MIN_RADIUS = 1;
    private static final int MAX_ITEMS = 5000;
    private static final int MAX_CLOSEST_ITEMS = 10;
    private static final int SEGMENT_ZOOM = 10;
    private static final int PHOTO_ZOOM = 15;
    private static final int MAX_ZOOM = 18;
    private static final int MOUSE_HOVER_MIN_DELAY = 500;
    private static final int MOUSE_HOVER_MAX_DELAY = 30000;

    private static final int AUTOPLAY_MIN_DELAY = 200;
    private static final int AUTOPLAY_MAX_DELAY = 2000;

    private static final String CONFIG_FILE = "openstreetcam.properties";
    private static final Config INSTANCE = new Config();


    private final String serviceBaseUrl;
    private final String serviceUrl;
    private final String photoDetailsUrl;
    private final String feedbackUrl;

    private final int nearbyPhotosMaxRadius;
    private final int nearbyPhotosMinRadius;
    private final int nearbyPhotosMaxItems;


    private final int closestPhotosMaxItems;

    private final int tracksMaxItems;
    private final int tracksMaxZoom;

    private final int preferencesMaxZoom;
    private final int mapPhotoZoom;
    private final int mapSegmentZoom;

    private final int mouseHoverMinDelay;
    private final int mouseHoverMaxDelay;

    private final int autoplayMinDelay;
    private final int autoplayMaxDelay;

    private Config() {
        super(CONFIG_FILE);

        serviceBaseUrl = readProperty("service.url");
        serviceUrl = serviceBaseUrl + readProperty("service.version");
        photoDetailsUrl = serviceBaseUrl + readProperty("service.details");
        feedbackUrl = readProperty("feedback.url");

        nearbyPhotosMaxRadius = readIntegerProperty("nearbyPhotos.maxRadius", MAX_RADIUS);
        nearbyPhotosMinRadius = readIntegerProperty("nearbyPhotos.minRadius", MIN_RADIUS);
        nearbyPhotosMaxItems = readIntegerProperty("nearbyPhotos.maxItems", MAX_ITEMS);

        closestPhotosMaxItems = readIntegerProperty("closestPhotos.maxNumber", MAX_CLOSEST_ITEMS);

        tracksMaxItems = readIntegerProperty("tracks.maxItems", MAX_ITEMS);
        tracksMaxZoom = readIntegerProperty("tracks.maxZoom", MAX_ZOOM);


        preferencesMaxZoom = readIntegerProperty("preferences.maxZoom", MAX_ZOOM);
        mapPhotoZoom = readIntegerProperty("map.photoZoom", PHOTO_ZOOM);
        mapSegmentZoom = readIntegerProperty("map.segmentZoom", SEGMENT_ZOOM);

        mouseHoverMinDelay = readIntegerProperty("mouseHover.minDelay", MOUSE_HOVER_MIN_DELAY);
        mouseHoverMaxDelay = readIntegerProperty("mouseHover.maxDelay", MOUSE_HOVER_MAX_DELAY);

        autoplayMinDelay = readIntegerProperty("autoplay.minDelay", AUTOPLAY_MIN_DELAY);
        autoplayMaxDelay = readIntegerProperty("autoplay.maxDelay", AUTOPLAY_MAX_DELAY);
    }


    public static Config getInstance() {
        return INSTANCE;
    }

    public String getServiceBaseUrl() {
        return serviceBaseUrl;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getPhotoDetailsUrl() {
        return photoDetailsUrl;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public int getNearbyPhotosMaxRadius() {
        return nearbyPhotosMaxRadius;
    }

    public int getNearbyPhotosMinRadius() {
        return nearbyPhotosMinRadius;
    }

    public int getNearbyPhotosMaxItems() {
        return nearbyPhotosMaxItems;
    }

    public int getTracksMaxItems() {
        return tracksMaxItems;
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
}