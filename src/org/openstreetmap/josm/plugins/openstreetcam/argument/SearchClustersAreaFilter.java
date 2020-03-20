package org.openstreetmap.josm.plugins.openstreetcam.argument;

        import com.grab.josm.common.argument.BoundingBox;


/**
 * Object containing filters for the searchDetections method.
 *
 * @author nicoleta.viregan
 */
public class SearchClustersAreaFilter {

    private final BoundingBox boundingBox;
    private final SearchClustersFilterBuilder searchClustersFilterBuilder;

    public SearchClustersAreaFilter(final BoundingBox boundingBox,
            final SearchClustersFilterBuilder searchClustersFilterBuilder) {
        this.boundingBox = boundingBox;
        this.searchClustersFilterBuilder = searchClustersFilterBuilder;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public SearchClustersFilterBuilder getSearchClustersFilterBuilder() {
        return searchClustersFilterBuilder;
    }
}