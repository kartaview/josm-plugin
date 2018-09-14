/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSize;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo.PhotoDetailsDialog;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Handles mouse selection events.
 *
 * @author beataj
 * @version $Revision$
 */
abstract class MouseSelectionHandler extends MouseAdapter {

    /** defines the number of mouse clicks that is considered as an un-select action */
    private static final int UNSELECT_CLICK_COUNT = 2;

    /** timer used for mouse hover events */
    private Timer mouseHoverTimer;


    @Override
    public void mouseClicked(final MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event) && selectionAllowed()) {
            SwingUtilities.invokeLater(() -> {
                if (event.getClickCount() == UNSELECT_CLICK_COUNT) {
                    handleDataUnselection();
                } else {
                    handleDataSelection(event);
                }
            });
        }
    }

    private void handleDataSelection(final MouseEvent event) {
        final Cluster cluster = DataSet.getInstance().nearbyCluster(event.getPoint());
        Detection detection = null;
        Photo photo = null;
        if (cluster != null) {
            enhanceCluster(cluster);
            final Photo clusterPhoto = cluster.getPhotos() != null ? cluster.getPhotos().get(0) : null;
            if (clusterPhoto != null) {
                System.out.println(clusterPhoto.getSequenceId() + " - " + clusterPhoto.getSequenceIndex());
                photo = ServiceHandler.getInstance().retrievePhotoDetails(clusterPhoto.getSequenceId(),
                        clusterPhoto.getSequenceIndex());
                photo.setHeading(clusterPhoto.getHeading());
                enhancePhotoWithDetections(photo);
            }
        } else {
            detection = DataSet.getInstance().nearbyDetection(event.getPoint());
            if (detection != null) {
                photo = loadDetectionPhoto(detection);
                detection = ServiceHandler.getInstance().retrieveDetection(detection.getId());
            } else {
                photo = DataSet.getInstance().nearbyPhoto(event.getPoint());
                if (photo != null) {
                    enhancePhotoWithDetections(photo);
                    detection = photoSelectedDetection(photo);
                }
            }
        }

        if (photo != null || detection != null || cluster != null) {
            handleDataSelection(photo, detection, cluster, true);
        }
    }


    void enhanceCluster(final Cluster selectedCluster) {
        final Cluster cluster = ServiceHandler.getInstance().retrieveClusterDetails(selectedCluster.getId());
        if (cluster.getOsmElement() != null) {
            selectedCluster.setOsmElement(cluster.getOsmElement());
        }
        if (cluster.getPhotos() != null) {
            selectedCluster.setPhotos(cluster.getPhotos());
        }
        if (cluster.getDetections() != null) {
            selectedCluster.setDetections(cluster.getDetections());
        }
    }

    Detection photoSelectedDetection(final Photo photo) {
        Detection detection = null;
        if (photo.getDetections() != null && !photo.getDetections().isEmpty()) {
            detection = ServiceHandler.getInstance().retrieveDetection(photo.getDetections().get(0).getId());
        }
        return detection;
    }

    void enhancePhotoWithDetections(final Photo photo) {
        if (photo != null
                && PreferenceManager.getInstance().loadSearchFilter().getDataTypes().contains(DataType.DETECTION)
                || PreferenceManager.getInstance().loadSearchFilter().getDataTypes().contains(DataType.CLUSTER)) {
            final List<Detection> detections = loadPhotoDetections(photo);
            photo.setDetections(detections);
        }
    }

    private Photo loadDetectionPhoto(final Detection detection) {
        Photo photo = DataSet.getInstance().getPhoto(detection.getSequenceId(), detection.getSequenceIndex());
        if (photo == null) {
            photo = ServiceHandler.getInstance().retrievePhotoDetails(detection.getSequenceId(),
                    detection.getSequenceIndex());
        }
        if (photo != null) {
            final List<Detection> detections = loadPhotoDetections(photo);
            photo.setDetections(detections);
        }
        return photo;
    }

    private List<Detection> loadPhotoDetections(final Photo photo) {
        List<Detection> photoDetections =
                ServiceHandler.getInstance().retrievePhotoDetections(photo.getSequenceId(), photo.getSequenceIndex());
        if (photoDetections != null && DataSet.getInstance().hasDetections()) {
            photoDetections = photoDetections.stream().filter(DataSet.getInstance().getDetections()::contains)
                    .collect(Collectors.toList());
        }
        return photoDetections;
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
            selectPhoto(photo, PhotoSize.THUMBNAIL, true);
            DataSet.getInstance().selectNearbyPhotos(photo);
            if (DataSet.getInstance().hasNearbyPhotos()) {
                PhotoDetailsDialog.getInstance().enableClosestPhotoButton(DataSet.getInstance().hasNearbyPhotos());
            }
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
                || (DataSet.getInstance().hasDetections() || DataSet.getInstance().hasPhotos());
    }


    abstract void handleDataUnselection();

    abstract void handleDataSelection(final Photo photo, final Detection detection, Cluster cluster,
            final boolean displayLoadingMessage);

    /**
     * Highlights the given photo on the map and displays in the left side panel.
     *
     * @param photo a {@code Photo} represents the selected photo
     */
    public abstract void selectPhoto(final Photo photo, final PhotoSize photoType, final boolean displayLoadingMessage);
}