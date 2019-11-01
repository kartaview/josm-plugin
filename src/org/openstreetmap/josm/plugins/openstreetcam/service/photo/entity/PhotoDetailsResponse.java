/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.photo.entity;

import org.openstreetmap.josm.plugins.openstreetcam.service.entity.BaseResponse;
import com.telenav.josm.common.entity.Status;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoDetailsResponse extends BaseResponse {

    private final OSV osv;


    public PhotoDetailsResponse(final Status status, final OSV osv) {
        super(status);
        this.osv = osv;
    }


    public OSV getOsv() {
        return osv;
    }
}