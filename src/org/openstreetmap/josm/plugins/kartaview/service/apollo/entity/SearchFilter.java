/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import java.util.Collection;
import java.util.Date;
import org.openstreetmap.josm.plugins.kartaview.argument.SignFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmComparison;
import com.grab.josm.common.formatter.DateFormatter;


/**
 * Class containing the common filters used by searchDetections and searchClusters operations.
 *
 * @author beata.tautan
 * @version $Revision$
 */
public class SearchFilter {

    private String date;
    private Collection<OsmComparison> osmComparisons;
    private SignFilter signFilter;


    public SearchFilter(final Date date) {
        this.date = DateFormatter.formatDay(date);
        this.date = this.date.isEmpty() ? null: this.date;
    }

    public Collection<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    public void setOsmComparisons(final Collection<OsmComparison> osmComparisons) {
        this.osmComparisons = osmComparisons;
    }

    public SignFilter getSignFilter() {
        return signFilter;
    }

    public void setSignFilter(final SignFilter signFilter) {
        this.signFilter = signFilter;
    }

    public String getDate() {
        return date;
    }
}