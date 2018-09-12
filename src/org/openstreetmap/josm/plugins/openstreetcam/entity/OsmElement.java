package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.List;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.data.osm.SimplePrimitiveId;


/**
 * Defines the OSM element entity, representing the corresponding OSM element to which a detection was matched.
 *
 * @author ioanao
 * @version $Revision$
 */
public class OsmElement {

    private final Long osmId;
    private final OsmElementType type;
    private final List<Long> members;


    public OsmElement(final Long osmId, final OsmElementType type, final List<Long> members) {
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

    public List<Long> getMembers() {
        return members;
    }

    public PrimitiveId getPrimitiveId() {
        return osmId != null ? new SimplePrimitiveId(osmId, type.getOsmPrimitiveType()) : null;
    }
}