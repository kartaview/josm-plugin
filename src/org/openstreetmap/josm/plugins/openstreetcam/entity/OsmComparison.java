package org.openstreetmap.josm.plugins.openstreetcam.entity;

/**
 * Defines the entity representing the comparison between a detection and an OSM entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum OsmComparison {

    SAME, NEW, CHANGED, UNKNOWN, IMPLIED;

    @Override
    public String toString() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }
}