package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;

/**
 * Defines the entity representing the comparison between a detection and an OSM entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum OsmComparison {

    SAME {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionSameOnOsmText();
        }
    },

    NEW {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionNewOnOsmText();
        }
    },

    CHANGED {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionChangedOnOsmText();
        }
    },

    UNKNOWN {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionUnknownOnOsmText();
        }
    }
}