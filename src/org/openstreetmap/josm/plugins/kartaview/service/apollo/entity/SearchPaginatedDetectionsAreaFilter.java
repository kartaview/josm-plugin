package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import org.openstreetmap.josm.plugins.kartaview.service.Paging;


/**
 * Object containing filters for the searchDetections method with paging limitation.
 *
 * @author nicoleta.viregan
 */
public class SearchPaginatedDetectionsAreaFilter extends SearchDetectionsAreaFilter {

    private final Paging paging;

    public SearchPaginatedDetectionsAreaFilter(final SearchDetectionsAreaFilter filter, final Paging paging) {
        super(filter.getBoundingBox(), filter.getSearchDetectionsFilter());
        this.paging = paging;
    }


    public Paging getPaging() {
        return paging;
    }
}