/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.photo.entity;

import com.google.gson.annotations.SerializedName;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class OSV {

    private final Photo photoObject;

    @SerializedName("access_token")
    private final String accessToken;


    public OSV(final Photo photoObject, final String accessToken) {
        this.photoObject = photoObject;
        this.accessToken = accessToken;
    }


    public Photo getPhotoObject() {
        return photoObject;
    }

    public String getAccessToken() {
        return accessToken;
    }
}