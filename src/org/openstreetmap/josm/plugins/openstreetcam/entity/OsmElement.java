package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 * Defines the OSM element entity, representing the corresponding OSM element to which a detection was matched.
 *
 * @author ioanao
 * @version $Revision$
 */
public class OsmElement {

    private final Long osmId;
    private final OsmElementType type;
    private final String members;


    public OsmElement(final Long osmId, final OsmElementType type, final String members) {
        this.osmId = osmId;
        this.type = type;
        this.members = members;
    }


    public Long getOsmId() {
        return osmId;
    }

    public OsmElementType getType() {
        return type;
    }

    public String getMembers() {
        return members;
    }
}