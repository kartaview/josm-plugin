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
import javax.swing.DebugGraphics;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.Main;
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
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetview.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetview.gui.details.OpenStreetViewDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetview.gui.layer.OpenStreetViewLayer;
import org.openstreetmap.josm.plugins.openstreetview.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetview.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetview.util.Util;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.ServiceConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.pref.PreferenceManager;


/**
 * Defines the main functionality of the OpenStreetView plugin.
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetViewPlugin extends Plugin implements ZoomChangeListener, LayerChangeListener, MouseListener,
        LocationObserver, SequenceObserver, PreferenceChangedListener {

    /* details dialog associated with this plugin */
    private OpenStreetViewDetailsDialog detailsDialog;

    /* layer associated with this plugin */
    private OpenStreetViewLayer layer;

    private static final int UNSELECT_CLICK_COUNT = 2;
    private static final int SEARCH_DELAY = 600;

    private Timer zoomTimer;


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
            Main.map.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
            detailsDialog = new OpenStreetViewDetailsDialog();
            detailsDialog.registerObservers(this, this);
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
                    Main.worker.execute(new DataUpdateThread(layer, detailsDialog, false));
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
        // OSV plugin does not have any special logic for the case when the layers order change
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
            detailsDialog.updateUI(null);
        }
    }


    /* Implementation of MouseListener */

    @Override
    public void mouseClicked(final MouseEvent event) {
        if (Util.zoom(Main.map.mapView.getRealBounds()) >= ServiceConfig.getInstance().getPhotoZoom()
                && SwingUtilities.isLeftMouseButton(event)) {
            if (event.getClickCount() == UNSELECT_CLICK_COUNT) {
                if (layer.getSelectedPhoto() != null) {
                    selectPhoto(null);
                }
            } else {
                final Photo photo = layer.nearbyPhoto(event.getPoint());
                if (photo != null) {
                    if (!layer.isPhotoPartOfSequence(photo)) {
                        loadSequence(photo);
                    }
                    selectPhoto(photo);
                }
            }
        }
    }

    private void selectPhoto(final Photo photo) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                layer.setSelectedPhoto(photo);
                detailsDialog.enableSequenceActions(!layer.isSequenceFirstPhoto(), !layer.isSequenceLastPhoto());
                if (photo == null) {
                    layer.setSelectedSequence(null);
                }
                Main.map.repaint();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (!detailsDialog.getButton().isSelected()) {
                    detailsDialog.getButton().doClick();
                }
                detailsDialog.updateUI(photo);
            }
        });
    }

    private void loadSequence(final Photo photo) {
        Main.worker.execute(new Runnable() {

            @Override
            public void run() {
                final Sequence sequence = ServiceHandler.getInstance().retrieveSequence(photo.getSequenceId());
                if (photo.equals(layer.getSelectedPhoto()) && sequence != null && sequence.hasPhotos()) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            layer.setSelectedSequence(sequence);
                            detailsDialog.enableSequenceActions(!layer.isSequenceFirstPhoto(),
                                    !layer.isSequenceLastPhoto());
                            Main.map.repaint();
                        }
                    });
                }
            }
        });
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


    /* implementation of LocationObserver */

    @Override
    public void zoomToSelectedPhoto() {
        if (layer.getSelectedPhoto() != null
                && !Main.map.mapView.getRealBounds().contains(layer.getSelectedPhoto().getLocation())) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final Photo selectedPhoto = layer.getSelectedPhoto();
                    layer.setPhotos(null, false);
                    Main.map.mapView.zoomTo(selectedPhoto.getLocation());
                    Main.map.repaint();
                }
            });
        }
    }


    /* implementation of SequenceObserver */

    @Override
    public void selectSequencePhoto(final int index) {
        final Photo photo = layer.sequencePhoto(index);
        if (photo != null) {
            selectPhoto(photo);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (!Main.map.mapView.getRealBounds().contains(photo.getLocation())) {
                        Main.map.mapView.zoomTo(photo.getLocation());
                        Main.map.repaint();
                    }
                }
            });

        }
    }


    /* implementation of PreferenceChangedListener */

    @Override
    public void preferenceChanged(final PreferenceChangeEvent event) {
        if (event != null && (event.getNewValue() != null && !event.getNewValue().equals(event.getOldValue()))) {
            if (event.getKey().equals(PreferenceManager.getInstance().getFiltersChangedFlagKey())) {
                Main.worker.execute(new DataUpdateThread(layer, detailsDialog, true));
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