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
 *
 * @author Beata
 * @version $Revision$
 */
public class ServiceConfig extends BaseConfig {

    private static final int DEFAULT_PHOTO_ZOOM = 15;
    private static final String CONFIG_FILE = "openstreetview_service.properties";
    private static final ServiceConfig INSTANCE = new ServiceConfig();

    private final String serviceUrl;
    private int photoZoom;


    public ServiceConfig() {
        super(CONFIG_FILE);

        serviceUrl = readProperty("service.url");

        final String photoZoomValue = readProperty("photoZoom");
        try {
            photoZoom = Integer.parseInt(photoZoomValue);
        } catch (final NumberFormatException e) {
            photoZoom = DEFAULT_PHOTO_ZOOM;
        }
    }


    public static ServiceConfig getInstance() {
        return INSTANCE;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public int getPhotoZoom() {
        return photoZoom;
    }
}