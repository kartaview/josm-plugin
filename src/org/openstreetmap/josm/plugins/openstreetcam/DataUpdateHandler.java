/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ImageDataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Paging;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.util.BoundingBoxUtil;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.OpenStreetCamServiceConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.argument.BoundingBox;
import com.telenav.josm.common.entity.Pair;


/**
 * Handles map view data download and update operations.
 *
 * @author beataj
 * @version $Revision$
 */
class DataUpdateHandler {


    /**
     * Updates the current map view with OpenStreetCam data. The data type displayed depends on the current zoom level.
     * Segments that have OpenStreetCam coverage are displayed in the following cases:
     * <ul>
     * <li>current zoom level>=minimum map data zoom (~10) and current zoom level < default photo location zoom</li>
     * <li>current zoom level>=minimum map data zoom (~10) and user enabled manual data switch and zoom level <=minimum
     * photo location zoom</li>
     * <li>current zoom level>=minimum map data zoom (~10) and user enabled manual data switch and had switched to
     * segment view</li>
     * </ul>
     * Photo locations are displayed in the following cases:
     * <ul>
     * <li>current zoom level >= default photo zoom level</li>
     * <li>current zoom level >= default photo zoom level and user had enabled manual data switch and had switched to
     * photo location view</li>
     * <li>a track is displayed (we displayed always photo locations near the a track)</li>
     * </ul>
     *
     * @param checkSelection if true verifies if the selected element is contained or not in the new data set, selection
     * is removed for the case when the data set does not contain the selection; if false it is ignored
     */
    void updateData(final boolean checkSelection, final boolean boundingBoxChanged) {
        final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
        if (zoom >= Config.getInstance().getMapSegmentZoom()) {
            final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
            final SearchFilter listFilter = PreferenceManager.getInstance().loadSearchFilter();

            if (DataSet.getInstance().hasSelectedSequence()) {
                // special case, we load always photos
                updateHighZoomLevelData(mapViewSettings, listFilter, checkSelection, boundingBoxChanged);
            } else {
                if (mapViewSettings.isManualSwitchFlag()) {
                    // change data type only if user changed manually
                    manualSwitchFlow(mapViewSettings, listFilter, zoom, checkSelection, boundingBoxChanged);
                } else {
                    // change data type if zoom >= mapViewSettings.photoZoom
                    normalFlow(mapViewSettings, listFilter, zoom, checkSelection, boundingBoxChanged);
                }
            }
        }
    }

    private void manualSwitchFlow(final MapViewSettings mapViewSettings, final SearchFilter listFilter, final int zoom,
            final boolean checkSelection, final boolean boundingBoxChanged) {
        // enable switch button based on zoom level
        final DataType dataType = PreferenceManager.getInstance().loadDataType();
        if (zoom >= Config.getInstance().getMapPhotoZoom()) {
            PhotoDetailsDialog.getInstance().updateDataSwitchButton(dataType, true, null);
        } else {
            PhotoDetailsDialog.getInstance().updateDataSwitchButton(dataType, false, null);
        }

        if (zoom < Config.getInstance().getMapPhotoZoom()) {
            if (dataType == DataType.PHOTO) {
                // user zoomed out to segment view
                PreferenceManager.getInstance().saveDataType(DataType.SEGMENT);
            }
            updateLowZoomLevelData(mapViewSettings, listFilter, zoom);
        } else {
            if (dataType == DataType.PHOTO) {
                updateHighZoomLevelData(mapViewSettings, listFilter, checkSelection, boundingBoxChanged);
            } else {
                updateLowZoomLevelData(mapViewSettings, listFilter, zoom);
            }
        }
    }

    private void normalFlow(final MapViewSettings mapViewSettings, final SearchFilter listFilter, final int zoom,
            final boolean checkSelection, final boolean boundingBoxChanged) {
        final DataType dataType = PreferenceManager.getInstance().loadDataType();
        if (zoom < mapViewSettings.getPhotoZoom()) {
            if (dataType == null || dataType == DataType.PHOTO) {
                // user zoomed out to segment view
                PreferenceManager.getInstance().saveDataType(DataType.SEGMENT);
            }
            updateLowZoomLevelData(mapViewSettings, listFilter, zoom);
        } else if (zoom >= mapViewSettings.getPhotoZoom()) {
            if (dataType == null || dataType == DataType.SEGMENT) {
                // user zoomed in to photo view
                PreferenceManager.getInstance().saveDataType(DataType.PHOTO);
            }
            updateHighZoomLevelData(mapViewSettings, listFilter, checkSelection, boundingBoxChanged);
        }
    }

    private void updateLowZoomLevelData(final MapViewSettings mapViewSettings, final SearchFilter filter,
            final int zoom) {
        if (DataSet.getInstance().hasPhotos()) {
            // SwingUtilities.invokeLater(() -> {
            DataSet.getInstance().clear();
            PhotoDetailsDialog.getInstance().updateUI(null, null, false);
            DetectionDetailsDialog.getInstance().updateDetectionDetails(null);
            if (mapViewSettings.isManualSwitchFlag()) {
                PhotoDetailsDialog.getInstance().updateDataSwitchButton(DataType.SEGMENT, null, null);
            }
            OpenStreetCamLayer.getInstance().invalidate();
            MainApplication.getMap().repaint();
            // });
        }
        OpenStreetCamLayer.getInstance().enablePhotoDataSetDownloadActions();
        final List<BoundingBox> areas = BoundingBoxUtil.currentBoundingBoxes();
        if (!areas.isEmpty()) {
            final List<Segment> segments = ServiceHandler.getInstance().listMatchedTracks(areas, filter, zoom);
            if (PreferenceManager.getInstance().loadDataType() == null
                    || PreferenceManager.getInstance().loadDataType() == DataType.SEGMENT) {
                if (MainApplication.getMap() != null && MainApplication.getMap().mapView != null) {
                    // SwingUtilities.invokeLater(() -> {
                    DataSet.getInstance().updateLowZoomLevelData(segments);
                    OpenStreetCamLayer.getInstance().invalidate();
                    MainApplication.getMap().repaint();
                    // });
                }
            }
        }
    }


    private void updateHighZoomLevelData(final MapViewSettings mapViewSettings, final SearchFilter filter,
            final boolean checkSelection, final boolean boundingBoxChanged) {
        if (DataSet.getInstance().hasSegments()) {
            SwingUtilities.invokeLater(() -> {

                DataSet.getInstance().clear();
                if (mapViewSettings.isManualSwitchFlag()) {
                    PhotoDetailsDialog.getInstance().updateDataSwitchButton(DataType.PHOTO, null, null);
                }
                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
        final BoundingBox bbox = BoundingBoxUtil.currentBoundingBox();
        if (bbox != null) {
            if (!boundingBoxChanged && PreferenceManager.getInstance().loadOnlyDetectionFilterChangedFlag()) {
                filter.getDataTypes().remove(ImageDataType.PHOTOS);
            }
            final Pair<PhotoDataSet, List<Detection>> dataSet =
                    ServiceHandler.getInstance().searchHighZoomData(bbox, filter);
            if (PreferenceManager.getInstance().loadDataType() == DataType.PHOTO) {
                updateUI(null, dataSet.getFirst(), dataSet.getSecond(), checkSelection);
            }
        }
    }


    /**
     * Downloads the next/previous set of photo locations from the current view.
     *
     * @param loadNextResults if true then the next photo location data set is downloaded; if false then the previous
     * photo location data set is downloaded
     * @return a {@code PhotoDataSet} containing the photo locations
     */
    PhotoDataSet downloadPhotos(final boolean loadNextResults) {
        PhotoDataSet photoDataSet = null;
        if (DataSet.getInstance().hasPhotos()) {
            int page = DataSet.getInstance().getPhotoDataSet().getPage();
            page = loadNextResults ? page + 1 : page - 1;
            final SearchFilter listFilter = PreferenceManager.getInstance().loadSearchFilter();
            photoDataSet = new PhotoDataSet();
            final BoundingBox bbox = BoundingBoxUtil.currentBoundingBox();
            if (bbox != null) {
                photoDataSet = ServiceHandler.getInstance().listNearbyPhotos(bbox, listFilter,
                        new Paging(page, OpenStreetCamServiceConfig.getInstance().getNearbyPhotosMaxItems()));
            }
        }
        return photoDataSet;
    }

    void updateUI(final PhotoDataSet photoDataSet) {
        // TODO: add logic
    }


    /**
     * Updates the UI with the given data set.
     *
     * @param dataSet a {@code DataSet} represents a new data set
     * @param checkSelection if true then the currently selected element will be removed if it is not present in the
     * given data set
     */
    // TODO: refactore this method
    void updateUI(final List<Segment> segments, final PhotoDataSet photoDataSet, final List<Detection> detections,
            final boolean checkSelection) {
        if (MainApplication.getMap() != null && MainApplication.getMap().mapView != null) {
            GuiHelper.runInEDT(() -> {
                if (PreferenceManager.getInstance().loadOnlyDetectionFilterChangedFlag()) {
                    DataSet.getInstance().updateHighZoomLevelDetectionData(detections, checkSelection);
                } else {
                    DataSet.getInstance().updateHighZoomLevelDetectionData(detections, checkSelection);
                    DataSet.getInstance().updateHighZoomLevelPhotoData(photoDataSet, checkSelection);
                }
                OpenStreetCamLayer.getInstance().enablePhotoDataSetDownloadActions();

                if (!DataSet.getInstance().hasSelectedPhoto() && PhotoDetailsDialog.getInstance().isPhotoSelected()) {
                    PhotoDetailsDialog.getInstance().updateUI(null, null, false);
                } else {

                    if (checkSelection) {
                        final List<Detection> displayedPhotoDetections =
                                PhotoDetailsDialog.getInstance().getDisplayedPhotoDetections();
                        List<Detection> exposedDetections = null;
                        if (displayedPhotoDetections != null && DataSet.getInstance().hasDetections()) {
                            exposedDetections = displayedPhotoDetections.stream()
                                    .filter(DataSet.getInstance().getDetections()::contains)
                                    .collect(Collectors.toList());
                        }
                        PhotoDetailsDialog.getInstance().updatePhotoDetections(exposedDetections);
                    }

                    if (DataSet.getInstance().hasNearbyPhotos()
                            && !PreferenceManager.getInstance().loadAutoplayStartedFlag()) {
                        PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
                    }
                }

                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }
}