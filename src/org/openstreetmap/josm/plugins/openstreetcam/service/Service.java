/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetcam.service;

import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Paging;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.ListPhotoResponse;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.SequencePhotoListResponse;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.Status;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.ServiceConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.telenav.josm.common.http.ContentType;
import com.telenav.josm.common.http.HttpConnector;
import com.telenav.josm.common.http.HttpConnectorException;


/**
 * Executes the operations of the OpenStreetView service.
 *
 * @author Beata
 * @version $Revision$
 */
public class Service {

    private final Gson gson = createGsonBuilder().create();

    private GsonBuilder createGsonBuilder() {
        final GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        builder.registerTypeAdapter(Photo.class, new PhotoTypeAdapter());
        return builder;
    }

    /**
     * Retrieves OpenStreetView photos from the given area based on the specified filters.
     *
     * @param circle a {@code Circle} defines the searching area
     * @param date a {@code Date} if not null, then the method returns the photos that were uploaded after the specified
     * date
     * @param osmUserId a {@code Long} specifies the user's OSM identifier; if not null return only the photos that were
     * uploaded by the logged in user
     * @param paging a {@code Paging} defines pagination arguments
     * @return a list of {@code Photo}s
     * @throws ServiceException if the operation failed
     */
    public List<Photo> listNearbyPhotos(final Circle circle, final Date date, final Long osmUserId, final Paging paging)
            throws ServiceException {
        final Map<String, String> arguments = new HttpContentBuilder(circle, date, osmUserId, paging).getContent();
        String response = null;
        try {
            final HttpConnector connector = new HttpConnector(
                    ServiceConfig.getInstance().getServiceUrl() + RequestConstants.LIST_NEARBY_PHOTOS);
            response = connector.post(arguments, ContentType.X_WWW_FORM_URLENCODED);
        } catch (final HttpConnectorException e) {
            throw new ServiceException(e);
        }
        final ListPhotoResponse listPhotoResponse = parseResponse(response, ListPhotoResponse.class);
        verifyResponseStatus(listPhotoResponse.getStatus());
        return listPhotoResponse.getCurrentPageItems();

    }

    public Sequence retrieveSequence(final Long id) throws ServiceException {
        final Map<String, String> arguments = new HttpContentBuilder(id).getContent();
        String response = null;
        try{
            final HttpConnector connector =
                    new HttpConnector(
                            ServiceConfig.getInstance().getServiceUrl() + RequestConstants.SEQUENCE_PHOTO_LIST);
            response = connector.post(arguments, ContentType.X_WWW_FORM_URLENCODED);

        } catch (final HttpConnectorException e) {
            throw new ServiceException(e);
        }
        // System.err.println(response);
        final SequencePhotoListResponse detailsResponse = parseResponse(response, SequencePhotoListResponse.class);
        verifyResponseStatus(detailsResponse.getStatus());
        return detailsResponse.getOsv();
    }

    private void verifyResponseStatus(final Status status) throws ServiceException {
        if (status != null && status.getHttpCode() != HttpURLConnection.HTTP_OK) {
            throw new ServiceException(status.getApiMessage());
        }
    }

    private <T> T parseResponse(final String response, final Class<T> responseType)
            throws ServiceException {
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
}