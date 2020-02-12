/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.photo.entity;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.BaseResponse;
import com.grab.josm.common.entity.Status;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class SequencePhotoListResponse extends BaseResponse {

    // setters are not required,since GSON sets the fields directly using reflection.
    private final Sequence osv;

    public SequencePhotoListResponse(final Status status, final Sequence osv) {
        super(status);
        this.osv = osv;
    }

    public Sequence getOsv() {
        return osv;
    }
}