/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import java.util.Objects;

import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.service.Paging;

import com.grab.josm.common.argument.BoundingBox;


/**
 * Class containing all the mandatory and optional filtering criteria for the search cluster operation.
 *
 * @author maria.mitisor
 */
public class SearchPaginatedEdgeClustersAreaFilter {

    private final BoundingBox boundingBox;
    private final SearchEdgeClustersFilter searchClustersFilter;
    private final Paging paging;


    public SearchPaginatedEdgeClustersAreaFilter(BoundingBox boundingBox, EdgeSearchFilter filter, Paging paging) {
        this.boundingBox = boundingBox;
        this.searchClustersFilter =
                Objects.nonNull(filter) && filter.hasClusterFilters() ? new SearchEdgeClustersFilter(filter) : null;
        this.paging = paging;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public SearchEdgeClustersFilter getSearchEdgeClustersFilter() {
        return searchClustersFilter;
    }

    public Paging getPaging() {
        return paging;
    }
}