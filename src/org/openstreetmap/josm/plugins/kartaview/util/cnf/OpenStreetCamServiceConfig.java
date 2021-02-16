/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.cnf;

import com.grab.josm.common.cnf.BaseConfig;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public final class OpenStreetCamServiceConfig extends BaseConfig {

    private static final String CONFIG_FILE = "openstreetcam_service.properties";
    private static final OpenStreetCamServiceConfig INSTANCE = new OpenStreetCamServiceConfig();

    private static final int MAX_ITEMS = 5000;

    private final String serviceBaseUrl;
    private final String serviceUrl;
    private final String photoDetailsUrl;
    private final String userPageUrl;
    private final String feedbackUrl;
    private final int nearbyPhotosMaxItems;
    private final int tracksMaxItems;


    private OpenStreetCamServiceConfig() {
        super(CONFIG_FILE);

        serviceBaseUrl = readProperty("service.url");
        serviceUrl = serviceBaseUrl + readProperty("service.version");
        photoDetailsUrl = readProperty("service.details.url");
        userPageUrl = readProperty("service.user.url");
        feedbackUrl = readProperty("feedback.url");
        nearbyPhotosMaxItems = readIntegerProperty("nearbyPhotos.maxItems", MAX_ITEMS);
        tracksMaxItems = readIntegerProperty("tracks.maxItems", MAX_ITEMS);
    }


    public static OpenStreetCamServiceConfig getInstance() {
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

    public String getUserPageUrl() {
        return userPageUrl;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public int getNearbyPhotosMaxItems() {
        return nearbyPhotosMaxItems;
    }

    public int getTracksMaxItems() {
        return tracksMaxItems;
    }
}