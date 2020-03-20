package org.openstreetmap.josm.plugins.openstreetcam.argument;

import com.grab.josm.common.argument.BoundingBox;


/**
 * Object containing filters for the searchDetections method.
 *
 * @author nicoleta.viregan
 */
public class SearchDetectionsAreaFilter {

    private final BoundingBox boundingBox;
    private final SearchDetectionsFilterBuilder searchDetectionsFilter;

    public SearchDetectionsAreaFilter(final BoundingBox boundingBox,
            final SearchDetectionsFilterBuilder searchDetectionsFilter) {
        this.boundingBox = boundingBox;
        this.searchDetectionsFilter = searchDetectionsFilter;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public SearchDetectionsFilterBuilder getSearchDetectionsFilter() {
        return searchDetectionsFilter;
    }
}