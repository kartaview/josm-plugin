/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;

import java.util.List;
import java.util.Optional;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.util.BoundingBoxUtil;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Handles remote selection actions. Currently a {@code Cluster} can be selected remotely.
 *
 * @author beataj
 * @version $Revision$
 */
public class RemoteSelectionHandler {

    private final static String SEPARATOR = ";";
    private final static int LAT_POZ = 0;
    private final static int LON_POZ = 0;
    private final static int FACING_POZ = 0;
    private final static int INTERNAL_NAME_POZ = 0;

    private final SelectionHandler selectionHandler = new SelectionHandler();


    /**
     * Selects the clusters associated with the given identifier.
     *
     * @param identifier a cluster identifier
     */
    public void selectCluster(final String identifier) {
        if (identifier != null && !identifier.isEmpty()) {
            ThreadPool.getInstance().execute(() -> {
                final List<Cluster> clusters =
                        ServiceHandler.getInstance().searchClusters(BoundingBoxUtil.currentBoundingBox(), null);
                if (clusters != null && !clusters.isEmpty()) {
                    Optional<Cluster> selectedCluster =
                            clusters.stream().filter(c -> c.toString().equals(identifier)).findFirst();
                    if (!selectedCluster.isPresent()) {
                        selectedCluster = findCluster(clusters, identifier);
                    }
                    if (selectedCluster.isPresent()) {
                        DataSet.getInstance().setRemoteSelection(true);
                        selectionHandler.handleClusterSelection(selectedCluster.get());
                    } else {
                        DataSet.getInstance().setRemoteSelection(false);
                        selectionHandler.handleDataUnselection();
                    }
                }
            });
        }
    }


    private Optional<Cluster> findCluster(final List<Cluster> clusters, final String identifier) {
        final String[] parts = identifier.split(SEPARATOR);
        final double lat = Double.parseDouble(parts[LAT_POZ]);
        final double lon = Double.parseDouble(parts[LON_POZ]);
        final double facing = Double.parseDouble(parts[FACING_POZ]);
        final LatLon point = new LatLon(lat, lon);
        final String signInternalName = parts[INTERNAL_NAME_POZ];
        final Optional<Cluster> selectedCluster = clusters.stream()
                .filter(ClusterPredicates.hasSameSign(signInternalName)).filter(ClusterPredicates.hasSameFacing(facing))
                .filter(ClusterPredicates.isNearby(point)).findFirst();
        return selectedCluster;
    }
}