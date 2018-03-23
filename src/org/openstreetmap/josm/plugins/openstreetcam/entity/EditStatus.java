package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;


/**
 * Defines the detection edit status entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum EditStatus {

    OPEN, FIXED {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionEditStatusFixedText();
        }
    },

    ALREADY_FIXED {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionEditStatusAlreadyFixedText();
        }
    },
    BAD_SIGN,
    OTHER;

    @Override
    public String toString() {
        return (name().substring(0, 1) + name().substring(1).toLowerCase()).replaceAll("_", " ");
    }
}