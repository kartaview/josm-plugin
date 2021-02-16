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
import org.openstreetmap.josm.plugins.kartaview.service.apollo.DetectionFilter;
import org.openstreetmap.josm.plugins.kartaview.util.Util;
import com.grab.josm.common.entity.EntityUtil;


/**
 * Defines the filters that the user can use to reduce the displayed results.
 *
 * @author beataj
 * @version $Revision$
 */
public class SearchFilter {

    public static final SearchFilter DEFAULT =
            new SearchFilter(null, false, Arrays.asList(DataType.PHOTO, DataType.CLUSTER), DetectionFilter.DEFAULT);

    private Date date;
    private final boolean olnyUserData;
    private List<DataType> dataTypes;
    private DetectionFilter detectionFilter;


    /**
     * Builds a new filter with the given arguments.
     *
     * @param olnyUserData if true, then only the data contributed by the logged in user is returned
     */
    public SearchFilter(final boolean olnyUserData) {
        this.olnyUserData = olnyUserData;
    }

    /**
     * Builds a new filter with the given arguments.
     *
     * @param date a {@code Date} represents the starting date from which the data is returned
     * @param onlyMineFlag if true, then only the data contributed by the logged in user is returned
     * @param dataTypes a {@code DataType} specifies the type of data to be displayed
     * @param detectionFilter a {@code DetectionFilter} specifies the {@code Detection} related filters
     */
    public SearchFilter(final Date date, final boolean onlyMineFlag, final List<DataType> dataTypes,
            final DetectionFilter detectionFilter) {
        this(onlyMineFlag);
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

    public boolean isOlnyUserData() {
        return olnyUserData;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(date);
        result = prime * result + EntityUtil.hashCode(olnyUserData);
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
            result = result && (olnyUserData == other.isOlnyUserData());
            result = result && EntityUtil.bothNullOrEqual(dataTypes, other.getDataTypes());
            result = result && EntityUtil.bothNullOrEqual(detectionFilter, other.getDetectionFilter());
        }
        return result;
    }

    /**
     * Returns the OSM user identifier of the currently logged in user. If the user is not logged in the method return
     * null.
     *
     * @return a {@code Long}
     */
    public Long getOsmUserId() {
        return isOlnyUserData() ? Util.getOsmUserId() : null;
    }
}