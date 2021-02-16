/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.photo.entity;

import org.openstreetmap.josm.plugins.kartaview.entity.Photo;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class OSV {

    private final Photo photoObject;


    public OSV(final Photo photoObject) {
        this.photoObject = photoObject;
    }


    public Photo getPhotoObject() {
        return photoObject;
    }
}