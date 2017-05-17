/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
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

    private static final int UNSELECT_CLICK_COUNT = 2;
    private Timer mouseHoverTimer;
    private Timer autoplayTimer;


    SelectionHandler() {}


    @Override
    public void mouseClicked(final MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event) && selectionAllowed()) {
            final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
            if (event.getClickCount() == UNSELECT_CLICK_COUNT) {
                handleUnSelection();
            } else {
                handlePhotoSelection(event);
            }
            if (layer.getClosestPhotos() != null) {
                OpenStreetCamDetailsDialog.getInstance().enableClosestPhotoButton(!layer.getClosestPhotos().isEmpty());
            }
        }
    }

    private void handleUnSelection() {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        if (layer.getSelectedPhoto() != null) {
            autoplayTimer = null;
            mouseHoverTimer = null;
            distance = 0;
            selectPhoto(null, null, false);
            layer.selectStartPhotoForClosestAction(null);
            ThreadPool.getInstance().execute(new DataUpdateThread(true));
        }
    }

    private void handlePhotoSelection(final MouseEvent event) {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final Photo photo = layer.nearbyPhoto(event.getPoint());
        if (photo != null) {
            if (shouldLoadSequence(photo)) {
                loadSequence(photo);
            }
            final PhotoSettings photoSettings = PreferenceManager.getInstance().loadPhotoSettings();
            final PhotoType photoType =
                    photoSettings.isHighQualityFlag() ? PhotoType.HIGH_QUALITY : PhotoType.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
            layer.selectStartPhotoForClosestAction(photo);
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
        return Util.zoom(Main.map.mapView.getRealBounds()) >= Config.getInstance().getMapPhotoZoom()
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
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
        if (photo == null) {
            CacheManager.getInstance().removePhotos(layer.getSelectedPhoto().getSequenceId());
            layer.setSelectedSequence(null);
            layer.setSelectedPhoto(null);
            if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                detailsDialog.updateDataSwitchButton(null, true, null);
            }
            detailsDialog.enableSequenceActions(false, false);
            detailsDialog.updateUI(null, null, false);
            Main.map.repaint();
        } else {
            SwingUtilities.invokeLater(() -> {
                ThreadPool.getInstance().execute(() -> {
                    final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                    ImageHandler.getInstance().loadPhotos(
                            layer.nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount()));
                });
                layer.setSelectedPhoto(photo);
                if (!Main.map.mapView.getRealBounds().contains(photo.getLocation())) {
                    Main.map.mapView.zoomTo(photo.getLocation());
                }
                Main.map.repaint();
            });
            ThreadPool.getInstance().execute(() -> {
                if (!detailsDialog.getButton().isSelected()) {
                    detailsDialog.getButton().doClick();
                }
                detailsDialog.updateUI(photo, photoType, displayLoadingMessage);
                SwingUtilities.invokeLater(() -> {
                    if (layer.getSelectedSequence() != null) {
                        if (autoplayTimer == null || !autoplayTimer.isRunning()) {
                            detailsDialog.enableSequenceActions(layer.enablePreviousPhotoAction(),
                                    layer.enableNextPhotoAction());
                        }
                    }
                });
            });
        }
    }

    /**
     * Loads the sequence to which the given photo belongs.
     *
     * @param photo a {@code Photo} represents the selected photo
     */
    void loadSequence(final Photo photo) {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
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
                        detailsDialog.updateDataSwitchButton(null, false, null);
                    }
                    Main.map.repaint();

                });
            }
        });
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
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final Photo photo = layer.sequencePhoto(index);
        if (photo != null) {
            final PhotoSettings photoSettings = PreferenceManager.getInstance().loadPhotoSettings();
            final PhotoType photoType =
                    photoSettings.isHighQualityFlag() ? PhotoType.HIGH_QUALITY : PhotoType.LARGE_THUMBNAIL;
            selectPhoto(photo, photoType, true);
            layer.selectStartPhotoForClosestAction(photo);
            SwingUtilities.invokeLater(() -> {
                if (!Main.map.mapView.getRealBounds().contains(photo.getLocation())) {
                    Main.map.mapView.zoomTo(photo.getLocation());
                    Main.map.repaint();
                }
            });
        }
    }

    private static double distance = 0;

    @Override
    public void play(final AutoplayAction action) {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        if (AutoplayAction.START.equals(action)) {
            // start autoplay
            if (autoplayTimer != null && autoplayTimer.isRunning()) {
                autoplayTimer.stop();
            } else if (autoplayTimer == null) {
                final AutoplaySettings autoplaySettings =
                        PreferenceManager.getInstance().loadTrackSettings().getAutoplaySettings();
                autoplayTimer = new Timer(0, new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
                        final Photo photo = layer.getSelectedPhoto();
                        final Photo nextPhoto = layer.sequencePhoto(layer.getSelectedPhoto().getSequenceIndex() + 1);
                        if (nextPhoto == null) {
                            // end of sequence
                            autoplayTimer.stop();
                            OpenStreetCamDetailsDialog.getInstance().updateAutoplayButton(AutoplayAction.STOP);
                            distance = 0;
                        } else {
                            distance += photo.getLocation().greatCircleDistance(nextPhoto.getLocation());
                            if ((autoplaySettings.getLength() != null && distance <= autoplaySettings.getLength())
                                    || autoplaySettings.getLength() == null) {
                                final PhotoType photoType =
                                        PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                                        ? PhotoType.HIGH_QUALITY : PhotoType.LARGE_THUMBNAIL;
                                selectPhoto(nextPhoto, photoType, false);
                                layer.selectStartPhotoForClosestAction(photo);
                            } else {
                                autoplayTimer.stop();
                                OpenStreetCamDetailsDialog.getInstance().updateAutoplayButton(AutoplayAction.STOP);
                                distance = 0;
                            }
                        }
                    }
                });

                autoplayTimer.setDelay(autoplaySettings.getDelay());
                autoplayTimer.start();
            } else {
                autoplayTimer.restart();
            }

        } else {
            // stop autoplay
            autoplayTimer.stop();
            if (layer.getClosestPhotos() != null) {
                OpenStreetCamDetailsDialog.getInstance().enableClosestPhotoButton(!layer.getClosestPhotos().isEmpty());
            }
        }
    }
}