/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

/**
 * POJO class containing information related to algorithm.
 *
 * @author nicoleta.viregan
 */
public class Algorithm {

    private String name;
    private Long creationTimestamp;
    private String version;
    private String description;

    public String getName() {
        return name;
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + ", " + version;
    }
}