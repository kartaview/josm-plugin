/*
 *  Copyright 2016 Telenav, Inc.
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
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Downloads the data from the current bounding box using the current filters and updates the UI accordingly.
 *
 * @author Beata
 * @version $Revision$
 */
class DataUpdateThread implements Runnable {

    private final Boolean checkSelectedPhoto;

    DataUpdateThread(final Boolean checkSelectedPhoto) {
        this.checkSelectedPhoto = checkSelectedPhoto;
    }


    @Override
    public void run() {
        final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
        if (zoom >= Config.getInstance().getMapSegmentZoom()) {
            final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
            final ListFilter listFilter = PreferenceManager.getInstance().loadListFilter();

            if (OpenStreetCamLayer.getInstance().getSelectedSequence() != null) {
                // special case, we load always photos
                ThreadPool.getInstance().execute(() -> updatePhotos(mapViewSettings, listFilter, zoom));
            } else {
                if (mapViewSettings.isManualSwitchFlag()) {
                    // change data only if user changed manually
                    if (zoom >= Config.getInstance().getMapPhotoZoom()) {
                        OpenStreetCamDetailsDialog.getInstance().enableManualSwitchButton(true);
                    } else {
                        OpenStreetCamDetailsDialog.getInstance().enableManualSwitchButton(false);
                    }
                    final DataType dataType = PreferenceManager.getInstance().loadManualSwitchDataType();
                    if (zoom < Config.getInstance().getMapPhotoZoom()) {
                        if (dataType == DataType.PHOTO) {
                            PreferenceManager.getInstance().saveManualSwitchDataType(DataType.SEGMENT);
                        }
                        ThreadPool.getInstance().execute(() -> updateSegments(mapViewSettings, listFilter, zoom));
                    } else {
                        if (dataType == DataType.PHOTO) {
                            ThreadPool.getInstance().execute(() -> updatePhotos(mapViewSettings, listFilter, zoom));
                        } else {
                            ThreadPool.getInstance().execute(() -> updateSegments(mapViewSettings, listFilter, zoom));
                        }
                    }
                } else {
                    final DataType dataType = PreferenceManager.getInstance().loadManualSwitchDataType();
                    if (zoom < mapViewSettings.getPhotoZoom()) {
                        if (dataType == null || dataType == DataType.PHOTO) {
                            PreferenceManager.getInstance().saveManualSwitchDataType(DataType.SEGMENT);
                        }
                        ThreadPool.getInstance().execute(() -> updateSegments(mapViewSettings, listFilter, zoom));
                    } else if (zoom >= mapViewSettings.getPhotoZoom()) {
                        if (dataType == null || dataType == DataType.SEGMENT) {
                            PreferenceManager.getInstance().saveManualSwitchDataType(DataType.PHOTO);
                        }
                        ThreadPool.getInstance().execute(() -> updatePhotos(mapViewSettings, listFilter, zoom));
                    }
                }
            }
        }
    }


    private void updateSegments(final MapViewSettings mapViewSettings, final ListFilter filter, final int zoom) {
        if (OpenStreetCamLayer.getInstance().getDataSet() != null
                && OpenStreetCamLayer.getInstance().getDataSet().getPhotos() != null) {
            // clear view
            SwingUtilities.invokeLater(() -> {
                if (mapViewSettings.isManualSwitchFlag()) {
                    OpenStreetCamDetailsDialog.getInstance().updateManualSwitchButton(DataType.SEGMENT);
                }
                updateUI(null, false);
            });
        }
        final List<BoundingBox> areas = Util.currentBoundingBoxes();
        final List<Segment> segments = ServiceHandler.getInstance().listMatchedTracks(areas, filter, zoom);
        if (PreferenceManager.getInstance().loadManualSwitchDataType() == null
                || PreferenceManager.getInstance().loadManualSwitchDataType() == DataType.SEGMENT) {
            updateUI(new DataSet(segments, null), checkSelectedPhoto);
        }
    }

    private void updatePhotos(final MapViewSettings mapViewSettings, final ListFilter filter, final int zoom) {
        if (OpenStreetCamLayer.getInstance().getDataSet() != null
                && OpenStreetCamLayer.getInstance().getDataSet().getSegments() != null) {
            // clear view
            SwingUtilities.invokeLater(() -> {
                if (mapViewSettings.isManualSwitchFlag()) {
                    OpenStreetCamDetailsDialog.getInstance().updateManualSwitchButton(DataType.PHOTO);
                }
                updateUI(null, false);
            });
        }
        final List<Circle> areas = Util.currentCircles();
        final List<Photo> photos = ServiceHandler.getInstance().listNearbyPhotos(areas, filter);
        if (PreferenceManager.getInstance().loadManualSwitchDataType() == DataType.PHOTO) {
            updateUI(new DataSet(null, photos), checkSelectedPhoto);
        }
    }

    private void updateUI(final DataSet dataSet, final boolean checkSelectedPhoto) {
        SwingUtilities.invokeLater(() -> {
            OpenStreetCamLayer.getInstance().setDataSet(dataSet, checkSelectedPhoto);
            if (OpenStreetCamLayer.getInstance().getSelectedPhoto() == null) {
                OpenStreetCamDetailsDialog.getInstance().updateUI(null);
            }
            Main.map.repaint();
        });
    }
}