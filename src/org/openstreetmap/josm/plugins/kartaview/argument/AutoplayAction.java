/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.argument;


/**
 * Specifies the actions supported by the auto-play feature.
 *
 * @author beataj
 * @version $Revision$
 */
public enum AutoplayAction {

    /** starts track auto-play */
    START,

    /** stops track auto-play */
    STOP;


    public static AutoplayAction getAutoplayAction(final String value) {
        return START.name().equals(value) ? START : STOP.name().equals(value) ? STOP : null;
    }
}