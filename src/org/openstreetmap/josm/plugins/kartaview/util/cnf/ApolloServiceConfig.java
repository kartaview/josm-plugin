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
public final class ApolloServiceConfig extends BaseConfig {

    private static final String CONFIG_FILE = "kartaview_apollo_service.properties";
    private static final ApolloServiceConfig INSTANCE = new ApolloServiceConfig();
    private final int retrieveSequenceDetectionsMaxItems;
    private final int searchDetectionsMaxItems;
    private final int searchClustersMaxItems;
    private final int searchEdgeDetectionsMaxItems;
    private final int searchEdgeClustersMaxItems;

    private final String serviceUrl;

    private ApolloServiceConfig() {
        super(CONFIG_FILE);
        serviceUrl = readProperty("service.url");
        retrieveSequenceDetectionsMaxItems = readIntegerProperty("retrieveSequenceDetections.maxItems");
        searchDetectionsMaxItems = readIntegerProperty("searchDetections.maxItems");
        searchClustersMaxItems = readIntegerProperty("searchClusters.maxItems");
        searchEdgeDetectionsMaxItems = readIntegerProperty("searchEdgeDetections.maxItems");
        searchEdgeClustersMaxItems = readIntegerProperty("searchEdgeClusters.maxItems");
    }

    public static ApolloServiceConfig getInstance() {
        return INSTANCE;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public int getRetrieveSequenceDetectionsMaxItems() {
        return retrieveSequenceDetectionsMaxItems;
    }

    public int getSearchDetectionsMaxItems() {
        return searchDetectionsMaxItems;
    }

    public int getSearchClustersMaxItems() {
        return searchClustersMaxItems;
    }

    public int getSearchEdgeDetectionsMaxItems() {
        return searchEdgeDetectionsMaxItems;
    }

    public int getSearchEdgeClustersMaxItems() {
        return searchEdgeClustersMaxItems;
    }
}