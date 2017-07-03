/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import java.util.Date;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the filters that the user can use to reduce the displayed results.
 *
 * @author Beata
 * @version $Revision$
 */
public class ListFilter {

    /** the default filter */
    public static final ListFilter DEFAULT = new ListFilter(null, false);

    private final Date date;
    private final boolean onlyUserFlag;


    /**
     * Builds a new filter with the given arguments.
     *
     * @param date a {@code Date} the photos that were uploaded after the specified date will be displayed
     * @param onlyUserFlag if true only the current user's photos will be displayed
     */
    public ListFilter(final Date date, final boolean onlyUserFlag) {
        this.date = date;
        this.onlyUserFlag = onlyUserFlag;
    }

    public Date getDate() {
        return date;
    }

    public boolean isOnlyUserFlag() {
        return onlyUserFlag;
    }

    public boolean isDefaultFilter() {
        return DEFAULT.equals(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(date);
        result = prime * result + EntityUtil.hashCode(onlyUserFlag);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final ListFilter other = (ListFilter) obj;
            result = EntityUtil.bothNullOrEqual(date, other.getDate());
            result = result && (onlyUserFlag == other.isOnlyUserFlag());
        }
        return result;
    }
}