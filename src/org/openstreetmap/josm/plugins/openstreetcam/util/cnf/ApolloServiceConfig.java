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
public class ApolloServiceConfig extends BaseConfig {

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