/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

/**
 * @author nicoleta.viregan
 */
public class Language {

    private final String name;
    private final String isoCode2;
    private final String isoCode3;

    public Language(final String name, final String isoCode2, final String isoCode3) {
        this.name = name;
        this.isoCode2 = isoCode2;
        this.isoCode3 = isoCode3;
    }

    public String getName() {
        return name;
    }

    public String getIsoCode2() {
        return isoCode2;
    }

    public String getIsoCode3() {
        return isoCode3;
    }
}