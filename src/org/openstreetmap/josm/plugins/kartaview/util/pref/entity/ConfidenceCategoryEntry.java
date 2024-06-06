/*
 * Copyright 2022 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils.StructEntry;
import org.openstreetmap.josm.plugins.kartaview.entity.ConfidenceLevelCategory;

/**
 * An entity containing preferences regarding the confidence category.
 * 
 * @author adina.misaras
 * @version $Revision$
 */
public class ConfidenceCategoryEntry {

	@StructEntry
    private String name;

    public ConfidenceCategoryEntry() {}


    public ConfidenceCategoryEntry(final ConfidenceLevelCategory confidenceCategory) {
        this.name = confidenceCategory.name();
    }

    public String getName() {
        return name;
    }
}