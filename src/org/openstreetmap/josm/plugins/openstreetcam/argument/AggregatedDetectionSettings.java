package org.openstreetmap.josm.plugins.openstreetcam.argument;

/**
 * Defines the user configurable aggregated detections settings.
 *
 * @author laurad
 * @version $Revision$
 */
public class AggregatedDetectionSettings {

    private final boolean displayImageLocations;
    private final boolean displayDetectionLocations;

    /**
     * Builds a new object with the arguments.
     *
     * @param displayImageLocations     specifies if image locations should be displayed when selecting an aggregated detection
     * @param displayDetectionLocations specifies if detection locations should be displayed when selecting an aggregated detection
     */
    public AggregatedDetectionSettings(final boolean displayImageLocations, final boolean displayDetectionLocations) {
        this.displayImageLocations = displayImageLocations;
        this.displayDetectionLocations = displayDetectionLocations;
    }

    public boolean isDisplayImageLocations() {
        return displayImageLocations;
    }

    public boolean isDisplayDetectionLocations() {
        return displayDetectionLocations;
    }
}
