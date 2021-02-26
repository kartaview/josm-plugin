/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.plugins.kartaview.gui.details.detection.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.details.filter.DetectionTypeContent;
import org.openstreetmap.josm.plugins.kartaview.gui.details.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.layer.KartaViewLayer;
import org.openstreetmap.josm.plugins.kartaview.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.gui.MainApplication;
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
import org.openstreetmap.josm.plugins.kartaview.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.kartaview.argument.MapViewType;
import org.openstreetmap.josm.plugins.kartaview.argument.PhotoSize;
import org.openstreetmap.josm.plugins.kartaview.argument.Projection;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.handler.DataUpdateHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.SelectionHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.ServiceHandler;
import org.openstreetmap.josm.plugins.kartaview.observer.DetectionChangeObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.LocationObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.MapViewTypeChangeObserver;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.DetectionFilter;
import org.openstreetmap.josm.spi.preferences.PreferenceChangeEvent;
import org.openstreetmap.josm.spi.preferences.PreferenceChangedListener;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Logging;
import com.grab.josm.common.thread.ThreadPool;


/**
 * Defines the main functionality of the KartaView plugin.
 *
 * @author Beata
 * @version $Revision$
 */
public class KartaViewPlugin extends Plugin implements MapViewTypeChangeObserver, LayerChangeListener,
LocationObserver, ZoomChangeListener, DetectionChangeObserver {

    private static final int SEARCH_DELAY = 1000;

    private JMenuItem layerActivatorMenuItem;
    private final SelectionHandler selectionHandler;
    private final PreferenceChangedHandler preferenceChangedHandler;
    private Timer zoomTimer;


    /**
     * Builds a new object. This constructor is automatically invoked by JOSM to bootstrap the plugin.
     *
     * @param pluginInfo the {@code PluginInformation} object
     */
    public KartaViewPlugin(final PluginInformation pluginInfo) {
        super(pluginInfo);

        // initialize detection signs
        ThreadPool.getInstance().execute(DetectionTypeContent::getInstance);

        this.selectionHandler = new SelectionHandler();
        this.preferenceChangedHandler = new PreferenceChangedHandler();
        if (layerActivatorMenuItem == null) {
            layerActivatorMenuItem = MainMenu.add(MainApplication.getMenu().imageryMenu, new LayerActivator(), false);
        }
        PreferenceManager.getInstance().savePluginLocalVersion(getPluginInformation().localversion);
        PreferenceManager.getInstance().saveAutoplayStartedFlag(false);
    }

    @Override
    public PreferenceSetting getPreferenceSetting() {
        return new PreferenceEditor();
    }

    @Override
    public void mapFrameInitialized(final MapFrame oldMapFrame, final MapFrame newMapFrame) {
        if (MainApplication.getMap() != null && !GraphicsEnvironment.isHeadless()) {
            // initialize detection details dialog
            initializeDetectionDetailsDialog(newMapFrame);

            // initialize photo details dialog
            initializePhotoDetailsDialog(newMapFrame);

            // initialize layer menu item & layer
            layerActivatorMenuItem.setEnabled(true);
            if (PreferenceManager.getInstance().loadLayerOpenedFlag()) {
                addLayer();
            }
            Preferences.main().addPreferenceChangeListener(preferenceChangedHandler);
        }

        if (oldMapFrame != null && newMapFrame == null) {
            // clean-up
            layerActivatorMenuItem.setEnabled(false);
            Preferences.main().removePreferenceChangeListener(preferenceChangedHandler);
            oldMapFrame.removeToggleDialog(PhotoDetailsDialog.getInstance());
            PhotoDetailsDialog.destroyInstance();
            DetectionDetailsDialog.destroyInstance();
            KartaViewLayer.destroyInstance();
            try {
                ThreadPool.getInstance().shutdown();
            } catch (final InterruptedException e) {
                Logging.error("Could not shutdown thread pool.", e);
            }
        }
    }

    private void initializePhotoDetailsDialog(final MapFrame mapFrame) {
        final PhotoDetailsDialog detailsDialog = PhotoDetailsDialog.getInstance();
        detailsDialog.registerObservers(selectionHandler, this, this, selectionHandler, selectionHandler,
                selectionHandler, selectionHandler);
        mapFrame.addToggleDialog(detailsDialog, false);
        if (PreferenceManager.getInstance().loadPhotoPanelOpenedFlag()) {
            detailsDialog.showDialog();
        } else {
            detailsDialog.hideDialog();
        }
    }

    private void initializeDetectionDetailsDialog(final MapFrame mapFrame) {
        final DetectionDetailsDialog detectionDetailsDialog = DetectionDetailsDialog.getInstance();
        mapFrame.addToggleDialog(detectionDetailsDialog, false);
        detectionDetailsDialog.registerObservers(this, selectionHandler, selectionHandler);
        if (PreferenceManager.getInstance().loadDetectionPanelOpenedFlag()) {
            detectionDetailsDialog.showDialog();
        } else {
            detectionDetailsDialog.hideDialog();
        }
    }

    private void addLayer() {
        // register listeners that needs to be registered only if the layer is created
        NavigatableComponent.addZoomChangeListener(this);
        MainApplication.getLayerManager().addLayerChangeListener(this);
        MainApplication.getMap().mapView.addMouseListener(selectionHandler);
        MainApplication.getMap().mapView.addMouseMotionListener(selectionHandler);

        // add layer
        MainApplication.getMap().mapView.getLayerManager().addLayer(KartaViewLayer.getInstance());
    }


    /* implementation of DataTypeChangeObserver */

    @Override
    public void update(final MapViewType mapViewType) {
        PreferenceManager.getInstance().saveMapViewType(mapViewType);
        ThreadPool.getInstance().execute(() -> new DataUpdateHandler().updateData(true));
    }


    /* implementation of LayerChangeListener */

    @Override
    public void layerAdded(final LayerAddEvent event) {
        if (event.getAddedLayer() instanceof KartaViewLayer) {
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
        if (event.getRemovedLayer() instanceof KartaViewLayer) {
            NavigatableComponent.removeZoomChangeListener(this);
            MainApplication.getMap().mapView.removeMouseListener(selectionHandler);
            MainApplication.getMap().mapView.removeMouseMotionListener(selectionHandler);
            MainApplication.getLayerManager().removeLayerChangeListener(this);
            PhotoDetailsDialog.getInstance().updateUI(null, null, false);
            DetectionDetailsDialog.getInstance().clearDetailsDialog();
            KartaViewLayer.destroyInstance();
            DataSet.getInstance().clear(true);
        }
    }


    /* implementation of LocationObserver */

    @Override
    public void zoomToSelectedPhoto() {
        final Photo selectedPhoto = DataSet.getInstance().getSelectedPhoto();
        if (selectedPhoto != null
                && !MainApplication.getMap().mapView.getRealBounds().contains(selectedPhoto.getPoint())) {
            SwingUtilities.invokeLater(() -> {
                MainApplication.getMap().mapView.zoomTo(selectedPhoto.getPoint());
                KartaViewLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }


    /* implementation of ZoomChangeListener */

    @Override
    public void zoomChanged() {
        if (zoomTimer != null && zoomTimer.isRunning()) {
            zoomTimer.restart();
        } else {
            if (MainApplication.getMap() != null && MainApplication.getMap().mapView != null) {
                zoomTimer = new Timer(SEARCH_DELAY,
                        event -> ThreadPool.getInstance().execute(() -> new DataUpdateHandler().updateData(false)));
                zoomTimer.setRepeats(false);
                zoomTimer.start();
            }
        }
    }


    /* implementation of DetectionChangeObserver */

    @Override
    public void editDetection(final EditStatus editStatus, final String text) {
        ThreadPool.getInstance().execute(() -> {
            ServiceHandler.getInstance().updateDetection(DataSet.getInstance().getSelectedDetection().getId(),
                    editStatus, text);
            final Detection changedDetection = ServiceHandler.getInstance()
                    .retrieveDetection(DataSet.getInstance().getSelectedDetection().getId());
            SwingUtilities.invokeLater(() -> updateDetection(changedDetection));
        });
    }

    private void updateDetection(final Detection detection) {
        final DetectionFilter filter = PreferenceManager.getInstance().loadSearchFilter().getDetectionFilter();
        if (!DataSet.getInstance().hasSelectedSequence()
                && (filter != null && !filter.containsEditStatus(detection.getEditStatus()))) {
            // remove detection
            DataSet.getInstance().removeDetection(detection);
            PhotoDetailsDialog.getInstance().removePhotoDetection(detection);
            KartaViewLayer.getInstance().invalidate();
            MainApplication.getMap().repaint();
            DataSet.getInstance().updateSelectedDetection(null);
            DetectionDetailsDialog.getInstance().updateDetectionDetails(null);
        } else {
            // update detection
            DataSet.getInstance().updateSelectedDetection(detection);
            DetectionDetailsDialog.getInstance().updateDetectionDetails(detection);
            KartaViewLayer.getInstance().invalidate();
            MainApplication.getMap().repaint();
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
            if (!MainApplication.getMap().mapView.getLayerManager().containsLayer(KartaViewLayer.getInstance())) {
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
                if (prefManager.dataDownloadPreferencesChanged(event.getKey(), newValue)) {
                    handleDataDownload();
                } else if (prefManager.hasHighQualityPhotoFlagChanged(event.getKey(), newValue)) {
                    handleHighQualityPhotoSelection();
                } else if (prefManager.isDisplayTrackFlag(event.getKey())) {
                    handleDisplayTrack(newValue);
                } else if (prefManager.isPhotoPanelIconVisibilityKey(event.getKey())) {
                    prefManager.savePhotoPanelOpenedFlag(event.getNewValue().toString());
                } else if (prefManager.isDetectionPanelIconVisibilityKey(event.getKey())) {
                    prefManager.saveDetectionPanelOpenedFlag(event.getNewValue().toString());
                } else if (prefManager.isMouseHoverDelayKey(event.getKey())) {
                    selectionHandler.changeMouseHoverTimerDelay();
                } else if (prefManager.hasMouseHoverFlagChanged(event.getKey(), newValue)) {
                    handleMouseHover();
                } else if (prefManager.isAutoplayDelayKey(event.getKey())) {
                    selectionHandler.changeAutoplayTimerDelay();
                } else if (prefManager.isDisplayDetectionLocationFlag(event.getKey())) {
                    KartaViewLayer.getInstance().invalidate();
                    MainApplication.getMap().repaint();
                } else if (prefManager.hasPhotoFormatFlagChanged(event.getKey())) {
                    updatePhotoPanel();
                }
            }
        }

        private void handleMouseHover() {
            final Photo selectedPhoto = DataSet.getInstance().getSelectedPhoto();
            if (selectedPhoto != null) {
                selectionHandler.selectPhoto(selectedPhoto, PhotoSize.THUMBNAIL, true);
            }
        }

        private void handleDataDownload() {
            ThreadPool.getInstance().execute(() -> new DataUpdateHandler().updateData(true));
        }

        private void handleHighQualityPhotoSelection() {
            final Photo selectedPhoto = DataSet.getInstance().getSelectedPhoto();
            if (selectedPhoto != null) {
                selectionHandler.selectPhoto(selectedPhoto, PhotoSize.HIGH_QUALITY, true);
            }
        }

        private void handleDisplayTrack(final String newValue) {
            final DataSet dataSet = DataSet.getInstance();
            if (newValue.equals(Boolean.TRUE.toString())
                    && (dataSet.hasSelectedPhoto() || dataSet.hasSelectedDetection())) {
                selectionHandler.loadSequence(dataSet.getSelectedPhoto());
            } else if (dataSet.hasSelectedSequence()) {
                dataSet.setSelectedSequence(null);
                selectionHandler.play(AutoplayAction.STOP);
                final PhotoDetailsDialog detailsDialog = PhotoDetailsDialog.getInstance();
                detailsDialog.enableSequenceActions(false, false, null);
                KartaViewLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            }
        }

        /**
         * Updates the PhotoPanel elements according to the selectedPhoto and to the filters set in
         * the preference panel. In case the photo filter is excluded, the selectPhoto value changes according to the
         * corresponding selected detection.
         *
         * It has to be called whenever there is a format change in order to update the text associated with the image.
         */
        private void updatePhotoPanel() {
            Photo selectedPhoto = DataSet.getInstance().getSelectedPhoto();
            if (DataSet.getInstance().getSelectedDetection() != null && selectedPhoto == null) {
                SelectionHandler handler = new SelectionHandler();
                selectedPhoto = handler.loadDetectionPhoto(DataSet.getInstance().getSelectedDetection());
            }
            final boolean preferencePanelValue =
                    PreferenceManager.getInstance().loadPhotoSettings().isDisplayFrontFacingFlag();
            if (selectedPhoto != null && selectedPhoto.getProjectionType().equals(Projection.SPHERE)) {
                final SelectionHandler handler = new SelectionHandler();
                DataSet.getInstance().setFrontFacingDisplayed(preferencePanelValue);
                PhotoDetailsDialog.getInstance().updateSwitchImageFormatButton(true, preferencePanelValue);
                handler.handleDataSelection(selectedPhoto, DataSet.getInstance().getSelectedDetection(),
                        DataSet.getInstance().getSelectedCluster(), true, false);
            } else if (selectedPhoto == null || !selectedPhoto.getProjectionType().equals(Projection.SPHERE)) {
                PhotoDetailsDialog.getInstance().updateSwitchImageFormatButton(false, preferencePanelValue);
            }
        }
    }
}