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
 * Class containing all the mandatory and optional filtering criteria for search edge detection operation.
 *
 * @author nicoleta.viregan
 */
public class SearchPaginatedEdgeDetectionsAreaFilter {

    private final BoundingBox boundingBox;
    private final SearchEdgeDetectionsFilter searchEdgeDetectionsFilter;
    private final Paging paging;

    public SearchPaginatedEdgeDetectionsAreaFilter(BoundingBox boundingBox, EdgeSearchFilter filter, Paging paging) {
        this.boundingBox = boundingBox;
        this.searchEdgeDetectionsFilter = Objects.nonNull(filter) && filter.hasDetectionFilters()
                ? new SearchEdgeDetectionsFilter(filter) : null;
        this.paging = paging;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public SearchEdgeDetectionsFilter getSearchEdgeDetectionsFilter() {
        return searchEdgeDetectionsFilter;
    }

    public Paging getPaging() {
        return paging;
    }
}