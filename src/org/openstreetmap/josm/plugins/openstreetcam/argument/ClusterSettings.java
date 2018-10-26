package org.openstreetmap.josm.plugins.openstreetcam.argument;

/**
 * Defines the user configurable aggregated detections (cluster) settings.
 *
 * @author laurad
 * @version $Revision$
 */
public class ClusterSettings {

    private final boolean displayDetectionLocations;

    /**
     * Builds a new object with the arguments.
     *
     * @param displayDetectionLocations specifies if detection locations should be displayed when selecting an
     * aggregated detection
     */
    public ClusterSettings(final boolean displayDetectionLocations) {
        this.displayDetectionLocations = displayDetectionLocations;
    }

    public boolean isDisplayDetectionLocations() {
        return displayDetectionLocations;
    }
}