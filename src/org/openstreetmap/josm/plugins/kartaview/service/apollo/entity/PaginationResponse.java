/*
 * Copyright 2022 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import com.grab.josm.common.entity.Status;


/**
 * Class containing the structure of a paginated response.
 *
 * @author nicoleta.viregan
 */
public class PaginationResponse extends Response {

    private Metadata metadata;

    public PaginationResponse(final Status status) {
        super(status);
    }

    public Metadata getMetadata() {
        return metadata;
    }
}