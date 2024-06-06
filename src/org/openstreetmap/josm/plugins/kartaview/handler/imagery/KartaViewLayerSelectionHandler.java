/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler.imagery;


import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.ILatLon;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.kartaview.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.kartaview.argument.CacheSettings;
import org.openstreetmap.josm.plugins.kartaview.argument.DataType;
import org.openstreetmap.josm.plugins.kartaview.argument.PhotoSize;
import org.openstreetmap.josm.plugins.kartaview.argument.Projection;
import org.openstreetmap.josm.plugins.kartaview.cache.CacheManager;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.ClusterBuilder;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.entity.Sequence;
import org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.detection.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.kartaview.gui.layer.KartaViewLayer;
import org.openstreetmap.josm.plugins.kartaview.handler.BaseMouseSelectionHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.PhotoHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.ServiceHandler;
import org.openstreetmap.josm.plugins.kartaview.observer.ClusterObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.DetectionSelectionObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.NearbyPhotoObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.RowSelectionObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.SequenceAutoplayObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.SwitchPhotoFormatObserver;
import org.openstreetmap.josm.plugins.kartaview.util.BoundingBoxUtil;
import org.openstreetmap.josm.plugins.kartaview.util.Util;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import com.grab.josm.common.thread.ThreadPool;


/**
 * Handles the operations associated with data selection from the KartaView layer.
 *
 * @author beataj
 * @version $Revision$
 */
public final class KartaViewLayerSelectionHandler extends BaseMouseSelectionHandler implements NearbyPhotoObserver,
        SequenceObserver, SequenceAutoplayObserver, ClusterObserver, DetectionSelectionObserver, RowSelectionObserver,
        SwitchPhotoFormatObserver {

    private Timer autoplayTimer;

    /** Used for computing the distance between the autoplay action start photo and the next photo. */
    private double autoplayDistance = 0.0;

    private Timer mouseHoverTimer;


    public KartaViewLayerSelectionHandler() {
    }

    @Override
    public void handleMapSelection(final Point point) {
        final Cluster cluster = DataSet.getInstance().nearbyCluster(point);
        if (Objects.nonNull(cluster)) {
            ThreadPool.getInstance().execute(() -> handleClusterSelection(cluster));
        } else {
            ThreadPool.getInstance().execute(() -> handleDataSelection(point));
        }
    }

    private void handleDataSelection(final Point point) {
        Detection detection;
        Photo photo;
        detection = DataSet.getInstance().nearbyDetection(point);
        if (detection != null) {
            photo = loadDetectionPhoto(detection);
            detection = ServiceHandler.getInstance().retrieveDetection(detection.getId());
            DataSet.getInstance().setFrontFacingDisplayed(Util.checkFrontFacingDisplay(detection));
        } else {
            photo = DataSet.getInstance().nearbyPhoto(point);
            if (photo != null) {
                enhancePhoto(photo);
                if (DataSet.getInstance().photoBelongsToSelectedCluster(photo)) {
                    final Optional<Detection> clusterDetection = DataSet.getInstance().selectedClusterDetection(photo
                            .getSequenceId(), photo.getSequenceIndex());
                    detection = clusterDetection.isPresent() ? clusterDetection.get() : null;
                    photo = enhanceClusterPhoto(photo, detection);
                    DataSet.getInstance().setFrontFacingDisplayed(Util.checkFrontFacingDisplay(detection));
                } else {
                    updatePhotoFormatDisplayed(photo);
                    enhancePhoto(photo);
                    detection = photoSelectedDetection(photo);
                }
            }
        }
        DataSet.getInstance().selectNearbyPhotos(photo);
        if (photo != null || detection != null) {
            handleDataSelection(photo, detection, null, true, false);
        }
    }

    private void updatePhotoFormatDisplayed(final Photo photo) {
        if (photo.getProjectionType().equals(Projection.SPHERE)) {
            DataSet.getInstance().setFrontFacingDisplayed(PreferenceManager.getInstance().loadPhotoSettings()
                    .isDisplayFrontFacingFlag());
        } else {
            DataSet.getInstance().setFrontFacingDisplayed(true);
        }
    }

    Photo enhanceClusterPhoto(final Photo clusterPhoto, final Detection detection) {
        // special case we need the complete Photo object and part of it needs to be loaded from OSC
        final Photo photo = ServiceHandler.getInstance().retrievePhotoDetails(clusterPhoto.getSequenceId(), clusterPhoto
                .getSequenceIndex());
        if (photo != null) {
            photo.setHeading(clusterPhoto.getHeading());
            enhancePhoto(photo);
            if (detection != null) {
                if (photo.getDetections() == null) {
                    photo.setDetections(Collections.singletonList(detection));
                } else if (!photo.getDetections().contains(detection)) {
                    photo.getDetections().add(detection);
                }
            }
        }
        return photo;
    }

    void handleClusterSelection(final Cluster selectedCluster) {
        final Cluster cluster = enhanceCluster(selectedCluster);

        // select photo belonging to the first detection
        final Detection clusterDetection = cluster.getDetections() != null ? cluster.getDetections().get(0) : null;
        Photo clusterPhoto = null;
        if (clusterDetection != null) {
            final Optional<Photo> photo = DataSet.getInstance().clusterPhoto(cluster, clusterDetection.getSequenceId(),
                    clusterDetection.getSequenceIndex());
            if (photo.isPresent()) {
                clusterPhoto = enhanceClusterPhoto(photo.get(), clusterDetection);
            }
            DataSet.getInstance().setFrontFacingDisplayed(Util.checkFrontFacingDisplay(clusterDetection));
        }
        DataSet.getInstance().selectNearbyPhotos(clusterPhoto);
        handleDataSelection(clusterPhoto, clusterDetection, cluster, true, false);
    }

    private Cluster enhanceCluster(final Cluster selectedCluster) {
        final Cluster cluster = ServiceHandler.getInstance().retrieveClusterDetails(selectedCluster.getId());
        final ClusterBuilder builder = new ClusterBuilder(selectedCluster);
        if (cluster.getOsmElements() != null) {
            builder.osmElements(cluster.getOsmElements());
        }
        if (cluster.getPhotos() != null) {
            builder.photos(cluster.getPhotos());
        }
        if (cluster.getDetections() != null) {
            builder.detections(cluster.getDetections());
        }
        return builder.build();
    }

    Detection photoSelectedDetection(final Photo photo) {
        Detection detection = null;
        if (photo.getDetections() != null && !photo.getDetections().isEmpty()) {
            detection = ServiceHandler.getInstance().retrieveDetection(photo.getDetections().get(0).getId());
        }
        return detection;
    }

    void enhancePhoto(final Photo photo) {
        if (photo != null) {
            if (PreferenceManager.getInstance().loadSearchFilter().getDataTypes().contains(DataType.DETECTION)) {
                final List<Detection> detections = loadPhotoDetections(photo);
                photo.setDetections(detections);
            } else {
                photo.setDetections(null);
            }
            final Photo detailedPhoto = ServiceHandler.getInstance().retrievePhoto(photo.getSequenceId(), photo
                    .getSequenceIndex());
            if (detailedPhoto != null) {
                photo.setMatching(detailedPhoto.getMatching());
                photo.setSize(detailedPhoto.getSize());
                photo.setRealSize(detailedPhoto.getRealSize());
            }
        }
    }

    public Photo loadDetectionPhoto(final Detection detection) {
        final Optional<Photo> dataSetPhoto = DataSet.getInstance().detectionPhoto(detection.getSequenceId(), detection
                .getSequenceIndex());
        Photo photo;
        if (dataSetPhoto.isPresent()) {
            photo = dataSetPhoto.get();
        } else {
            photo = ServiceHandler.getInstance().retrievePhotoDetails(detection.getSequenceId(), detection
                    .getSequenceIndex());
        }
        if (photo != null) {
            enhancePhoto(photo);
        }
        return photo;
    }

    private List<Detection> loadPhotoDetections(final Photo photo) {
        return ServiceHandler.getInstance().retrievePhotoDetections(photo.getSequenceId(), photo.getSequenceIndex());
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        if (PreferenceManager.getInstance().loadPhotoSettings().isMouseHoverFlag() && selectionAllowed()) {
            if (mouseHoverTimer != null && mouseHoverTimer.isRunning()) {
                mouseHoverTimer.restart();
            } else {
                mouseHoverTimer = new Timer(PreferenceManager.getInstance().loadPhotoSettings().getMouseHoverDelay(),
                        event -> ThreadPool.getInstance().execute(() -> handleMouseHover(e)));
                mouseHoverTimer.setRepeats(false);
                mouseHoverTimer.start();
            }
        }
    }

    public void changeMouseHoverTimerDelay() {
        if (mouseHoverTimer != null) {
            mouseHoverTimer.setDelay(PreferenceManager.getInstance().loadPhotoSettings().getMouseHoverDelay());
            if (mouseHoverTimer.isRunning()) {
                mouseHoverTimer.restart();
            }
        }
    }

    private void handleMouseHover(final MouseEvent event) {
        final Photo photo = DataSet.getInstance().nearbyPhoto(event.getPoint());
        if (photo != null && !photo.equals(DataSet.getInstance().getSelectedPhoto())) {
            updatePhotoFormatDisplayed(photo);
            selectPhoto(photo, PhotoSize.THUMBNAIL, true);
            DataSet.getInstance().selectNearbyPhotos(photo);
            if (DataSet.getInstance().hasNearbyPhotos()) {
                PhotoDetailsDialog.getInstance().enableClosestPhotoButton(DataSet.getInstance().hasNearbyPhotos());
            }
        }
    }

    /**
     * Verifies if the user is allowed to select a photo location from the map. A photo location can be selected if
     * the zoom level is larger than or equal to the minimum map zoom, and if the layer has available photo locations.
     *
     * @return true if the selection is allowed; false otherwise
     */
    @Override
    public boolean selectionAllowed() {
        return Util.zoom(MainApplication.getMap().mapView.getRealBounds()) >= PreferenceManager.getInstance()
                .loadMapViewSettings().getPhotoZoom() || (DataSet.getInstance().hasDetections() || DataSet.getInstance()
                        .hasPhotos());
    }

    @Override
    public synchronized void handleMapDataUnselection() {
        handlePhotoUnselection();
        if (DataSet.getInstance().hasSelectedSequence() && DataSet.getInstance().hasItems() && Util.zoom(MainApplication
                .getMap().mapView.getRealBounds()) < PreferenceManager.getInstance().loadMapViewSettings()
                        .getPhotoZoom()) {
            // user zoomed out to segment view
            DataSet.getInstance().cleaHighZoomLevelData();
        }
        DataSet.getInstance().clearKartaViewLayerSelection();
        DetectionDetailsDialog.getInstance().clearDetailsDialog();
        KartaViewLayer.getInstance().invalidate();
        MainApplication.getMap().repaint();
    }

    private void handlePhotoUnselection() {
        stopAutoplay();
        if (Config.getInstance().isCacheEnabled()) {
            if (DataSet.getInstance().hasSelectedPhoto()) {
                CacheManager.getInstance().removePhotos(DataSet.getInstance().getSelectedPhoto().getSequenceId());
            }
        }
        DataSet.getInstance().setSelectedPhoto(null);
        PhotoDetailsDialog.getInstance().updateUI(null, null, false);
    }

    /**
     * Handles the situation when a new data element is selected from the map.
     *
     * @param photo represents the currently selected photo
     * @param detection represents the currently selected detection
     * @param cluster represents the currently selected cluster
     * @param displayLoadingMessage specifies if a default loading message is displayed or not while the photo is loaded
     */
    public void handleDataSelection(final Photo photo, final Detection detection, final Cluster cluster,
            final boolean displayLoadingMessage, final boolean isSwitchAction) {
        DataSet.getInstance().setSwitchPhotoFormatAction(isSwitchAction);
        if (cluster != null) {
            selectCluster(cluster, detection);
            if (detection != null) {
                // special case
                DataSet.getInstance().setSelectedDetection(detection);
                selectDetectionFromTable(detection, false);
            }
        } else {
            if (!DataSet.getInstance().detectionBelongsToSelectedCluster(detection)) {
                DetectionDetailsDialog.getInstance().updateClusterDetails(null, null);
                if (!DataSet.getInstance().hasSelectedSequence()) {
                    DataSet.getInstance().setSelectedCluster(null);
                }
            } else {
                final Cluster selectedCluster = DataSet.getInstance().getSelectedCluster();
                DetectionDetailsDialog.getInstance().updateClusterDetails(selectedCluster, detection);
            }
            selectDetection(detection);
            selectPhoto(photo);
        }
        KartaViewLayer.getInstance().invalidate();
    }

    private void selectPhoto(final Photo photo) {
        if (photo != null) {
            if (autoplayTimer != null && autoplayTimer.isRunning()) {
                stopAutoplay();
            }
            if (shouldLoadSequence(photo)) {
                loadSequence(photo);
            }
            final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                    ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
        } else {
            selectPhoto(null, null, false);
        }
    }

    private void selectDetection(final Detection detection) {
        DataSet.getInstance().setSelectedDetection(detection);
        DataSet.getInstance().setMatchedData(null);
        if (DataSet.getInstance().getSelectedCluster() == null || (DataSet.getInstance().getSelectedSequence() != null
                && !DataSet.getInstance().selectedPhotoBelongsToSelectedCluster())) {
            DetectionDetailsDialog.getInstance().updateDetectionDetails(detection);
        }
        if (detection != null) {
            if (!MainApplication.getMap().mapView.getRealBounds().contains(detection.getPoint())) {
                MainApplication.getMap().mapView.zoomTo(detection.getPoint());
            }
        }
    }

    private void selectCluster(final Cluster cluster, final Detection detection) {
        DataSet.getInstance().setSelectedCluster(cluster);
        DataSet.getInstance().setMatchedData(null);
        DetectionDetailsDialog.getInstance().updateClusterDetails(cluster, detection);
        if (cluster != null) {
            if (!MainApplication.getMap().mapView.getRealBounds().contains(cluster.getPoint())) {
                MainApplication.getMap().mapView.zoomTo(cluster.getPoint());
            }
        }
    }

    /**
     * Verifies if the sequence associated with the given photo should be loaded or not.
     * A sequence should be loaded if the user had selected the display track flag from user preference settings,
     * and the sequence to which the photo belongs was not yet loaded.
     *
     * @param photo a {@code Photo} the currently selected photo
     * @return true if the sequence needs to be loaded; false otherwise
     */
    private boolean shouldLoadSequence(final Photo photo) {
        return PreferenceManager.getInstance().loadPreferenceSettings().getTrackSettings().isDisplayTrack() && !DataSet
                .getInstance().isPhotoPartOfSequence(photo);
    }

    /**
     * Highlights the given photo on the map and displays in the left side panel.
     *
     * @param photo represents the photo to be selected
     * @param photoSize represents the photo size to be displayed
     * @param displayLoadingMessage specifies if a default loading message is displayed or not while the photo is loaded
     */
    public synchronized void selectPhoto(final Photo photo, final PhotoSize photoSize,
            final boolean displayLoadingMessage) {
        if (photo == null) {
            if (DataSet.getInstance().hasSelectedCluster() || DataSet.getInstance().hasSelectedDetection()) {
                // special case the cluster or detection has no photo
                SwingUtilities.invokeLater(this::handlePhotoUnselection);
            } else {
                SwingUtilities.invokeLater(this::handleMapDataUnselection);
            }
        } else {
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().setSelectedPhoto(photo);
                if (DataSet.getInstance().hasNearbyPhotos() && (autoplayTimer == null || !autoplayTimer.isRunning())) {
                    PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
                }
                if (!DataSet.getInstance().hasSelectedDetection() && !MainApplication.getMap().mapView.getRealBounds()
                        .contains(photo.getPoint())) {
                    MainApplication.getMap().mapView.zoomTo(photo.getPoint());
                }

                KartaViewLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
                if (PhotoDetailsDialog.getInstance().getButton() != null && !PhotoDetailsDialog.getInstance()
                        .getButton().isSelected()) {
                    PhotoDetailsDialog.getInstance().getButton().doClick();
                }
                PhotoDetailsDialog.getInstance().updateUI(photo, photoSize, displayLoadingMessage);
                if (DataSet.getInstance().hasSelectedSequence() && (autoplayTimer == null)) {
                    PhotoDetailsDialog.getInstance().enableSequenceActions(DataSet.getInstance()
                            .enablePreviousPhotoAction(), DataSet.getInstance().enableNextPhotoAction(), null);
                }
                if (Config.getInstance().isCacheEnabled()) {
                    final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                    ThreadPool.getInstance().execute(() -> PhotoHandler.getInstance().loadPhotos(DataSet.getInstance()
                            .nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount())));
                }
            });
        }
    }

    /**
     * Loads the sequence to which the given photo belongs.
     *
     * @param photo a {@code Photo} represents the selected photo
     */
    public synchronized void loadSequence(final Photo photo) {
        cleanUpOldSequence();

        ThreadPool.getInstance().execute(() -> {
            if (Objects.nonNull(photo) && !photo.equals(DataSet.getInstance().getSelectedPhoto())) {
                final Bounds bounds = BoundingBoxUtil.currentBounds();
                final Sequence sequence = ServiceHandler.getInstance().retrieveSequence(photo.getSequenceId(),
                        BoundingBoxUtil.boundsToBoundingBox(bounds));

                if (Objects.nonNull(sequence) && sequence.hasData() && Objects.nonNull(photo) && photo.equals(DataSet
                        .getInstance().getSelectedPhoto())) {

                    SwingUtilities.invokeLater(() -> {
                        DataSet.getInstance().setSelectedSequence(sequence, bounds);
                        PhotoDetailsDialog.getInstance().enableSequenceActions(DataSet.getInstance()
                                .enablePreviousPhotoAction(), DataSet.getInstance().enableNextPhotoAction(), null);
                        KartaViewLayer.getInstance().invalidate();
                        MainApplication.getMap().repaint();
                    });
                }
            }
        });
    }

    private void cleanUpOldSequence() {
        if (DataSet.getInstance().hasSelectedPhoto() && DataSet.getInstance().hasSelectedSequence()) {
            // clean up old sequence
            if (Config.getInstance().isCacheEnabled()) {
                CacheManager.getInstance().removePhotos(DataSet.getInstance().getSelectedPhoto().getSequenceId());
            }
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().setSelectedSequence(null, null);
                PhotoDetailsDialog.getInstance().enableSequenceActions(false, false, null);
                KartaViewLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }

    /* implementation of NearbyPhotoObserver */
    @Override
    public void selectNearbyPhoto() {
        final Photo photo = DataSet.getInstance().nearbyPhoto();
        if (photo != null) {
            ThreadPool.getInstance().execute(() -> {
                if (shouldLoadSequence(photo)) {
                    loadSequence(photo);
                }
                enhancePhoto(photo);
                final Detection detection = photoSelectedDetection(photo);
                updatePhotoFormatDisplayed(photo);
                handleDataSelection(photo, detection, null, true, false);

            });
        }
    }

    /* implementation of SequenceObserver */
    @Override
    public void selectSequencePhoto(final int index) {
        final Photo photo = DataSet.getInstance().sequencePhoto(index);
        if (photo != null) {
            final List<DataType> dataTypes = PreferenceManager.getInstance().loadSearchFilter().getDataTypes();
            Detection detection = null;
            Cluster cluster = null;
            if (dataTypes != null) {
                if (!dataTypes.contains(DataType.DETECTION)) {
                    photo.setDetections(null);
                }
                if (dataTypes.contains(DataType.DETECTION)) {
                    enhancePhoto(photo);
                    detection = photoSelectedDetection(photo);
                } else {
                    photo.setDetections(null);
                    if (DataSet.getInstance().getSelectedCluster() != null) {
                        // special case we need to display information of the already selected cluster
                        final Optional<Detection> clusterDetection = DataSet.getInstance().selectedClusterDetection(
                                photo.getSequenceId(), photo.getSequenceIndex());
                        if (clusterDetection.isPresent()) {
                            detection = clusterDetection.get();
                            photo.setDetections(Collections.singletonList(detection));
                            cluster = DataSet.getInstance().getSelectedCluster();
                        }
                    }
                }
            }
            updatePhotoFormatDisplayed(photo);
            handleDataSelection(photo, detection, cluster, true, false);
        }
    }

    /* implementation of SequenceAutoplayObserver */
    @Override
    public void play(final AutoplayAction action) {
        ThreadPool.getInstance().execute(() -> {
            if (AutoplayAction.START.equals(action)) {
                PreferenceManager.getInstance().saveAutoplayStartedFlag(true);

                // start autoplay
                if (autoplayTimer != null && autoplayTimer.isRunning()) {
                    autoplayTimer.stop();
                } else if (autoplayTimer == null) {
                    final AutoplaySettings autoplaySettings = PreferenceManager.getInstance().loadTrackSettings()
                            .getAutoplaySettings();
                    autoplayTimer = new Timer(0, event -> handleTrackAutoplay());
                    autoplayTimer.setDelay(autoplaySettings.getDelay());
                    autoplayTimer.start();
                } else {
                    autoplayTimer.restart();
                }
            } else {
                stopAutoplay();
                if (DataSet.getInstance().hasNearbyPhotos()) {
                    PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
                }
            }
        });
    }

    private void handleTrackAutoplay() {
        final AutoplaySettings autoplaySettings = PreferenceManager.getInstance().loadAutoplaySettings();
        final Photo photo = DataSet.getInstance().getSelectedPhoto();
        if (photo != null) {
            Photo nextPhoto = DataSet.getInstance().sequencePhoto(photo.getSequenceIndex() + 1);
            if (nextPhoto != null && (MainApplication.getLayerManager().getActiveLayer() instanceof OsmDataLayer)
                    && PreferenceManager.getInstance().loadMapViewSettings().isDataLoadFlag() && !Util
                            .isPointInActiveArea(nextPhoto.getPoint())) {
                nextPhoto = null;
            }
            if (nextPhoto != null && autoplaySettings.getLength() != null) {
                autoplayDistance += photo.getPoint().greatCircleDistance((ILatLon) nextPhoto.getPoint());
                if (autoplayDistance > autoplaySettings.getLength()) {
                    nextPhoto = null;
                }
            }
            if (nextPhoto != null) {
                enhancePhoto(nextPhoto);
                final Detection detection = photoSelectedDetection(nextPhoto);
                handleNextPhotoSelection(nextPhoto, detection);
            } else {
                stopAutoplay();
            }
        } else {
            stopAutoplay();
        }
    }

    private void handleNextPhotoSelection(final Photo photo, final Detection detection) {
        DetectionDetailsDialog.getInstance().updateDetectionDetails(detection);
        DataSet.getInstance().setSelectedDetection(detection);
        DataSet.getInstance().selectNearbyPhotos(photo);
        if (detection != null && !MainApplication.getMap().mapView.getRealBounds().contains(detection.getPoint())) {
            MainApplication.getMap().mapView.zoomTo(detection.getPoint());
        }
        final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
        if (DataSet.getInstance().selectedSequenceLastPhoto() != null && DataSet.getInstance()
                .selectedSequenceLastPhoto().equals(photo)) {
            selectPhoto(photo, photoType, true);
            if (DataSet.getInstance().hasNearbyPhotos()) {
                PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
            }
            stopAutoplay();
            PhotoDetailsDialog.getInstance().enableSequenceActions(true, false, AutoplayAction.START);
            if (DataSet.getInstance().hasNearbyPhotos()) {
                PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
            }
        } else {
            selectPhoto(photo, photoType, true);
        }
    }

    private void stopAutoplay() {
        PreferenceManager.getInstance().saveAutoplayStartedFlag(false);
        if (autoplayTimer != null) {
            if (autoplayTimer.isRunning()) {
                autoplayTimer.stop();
            }
            autoplayTimer = null;
        }
        autoplayDistance = 0;
        PhotoDetailsDialog.getInstance().enableSequenceActions(DataSet.getInstance().enablePreviousPhotoAction(),
                DataSet.getInstance().enableNextPhotoAction(), AutoplayAction.START);
        DataSet.getInstance().selectNearbyPhotos(DataSet.getInstance().getSelectedPhoto());
        if (DataSet.getInstance().hasNearbyPhotos()) {
            PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
        }
    }

    public void changeAutoplayTimerDelay() {
        if (autoplayTimer != null) {
            autoplayTimer.setDelay(PreferenceManager.getInstance().loadAutoplaySettings().getDelay());
            if (autoplayTimer.isRunning()) {
                autoplayTimer.restart();
            }
        }
    }

    @Override
    public void selectPhoto(final boolean isNext) {
        final Detection clusterDetection = DataSet.getInstance().clusterDetection(isNext);
        if (clusterDetection != null) {
            ThreadPool.getInstance().execute(() -> {
                final Optional<Photo> clusterPhoto = DataSet.getInstance().selectedClusterPhoto(clusterDetection
                        .getSequenceId(), clusterDetection.getSequenceIndex());
                Photo photo = clusterPhoto.isPresent() ? clusterPhoto.get() : null;
                photo = enhanceClusterPhoto(photo, clusterDetection);
                DataSet.getInstance().setSelectedDetection(clusterDetection);
                final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                        ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
                selectPhoto(photo, photoType, true);
                DetectionDetailsDialog.getInstance().updateClusterDetails(DataSet.getInstance().getSelectedCluster(),
                        clusterDetection);
                DataSet.getInstance().selectNearbyPhotos(photo);
            });
        }
    }

    @Override
    public void selectPhotoDetection(final Detection selectedDetection) {
        DataSet.getInstance().setSelectedDetection(selectedDetection);
        ThreadPool.getInstance().execute(() -> {
            final Detection detection = selectedDetection != null ? ServiceHandler.getInstance().retrieveDetection(
                    selectedDetection.getId()) : null;
            SwingUtilities.invokeLater(() -> selectDetection(detection));
        });
    }

    @Override
    public void selectDetectionFromTable(final Detection detection, final boolean isSelectionMadeInTable) {
        if (detection != null) {
            ThreadPool.getInstance().execute(() -> {
                final Photo photo = loadDetectionPhoto(detection);
                if (isSelectionMadeInTable) {
                    DataSet.getInstance().setFrontFacingDisplayed(Util.checkFrontFacingDisplay(detection));
                    DataSet.getInstance().setSwitchPhotoFormatAction(false);
                }
                final Optional<Photo> clusterPhoto = DataSet.getInstance().selectedClusterPhoto(detection
                        .getSequenceId(), detection.getSequenceIndex());
                if (clusterPhoto.isPresent() && photo != null && clusterPhoto.get().getHeading() != null) {
                    photo.setHeading(clusterPhoto.get().getHeading());
                }
                if (photo != null) {
                    photo.setDetections(Collections.singletonList(detection));
                }
                SwingUtilities.invokeLater(() -> handleDataSelection(photo, detection, null, true, DataSet.getInstance()
                        .isSwitchPhotoFormatAction()));
            });
        }
    }

    @Override
    public void switchPhotoFormat() {
        handleDataSelection(DataSet.getInstance().getSelectedPhoto(), DataSet.getInstance().getSelectedDetection(),
                DataSet.getInstance().getSelectedCluster(), true, true);
    }
}