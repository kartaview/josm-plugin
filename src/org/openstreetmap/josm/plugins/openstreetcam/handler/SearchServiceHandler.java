/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.HighZoomResultSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.openstreetcam.service.ServiceException;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.ApolloService;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.DetectionFilter;
import org.openstreetmap.josm.plugins.openstreetcam.service.photo.OpenStreetCamService;
import org.openstreetmap.josm.plugins.openstreetcam.service.photo.Paging;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Executes the service search operations
 *
 * @author beataj
 * @version $Revision$
 */
class SearchServiceHandler {

    protected final OpenStreetCamService openStreetCamService;
    protected final ApolloService apolloService;


    SearchServiceHandler() {
        openStreetCamService = new OpenStreetCamService();
        apolloService = new ApolloService();
    }

    /**
     * Searches for data high zoom levels. For high zoom levels depending on the selected filter the following data
     * types are displayed: photo locations, detections and clusters (aggregated detections).
     *
     * @param areas a list of {@code BoundingBox}s representing the search areas. If the OsmDataLayer is active, there
     * might be several bounding boxes.
     * @param filter a {@code SearchFilter} represents the currently selected search filters.
     * @return a {@code HighZoomResultSet} containing the result
     */
    HighZoomResultSet searchHighZoomData(final List<BoundingBox> areas, final SearchFilter filter) {
        final ExecutorService executorService = Executors.newFixedThreadPool(filter.getDataTypes().size());
        final List<Future<PhotoDataSet>> futurePhotoDataSets = new ArrayList<>();
        final List<Future<List<Detection>>> futureDetections = new ArrayList<>();
        final List<Future<List<Cluster>>> futureClusters = new ArrayList<>();

        for (final BoundingBox area : areas) {
            final Future<PhotoDataSet> futurePhotoDataSet = filter.getDataTypes().contains(DataType.PHOTO)
                    ? executorService.submit(() -> listNearbyPhotos(area, filter, Paging.NEARBY_PHOTOS_DEAFULT)) : null;
                    if (futurePhotoDataSet != null) {
                        futurePhotoDataSets.add(futurePhotoDataSet);
                    }
                    final Future<List<Detection>> futureDetectionList = filter.getDataTypes().contains(DataType.DETECTION)
                            ? executorService.submit(() -> searchDetections(area, filter)) : null;
                            if (futureDetectionList != null) {
                                futureDetections.add(futureDetectionList);
                            }
                            final Future<List<Cluster>> futureClusterList = filter.getDataTypes().contains(DataType.CLUSTER)
                                    ? executorService.submit(() -> searchClusters(area, filter)) : null;
                                    if (futureClusterList != null) {
                                        futureClusters.add(futureClusterList);
                                    }
        }
        PhotoDataSet photoDataSet = null;
        try {
            if (!futurePhotoDataSets.isEmpty()) {
                photoDataSet = futurePhotoDataSets.get(0).get();
                for (int i = 1; i < futurePhotoDataSets.size(); i++) {
                    photoDataSet.addPhotos(futurePhotoDataSets.get(i).get().getPhotos());
                }
            }
        } catch (final Exception ex) {
            if (!PreferenceManager.getInstance().loadPhotosSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoListText());
                PreferenceManager.getInstance().savePhotosSearchErrorSuppressFlag(flag);
            }
        }
        photoDataSet = photoDataSet != null && photoDataSet.hasItems() ? photoDataSet : null;

        List<Detection> detections = new ArrayList<>();
        try {
            for (final Future<List<Detection>> futureDetect : futureDetections) {
                detections.addAll(futureDetect.get());
            }
        } catch (final Exception ex) {
            if (!PreferenceManager.getInstance().loadDetectionsSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionRetrieveText());
                PreferenceManager.getInstance().saveDetectionsSearchErrorSuppressFlag(flag);
            }
        }
        detections = detections.isEmpty() ? null : detections;

        List<Cluster> clusters = new ArrayList<>();
        try {
            for (final Future<List<Cluster>> futureCluster : futureClusters) {
                clusters.addAll(futureCluster.get());
            }
        } catch (final Exception ex) {
            if (!PreferenceManager.getInstance().loadClustersSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveClustersSearchErrorSuppressFlag(flag);
            }
        }
        clusters = clusters.isEmpty() ? null : clusters;

        if (detections != null && clusters != null) {
            // remove detections that belongs to a cluster
            detections = filterClusterDetections(clusters, detections);
        }
        executorService.shutdown();
        return new HighZoomResultSet(photoDataSet, detections, clusters);
    }

    private List<Detection> filterClusterDetections(final List<Cluster> clusters, final List<Detection> detections) {
        final List<Detection> result = new ArrayList<>();

        final List<Long> clusterDetectionIds = clusters.stream().flatMap(
                cluster -> cluster.getDetectionIds() != null ? cluster.getDetectionIds().stream() : Stream.empty())
                .collect(Collectors.toList());
        for (final Detection detection : detections) {
            if (!clusterDetectionIds.contains(detection.getId())) {
                result.add(detection);
            }
        }
        return result;
    }

    /**
     * Lists the photos from the current area based on the given filters.
     *
     * @param area a {@code Circle} representing the search areas.
     * @param filter a {@code Filter} represents the user's search filters. Null values are ignored.
     * @param paging a {@code Paging} representing the pagination
     * @return a list of {@code Photo}s
     */
    public PhotoDataSet listNearbyPhotos(final BoundingBox area, final SearchFilter filter, final Paging paging) {
        Long osmUserId = null;
        Date date = null;
        if (filter != null) {
            osmUserId = filter.getOsmUserId();
            date = filter.getDate();
        }
        PhotoDataSet result = new PhotoDataSet();
        try {
            result = openStreetCamService.listNearbyPhotos(area, date, osmUserId, paging);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoListText());
                PreferenceManager.getInstance().savePhotosSearchErrorSuppressFlag(flag);
            }
        }
        return result;
    }

    private List<Detection> searchDetections(final BoundingBox area, final SearchFilter filter) {
        Long osmUserId = null;
        Date date = null;
        DetectionFilter detectionFilter = null;
        if (filter != null) {
            osmUserId = filter.getOsmUserId();
            date = filter.getDate();
            detectionFilter = filter.getDetectionFilter();
        }
        List<Detection> result = null;
        try {
            result = apolloService.searchDetections(area, date, osmUserId, detectionFilter);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadDetectionsSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionRetrieveText());
                PreferenceManager.getInstance().saveDetectionsSearchErrorSuppressFlag(flag);
            }
        }
        return result;
    }

    public List<Cluster> searchClusters(final BoundingBox area, final SearchFilter filter) {
        Date date = null;
        DetectionFilter detectionFilter = null;
        if (filter != null) {
            date = filter.getDate();
            detectionFilter = filter.getDetectionFilter();
        }
        List<Cluster> result = null;
        try {
            result = apolloService.searchClusters(area, date, detectionFilter);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadClustersSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveClustersSearchErrorSuppressFlag(flag);
            }
        }
        return result;

    }

    boolean handleException(final String message) {
        final int val = JOptionPane.showOptionDialog(MainApplication.getMainPanel(), message,
                GuiConfig.getInstance().getErrorTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                null, null);
        return val == JOptionPane.YES_OPTION;
    }
}