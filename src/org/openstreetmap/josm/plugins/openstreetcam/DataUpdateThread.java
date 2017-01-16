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

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.OpenStreetCamDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


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
            final List<Circle> areas = new ArrayList<>();
            if (Main.getLayerManager().getEditLayer() != null
                    && (Main.getLayerManager().getActiveLayer() instanceof OsmDataLayer)) {
                final List<Bounds> osmDataLayerBounds =
                        Main.getLayerManager().getEditLayer().data.getDataSourceBounds();
                if (osmDataLayerBounds != null && !osmDataLayerBounds.isEmpty()) {
                    for (final Bounds bounds : osmDataLayerBounds) {
                        if (Main.map.mapView.getRealBounds().intersects(bounds)) {
                            areas.add(new Circle(Main.map.mapView.getRealBounds()));
                        } else {
                            areas.add(new Circle(Main.map.mapView.getRealBounds()));
                        }
                    }
                } else {
                    areas.add(new Circle(Main.map.mapView.getRealBounds()));
                }
            } else {
                areas.add(new Circle(Main.map.mapView.getRealBounds()));
            }
            final ListFilter filter = PreferenceManager.getInstance().loadListFilter();
            final List<Photo> photos = ServiceHandler.getInstance().listNearbyPhotos(areas, filter);
            updateUI(photos, checkSelectedPhoto);
        }
    }

    private void updateUI(final List<Photo> photos, final boolean checkSelectedPhoto) {
        SwingUtilities.invokeLater(() -> {
            layer.setPhotos(photos, checkSelectedPhoto);
            if (layer.getSelectedPhoto() == null) {
                detailsDialog.updateUI(null);
            }
            Main.map.repaint();
        });
    }
}