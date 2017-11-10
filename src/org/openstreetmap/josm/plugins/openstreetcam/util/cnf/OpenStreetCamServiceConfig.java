/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class OpenStreetCamServiceConfig extends BaseConfig {

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
        photoDetailsUrl = serviceBaseUrl + readProperty("service.details");
        userPageUrl = serviceBaseUrl + readProperty("service.userPage");
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