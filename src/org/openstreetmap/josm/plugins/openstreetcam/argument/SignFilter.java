package org.openstreetmap.josm.plugins.openstreetcam.argument;

import com.grab.josm.common.http.HttpUtil;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.RequestConstants;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * Common field of the filter for searchDetections and searchClusters method.
 *
 * @author nicoleta.viregan
 */
public class SignFilter {

    private final String region;
    private final Collection<String> includedTypes;
    private final Collection<String> excludedTypes;
    private final Collection<String> internalNames;

    public SignFilter(final String region, final Collection<String> includedTypes,
            final Collection<String> internalNames) {
        this.region = region;
        this.includedTypes = includedTypes;
        this.excludedTypes = new ArrayList(Collections.singleton(HttpUtil.utf8Encode(RequestConstants.BLURRING_TYPE)));
        this.internalNames = internalNames;
    }

    public String getRegion() {
        return region;
    }

    public Collection<String> getIncludedTypes() {
        return includedTypes;
    }

    public Collection<String> getExcludedTypes() {
        return excludedTypes;
    }

    public Collection<String> getInternalNames() {
        return internalNames;
    }
}