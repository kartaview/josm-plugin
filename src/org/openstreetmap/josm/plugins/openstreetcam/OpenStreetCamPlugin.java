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
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.OpenStreetCamDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DataTypeChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObserver;
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
public class OpenStreetCamPlugin extends Plugin
        implements DataTypeChangeObserver, LayerChangeListener, LocationObserver, ZoomChangeListener {

    private JMenuItem layerActivatorMenuItem;
    private final SelectionHandler selectionHandler;
    private final PreferenceChangedHandler preferenceChangedHandler;

    private static final int SEARCH_DELAY = 500;

    private Timer zoomTimer;


    /**
     * Builds a new object. This constructor is automatically invoked by JOSM to bootstrap the plugin.
     *
     * @param pluginInfo the {@code PluginInformation} object
     */
    public OpenStreetCamPlugin(final PluginInformation pluginInfo) {
        super(pluginInfo);
        this.selectionHandler = new SelectionHandler();
        this.preferenceChangedHandler = new PreferenceChangedHandler();
        if (layerActivatorMenuItem == null) {
            layerActivatorMenuItem = MainMenu.add(Main.main.menu.imageryMenu, new LayerActivator(), false);
        }
    }

    @Override
    public PreferenceSetting getPreferenceSetting() {
        return new PreferenceEditor();
    }

    @Override
    public void mapFrameInitialized(final MapFrame oldMapFrame, final MapFrame newMapFrame) {
        if (Main.map != null && !GraphicsEnvironment.isHeadless()) {
            // initialize details dialog
            final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
            detailsDialog.registerObservers(this, selectionHandler, selectionHandler, this);
            newMapFrame.addToggleDialog(detailsDialog, true);
            if (PreferenceManager.getInstance().loadPanelOpenedFlag()) {
                detailsDialog.showDialog();
            } else {
                detailsDialog.hideDialog();
            }

            // initialize layer menu item & layer
            layerActivatorMenuItem.setEnabled(true);
            if (PreferenceManager.getInstance().loadLayerOpenedFlag()) {
                addLayer();
            }

            Main.pref.addPreferenceChangeListener(preferenceChangedHandler);
        }

        if (oldMapFrame != null && newMapFrame == null) {
            // clean-up
            Main.pref.removePreferenceChangeListener(preferenceChangedHandler);
            layerActivatorMenuItem.setEnabled(false);
            OpenStreetCamDetailsDialog.destroyInstance();
            try {
                ThreadPool.getInstance().shutdown();
            } catch (final InterruptedException e) {
                Main.error(e, "Could not shutdown thead pool.");
            }
        }
    }

    private void addLayer() {
        // register listeners that needs to be registered only if the layer is created
        NavigatableComponent.addZoomChangeListener(this);
        Main.getLayerManager().addLayerChangeListener(this);
        Main.map.mapView.addMouseListener(selectionHandler);
        Main.map.mapView.addMouseMotionListener(selectionHandler);

        // add layer
        Main.map.mapView.getLayerManager().addLayer(OpenStreetCamLayer.getInstance());
    }


    /* implementation of DataTypeChangeObserver */

    @Override
    public void update(final DataType dataType) {
        PreferenceManager.getInstance().saveDataType(dataType);
        ThreadPool.getInstance().execute(new DataUpdateThread(true));
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
            Main.map.mapView.removeMouseListener(selectionHandler);
            Main.map.mapView.removeMouseMotionListener(selectionHandler);
            Main.getLayerManager().removeLayerChangeListener(this);
            OpenStreetCamLayer.destroyInstance();
            OpenStreetCamDetailsDialog.getInstance().updateUI(null);
        }
    }


    /* implementation of LocationObserver */

    @Override
    public void zoomToSelectedPhoto() {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final Photo selectedPhoto = layer.getSelectedPhoto();
        if (selectedPhoto != null && !Main.map.mapView.getRealBounds().contains(selectedPhoto.getLocation())) {
            SwingUtilities.invokeLater(() -> {
                layer.setDataSet(null, false);
                Main.map.mapView.zoomTo(selectedPhoto.getLocation());
                Main.map.repaint();
            });
        }
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

    /**
     * Activates the layer.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class LayerActivator extends JosmAction {

        private static final long serialVersionUID = -1361735274900300621L;

        private LayerActivator() {
            super(GuiConfig.getInstance().getPluginShortName(),
                    new ImageProvider(IconConfig.getInstance().getLayerIconName()), null, null, false, null, false);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!Main.map.mapView.getLayerManager().containsLayer(OpenStreetCamLayer.getInstance())) {
                addLayer();
                PreferenceManager.getInstance().saveLayerOpenedFlag(true);
            }
        }
    }


    /**
     * Handles preference change events.
     *
     * @author beataj
     * @version $Revision$
     */

    private final class PreferenceChangedHandler implements PreferenceChangedListener {

        @Override
        public void preferenceChanged(final PreferenceChangeEvent event) {
            if (event != null && (event.getNewValue() != null && !event.getNewValue().equals(event.getOldValue()))) {
                final PreferenceManager prefManager = PreferenceManager.getInstance();
                final String newValue = event.getNewValue().getValue().toString();
                if (prefManager.hasManualSwitchDataTypeChanged(event.getKey(), newValue)) {
                    handleManualDataSwitch(newValue);
                } else if (prefManager.dataDownloadPreferencesChanged(event.getKey(), newValue)) {
                    handleDataDownload();
                } else if (prefManager.isHighQualityPhotoFlag(event.getKey())) {
                    handleHighQualityPhotoSelection();
                } else if (prefManager.isDisplayTackFlag(event.getKey())) {
                    handleDisplayTrack(newValue);
                } else if (PreferenceManager.getInstance().isPanelIconVisibilityKey(event.getKey())) {
                    PreferenceManager.getInstance().savePanelOpenedFlag(event.getNewValue().toString());
                } else if (PreferenceManager.getInstance().isMouseHoverDelayKey(event.getKey())) {
                    selectionHandler.changeMouseHoverTimerDelay();
                }
            }
        }

        private void handleManualDataSwitch(final String newValue) {
            final boolean manualSwitchFlag = Boolean.parseBoolean(newValue);
            SwingUtilities.invokeLater(() -> {
                OpenStreetCamDetailsDialog.getInstance().updateDataSwitchButton(null, null, manualSwitchFlag);
                OpenStreetCamLayer.getInstance().setDataSet(null, false);
                if (OpenStreetCamLayer.getInstance().getSelectedPhoto() == null) {
                    OpenStreetCamDetailsDialog.getInstance().updateUI(null);
                }
                Main.map.repaint();
            });
            ThreadPool.getInstance().execute(new DataUpdateThread(true));
        }


        private void handleDataDownload() {
            // clean up previous data
            SwingUtilities.invokeLater(() -> {
                OpenStreetCamLayer.getInstance().setDataSet(null, false);
                if (OpenStreetCamLayer.getInstance().getSelectedPhoto() == null) {
                    OpenStreetCamDetailsDialog.getInstance().updateUI(null);
                }
                Main.map.repaint();
            });
            ThreadPool.getInstance().execute(new DataUpdateThread(true));
        }

        private void handleHighQualityPhotoSelection() {
            final Photo selectedPhoto = OpenStreetCamLayer.getInstance().getSelectedPhoto();
            if (selectedPhoto != null) {
                selectionHandler.selectPhoto(selectedPhoto);
            }
        }

        private void handleDisplayTrack(final String newValue) {
            final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
            if (newValue.equals(Boolean.TRUE.toString()) && layer.getSelectedPhoto() != null
                    && layer.getSelectedSequence() == null) {
                selectionHandler.loadSequence(layer.getSelectedPhoto());
            } else if (layer.getSelectedSequence() != null) {
                layer.setSelectedSequence(null);
                final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
                detailsDialog.updateDataSwitchButton(null, false, null);
                detailsDialog.enableSequenceActions(false, false);
                Main.map.repaint();
            }
        }
    }
}