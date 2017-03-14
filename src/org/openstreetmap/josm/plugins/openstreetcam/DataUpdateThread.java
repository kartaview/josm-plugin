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


/**
 * Downloads the data from the current bounding box using the current filters and updates the UI accordingly.
 *
 * @author Beata
 * @version $Revision$
 */
class DataUpdateThread implements Runnable {

    private final OpenStreetCamLayer layer;
    private final OpenStreetCamDetailsDialog detailsDialog;
    private final Boolean checkSelectedPhoto;


    DataUpdateThread(final OpenStreetCamLayer layer, final OpenStreetCamDetailsDialog detailsDialog,
            final Boolean checkSelectedPhoto) {
        this.layer = layer;
        this.detailsDialog = detailsDialog;
        this.checkSelectedPhoto = checkSelectedPhoto;
    }


    @Override
    public void run() {
        if (Main.map != null && Main.map.mapView != null) {
            // case 1 search for segments
            final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
            if (zoom >= Config.getInstance().getMapSegmentZoom()) {

                final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
                final DataType dataType = PreferenceManager.getInstance().loadManualSwitchDataType();
                final ListFilter listFilter = PreferenceManager.getInstance().loadListFilter();

                if (layer.getSelectedSequence() == null && shouldUpdateSegments(mapViewSettings, dataType, zoom)) {
                    updateSegments(mapViewSettings, listFilter, zoom);
                } else if (shouldUpdatePhotos(mapViewSettings, dataType, zoom)) {
                    updatePhotos(mapViewSettings, listFilter, zoom);
                }
            }
        }
    }

    private boolean shouldUpdateSegments(final MapViewSettings mapViewSettings, final DataType dataType,
            final int zoom) {
        boolean result = false;

        // if zoom level < 16 switch automatically to segment view (unless a sequence is displayed)
        if (zoom < Config.getInstance().getMapPhotoZoom()) {
            result = true;
        } else if (zoom >= Config.getInstance().getMapPhotoZoom()) {
            if (mapViewSettings.isManualSwitchFlag()) {
                result = (dataType == null || dataType.equals(DataType.SEGMENT));
            } else {
                result = zoom < mapViewSettings.getPhotoZoom();
            }
        }
        return result;
    }


    private boolean shouldUpdatePhotos(final MapViewSettings mapViewSettings, final DataType dataType, final int zoom) {
        boolean result = false;
        if (mapViewSettings.isManualSwitchFlag()) {
            result = zoom >= Config.getInstance().getMapPhotoZoom()
                    && (dataType != null && dataType.equals(DataType.PHOTO));
        } else {
            result = zoom >= mapViewSettings.getPhotoZoom();
        }
        return result;
    }

    private void updateSegments(final MapViewSettings mapViewSettings, final ListFilter filter, final int zoom) {
        if (layer.getDataSet() != null && layer.getDataSet().getPhotos() != null) {
            // clear view
            detailsDialog.updateManualSwitchButton(DataType.SEGMENT);
            updateUI(null, false, false);
        }
        final List<BoundingBox> areas = Util.currentBoundingBoxes();
        final List<Segment> segments = ServiceHandler.getInstance().listMatchedTracks(areas, filter, zoom);
        final boolean enableManualSwitchButton =
                zoom >= Config.getInstance().getMapPhotoZoom() && mapViewSettings.isManualSwitchFlag();
        updateUI(new DataSet(segments, null), checkSelectedPhoto, enableManualSwitchButton);

    }

    private void updatePhotos(final MapViewSettings mapViewSettings, final ListFilter filter, final int zoom) {
        if (layer.getDataSet() != null && layer.getDataSet().getSegments() != null) {
            // clear view
            detailsDialog.updateManualSwitchButton(DataType.PHOTO);
            updateUI(null, false, false);
        }
        final List<Circle> areas = Util.currentCircles();
        final List<Photo> photos = ServiceHandler.getInstance().listNearbyPhotos(areas, filter);
        final boolean enableManualSwitchButton =
                zoom >= Config.getInstance().getMapPhotoZoom() && mapViewSettings.isManualSwitchFlag();
        updateUI(new DataSet(null, photos), checkSelectedPhoto, enableManualSwitchButton);
    }


    private void updateUI(final DataSet dataSet, final boolean checkSelectedPhoto, final boolean enableSwitchButton) {
        SwingUtilities.invokeLater(() -> {
            layer.setDataSet(dataSet, checkSelectedPhoto);
            if (layer.getSelectedPhoto() == null) {
                detailsDialog.updateUI(null);
            }
            detailsDialog.enableManualSwitchButton(enableSwitchButton);
            Main.map.repaint();
        });
    }
}