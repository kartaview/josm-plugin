/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.entity;

import java.util.List;


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