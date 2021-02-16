/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;


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
    }
}