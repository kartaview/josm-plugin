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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSize;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ClusterBuilder;
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
        if (cluster != null) {
            handleClusterSelection(cluster);
        } else {
            Detection detection;
            Photo photo;
            detection = DataSet.getInstance().nearbyDetection(event.getPoint());
            if (detection != null) {
                photo = loadDetectionPhoto(detection);
                detection = ServiceHandler.getInstance().retrieveDetection(detection.getId());
            } else {
                photo = DataSet.getInstance().nearbyPhoto(event.getPoint());
                if (photo != null) {
                    if (DataSet.getInstance().photoBelongsToSelectedCluster(photo)) {
                        final Optional<Detection> clusterDetection = DataSet.getInstance()
                                .selectedClusterDetection(photo.getSequenceId(), photo.getSequenceIndex());
                        detection = clusterDetection.isPresent() ? clusterDetection.get() : null;
                        photo = enhanceClusterPhoto(photo, detection);
                    } else {
                        enhancePhotoWithDetections(photo);
                        detection = photoSelectedDetection(photo);
                    }
                }
            }
            DataSet.getInstance().selectNearbyPhotos(photo);
            if (photo != null || detection != null) {
                handleDataSelection(photo, detection, null, true);
            }
        }
    }


    Photo enhanceClusterPhoto(final Photo clusterPhoto, final Detection detection) {
        // special case we need the complete Photo object and part of it needs to be loaded from OSC
        final Photo photo = ServiceHandler.getInstance().retrievePhotoDetails(clusterPhoto.getSequenceId(),
                clusterPhoto.getSequenceIndex());
        photo.setHeading(clusterPhoto.getHeading());
        enhancePhotoWithDetections(photo);
        if (detection != null) {
            if (photo.getDetections() == null) {
                photo.setDetections(Collections.singletonList(detection));
            } else if (!photo.getDetections().contains(detection)) {
                photo.getDetections().add(detection);
            }
        }
        return photo;
    }


    private void handleClusterSelection(final Cluster selectedCluster) {
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
        }
        DataSet.getInstance().selectNearbyPhotos(clusterPhoto);
        handleDataSelection(clusterPhoto, clusterDetection, cluster, true);
    }

    private Cluster enhanceCluster(final Cluster selectedCluster) {
        final Cluster cluster = ServiceHandler.getInstance().retrieveClusterDetails(selectedCluster.getId());
        final ClusterBuilder builder = new ClusterBuilder(selectedCluster);
        if (cluster.getOsmElement() != null) {
            builder.osmElement(cluster.getOsmElement());
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


    void enhancePhotoWithDetections(final Photo photo) {
        if (photo != null
                && PreferenceManager.getInstance().loadSearchFilter().getDataTypes().contains(DataType.DETECTION)) {
            final List<Detection> detections = loadPhotoDetections(photo);
            photo.setDetections(detections);
        }
    }

    private Photo loadDetectionPhoto(final Detection detection) {
        final Optional<Photo> dataSetPhoto =
                DataSet.getInstance().detectionPhoto(detection.getSequenceId(), detection.getSequenceIndex());
        Photo photo;
        if (dataSetPhoto.isPresent()) {
            photo = dataSetPhoto.get();
        } else {
            photo = ServiceHandler.getInstance().retrievePhotoDetails(detection.getSequenceId(),
                    detection.getSequenceIndex());
        }
        final List<Detection> detections = DataSet.getInstance().detectionBelongsToSelectedCluster(detection)
                ? Collections.singletonList(detection) : loadPhotoDetections(photo);
                photo.setDetections(detections);
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

    /**
     * Handles the situation when the previously selected data is unselected.
     */
    abstract void handleDataUnselection();

    /**
     * Handles the situation when a new data element is selected from the map.
     *
     * @param photo represents the currently selected photo
     * @param detection represents the currently selected detection
     * @param cluster represents the currently selected cluster
     * @param displayLoadingMessage specifies if a default loading message is displayed or not while the photo is loaded
     */
    abstract void handleDataSelection(final Photo photo, final Detection detection, Cluster cluster,
            final boolean displayLoadingMessage);

    /**
     * Highlights the given photo on the map and displays in the left side panel.
     *
     * @param photo represents the photo to be selected
     * @param photoSize represents the photo size to be displayed
     * @param displayLoadingMessage specifies if a default loading message is displayed or not while the photo is loaded
     */
    public abstract void selectPhoto(final Photo photo, final PhotoSize photoSize, final boolean displayLoadingMessage);
}