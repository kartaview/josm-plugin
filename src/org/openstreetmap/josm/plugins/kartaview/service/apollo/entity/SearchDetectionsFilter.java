/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import org.openstreetmap.josm.plugins.kartaview.argument.DetectionFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.SignFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.DetectionMode;
import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.entity.ValidationStatus;


/**
 * Class containing the filter specific for the searchDetections method.
 *
 * @author nicoleta.viregan
 */

public class SearchDetectionsFilter extends SearchFilter {

    private Collection<EditStatus> editStatuses;
    private Collection<ValidationStatus> validationStatuses;
    private Collection<DetectionMode> detectionModes;


    public SearchDetectionsFilter(final Date date, final DetectionFilter detectionFilter) {
        super(date);
        if (Objects.nonNull(detectionFilter)) {
            this.editStatuses = detectionFilter.getEditStatuses();
            this.detectionModes = detectionFilter.getModes();
            setOsmComparisons(detectionFilter.getOsmComparisons());
            Collection<String> includedNames = null;
            if (detectionFilter.getSpecificSigns() != null) {
                includedNames = detectionFilter.getSpecificSigns().stream().map(Sign::getInternalName).collect(
                        Collectors.toList());
            }
            setSignFilter(new SignFilter(detectionFilter.getRegion(), detectionFilter.getSignTypes(),
                    SignFilter.EXCLUDED_SIGN_TYPES, includedNames));

        }
    }

    public Collection<EditStatus> getEditStatuses() {
        return editStatuses;
    }

    public Collection<ValidationStatus> getValidationStatuses() {
        return validationStatuses;
    }

    public Collection<DetectionMode> getDetectionModes() {
        return detectionModes;
    }
}