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
 * Loads service related information.
 *
 * @author Beata
 * @version $Revision$
 */
public final class ServiceConfig extends BaseConfig {

    private static final int DEFAULT_SEGMENT_ZOOM = 10;
    private static final int DEFAULT_PHOTO_ZOOM = 17;
    private static final int MAX_ZOOM = 21;
    private static final int MAX_RADIUS = 5000;
    private static final int MIN_RADIUS = 1;
    private static final int MAX_ITEMS = 5000;
    private static final String CONFIG_FILE = "openstreetcam_service.properties";
    private static final ServiceConfig INSTANCE = new ServiceConfig();

    private final String baseUrl;
    private final String serviceUrl;
    private final String photoDetailsUrl;
    private final String feedbackUrl;
    private final int segmentZoom;
    private final int maxZoom;
    private final int photoZoom;
    private final int minRadius;
    private final int maxRadius;
    private final int maxItems;

    private ServiceConfig() {
        super(CONFIG_FILE);

        baseUrl = readProperty("service.url");
        serviceUrl = baseUrl + readProperty("service.version");
        photoDetailsUrl = baseUrl + readProperty("service.details");
        feedbackUrl = readProperty("feedback.url");
        segmentZoom = readIntegerProperty("segmentZoom", DEFAULT_SEGMENT_ZOOM);
        maxZoom = readIntegerProperty("maxZoom", MAX_ZOOM);
        photoZoom = readIntegerProperty("photoZoom", DEFAULT_PHOTO_ZOOM);
        minRadius = readIntegerProperty("minRadius", MIN_RADIUS);
        maxRadius = readIntegerProperty("maxRadius", MAX_RADIUS);
        maxItems = readIntegerProperty("maxItems", MAX_ITEMS);
    }


    public static ServiceConfig getInstance() {
        return INSTANCE;
    }


    public String getBaseUrl() {
        return baseUrl;
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

    public int getSegmentZoom() {
        return segmentZoom;
    }

    public int getPhotoZoom() {
        return photoZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }


    public int getMinRadius() {
        return minRadius;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public int getMaxItems() {
        return maxItems;
    }
}