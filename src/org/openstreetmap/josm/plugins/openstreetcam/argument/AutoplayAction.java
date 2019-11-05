/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;


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