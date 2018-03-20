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
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSize;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.observer.NearbyPhotoObserver;
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
        SequenceAutoplayObserver {

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

        if (DataSet.getInstance().hasSelectedSequence() && DataSet.getInstance().hasItems() && Util.zoom(MainApplication
                .getMap().mapView.getRealBounds()) < PreferenceManager.getInstance().loadMapViewSettings()
                        .getPhotoZoom()) {
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
            selectPhoto(null, null, false);
        }
        DetectionDetailsDialog.getInstance().updateDetectionDetails(detection);
        DataSet.getInstance().setSelectedDetection(detection);
        if (detection != null && !MainApplication.getMap().mapView.getRealBounds().contains(detection.getPoint())) {
            MainApplication.getMap().mapView.zoomTo(detection.getPoint());
        }
        DataSet.getInstance().selectNearbyPhotos(photo);
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
        return PreferenceManager.getInstance().loadPreferenceSettings().getTrackSettings().isDisplayTrack() && !DataSet
                .getInstance().isPhotoPartOfSequence(photo);
    }

    @Override
    public void selectPhoto(final Photo photo, final PhotoSize photoType, final boolean displayLoadingMessage) {
        if (photo == null) {
            SwingUtilities.invokeLater(() -> handleDataUnselection());
        } else {
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().setSelectedPhoto(photo);
                if (DataSet.getInstance().hasNearbyPhotos()) {
                    PhotoDetailsDialog.getInstance().enableClosestPhotoButton(true);
                }
                if (!DataSet.getInstance().hasSelectedDetection() && !MainApplication.getMap().mapView.getRealBounds()
                        .contains(photo.getLocation())) {
                    MainApplication.getMap().mapView.zoomTo(photo.getLocation());
                }

                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
                if (!PhotoDetailsDialog.getInstance().getButton().isSelected()) {
                    PhotoDetailsDialog.getInstance().getButton().doClick();
                }
                PhotoDetailsDialog.getInstance().updateUI(photo, photoType, displayLoadingMessage);
                if (DataSet.getInstance().hasSelectedSequence() && (autoplayTimer == null)) {
                    PhotoDetailsDialog.getInstance().enableSequenceActions(DataSet.getInstance()
                            .enablePreviousPhotoAction(), DataSet.getInstance().enableNextPhotoAction());
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
            final Long sequenceId = photo != null ? photo.getSequenceId() : DataSet.getInstance().getSelectedPhoto()
                    .getSequenceId();
            final Sequence sequence = ServiceHandler.getInstance().retrieveSequence(sequenceId);

            if (shouldUpdateUI(photo, sequence)) {
                SwingUtilities.invokeLater(() -> {
                    DataSet.getInstance().setSelectedSequence(sequence);
                    PhotoDetailsDialog.getInstance().enableSequenceActions(DataSet.getInstance()
                            .enablePreviousPhotoAction(), DataSet.getInstance().enableNextPhotoAction());
                    if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                        PhotoDetailsDialog.getInstance().updateDataSwitchButton(null, false, null);
                    }
                    OpenStreetCamLayer.getInstance().invalidate();
                    MainApplication.getMap().repaint();
                });
            }
        });
    }

    private boolean shouldUpdateUI(final Photo photo, final Sequence sequence) {
        return photo == null || photo.equals(DataSet.getInstance().getSelectedPhoto()) && sequence != null && (sequence
                .hasDetections() || sequence.hasPhotos());
    }

    private void cleanUpOldSequence() {
        if (DataSet.getInstance().hasSelectedPhoto() && DataSet.getInstance().hasSelectedSequence()) {
            // clean up old sequence
            CacheManager.getInstance().removePhotos(DataSet.getInstance().getSelectedPhoto().getSequenceId());
            SwingUtilities.invokeLater(() -> {
                DataSet.getInstance().setSelectedSequence(null);
                PhotoDetailsDialog.getInstance().enableSequenceActions(false, false);
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
            final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                    ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
        }
    }


    /* implementation of SequenceObserver */

    @Override
    public void selectSequencePhoto(final int index) {
        final Photo photo = DataSet.getInstance().sequencePhoto(index);
        if (photo != null) {
            final List<ImageDataType> dataTypes = PreferenceManager.getInstance().loadSearchFilter().getDataTypes();
            Detection detection = null;
            if (dataTypes != null && dataTypes.contains(ImageDataType.DETECTIONS)) {
                final List<Detection> detections = loadPhotoDetections(photo);
                photo.setDetections(detections);
                detection = detections != null && !detections.isEmpty() ? detections.get(0) : null;
            }
            handleDataSelection(photo, detection);
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
            }
        } else {
            stopAutoplay();
        }
    }

    private void handleLastPhotoSelection(final Photo nextPhoto) {
        final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
        selectPhoto(nextPhoto, photoType, false);
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