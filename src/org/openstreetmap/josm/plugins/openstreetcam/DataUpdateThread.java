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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.SwingUtilities;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Paging;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.OpenStreetCamDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.ServiceConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.Pair;
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
            if (zoom > 9 && zoom < 17) {
                if (layer.getDataSet() != null && layer.getDataSet().getPhotos() != null) {
                    // clear view
                    updateUI(null, false);
                }
                final Bounds bounds = new Bounds(Main.map.mapView.getLatLon(0, Main.map.mapView.getHeight()),
                        Main.map.mapView.getLatLon(Main.map.mapView.getWidth(), 0));
                final BoundingBox bbox = new BoundingBox(bounds.getMax().lat(), bounds.getMin().lat(),
                        bounds.getMax().lon(), bounds.getMin().lon());
                final ListFilter filter = PreferenceManager.getInstance().loadListFilter();
                final Pair<Integer, List<Segment>> result =
                        ServiceHandler.getInstance().listMatchedTracks2(bbox, filter, zoom, Paging.DEFAULT);
                System.out.println("total:" + result.a);
                if (result.a > ServiceConfig.getInstance().getMaxItems()) {
                    final int total = result.a - ServiceConfig.getInstance().getMaxItems();
                    int pages = total > ServiceConfig.getInstance().getMaxItems()
                            ? total / ServiceConfig.getInstance().getMaxItems() : 1;
                            pages += 2;
                            final ExecutorService executor = Executors.newFixedThreadPool(pages);
                            final List<Future<Pair<Integer, List<Segment>>>> futures = new ArrayList<>();

                            for (int i = 2; i <= pages; i++) {
                                final int page = i;
                                final Callable<Pair<Integer, List<Segment>>> callable =
                                        () -> ServiceHandler.getInstance().listMatchedTracks2(bbox, filter, zoom,
                                        new Paging(page, ServiceConfig.getInstance().getMaxItems()));
                                        futures.add(executor.submit(callable));

                            }
                            final List<Segment> segments = readResult(bbox, futures);
                            executor.shutdown();
                            segments.addAll(result.b);
                            updateUI(new DataSet(segments, null), checkSelectedPhoto);
                }

            } else if (zoom >= 17) {
                // case 2 search for photos
                if (layer.getDataSet() != null && layer.getDataSet().getSegments() != null) {
                    // clear view
                    updateUI(null, false);
                }
                final List<Circle> areas = searchArea();
                final ListFilter filter = PreferenceManager.getInstance().loadListFilter();
                final List<Photo> photos = ServiceHandler.getInstance().listNearbyPhotos(areas, filter);
                updateUI(new DataSet(null, photos), checkSelectedPhoto);
            }
        }
    }


    private ArrayList<Segment> readResult(final BoundingBox bbox,
            final List<Future<Pair<Integer, List<Segment>>>> futures) {
        final Set<Segment> result = new HashSet<>();
        for (final Future<Pair<Integer, List<Segment>>> future : futures) {
            try {
                result.addAll(future.get().b);
            } catch (InterruptedException | ExecutionException e) {
                // throw new ServiceException(e);
            }
        }
        return new ArrayList<>(result);
    }

    private List<Circle> searchArea() {
        final List<Circle> result = new ArrayList<>();
        if (Main.getLayerManager().getEditLayer() != null
                && (Main.getLayerManager().getActiveLayer() instanceof OsmDataLayer)) {
            final List<Bounds> osmDataLayerBounds = Main.getLayerManager().getEditLayer().data.getDataSourceBounds();
            if (osmDataLayerBounds != null && !osmDataLayerBounds.isEmpty()) {
                for (final Bounds bounds : osmDataLayerBounds) {
                    final Circle circle = Main.map.mapView.getRealBounds().intersects(bounds)
                            ? new Circle(Main.map.mapView.getRealBounds()) : new Circle(bounds);
                            result.add(circle);
                }
            } else {
                result.add(new Circle(Main.map.mapView.getRealBounds()));
            }
        } else {
            result.add(new Circle(Main.map.mapView.getRealBounds()));
        }
        return result;
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