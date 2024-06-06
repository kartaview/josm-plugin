/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service;

import org.openstreetmap.josm.plugins.kartaview.util.cnf.ApolloServiceConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.KartaViewServiceConfig;


/**
 * Defines the attributes of a pagination.
 *
 * @author Beata
 * @version $Revision$
 */
public class Paging {

    /** default paging used for the list/nearby-photos method */
    public static final Paging NEARBY_PHOTOS_DEFAULT =
            new Paging(1, KartaViewServiceConfig.getInstance().getNearbyPhotosMaxItems());

    /** default paging used for the tracks method */
    public static final Paging TRACKS_DEFAULT =
            new Paging(1, KartaViewServiceConfig.getInstance().getTracksMaxItems());

    /** default paging used for the retrieve sequence detections method */
    public static final Paging  RETRIEVE_SEQUENCE_DETECTIONS_DEFAULT =
            new Paging(1, ApolloServiceConfig.getInstance().getRetrieveSequenceDetectionsMaxItems());

    /** default paging used for the search detections method */
    public static final Paging  SEARCH_DETECTIONS_DEFAULT =
            new Paging(1, ApolloServiceConfig.getInstance().getSearchDetectionsMaxItems());

    /** default paging used for the search clusters method */
    public static final Paging SEARCH_CLUSTERS_DEFAULT =
            new Paging(1, ApolloServiceConfig.getInstance().getSearchClustersMaxItems());

    /** default paging used for the search edge detections method */
    public static final Paging  SEARCH_EDGE_DETECTIONS_DEFAULT =
            new Paging(1, ApolloServiceConfig.getInstance().getSearchEdgeDetectionsMaxItems());

    /** default paging used for the search edge clusters method */
    public static final Paging  SEARCH_EDGE_CLUSTERS_DEFAULT =
            new Paging(1, ApolloServiceConfig.getInstance().getSearchEdgeClustersMaxItems());

    private final int page;
    private final int items;


    /**
     * Builds a new paging with the given arguments.
     *
     * @param page the page number to be returned
     * @param itemsPerPage the number of items to be returned
     */
    public Paging(final int page, final int itemsPerPage) {
        this.page = page;
        this.items = itemsPerPage;
    }


    public int getPage() {
        return page;
    }

    public int getItems() {
        return items;
    }
}