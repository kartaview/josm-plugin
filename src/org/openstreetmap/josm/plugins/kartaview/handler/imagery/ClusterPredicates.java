/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler.imagery;

import java.util.function.Predicate;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import org.openstreetmap.josm.data.coor.ILatLon;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;


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
        return cluster -> cluster.getPoint().greatCircleDistance((ILatLon) point) <= Config.getInstance()
                .getClusterDistanceThreshold();
    }
}