/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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
import org.openstreetmap.josm.plugins.kartaview.argument.DetectionFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.PhotoSize;
import org.openstreetmap.josm.plugins.kartaview.argument.Projection;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.gui.details.common.DetectionTypeContent;
import org.openstreetmap.josm.plugins.kartaview.gui.details.edge.detection.EdgeDetectionDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.detection.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.layer.EdgeLayer;
import org.openstreetmap.josm.plugins.kartaview.gui.layer.KartaViewLayer;
import org.openstreetmap.josm.plugins.kartaview.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.kartaview.handler.ServiceHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.ZoomActionListener;
import org.openstreetmap.josm.plugins.kartaview.handler.edge.EdgeDataUpdateHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.edge.EdgeSelectionHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.imagery.KartaViewDataUpdateHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.imagery.KartaViewLayerSelectionHandler;
import org.openstreetmap.josm.plugins.kartaview.observer.DetectionChangeObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.LocationObserver;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;
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
public class KartaViewPlugin extends Plugin implements LayerChangeListener, LocationObserver, ZoomChangeListener,
        DetectionChangeObserver {

    private static final int SEARCH_DELAY = 1000;

    private JMenuItem kartaViewLayerActivatorMenuItem;
    private JMenuItem edgeLayerActivatorMenuItem;
    private final KartaViewLayerSelectionHandler selectionHandler;
    private final EdgeSelectionHandler edgeSelectionHandler;
    private final PreferenceChangedHandler preferenceChangedHandler;
    private Timer zoomTimer;
    private boolean kartaViewLayerListenersRegistered = false;
    private boolean edgeLayerListenersRegistered = false;
    private boolean commonListenersRegistered = false;


    /**
     * Builds a new object. This constructor is automatically invoked by JOSM to bootstrap the plugin.
     *
     * @param pluginInfo the {@code PluginInformation} object
     */
    public KartaViewPlugin(final PluginInformation pluginInfo) {
        super(pluginInfo);

        // initialize detection signs
        ThreadPool.getInstance().execute(DetectionTypeContent::getInstance);

        this.selectionHandler = new KartaViewLayerSelectionHandler();
        this.edgeSelectionHandler = new EdgeSelectionHandler();
        this.preferenceChangedHandler = new PreferenceChangedHandler();

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
            initializeEdgeDetectionDetailsDialog(newMapFrame);

            // initialize photo details dialog
            initializePhotoDetailsDialog(newMapFrame);

            // initialize menu items
            initializeMenuItems();

            // initialize KartaView menu item & layer
            kartaViewLayerActivatorMenuItem.setEnabled(true);
            if (PreferenceManager.getInstance().loadKartaViewLayerOpenedFlag()) {
                registerKartaViewLayerListeners();
                addKartaViewLayer();
            }

            // initialize Edge detections menu item & layer
            edgeLayerActivatorMenuItem.setEnabled(true);
            if (PreferenceManager.getInstance().loadEdgeLayerOpenedFlag()) {
                registerEdgeLayerListeners();
                addEdgeLayer();
            }
            if (kartaViewLayerListenersRegistered || edgeLayerListenersRegistered) {
                registerCommonListeners();
            }
        }

        if (oldMapFrame != null && newMapFrame == null) {
            // clean-up
            kartaViewLayerActivatorMenuItem.setEnabled(false);
            edgeLayerActivatorMenuItem.setEnabled(false);
            removeCommonListeners();
            removeEdgeLayerListeners();
            removeKartaViewLayerListeners();
            if (Objects.nonNull(oldMapFrame.getToggleDialog(PhotoDetailsDialog.class))) {
                oldMapFrame.removeToggleDialog(PhotoDetailsDialog.getInstance());
            }
            if (Objects.nonNull(oldMapFrame.getToggleDialog(DetectionDetailsDialog.class))) {
                oldMapFrame.removeToggleDialog(DetectionDetailsDialog.getInstance());
            }
            if (Objects.nonNull(oldMapFrame.getToggleDialog(EdgeDetectionDetailsDialog.class))) {
                oldMapFrame.removeToggleDialog(EdgeDetectionDetailsDialog.getInstance());
            }
            PhotoDetailsDialog.destroyInstance();
            DetectionDetailsDialog.destroyInstance();
            EdgeDetectionDetailsDialog.destroyInstance();
            KartaViewLayer.destroyInstance();
            EdgeLayer.destroyInstance();
            try {
                ThreadPool.getInstance().shutdown();
            } catch (final InterruptedException e) {
                Logging.error("Could not shutdown thread pool.", e);
            }
        }
    }

    private void initializeMenuItems() {
        if (!MainApplication.getMenu().getMenu(GuiConfig.getInstance().getMenuItemPosition()).getText().equals(GuiConfig
                .getInstance().getMenuItemName())) {
            MainApplication.getMenu().addMenu(GuiConfig.getInstance().getMenuItemName(), GuiConfig.getInstance()
                    .getMenuItemName(), GuiConfig.getInstance().getMenuItemMnemonicKey(), GuiConfig.getInstance()
                            .getMenuItemPosition(), GuiConfig.getInstance().getMenuItemHelpTopic());
        }
        if (Objects.isNull(kartaViewLayerActivatorMenuItem)) {
            kartaViewLayerActivatorMenuItem = MainMenu.add(MainApplication.getMenu().getMenu(GuiConfig.getInstance()
                    .getMenuItemPosition()), new LayerActivator());
        }
        if (Objects.isNull(edgeLayerActivatorMenuItem)) {
            edgeLayerActivatorMenuItem = MainMenu.add(MainApplication.getMenu().getMenu(GuiConfig.getInstance()
                    .getMenuItemPosition()), new EdgeLayerActivator());
        }
    }

    private void initializePhotoDetailsDialog(final MapFrame mapFrame) {
        final PhotoDetailsDialog detailsDialog = PhotoDetailsDialog.getInstance();
        detailsDialog.registerObservers(selectionHandler, this, selectionHandler, selectionHandler, selectionHandler,
                selectionHandler);
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

    private void initializeEdgeDetectionDetailsDialog(final MapFrame mapFrame) {
        final EdgeDetectionDetailsDialog edgeDetectionDetailsDialog = EdgeDetectionDetailsDialog.getInstance();
        mapFrame.addToggleDialog(edgeDetectionDetailsDialog, false);
        edgeDetectionDetailsDialog.registerObservers(edgeSelectionHandler);
        if (PreferenceManager.getInstance().loadEdgeDetectionPanelOpenedFlag()) {
            edgeDetectionDetailsDialog.showDialog();
        } else {
            edgeDetectionDetailsDialog.hideDialog();
        }
    }

    /**
     * Registers listeners that need to be registered only if one of the plugin layers is created.
     */
    private void registerKartaViewLayerListeners() {
        if (!kartaViewLayerListenersRegistered) {
            MainApplication.getMap().mapView.addMouseListener(selectionHandler);
            MainApplication.getMap().mapView.addMouseMotionListener(selectionHandler);
            kartaViewLayerListenersRegistered = true;
        }
    }

    /**
     * Registers listeners that need to be registered only if one of the plugin layers is created.
     */
    private void registerEdgeLayerListeners() {
        if (!edgeLayerListenersRegistered) {
            MainApplication.getMap().mapView.addMouseListener(edgeSelectionHandler);
            MainApplication.getMap().mapView.addMouseMotionListener(edgeSelectionHandler);
            edgeLayerListenersRegistered = true;
        }
    }

    /**
     * Registers listeners that need to be registered only if one of the plugin layers is created.
     */
    private void registerCommonListeners() {
        if (!commonListenersRegistered) {
            Preferences.main().addPreferenceChangeListener(preferenceChangedHandler);
            NavigatableComponent.addZoomChangeListener(this);
            MainApplication.getLayerManager().addLayerChangeListener(this);
            commonListenersRegistered = true;
        }
    }


    private void addKartaViewLayer() {
        MainApplication.getMap().mapView.getLayerManager().addLayer(KartaViewLayer.getInstance());
    }

    private void addEdgeLayer() {
        MainApplication.getMap().mapView.getLayerManager().addLayer(EdgeLayer.getInstance());
    }

    /* implementation of LayerChangeListener */

    @Override
    public void layerAdded(final LayerAddEvent event) {
        if (event.getAddedLayer() instanceof KartaViewLayer) {
            PreferenceManager.getInstance().saveKartaViewLayerOpenedFlag(true);
            zoomChanged();
        }
        if (event.getAddedLayer() instanceof EdgeLayer) {
            PreferenceManager.getInstance().saveEdgeLayerOpenedFlag(true);
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
            PhotoDetailsDialog.getInstance().updateUI(null, null, false);
            DetectionDetailsDialog.getInstance().clearDetailsDialog();
            KartaViewLayer.destroyInstance();
            DataSet.getInstance().clearKartaViewLayerData(true);
            if (!MainApplication.getLayerManager().getLayers().contains(EdgeLayer.getInstance())) {
                removeKartaViewLayerListeners();
            }
        }

        if (event.getRemovedLayer() instanceof EdgeLayer) {
            EdgeDetectionDetailsDialog.getInstance().clearDetailsDialog();
            EdgeLayer.destroyInstance();
            DataSet.getInstance().clearEdgeLayerData(true);
            if (!MainApplication.getLayerManager().getLayers().contains(KartaViewLayer.getInstance())) {
                removeEdgeLayerListeners();
            }
        }
    }

    /**
     * Removes the mouse listeners when all layers have been deleted.
     */
    private void removeKartaViewLayerListeners() {
        if (Objects.nonNull(MainApplication.getMap())) {
            if (kartaViewLayerListenersRegistered) {
                MainApplication.getMap().mapView.removeMouseListener(selectionHandler);
                MainApplication.getMap().mapView.removeMouseMotionListener(selectionHandler);
                kartaViewLayerListenersRegistered = false;
            }
        }
    }

    /**
     * Removes the mouse listeners when all layers have been deleted.
     */
    private void removeEdgeLayerListeners() {
        if (Objects.nonNull(MainApplication.getMap())) {
            if (edgeLayerListenersRegistered) {
                MainApplication.getMap().mapView.removeMouseListener(edgeSelectionHandler);
                MainApplication.getMap().mapView.removeMouseMotionListener(edgeSelectionHandler);
                edgeLayerListenersRegistered = false;
            }
        }
    }

    private void removeCommonListeners() {
        if (commonListenersRegistered) {
            Preferences.main().removePreferenceChangeListener(preferenceChangedHandler);
            NavigatableComponent.removeZoomChangeListener(this);
            MainApplication.getLayerManager().removeLayerChangeListener(this);
            commonListenersRegistered = false;
        }
    }

    /* implementation of LocationObserver */

    @Override
    public void zoomToSelectedPhoto() {
        final Photo selectedPhoto = DataSet.getInstance().getSelectedPhoto();
        if (selectedPhoto != null && !MainApplication.getMap().mapView.getRealBounds().contains(selectedPhoto
                .getPoint())) {
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
                zoomTimer = new Timer(SEARCH_DELAY, new ZoomActionListener());
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
            final Detection changedDetection = ServiceHandler.getInstance().retrieveDetection(DataSet.getInstance()
                    .getSelectedDetection().getId());
            SwingUtilities.invokeLater(() -> updateDetection(changedDetection));
        });
    }

    private void updateDetection(final Detection detection) {
        final DetectionFilter filter = PreferenceManager.getInstance().loadSearchFilter().getDetectionFilter();
        if (!DataSet.getInstance().hasSelectedSequence() && (filter != null && !filter.containsEditStatus(detection
                .getEditStatus()))) {
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
     * Activates the KartaView layer.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class LayerActivator extends JosmAction {

        private static final long serialVersionUID = -1361735274900300621L;

        private LayerActivator() {
            super(GuiConfig.getInstance().getPluginShortName(), new ImageProvider(IconConfig.getInstance()
                    .getKartaViewLayerIconName()), null, null, false, null, false);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!MainApplication.getMap().mapView.getLayerManager().containsLayer(KartaViewLayer.getInstance())) {
                registerKartaViewLayerListeners();
                registerCommonListeners();
                addKartaViewLayer();
                PreferenceManager.getInstance().saveKartaViewLayerOpenedFlag(true);
            }
        }
    }


    /**
     * Activates the Edge detections layer.
     *
     * @author maria.mitisor
     */
    private final class EdgeLayerActivator extends JosmAction {

        private static final long serialVersionUID = -5200864370725838383L;

        private EdgeLayerActivator() {
            super(GuiConfig.getInstance().getEdgeDataName(), new ImageProvider(IconConfig.getInstance()
                    .getEdgeLayerIconName()), null, null, false, null, false);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!MainApplication.getMap().mapView.getLayerManager().containsLayer(EdgeLayer.getInstance())) {
                registerEdgeLayerListeners();
                registerCommonListeners();
                addEdgeLayer();
                PreferenceManager.getInstance().saveEdgeLayerOpenedFlag(true);
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
                if (prefManager.kartaViewLayerDataDownloadPreferencesChanged(event.getKey(), newValue)) {
                    handleKartaViewLayerDataDownload();
                } else if (prefManager.edgeLayerDataDownloadPreferencesChanged(event.getKey(), newValue)) {
                    handleEdgeLayerDataDownload();
                } else if (prefManager.hasHighQualityPhotoFlagChanged(event.getKey(), newValue)) {
                    handleHighQualityPhotoSelection();
                } else if (prefManager.isDisplayTrackFlag(event.getKey())) {
                    handleDisplayTrack(newValue);
                } else if (prefManager.isPhotoPanelIconVisibilityKey(event.getKey())) {
                    prefManager.savePhotoPanelOpenedFlag(newValue);
                } else if (prefManager.isDetectionPanelIconVisibilityKey(event.getKey())) {
                    prefManager.saveDetectionPanelOpenedFlag(newValue);
                } else if (prefManager.isEdgeDetectionPanelIconVisibilityKey(event.getKey())) {
                    prefManager.saveEdgeDetectionPanelOpenedFlag(newValue);
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

        private void handleKartaViewLayerDataDownload() {
            ThreadPool.getInstance().execute(() -> new KartaViewDataUpdateHandler().updateData(true));
        }

        private void handleEdgeLayerDataDownload() {
            ThreadPool.getInstance().execute(() -> new EdgeDataUpdateHandler().updateData(true));
        }

        private void handleHighQualityPhotoSelection() {
            final Photo selectedPhoto = DataSet.getInstance().getSelectedPhoto();
            if (selectedPhoto != null) {
                selectionHandler.selectPhoto(selectedPhoto, PhotoSize.HIGH_QUALITY, true);
            }
        }

        private void handleDisplayTrack(final String newValue) {
            final DataSet dataSet = DataSet.getInstance();
            if (newValue.equals(Boolean.TRUE.toString()) && (dataSet.hasSelectedPhoto() || dataSet
                    .hasSelectedDetection())) {
                selectionHandler.loadSequence(dataSet.getSelectedPhoto());
            } else if (dataSet.hasSelectedSequence()) {
                dataSet.setSelectedSequence(null, null);
                selectionHandler.play(AutoplayAction.STOP);
                final PhotoDetailsDialog detailsDialog = PhotoDetailsDialog.getInstance();
                detailsDialog.enableSequenceActions(false, false, null);
                KartaViewLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            }
        }

        /**
         * Updates the PhotoPanel elements according to the selectedPhoto and to the filters set in the preference
         * panel. In case the photo filter is excluded, the selectPhoto value changes according to the corresponding
         * selected detection.
         *
         * It has to be called whenever there is a format change in order to update the text associated with the image.
         */
        private void updatePhotoPanel() {
            Photo selectedPhoto = DataSet.getInstance().getSelectedPhoto();
            if (DataSet.getInstance().getSelectedDetection() != null && selectedPhoto == null) {
                final KartaViewLayerSelectionHandler handler = new KartaViewLayerSelectionHandler();
                selectedPhoto = handler.loadDetectionPhoto(DataSet.getInstance().getSelectedDetection());
            }
            final boolean preferencePanelValue = PreferenceManager.getInstance().loadPhotoSettings()
                    .isDisplayFrontFacingFlag();
            if (selectedPhoto != null && selectedPhoto.getProjectionType().equals(Projection.SPHERE)) {
                final KartaViewLayerSelectionHandler handler = new KartaViewLayerSelectionHandler();
                DataSet.getInstance().setFrontFacingDisplayed(preferencePanelValue);
                PhotoDetailsDialog.getInstance().updateSwitchImageFormatButton(true, preferencePanelValue);
                handler.handleDataSelection(selectedPhoto, DataSet.getInstance().getSelectedDetection(), DataSet
                        .getInstance().getSelectedCluster(), true, false);
            } else if (selectedPhoto == null || !selectedPhoto.getProjectionType().equals(Projection.SPHERE)) {
                PhotoDetailsDialog.getInstance().updateSwitchImageFormatButton(false, preferencePanelValue);
            }
        }
    }
}