/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.entity;

import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity.Response;
import com.telenav.josm.common.entity.Status;


/**
 * Defines a general response that is returned by the list methods.
 *
 * @author Beata
 * @version $Revision$
 * @param <T> specifies the current page items type
 */
public class ListResponse<T> extends Response {

    private final List<Integer> totalFilteredItems;
    private final List<T> currentPageItems;


    public ListResponse(final Status status, final List<Integer> totalFilteredItems, final List<T> currentPageItems) {
        super(status);
        this.totalFilteredItems = totalFilteredItems;
        this.currentPageItems = currentPageItems;
    }

    public List<T> getCurrentPageItems() {
        return currentPageItems;
    }

    public List<Integer> getTotalFilteredItems() {
        return totalFilteredItems;
    }

    public int getTotalItems() {
        return totalFilteredItems != null ? totalFilteredItems.get(0) : 0;
    }
}