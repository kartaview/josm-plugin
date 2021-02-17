/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo;

import com.grab.josm.common.http.HttpUtil;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.ApolloServiceConfig;


/**
 * @author beataj
 * @version $Revision$
 */
class HttpQueryBuilder {

    private static final char QUESTIONM = '?';
    private static final char EQ = '=';
    private static final char AND = '&';
    private static final char COMMA = ',';

    private final StringBuilder query;


    HttpQueryBuilder() {
        query = new StringBuilder();
    }

    String buildCommentQuery() {
        query.append(RequestConstants.UPDATE_DETECTION);
        query.append(QUESTIONM);
        appendFormatFilter(query);
        return build();
    }

    String buildSearchDetectionsQuery() {
        query.append(RequestConstants.SEARCH_DETECTIONS);
        query.append(QUESTIONM);
        appendFormatFilter(query);
        return build();
    }

    String buildSearchClustersQuery() {
        query.append(RequestConstants.SEARCH_CLUSTERS);
        query.append(QUESTIONM);
        appendFormatFilter(query);
        return build();
    }

    String buildRetrieveSequenceDetectionsQuery(final Long sequenceId) {
        query.append(RequestConstants.RETRIEVE_SEQUENCE_DETECTIONS);
        query.append(QUESTIONM);
        query.append(RequestConstants.SEQUENCE_ID).append(EQ).append(sequenceId);
        appendExcludedSignTypeFilter();
        return build();
    }

    String buildRetrievePhotoDetectionsQuery(final Long sequenceId, final Integer sequenceIndex) {
        query.append(RequestConstants.RETRIEVE_PHOTO_DETECTIONS);
        query.append(QUESTIONM);
        appendPhotoIdFilter(sequenceId, sequenceIndex);
        appendExcludedSignTypeFilter();
        return build();
    }

    String buildRetrieveDetectionQuery(final Long id) {
        return buildRetrieveByIdQuery(RequestConstants.RETRIEVE_DETECTION, id);
    }

    String buildRetrieveClusterQuery(final Long id) {
        return buildRetrieveByIdQuery(RequestConstants.RETRIEVE_CLUSTER, id);
    }

    String buildRetrieveClusterDetectionsQuery(final Long id) {
        return buildRetrieveByIdQuery(RequestConstants.RETRIEVE_CLUSTER_DETECTIONS, id);
    }

    String buildRetrieveClusterPhotosQuery(final Long id) {
        return buildRetrieveByIdQuery(RequestConstants.RETRIEVE_CLUSTER_PHOTOS, id);
    }

    String buildRetrievePhotoQuery(final Long sequenceId, final Integer sequenceIndex) {
        query.append(RequestConstants.RETRIEVE_PHOTO);
        query.append(QUESTIONM);
        appendPhotoIdFilter(sequenceId, sequenceIndex);
        return build();
    }

    private String buildRetrieveByIdQuery(final String method, final Long id) {
        query.append(method);
        query.append(QUESTIONM);
        query.append(RequestConstants.ID).append(EQ).append(id);
        return build();
    }


    private void appendPhotoIdFilter(final Long sequenceId, final Integer sequenceIndex) {
        query.append(RequestConstants.SEQUENCE_ID).append(EQ).append(sequenceId);
        query.append(AND).append(RequestConstants.SEQUENCE_INDEX).append(EQ).append(sequenceIndex);
    }

    String buildListSignsQuery() {
        query.append(RequestConstants.LIST_SIGNS);
        return build();
    }

    String buildListRegionsQuery() {
        query.append(RequestConstants.LIST_REGIONS);
        return build();
    }

    private void appendExcludedSignTypeFilter() {
        query.append(AND).append(RequestConstants.EXCLUDED_SIGN_TYPES);
        query.append(EQ).append(HttpUtil.utf8Encode(RequestConstants.BLURRING_TYPE));
        query.append(COMMA).append(HttpUtil.utf8Encode(RequestConstants.PHOTO_QUALITY_TYPE));
        query.append(COMMA).append(HttpUtil.utf8Encode(RequestConstants.POI));
    }

    private void appendFormatFilter(final StringBuilder query) {
        query.append(RequestConstants.FORMAT).append(EQ).append(RequestConstants.FORMAT_VAL);
    }

    private String build() {
        return query.insert(0, ApolloServiceConfig.getInstance().getServiceUrl()).toString();
    }
}