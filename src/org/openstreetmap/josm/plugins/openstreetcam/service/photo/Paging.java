/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.photo;

import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.OpenStreetCamServiceConfig;


/**
 * Defines the attributes of a pagination.
 *
 * @author Beata
 * @version $Revision$
 */
public class Paging {

    /** default paging used for the list/nearby-photos method */
    public static final Paging NEARBY_PHOTOS_DEAFULT =
            new Paging(1, OpenStreetCamServiceConfig.getInstance().getNearbyPhotosMaxItems());

    /** default paging used for the tracks method */
    public static final Paging TRACKS_DEFAULT =
            new Paging(1, OpenStreetCamServiceConfig.getInstance().getTracksMaxItems());

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