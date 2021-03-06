/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

/**
 * Defines the detection mode entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum DetectionMode {

    AUTOMATIC, MANUAL;

    @Override
    public String toString() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }
}