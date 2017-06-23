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
import javax.swing.SwingUtilities;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.OpenStreetCamDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.argument.BoundingBox;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class DataUpdateHandler {


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
    public void updateData(final boolean checkSelection) {
        final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
        if (zoom >= Config.getInstance().getMapSegmentZoom()) {
            final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
            final ListFilter listFilter = PreferenceManager.getInstance().loadListFilter();

            if (OpenStreetCamLayer.getInstance().getSelectedSequence() != null) {
                // special case, we load always photos
                updatePhotos(mapViewSettings, listFilter, checkSelection);
            } else {
                if (mapViewSettings.isManualSwitchFlag()) {
                    // change data type only if user changed manually
                    manualSwitchFlow(mapViewSettings, listFilter, zoom, checkSelection);
                } else {
                    // change data type if zoom >= mapViewSettings.photoZoom
                    normalFlow(mapViewSettings, listFilter, zoom, checkSelection);
                }
            }
        }
    }

    private void manualSwitchFlow(final MapViewSettings mapViewSettings, final ListFilter listFilter, final int zoom,
            final boolean checkSelection) {
        // enable switch button based on zoom level
        final DataType dataType = PreferenceManager.getInstance().loadDataType();
        if (zoom >= Config.getInstance().getMapPhotoZoom()) {
            OpenStreetCamDetailsDialog.getInstance().updateDataSwitchButton(dataType, true, null);
        } else {
            OpenStreetCamDetailsDialog.getInstance().updateDataSwitchButton(dataType, false, null);
        }

        if (zoom < Config.getInstance().getMapPhotoZoom()) {
            if (dataType == DataType.PHOTO) {
                // user zoomed out to segment view
                PreferenceManager.getInstance().saveDataType(DataType.SEGMENT);
            }
            updateSegments(mapViewSettings, listFilter, zoom, checkSelection);
        } else {
            if (dataType == DataType.PHOTO) {
                updatePhotos(mapViewSettings, listFilter, checkSelection);
            } else {
                updateSegments(mapViewSettings, listFilter, zoom, checkSelection);
            }
        }
    }

    private void normalFlow(final MapViewSettings mapViewSettings, final ListFilter listFilter, final int zoom,
            final boolean checkSelection) {
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
            updatePhotos(mapViewSettings, listFilter, checkSelection);
        }
    }


    private void updateSegments(final MapViewSettings mapViewSettings, final ListFilter filter, final int zoom,
            final boolean checkSelection) {
        if (OpenStreetCamLayer.getInstance().getDataSet() != null
                && OpenStreetCamLayer.getInstance().getDataSet().getPhotos() != null) {
            // clear view
            SwingUtilities.invokeLater(() -> {
                if (mapViewSettings.isManualSwitchFlag()) {
                    OpenStreetCamDetailsDialog.getInstance().updateDataSwitchButton(DataType.SEGMENT, null, null);
                }
                updateUI(null, true);
            });
        }
        final List<BoundingBox> areas = Util.currentBoundingBoxes();
        if (!areas.isEmpty()) {
            final List<Segment> segments = ServiceHandler.getInstance().listMatchedTracks(areas, filter, zoom);
            if (PreferenceManager.getInstance().loadDataType() == null
                    || PreferenceManager.getInstance().loadDataType() == DataType.SEGMENT) {
                updateUI(new DataSet(segments, null), checkSelection);
            }
        }
    }

    private void updatePhotos(final MapViewSettings mapViewSettings, final ListFilter filter,
            final boolean checkSelection) {
        if (OpenStreetCamLayer.getInstance().getDataSet() != null
                && OpenStreetCamLayer.getInstance().getDataSet().getSegments() != null) {
            // clear view
            SwingUtilities.invokeLater(() -> {
                if (mapViewSettings.isManualSwitchFlag()) {
                    OpenStreetCamDetailsDialog.getInstance().updateDataSwitchButton(DataType.PHOTO, null, null);
                }
                updateUI(null, false);
            });
        }
        final List<Circle> areas = Util.currentCircles();
        if (!areas.isEmpty()) {
            final List<Photo> photos = ServiceHandler.getInstance().listNearbyPhotos(areas, filter);
            if (PreferenceManager.getInstance().loadDataType() == DataType.PHOTO) {
                updateUI(new DataSet(null, photos), checkSelection);
            }
        }
    }

    private void updateUI(final DataSet dataSet, final boolean checkSelection) {
        if (Main.map != null && Main.map.mapView != null) {
            GuiHelper.runInEDT(() -> {
                OpenStreetCamLayer.getInstance().setDataSet(dataSet, checkSelection);
                if (OpenStreetCamLayer.getInstance().getSelectedPhoto() == null
                        && OpenStreetCamDetailsDialog.getInstance().isPhotoSelected()) {
                    OpenStreetCamDetailsDialog.getInstance().updateUI(null, null, false);
                } else {
                    if (OpenStreetCamLayer.getInstance().getClosestPhotos() != null
                            && !OpenStreetCamLayer.getInstance().getClosestPhotos().isEmpty()
                            && !PreferenceManager.getInstance().loadAutoplayStartedFlag()) {
                        OpenStreetCamDetailsDialog.getInstance().enableClosestPhotoButton(true);
                    }
                }
                OpenStreetCamLayer.getInstance().invalidate();
                Main.map.repaint();
            });
        }
    }
}