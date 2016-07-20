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
package org.openstreetmap.josm.plugins.openstreetview.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Loads service related information.
 *
 * @author Beata
 * @version $Revision$
 */
public final class ServiceConfig extends BaseConfig {

    private static final int DEFAULT_PHOTO_ZOOM = 15;
    private static final int MAX_RADIUS = 5000;
    private static final int MIN_RADIUS = 1;
    private static final int MAX_ITEMS = 5000;
    private static final String CONFIG_FILE = "openstreetview_service.properties";
    private static final ServiceConfig INSTANCE = new ServiceConfig();

    private final String baseUrl;
    private final String serviceUrl;
    private final String photoDetailsUrl;
    private final String feedbackUrl;
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
        photoZoom = readInt("photoZoom", DEFAULT_PHOTO_ZOOM);
        minRadius = readInt("minRadius", MIN_RADIUS);
        maxRadius = readInt("maxRadius", MAX_RADIUS);
        maxItems = readInt("maxItems", MAX_ITEMS);
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

    public int getPhotoZoom() {
        return photoZoom;
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

    private int readInt(final String key, final int defaultValue) {
        final String valueStr = readProperty(key);
        int value;
        try {
            value = Integer.parseInt(valueStr);
        } catch (final NumberFormatException e) {
            value = defaultValue;
        }
        return value;
    }
}