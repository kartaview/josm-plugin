/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.photo.entity;

import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.BaseResponse;
import com.grab.josm.common.entity.Status;


/**
 * Defines a general response that is returned by the list methods.
 *
 * @author Beata
 * @version $Revision$
 * @param <T> specifies the current page items type
 */
public class ListResponse<T> extends BaseResponse {

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