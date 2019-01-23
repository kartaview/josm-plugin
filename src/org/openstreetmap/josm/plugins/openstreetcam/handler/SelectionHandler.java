/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSize;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClusterObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionSelectionObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.NearbyPhotoObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.RowSelectionObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceAutoplayObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Handles operations associated with OpenStreetCam data selection.
 *
 * @author beataj
 * @version $Revision$
 */
public final class SelectionHandler extends MouseSelectionHandler implements NearbyPhotoObserver, SequenceObserver,
        SequenceAutoplayObserver, ClusterObserver, DetectionSelectionObserver, RowSelectionObserver {

    /** timer used for track auto-play events */
    private Timer autoplayTimer;

    /** used for computing the distance from the auto-play action start photo and next photo */
    private double autoplayDistance = 0.0;


    public SelectionHandler() {}


    @Override
    void handleDataUnselection() {
        stopAutoplay();
        if (DataSet.getInstance().hasSelectedPhoto()) {
            CacheManager.getInstance().removePhotos(DataSet.getInstance().getSelectedPhoto().getSequenceId());
        }

        if (DataSet.getInstance().hasSelectedSequence() && DataSet.getInstance().hasItems()
                && Util.zoom(MainApplication.getMap().mapView.getRealBounds()) < PreferenceManager.getInstance()
                        .loadMapViewSettings().getPhotoZoom()) {
            // user zoomed out to segment view
            DataSet.getInstance().cleaHighZoomLevelData();
        }

        DataSet.getInstance().clearSelection();
        DetectionDetailsDialog.getInstance().updateDetectionDetails(null);
        PhotoDetailsDialog.getInstance().updateUI(null, null, false);
        OpenStreetCamLayer.getInstance().invalidate();
        MainApplication.getMap().repaint();
    }

    @Override
    void handleDataSelection(final Photo photo, final Detection detection, final Cluster cluster,
            final boolean displayLoadingMessage) {
        if (cluster != null) {
            selectCluster(cluster, detection);
            selectPhoto(photo);
            if (detection != null) {
                // special case
                DataSet.getInstance().setSelectedDetection(detection);
            }
        } else {
            selectPhoto(photo);
            if (!DataSet.getInstance().detectionBelongsToSelectedCluster(detection)) {
                DetectionDetailsDialog.getInstance().updateClusterDetails(null, null);
                if (!DataSet.getInstance().hasSelectedSequence()) {
                    DataSet.getInstance().setSelectedCluster(null);
                }
            } else {
                Cluster selectedCluster = DataSet.getInstance().getSelectedCluster();
                DetectionDetailsDialog.getInstance().updateClusterDetails(selectedCluster, detection);
            }
            selectDetection(detection);
        }
        OpenStreetCamLayer.getInstance().invalidate();
        MainApplication.getMap().repaint();
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
        if (DataSet.getInstance().getSelectedCluster() == null || (DataSet.getInstance().getSelectedSequence() != null
                && !DataSet.getInstance().selectedPhotoBelongsToSelectedCluster())) {
            DetectionDetailsDialog.getInstance().updateDetectionDetails(detection);
        }
        DataSet.getInstance().setSelectedDetection(detection);
        if (detection != null) {
            if (!MainApplication.getMap().mapView.getRealBounds().contains(detection.getPoint())) {
                MainApplication.getMap().mapView.zoomTo(detection.getPoint());
            }
            if (!PhotoDetailsDialog.getInstance().isDialogShowing()) {
                DetectionDetailsDialog.getInstance().expand();
            } else {
                if (DetectionDetailsDialog.getInstance().getButton() != null
                        && !DetectionDetailsDialog.getInstance().getButton().isSelected()) {
                    DetectionDetailsDialog.getInstance().getButton().doClick();
                }
            }
        }
    }

    private void selectCluster(final Cluster cluster, final Detection detection) {
        DetectionDetailsDialog.getInstance().updateClusterDetails(cluster, detection);
        DataSet.getInstance().setSelectedCluster(cluster);
        if (cluster != null) {
            if (!MainApplication.getMap().mapView.getRealBounds().contains(cluster.getPoint())) {
                MainApplication.getMap().mapView.zoomTo(cluster.getPoint());
            }
            if (!PhotoDetailsDialog.getInstance().isDialogShowing()) {
                DetectionDetailsDialog.getInstance().expand();
            } else {
                if (DetectionDetailsDialog.getInstance().getButton() != null
                        && !DetectionDetailsDialog.getInstance().getButton().isSelected()) {
                    DetectionDetailsDialog.getInstance().getButton().doClick();
                }
            }
        }
    }


    /**
     * Verifies if the sequence associated with the given photo should be loaded or not. A sequence should be loaded if:
     * <ul>
     * <li>the user had selected the display track flag from user preference settings, and</li>
     * <li>the sequence to which the photo belongs was not yet loaded</li>
     * </ul>
     *
     * @param photo a {@code Photo} the currently selected photo
     * @return true if the sequence needs to be loaded; false otherwise
     */
    private boolean shouldLoadSequence(final Photo photo) {
        return PreferenceManager.getInstance().loadPreferenceSettings().getTrackSettings().isDisplayTrack()
                && !DataSet.getInstance().isPhotoPartOfSequence(photo);
    }

    @Override
    public synchronized void selectPhoto(final Photo photo, final PhotoSize photoType,
            final boolean displayLoadingMessage) {
        if (photo == null) {
            SwingUtilities.invokeLater(() -> handleDataUnselection());
        } else {
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().setSelectedPhoto(photo);
                if (DataSet.getInstance().hasNearbyPhotos() && (autoplayTimer == null || !autoplayTimer.isRunning())) {
                    PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
                }
                if (!DataSet.getInstance().hasSelectedDetection()
                        && !MainApplication.getMap().mapView.getRealBounds().contains(photo.getPoint())) {
                    MainApplication.getMap().mapView.zoomTo(photo.getPoint());
                }

                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
                if (PhotoDetailsDialog.getInstance().getButton() != null
                        && !PhotoDetailsDialog.getInstance().getButton().isSelected()) {
                    PhotoDetailsDialog.getInstance().getButton().doClick();
                }
                PhotoDetailsDialog.getInstance().updateUI(photo, photoType, displayLoadingMessage);
                if (DataSet.getInstance().hasSelectedSequence() && (autoplayTimer == null)) {
                    PhotoDetailsDialog.getInstance().enableSequenceActions(
                            DataSet.getInstance().enablePreviousPhotoAction(),
                            DataSet.getInstance().enableNextPhotoAction(), null);
                }
                final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                ThreadPool.getInstance().execute(() -> PhotoHandler.getInstance().loadPhotos(DataSet.getInstance()
                        .nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount())));
            });
        }
    }

    /**
     * Loads the sequence to which the given photo belongs.
     *
     * @param photo a {@code Photo} represents the selected photo
     */
    public void loadSequence(final Photo photo) {
        cleanUpOldSequence();

        ThreadPool.getInstance().execute(() -> {
            final Long sequenceId =
                    photo != null ? photo.getSequenceId() : DataSet.getInstance().getSelectedPhoto().getSequenceId();
            final Sequence sequence = ServiceHandler.getInstance().retrieveSequence(sequenceId);

            if (sequence != null && sequence.hasData() && photo.equals(DataSet.getInstance().getSelectedPhoto())) {
                SwingUtilities.invokeLater(() -> {
                    DataSet.getInstance().setSelectedSequence(sequence);
                    PhotoDetailsDialog.getInstance().enableSequenceActions(
                            DataSet.getInstance().enablePreviousPhotoAction(),
                            DataSet.getInstance().enableNextPhotoAction(), null);
                    if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                        PhotoDetailsDialog.getInstance().updateDataSwitchButton(null, false, null);
                    }
                    OpenStreetCamLayer.getInstance().invalidate();
                    MainApplication.getMap().repaint();
                });
            }
        });
    }


    private void cleanUpOldSequence() {
        if (DataSet.getInstance().hasSelectedPhoto() && DataSet.getInstance().hasSelectedSequence()) {
            // clean up old sequence
            CacheManager.getInstance().removePhotos(DataSet.getInstance().getSelectedPhoto().getSequenceId());
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().setSelectedSequence(null);
                PhotoDetailsDialog.getInstance().enableSequenceActions(false, false, null);
                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }


    /* implementation of NearbyPhotoObserver */

    @Override
    public void selectNearbyPhoto() {
        final Photo photo = DataSet.getInstance().nearbyPhoto();
        if (photo != null) {
            if (shouldLoadSequence(photo)) {
                loadSequence(photo);
            }
            enhancePhoto(photo);
            final Detection detection = photoSelectedDetection(photo);
            handleDataSelection(photo, detection, null, true);
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
                if (dataTypes.contains(DataType.DETECTION)) {
                    enhancePhoto(photo);
                    detection = photoSelectedDetection(photo);
                } else if (DataSet.getInstance().getSelectedCluster() != null) {
                    // special case we need to display information of the already selected cluster
                    final Optional<Detection> clusterDetection = DataSet.getInstance()
                            .selectedClusterDetection(photo.getSequenceId(), photo.getSequenceIndex());
                    if (clusterDetection.isPresent()) {
                        detection = clusterDetection.get();
                        photo.setDetections(Collections.singletonList(detection));
                        cluster = DataSet.getInstance().getSelectedCluster();
                    }
                }
            }
            handleDataSelection(photo, detection, cluster, true);
        }
    }


    /* implementation of SequenceAutoplayObserver */
    @Override
    public void play(final AutoplayAction action) {
        if (AutoplayAction.START.equals(action)) {
            PreferenceManager.getInstance().saveAutoplayStartedFlag(true);

            // start autoplay
            if (autoplayTimer != null && autoplayTimer.isRunning()) {
                autoplayTimer.stop();
            } else if (autoplayTimer == null) {
                final AutoplaySettings autoplaySettings =
                        PreferenceManager.getInstance().loadTrackSettings().getAutoplaySettings();
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
    }

    private void handleTrackAutoplay() {
        final AutoplaySettings autoplaySettings = PreferenceManager.getInstance().loadAutoplaySettings();
        final Photo photo = DataSet.getInstance().getSelectedPhoto();
        if (photo != null) {
            Photo nextPhoto = DataSet.getInstance().sequencePhoto(photo.getSequenceIndex() + 1);
            if (nextPhoto != null && !Util.isPointInActiveArea(nextPhoto.getPoint())) {
                nextPhoto = null;
            }
            if (nextPhoto != null && autoplaySettings.getLength() != null) {
                autoplayDistance += photo.getPoint().greatCircleDistance(nextPhoto.getPoint());
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
        if (DataSet.getInstance().selectedSequenceLastPhoto().equals(photo)) {
            selectPhoto(photo, photoType, false);
            if (DataSet.getInstance().hasNearbyPhotos()) {
                PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
            }
            stopAutoplay();
            PhotoDetailsDialog.getInstance().enableSequenceActions(true, false, AutoplayAction.START);
            if (DataSet.getInstance().hasNearbyPhotos()) {
                PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
            }
        } else {
            selectPhoto(photo, photoType, false);
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
            final Optional<Photo> clusterPhoto = DataSet.getInstance()
                    .selectedClusterPhoto(clusterDetection.getSequenceId(), clusterDetection.getSequenceIndex());
            Photo photo = clusterPhoto.isPresent() ? clusterPhoto.get() : null;
            photo = enhanceClusterPhoto(photo, clusterDetection);
            DataSet.getInstance().setSelectedDetection(clusterDetection);
            final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                    ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
            DataSet.getInstance().selectNearbyPhotos(photo);
        }
    }


    @Override
    public void selectPhotoDetection(final Detection selectedDetection) {
        SwingUtilities.invokeLater(() -> {
            final Detection detection = selectedDetection != null
                    ? ServiceHandler.getInstance().retrieveDetection(selectedDetection.getId()) : null;
            selectDetection(detection);
        });
    }


    @Override
    public void selectDetectionFromTable(final Detection detection) {
        Photo photo = null;
   
        if (detection != null) {
            photo = loadDetectionPhoto(detection);
            handleDataSelection(photo, detection, null, true);
        } 
    }
}