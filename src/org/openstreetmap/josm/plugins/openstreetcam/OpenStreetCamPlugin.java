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

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.Preferences.PreferenceChangeEvent;
import org.openstreetmap.josm.data.Preferences.PreferenceChangedListener;
import org.openstreetmap.josm.gui.IconToggleButton;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.NavigatableComponent;
import org.openstreetmap.josm.gui.NavigatableComponent.ZoomChangeListener;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerAddEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerChangeListener;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerOrderChangeEvent;
import org.openstreetmap.josm.gui.layer.LayerManager.LayerRemoveEvent;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.OpenStreetCamDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.ImageProvider;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Defines the main functionality of the OpenStreetCam plugin.
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetCamPlugin extends Plugin implements ZoomChangeListener, LayerChangeListener, MouseListener,
LocationObserver, SequenceObserver, PreferenceChangedListener {

    /* details dialog associated with this plugin */
    private OpenStreetCamDetailsDialog detailsDialog;

    /* layer associated with this plugin */
    private OpenStreetCamLayer layer;

    private JMenuItem layerActivatorMenuItem;

    private static final int UNSELECT_CLICK_COUNT = 2;
    private static final int SEARCH_DELAY = 600;

    private Timer zoomTimer;


    /**
     * Builds a new object. This constructor is automatically invoked by JOSM to bootstrap the plugin.
     *
     * @param pluginInfo the {@code PluginInformation} object
     */
    public OpenStreetCamPlugin(final PluginInformation pluginInfo) {
        super(pluginInfo);
        PreferenceManager.getInstance().saveManualSwitchDataType(null);
    }

    @Override
    public PreferenceSetting getPreferenceSetting() {
        return new PreferenceEditor();
    }


    @Override
    public void mapFrameInitialized(final MapFrame oldMapFrame, final MapFrame newMapFrame) {
        if (Main.map != null && !GraphicsEnvironment.isHeadless()) {
            // create the dialog
            detailsDialog = new OpenStreetCamDetailsDialog();
            detailsDialog.registerObservers(this, this);
            newMapFrame.addToggleDialog(detailsDialog);
            detailsDialog.getButton().addActionListener(new ToggleButtonActionListener());

            // read preferences
            if (PreferenceManager.getInstance().loadLayerOpenedFlag()) {
                addLayer();
            }
            if (PreferenceManager.getInstance().loadPanelOpenedFlag()) {
                detailsDialog.getButton().setSelected(true);
                detailsDialog.showDialog();
            } else {
                detailsDialog.getButton().setSelected(false);
                detailsDialog.hideDialog();
            }
            if (layerActivatorMenuItem == null) {
                layerActivatorMenuItem = MainMenu.add(Main.main.menu.imageryMenu, new LayerActivator(), false);
            }
            layerActivatorMenuItem.setEnabled(true);
        }

        // all layers are deleted (there is no map frame)
        if (oldMapFrame != null && newMapFrame == null) {
            layerActivatorMenuItem.setEnabled(false);
            try {
                ThreadPool.getInstance().shutdown();
            } catch (final InterruptedException e) {
                Main.error(e, "Could not shutdown thead pool.");
            }
        }
    }

    private void addLayer() {
        // register listeners
        NavigatableComponent.addZoomChangeListener(this);
        Main.getLayerManager().addLayerChangeListener(this);
        Main.pref.addPreferenceChangeListener(this);
        Main.map.mapView.addMouseListener(this);

        // add .layer
        layer = new OpenStreetCamLayer();
        Main.map.mapView.getLayerManager().addLayer(layer);
    }


    /* implementation of ZoomChangeListener */

    @Override
    public void zoomChanged() {
        if (zoomTimer != null && zoomTimer.isRunning()) {
            zoomTimer.restart();
        } else {
            zoomTimer = new Timer(SEARCH_DELAY,
                    event -> Main.worker.execute(new DataUpdateThread(layer, detailsDialog, false)));
            zoomTimer.setRepeats(false);
            zoomTimer.start();
        }
    }


    /* implementation of LayerChangeListener */

    @Override
    public void layerAdded(final LayerAddEvent event) {
        if (event.getAddedLayer() instanceof OpenStreetCamLayer) {
            PreferenceManager.getInstance().saveLayerOpenedFlag(true);
            zoomChanged();
        }
    }

    @Override
    public void layerOrderChanged(final LayerOrderChangeEvent event) {
        // OSV plugin does not have any special logic for the case when the layers order change
    }

    @Override
    public void layerRemoving(final LayerRemoveEvent event) {
        if (event.getRemovedLayer() instanceof OpenStreetCamLayer) {
            NavigatableComponent.removeZoomChangeListener(this);
            Main.getLayerManager().removeLayerChangeListener(this);
            Main.map.mapView.removeMouseListener(this);
            Main.pref.removePreferenceChangeListener(this);
            layer = null;
        }
    }


    /* Implementation of MouseListener */

    @Override
    public void mouseClicked(final MouseEvent event) {
        if ((Util.zoom(Main.map.mapView.getRealBounds()) >= PreferenceManager.getInstance().loadMapViewSettings()
                .getPhotoZoom() || (layer.getDataSet() != null && layer.getDataSet().getPhotos() != null))
                && SwingUtilities.isLeftMouseButton(event)) {
            if (event.getClickCount() == UNSELECT_CLICK_COUNT) {
                if (layer.getSelectedPhoto() != null) {
                    selectPhoto(null);
                }
            } else {
                final Photo photo = layer.nearbyPhoto(event.getPoint());
                if (photo != null) {
                    if (PreferenceManager.getInstance().loadPreferenceSettings().getPhotoSettings().isDisplayTrackFlag()
                            && !layer.isPhotoPartOfSequence(photo)) {
                        loadSequence(photo);
                    }
                    selectPhoto(photo);
                }
            }
        }
    }

    private void selectPhoto(final Photo photo) {
        SwingUtilities.invokeLater(() -> {
            if (photo == null) {
                CacheManager.getInstance().removePhotos(layer.getSelectedPhoto().getSequenceId());
                layer.setSelectedSequence(null);
                detailsDialog.enableManualSwitchButton(true);
            } else {
                ThreadPool.getInstance().execute(() -> {
                    final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                    ImageHandler.getInstance().loadPhotos(
                            layer.nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount()));
                });
            }
            layer.setSelectedPhoto(photo);
            Main.map.repaint();
        });

        ThreadPool.getInstance().execute(() -> {
            if (!detailsDialog.getButton().isSelected()) {
                detailsDialog.getButton().doClick();
            }
            detailsDialog.updateUI(photo);
            SwingUtilities.invokeLater(() -> {
                if (layer.getSelectedSequence() != null) {
                    detailsDialog.enableManualSwitchButton(false);
                    detailsDialog.enableSequenceActions(layer.enablePreviousPhotoAction(),
                            layer.enableNextPhotoAction());
                } else {
                    detailsDialog.enableManualSwitchButton(true);
                    detailsDialog.enableSequenceActions(false, false);
                }
            });
        });

    }

    private void loadSequence(final Photo photo) {
        if (layer.getSelectedPhoto() != null && layer.getSelectedSequence() != null) {
            // clean up old sequence
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    CacheManager.getInstance().removePhotos(layer.getSelectedPhoto().getSequenceId());
                    layer.setSelectedSequence(null);
                    detailsDialog.enableSequenceActions(false, false);
                    detailsDialog.enableManualSwitchButton(true);
                    Main.map.repaint();
                }
            });
        }
        ThreadPool.getInstance().execute(() -> {

            final Sequence sequence = ServiceHandler.getInstance().retrieveSequence(photo.getSequenceId());
            if (photo.equals(layer.getSelectedPhoto()) && sequence != null && sequence.hasPhotos()) {
                SwingUtilities.invokeLater(() -> {
                    layer.setSelectedSequence(sequence);
                    detailsDialog.enableManualSwitchButton(false);
                    detailsDialog.enableSequenceActions(layer.enablePreviousPhotoAction(),
                            layer.enableNextPhotoAction());
                    Main.map.repaint();

                });
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
            SwingUtilities.invokeLater(() -> {
                final Photo selectedPhoto = layer.getSelectedPhoto();
                layer.setDataSet(null, false);
                Main.map.mapView.zoomTo(selectedPhoto.getLocation());
                Main.map.repaint();
            });
        }
    }


    /* implementation of SequenceObserver */

    @Override
    public void selectSequencePhoto(final int index) {
        final Photo photo = layer.sequencePhoto(index);
        if (photo != null) {
            selectPhoto(photo);
            SwingUtilities.invokeLater(() -> {
                if (!Main.map.mapView.getRealBounds().contains(photo.getLocation())) {
                    Main.map.mapView.zoomTo(photo.getLocation());
                    Main.map.repaint();
                }
            });

        }
    }


    /* implementation of PreferenceChangedListener */

    @Override
    public void preferenceChanged(final PreferenceChangeEvent event) {
        if (event != null && (event.getNewValue() != null && !event.getNewValue().equals(event.getOldValue()))) {
            final PreferenceManager prefManager = PreferenceManager.getInstance();
            if (prefManager.dataDownloadPreferencesChanged(event.getKey(), event.getNewValue().getValue().toString())) {
                ThreadPool.getInstance().execute(new DataUpdateThread(layer, detailsDialog, true));
            } else if (prefManager.isHighQualityPhotoFlag(event.getKey())) {
                selectPhoto(layer.getSelectedPhoto());
            } else if (prefManager.isDisplayTackFlag(event.getKey())) {
                if (event.getNewValue().getValue().equals(Boolean.TRUE.toString()) && layer.getSelectedPhoto() != null
                        && layer.getSelectedSequence() == null) {
                    loadSequence(layer.getSelectedPhoto());
                } else if (layer.getSelectedSequence() != null) {
                    layer.setSelectedSequence(null);
                    detailsDialog.enableSequenceActions(false, false);
                    Main.map.repaint();
                }
            }
        }
    }

    private class LayerActivator extends JosmAction {

        private static final long serialVersionUID = -1361735274900300621L;

        private LayerActivator() {
            super(GuiConfig.getInstance().getPluginShortName(),
                    new ImageProvider(IconConfig.getInstance().getLayerIconName()), null, null, false, null, false);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (layer == null) {
                addLayer();
                PreferenceManager.getInstance().saveLayerOpenedFlag(true);
            }
        }
    }

    private class ToggleButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getSource() instanceof IconToggleButton) {
                final IconToggleButton btn = (IconToggleButton) event.getSource();
                SwingUtilities.invokeLater(() -> {
                    if (btn.isSelected()) {
                        PreferenceManager.getInstance().savePanelOpenedFlag(true);
                        detailsDialog.setVisible(true);
                        btn.setFocusable(false);
                    } else {
                        PreferenceManager.getInstance().savePanelOpenedFlag(false);
                        detailsDialog.setVisible(false);
                        btn.setFocusable(false);
                    }
                });
            }
        }
    }

}