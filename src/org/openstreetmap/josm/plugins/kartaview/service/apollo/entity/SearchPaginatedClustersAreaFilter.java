/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import org.openstreetmap.josm.plugins.kartaview.service.Paging;


/**
 * Object containing filters for the searchClusters method with paging limitation.
 *
 * @author beata.tautan
 * @version $Revision$
 */
public class SearchPaginatedClustersAreaFilter extends SearchClustersAreaFilter {

    private final Paging paging;

    public SearchPaginatedClustersAreaFilter(final SearchClustersAreaFilter filter, final Paging paging) {
        super(filter.getBoundingBox(), filter.getSearchClustersFilter());
        this.paging = paging;
    }

    public Paging getPaging() {
        return paging;
    }
}