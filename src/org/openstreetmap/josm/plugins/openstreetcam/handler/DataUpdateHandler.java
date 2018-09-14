/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.HighZoomResultSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.util.BoundingBoxUtil;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Handles map view data download and update operations.
 *
 * @author beataj
 * @version $Revision$
 */
public class DataUpdateHandler {

    /**
     * Updates the current map view with new data. The data type displayed depends on the current zoom level. Segments
     * that have OpenStreetCam coverage are displayed in the following cases:
     * <ul>
     * <li>current zoom level>=minimum map data zoom (~10) and current zoom level < default photo location zoom</li>
     * <li>current zoom level>=minimum map data zoom (~10) and user enabled manual data switch and zoom level <=minimum
     * photo location zoom</li>
     * <li>current zoom level>=minimum map data zoom (~10) and user enabled manual data switch and had switched to
     * segment view</li>
     * </ul>
     * Photo locations and/or detections are displayed in the following cases:
     * <ul>
     * <li>current zoom level >= default photo zoom level</li>
     * <li>current zoom level >= default photo zoom level and user had enabled manual data switch and had switched to
     * photo location view</li>
     * <li>a track is displayed (we displayed photo and detection locations near the a track)</li>
     * </ul>
     * A previously selected photo/detection location should be removed if the user changed the data filters and
     * according the new filters the selection should not be displayed
     *
     * @param checkSelection flag indicating if the previously selected elements should be checked in the new data set
     * @param boundingBoxChanged flag indicating if the bounding box have changed or not
     */
    public void updateData(final boolean checkSelection, final boolean boundingBoxChanged) {
        final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
        if (zoom >= Config.getInstance().getMapSegmentZoom()) {
            final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();

            if (DataSet.getInstance().hasSelectedSequence()) {
                // special case, we load always photos
                updateHighZoomLevelData(mapViewSettings, checkSelection, boundingBoxChanged);
            } else {
                if (mapViewSettings.isManualSwitchFlag()) {
                    // change data type only if user changed manually
                    manualSwitchFlow(mapViewSettings, zoom, checkSelection, boundingBoxChanged);
                } else {
                    // change data type if zoom >= mapViewSettings.photoZoom
                    normalFlow(mapViewSettings, zoom, checkSelection, boundingBoxChanged);
                }
            }
        }
    }

    private void manualSwitchFlow(final MapViewSettings mapViewSettings, final int zoom, final boolean checkSelection,
            final boolean boundingBoxChanged) {
        final MapViewType dataType = PreferenceManager.getInstance().loadMapViewType();

        // enable switch button based on zoom level
        SwingUtilities.invokeLater(() -> {
            final boolean dataSwitchButtonEnabled = zoom >= Config.getInstance().getMapPhotoZoom();
            PhotoDetailsDialog.getInstance().updateDataSwitchButton(dataType, dataSwitchButtonEnabled, null);
        });

        if (zoom < Config.getInstance().getMapPhotoZoom()) {
            if (dataType == MapViewType.ELEMENT) {
                // user zoomed out to segment view
                PreferenceManager.getInstance().saveMapViewType(MapViewType.COVERAGE);
            }
            updateLowZoomLevelData(mapViewSettings, zoom);
        } else {
            if (dataType == MapViewType.ELEMENT) {
                updateHighZoomLevelData(mapViewSettings, checkSelection, boundingBoxChanged);
            } else {
                updateLowZoomLevelData(mapViewSettings, zoom);
            }
        }
    }

    private void normalFlow(final MapViewSettings mapViewSettings, final int zoom, final boolean checkSelection,
            final boolean boundingBoxChanged) {
        final MapViewType dataType = PreferenceManager.getInstance().loadMapViewType();
        if (zoom < mapViewSettings.getPhotoZoom()) {
            if (dataType == null || dataType == MapViewType.ELEMENT) {
                // user zoomed out to segment view
                PreferenceManager.getInstance().saveMapViewType(MapViewType.COVERAGE);
            }
            updateLowZoomLevelData(mapViewSettings, zoom);
        } else if (zoom >= mapViewSettings.getPhotoZoom()) {
            if (dataType == null || dataType == MapViewType.COVERAGE) {
                // user zoomed in to photo view
                PreferenceManager.getInstance().saveMapViewType(MapViewType.ELEMENT);
            }
            updateHighZoomLevelData(mapViewSettings, checkSelection, boundingBoxChanged);
        }
    }

    private void updateLowZoomLevelData(final MapViewSettings mapViewSettings, final int zoom) {
        // clear previous data type
        if (DataSet.getInstance().hasPhotos()) {
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().clear();
                PhotoDetailsDialog.getInstance().updateUI(null, null, false);
                DetectionDetailsDialog.getInstance().updateDetectionDetails(null);
                if (mapViewSettings.isManualSwitchFlag()) {
                    PhotoDetailsDialog.getInstance().updateDataSwitchButton(MapViewType.COVERAGE, null, null);
                }
                OpenStreetCamLayer.getInstance().enablePhotoDataSetDownloadActions();
                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }

        final List<BoundingBox> areas = BoundingBoxUtil.currentBoundingBoxes();
        if (!areas.isEmpty()) {
            final SearchFilter searchFilter = PreferenceManager.getInstance().loadSearchFilter();
            final List<Segment> segments = ServiceHandler.getInstance().listMatchedTracks(areas, searchFilter, zoom);
            if (MapViewType.COVERAGE.equals(PreferenceManager.getInstance().loadMapViewType())
                    && (MainApplication.getMap() != null && MainApplication.getMap().mapView != null)) {
                SwingUtilities.invokeLater(() -> {
                    DataSet.getInstance().updateLowZoomLevelData(segments);
                    OpenStreetCamLayer.getInstance().invalidate();
                    MainApplication.getMap().repaint();
                });
            }
        }
    }

    private void updateHighZoomLevelData(final MapViewSettings mapViewSettings, final boolean checkSelection,
            final boolean boundingBoxChanged) {
        // clear previous data type
        if (DataSet.getInstance().hasSegments()) {
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().clear();
                if (mapViewSettings.isManualSwitchFlag()) {
                    PhotoDetailsDialog.getInstance().updateDataSwitchButton(MapViewType.ELEMENT, null, null);
                }
                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }

        final SearchFilter searchFilter = PreferenceManager.getInstance().loadSearchFilter();
        final BoundingBox bbox = BoundingBoxUtil.currentBoundingBox();
        if (!boundingBoxChanged && PreferenceManager.getInstance().loadOnlyDetectionFilterChangedFlag()) {
            searchFilter.getDataTypes().remove(DataType.PHOTO);
        }
        final HighZoomResultSet resultSet = ServiceHandler.getInstance().searchHighZoomData(bbox, searchFilter);
        if (MapViewType.ELEMENT.equals(PreferenceManager.getInstance().loadMapViewType())) {
            updateUI(resultSet, checkSelection);
        }
    }

    private void updateUI(final HighZoomResultSet resultSet, final boolean checkSelection) {
        if (MainApplication.getMap() != null && MainApplication.getMap().mapView != null) {
            GuiHelper.runInEDT(() -> {
                DataSet.getInstance().updateHighZoomLevelDetectionData(resultSet.getDetections(), checkSelection);
                DataSet.getInstance().updateHighZoomLevelClusterData(resultSet.getClusters(), checkSelection);
                if (!PreferenceManager.getInstance().loadOnlyDetectionFilterChangedFlag()) {
                    DataSet.getInstance().updateHighZoomLevelPhotoData(resultSet.getPhotoDataSet(), checkSelection);
                }
                updateSelection(checkSelection);
                if (DataSet.getInstance().hasNearbyPhotos()
                        && !PreferenceManager.getInstance().loadAutoplayStartedFlag()) {
                    PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
                }
                OpenStreetCamLayer.getInstance().enablePhotoDataSetDownloadActions();
                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }

    private void updateSelection(final boolean checkSelection) {
        if (!DataSet.getInstance().hasSelectedPhoto() && PhotoDetailsDialog.getInstance().isPhotoSelected()) {
            PhotoDetailsDialog.getInstance().updateUI(null, null, false);
        } else {
            if (checkSelection) {
                final List<Detection> displayedPhotoDetections =
                        PhotoDetailsDialog.getInstance().getDisplayedPhotoDetections();
                List<Detection> exposedDetections = null;
                if (displayedPhotoDetections != null && DataSet.getInstance().hasDetections()) {
                    exposedDetections = displayedPhotoDetections.stream()
                            .filter(DataSet.getInstance().getDetections()::contains).collect(Collectors.toList());
                }
                PhotoDetailsDialog.getInstance().updatePhotoDetections(exposedDetections);
            }
        }
    }
}