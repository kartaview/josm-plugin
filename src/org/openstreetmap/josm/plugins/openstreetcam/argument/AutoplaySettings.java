/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class AutoplaySettings {

    private final Integer length;
    private final Integer delay;


    public AutoplaySettings(final Integer length, final Integer delay) {
        this.length = length;
        this.delay = delay;
    }

    public Integer getLength() {
        return length;
    }

    public Integer getDelay() {
        return delay;
    }
}