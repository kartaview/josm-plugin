/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public enum SignType {
    INTERSECTION_LANE_CONTROL,
    JUGHEAD,
    LANE_CONTROL,
    ONE_WAY,
    OTHER,
    ROAD_CLOSED,
    ROUNDABOUT,
    SIGN_POST,
    SPEED_LIMIT,
    STOP,
    TRAFFIC_SIGNAL,
    TURN_RESTRICTION;

    @Override
    public String toString() {
        return (name().substring(0, 1) + name().substring(1).toLowerCase()).replaceAll("_", " ");
    }
}