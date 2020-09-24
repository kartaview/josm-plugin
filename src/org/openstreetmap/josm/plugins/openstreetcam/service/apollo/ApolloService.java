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
		final Instant startTime = Instant.now();
		final Response response = executePost(url, content, Response.class);
		final Instant endTime = Instant.now();
		logger.log("searchDetections " + url + " with content: " + content + " responded in " + Duration
				.between(startTime, endTime).toMillis() + "ms", null);
		verifyResponseStatus(response);
		final int responseSize = response.getDetections() != null ? response.getDetections().size() : 0;
		logger.log("searchDetections returned:  " + responseSize + " detections.", null);
		return response.getDetections() != null ? response.getDetections() : new ArrayList<>();
	}

	public List<Cluster> searchClusters(final SearchClustersAreaFilter searchFilter) throws ServiceException {
		final String url = new HttpQueryBuilder().buildSearchClustersQuery();
		final String content = buildRequest(searchFilter, SearchClustersAreaFilter.class);
		final Instant startTime = Instant.now();
		final Response response = executePost(url, content, Response.class);
		final Instant endTime = Instant.now();
		logger.log("searchClusters " + url + " with content: " + content + " responded in " + Duration
				.between(startTime, endTime).toMillis() + "ms", null);
		final int responseSize = response.getClusters() != null ? response.getClusters().size() : 0;
		logger.log("searchClusters returned:  " + responseSize + " clusters.", null);
		verifyResponseStatus(response);
		return response.getClusters() != null ? response.getClusters() : new ArrayList<>();
	}

	public void updateDetection(final Detection detection, final Contribution contribution) throws ServiceException {
		final String url = new HttpQueryBuilder().buildCommentQuery();
		final Request request = new Request(detection, contribution);
		final String content = buildRequest(request, Request.class);
		final Instant startTime = Instant.now();
		final Response root = executePost(url, content, Response.class);
		final Instant endTime = Instant.now();
		logger.log("updateDetection " + url + " with content: " + content + " responded in " + Duration
				.between(startTime, endTime).toMillis() + "ms", null);
		verifyResponseStatus(root);
	}

	public List<Detection> retrieveSequenceDetections(final Long sequenceId) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveSequenceDetectionsQuery(sequenceId);
		final Instant startTime = Instant.now();
		final Response response = executeGet(url, Response.class);
		final Instant endTime = Instant.now();
		verifyResponseStatus(response);
		logger.log(
				"retrieveSequenceDetections " + url + " responded in " + Duration.between(startTime, endTime).toMillis()
						+ "ms", null);
		final int responseSize = response.getDetections() != null ? response.getDetections().size() : 0;
		logger.log("retrieveSequenceDetections returned:  " + responseSize + " detections.", null);
		return response.getDetections();
	}

	public List<Detection> retrievePhotoDetections(final Long sequenceId, final Integer sequenceIndex)
			throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrievePhotoDetectionsQuery(sequenceId, sequenceIndex);
		final Instant startTime = Instant.now();
		final Response response = executeGet(url, Response.class);
		final Instant endTime = Instant.now();
		logger.log("retrievePhotoDetections " + url + " responded in " + Duration.between(startTime, endTime).toMillis()
				+ "ms", null);
		verifyResponseStatus(response);
		final int responseSize = response.getDetections() != null ? response.getDetections().size() : 0;
		logger.log("retrievePhotoDetections returned:  " + responseSize + " detections.", null);
		return response.getDetections();
	}

	public Detection retrieveDetection(final Long id) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveDetectionQuery(id);
		final Instant startTime = Instant.now();
		final Response response = executeGet(url, Response.class);
		final Instant endTime = Instant.now();
		logger.log("retrieveDetection " + url + " responded in " + Duration.between(startTime, endTime).toMillis() + "ms",
				null);
		verifyResponseStatus(response);
		return response.getDetection();
	}

	public Cluster retrieveCluster(final Long id) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveClusterQuery(id);
		final Instant startTime = Instant.now();
		final Response response = executeGet(url, Response.class);
		final Instant endTime = Instant.now();
		logger.log("retrieveCluster " + url + " responded in " + Duration.between(startTime, endTime).toMillis() + "ms",
				null);
		verifyResponseStatus(response);
		return response.getCluster();
	}

	public List<Detection> retrieveClusterDetections(final Long id) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveClusterDetectionsQuery(id);
		final Instant startTime = Instant.now();
		final Response response = executeGet(url, Response.class);
		final Instant endTime = Instant.now();
		logger.log("retrieveClusterDetections " + url + " responded in " + Duration.between(startTime, endTime).toMillis()
				+ "ms", null);
		verifyResponseStatus(response);
		final int responseSize = response.getDetections() != null ? response.getDetections().size() : 0;
		logger.log("retrieveClusterDetections returned:  " + responseSize + " detections.", null);
		return response.getDetections();
	}

	public List<Photo> retrieveClusterPhotos(final Long id) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrieveClusterPhotosQuery(id);
		final Instant startTime = Instant.now();
		final Response response = executeGet(url, Response.class);
		final Instant endTime = Instant.now();
		logger.log("retrieveClusterPhotos " + url + " responded in " + Duration.between(startTime, endTime).toMillis()
				+ "ms", null);
		verifyResponseStatus(response);
		final int responseSize = response.getPhotos() != null ? response.getPhotos().size() : 0;
		logger.log("retrieveClusterPhotos returned:  " + responseSize + " photos.", null);
		return response.getPhotos();
	}

	public Photo retrievePhoto(final Long sequenceId, final Integer sequenceIndex) throws ServiceException {
		final String url = new HttpQueryBuilder().buildRetrievePhotoQuery(sequenceId, sequenceIndex);
		final Instant startTime = Instant.now();
		final Response response = executeGet(url, Response.class);
		final Instant endTime = Instant.now();
		logger.log("retrievePhoto " + url + " responded in " + Duration.between(startTime, endTime).toMillis() + "ms",
				null);
		verifyResponseStatus(response);
		return response.getPhoto();
	}

	public List<Sign> listSigns() throws ServiceException {
		final String url = new HttpQueryBuilder().buildListSignsQuery();
		final Instant startTime = Instant.now();
		final Response response = executeGet(url, Response.class);
		final Instant endTime = Instant.now();
		logger.log("listSigns " + url + " responded in " + Duration.between(startTime, endTime).toMillis() + "ms", null);
		verifyResponseStatus(response);
		return response.getSigns();
	}

	public List<String> listRegions() throws ServiceException {
		final String url = new HttpQueryBuilder().buildListRegionsQuery();
		final Instant startTime = Instant.now();
		final Response response = executeGet(url, Response.class);
		final Instant endTime = Instant.now();
		logger.log("listRegions " + url + " responded in " + Duration.between(startTime, endTime).toMillis() + "ms",
				null);
		verifyResponseStatus(response);
		return response.getRegions();
	}
}