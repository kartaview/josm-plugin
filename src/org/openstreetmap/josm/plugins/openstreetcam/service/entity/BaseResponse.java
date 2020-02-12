/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.entity;

import com.grab.josm.common.entity.Status;

/**
 *
 * @author beataj
 * @version $Revision$
 */
public class BaseResponse {

    private final Status status;

    public BaseResponse(final Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}