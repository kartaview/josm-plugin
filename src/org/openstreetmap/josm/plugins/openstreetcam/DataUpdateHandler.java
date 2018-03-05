/*
 *  Copyright 2017 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
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
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSize;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
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

            if (OpenStreetCamLayer.getInstance().getSelectedSequence() != null) {
                // special case, we load always photos
                updatePhotos(mapViewSettings, listFilter, checkSelection, boundingBoxChanged);
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
            updateSegments(mapViewSettings, listFilter, zoom, checkSelection);
        } else {
            if (dataType == DataType.PHOTO) {
                updatePhotos(mapViewSettings, listFilter, checkSelection, boundingBoxChanged);
            } else {
                updateSegments(mapViewSettings, listFilter, zoom, checkSelection);
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
            updateSegments(mapViewSettings, listFilter, zoom, checkSelection);
        } else if (zoom >= mapViewSettings.getPhotoZoom()) {
            if (dataType == null || dataType == DataType.SEGMENT) {
                // user zoomed in to photo view
                PreferenceManager.getInstance().saveDataType(DataType.PHOTO);
            }
            updatePhotos(mapViewSettings, listFilter, checkSelection, boundingBoxChanged);
        }
    }

    private void updateSegments(final MapViewSettings mapViewSettings, final SearchFilter filter, final int zoom,
            final boolean checkSelection) {
        if (OpenStreetCamLayer.getInstance().getDataSet() != null
                && !OpenStreetCamLayer.getInstance().getDataSet().getPhotos().isEmpty()) {
            // clear view
            SwingUtilities.invokeLater(() -> {
                if (mapViewSettings.isManualSwitchFlag()) {
                    PhotoDetailsDialog.getInstance().updateDataSwitchButton(DataType.SEGMENT, null, null);
                }
                updateUI(null, true);
            });
        }
        final List<BoundingBox> areas = BoundingBoxUtil.currentBoundingBoxes();
        if (!areas.isEmpty()) {
            final List<Segment> segments = ServiceHandler.getInstance().listMatchedTracks(areas, filter, zoom);
            if (PreferenceManager.getInstance().loadDataType() == null
                    || PreferenceManager.getInstance().loadDataType() == DataType.SEGMENT) {
                updateUI(new DataSet(segments, null, null), checkSelection);
            }
        }
    }

    private void updatePhotos(final MapViewSettings mapViewSettings, final SearchFilter filter,
            final boolean checkSelection, final boolean boundingBoxChanged) {
        if (OpenStreetCamLayer.getInstance().getDataSet() != null
                && OpenStreetCamLayer.getInstance().getDataSet().getSegments() != null) {
            // clear view
            SwingUtilities.invokeLater(() -> {
                if (mapViewSettings.isManualSwitchFlag()) {
                    PhotoDetailsDialog.getInstance().updateDataSwitchButton(DataType.PHOTO, null, null);
                    DetectionDetailsDialog.getInstance().updateDetectionDetails(null);
                }
                updateUI(null, false);
            });
        }
        OpenStreetCamLayer.getInstance().enableDownloadPreviousPhotoAction(false);
        OpenStreetCamLayer.getInstance().enabledDownloadNextPhotosAction(false);
        final BoundingBox bbox = BoundingBoxUtil.currentBoundingBox();
        if (bbox != null) {
            PhotoDataSet photoDataSet = null;
            if (!boundingBoxChanged && PreferenceManager.getInstance().loadOnlyDetectionFilterChangedFlag()) {
                filter.getDataTypes().remove(ImageDataType.PHOTOS);
                photoDataSet = OpenStreetCamLayer.getInstance().getDataSet() != null
                        ? OpenStreetCamLayer.getInstance().getDataSet().getPhotoDataSet() : null;
            }

            Pair<PhotoDataSet, List<Detection>> dataSet = ServiceHandler.getInstance().searchHighZoomData(bbox, filter);
            if (photoDataSet != null) {
                dataSet = new Pair<PhotoDataSet, List<Detection>>(photoDataSet, dataSet.getSecond());
            }
            if (PreferenceManager.getInstance().loadDataType() == DataType.PHOTO) {
                updateUI(new DataSet(null, dataSet.getFirst(), dataSet.getSecond()), checkSelection);
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
        final PhotoDataSet currentPhotoDataSet = OpenStreetCamLayer.getInstance().getDataSet() != null
                ? OpenStreetCamLayer.getInstance().getDataSet().getPhotoDataSet() : null;
        PhotoDataSet photoDataSet = null;
        if (currentPhotoDataSet != null) {
            int page = currentPhotoDataSet.getPage();
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

    /**
     * Verifies if the photo download is allowed or not. A new photo data set download is allowed in the following
     * cases:
     * <ul>
     * <li>current zoom >=photo zoom and no track is displayed</li>
     * <li>user has manual data switch enabled and photo locations are displayed on the map</li>
     * </ul>
     *
     * @return a boolean value
     */
    boolean photoDataSetDownloadAllowed() {
        boolean result = false;
        final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
        final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
        if (mapViewSettings.isManualSwitchFlag()) {
            result = zoom >= Config.getInstance().getMapPhotoZoom()
                    && PreferenceManager.getInstance().loadDataType() == DataType.PHOTO;
        } else if (zoom >= mapViewSettings.getPhotoZoom()) {
            result = OpenStreetCamLayer.getInstance().getSelectedSequence() == null;
        }
        return result;
    }

    void updateUI(final PhotoDataSet photoDataSet) {
        final List<Detection> detections = OpenStreetCamLayer.getInstance().getDataSet() != null
                ? OpenStreetCamLayer.getInstance().getDataSet().getDetections() : null;
        updateUI(new DataSet(null, photoDataSet, detections), true);
    }

    /**
     * Updates the UI with the given data set.
     *
     * @param dataSet a {@code DataSet} represents a new data set
     * @param checkSelection if true then the currently selected element will be removed if it is not present in the
     * given data set
     */
    // TODO: refactore this method
    void updateUI(final DataSet dataSet, final boolean checkSelection) {
        if (MainApplication.getMap() != null && MainApplication.getMap().mapView != null) {
            GuiHelper.runInEDT(() -> {
                OpenStreetCamLayer.getInstance().setDataSet(dataSet, checkSelection);

                DetectionDetailsDialog.getInstance()
                        .updateDetectionDetails(OpenStreetCamLayer.getInstance().getSelectedDetection());
                if (OpenStreetCamLayer.getInstance().getSelectedPhoto() == null
                        && PhotoDetailsDialog.getInstance().isPhotoSelected()) {
                    PhotoDetailsDialog.getInstance().updateUI(null, null, false);
                } else {
                    // TODO this logic is duplicated in SelectionHandler; refactoring needed
                    final Photo photo = OpenStreetCamLayer.getInstance().getSelectedPhoto();

                    if (OpenStreetCamLayer.getInstance().getSelectedDetection() != null && photo != null) {
                        final List<Detection> photoDetections = ServiceHandler.getInstance()
                                .retrievePhotoDetections(photo.getSequenceId(), photo.getSequenceIndex());
                        List<Detection> exposedDetections = null;
                        if (photoDetections != null && OpenStreetCamLayer.getInstance().getDataSet() != null
                                && OpenStreetCamLayer.getInstance().getDataSet().getDetections() != null) {
                            exposedDetections = photoDetections.stream()
                                    .filter(OpenStreetCamLayer.getInstance().getDataSet().getDetections()::contains)
                                    .collect(Collectors.toList());
                            photo.setDetections(exposedDetections);
                        }
                    }
                    final PhotoSettings photoSettings = PreferenceManager.getInstance().loadPhotoSettings();
                    PhotoDetailsDialog.getInstance().updateUI(photo,
                            photoSettings.isHighQualityFlag() ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL,
                            true);

                    if (OpenStreetCamLayer.getInstance().getClosestPhotos() != null
                            && !OpenStreetCamLayer.getInstance().getClosestPhotos().isEmpty()
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