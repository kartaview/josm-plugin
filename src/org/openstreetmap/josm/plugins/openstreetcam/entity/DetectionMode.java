package org.openstreetmap.josm.plugins.openstreetcam.entity;

/**
 * Defines the detection mode entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum DetectionMode {

    AUTOMATIC, MANUAL;

    @Override
    public String toString() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }
}