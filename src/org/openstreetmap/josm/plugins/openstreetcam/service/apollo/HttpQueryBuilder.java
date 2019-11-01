/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ConfidenceLevelFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.ApolloServiceConfig;
import com.telenav.josm.common.argument.BoundingBox;
import com.telenav.josm.common.http.HttpUtil;


/**
 *
 * @author beataj
 * @version $Revision$
 */
class HttpQueryBuilder {

    private static final char QUESTIONM = '?';
    private static final char EQ = '=';
    private static final char AND = '&';
    private static final String DATE_FORMAT = "YYYY-MM-dd";

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

    String buildSearchDetectionsQuery(final BoundingBox area, final Date date, final Long osmUserId,
            final DetectionFilter filter) {
        query.append(RequestConstants.SEARCH_DETECTIONS);
        query.append(QUESTIONM);
        appendCommonSearchFilters(area, date, osmUserId);
        if (filter != null) {
            appendOsmComparisonFilter(filter.getOsmComparisons());
            appendEditStatusFilter(filter.getEditStatuses());
            appendSignTypeFilter(filter.getSignTypes());
            appendSignInternalNameFilter(filter.getSignInternalNames());
            appendSignRegionFilter(filter.getRegion());
            appendDetectionModeFilter(filter.getModes());
        }
        appendExcludedSignTypeFitler();
        return build();
    }

    String buildSearchClustersQuery(final BoundingBox area, final Date date, final DetectionFilter filter) {
        query.append(RequestConstants.SEARCH_CLUSTERS);
        query.append(QUESTIONM);
        appendCommonSearchFilters(area, date, null);
        if (filter != null) {
            appendOsmComparisonFilter(filter.getOsmComparisons());
            appendSignTypeFilter(filter.getSignTypes());
            appendSignInternalNameFilter(filter.getSignInternalNames());
            appendSignRegionFilter(filter.getRegion());
            appendConfidenceLevelFilter(filter.getConfidenceLevelFilter());
        }
        return build();
    }

    private void appendCommonSearchFilters(final BoundingBox area, final Date date, final Long osmUserId) {
        appendFormatFilter(query);
        appendBoundingBoxFilter(query, area);
        appendDateFilter(date);
        appendUserFilter(osmUserId);
    }

    String buildRetrieveSequenceDetectionsQuery(final Long sequenceId) {
        query.append(RequestConstants.RETRIEVE_SEQUENCE_DETECTIONS);
        query.append(QUESTIONM);
        query.append(RequestConstants.SEQUENCE_ID).append(EQ).append(sequenceId);
        appendExcludedSignTypeFitler();
        return build();
    }

    String buildRetrievePhotoDetectionsQuery(final Long sequenceId, final Integer sequenceIndex) {
        query.append(RequestConstants.RETRIEVE_PHOTO_DETECTIONS);
        query.append(QUESTIONM);
        appendPhotoIdFilter(sequenceId, sequenceIndex);
        appendExcludedSignTypeFitler();
        return build();
    }

    String buildRetrieveDetectionQuery(final Long id) {
        return buildRetrieveByIdQuery(RequestConstants.RETRIVE_DETECTION, id);
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

    private void appendUserFilter(final Long osmUserId) {
        if (osmUserId != null) {
            query.append(AND).append(RequestConstants.EXTERNAL_ID).append(EQ).append(osmUserId);
            query.append(AND).append(RequestConstants.AUTHOR_TYPE).append(EQ).append(RequestConstants.AUTHOR_TYPE_VAL);
        }
    }

    private void appendOsmComparisonFilter(final List<OsmComparison> osmComparisons) {
        if (osmComparisons != null && !osmComparisons.isEmpty()) {
            query.append(AND).append(RequestConstants.OSM_COMPARISONS).append(EQ)
            .append(HttpUtil.utf8Encode(new HashSet<>(osmComparisons)));
        }
    }

    private void appendEditStatusFilter(final List<EditStatus> editStatuses) {
        if (editStatuses != null && !editStatuses.isEmpty()) {
            final Set<String> editStatusValues = new HashSet<>();
            for (final EditStatus editStatus : editStatuses) {
                if (editStatus.equals(EditStatus.MAPPED)) {
                    editStatusValues.add(RequestConstants.EDIT_STATUS_FIXED);
                    editStatusValues.add(RequestConstants.EDIT_STATUS_ALREADY_FIXED);
                } else {
                    editStatusValues.add(editStatus.name());
                }
            }
            query.append(AND).append(RequestConstants.EDIT_STATUSES).append(EQ)
            .append(HttpUtil.utf8Encode(editStatusValues));
        }

    }

    private void appendSignTypeFilter(final List<String> signTypes) {
        if (signTypes != null && !signTypes.isEmpty()) {
            query.append(AND).append(RequestConstants.INCLUDED_SIGN_TYPES).append(EQ)
            .append(HttpUtil.utf8Encode(new HashSet<>(signTypes)));
        }
    }

    private void appendSignInternalNameFilter(final List<String> signInternalNames) {
        if (signInternalNames != null && !signInternalNames.isEmpty()) {
            query.append(AND).append(RequestConstants.SIGN_INTERNAL_NAMES).append(EQ)
            .append(HttpUtil.utf8Encode(new HashSet<>(signInternalNames)));
        }
    }

    private void appendSignRegionFilter(final String region) {
        if (region != null && !region.isEmpty()) {
            query.append(AND).append(RequestConstants.REGION).append(EQ).append(region);
        }
    }

    private void appendDetectionModeFilter(final List<DetectionMode> modes) {
        if (modes != null && !modes.isEmpty()) {
            query.append(AND).append(RequestConstants.MODES).append(EQ)
            .append(HttpUtil.utf8Encode(new HashSet<>(modes)));
        }
    }

    private void appendDateFilter(final Date date) {
        if (date != null) {
            query.append(AND).append(RequestConstants.DATE).append(EQ)
            .append(new SimpleDateFormat(DATE_FORMAT).format(date));
        }
    }

    private void appendExcludedSignTypeFitler() {
        query.append(AND).append(RequestConstants.EXCLUDED_SIGN_TYPES);
        query.append(EQ).append(HttpUtil.utf8Encode(RequestConstants.BLURRING_TYPE));
    }

    private void appendBoundingBoxFilter(final StringBuilder query, final BoundingBox area) {
        query.append(AND).append(RequestConstants.NORTH).append(EQ).append(area.getNorth());
        query.append(AND).append(RequestConstants.SOUTH).append(EQ).append(area.getSouth());
        query.append(AND).append(RequestConstants.EAST).append(EQ).append(area.getEast());
        query.append(AND).append(RequestConstants.WEST).append(EQ).append(area.getWest());
    }

    private void appendFormatFilter(final StringBuilder query) {
        query.append(RequestConstants.FORMAT).append(EQ).append(RequestConstants.FORMAT_VAL);
    }

    private void appendConfidenceLevelFilter(final ConfidenceLevelFilter confidenceLevelFilter) {
        if (confidenceLevelFilter != null) {
            if (confidenceLevelFilter.getMinConfidenceLevel() != null) {
                query.append(AND).append(RequestConstants.MIN_CONFIDENCE_LEVEL).append(EQ)
                .append(confidenceLevelFilter.getMinConfidenceLevel());
            }
            if (confidenceLevelFilter.getMaxConfidenceLevel() != null) {
                query.append(AND).append(RequestConstants.MAX_CONFIDENCE_LEVEL).append(EQ)
                .append(confidenceLevelFilter.getMaxConfidenceLevel());
            }
        }
    }

    private String build() {
        return query.insert(0, ApolloServiceConfig.getInstance().getServiceUrl()).toString();
    }
}