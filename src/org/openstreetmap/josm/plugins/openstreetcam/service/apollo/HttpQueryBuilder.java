/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;

import java.text.SimpleDateFormat;
import org.openstreetmap.josm.plugins.openstreetcam.argument.FilterPack;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.ApolloServiceConfig;
import com.telenav.josm.common.argument.BoundingBox;
import com.telenav.josm.common.http.HttpUtil;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class HttpQueryBuilder {

    private static final char QUESTIONM = '?';
    private static final char EQ = '=';
    private static final char AND = '&';
    private static final String DATE_FORMAT = "YYYY-MM-dd";


    private final StringBuilder query;


    HttpQueryBuilder(final BoundingBox area, final FilterPack filterPack) {
        query = new StringBuilder(RequestConstants.SEARCH_DETECTIONS);
        query.append(QUESTIONM);

        appendFormatFilter(query);
        appendBoundingBoxFilter(query, area);
        if (filterPack != null) {
            appendFilters(filterPack);
        }
    }

    private void appendFilters(final FilterPack filterPack) {
        if (filterPack.getExternalId() != null) {
            query.append(AND).append(RequestConstants.EXTERNAL_ID).append(EQ).append(filterPack.getExternalId());
            query.append(AND).append(RequestConstants.AUTHOR_TYPE).append(EQ).append(RequestConstants.AUTHOR_TYPE_VAL);
        }

        if (filterPack.getDate() != null) {
            query.append(AND).append(RequestConstants.DATE)
            .append(new SimpleDateFormat(DATE_FORMAT).format(filterPack.getDate()));
        }

        if (filterPack.getOsmComparisons() != null && !filterPack.getOsmComparisons().isEmpty()) {
            query.append(AND).append(RequestConstants.OSM_COMPARISONS).append(EQ)
            .append(HttpUtil.utf8Encode(filterPack.getOsmComparisons()));
        }
        if (filterPack.getValidationStatuses() != null && !filterPack.getValidationStatuses().isEmpty()) {
            query.append(AND).append(RequestConstants.VALIDATION_STATUSES).append(EQ)
            .append(HttpUtil.utf8Encode(filterPack.getValidationStatuses()));
        }
        if (filterPack.getEditStatuses() != null && !filterPack.getEditStatuses().isEmpty()) {
            query.append(AND).append(RequestConstants.EDIT_STATUSES).append(EQ)
            .append(HttpUtil.utf8Encode(filterPack.getEditStatuses()));
        }

        if (filterPack.getTypes() != null && !filterPack.getTypes().isEmpty()) {
            query.append(AND).append(RequestConstants.TYPES).append(EQ)
            .append(HttpUtil.utf8Encode(filterPack.getTypes()));
        }
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

    String build() {
        final StringBuilder url = new StringBuilder(ApolloServiceConfig.getInstance().getServiceUrl());
        url.append(query);
        return url.toString();
    }
}