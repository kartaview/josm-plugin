package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 * Defines the point entity which represents a gps position.
 *
 * @author ioanao
 * @version $Revision$
 */
public class Point {

    private final Double lat;
    private final Double lon;

    public Point(final Double lat, final Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }
}