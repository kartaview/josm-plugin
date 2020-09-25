/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ClusterConfidenceLevel;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Contribution;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OcrValue;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.service.BaseService;
import org.openstreetmap.josm.plugins.openstreetcam.service.ClientLogger;
import org.openstreetmap.josm.plugins.openstreetcam.service.ServiceException;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity.Request;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity.Response;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity.SearchClustersAreaFilter;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity.SearchDetectionsAreaFilter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes the operations of the ApolloService components.
 *
 * @author beataj
 * @version $Revision$
 */
public class ApolloService extends BaseService {

	private static final ClientLogger logger = new ClientLogger("apolloService");

	@Override
	public Gson createGson() {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(EditStatus.class, new EditStatusTypeAdapter());
		builder.registerTypeAdapter(LatLon.class, new LatLonDeserializer());
		builder.registerTypeAdapter(ClusterConfidenceLevel.class, new ClusterConfidenceLevelDeserializer());
		builder.registerTypeAdapter(OcrValue.class, new OcrValueDeserializer());
		return builder.create();
	}

	public List<Detection> searchDetections(final SearchDetectionsAreaFilter searchFilter) throws ServiceException {
		final String url = new HttpQueryBuilder().buildSearchDetectionsQuery();
		final String content = buildRequest(searchFilter, SearchDetectionsAreaFilter.class);
		final Response response = executePost(url, content, Response.class, logger, "searchDetections ");
		verifyResponseStatus(response);
		logResponseSize(logger, "searchDetections ", response.getDetections());
		return response.getDetections() != null ? response.getDetections() : new ArrayList<>();
	}

	public List<Cluster> searchClusters(final SearchClustersAreaFilter searchFilter) throws ServiceException {
		final String url = new HttpQueryBuilder().buildSearchClustersQuery();
		final String content = buildRequest(searchFilter, SearchClustersAreaFilter.class);
		final Response response = executePost(url, content, Response.class, logger, "searchClusters ");
		verifyResponseStatus(response);
		logResponseSize(logger, "searchClusters ", response.getClusters());
		return response.getClusters() != null ? response.getClusters() : new ArrayList<>();
	}

	public void updateDetection(final Detection detection, final Contribution contribution) throws ServiceException {
		final String url = new HttpQueryBuilder().buildCommentQuery();
		final Request request = new Request(detection, contribution);
		final String content = buildRequest(request, Request.class);
		final Response root = executePost(url, content, Response.class, logger, "updateDetection ");
		verifyResponseStatus(root);
	}

	public List<Detection> retrieveSequenceDetections(final Long sequenceId) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveSequenceDetectionsQuery(sequenceId);
		final Response response = executeGet(url, Response.class, logger, "retrieveSequenceDetections ");
		verifyResponseStatus(response);
		logResponseSize(logger, "retrieveSequenceDetections ", response.getDetections());
		return response.getDetections();
	}

	public List<Detection> retrievePhotoDetections(final Long sequenceId, final Integer sequenceIndex)
			throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrievePhotoDetectionsQuery(sequenceId, sequenceIndex);
		final Response response = executeGet(url, Response.class, logger, "retrievePhotoDetections ");
		verifyResponseStatus(response);
		logResponseSize(logger, "retrievePhotoDetections ", response.getDetections());
		return response.getDetections();
	}

	public Detection retrieveDetection(final Long id) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveDetectionQuery(id);
		final Response response = executeGet(url, Response.class, logger, "retrieveDetection ");
		verifyResponseStatus(response);
		return response.getDetection();
	}

	public Cluster retrieveCluster(final Long id) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveClusterQuery(id);
		final Response response = executeGet(url, Response.class, logger, "retrieveCluster ");
		verifyResponseStatus(response);
		return response.getCluster();
	}

	public List<Detection> retrieveClusterDetections(final Long id) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveClusterDetectionsQuery(id);
		final Response response = executeGet(url, Response.class, logger, "retrieveClusterDetections ");
		verifyResponseStatus(response);
		logResponseSize(logger, "retrieveClusterDetections ", response.getDetections());
		return response.getDetections();
	}

	public List<Photo> retrieveClusterPhotos(final Long id) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveClusterPhotosQuery(id);
		final Response response = executeGet(url, Response.class, logger, "retrieveClusterPhotos ");
		verifyResponseStatus(response);
		logResponseSize(logger, "retrieveClusterPhotos ", response.getPhotos());
		return response.getPhotos();
	}

	public Photo retrievePhoto(final Long sequenceId, final Integer sequenceIndex) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrievePhotoQuery(sequenceId, sequenceIndex);
		final Response response = executeGet(url, Response.class, logger, "retrievePhoto ");
		verifyResponseStatus(response);
		return response.getPhoto();
	}

	public List<Sign> listSigns() throws ServiceException {
		final String url = new HttpQueryBuilder().buildListSignsQuery();
		final Response response = executeGet(url, Response.class, logger, "listSigns ");
		verifyResponseStatus(response);
		return response.getSigns();
	}

	public List<String> listRegions() throws ServiceException {
		final String url = new HttpQueryBuilder().buildListRegionsQuery();
		final Response response = executeGet(url, Response.class, logger, "listRegions ");
		verifyResponseStatus(response);
		return response.getRegions();
	}
}