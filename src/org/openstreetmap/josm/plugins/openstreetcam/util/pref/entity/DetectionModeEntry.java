/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
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