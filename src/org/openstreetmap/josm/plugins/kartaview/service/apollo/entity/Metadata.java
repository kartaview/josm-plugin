/*
 * Copyright 2022 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

/**
 * Pojo class containing information related to pagination.
 *
 * @author nicoleta.viregan
 */
public class Metadata {
    private final Integer page;
    private final Integer itemsPerPage;
    private final Integer totalElements;

    public Metadata(final Integer page, final Integer itemsPerPage, final Integer totalElements) {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
        this.totalElements = totalElements;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public Integer getTotalElements() {
        return totalElements;
    }
}
