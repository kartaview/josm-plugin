/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import java.util.Date;
import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.SignType;
import com.telenav.josm.common.entity.EntityUtil;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class SearchFilter {

    private Date date;
    private final boolean onlyMineFlag;

    private PhotoDataTypeFilter photoType;
    private List<OsmComparison> osmComparisons;
    private List<EditStatus> editStatuses;
    private List<SignType> signTypes;


    public SearchFilter(final boolean onlyMineFlag) {
        this.onlyMineFlag = onlyMineFlag;
    }

    public SearchFilter(final Date date, final boolean onlyMineFlag) {
        this(onlyMineFlag);
        this.date = date;
    }

    public SearchFilter(final Date date, final boolean onlyMineFlag, final PhotoDataTypeFilter photoType,
            final List<OsmComparison> osmComparisons, final List<EditStatus> editStatuses,
            final List<SignType> signTypes) {
        this(date, onlyMineFlag);
        this.photoType = photoType;
        this.osmComparisons = osmComparisons;
        this.editStatuses = editStatuses;
        this.signTypes = signTypes;
    }


    public PhotoDataTypeFilter getPhotoType() {
        return photoType;
    }

    public List<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    public List<EditStatus> getEditStatuses() {
        return editStatuses;
    }

    public List<SignType> getSignTypes() {
        return signTypes;
    }

    public Date getDate() {
        return date;
    }

    public boolean isOnlyMineFlag() {
        return onlyMineFlag;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(date);
        result = prime * result + EntityUtil.hashCode(onlyMineFlag);
        result = prime * result + EntityUtil.hashCode(photoType);
        result = prime * result + EntityUtil.hashCode(osmComparisons);
        result = prime * result + EntityUtil.hashCode(editStatuses);
        result = prime * result + EntityUtil.hashCode(signTypes);
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
            result = result && (onlyMineFlag == other.isOnlyMineFlag());
            result = result && EntityUtil.bothNullOrEqual(photoType, other.getPhotoType());
            result = result && EntityUtil.bothNullOrEqual(osmComparisons, other.getOsmComparisons());
            result = result && EntityUtil.bothNullOrEqual(editStatuses, other.getEditStatuses());
            result = result && EntityUtil.bothNullOrEqual(signTypes, other.getSignTypes());
        }
        return result;
    }
}