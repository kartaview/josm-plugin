/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils.StructEntry;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;


/**
 * An entity containing preferences regarding the detection mode.
 *
 * @author ioanao
 * @version $Revision$
 */
public class DetectionModeEntry {

    @StructEntry
    private String name;

    public DetectionModeEntry() {}


    public DetectionModeEntry(final DetectionMode mode) {
        this.name = mode.name();
    }

    public String getName() {
        return name;
    }
}