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
package org.openstreetmap.josm.plugins.openstreetview;

import java.util.List;
import javax.swing.SwingUtilities;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetview.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetview.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetview.gui.details.OpenStreetViewDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetview.gui.layer.OpenStreetViewLayer;
import org.openstreetmap.josm.plugins.openstreetview.util.Util;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.ServiceConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.pref.PreferenceManager;


/**
 * Downloads the data from the current bounding box using the current filters and updates the UI accordingly.
 *
 * @author Beata
 * @version $Revision$
 */
class DataUpdateThread implements Runnable {

    private final OpenStreetViewLayer layer;
    private final OpenStreetViewDetailsDialog detailsDialog;


    DataUpdateThread(final OpenStreetViewLayer layer, final OpenStreetViewDetailsDialog detailsDialog) {
        this.layer = layer;
        this.detailsDialog = detailsDialog;
    }

    @Override
    public void run() {
        if (Main.map != null && Main.map.mapView != null) {
            final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
            if (zoom >= ServiceConfig.getInstance().getPhotoZoom()) {
                final Circle circle = new Circle(Main.map.mapView);
                final ListFilter filter = PreferenceManager.getInstance().loadListFilter();
                final List<Photo> photos = ServiceHandler.getInstance().listNearbyPhotos(circle, filter);
                updateUI(photos);
            } else {
                updateUI(null);
            }
        }
    }

    private void updateUI(final List<Photo> photos) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                layer.setPhotos(photos);
                if (layer.getSelectedPhoto() == null) {
                    detailsDialog.updateUI(null);
                }
                Main.map.repaint();
            }
        });
    }
}