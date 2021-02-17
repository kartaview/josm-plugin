/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.photo;

import org.openstreetmap.josm.plugins.kartaview.util.cnf.KartaViewServiceConfig;


/**
 * Defines the attributes of a pagination.
 *
 * @author Beata
 * @version $Revision$
 */
public class Paging {

    /** default paging used for the list/nearby-photos method */
    public static final Paging NEARBY_PHOTOS_DEAFULT =
            new Paging(1, KartaViewServiceConfig.getInstance().getNearbyPhotosMaxItems());

    /** default paging used for the tracks method */
    public static final Paging TRACKS_DEFAULT =
            new Paging(1, KartaViewServiceConfig.getInstance().getTracksMaxItems());

    private final int page;
    private final int itemsPerPage;


    /**
     * Builds a new paging with the given arguments.
     *
     * @param page the page number to be returned
     * @param itemsPerPage the number of items to be returned
     */
    public Paging(final int page, final int itemsPerPage) {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
    }


    public int getPage() {
        return page;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }
}