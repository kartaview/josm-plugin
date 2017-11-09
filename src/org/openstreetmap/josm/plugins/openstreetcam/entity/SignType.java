/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public enum SignType {
    ONE_WAY, OTHER, TURN_RESTRICTION, SPEED_LIMIT, SIGN_POST;

    @Override
    public String toString() {
        return (name().substring(0, 1) + name().substring(1).toLowerCase()).replaceAll("_", " ");
    }
}