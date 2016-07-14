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

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.Preferences.PreferenceChangeEvent;
import org.openstreetmap.josm.data.Preferences.PreferenceChangedListener;
import org.openstreetmap.josm.gui.IconToggleButton;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.NavigatableComponent;
import org.openstreetmap.josm.gui.NavigatableComponent.ZoomChangeListener;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerAddEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerChangeListener;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerOrderChangeEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerRemoveEvent;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.openstreetview.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetview.gui.details.OpenStreetViewDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetview.gui.layer.OpenStreetViewLayer;
import org.openstreetmap.josm.plugins.openstreetview.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetview.util.Util;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.ServiceConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.pref.PreferenceManager;


/**
 * Defines the main functionality of the OpenStreetView plugin.
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetViewPlugin extends Plugin
implements ZoomChangeListener, LayerChangeListener, MouseListener, LocationObserver, PreferenceChangedListener {

    /* details dialog associated with this plugin */
    private OpenStreetViewDetailsDialog detailsDialog;

    /* layer associated with this plugin */
    private OpenStreetViewLayer layer;

    private static final int SEARCH_DELAY = 700;
    private Timer zoomTimer;
    private Bounds selectedPhotoBounds;


    /**
     * Builds a new object. This constructor is automatically invoked by JOSM to bootstrap the plugin.
     *
     * @param pluginInfo the {@code PluginInformation} object
     */
    public OpenStreetViewPlugin(final PluginInformation pluginInfo) {
        super(pluginInfo);
    }

    @Override
    public void mapFrameInitialized(final MapFrame oldMapFrame, final MapFrame newMapFrame) {
        if (Main.map != null && !GraphicsEnvironment.isHeadless()) {
            detailsDialog = new OpenStreetViewDetailsDialog();
            detailsDialog.registerLocationObserver(this);
            newMapFrame.addToggleDialog(detailsDialog);
            detailsDialog.getButton().addActionListener(new ToggleButtonActionListener());

            // register listeners
            registerListeners();

            // add layer
            layer = new OpenStreetViewLayer();
            newMapFrame.mapView.getLayerManager().addLayer(layer);
            if (!detailsDialog.getButton().isSelected()) {
                detailsDialog.getButton().doClick();
            }
        }
    }

    private void registerListeners() {
        NavigatableComponent.addZoomChangeListener(this);
        Main.getLayerManager().addLayerChangeListener(this);
        Main.pref.addPreferenceChangeListener(this);
        Main.map.mapView.addMouseListener(this);
    }
    /* implementation of ZoomChangeListener */

    @Override
    public void zoomChanged() {
        if (zoomTimer != null && zoomTimer.isRunning()) {
            zoomTimer.restart();
        } else {
            zoomTimer = new Timer(SEARCH_DELAY, new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent event) {
                    Main.worker.execute(new DataUpdateThread(layer));
                }
            });
            zoomTimer.setRepeats(false);
            zoomTimer.start();
        }
    }


    /* implementation of LayerChangeListener */

    @Override
    public void layerAdded(final LayerAddEvent event) {
        if (event.getAddedLayer() instanceof OpenStreetViewLayer) {
            zoomChanged();
        }
    }

    @Override
    public void layerOrderChanged(final LayerOrderChangeEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void layerRemoving(final LayerRemoveEvent event) {
        if (event.getRemovedLayer() instanceof OpenStreetViewLayer) {
            NavigatableComponent.removeZoomChangeListener(this);
            Main.getLayerManager().removeLayerChangeListener(this);
            Main.map.mapView.removeMouseListener(this);
            Main.map.remove(detailsDialog);
            Main.pref.removePreferenceChangeListener(this);
            layer = null;
            detailsDialog.hideDialog();
        }
    }


    /* Implementation of MouseListener */
    @Override
    public void mouseClicked(final MouseEvent event) {
        if (shouldSelectPhoto() && SwingUtilities.isLeftMouseButton(event)) {
            if (event.getClickCount() == 2) {
                this.selectedPhotoBounds = null;
                selectPhoto(null);
            } else {
                final Photo photo = layer.nearbyPhoto(event.getPoint());
                this.selectedPhotoBounds = Main.map.mapView.getRealBounds();
                if (photo != null) {
                    selectPhoto(photo);
                }
            }
        }
    }

    private void selectPhoto(final Photo photo) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (!detailsDialog.getButton().isSelected()) {
                    detailsDialog.getButton().doClick();
                }
                detailsDialog.updateUI(photo);
                layer.setSelectedPhoto(photo);
                Main.map.repaint();
            }
        });
    }

    private boolean shouldSelectPhoto() {
        final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
        return zoom >= ServiceConfig.getInstance().getPhotoZoom()
                && Main.getLayerManager().getActiveLayer() instanceof OpenStreetViewLayer;
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        // no logic for this action
    }

    @Override
    public void zoomToSelectedPhoto() {
        if (selectedPhotoBounds != null && !selectedPhotoBounds.equals(Main.map.mapView.getRealBounds())
                && layer.getSelectedPhoto() != null) {
            final Circle circle = new Circle(selectedPhotoBounds);
            Main.worker.submit(new Runnable() {

                @Override
                public void run() {
                    final List<Photo> photos = ServiceHandler.getInstance().listNearbyPhotos(circle, null);
                    layer.setPhotos(photos, false);
                    Main.map.mapView.zoomTo(layer.getSelectedPhoto().getLocation());
                }
            });
        }
    }

    @Override
    public void preferenceChanged(final PreferenceChangeEvent event) {
        if (event != null && (event.getNewValue() != null && !event.getNewValue().equals(event.getOldValue()))) {
            if (event.getKey().equals(PreferenceManager.getInstance().getFiltersChangedFlag())) {
                Main.worker.execute(new DataUpdateThread(layer));
            }
        }
    }

    /*
     * Listens to toggle dialog button actions.
     */
    private class ToggleButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getSource() instanceof IconToggleButton) {
                final IconToggleButton btn = (IconToggleButton) event.getSource();
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        if (btn.isSelected()) {
                            detailsDialog.setVisible(true);
                            btn.setSelected(true);
                        } else {
                            detailsDialog.setVisible(false);
                            btn.setSelected(false);
                            btn.setFocusable(false);
                        }
                        if (layer == null) {
                            registerListeners();
                            layer = new OpenStreetViewLayer();
                            Main.map.mapView.getLayerManager().addLayer(layer);
                        }
                    }
                });
            }
        }
    }
}