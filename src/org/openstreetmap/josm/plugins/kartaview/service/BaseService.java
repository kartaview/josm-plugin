/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.openstreetmap.josm.plugins.kartaview.service.entity.BaseResponse;
import org.openstreetmap.josm.plugins.kartaview.service.photo.entity.ListResponse;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.PluginException;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.kartaview.argument.UserAgent;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.grab.josm.common.http.ContentType;
import com.grab.josm.common.http.HttpConnector;
import com.grab.josm.common.http.HttpConnectorException;
import javax.swing.JOptionPane;


/**
 * Provides general methods used by the service.
 *
 * @author beataj
 * @version $Revision$
 */
public abstract class BaseService {

    private static final String USER_AGENT = "User-Agent";
    private static final String PLUGIN_VERSION = "Plugin-Version";
    private static final ClientLogger logger = new ClientLogger("error");
    private static final String SPACE = " ";

    private final Gson gson;

    protected BaseService() {
        gson = createGson();
    }

    protected abstract Gson createGson();

    /**
     * Executes a HTTP POST method and reads the service response. The response is transformed to the specified type.
     *
     * @param <T> represents the type the objects that will be returned by the POST method
     * @param url represents the service URL
     * @param arguments represents the request's body
     * @param responseType represents the response type
     * @return a {@code T} object
     * @throws ServiceException if the operation failed
     */
    protected <T> T executePost(final String url, final Map<String, String> arguments, final Type responseType,
            final ClientLogger serviceLogger, final String componentName)
            throws ServiceException {
        final String response;
        try {
            final HttpConnector connector = new HttpConnector(url, getHeaders());
            final Instant startTime = Instant.now();
            response = connector.post(arguments, ContentType.X_WWW_FORM_URLENCODED);
            final Instant endTime = Instant.now();
            serviceLogger
                    .log(componentName + SPACE + url + " with arguments: " + arguments + " responded in " + Duration
                            .between(startTime, endTime).toMillis() + "ms", null);
        } catch (final HttpConnectorException e) {
            logger.log("Error calling " + url, e);
            throw new ServiceException(e);
        }
        return parseResponse(response, responseType);
    }

    protected <T> T executePost(final String url, final String content, final Class<T> responseType,
            final ClientLogger serviceLogger, final String componentName)
            throws ServiceException {
        String response;
        try {
            final Instant startTime = Instant.now();
            response = new HttpConnector(url).post(content, ContentType.JSON);
            final Instant endTime = Instant.now();
            serviceLogger.log(componentName + SPACE + url + " with content: " + content + " responded in " + Duration
                    .between(startTime, endTime).toMillis() + "ms", null);
        } catch (final HttpConnectorException e) {
            logger.log("Error calling " + url, e);
            throw new ServiceException(e);
        }
        return parseResponse(response, responseType);
    }

    protected <T> T executeGet(final String url, final Class<T> responseType, final ClientLogger serviceLogger,
            final String componentName) throws ServiceException {
        String response;
        try {
            final Instant startTime = Instant.now();
            response = new HttpConnector(url, getHeaders()).get();
            final Instant endTime = Instant.now();
            serviceLogger.log(componentName + SPACE + url + " responded in " + Duration.between(startTime, endTime)
                    .toMillis() + "ms", null);
        } catch (final HttpConnectorException e) {
            logger.log("Error calling " + url, e);
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
                logger.log("Error parsing json for" + responseType.getTypeName(), e);
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
    protected void verifyResponseStatus(final BaseResponse response) throws ServiceException {
        if (response != null && response.getStatus() != null && response.getStatus().isErrorHttpCode()) {
            throw new ServiceException(response.getStatus().getApiMessage());
        }
    }

    /**
     * Read the results from the given list of {@code Future}s.
     *
     * @param <T> represents the type the objects that will be read
     * @param futures a list of {@code Future}s containing the result of parallel executed threads
     * @return a set of objects of the {@code T}
     * @throws ServiceException if the thread execution failed or if the process was interrupted.
     */
    protected <T> Set<T> readResult(final List<Future<ListResponse<T>>> futures) throws ServiceException {
        final Set<T> result = new HashSet<>();
        for (final Future<ListResponse<T>> future : futures) {
            try {
                result.addAll(future.get().getCurrentPageItems());
            } catch (InterruptedException | ExecutionException e) {
                logger.log("Error reading result (readResult).", e);
                throw new ServiceException(e);
            }
        }
        return result;
    }

    protected Map<String, String> getHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put(USER_AGENT, new UserAgent().toString());
        final String version = this.getPluginVersion();
        if (version != null && !version.isEmpty()) {
            headers.put(PLUGIN_VERSION, version);
        }
        return headers;
    }

    private String getPluginVersion() {
        PluginInformation pluginInfo = null;
        try {
            pluginInfo = PluginInformation.findPlugin(GuiConfig.getInstance().getPluginShortName());
        } catch (final PluginException e) {
            JOptionPane.showMessageDialog(MainApplication.getMainFrame(),
                    GuiConfig.getInstance().getErrorPluginVersionText(), GuiConfig.getInstance().getErrorTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
        return pluginInfo != null ? pluginInfo.version : null;
    }

    protected <T> String buildRequest(final T request, final Class<T> requestType) {
        return gson.toJson(request, requestType);
    }

    protected void logResponseSize(final ClientLogger logger, final String componentName, final List<?> response) {
        final int size = response != null ? response.size() : -1;
        logger.log(componentName + " returned:  " + size + " elements.", null);
    }

    protected void logResponseSize(final ClientLogger logger, final String componentName, final int size) {
        logger.log(componentName + " returned:  " + size + " elements.", null);
    }
}