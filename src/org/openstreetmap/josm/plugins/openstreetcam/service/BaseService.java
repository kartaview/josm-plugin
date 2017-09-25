/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.openstreetmap.josm.plugins.openstreetcam.argument.UserAgent;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.service.adapter.PhotoTypeAdapter;
import org.openstreetmap.josm.plugins.openstreetcam.service.adapter.SegmentTypeAdapter;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.ListResponse;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.telenav.josm.common.http.ContentType;
import com.telenav.josm.common.http.HttpConnector;
import com.telenav.josm.common.http.HttpConnectorException;


/**
 * Provides general methods used by the service.
 *
 * @author beataj
 * @version $Revision$
 */
class BaseService {

    private final Gson gson;

    BaseService() {
        final GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        builder.registerTypeAdapter(Photo.class, new PhotoTypeAdapter());
        builder.registerTypeAdapter(Segment.class, new SegmentTypeAdapter());
        gson = builder.create();
    }


    /**
     * Executes a HTTP POST method and reads the service response. The response is transformed to the specified type.
     *
     * @param url represents the service URL
     * @param content represents the request's body
     * @param responseType represents the response type
     * @return a {@code T} object
     * @throws ServiceException if the operation failed
     */
    <T> T executePost(final String url, final Map<String, String> arguments, final Type responseType)
            throws ServiceException {
        final String response;
        try {
            final HttpConnector connector = new HttpConnector(url, getHeaders());
            response = connector.post(arguments, ContentType.X_WWW_FORM_URLENCODED);
        } catch (final HttpConnectorException e) {
            throw new ServiceException(e);
        }
        return parseResponse(response, responseType);
    }


    private <T> T parseResponse(final String response, final Type responseType) throws ServiceException {
        T root = null;
        if (response != null) {
            try {
                root = gson.fromJson(response, responseType);
            } catch (final JsonSyntaxException e) {
                throw new ServiceException(e);
            }
        }
        return root;
    }

    /**
     * Verifies the response status.
     *
     * @param response a represents the response of the service
     * @throws ServiceException if the response contains a HTTP error code
     */
    void verifyResponseStatus(final Response response) throws ServiceException {
        if (response != null && response.getStatus() != null && response.getStatus().isErrorHttpCode()) {
            throw new ServiceException(response.getStatus().getApiMessage());
        }
    }

    /**
     * Read the results from the given list of {@code Future}s.
     *
     * @param futures a list of {@code Future}s containing the result of parallel executed threads
     * @return a set of objects of the {@code T}
     * @throws ServiceException if the thread execution failed or if the process was interrupted.
     */
    <T> Set<T> readResult(final List<Future<ListResponse<T>>> futures) throws ServiceException {
        final Set<T> result = new HashSet<>();
        for (final Future<ListResponse<T>> future : futures) {
            try {
                result.addAll(future.get().getCurrentPageItems());
            } catch (InterruptedException | ExecutionException e) {
                throw new ServiceException(e);
            }
        }
        return result;
    }

    Map<String, String> getHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put(RequestConstants.USER_AGENT, new UserAgent().toString());
        System.out.println(new UserAgent().toString());
        return headers;
    }
}