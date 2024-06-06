/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo;

import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.LIST_REGIONS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.LIST_SIGNS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.RETRIEVE_CLUSTER;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.RETRIEVE_CLUSTER_DETECTIONS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.RETRIEVE_CLUSTER_PHOTOS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.RETRIEVE_DETECTION;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.RETRIEVE_EDGE_CLUSTER;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.RETRIEVE_EDGE_CLUSTER_DETECTIONS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.RETRIEVE_PHOTO;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.RETRIEVE_PHOTO_DETECTIONS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.RETRIEVE_SEQUENCE_DETECTIONS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.SEARCH_CLUSTERS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.SEARCH_DETECTIONS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.SEARCH_EDGE_CLUSTERS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.SEARCH_EDGE_DETECTIONS;
import static org.openstreetmap.josm.plugins.kartaview.service.apollo.RequestConstants.UPDATE_DETECTION;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.Contribution;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;
import org.openstreetmap.josm.plugins.kartaview.entity.OcrValue;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.service.BaseService;
import org.openstreetmap.josm.plugins.kartaview.service.ClientLogger;
import org.openstreetmap.josm.plugins.kartaview.service.Paging;
import org.openstreetmap.josm.plugins.kartaview.service.ServiceException;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.PaginationResponse;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.Request;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.Response;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchClustersAreaFilter;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchDetectionsAreaFilter;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchPaginatedClustersAreaFilter;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchPaginatedDetectionsAreaFilter;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchPaginatedEdgeClustersAreaFilter;
import org.openstreetmap.josm.plugins.kartaview.service.apollo.entity.SearchPaginatedEdgeDetectionsAreaFilter;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.ApolloServiceConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grab.josm.common.argument.BoundingBox;


/**
 * Executes the operations of the ApolloService components.
 *
 * @author beataj
 * @version $Revision$
 */
public class ApolloService extends BaseService {

    private static final ClientLogger logger = new ClientLogger("apolloService");
    private static final int SECOND_PAGE = 2;


    @Override
    public Gson createGson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(EditStatus.class, new EditStatusTypeAdapter());
        builder.registerTypeAdapter(LatLon.class, new LatLonDeserializer());
        builder.registerTypeAdapter(OcrValue.class, new OcrValueDeserializer());
        builder.registerTypeAdapter(Instant.class, new InstantSerializer());
        return builder.create();
    }


    public List<Detection> searchDetections(final SearchDetectionsAreaFilter searchFilter) throws ServiceException {
        final PaginationResponse searchDetectionsResponse = searchDetections(new SearchPaginatedDetectionsAreaFilter(
                searchFilter, Paging.SEARCH_DETECTIONS_DEFAULT));
        final Set<Detection> detections = new HashSet<>();
        if (searchDetectionsResponse.getDetections() != null) {
            detections.addAll(searchDetectionsResponse.getDetections());
        }
        if (searchDetectionsResponse.getMetadata() != null && searchDetectionsResponse.getMetadata().getTotalElements()
                != null && searchDetectionsResponse.getMetadata().getTotalElements() > ApolloServiceConfig.getInstance()
                        .getSearchDetectionsMaxItems()) {
            final int pages = searchDetectionsResponse.getMetadata().getTotalElements() / ApolloServiceConfig
                    .getInstance().getSearchDetectionsMaxItems() + 1;
            final ExecutorService executor = Executors.newFixedThreadPool(pages);
            final List<Future<PaginationResponse>> futures = new ArrayList<>();
            for (int i = SECOND_PAGE; i <= pages; ++i) {
                final Paging paging = new Paging(i, ApolloServiceConfig.getInstance().getSearchDetectionsMaxItems());
                final Callable<PaginationResponse> callable = () -> searchDetections(
                        new SearchPaginatedDetectionsAreaFilter(searchFilter, paging));
                futures.add(executor.submit(callable));
            }
            detections.addAll(readPaginationResult(futures, response -> new HashSet<>(response.getDetections())));
            executor.shutdown();
        }
        return new ArrayList<>(detections);
    }

    private PaginationResponse searchDetections(final SearchPaginatedDetectionsAreaFilter searchFilter)
            throws ServiceException {
        final String url = new HttpQueryBuilder().buildSearchDetectionsQuery();
        final String content = buildRequest(searchFilter, SearchPaginatedDetectionsAreaFilter.class);
        final PaginationResponse response = executePost(url, content, PaginationResponse.class, logger,
                SEARCH_DETECTIONS);
        verifyResponseStatus(response);
        logResponseSize(logger, "searchDetections ", response.getDetections());
        return response;
    }

    public List<Cluster> searchClusters(final SearchClustersAreaFilter searchFilter) throws ServiceException {
        final PaginationResponse paginationResponse = searchClusters(new SearchPaginatedClustersAreaFilter(searchFilter,
                Paging.SEARCH_CLUSTERS_DEFAULT));
        final Set<Cluster> clusters = new HashSet<>();
        if (Objects.nonNull(paginationResponse.getClusters())) {
            clusters.addAll(paginationResponse.getClusters());
        }
        if (paginationResponse.getMetadata() != null && paginationResponse.getMetadata().getTotalElements() != null
                && paginationResponse.getMetadata().getTotalElements() > ApolloServiceConfig.getInstance()
                        .getSearchClustersMaxItems()) {
            final int pages = paginationResponse.getMetadata().getTotalElements() / ApolloServiceConfig.getInstance()
                    .getSearchClustersMaxItems() + 1;
            final ExecutorService executor = Executors.newFixedThreadPool(pages);
            final List<Future<PaginationResponse>> futures = new ArrayList<>();
            for (int i = SECOND_PAGE; i <= pages; ++i) {
                final Paging paging = new Paging(i, ApolloServiceConfig.getInstance().getSearchClustersMaxItems());
                final Callable<PaginationResponse> callable = () -> searchClusters(
                        new SearchPaginatedClustersAreaFilter(searchFilter, paging));
                futures.add(executor.submit(callable));
            }
            clusters.addAll(readPaginationResult(futures, response -> new HashSet<>(response.getClusters())));
            executor.shutdown();
        }
        return new ArrayList<>(clusters);
    }

    private PaginationResponse searchClusters(final SearchPaginatedClustersAreaFilter searchFilter)
            throws ServiceException {
        final String url = new HttpQueryBuilder().buildSearchClustersQuery();
        final String content = buildRequest(searchFilter, SearchPaginatedClustersAreaFilter.class);
        final PaginationResponse response = executePost(url, content, PaginationResponse.class, logger,
                SEARCH_CLUSTERS);
        verifyResponseStatus(response);
        logResponseSize(logger, "searchClusters ", response.getClusters());
        return response;
    }

    public void updateDetection(final Detection detection, final Contribution contribution) throws ServiceException {
        final String url = new HttpQueryBuilder().buildCommentQuery();
        final Request request = new Request(detection, contribution);
        final String content = buildRequest(request, Request.class);
        final Response root = executePost(url, content, Response.class, logger, UPDATE_DETECTION);
        verifyResponseStatus(root);
    }

    public List<Detection> retrieveSequenceDetections(final Long sequenceId, final BoundingBox boundingBox)
            throws ServiceException {
        final PaginationResponse retrieveSequenceDetectionsResponse = retrieveSequenceDetections(sequenceId,
                Paging.RETRIEVE_SEQUENCE_DETECTIONS_DEFAULT, boundingBox);
        final Set<Detection> detections = new HashSet<>();
        if (retrieveSequenceDetectionsResponse.getDetections() != null && !retrieveSequenceDetectionsResponse
                .getDetections().isEmpty()) {
            detections.addAll(retrieveSequenceDetectionsResponse.getDetections());
        }
        if (retrieveSequenceDetectionsResponse.getMetadata() != null && retrieveSequenceDetectionsResponse.getMetadata()
                .getTotalElements() != null && retrieveSequenceDetectionsResponse.getMetadata().getTotalElements()
                        > ApolloServiceConfig.getInstance().getRetrieveSequenceDetectionsMaxItems()) {
            final int pages = retrieveSequenceDetectionsResponse.getMetadata().getTotalElements() / ApolloServiceConfig
                    .getInstance().getRetrieveSequenceDetectionsMaxItems() + 1;
            final ExecutorService executor = Executors.newFixedThreadPool(pages);
            final List<Future<PaginationResponse>> futures = new ArrayList<>();
            for (int i = SECOND_PAGE; i <= pages; i++) {
                final Paging paging = new Paging(i, ApolloServiceConfig.getInstance()
                        .getRetrieveSequenceDetectionsMaxItems());
                final Callable<PaginationResponse> callable = () -> retrieveSequenceDetections(sequenceId, paging,
                        boundingBox);
                futures.add(executor.submit(callable));
            }
            detections.addAll(readPaginationResult(futures, response -> new HashSet<>(response.getDetections())));
            executor.shutdown();
        }
        return new ArrayList<>(detections);
    }

    /**
     * Retrieves a list of detections and the metadata info for a given page and items number.
     *
     * @param sequenceId the id of the sequence
     * @param paging Paging object
     * @return PaginationResponse containing the metadata relevant for response pagination
     * @throws ServiceException
     */
    private PaginationResponse retrieveSequenceDetections(final Long sequenceId, final Paging paging,
            final BoundingBox boundingBox) throws ServiceException {
        final String url = new HttpQueryBuilder().buildRetrieveSequenceDetectionsQuery(sequenceId, paging, boundingBox);
        final PaginationResponse response = executeGet(url, PaginationResponse.class, logger,
                RETRIEVE_SEQUENCE_DETECTIONS);
        verifyResponseStatus(response);
        logResponseSize(logger, "retrieveSequenceDetections ", response.getDetections());
        return response;
    }

    public List<Detection> retrievePhotoDetections(final Long sequenceId, final Integer sequenceIndex)
            throws ServiceException {
        final String url = new HttpQueryBuilder().buildRetrievePhotoDetectionsQuery(sequenceId, sequenceIndex);
        final Response response = executeGet(url, Response.class, logger, RETRIEVE_PHOTO_DETECTIONS);
        verifyResponseStatus(response);
        logResponseSize(logger, "retrievePhotoDetections ", response.getDetections());
        return response.getDetections();
    }

    public Detection retrieveDetection(final Long id) throws ServiceException {
        final String url = new HttpQueryBuilder().buildRetrieveDetectionQuery(id);
        final Response response = executeGet(url, Response.class, logger, RETRIEVE_DETECTION);
        verifyResponseStatus(response);
        return response.getDetection();
    }

    public Cluster retrieveCluster(final Long id) throws ServiceException {
        final String url = new HttpQueryBuilder().buildRetrieveClusterQuery(id);
        final Response response = executeGet(url, Response.class, logger, RETRIEVE_CLUSTER);
        verifyResponseStatus(response);
        return response.getCluster();
    }

    public List<Detection> retrieveClusterDetections(final Long id) throws ServiceException {
        final String url = new HttpQueryBuilder().buildRetrieveClusterDetectionsQuery(id);
        final Response response = executeGet(url, Response.class, logger, RETRIEVE_CLUSTER_DETECTIONS);
        verifyResponseStatus(response);
        logResponseSize(logger, "retrieveClusterDetections ", response.getDetections());
        return response.getDetections();
    }

    public List<Photo> retrieveClusterPhotos(final Long id) throws ServiceException {
        final String url = new HttpQueryBuilder().buildRetrieveClusterPhotosQuery(id);
        final Response response = executeGet(url, Response.class, logger, RETRIEVE_CLUSTER_PHOTOS);
        verifyResponseStatus(response);
        logResponseSize(logger, "retrieveClusterPhotos ", response.getPhotos());
        return response.getPhotos();
    }

    public Photo retrievePhoto(final Long sequenceId, final Integer sequenceIndex) throws ServiceException {
        final String url = new HttpQueryBuilder().buildRetrievePhotoQuery(sequenceId, sequenceIndex);
        final Response response = executeGet(url, Response.class, logger, RETRIEVE_PHOTO);
        verifyResponseStatus(response);
        return response.getPhoto();
    }

    public List<Sign> listSigns() throws ServiceException {
        final String url = new HttpQueryBuilder().buildListSignsQuery();
        final Response response = executeGet(url, Response.class, logger, LIST_SIGNS);
        verifyResponseStatus(response);
        return response.getSigns();
    }

    public List<String> listRegions() throws ServiceException {
        final String url = new HttpQueryBuilder().buildListRegionsQuery();
        final Response response = executeGet(url, Response.class, logger, LIST_REGIONS);
        verifyResponseStatus(response);
        return response.getRegions();
    }

    /**
     * Search edge detections matching criteria.
     *
     * @param filter - objects containing the selected criteria for search
     * @return list of edge detections matching the criteria
     * @throws ServiceException in case the operation fails
     */
    public List<EdgeDetection> searchEdgeDetections(final BoundingBox area, final EdgeSearchFilter filter)
            throws ServiceException {
        final PaginationResponse searchEdgeDetectionsResponse = searchEdgeDetections(
                new SearchPaginatedEdgeDetectionsAreaFilter(area, filter, Paging.SEARCH_EDGE_DETECTIONS_DEFAULT));
        final Set<EdgeDetection> detections = new HashSet<>();
        if (Objects.nonNull(searchEdgeDetectionsResponse.getEdgeDetections())) {
            detections.addAll(searchEdgeDetectionsResponse.getEdgeDetections());
        }
        if (Objects.nonNull(searchEdgeDetectionsResponse.getMetadata()) && Objects.nonNull(searchEdgeDetectionsResponse
                .getMetadata().getTotalElements()) && searchEdgeDetectionsResponse.getMetadata().getTotalElements()
                        > ApolloServiceConfig.getInstance().getSearchEdgeDetectionsMaxItems()) {
            final int pages = searchEdgeDetectionsResponse.getMetadata().getTotalElements() / ApolloServiceConfig
                    .getInstance().getSearchEdgeDetectionsMaxItems() + 1;
            final ExecutorService executor = Executors.newFixedThreadPool(pages);
            final List<Future<PaginationResponse>> futures = new ArrayList<>();
            for (int i = SECOND_PAGE; i <= pages; ++i) {
                final Paging paging = new Paging(i, ApolloServiceConfig.getInstance()
                        .getSearchEdgeDetectionsMaxItems());
                final Callable<PaginationResponse> callable = () -> searchEdgeDetections(
                        new SearchPaginatedEdgeDetectionsAreaFilter(area, filter, paging));
                futures.add(executor.submit(callable));
            }
            detections.addAll(readPaginationResult(futures, rs -> new HashSet<>(rs.getEdgeDetections())));

            executor.shutdown();
        }
        return new ArrayList<>(detections);
    }

    private PaginationResponse searchEdgeDetections(final SearchPaginatedEdgeDetectionsAreaFilter searchFilter)
            throws ServiceException {
        final String url = new HttpQueryBuilder().buildSearchEdgeDetectionsQuery();
        final String content = buildRequest(searchFilter, SearchPaginatedEdgeDetectionsAreaFilter.class);
        final PaginationResponse response = executePost(url, content, PaginationResponse.class, logger,
                SEARCH_EDGE_DETECTIONS);
        verifyResponseStatus(response);
        logResponseSize(logger, "searchEdgeDetections ", response.getEdgeDetections());
        return response;
    }

    /**
     * List of edge detections belonging to an edge cluster.
     *
     * @param id - identifier of the edge cluster
     * @return the cluster with the given identifier
     * @throws ServiceException in case of operation fails
     */
    public List<EdgeDetection> retrieveEdgeClusterDetections(final Long id) throws ServiceException {
        final String url = new HttpQueryBuilder().buildRetrieveEdgeClusterDetectionsQuery(id);
        final Response response = executeGet(url, Response.class, logger, RETRIEVE_EDGE_CLUSTER_DETECTIONS);
        verifyResponseStatus(response);
        return response.getEdgeDetections();
    }

    /**
     * Searches for the edge clusters matching the filtering criteria.
     *
     * @param area the current search area
     * @param filter the filters set by the user
     * @return list of edge clusters matching the filters
     * @throws ServiceException in case the operation fails
     */
    public List<Cluster> searchEdgeClusters(final BoundingBox area, final EdgeSearchFilter filter)
            throws ServiceException {
        final PaginationResponse firstPageResponse = searchEdgeClusters(new SearchPaginatedEdgeClustersAreaFilter(area,
                filter, Paging.SEARCH_EDGE_CLUSTERS_DEFAULT));
        final Set<Cluster> clusters = new HashSet<>();
        if (Objects.nonNull(firstPageResponse.getClusters())) {
            clusters.addAll(firstPageResponse.getClusters());
        }
        if (Objects.nonNull(firstPageResponse.getMetadata()) && Objects.nonNull(firstPageResponse.getMetadata()
                .getTotalElements()) && firstPageResponse.getMetadata().getTotalElements() > ApolloServiceConfig
                        .getInstance().getSearchEdgeDetectionsMaxItems()) {
            final int pages = firstPageResponse.getMetadata().getTotalElements() / ApolloServiceConfig.getInstance()
                    .getSearchEdgeClustersMaxItems() + 1;
            final ExecutorService executor = Executors.newFixedThreadPool(pages);
            final List<Future<PaginationResponse>> futures = new ArrayList<>();
            for (int i = SECOND_PAGE; i <= pages; ++i) {
                final Paging paging = new Paging(i, ApolloServiceConfig.getInstance()
                        .getSearchEdgeDetectionsMaxItems());
                final Callable<PaginationResponse> callable = () -> searchEdgeClusters(
                        new SearchPaginatedEdgeClustersAreaFilter(area, filter, paging));
                futures.add(executor.submit(callable));
            }

            clusters.addAll(readPaginationResult(futures, rs -> new HashSet<>(rs.getClusters())));
            executor.shutdown();
        }
        return new ArrayList<>(clusters);
    }

    private PaginationResponse searchEdgeClusters(SearchPaginatedEdgeClustersAreaFilter filter)
            throws ServiceException {
        final String url = new HttpQueryBuilder().buildSearchEdgeClustersQuery();
        final String content = buildRequest(filter, SearchPaginatedEdgeClustersAreaFilter.class);
        final PaginationResponse response = executePost(url, content, PaginationResponse.class, logger,
                SEARCH_EDGE_CLUSTERS);
        verifyResponseStatus(response);
        logResponseSize(logger, "searchEdgeClusters", response.getClusters());
        return response;
    }

    /**
     * Retrieves an edge cluster.
     *
     * @param id - identifier of the edge cluster
     * @return the cluster with the given identifier
     * @throws ServiceException in case of operation fails
     */
    public Cluster retrieveEdgeCluster(final Long id) throws ServiceException {
        final String url = new HttpQueryBuilder().buildRetrieveEdgeCluster(id);
        final Response response = executeGet(url, Response.class, logger, RETRIEVE_EDGE_CLUSTER);
        verifyResponseStatus(response);
        return response.getCluster();
    }

    /**
     * Reads the pagination results from the list of {@code Future} using the provided extractor function.
     *
     * @param futures A list of {@code Future} containing pagination responses.
     * @param extractor A function to extract elements from each {@code PaginationResponse} and return them as a set.
     * @param <T> The type of elements extracted.
     * @return A set containing the elements from all pagination responses.
     * @throws ServiceException If the thread execution failed or if the process was interrupted.
     */
    private <T> Set<T> readPaginationResult(final List<Future<PaginationResponse>> futures,
            final Function<PaginationResponse, Set<T>> extractor) throws ServiceException {
        final Set<T> resultSet = new HashSet<>();
        for (final Future<PaginationResponse> future : futures) {
            try {
                resultSet.addAll(extractor.apply(future.get()));
            } catch (InterruptedException | ExecutionException e) {
                logger.log("Error reading result (readResult).", e);
                throw new ServiceException(e);
            }
        }
        return resultSet;
    }
}