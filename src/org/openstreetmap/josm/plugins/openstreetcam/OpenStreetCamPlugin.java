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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.Preferences.PreferenceChangeEvent;
import org.openstreetmap.josm.data.Preferences.PreferenceChangedListener;
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
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.OpenStreetCamDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClosestPhotoObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DataTypeChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
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
LocationObserver, SequenceObserver, ClosestPhotoObserver, PreferenceChangedListener, DataTypeChangeObserver {

    /* details dialog associated with this plugin */
    private OpenStreetCamDetailsDialog detailsDialog;

    /* layer associated with this plugin */
    private OpenStreetCamLayer layer;

    private JMenuItem layerActivatorMenuItem;

    private static final int UNSELECT_CLICK_COUNT = 2;
    private static final int SEARCH_DELAY = 500;

    private Timer zoomTimer;
    private boolean isPreferenceListenerRegistered;

    /**
     * Builds a new object. This constructor is automatically invoked by JOSM to bootstrap the plugin.
     *
     * @param pluginInfo the {@code PluginInformation} object
     */
    public OpenStreetCamPlugin(final PluginInformation pluginInfo) {
        super(pluginInfo);
    }

    @Override
    public PreferenceSetting getPreferenceSetting() {
        return new PreferenceEditor();
    }


    @Override
    public void mapFrameInitialized(final MapFrame oldMapFrame, final MapFrame newMapFrame) {
        if (Main.map != null && !GraphicsEnvironment.isHeadless()) {
            initializeDetailsDialog(newMapFrame);
            if (layerActivatorMenuItem == null) {
                layerActivatorMenuItem = MainMenu.add(Main.main.menu.imageryMenu, new LayerActivator(), false);
            }
            layerActivatorMenuItem.setEnabled(true);
            if (PreferenceManager.getInstance().loadLayerOpenedFlag()) {
                addLayer();
            }
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

    private void initializeDetailsDialog(final MapFrame newMapFrame) {
        detailsDialog = OpenStreetCamDetailsDialog.getInstance();
        detailsDialog.registerObservers(this, this, this, this);
        newMapFrame.addToggleDialog(detailsDialog);
        if (PreferenceManager.getInstance().loadPanelOpenedFlag()) {
            detailsDialog.showDialog();
        } else {
            detailsDialog.hideDialog();
        }
        Main.pref.addPreferenceChangeListener(this);
        isPreferenceListenerRegistered = true;
    }


    private void addLayer() {
        // register listeners
        NavigatableComponent.addZoomChangeListener(this);
        Main.getLayerManager().addLayerChangeListener(this);
        Main.map.mapView.addMouseListener(this);

        // add layer
        layer = OpenStreetCamLayer.getInstance();
        Main.map.mapView.getLayerManager().addLayer(layer);
    }


    /* implementation of ZoomChangeListener */

    @Override
    public void zoomChanged() {
        if (zoomTimer != null && zoomTimer.isRunning()) {
            zoomTimer.restart();
        } else {
            if (Main.map != null && Main.map.mapView != null) {
                zoomTimer =
                        new Timer(SEARCH_DELAY, event -> ThreadPool.getInstance().execute(new DataUpdateThread(false)));
                zoomTimer.setRepeats(false);
                zoomTimer.start();
            }
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
            isPreferenceListenerRegistered = false;
            OpenStreetCamLayer.destroyInstance();
            layer = null;
        }
    }


    @Override
    public void update(final DataType dataType) {
        PreferenceManager.getInstance().saveDataType(dataType);
        ThreadPool.getInstance().execute(new DataUpdateThread(true));
    }

    /* Implementation of MouseListener */

    @Override
    public void mouseClicked(final MouseEvent event) {
        if ((Util.zoom(Main.map.mapView.getRealBounds()) >= Config.getInstance().getMapPhotoZoom()
                || (layer.getDataSet() != null && layer.getDataSet().getPhotos() != null))
                && SwingUtilities.isLeftMouseButton(event)) {
            if (event.getClickCount() == UNSELECT_CLICK_COUNT) {
                if (layer.getSelectedPhoto() != null) {
                    selectPhoto(null);
                    layer.selectStartPhotoForClosestAction(null);
                    ThreadPool.getInstance().execute(new DataUpdateThread(true));
                }
            } else {
                final Photo photo = layer.nearbyPhoto(event.getPoint());
                if (photo != null) {
                    handlePhotoSelection(photo);
                    layer.selectStartPhotoForClosestAction(photo);
                }
            }
            if (layer.getClosestPhotos() != null) {
                detailsDialog.enableClosestPhotoButton(!layer.getClosestPhotos().isEmpty());
            }
        }
    }

    private void handlePhotoSelection(final Photo photo) {
        if (PreferenceManager.getInstance().loadPreferenceSettings().getPhotoSettings().isDisplayTrackFlag()
                && !layer.isPhotoPartOfSequence(photo)) {
            loadSequence(photo);
        }
        selectPhoto(photo);
    }

    private void selectPhoto(final Photo photo) {
        if (photo == null) {
            CacheManager.getInstance().removePhotos(layer.getSelectedPhoto().getSequenceId());
            layer.setSelectedSequence(null);
            layer.setSelectedPhoto(null);
            if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                detailsDialog.enableDataSwitchButton(true);
            }
            detailsDialog.enableSequenceActions(false, false);
            detailsDialog.updateUI(null);
        } else {
            SwingUtilities.invokeLater(() -> {
                ThreadPool.getInstance().execute(() -> {
                    final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                    ImageHandler.getInstance().loadPhotos(
                            layer.nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount()));
                });
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
                        detailsDialog.enableSequenceActions(layer.enablePreviousPhotoAction(),
                                layer.enableNextPhotoAction());

                    }
                });
            });
        }
    }

    private void loadSequence(final Photo photo) {
        if (layer.getSelectedPhoto() != null && layer.getSelectedSequence() != null) {
            // clean up old sequence
            SwingUtilities.invokeLater(() -> {
                CacheManager.getInstance().removePhotos(layer.getSelectedPhoto().getSequenceId());
                layer.setSelectedSequence(null);
                detailsDialog.enableSequenceActions(false, false);
                Main.map.repaint();
            });
        }
        ThreadPool.getInstance().execute(() -> {

            final Sequence sequence = ServiceHandler.getInstance().retrieveSequence(photo.getSequenceId());
            if (photo.equals(layer.getSelectedPhoto()) && sequence != null && sequence.hasPhotos()) {
                SwingUtilities.invokeLater(() -> {
                    layer.setSelectedSequence(sequence);
                    detailsDialog.enableSequenceActions(layer.enablePreviousPhotoAction(),
                            layer.enableNextPhotoAction());
                    if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                        detailsDialog.enableDataSwitchButton(false);
                    }
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
            layer.selectStartPhotoForClosestAction(photo);
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
                // clean up previous data
                SwingUtilities.invokeLater(() -> {
                    OpenStreetCamLayer.getInstance().setDataSet(null, false);
                    if (OpenStreetCamLayer.getInstance().getSelectedPhoto() == null) {
                        OpenStreetCamDetailsDialog.getInstance().updateUI(null);
                    }
                    Main.map.repaint();
                });
                ThreadPool.getInstance().execute(new DataUpdateThread(true));
            } else if (prefManager.isHighQualityPhotoFlag(event.getKey())) {
                if (layer.getSelectedPhoto() != null) {
                    selectPhoto(layer.getSelectedPhoto());
                }
            } else if (prefManager.isDisplayTackFlag(event.getKey())) {
                if (event.getNewValue().getValue().equals(Boolean.TRUE.toString()) && layer.getSelectedPhoto() != null
                        && layer.getSelectedSequence() == null) {
                    loadSequence(layer.getSelectedPhoto());
                } else if (layer.getSelectedSequence() != null) {
                    layer.setSelectedSequence(null);
                    detailsDialog.enableDataSwitchButton(false);
                    detailsDialog.enableSequenceActions(false, false);
                    Main.map.repaint();
                }
            } else if (PreferenceManager.getInstance().isPanelIconVisibilityKey(event.getKey())) {
                PreferenceManager.getInstance().savePanelOpenedFlag(event.getNewValue().toString());
            }
        }
    }


    /* implementation of ClosestImageObserver */

    @Override
    public void selectClosestPhoto() {
        handlePhotoSelection(layer.getClosestSelectedPhoto());
    }


    private final class LayerActivator extends JosmAction {

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
                if (!isPreferenceListenerRegistered) {
                    Main.pref.addPreferenceChangeListener(OpenStreetCamPlugin.this);
                    isPreferenceListenerRegistered = true;
                }
                PreferenceManager.getInstance().saveLayerOpenedFlag(true);
            }
        }
    }

}