package org.openstreetmap.josm.plugins.openstreetcam.entity;

/**
 * Defines the detection edit status entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum EditStatus {

    OPEN, MAPPED, BAD_SIGN, OTHER;

    @Override
    public String toString() {
        return (name().substring(0, 1) + name().substring(1).toLowerCase()).replaceAll("_", " ");
    }
}