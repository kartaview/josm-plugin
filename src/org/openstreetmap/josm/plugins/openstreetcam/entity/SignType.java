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
    SPEED_LIMIT_ADDITIONAL,
    SPEED_LIMIT_AHEAD,
    SPEED_LIMIT_AREA,
    SPEED_LIMIT_BEGIN,
    SPEED_LIMIT_CITY,
    SPEED_LIMIT_END,
    SPEED_LIMIT_MINIMUM,
    SPEED_LIMIT_NIGHT,
    SPEED_LIMIT_SCHOOL_AHEAD,
    SPEED_LIMIT_STATE,
    SPEED_LIMIT_TOWN,
    SPEED_LIMIT_TRUCKS,
    SPEED_LIMIT_VILLAGE,
    SPEED_LIMIT_ZONE_AHEAD,
    STOP,
    TRAFFIC_SIGNAL,
    TURN_LANE,
    TURN_LANE_ADDITIONAL,
    TURN_RESTRICTION;


    @Override
    public String toString() {
        return (name().substring(0, 1) + name().substring(1).toLowerCase()).replaceAll("_", " ");
    }
}