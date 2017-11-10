/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSize;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.DetectionDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.OpenStreetCamDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClosestPhotoObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.TrackAutoplayObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Handles operations associated with OpenStreetCam data selection.
 *
 * @author beataj
 * @version $Revision$
 */
final class SelectionHandler extends MouseAdapter
implements ClosestPhotoObserver, SequenceObserver, TrackAutoplayObserver {

    /** defines the number of mouse clicks that is considered as an un-select action */
    private static final int UNSELECT_CLICK_COUNT = 2;

    /** timer used for mouse hover events */
    private Timer mouseHoverTimer;

    /** timer used for track auto-play events */
    private Timer autoplayTimer;

    /** used for computing the distance from the auto-play action start photo and next photo */
    private double autoplayDistance = 0.0;


    SelectionHandler() {}


    @Override
    public void mouseClicked(final MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event) && selectionAllowed()) {
            if (event.getClickCount() == UNSELECT_CLICK_COUNT) {
                handleUnSelection();
            } else {
                final Detection detection = OpenStreetCamLayer.getInstance().nearbyDetection(event.getPoint());
                if (detection != null) {

                    handleDetectionSelection(detection);
                } else {
                    final Photo photo = OpenStreetCamLayer.getInstance().nearbyPhoto(event.getPoint());
                    handlePhotoSelection(photo);
                }
            }
            if (OpenStreetCamLayer.getInstance().getClosestPhotos() != null) {
                OpenStreetCamDetailsDialog.getInstance()
                .enableClosestPhotoButton(!OpenStreetCamLayer.getInstance().getClosestPhotos().isEmpty());
            }
        }
    }

    private void handleUnSelection() {
        if (OpenStreetCamLayer.getInstance().getSelectedPhoto() != null) {
            if (autoplayTimer != null) {
                stopAutoplay();
            }
            if (mouseHoverTimer != null) {
                mouseHoverTimer.stop();
                mouseHoverTimer = null;
            }
            selectPhoto(null, null, false);
            OpenStreetCamLayer.getInstance().selectStartPhotoForClosestAction(null);

            ThreadPool.getInstance().execute(() -> new DataUpdateHandler().updateData(true));
        }
        if (OpenStreetCamLayer.getInstance().getSelectedDetection() != null) {
            SwingUtilities.invokeLater(() -> {
                OpenStreetCamLayer.getInstance().setSelectedDetection(null);
                OpenStreetCamLayer.getInstance().invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }

    private void handlePhotoSelection(final Photo photo) {
        if (photo != null) {
            if (autoplayTimer != null && autoplayTimer.isRunning()) {
                stopAutoplay();
            }
            if (shouldLoadSequence(photo)) {
                loadSequence(photo);
            }
            final PhotoSettings photoSettings = PreferenceManager.getInstance().loadPhotoSettings();
            final PhotoSize photoType =
                    photoSettings.isHighQualityFlag() ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
            OpenStreetCamLayer.getInstance().selectStartPhotoForClosestAction(photo);
        }
    }

    private void handleDetectionSelection(final Detection detection) {
        if (detection != null) {
            DetectionDetailsDialog.getInstance().updateDetectionDetails(detection);
            OpenStreetCamLayer.getInstance().setSelectedDetection(detection);

            OpenStreetCamLayer.getInstance().invalidate();
            MainApplication.getMap().repaint();
        }
    }

    /**
     * Verifies if the user is allowed to select a photo location from the map. A photo location can be selected if:
     * <ul>
     * <li>zoom level is at >= minimum map zoom, and</li>
     * <li>layer has available photo locations, and</li>
     * </ul>
     *
     * @return true if the selection is allowed; false otherwise
     */
    private boolean selectionAllowed() {
        return Util.zoom(MainApplication.getMap().mapView.getRealBounds()) >= PreferenceManager.getInstance()
                .loadMapViewSettings().getPhotoZoom()
                || (OpenStreetCamLayer.getInstance().getDataSet() != null
                && OpenStreetCamLayer.getInstance().getDataSet().getPhotos() != null);
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
                && !OpenStreetCamLayer.getInstance().isPhotoPartOfSequence(photo);
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

    void changeMouseHoverTimerDelay() {
        if (mouseHoverTimer != null) {
            mouseHoverTimer.setDelay(PreferenceManager.getInstance().loadPhotoSettings().getMouseHoverDelay());
            if (mouseHoverTimer.isRunning()) {
                mouseHoverTimer.restart();
            }
        }
    }

    private void handleMouseHover(final MouseEvent event) {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final Photo photo = layer.nearbyPhoto(event.getPoint());
        if (photo != null && !photo.equals(layer.getSelectedPhoto())) {
            selectPhoto(photo, PhotoSize.THUMBNAIL, true);
            layer.selectStartPhotoForClosestAction(photo);
            if (layer.getClosestPhotos() != null) {
                OpenStreetCamDetailsDialog.getInstance().enableClosestPhotoButton(!layer.getClosestPhotos().isEmpty());
            }
        }
    }

    /**
     * Highlights the given photo on the map and displays in the left side panel.
     *
     * @param photo a {@code Photo} represents the selected photo
     */
    void selectPhoto(final Photo photo, final PhotoSize photoType, final boolean displayLoadingMessage) {
        if (photo == null) {
            SwingUtilities.invokeLater(() -> handlePhotoUnselection());
        } else {
            SwingUtilities.invokeLater(() -> {
                final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
                final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
                layer.setSelectedPhoto(photo);
                if (!MainApplication.getMap().mapView.getRealBounds().contains(photo.getLocation())) {
                    MainApplication.getMap().mapView.zoomTo(photo.getLocation());
                }
                layer.invalidate();
                MainApplication.getMap().repaint();
                if (!detailsDialog.getButton().isSelected()) {
                    detailsDialog.getButton().doClick();
                }
                detailsDialog.updateUI(photo, photoType, displayLoadingMessage);
                if (layer.getSelectedSequence() != null && (autoplayTimer == null)) {
                    detailsDialog.enableSequenceActions(layer.enablePreviousPhotoAction(),
                            layer.enableNextPhotoAction());
                }
                final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                PhotoHandler.getInstance().loadPhotos(
                        layer.nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount()));
            });
        }
    }

    private void handlePhotoUnselection() {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
        CacheManager.getInstance().removePhotos(layer.getSelectedPhoto().getSequenceId());
        if (layer.getSelectedSequence() != null && layer.getDataSet() != null
                && layer.getDataSet().getPhotos() != null) {
            final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
            if (zoom < PreferenceManager.getInstance().loadMapViewSettings().getPhotoZoom()) {
                layer.setDataSet(null, false);
            }
        }
        layer.setSelectedSequence(null);
        layer.setSelectedPhoto(null);
        if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
            detailsDialog.updateDataSwitchButton(null, true, null);
        }
        detailsDialog.enableSequenceActions(false, false);
        detailsDialog.updateUI(null, null, false);
        layer.invalidate();
        MainApplication.getMap().repaint();
    }

    /**
     * Loads the sequence to which the given photo belongs.
     *
     * @param photo a {@code Photo} represents the selected photo
     */
    void loadSequence(final Photo photo) {
        cleanUpOldSequence();

        ThreadPool.getInstance().execute(() -> {
            final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
            final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
            final Pair<Sequence, List<Detection>> result =
                    ServiceHandler.getInstance().retrieveSequence(photo.getSequenceId(), null);

            // TODO: handle detections
            if (photo.equals(layer.getSelectedPhoto()) && result.getFirst() != null && result.getFirst().hasPhotos()) {
                layer.setSelectedSequence(result);
                detailsDialog.enableSequenceActions(layer.enablePreviousPhotoAction(), layer.enableNextPhotoAction());
                if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                    detailsDialog.updateDataSwitchButton(null, false, null);
                }
                layer.invalidate();
                MainApplication.getMap().repaint();
            }
        });
    }

    private void cleanUpOldSequence() {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        if (layer.getSelectedPhoto() != null && layer.getSelectedSequence() != null) {
            // clean up old sequence
            CacheManager.getInstance().removePhotos(layer.getSelectedPhoto().getSequenceId());
            SwingUtilities.invokeLater(() -> {
                layer.setSelectedSequence(null);
                OpenStreetCamDetailsDialog.getInstance().enableSequenceActions(false, false);
                layer.invalidate();
                MainApplication.getMap().repaint();
            });
        }
    }

    @Override
    public void selectClosestPhoto() {
        final Photo photo = OpenStreetCamLayer.getInstance().closestSelectedPhoto();
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
        final Photo photo = OpenStreetCamLayer.getInstance().sequencePhoto(index);
        if (photo != null) {
            final PhotoSettings photoSettings = PreferenceManager.getInstance().loadPhotoSettings();
            final PhotoSize photoType =
                    photoSettings.isHighQualityFlag() ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
            OpenStreetCamLayer.getInstance().selectStartPhotoForClosestAction(photo);
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
            if (OpenStreetCamLayer.getInstance().getClosestPhotos() != null) {
                OpenStreetCamDetailsDialog.getInstance()
                .enableClosestPhotoButton(!OpenStreetCamLayer.getInstance().getClosestPhotos().isEmpty());
            }
        }
    }


    private void handleTrackAutoplay() {
        final AutoplaySettings autoplaySettings = PreferenceManager.getInstance().loadAutoplaySettings();
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final Photo photo = layer.getSelectedPhoto();
        if (photo != null) {
            Photo nextPhoto = layer.sequencePhoto(photo.getSequenceIndex() + 1);
            if (nextPhoto != null && autoplaySettings.getLength() != null) {
                autoplayDistance += photo.getLocation().greatCircleDistance(nextPhoto.getLocation());
                if (autoplayDistance > autoplaySettings.getLength()) {
                    nextPhoto = null;
                }
            }
            if (layer.getSelectedSequence().getFirst().getPhotos()
                    .get(layer.getSelectedSequence().getFirst().getPhotos().size() - 1).equals(nextPhoto)) {
                handleLastPhotoSelection(nextPhoto);
            } else {
                final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                        ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
                selectPhoto(nextPhoto, photoType, false);
                layer.selectStartPhotoForClosestAction(nextPhoto);
            }
        } else {
            stopAutoplay();
        }
    }

    private void handleLastPhotoSelection(final Photo nextPhoto) {
        final PhotoSize photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                ? PhotoSize.HIGH_QUALITY : PhotoSize.LARGE_THUMBNAIL;
        selectPhoto(nextPhoto, photoType, false);
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        layer.selectStartPhotoForClosestAction(nextPhoto);
        if (layer.getClosestPhotos() != null && !layer.getClosestPhotos().isEmpty()) {
            OpenStreetCamDetailsDialog.getInstance().enableClosestPhotoButton(true);
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

    void changeAutoplayTimerDelay() {
        if (autoplayTimer != null) {
            autoplayTimer.setDelay(PreferenceManager.getInstance().loadAutoplaySettings().getDelay());
            if (autoplayTimer.isRunning()) {
                autoplayTimer.restart();
            }
        }
    }
}