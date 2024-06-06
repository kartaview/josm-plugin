/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.SignFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;


/**
 * Class containing search filtering criteria for search edge detection operation.
 *
 * @author nicoleta.viregan
 */
public class SearchEdgeDetectionsFilter {

    private final TimestampIntervalFilter timestampIntervalFilter;
    private final SignFilter signFilter;


    public SearchEdgeDetectionsFilter(final EdgeSearchFilter filter) {
        Collection<String> includedNames = null;
        String region = null;
        List<String> signTypes = null;
        if (Objects.nonNull(filter)) {
            includedNames = Objects.nonNull(filter.getSpecificSigns()) ? filter.getSpecificSigns().stream().map(
                    Sign::getInternalName).collect(Collectors.toList()) : null;
            region = Objects.nonNull(filter.getRegion()) ? filter.getRegion() : null;
            signTypes = Objects.nonNull(filter.getSignTypes()) ? filter.getSignTypes() : null;
        }
        this.signFilter = Objects.nonNull(includedNames) || Objects.nonNull(region) || Objects.nonNull(signTypes)
                ? new SignFilter(region, signTypes, null, includedNames) : null;
        this.timestampIntervalFilter = null;
    }

    public TimestampIntervalFilter getTimestampIntervalFilter() {
        return timestampIntervalFilter;
    }

    public SignFilter getSignFilter() {
        return signFilter;
    }
}