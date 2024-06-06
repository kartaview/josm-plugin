/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.kartaview.argument.DataType;
import org.openstreetmap.josm.plugins.kartaview.argument.DetectionFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.argument.SearchFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDataResultSet;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.entity.HighZoomResultSet;
import org.openstreetmap.josm.plugins.kartaview.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.kartaview.service.ClientLogger;
import org.openstreetmap.josm.plugins.kartaview.service.Paging;
import org.openstreetmap.josm.plugins.kartaview.service.ServiceException;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.ApolloService;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchClustersAreaFilter;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchClustersFilter;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchDetectionsAreaFilter;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchDetectionsFilter;
import org.openstreetmap.josm.plugins.kartaview.service.photo.KartaViewService;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import com.grab.josm.common.argument.BoundingBox;


/**
 * Executes the service search operations
 *
 * @author beataj
 * @version $Revision$
 */
class SearchServiceHandler {

    private static final double AREA_EXTEND = 0.004;

    protected static final ClientLogger LOGGER = new ClientLogger("error");
    protected final KartaViewService kartaViewService;
    protected final ApolloService apolloService;

    SearchServiceHandler() {
        kartaViewService = new KartaViewService();
        apolloService = new ApolloService();
    }


    /**
     * Searches for imagery layer data high zoom levels. For high zoom levels depending on the selected filter the
     * following data
     * types are displayed: photo locations, detections and clusters.
     *
     * @param areas a list of {@code BoundingBox}s representing the search areas. If the OsmDataLayer is active, there
     * might be several bounding boxes.
     * @param filter a {@code SearchFilter} represents the currently selected search filters.
     * @return a {@code HighZoomResultSet} containing the result
     */
    HighZoomResultSet searchHighZoomImageryData(final List<BoundingBox> areas, final SearchFilter filter) {
        final ExecutorService executorService = Executors.newFixedThreadPool(filter.getDataTypes().size());
        final List<Future<PhotoDataSet>> futurePhotoDataSets = new ArrayList<>();
        final List<Future<List<Detection>>> futureDetections = new ArrayList<>();
        final List<Future<List<Cluster>>> futureClusters = new ArrayList<>();

        for (final BoundingBox area : areas) {
            if (filter.getDataTypes().contains(DataType.PHOTO)) {
                futurePhotoDataSets.add(executorService.submit(() -> listNearbyPhotos(area, filter,
                        Paging.NEARBY_PHOTOS_DEFAULT)));
            }
            if (filter.getDataTypes().contains(DataType.DETECTION)) {
                futureDetections.add(executorService.submit(() -> searchDetections(area, filter)));
            }
            if (filter.getDataTypes().contains(DataType.CLUSTER)) {
                futureClusters.add(executorService.submit(() -> searchClusters(area, filter)));
            }
        }
        final PhotoDataSet photoDataSet = !futurePhotoDataSets.isEmpty() ? readPhotoDataSet(futurePhotoDataSets) : null;
        List<Detection> detections = !futureDetections.isEmpty() ? readDetections(futureDetections) : null;
        final List<Cluster> clusters = !futureClusters.isEmpty() ? readClusters(futureClusters) : null;

        if (detections != null && clusters != null) {
            // remove detections that belongs to a cluster
            detections = filterClusterDetections(clusters, detections);
        }
        executorService.shutdown();
        return new HighZoomResultSet(photoDataSet, detections, clusters);
    }

    private List<Detection> searchDetections(final BoundingBox area, final SearchFilter filter) {
        Date date = null;
        DetectionFilter detectionFilter = null;
        if (filter != null) {
            date = filter.getDate();
            detectionFilter = filter.getDetectionFilter();
        }
        List<Detection> result = null;
        try {
            result = apolloService.searchDetections(new SearchDetectionsAreaFilter(area, new SearchDetectionsFilter(
                    date, detectionFilter)));
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadDetectionsSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionRetrieveText());
                PreferenceManager.getInstance().saveDetectionsSearchErrorSuppressFlag(flag);
            }
        }
        return result;
    }

    private PhotoDataSet readPhotoDataSet(List<Future<PhotoDataSet>> futurePhotoDataSets) {
        PhotoDataSet photoDataSet = null;
        try {
            photoDataSet = futurePhotoDataSets.get(0).get();
            for (int i = 1; i < futurePhotoDataSets.size(); i++) {
                photoDataSet.addPhotos(futurePhotoDataSets.get(i).get().getPhotos());
            }
        } catch (final Exception ex) {
            LOGGER.log("Error displaying photo data sets: ", ex);
            if (!PreferenceManager.getInstance().loadPhotosSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoListText());
                PreferenceManager.getInstance().savePhotosSearchErrorSuppressFlag(flag);
            }
        }
        return photoDataSet != null && photoDataSet.hasItems() ? photoDataSet : null;
    }

    private List<Detection> readDetections(final List<Future<List<Detection>>> futureDetections) {
        List<Detection> detections = null;
        try {
            detections = readListResult(futureDetections);
        } catch (final Exception ex) {
            LOGGER.log("Error displaying detections: ", ex);
            if (!PreferenceManager.getInstance().loadDetectionsSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionRetrieveText());
                PreferenceManager.getInstance().saveDetectionsSearchErrorSuppressFlag(flag);
            }
        }
        return detections;
    }

    private List<Cluster> readClusters(final List<Future<List<Cluster>>> futureClusters) {
        List<Cluster> clusters = new ArrayList<>();
        try {
            clusters = readListResult(futureClusters);
        } catch (final Exception ex) {
            LOGGER.log("Error displaying clusters: ", ex);
            if (!PreferenceManager.getInstance().loadClustersSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveClustersSearchErrorSuppressFlag(flag);
            }
        }
        return Objects.nonNull(clusters) ? (clusters.isEmpty() ? null : clusters) : null;
    }

    private List<Detection> filterClusterDetections(final List<Cluster> clusters, final List<Detection> detections) {
        final List<Detection> result = new ArrayList<>();

        final List<Long> clusterDetectionIds = clusters.stream().flatMap(cluster -> cluster.getDetectionIds() != null
                ? cluster.getDetectionIds().stream() : Stream.empty()).collect(Collectors.toList());
        for (final Detection detection : detections) {
            if (!clusterDetectionIds.contains(detection.getId())) {
                result.add(detection);
            }
        }
        return result;
    }

    private <T> List<T> readListResult(final List<Future<List<T>>> futures) throws InterruptedException,
    ExecutionException {
        List<T> result = new ArrayList<>();
        for (final Future<List<T>> f : futures) {
            result = f.get();
        }
        return result;
    }

    public EdgeDataResultSet searchEdgeData(final List<BoundingBox> areas, final EdgeSearchFilter filter) {
        final ExecutorService executorService = Executors.newFixedThreadPool(filter.getDataTypes().size());
        final List<Future<List<EdgeDetection>>> futureDetections = new ArrayList<>();
        final List<Future<List<Cluster>>> futureClusters = new ArrayList<>();

        for (final BoundingBox area : areas) {
            //load detections
            if (filter.getDataTypes().contains(DataType.DETECTION)) {
                futureDetections.add(executorService.submit(() -> apolloService.searchEdgeDetections(area, filter)));
            }

            // load clusters
            if (filter.getDataTypes().contains(DataType.CLUSTER)) {
                futureClusters.add(executorService.submit(() -> {
                    final BoundingBox extendedArea = new BoundingBox(area.getNorth() + AREA_EXTEND, area.getSouth()
                            - AREA_EXTEND, area.getEast() + AREA_EXTEND, area.getWest() - AREA_EXTEND);
                    return apolloService.searchEdgeClusters(extendedArea, filter);
                }));
            }
        }
        List<EdgeDetection> detections = !futureDetections.isEmpty() ? readEdgeDetections(futureDetections) : null;
        final List<Cluster> clusters = !futureClusters.isEmpty() ? readEdgeClusters(futureClusters) : null;

        if (detections != null && clusters != null) {
            // remove detections that belongs to a cluster
            detections = filterEdgeClusterDetections(clusters, detections);
        }
        executorService.shutdown();
        return new EdgeDataResultSet(detections, clusters);
    }

    private List<EdgeDetection> readEdgeDetections(final List<Future<List<EdgeDetection>>> futureDetections) {
        List<EdgeDetection> detections = null;
        try {
            detections = readListResult(futureDetections);
        } catch (final Exception ex) {
            LOGGER.log("Error displaying edge detections: ", ex);
            if (!PreferenceManager.getInstance().loadEdgeDetectionsSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorEdgeDetectionRetrieveText());
                PreferenceManager.getInstance().saveEdgeDetectionsSearchErrorSuppressFlag(flag);
            }
        }
        return detections;
    }

    private List<Cluster> readEdgeClusters(final List<Future<List<Cluster>>> futureClusters) {
        List<Cluster> clusters = new ArrayList<>();
        try {
            clusters = readListResult(futureClusters);
        } catch (final Exception ex) {
            LOGGER.log("Error displaying edge clusters: ", ex);
            if (!PreferenceManager.getInstance().loadEdgeClustersSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveEdgeClustersSearchErrorSuppressFlag(flag);
            }
        }
        return Objects.nonNull(clusters) ? (clusters.isEmpty() ? null : clusters) : null;
    }

    private List<EdgeDetection> filterEdgeClusterDetections(final List<Cluster> clusters,
            final List<EdgeDetection> detections) {
        final List<EdgeDetection> result = new ArrayList<>();

        final List<Long> clusterDetectionIds = clusters.stream().flatMap(cluster -> cluster.getDetectionIds() != null
                ? cluster.getDetectionIds().stream() : Stream.empty()).collect(Collectors.toList());
        for (final EdgeDetection detection : detections) {
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
        Date date = null;
        if (filter != null) {
            date = filter.getDate();
        }
        PhotoDataSet result = new PhotoDataSet();
        try {
            result = kartaViewService.listNearbyPhotos(area, date, paging);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoListText());
                PreferenceManager.getInstance().savePhotosSearchErrorSuppressFlag(flag);
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
            final BoundingBox extendedArea = new BoundingBox(area.getNorth() + AREA_EXTEND, area.getSouth()
                    - AREA_EXTEND, area.getEast() + AREA_EXTEND, area.getWest() - AREA_EXTEND);
            result = apolloService.searchClusters(new SearchClustersAreaFilter(extendedArea, new SearchClustersFilter(
                    date, detectionFilter)));
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadClustersSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorClusterRetrieveText());
                PreferenceManager.getInstance().saveClustersSearchErrorSuppressFlag(flag);
            }
        }
        return result;
    }


    boolean handleException(final String message) {
        final int val = JOptionPane.showOptionDialog(MainApplication.getMainPanel(), message, GuiConfig.getInstance()
                .getErrorTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        return val == JOptionPane.YES_OPTION;
    }
}