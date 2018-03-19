/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;


import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ImageDataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSize;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClosestPhotoObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.TrackAutoplayObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Handles operations associated with OpenStreetCam data selection.
 *
 * @author beataj
 * @version $Revision$
 */
public final class SelectionHandler extends MouseSelectionHandler
implements ClosestPhotoObserver, SequenceObserver, TrackAutoplayObserver {

    /** timer used for track auto-play events */
    private Timer autoplayTimer;

    /** used for computing the distance from the auto-play action start photo and next photo */
    private double autoplayDistance = 0.0;


    public SelectionHandler() {}


    @Override
    void handleUnSelection() {
        if (DataSet.getInstance().getSelectedPhoto() != null) {
            stopAutoplay();
            selectPhoto(null, null, false);
        }
    }

    @Override
    void handleDataSelection(final Photo photo, final Detection detection) {
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
            handleUnSelection();
        }
        DetectionDetailsDialog.getInstance().updateDetectionDetails(detection);
        DataSet.getInstance().setSelectedDetection(detection);
        OpenStreetCamLayer.getInstance().invalidate();
        MainApplication.getMap().repaint();
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

    /**
     * Highlights the given photo on the map and displays in the left side panel.
     *
     * @param photo a {@code Photo} represents the selected photo
     */
    @Override
    public void selectPhoto(final Photo photo, final PhotoSize photoType, final boolean displayLoadingMessage) {
        if (photo == null) {
            SwingUtilities.invokeLater(() -> handleDataUnselection());
        } else {
            SwingUtilities.invokeLater(() -> {
                final PhotoDetailsDialog detailsDialog = PhotoDetailsDialog.getInstance();
                final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
                DataSet.getInstance().setSelectedPhoto(photo);
                DataSet.getInstance().selectNearbyPhotos(photo);
                if (DataSet.getInstance().hasNearbyPhotos()) {
                    PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
                }
                if (!DataSet.getInstance().hasSelectedDetection()
                        && !MainApplication.getMap().mapView.getRealBounds().contains(photo.getLocation())) {
                    MainApplication.getMap().mapView.zoomTo(photo.getLocation());
                }
                layer.invalidate();
                MainApplication.getMap().repaint();
                if (!detailsDialog.getButton().isSelected()) {
                    detailsDialog.getButton().doClick();
                }
                detailsDialog.updateUI(photo, photoType, displayLoadingMessage);
                if (DataSet.getInstance().hasSelectedSequence() && (autoplayTimer == null)) {
                    detailsDialog.enableSequenceActions(DataSet.getInstance().enablePreviousPhotoAction(),
                            DataSet.getInstance().enableNextPhotoAction());
                }
                final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                ThreadPool.getInstance().execute(() -> PhotoHandler.getInstance().loadPhotos(DataSet.getInstance()
                        .nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount())));
            });
        }
    }

    private void handleDataUnselection() {
        final DataSet dataSet = DataSet.getInstance();
        if (dataSet.hasSelectedPhoto()) {
            CacheManager.getInstance().removePhotos(dataSet.getSelectedPhoto().getSequenceId());
        }

        if (dataSet.hasSelectedSequence() && dataSet.hasItems()
                && Util.zoom(MainApplication.getMap().mapView.getRealBounds()) < PreferenceManager.getInstance()
                .loadMapViewSettings().getPhotoZoom()) {
            // user zoomed out to segment view
            dataSet.cleaHighZoomLevelData();
        }

        dataSet.clearSelection();

        DetectionDetailsDialog.getInstance().updateDetectionDetails(null);
        PhotoDetailsDialog.getInstance().updateUI(null, null, false);
        OpenStreetCamLayer.getInstance().invalidate();
        MainApplication.getMap().repaint();
    }

    /**
     * Loads the sequence to which the given photo belongs.
     *
     * @param photo a {@code Photo} represents the selected photo
     */
    public void loadSequence(final Photo photo) {
        cleanUpOldSequence();

        ThreadPool.getInstance().execute(() -> {
            final DataSet dataSet = DataSet.getInstance();
            final PhotoDetailsDialog detailsDialog = PhotoDetailsDialog.getInstance();
            final Long sequenceId = photo != null ? photo.getSequenceId() : dataSet.getSelectedPhoto().getSequenceId();
            final Sequence result = ServiceHandler.getInstance().retrieveSequence(sequenceId);

            if (photo == null || photo.equals(dataSet.getSelectedPhoto()) && result != null
                    && (result.hasDetections() || result.hasPhotos())) {
                dataSet.setSelectedSequence(result);
                detailsDialog.enableSequenceActions(dataSet.enablePreviousPhotoAction(),
                        dataSet.enableNextPhotoAction());
                if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                    detailsDialog.updateDataSwitchButton(null, false, null);
                }
                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            }
        });
    }


    private void cleanUpOldSequence() {
        final DataSet dataSet = DataSet.getInstance();
        if (dataSet.hasSelectedPhoto() && dataSet.hasSelectedSequence()) {
            // clean up old sequence
            CacheManager.getInstance().removePhotos(dataSet.getSelectedPhoto().getSequenceId());
            SwingUtilities.invokeLater(() -> {
                dataSet.setSelectedSequence(null);
                PhotoDetailsDialog.getInstance().enableSequenceActions(false, false);
                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }

    @Override
    public void selectClosestPhoto() {
        final Photo photo = DataSet.getInstance().nearbyPhoto();
        if (photo != null) {
            if (shouldLoadSequence(photo)) {
                loadSequence(photo);
            }
            final PhotoSettings photoSettings = PreferenceManager.getInstance().loadPhotoSettings();
            final PhotoSize photoType =
                    photoSettings.isHighQualityFlag() ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
        }
    }

    /* implementation of SequenceObserver */

    @Override
    public void selectSequencePhoto(final int index) {
        final Photo photo = DataSet.getInstance().sequencePhoto(index);
        if (photo != null) {
            final List<ImageDataType> dataTypes = PreferenceManager.getInstance().loadSearchFilter().getDataTypes();
            if (dataTypes != null && dataTypes.contains(ImageDataType.DETECTIONS)) {
                final List<Detection> detections = ServiceHandler.getInstance()
                        .retrievePhotoDetections(photo.getSequenceId(), photo.getSequenceIndex());
                photo.setDetections(detections);
                final Detection selectedDetection = detections != null ? detections.get(0) : null;
                DetectionDetailsDialog.getInstance().updateDetectionDetails(selectedDetection);
                DataSet.getInstance().setSelectedDetection(selectedDetection);

                if (selectedDetection != null
                        && !MainApplication.getMap().mapView.getRealBounds().contains(selectedDetection.getPoint())) {
                    MainApplication.getMap().mapView.zoomTo(selectedDetection.getPoint());
                }
            }

            final PhotoSettings photoSettings = PreferenceManager.getInstance().loadPhotoSettings();
            final PhotoSize photoType =
                    photoSettings.isHighQualityFlag() ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
            DataSet.getInstance().selectNearbyPhotos(photo);
        }
    }

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
            if (nextPhoto != null && autoplaySettings.getLength() != null) {
                autoplayDistance += photo.getLocation().greatCircleDistance(nextPhoto.getLocation());
                if (autoplayDistance > autoplaySettings.getLength()) {
                    nextPhoto = null;
                }
            }
            if (DataSet.getInstance().selectedSequenceLastPhoto().equals(nextPhoto)) {
                handleLastPhotoSelection(nextPhoto);
            } else {
                final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                        ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
                selectPhoto(nextPhoto, photoType, false);
                DataSet.getInstance().selectNearbyPhotos(nextPhoto);
            }
        } else {
            stopAutoplay();
        }
    }

    private void handleLastPhotoSelection(final Photo nextPhoto) {
        final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
        selectPhoto(nextPhoto, photoType, false);
        DataSet.getInstance().selectNearbyPhotos(nextPhoto);
        if (DataSet.getInstance().hasNearbyPhotos()) {
            PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
        }
        stopAutoplay();
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
    }

    public void changeAutoplayTimerDelay() {
        if (autoplayTimer != null) {
            autoplayTimer.setDelay(PreferenceManager.getInstance().loadAutoplaySettings().getDelay());
            if (autoplayTimer.isRunning()) {
                autoplayTimer.restart();
            }
        }
    }
}