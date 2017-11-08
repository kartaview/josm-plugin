package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;


/**
 * Defines the detection mode entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum DetectionMode {

    AUTOMATIC {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionAutomaticModeText();
        }
    },

    MANUAL {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionManualModeText();
        }
    }
}