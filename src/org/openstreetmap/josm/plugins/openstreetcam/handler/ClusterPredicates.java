/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;

import java.util.function.Predicate;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;


/**
 *
 * @author beataj
 * @version $Revision$
 */
final class ClusterPredicates {

    private static final double ANGLE_360 = 360;

    private ClusterPredicates() {}

    static Predicate<Cluster> hasSameSign(final String signInternalName) {
        return cluster -> cluster.getSign().getInternalName().equals(signInternalName);
    }

    static Predicate<Cluster> hasSameFacing(final Double facing) {
        return cluster -> normalizedAngleDifference(cluster.getFacing(), facing) <= Config.getInstance()
                .getClusterFacingThreshold();
    }

    private static double normalizedAngleDifference(final double facing1, final double facing2) {
        final double diff1 = facing1 - facing2;
        final double diff2 = facing2 - facing1;
        final double diff = Math.min(diff1 < 0 ? diff1 + ANGLE_360 : diff1, (diff2) < 0 ? diff2 + ANGLE_360 : diff2);
        return diff;
    }

    static Predicate<Cluster> isNearby(final LatLon point) {
        return cluster -> cluster.getPoint().greatCircleDistance(point) <= Config.getInstance()
                .getClusterDistanceThreshold();
    }
}