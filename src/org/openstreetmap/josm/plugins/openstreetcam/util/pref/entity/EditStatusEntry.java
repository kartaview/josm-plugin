/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
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