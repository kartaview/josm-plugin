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

    /**
     * Builds a new object with the arguments.
     *
     * @param displayDetectionLocations specifies if detection locations should be displayed when selecting an
     * aggregated detection
     */
    public ClusterSettings(final boolean displayDetectionLocations, final boolean displayTags) {
        this.displayDetectionLocations = displayDetectionLocations;
        this.displayTags = displayTags;
    }

    public boolean isDisplayDetectionLocations() {
        return displayDetectionLocations;
    }

    public boolean isDisplayTags() {
        return displayTags;
    }
}