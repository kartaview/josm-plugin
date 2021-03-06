/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils.StructEntry;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmComparison;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class OsmComparisonEntry {

    // preference entities must be declared public, otherwise JOSM preference loading does not work!
    @StructEntry
    private String name;

    public OsmComparisonEntry() {}


    public OsmComparisonEntry(final OsmComparison osmComparison) {
        this.name = osmComparison.name();
    }

    public String getName() {
        return name;
    }
}