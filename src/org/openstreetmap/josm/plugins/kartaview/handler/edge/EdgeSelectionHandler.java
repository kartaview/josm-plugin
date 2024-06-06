/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler.edge;

import java.awt.Point;
import java.util.Objects;

import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.gui.details.edge.detection.EdgeDetectionDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.layer.EdgeLayer;
import org.openstreetmap.josm.plugins.kartaview.handler.BaseMouseSelectionHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.ServiceHandler;
import org.openstreetmap.josm.plugins.kartaview.observer.EdgeDetectionRowSelectionObserver;
import org.openstreetmap.josm.plugins.kartaview.util.Util;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import com.grab.josm.common.thread.ThreadPool;


/**
 * Handles operations associated with Edge data selection.
 *
 * @author nicoleta.viregan
 */
public final class EdgeSelectionHandler extends BaseMouseSelectionHandler implements EdgeDetectionRowSelectionObserver {

    @Override
    public void handleMapSelection(final Point point) {
        if (MainApplication.getMap().mapView.getLayerManager().getActiveLayer().equals(EdgeLayer.getInstance())) {
            final Cluster edgeCluster = DataSet.getInstance().nearbyEdgeCluster(point);
            if (Objects.nonNull(edgeCluster)) {
                ThreadPool.getInstance().execute(() -> handleEdgeClusterSelection(edgeCluster));
            } else {
                final EdgeDetection edgeDetection = DataSet.getInstance().nearbyEdgeDetection(point);
                if (Objects.nonNull(edgeDetection)) {
                    ThreadPool.getInstance().execute(() -> selectEdgeDetection(edgeDetection));
                }
            }
        }
    }

    private void handleEdgeClusterSelection(final Cluster selectedCluster) {
        // retrieve EdgeCluster details
        final Cluster enhancedCluster = ServiceHandler.getInstance().retrieveEdgeCluster(selectedCluster.getId());

        if (Objects.nonNull(enhancedCluster)) {
            DataSet.getInstance().setSelectedEdgeCluster(enhancedCluster);
            EdgeDetectionDetailsDialog.getInstance().updateEdgeClusterDetails(enhancedCluster);

            // move map if cluster is not visible
            if (!MainApplication.getMap().mapView.getRealBounds().contains(enhancedCluster.getPoint())) {
                MainApplication.getMap().mapView.zoomTo(enhancedCluster.getPoint());
            }
        }
        EdgeLayer.getInstance().invalidate();
    }

    private void selectEdgeDetection(final EdgeDetection edgeDetection) {
        if (Objects.nonNull(edgeDetection)) {
            if (Objects.nonNull(DataSet.getInstance().getSelectedEdgeCluster()) && DataSet.getInstance()
                    .edgeDetectionBelongsToSelectedCluster(edgeDetection)) {
                // detections of the selected edge cluster was selected from the map

                EdgeDetectionDetailsDialog.getInstance().selectDetectionFromTable(edgeDetection);
            } else {
                // new detection was selected
                DataSet.getInstance().setSelectedEdgeCluster(null);
                DataSet.getInstance().setSelectedEdgeDetection(edgeDetection);
                EdgeDetectionDetailsDialog.getInstance().updateEdgeDetectionDetails(edgeDetection);
            }

            // move map if detection is not visible
            if (!MainApplication.getMap().mapView.getRealBounds().contains(edgeDetection.getPoint())) {
                MainApplication.getMap().mapView.zoomTo(edgeDetection.getPoint());
            }
        }
        EdgeLayer.getInstance().invalidate();
    }

    @Override
    public void handleMapDataUnselection() {
        DataSet.getInstance().clearEdgeLayerSelection();
        EdgeDetectionDetailsDialog.getInstance().clearDetailsDialog();
        EdgeLayer.getInstance().invalidate();
    }

    @Override
    public boolean selectionAllowed() {
        final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
        return zoom >= PreferenceManager.getInstance().loadMapViewSettings().getPhotoZoom() || (DataSet.getInstance()
                .hasEdgeDetections() || DataSet.getInstance().hasEdgeClusters());
    }

    @Override
    public void selectEdgeDetectionFromTable(final EdgeDetection edgeDetection) {
        if (Objects.nonNull(edgeDetection)) {
            DataSet.getInstance().setSelectedEdgeDetection(edgeDetection);
            EdgeLayer.getInstance().invalidate();
        }
    }
}