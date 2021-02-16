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
public class CharacterSet {

    private final String name;

    public CharacterSet(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}