/*
 *  Copyright 2017 Telenav, Inc.
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
package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoDataSet {

    private final List<Photo> photos;
    private Integer page;
    private Integer totalItems;


    public PhotoDataSet() {
        this.photos = new ArrayList<>();
    }

    public PhotoDataSet(final List<Photo> photos) {
        this.photos = photos;
    }


    public PhotoDataSet(final List<Photo> photos, final Integer page, final Integer totalItems) {
        this.photos = photos;
        this.page = page;
        this.totalItems = totalItems;
    }

    public List<Photo> getPhotos() {
        return photos;
    }


    public Integer getPage() {
        return page;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public boolean hasPreviousItems() {
        return page != null && page > 1;
    }

    public boolean hasNextItems() {
        return page != null && totalItems > page * Config.getInstance().getNearbyPhotosMaxItems();
    }
}