/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.SearchFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.Author;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.ClusterBuilder;
import org.openstreetmap.josm.plugins.kartaview.entity.Contribution;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDataResultSet;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;
import org.openstreetmap.josm.plugins.kartaview.entity.HighZoomResultSet;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.entity.Segment;
import org.openstreetmap.josm.plugins.kartaview.entity.Sequence;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.service.ServiceException;
import org.openstreetmap.josm.plugins.kartaview.util.Util;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import com.grab.josm.common.argument.BoundingBox;


/**
 * Executes the service operations corresponding to user actions. If an operation fails, a corresponding message is
 * displayed to the user.
 *
 * @author Beata
 * @version $Revision$
 */
public final class ServiceHandler extends SearchServiceHandler {

    private static final int CLUSTER_THREAD_POOL_SIZE = 3;
    private static final int SEQUENCE_THREAD_POOL_SIZE = 2;
    private static final int EDGE_CLUSTER_THREAD_POOL_SIZE = 2;

    private static final ServiceHandler INSTANCE = new ServiceHandler();

    public static ServiceHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public HighZoomResultSet searchHighZoomImageryData(final List<BoundingBox> areas, final SearchFilter filter) {
        return filter != null && filter.getDataTypes() != null && !filter.getDataTypes().isEmpty()
                ? super.searchHighZoomImageryData(areas, filter) : new HighZoomResultSet();
    }

    @Override
    public EdgeDataResultSet searchEdgeData(final List<BoundingBox> areas, final EdgeSearchFilter filter) {
        return filter != null && filter.getDataTypes() != null && !filter.getDataTypes().isEmpty()
                ? super.searchEdgeData(areas, filter) : new EdgeDataResultSet();
    }

    /**
     * Retrieves the sequence identified by the given identifier.
     *
     * @param sequenceId the identifier of the sequence
     * @return a {@code Sequence} object
     */
    public Sequence retrieveSequence(final Long sequenceId, final BoundingBox area) {
        final ExecutorService executorService = Executors.newFixedThreadPool(SEQUENCE_THREAD_POOL_SIZE);

        final Future<Sequence> sequenceFuture = executorService.submit(() -> retrieveSequencePhotos(sequenceId));
        final Future<List<Detection>> detectionsFuture = executorService.submit(() -> retrieveSequenceDetections(
                sequenceId, area));

        List<Photo> photos = null;
        try {
            final Sequence sequence = sequenceFuture.get();
            photos = sequence != null ? sequence.getPhotos() : null;
        } catch (final Exception ex) {
            LOGGER.log("Error retrieving photos: ", ex);
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorSequenceText());
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        List<Detection> detections = null;
        try {
            detections = detectionsFuture.get();
        } catch (final Exception ex) {
            LOGGER.log("Error retrieving detections: ", ex);
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorSequenceText());
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        executorService.shutdown();
        return new Sequence(sequenceId, photos, detections);
    }

    /**
     * Retrieves the details of the cluster identified by the given identifier.
     *
     * @param id the identifier of the cluster
     * @return a {@code Cluster} object.
     */
    public Cluster retrieveClusterDetails(final Long id) {
        final ExecutorService executorService = Executors.newFixedThreadPool(CLUSTER_THREAD_POOL_SIZE);
        final Future<Cluster> clusterFuture = executorService.submit(() -> apolloService.retrieveCluster(id));
        final Future<List<Photo>> photosFuture = executorService.submit(() -> apolloService.retrieveClusterPhotos(id));
        final Future<List<Detection>> detectionsFuture = executorService.submit(() -> apolloService
                .retrieveClusterDetections(id));

        ClusterBuilder clusterBuilder = new ClusterBuilder();
        try {
            clusterBuilder = new ClusterBuilder(clusterFuture.get());
        } catch (final Exception ex) {
            LOGGER.log("Error retrieving clusters: ", ex);
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        try {
            clusterBuilder.photos(photosFuture.get());
        } catch (final Exception ex) {
            LOGGER.log("Error retrieving clusters identified by photos: ", ex);
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        try {
            clusterBuilder.detections(detectionsFuture.get());
        } catch (final Exception ex) {
            LOGGER.log("Error retrieving clusters identified by detections: ", ex);
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        executorService.shutdown();
        return clusterBuilder.build();
    }

    private Sequence retrieveSequencePhotos(final Long id) {
        Sequence sequence = null;
        try {
            sequence = kartaViewService.retrieveSequence(id);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorSequenceText());
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        return sequence;
    }

    public List<Detection> retrieveSequenceDetections(final Long id, final BoundingBox boundingBox) {
        List<Detection> result = null;
        try {
            result = apolloService.retrieveSequenceDetections(id, boundingBox);
            if (result != null) {
                result = result.stream().filter(detection -> Util.isDetectionMatchingFilters(PreferenceManager
                        .getInstance().loadSearchFilter(), detection)).collect(Collectors.toList());
            }
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSequenceDetectionsErrorFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionRetrieveText());
                PreferenceManager.getInstance().saveSequenceDetectionsErrorFlag(flag);
            }
        }
        return result;
    }

    /**
     * Retrieve the list of detections corresponding to the given sequence.
     *
     * @param sequenceId the identifier of the sequence
     * @param sequenceIndex the photo index in the given sequence
     * @return a list of {@code Detection}s
     */
    public List<Detection> retrievePhotoDetections(final Long sequenceId, final Integer sequenceIndex) {
        List<Detection> result = null;
        try {
            result = apolloService.retrievePhotoDetections(sequenceId, sequenceIndex);
            if (result != null) {
                result = result.stream().filter(detection -> Util.isDetectionMatchingFilters(PreferenceManager
                        .getInstance().loadSearchFilter(), detection)).collect(Collectors.toList());
            }
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSequenceDetectionsErrorFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionRetrieveText());
                PreferenceManager.getInstance().saveSequenceDetectionsErrorFlag(flag);
            }
        }
        return result;
    }

    /**
     * Lists the segments that have KartaView coverage from the given area(s) corresponding to the specified zoom level.
     *
     * @param areas a list of {@code BoundingBox}s representing the search areas. If the OsmDataLayer is active, there
     * might be several bounding boxes.
     * @param filter a {@code Filter} represents the user's search filters. Null values are ignored.
     * @param zoom the current zoom level
     * @return a list of {@code Segment}s
     */
    public List<Segment> listMatchedTracks(final List<BoundingBox> areas, final SearchFilter filter, final int zoom) {
        List<Segment> finalResult = new ArrayList<>();
        try {
            if (areas.size() > 1) {
                // special case: there are several areas visible in the OSM data layer
                final ExecutorService executor = Executors.newFixedThreadPool(areas.size());
                final List<Future<List<Segment>>> futures = new ArrayList<>();
                for (final BoundingBox bbox : areas) {
                    final Callable<List<Segment>> callable = () -> kartaViewService.listMatchedTracks(bbox, zoom);
                    futures.add(executor.submit(callable));
                }
                finalResult.addAll(readResult(futures));
                executor.shutdown();
            } else {
                finalResult = kartaViewService.listMatchedTracks(areas.get(0), zoom);
            }
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSegmentsErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorSegmentListText());
                PreferenceManager.getInstance().saveSegmentsErrorSuppressFlag(flag);
            }
        }
        return finalResult;
    }

    /**
     * Retrieves the photo with the given name.
     *
     * @param photoName the name of a photo
     * @return the photo content in byte array format
     * @throws ServiceException if the download operation fails
     */
    byte[] retrievePhoto(final String photoName) throws ServiceException {
        return kartaViewService.retrievePhoto(photoName);
    }

    /**
     * Retrieves details of the given photo.
     *
     * @param sequenceId the identifier of the sequence
     * @param sequenceIndex the photo index in the given sequence
     * @return a {@code Photo}
     */
    public Photo retrievePhotoDetails(final Long sequenceId, final Integer sequenceIndex) {
        Photo result = null;
        try {
            result = kartaViewService.retrievePhotoDetails(sequenceId, sequenceIndex);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoLoadingText());
                PreferenceManager.getInstance().savePhotoDetectionsErrorFlag(flag);
            }
        }
        return result;
    }

    /**
     * Updates the edit status of the given detection.
     *
     * @param detectionId the identifier of the detection that need to be updated
     * @param editStatus a new edit status
     * @param comment a descriptive comment for the update.
     */
    public void updateDetection(final Long detectionId, final EditStatus editStatus, final String comment) {
        final Long userId = Util.getOsmUserId();
        final String userName = UserIdentityManager.getInstance().getUserName();
        if (userId == null) {
            JOptionPane.showMessageDialog(MainApplication.getMap().mapView, GuiConfig.getInstance()
                    .getAuthenticationNeededErrorMessage(), GuiConfig.getInstance().getWarningTitle(),
                    JOptionPane.WARNING_MESSAGE, null);
        } else {
            final Author author = new Author(userId.toString(), userName);
            try {
                apolloService.updateDetection(new Detection(detectionId, editStatus), new Contribution(author,
                        comment));
            } catch (final ServiceException e) {
                if (!PreferenceManager.getInstance().loadDetectionUpdateErrorSuppressFlag()) {
                    final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionUpdateText());
                    PreferenceManager.getInstance().saveDetectionUpdateErrorSuppressFlag(flag);
                }
            }
        }
    }

    /**
     * Retrieves the detection corresponding to the given identifier.
     *
     * @param detectionId the unique identifier of a detection
     * @return a {@code Detection}
     */
    public Detection retrieveDetection(final Long detectionId) {
        Detection result = null;
        try {
            result = apolloService.retrieveDetection(detectionId);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoLoadingText());
                PreferenceManager.getInstance().savePhotoDetectionsErrorFlag(flag);
            }
        }
        return result;
    }

    /**
     * Retrieves the photo corresponding to the given sequence identifier and sequence index.
     *
     * @param sequenceId the identifier of a sequence
     * @param sequenceIndex the index of the photo in the sequence
     * @return a {@code Photo} entity
     */
    public Photo retrievePhoto(final Long sequenceId, final Integer sequenceIndex) {
        Photo result = null;
        try {
            result = apolloService.retrievePhoto(sequenceId, sequenceIndex);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoLoadingText());
                PreferenceManager.getInstance().savePhotoDetectionsErrorFlag(flag);
            }
        }
        return result;
    }

    /**
     * Retrieves the full list of possible Sign Objects
     *
     * @return a List of Sign objects
     */
    public List<Sign> listSigns() {
        List<Sign> result = null;
        try {
            result = apolloService.listSigns();
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadListSignsErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorListSignsText());
                PreferenceManager.getInstance().saveListSignsErrorSuppressFlag(flag);
            }
        }
        return result;
    }

    /**
     * Retrieves the list of Sign regions.
     *
     * @return a list of {@code String}s representing the supported regions
     */
    public List<String> listRegions() {
        List<String> result = null;
        try {
            result = apolloService.listRegions();
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadListSignRegionsSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorListRegionsText());
                PreferenceManager.getInstance().saveListSignRegionErrorSuppressFlag(flag);
            }
        }
        return result;
    }

    /**
     * Retrieves the edge cluster with the given identifier and all associated data.
     *
     * @param id - the identifier of the edge cluster id
     * @return edge cluster entity
     */
    public Cluster retrieveEdgeCluster(final Long id) {
        final ExecutorService executorService = Executors.newFixedThreadPool(EDGE_CLUSTER_THREAD_POOL_SIZE);
        final Future<Cluster> clusterFuture = executorService.submit(() -> apolloService.retrieveEdgeCluster(id));
        final Future<List<EdgeDetection>> detectionsFuture = executorService.submit(() -> apolloService
                .retrieveEdgeClusterDetections(id));

        ClusterBuilder clusterBuilder = new ClusterBuilder();
        try {
            clusterBuilder = new ClusterBuilder(clusterFuture.get());
        } catch (final Exception ex) {
            LOGGER.log("Error retrieving clusters: ", ex);
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        try {
            clusterBuilder.edgeDetections(detectionsFuture.get());
        } catch (final Exception ex) {
            LOGGER.log("Error retrieving clusters identified by detections: ", ex);
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        executorService.shutdown();
        return clusterBuilder.build();
    }

    private <T> Set<T> readResult(final List<Future<List<T>>> futures) throws ServiceException {
        final Set<T> result = new HashSet<>();
        for (final Future<List<T>> future : futures) {
            try {
                result.addAll(future.get());
            } catch (final InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new ServiceException(ie);
            } catch (final ExecutionException ee) {
                throw new ServiceException(ee);
            }
        }
        return result;
    }
}