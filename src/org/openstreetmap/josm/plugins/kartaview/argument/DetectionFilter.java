/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.argument;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openstreetmap.josm.plugins.kartaview.entity.ConfidenceLevelCategory;
import org.openstreetmap.josm.plugins.kartaview.entity.DetectionMode;
import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmComparison;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;

import com.grab.josm.common.entity.EntityUtil;


/**
 * Defines the detection filters displayed in the filter panels for the layers.
 *
 * @author beataj
 * @version $Revision$
 */
public class DetectionFilter {

    public static final DetectionFilter DEFAULT =
            new DetectionFilter(null, Arrays.asList(EditStatus.OPEN), null, null, null, "", null);

    private final List<OsmComparison> osmComparisons;
    private final List<EditStatus> editStatuses;
    private final List<String> signTypes;
    private final List<Sign> specificSigns;
    private final List<DetectionMode> modes;
    private final String region;
    private final List<ConfidenceLevelCategory> confidenceCategories;


    public DetectionFilter(final List<OsmComparison> osmComparisons, final List<EditStatus> editStatuses,
            final List<String> signTypes, final List<Sign> specificSigns, final List<DetectionMode> modes,
            final String region, final List<ConfidenceLevelCategory> confidenceCategories) {
        this.osmComparisons = osmComparisons;
        this.editStatuses = editStatuses;
        this.signTypes = signTypes;
        this.specificSigns = specificSigns;
        this.modes = modes;
        this.region = region;
        this.confidenceCategories = confidenceCategories;
    }


    public List<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    public List<EditStatus> getEditStatuses() {
        return editStatuses;
    }

    public List<String> getSignTypes() {
        return signTypes;
    }

    public List<Sign> getSpecificSigns() {
        return specificSigns;
    }

    List<String> getSignInternalNames() {
        return specificSigns != null
                ? specificSigns.stream().map(Sign::getInternalName).distinct().collect(Collectors.toList()) : null;
    }

    public List<DetectionMode> getModes() {
        return modes;
    }

    public String getRegion() {
        return region;
    }

    public List<ConfidenceLevelCategory> getConfidenceCategories() {
        return confidenceCategories;
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
        result = prime * result + EntityUtil.hashCode(specificSigns);
        result = prime * result + EntityUtil.hashCode(region);
        result = prime * result + EntityUtil.hashCode(modes);
        result = prime * result + EntityUtil.hashCode(confidenceCategories);
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
            result = result && EntityUtil.bothNullOrEqual(specificSigns, other.getSpecificSigns());
            result = result && EntityUtil.bothNullOrEqual(region, other.getRegion());
            result = result && EntityUtil.bothNullOrEqual(modes, other.getModes());
            result = result && EntityUtil.bothNullOrEqual(confidenceCategories, other.getConfidenceCategories());
        }
        return result;
    }
}