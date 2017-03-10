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
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;


/**
 * Defines the attributes of a pagination.
 *
 * @author Beata
 * @version $Revision$
 */
public class Paging {

    public static final Paging DEFAULT = new Paging(1, Config.getInstance().getMaxItems());

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