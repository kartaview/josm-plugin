package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.data.osm.OsmPrimitiveType;


/**
 * Defines the OSM element type entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum OsmElementType {

    NODE {

        @Override
        public OsmPrimitiveType getOsmPrimitiveType() {
            return OsmPrimitiveType.NODE;
        }

    },
    WAY {

        @Override
        public OsmPrimitiveType getOsmPrimitiveType() {
            return OsmPrimitiveType.WAY;
        }
    },
    RELATION {

        @Override
        public OsmPrimitiveType getOsmPrimitiveType() {
            return OsmPrimitiveType.RELATION;
        }
    },
    WAY_SECTION {

        @Override
        public OsmPrimitiveType getOsmPrimitiveType() {
            return OsmPrimitiveType.WAY;
        }
    };

    public abstract OsmPrimitiveType getOsmPrimitiveType();
}