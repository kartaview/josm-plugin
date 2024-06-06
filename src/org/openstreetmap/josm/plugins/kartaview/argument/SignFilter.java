/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.argument;

import java.util.Arrays;
import java.util.Collection;

import org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants;


/**
 * Common field of the filter for searchDetections and searchClusters method.
 *
 * @author nicoleta.viregan
 */
public class SignFilter {

    public static final Collection<String> EXCLUDED_SIGN_TYPES = Arrays.asList(RequestConstants.BLURRING_TYPE,
            RequestConstants.POI, RequestConstants.PHOTO_QUALITY_TYPE);

    private final String signRegion;
    private final Collection<String> includedSignTypes;
    private final Collection<String> excludedSignTypes;
    private final Collection<String> signInternalNames;
    private Collection<String> signScopes;


    public SignFilter(final String signRegion, final Collection<String> includedSignTypes,
            final Collection<String> excludedSignTypes, final Collection<String> signInternalNames) {
        this.signRegion = signRegion;
        this.includedSignTypes = includedSignTypes;
        this.signInternalNames = signInternalNames;
        this.excludedSignTypes = excludedSignTypes;
    }

    public String getSignRegion() {
        return signRegion;
    }

    public Collection<String> getIncludedSignTypes() {
        return includedSignTypes;
    }

    public Collection<String> getExcludedSignTypes() {
        return excludedSignTypes;
    }

    public Collection<String> getSignInternalNames() {
        return signInternalNames;
    }

    public Collection<String> getSignScopes() {
        return signScopes;
    }
}