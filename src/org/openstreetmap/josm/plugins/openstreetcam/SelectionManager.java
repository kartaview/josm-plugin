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
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.OpenStreetCamDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.thread.ThreadPool;


/**
 *
 * @author beataj
 * @version $Revision$
 */
final class SelectionManager extends MouseAdapter {

    private static final int UNSELECT_CLICK_COUNT = 2;
    private Timer mouseHoverTimer;

    SelectionManager() {}


    @Override
    public void mouseClicked(final MouseEvent event) {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
        if ((Util.zoom(Main.map.mapView.getRealBounds()) >= Config.getInstance().getMapPhotoZoom()
                || (OpenStreetCamLayer.getInstance().getDataSet() != null
                && OpenStreetCamLayer.getInstance().getDataSet().getPhotos() != null))
                && SwingUtilities.isLeftMouseButton(event)) {
            if (event.getClickCount() == UNSELECT_CLICK_COUNT) {
                if (layer.getSelectedPhoto() != null) {
                    selectPhoto(null);
                    layer.selectStartPhotoForClosestAction(null);
                    ThreadPool.getInstance().execute(new DataUpdateThread(true));
                }
            } else {
                final Photo photo = layer.nearbyPhoto(event.getPoint());
                if (photo != null) {
                    handlePhotoSelection(photo);
                    layer.selectStartPhotoForClosestAction(photo);
                }
            }
            if (layer.getClosestPhotos() != null) {
                detailsDialog.enableClosestPhotoButton(!layer.getClosestPhotos().isEmpty());
            }
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        if (mouseHoverTimer != null && mouseHoverTimer.isRunning()) {
            mouseHoverTimer.restart();
        } else {
            if (Main.map != null && Main.map.mapView != null) {
                mouseHoverTimer = new Timer(150, event -> ThreadPool.getInstance().execute(new Runnable() {

                    @Override
                    public void run() {
                        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
                        if (layer.getDataSet() != null && layer.getDataSet().getPhotos() != null) {
                            final Photo photo = layer.nearbyPhoto(e.getPoint());
                            if (photo != null) {
                                selectPhoto(photo);
                                System.out.println("mouse moved:" + photo.getThumbnailName());
                            }
                        }
                    }
                }));
                mouseHoverTimer.setRepeats(false);
                mouseHoverTimer.start();
            }
        }

    }

    void selectPhoto(final Photo photo) {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        final OpenStreetCamDetailsDialog detailsDialog = OpenStreetCamDetailsDialog.getInstance();
        if (photo == null) {
            CacheManager.getInstance().removePhotos(layer.getSelectedPhoto().getSequenceId());
            layer.setSelectedSequence(null);
            layer.setSelectedPhoto(null);
            if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                detailsDialog.enableDataSwitchButton(true);
            }
            detailsDialog.enableSequenceActions(false, false);
            detailsDialog.updateUI(null);
        } else {
            SwingUtilities.invokeLater(() -> {
                ThreadPool.getInstance().execute(() -> {
                    final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                    ImageHandler.getInstance().loadPhotos(
                            layer.nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount()));
                });
                layer.setSelectedPhoto(photo);
                Main.map.repaint();
            });
            ThreadPool.getInstance().execute(() -> {
                if (!detailsDialog.getButton().isSelected()) {
                    detailsDialog.getButton().doClick();
                }
                detailsDialog.updateUI(photo);
                SwingUtilities.invokeLater(() -> {
                    if (layer.getSelectedSequence() != null) {
                        detailsDialog.enableSequenceActions(layer.enablePreviousPhotoAction(),
                                layer.enableNextPhotoAction());

                    }
                });
            });
        }
    }

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
                        detailsDialog.enableDataSwitchButton(false);
                    }
                    Main.map.repaint();

                });
            }
        });

    }

    void handlePhotoSelection(final Photo photo) {
        final OpenStreetCamLayer layer = OpenStreetCamLayer.getInstance();
        if (PreferenceManager.getInstance().loadPreferenceSettings().getPhotoSettings().isDisplayTrackFlag()
                && !layer.isPhotoPartOfSequence(photo)) {
            loadSequence(photo);
        }
        selectPhoto(photo);
    }
}