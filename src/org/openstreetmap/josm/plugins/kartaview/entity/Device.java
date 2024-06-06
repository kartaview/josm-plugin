/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

/**
 * POJO class for the entity storing device information.
 *
 * @author nicoleta.viregan
 */
public class Device {

    private final String kartaIotId;
    private final String name;
    private final String version;


    public Device(final String kartaIotId, final String name, final String version) {
        this.kartaIotId = kartaIotId;
        this.name = name;
        this.version = version;
    }

    public String getKartaIotId() {
        return kartaIotId;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return kartaIotId + ", " + name + ", " + version;
    }
}