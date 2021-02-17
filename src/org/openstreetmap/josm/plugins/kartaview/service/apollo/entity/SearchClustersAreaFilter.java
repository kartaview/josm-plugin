package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import com.grab.josm.common.argument.BoundingBox;


/**
 * Object containing filters for the searchDetections method.
 *
 * @author nicoleta.viregan
 */
public class SearchClustersAreaFilter {

    private final BoundingBox boundingBox;
    private final SearchClustersFilter
            searchClustersFilter;

    public SearchClustersAreaFilter(final BoundingBox boundingBox, final SearchClustersFilter searchClustersFilter) {
        this.boundingBox = boundingBox;
        this.searchClustersFilter = searchClustersFilter;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public SearchClustersFilter getSearchClustersFilter() {
        return searchClustersFilter;
    }
}