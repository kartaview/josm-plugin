/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.argument;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.grab.josm.common.entity.EntityUtil;


/**
 * Defines the filters that the user can use to reduce the displayed results.
 *
 * @author beataj
 * @version $Revision$
 */
public class SearchFilter {

    public static final SearchFilter DEFAULT =
            new SearchFilter(null, Arrays.asList(DataType.PHOTO, DataType.CLUSTER), DetectionFilter.DEFAULT);

    private final Date date;
    private final List<DataType> dataTypes;
    private final DetectionFilter detectionFilter;

    /**
     * Builds a new filter with the given arguments.
     *
     * @param date a {@code Date} represents the starting date from which the data is returned
     * @param dataTypes a {@code DataType} specifies the type of data to be displayed
     * @param detectionFilter a {@code DetectionFilter} specifies the {@code Detection} related filters
     */

    public SearchFilter(final Date date, final List<DataType> dataTypes,
            final DetectionFilter detectionFilter) {
        this.date = date;
        this.dataTypes = dataTypes;
        this.detectionFilter = detectionFilter;
    }

    public List<DataType> getDataTypes() {
        return dataTypes;
    }

    public DetectionFilter getDetectionFilter() {
        return detectionFilter;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(date);
        result = prime * result + EntityUtil.hashCode(dataTypes);
        result = prime * result + EntityUtil.hashCode(detectionFilter.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final SearchFilter other = (SearchFilter) obj;
            result = EntityUtil.bothNullOrEqual(date, other.getDate());
            result = result && EntityUtil.bothNullOrEqual(dataTypes, other.getDataTypes());
            result = result && EntityUtil.bothNullOrEqual(detectionFilter, other.getDetectionFilter());
        }
        return result;
    }
}