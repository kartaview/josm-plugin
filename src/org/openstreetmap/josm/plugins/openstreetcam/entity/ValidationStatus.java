package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;


/**
 * Defines the detection validation status entity.
 *
 * @author ioanao
 * @version $Revision$
 */
public enum ValidationStatus {

    CHANGED {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionChangedValidationStatusText();
        }
    },

    CONFIRMED {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionConfirmedValidationStatusText();
        }
    },

    REMOVED {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionRemovedValidationStatusText();
        }
    },

    TO_BE_CHECKED {

        @Override
        public String toString() {
            return GuiConfig.getInstance().getDetectionToBeCheckedValidationStatusText();
        }
    };
}