/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */

package org.openstreetmap.josm.plugins.openstreetcam.argument;

/**
 * Defines the user configurable aggregated detections (cluster) settings.
 *
 * @author laurad
 * @version $Revision$
 */
public class ClusterSettings {

    private final boolean displayDetectionLocations;
    private final boolean displayTags;
    private final boolean displayColorCoded;

    /**
     * Builds a new object with the arguments.
     *
     * @param displayDetectionLocations specifies if detection locations should be displayed when selecting an
     * aggregated detection
     * @param displayTags specifies if the tags associated with an OSM element are displayed on the map
     * @param displayColorCoded specifies if the clusters are displayed taking into consideration the color scheme
     * associated with the confidence level
     */
    public ClusterSettings(final boolean displayDetectionLocations, final boolean displayTags,
            final boolean displayColorCoded) {
        this.displayDetectionLocations = displayDetectionLocations;
        this.displayTags = displayTags;
        this.displayColorCoded = displayColorCoded;
    }

    public boolean isDisplayDetectionLocations() {
        return displayDetectionLocations;
    }

    public boolean isDisplayTags() {
        return displayTags;
    }

    public boolean isDisplayColorCoded() {
        return displayColorCoded;
    }
}