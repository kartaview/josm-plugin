/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;

import java.util.Arrays;
import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.SignType;
import com.telenav.josm.common.entity.EntityUtil;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class DetectionFilter {

    public static final DetectionFilter DEFAULT =
            new DetectionFilter(Arrays.asList(OsmComparison.NEW, OsmComparison.SAME), null, null, null);

    private final List<OsmComparison> osmComparisons;
    private final List<EditStatus> editStatuses;
    private final List<SignType> signTypes;
    private final List<DetectionMode> modes;


    public DetectionFilter(final List<OsmComparison> osmComparisons, final List<EditStatus> editStatuses,
            final List<SignType> signTypes, final List<DetectionMode> modes) {
        this.osmComparisons = osmComparisons;
        this.editStatuses = editStatuses;
        this.signTypes = signTypes;
        this.modes = modes;
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

    public List<DetectionMode> getModes() {
        return modes;
    }

    public boolean containsEditStatus(final EditStatus editStatus) {
        return editStatuses != null && editStatuses.contains(editStatus);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(osmComparisons);
        result = prime * result + EntityUtil.hashCode(editStatuses);
        result = prime * result + EntityUtil.hashCode(signTypes);
        result = prime * result + EntityUtil.hashCode(modes);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final DetectionFilter other = (DetectionFilter) obj;
            result = EntityUtil.bothNullOrEqual(osmComparisons, other.getOsmComparisons());
            result = result && EntityUtil.bothNullOrEqual(editStatuses, other.getEditStatuses());
            result = result && EntityUtil.bothNullOrEqual(signTypes, other.getSignTypes());
            result = result && EntityUtil.bothNullOrEqual(modes, other.getModes());
        }
        return result;
    }
}