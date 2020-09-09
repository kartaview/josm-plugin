package org.openstreetmap.josm.plugins.openstreetcam.argument;

import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.RequestConstants;

import java.util.Arrays;
import java.util.Collection;


/**
 * Common field of the filter for searchDetections and searchClusters method.
 *
 * @author nicoleta.viregan
 */
public class SignFilter {

    private final String signRegion;
    private final Collection<String> includedSignTypes;
    private final Collection<String> excludedSignTypes =
            Arrays.asList(RequestConstants.BLURRING_TYPE, RequestConstants.POI);
    private final Collection<String> signInternalNames;

    public SignFilter(final String signRegion, final Collection<String> includedSignTypes,
            final Collection<String> signInternalNames) {
        this.signRegion = signRegion;
        this.includedSignTypes = includedSignTypes;
        this.signInternalNames = signInternalNames;
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
}