/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils.StructEntry;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;

/**
 *
 * @author beataj
 * @version $Revision$
 */
public class EditStatusEntry {

    // preference entities must be declared public, otherwise JOSM preference loading does not work!
    @StructEntry
    private String name;

    public EditStatusEntry() {}


    public EditStatusEntry(final EditStatus editStatus) {
        this.name = editStatus.name();
    }

    public String getName() {
        return name;
    }
}