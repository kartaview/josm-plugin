/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
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
    private final Long fromId;
    private final Long toId;
    private final OsmElementType type;
    private final String tag;
    private final List<OsmElement> members;


    public OsmElement(final Long osmId, final OsmElementType type, final List<OsmElement> members, final Long fromId,
            final Long toId, final String tag) {
        this.osmId = osmId;
        this.type = type;
        this.members = members;
        this.fromId = fromId;
        this.toId = toId;
        this.tag = tag;
    }


    public Long getOsmId() {
        return osmId;
    }

    public OsmElementType getType() {
        return type;
    }

    public List<OsmElement> getMembers() {
        return members;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getToId() {
        return toId;
    }

    public String getTag() {
        return tag;
    }

    public PrimitiveId getPrimitiveId() {
        return osmId != null ? new SimplePrimitiveId(osmId, type.getOsmPrimitiveType()) : null;
    }
}