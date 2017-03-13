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

            final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
            final DataType dataType = PreferenceManager.getInstance().loadManualSwitchDataType();
            final ListFilter listFilter = PreferenceManager.getInstance().loadListFilter();
            if (layer.getSelectedSequence() == null && (zoom >= Config.getInstance().getMapSegmentZoom()
                    && (zoom < mapViewSettings.getPhotoZoom() || mapViewSettings.isManualSwitchFlag())
                    && dataType == null) || (dataType != null && dataType.equals(DataType.SEGMENT))) {
                detailsDialog.updateManualSwitchButton(DataType.SEGMENT, zoom);
                updateSegments(listFilter, zoom);
            } else if ((zoom >= mapViewSettings.getPhotoZoom() && !mapViewSettings.isManualSwitchFlag())
                    || (dataType != null && dataType.equals(DataType.PHOTO)
                    && zoom >= Config.getInstance().getMapPhotoZoom())) {
                if (layer.getSelectedSequence() == null) {
                    detailsDialog.updateManualSwitchButton(DataType.PHOTO, zoom);
                }
                updatePhotos(listFilter);
            }
        }
    }

    private void updateSegments(final ListFilter filter, final int zoom) {
        if (layer.getDataSet() != null && layer.getDataSet().getPhotos() != null) {
            // clear view
            updateUI(null, false);
        }
        final List<BoundingBox> areas = Util.currentBoundingBoxes();
        final List<Segment> segments = ServiceHandler.getInstance().listMatchedTracks(areas, filter, zoom);
        updateUI(new DataSet(segments, null), checkSelectedPhoto);

    }

    private void updatePhotos(final ListFilter filter) {
        if (layer.getDataSet() != null && layer.getDataSet().getSegments() != null) {
            // clear view
            updateUI(null, false);
        }
        final List<Circle> areas = Util.currentCircles();
        final List<Photo> photos = ServiceHandler.getInstance().listNearbyPhotos(areas, filter);
        updateUI(new DataSet(null, photos), checkSelectedPhoto);
    }


    private void updateUI(final DataSet dataSet, final boolean checkSelectedPhoto) {
        SwingUtilities.invokeLater(() -> {
            layer.setDataSet(dataSet, checkSelectedPhoto);
            if (layer.getSelectedPhoto() == null) {
                detailsDialog.updateUI(null);
            }
            Main.map.repaint();
        });
    }
}