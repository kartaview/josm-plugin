/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 */
package org.openstreetmap.josm.plugins.kartaview.handler.edge;

import java.util.List;

import java.util.Objects;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDataResultSet;
import org.openstreetmap.josm.plugins.kartaview.gui.details.edge.detection.EdgeDetectionDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.layer.EdgeLayer;
import org.openstreetmap.josm.plugins.kartaview.handler.ServiceHandler;
import org.openstreetmap.josm.plugins.kartaview.util.BoundingBoxUtil;
import org.openstreetmap.josm.plugins.kartaview.util.Util;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import com.grab.josm.common.argument.BoundingBox;


/**
 * Data update handler for the edge layer.
 *
 * @author beata.tautan
 * @version $Revision$
 */
public class EdgeDataUpdateHandler {

    /**
     * Updates the current map view with new EDGE data.
     */
    public void updateData(final boolean checkSelection) {
        if (Objects.nonNull(MainApplication.getMap())) {
            final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
            final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
            final List<BoundingBox> areas = BoundingBoxUtil.currentBoundingBoxes(mapViewSettings.isDataLoadFlag());
            if (!areas.isEmpty()) {
                final List<EdgeLayer> displayedLayers =
                        MainApplication.getLayerManager().getLayersOfType(EdgeLayer.class);
                if (!displayedLayers.isEmpty() && zoom >= mapViewSettings.getPhotoZoom()) {
                    final EdgeDataResultSet resultSet =
                            ServiceHandler.getInstance().searchEdgeData(areas, PreferenceManager
                                    .getInstance().loadEdgeSearchFilter());
                    updateUI(resultSet, checkSelection);
                }
            }
        }
    }

    private void updateUI(final EdgeDataResultSet resultSet, final boolean checkSelection) {
        if (Objects.nonNull(MainApplication.getMap()) && Objects.nonNull(MainApplication.getMap().mapView)) {
            GuiHelper.runInEDT(() -> {
                DataSet.getInstance().updateHighZoomLevelEdgeDetectionData(resultSet.getEdgeDetections());
                DataSet.getInstance().updateHighZoomLevelEdgeClusterData(resultSet.getEdgeClusters());
                if (checkSelection) {
                    DataSet.getInstance().updateEdgeLayerSelection();
                    if (!DataSet.getInstance().hasSelectedEdgeCluster() && !DataSet.getInstance()
                            .hasSelectedEdgeDetection()) {
                        EdgeDetectionDetailsDialog.getInstance().clearDetailsDialog();
                    }
                }
                EdgeLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }
}