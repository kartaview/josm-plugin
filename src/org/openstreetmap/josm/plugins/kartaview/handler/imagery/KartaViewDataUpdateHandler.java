/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler.imagery;

import java.util.List;
import java.util.Objects;

import javax.swing.SwingUtilities;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.SearchFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.HighZoomResultSet;
import org.openstreetmap.josm.plugins.kartaview.entity.Segment;
import org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.detection.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.layer.KartaViewLayer;
import org.openstreetmap.josm.plugins.kartaview.handler.ServiceHandler;
import org.openstreetmap.josm.plugins.kartaview.util.BoundingBoxUtil;
import org.openstreetmap.josm.plugins.kartaview.util.Util;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import com.grab.josm.common.argument.BoundingBox;


/**
 * Handles map view data download and update operations.
 *
 * @author beataj
 * @version $Revision$
 */
public class KartaViewDataUpdateHandler {

    /**
     * Updates the current map view with new data. The data type displayed depends on the current zoom level. Segments
     * that have KartaView coverage are displayed in the following cases:
     * <ul>
     * <li>current zoom level grater or equals to the minimum map data zoom (~10) and current zoom level less than the
     * default photo location zoom</li> Photo locations and/or detections are displayed in the following cases:
     * <ul>
     * <li>current zoom level grater or equals to the default photo zoom level</li>
     * <li>a track is displayed (we displayed photo and detection locations near the a track)</li>
     * </ul>
     * A previously selected photo/detection location should be removed if the user changed the data filters and
     * according the new filters the selection should not be displayed
     *
     * @param checkSelection flag indicating if the previously selected elements should be checked in the new data set
     */
    public void updateData(final boolean checkSelection) {
        if (Objects.nonNull(MainApplication.getMap())) {
            final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
            final List<KartaViewLayer> displayedLayers = MainApplication.getLayerManager().getLayersOfType(
                    KartaViewLayer.class);
            if (!displayedLayers.isEmpty() && zoom >= Config.getInstance().getMapSegmentZoom()) {
                final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();

                if (DataSet.getInstance().hasSelectedSequence()) {
                    // special case, we load always photos
                    updateHighZoomLevelData(mapViewSettings, checkSelection);
                } else {
                    // change data type if zoom >= mapViewSettings.photoZoom
                    normalFlow(mapViewSettings, zoom, checkSelection);
                }
            } else {
                KartaViewLayer.getInstance().enableFilterAction(false);
            }
        }
    }

    private void normalFlow(final MapViewSettings mapViewSettings, final int zoom, final boolean checkSelection) {
        if (zoom < mapViewSettings.getPhotoZoom() && !DataSet.getInstance().hasActiveSelection()) {
            KartaViewLayer.getInstance().enableFilterAction(false);
            updateLowZoomLevelData(mapViewSettings, zoom);
        } else if (zoom >= mapViewSettings.getPhotoZoom()) {
            KartaViewLayer.getInstance().enableFilterAction(true);
            updateHighZoomLevelData(mapViewSettings, checkSelection);
        }
    }

    private void updateLowZoomLevelData(final MapViewSettings mapViewSettings, final int zoom) {
        // clear previous data type
        if (DataSet.getInstance().hasPhotos()) {
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().clearKartaViewLayerData(false);
                PhotoDetailsDialog.getInstance().updateUI(null, null, false);
                DetectionDetailsDialog.getInstance().updateDetectionDetails(null);
                KartaViewLayer.getInstance().enablePhotoDataSetDownloadActions();
                KartaViewLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }

        final List<BoundingBox> areas = BoundingBoxUtil.currentBoundingBoxes(mapViewSettings.isDataLoadFlag());
        if (!areas.isEmpty()) {
            final SearchFilter searchFilter = PreferenceManager.getInstance().loadSearchFilter();
            final List<Segment> segments = ServiceHandler.getInstance().listMatchedTracks(areas, searchFilter, zoom);
            if (MainApplication.getMap() != null && MainApplication.getMap().mapView != null) {
                SwingUtilities.invokeLater(() -> {
                    DataSet.getInstance().updateLowZoomLevelData(segments);
                    KartaViewLayer.getInstance().invalidate();
                    MainApplication.getMap().repaint();
                });
            }
        }
    }

    private void updateHighZoomLevelData(final MapViewSettings mapViewSettings, final boolean checkSelection) {
        // clear previous data type
        if (DataSet.getInstance().hasSegments()) {
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().clearKartaViewLayerData(false);
                KartaViewLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }

        final SearchFilter searchFilter = PreferenceManager.getInstance().loadSearchFilter();
        final List<BoundingBox> areas = BoundingBoxUtil.currentBoundingBoxes(mapViewSettings.isDataLoadFlag());
        final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
        if (!areas.isEmpty() && zoom >= mapViewSettings.getPhotoZoom()) {
            final HighZoomResultSet resultSet = ServiceHandler.getInstance().searchHighZoomImageryData(areas,
                    searchFilter);

            // download sequence detections for only visible area
            final Bounds visibleArea = BoundingBoxUtil.currentBounds();
            if (DataSet.getInstance().hasSelectedSequence() && !DataSet.getInstance()
                    .selectedSequenceHasDetectionsForBounds(visibleArea)) {
                final List<Detection> sequenceDetections = ServiceHandler.getInstance().retrieveSequenceDetections(
                        DataSet.getInstance().getSelectedSequence().getId(), BoundingBoxUtil.boundsToBoundingBox(
                                visibleArea));
                DataSet.getInstance().updateSelectedSequenceDetections(sequenceDetections, visibleArea);
            }
            updateUI(resultSet, checkSelection);
        }
    }

    private void updateUI(final HighZoomResultSet resultSet, final boolean checkSelection) {
        final boolean isClusterInfoInPanel = DataSet.getInstance().getSelectedCluster() != null;
        if (MainApplication.getMap() != null && MainApplication.getMap().mapView != null) {
            GuiHelper.runInEDT(() -> {
                DataSet.getInstance().updateHighZoomLevelClusterData(resultSet.getClusters(), checkSelection);
                DataSet.getInstance().updateHighZoomLevelDetectionData(resultSet.getDetections(), checkSelection);
                DataSet.getInstance().updateHighZoomLevelPhotoData(resultSet.getPhotoDataSet());
                updateSelection(checkSelection, isClusterInfoInPanel);
                if (DataSet.getInstance().hasNearbyPhotos() && !PreferenceManager.getInstance()
                        .loadAutoplayStartedFlag()) {
                    PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
                }
                if (!DataSet.getInstance().hasSelectedCluster() && !DataSet.getInstance().hasSelectedDetection()) {
                    DetectionDetailsDialog.getInstance().clearDetailsDialog();
                }
                if (!DataSet.getInstance().hasSelectedPhoto()) {
                    PhotoDetailsDialog.getInstance().updateUI(null, null, false);
                }
                KartaViewLayer.getInstance().enablePhotoDataSetDownloadActions();
                KartaViewLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }

    private void updateSelection(final boolean checkSelection, final boolean isClusterInfoInPanel) {
        final SearchFilter searchFilter = PreferenceManager.getInstance().loadSearchFilter();
        if (!DataSet.getInstance().hasSelectedPhoto() && PhotoDetailsDialog.getInstance().isPhotoSelected()) {
            DetectionDetailsDialog.getInstance().changeClusterDetailsDialog(isClusterInfoInPanel);
            if (DataSet.getInstance().getSelectedDetection() != null && !Util.isDetectionMatchingFilters(searchFilter,
                    DataSet.getInstance().getSelectedDetection())) {
                PhotoDetailsDialog.getInstance().updateUI(null, null, false);
                if (!Util.isDetectionMatchingFilters(searchFilter, DataSet.getInstance().getSelectedDetection())) {
                    DetectionDetailsDialog.getInstance().updateDetectionDetails(null);
                }
            }
        } else {
            if (checkSelection) {
                if (isClusterInfoInPanel) {
                    if (!DataSet.getInstance().hasSelectedCluster() && !DataSet.getInstance().hasSelectedDetection()) {
                        DetectionDetailsDialog.getInstance().updateDetectionDetails(DataSet.getInstance()
                                .getSelectedDetection());
                        DetectionDetailsDialog.getInstance().updateClusterDetails(DataSet.getInstance()
                                .getSelectedCluster(), DataSet.getInstance().getSelectedDetection());
                    } else if (!DataSet.getInstance().hasSelectedCluster()) {
                        updateDetectionPanelAccordingToFilters();
                        DetectionDetailsDialog.getInstance().clearClusterPanel();
                    }
                } else {
                    if (DataSet.getInstance().getSelectedDetection() != null && DataSet.getInstance()
                            .getSelectedCluster() == null) {
                        updateDetectionPanelAccordingToFilters();
                        if (!DataSet.getInstance().hasSelectedDetection()) {
                            PhotoDetailsDialog.getInstance().updateUI(null, null, false);
                        }
                    } else {
                        DetectionDetailsDialog.getInstance().changeClusterDetailsDialog(false);
                    }
                }
            }
        }
    }

    private void updateDetectionPanelAccordingToFilters() {
        final SearchFilter searchFilter = PreferenceManager.getInstance().loadSearchFilter();
        if (!Util.isDetectionMatchingFilters(searchFilter, DataSet.getInstance().getSelectedDetection())) {
            DataSet.getInstance().setSelectedDetection(null);
            DetectionDetailsDialog.getInstance().updateDetectionDetails(null);
        } else {
            DetectionDetailsDialog.getInstance().updateDetectionDetails(DataSet.getInstance().getSelectedDetection());
        }
    }
}