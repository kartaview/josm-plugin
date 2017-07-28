/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplaySettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSettings;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoType;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.OpenStreetCamDetailsDialog;
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
                handlePhotoSelection(event);
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
    }

    private void handlePhotoSelection(final MouseEvent event) {
        final Photo photo = OpenStreetCamLayer.getInstance().nearbyPhoto(event.getPoint());
        if (photo != null) {
            if (autoplayTimer != null && autoplayTimer.isRunning()) {
                stopAutoplay();
            }
            if (shouldLoadSequence(photo)) {
                loadSequence(photo);
            }
            final PhotoSettings photoSettings = PreferenceManager.getInstance().loadPhotoSettings();
            final PhotoType photoType =
                    photoSettings.isHighQualityFlag() ? PhotoType.HIGH_QUALITY : PhotoType.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
            OpenStreetCamLayer.getInstance().selectStartPhotoForClosestAction(photo);
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
        return Util.zoom(Main.map.mapView.getRealBounds()) >= PreferenceManager.getInstance().loadMapViewSettings()
                .getPhotoZoom()
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
            selectPhoto(photo, PhotoType.THUMBNAIL, true);
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
    void selectPhoto(final Photo photo, final PhotoType photoType, final boolean displayLoadingMessage) {
        if (photo == null) {
            SwingUtilities.invokeLater(() -> handlePhotoUnselection());
        } else {
            SwingUtilities.invokeLater(() -> {
                final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
                final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
                layer.setSelectedPhoto(photo);
                if (!Main.map.mapView.getRealBounds().contains(photo.getLocation())) {
                    Main.map.mapView.zoomTo(photo.getLocation());
                }
                layer.invalidate();
                Main.map.repaint();
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
            final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
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
        Main.map.repaint();
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
            final Sequence sequence = ServiceHandler.getInstance().retrieveSequence(photo.getSequenceId());
            if (photo.equals(layer.getSelectedPhoto()) && sequence != null && sequence.hasPhotos()) {
                layer.setSelectedSequence(sequence);
                detailsDialog.enableSequenceActions(layer.enablePreviousPhotoAction(), layer.enableNextPhotoAction());
                if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                    detailsDialog.updateDataSwitchButton(null, false, null);
                }
                layer.invalidate();
                Main.map.repaint();
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
                Main.map.repaint();
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
            final PhotoType photoType =
                    photoSettings.isHighQualityFlag() ? PhotoType.HIGH_QUALITY : PhotoType.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
        }
    }

    /* implementation of SequenceObserver */

    @Override
    public void selectSequencePhoto(final int index) {
        final Photo photo = OpenStreetCamLayer.getInstance().sequencePhoto(index);
        if (photo != null) {
            final PhotoSettings photoSettings = PreferenceManager.getInstance().loadPhotoSettings();
            final PhotoType photoType =
                    photoSettings.isHighQualityFlag() ? PhotoType.HIGH_QUALITY : PhotoType.LARGE_THUMBNAIL;
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
            if (layer.getSelectedSequence().getPhotos().get(layer.getSelectedSequence().getPhotos().size() - 1)
                    .equals(nextPhoto)) {
                handleLastPhotoSelection(nextPhoto);
            } else {
                final PhotoType photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                        ? PhotoType.HIGH_QUALITY : PhotoType.LARGE_THUMBNAIL;
                selectPhoto(nextPhoto, photoType, false);
                layer.selectStartPhotoForClosestAction(nextPhoto);
            }
        } else {
            stopAutoplay();
        }
    }

    private void handleLastPhotoSelection(final Photo nextPhoto) {
        final PhotoType photoType = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                ? PhotoType.HIGH_QUALITY : PhotoType.LARGE_THUMBNAIL;
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
        autoplayTimer.stop();
        autoplayTimer = null;
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