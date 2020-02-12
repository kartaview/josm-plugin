/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.cnf;

import com.grab.josm.common.cnf.BaseConfig;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public final class ApolloServiceConfig extends BaseConfig {

    private static final String CONFIG_FILE = "openstreetcam_apollo_service.properties";
    private static final ApolloServiceConfig INSTANCE = new ApolloServiceConfig();

    private final String serviceUrl;

    private ApolloServiceConfig() {
        super(CONFIG_FILE);
        serviceUrl = readProperty("service.url");
    }


    public static ApolloServiceConfig getInstance() {
        return INSTANCE;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }
}