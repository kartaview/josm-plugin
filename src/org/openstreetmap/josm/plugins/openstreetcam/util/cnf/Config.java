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

import java.util.Date;
import com.telenav.josm.common.cnf.BaseConfig;
import com.telenav.josm.common.util.DateUtil;


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

    private final Date filterMaxDate;
    private final int preferencesMaxZoom;
    private final int mapPhotoZoom;
    private final int mapSegmentZoom;


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


        filterMaxDate = DateUtil.parseDay(readProperty("filter.maxDate"));
        preferencesMaxZoom = readIntegerProperty("preferences.maxZoom", MAX_ZOOM);
        mapPhotoZoom = readIntegerProperty("map.photoZoom", PHOTO_ZOOM);
        mapSegmentZoom = readIntegerProperty("map.segmentZoom", SEGMENT_ZOOM);
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

    public Date getFilterMaxDate() {
        return filterMaxDate;
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
}