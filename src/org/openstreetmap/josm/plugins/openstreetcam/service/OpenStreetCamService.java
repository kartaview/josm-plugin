/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Paging;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.ListResponse;
import org.openstreetmap.josm.plugins.openstreetcam.service.entity.SequencePhotoListResponse;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.OpenStreetCamServiceConfig;
import com.google.gson.reflect.TypeToken;
import com.telenav.josm.common.argument.BoundingBox;
import com.telenav.josm.common.http.HttpConnector;
import com.telenav.josm.common.http.HttpConnectorException;


/**
 * Executes the operations of the OpenStreetCam service.
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetCamService extends BaseService {

    private static final int SECOND_PAGE = 2;

    /**
     * Retrieves OpenStreetCam photos from the given area based on the specified filters.
     *
     * @param area a {@code BoundingBox} defines the searching area
     * @param date a {@code Date} if not null, then the method returns the photos that were uploaded after the specified
     * date
     * @param osmUserId a {@code Long} specifies the user's OSM identifier; if not null return only the photos that were
     * uploaded by the logged in user
     * @param paging a {@code Paging} represents the pagination for the data set
     * @return a list of {@code Photo}s
     * @throws ServiceException if the operation fails
     */
    public PhotoDataSet listNearbyPhotos(final BoundingBox area, final Date date, final Long osmUserId,
            final Paging paging) throws ServiceException {
        final Map<String, String> arguments = new HttpContentBuilder(area, date, osmUserId, paging).getContent();
        final String url = new StringBuilder(OpenStreetCamServiceConfig.getInstance().getServiceUrl())
                .append(RequestConstants.LIST_NEARBY_PHOTOS).toString();
        final ListResponse<Photo> listPhotoResponse =
                executePost(url, arguments, new TypeToken<ListResponse<Photo>>() {}.getType());
        verifyResponseStatus(listPhotoResponse);
        return listPhotoResponse != null ? new PhotoDataSet(listPhotoResponse.getCurrentPageItems(), paging.getPage(),
                listPhotoResponse.getTotalItems()) : new PhotoDataSet();
    }

    /**
     * Retrieves the sequence associated with the given identifier.
     *
     * @param id a sequence identifier
     * @return a {@code Sequence} that contains all the photos that belongs to the sequence
     * @throws ServiceException if the operation fails
     */
    public Sequence retrieveSequence(final Long id) throws ServiceException {
        final Map<String, String> arguments = new HttpContentBuilder(id).getContent();
        final String url = new StringBuilder(OpenStreetCamServiceConfig.getInstance().getServiceUrl())
                .append(RequestConstants.SEQUENCE_PHOTO_LIST).toString();
        final SequencePhotoListResponse detailsResponse = executePost(url, arguments, SequencePhotoListResponse.class);
        verifyResponseStatus(detailsResponse);

        Sequence sequence = null;
        if (detailsResponse != null) {
            sequence = detailsResponse.getOsv();

            // order photos by sequence index
            sequence.getPhotos().sort((p1, p2) -> p1.getSequenceIndex().compareTo(p2.getSequenceIndex()));
        }
        return sequence;
    }

    /**
     * Retrieves the photo with the given name.
     *
     * @param photoName represents the full name (contains also the path) of an image
     * @return the photo in byte format
     * @throws ServiceException if the operation failed
     */
    public byte[] retrievePhoto(final String photoName) throws ServiceException {
        final StringBuilder url = new StringBuilder(OpenStreetCamServiceConfig.getInstance().getServiceBaseUrl());
        url.append(photoName);
        byte[] image;
        try {
            final HttpConnector connector = new HttpConnector(url.toString(), getHeaders());
            image = connector.getBytes();
        } catch (final HttpConnectorException e) {
            throw new ServiceException(e);
        }
        return image;
    }

    /**
     * Returns a list of segments that has OpenStreetCam coverage from the given area.
     *
     * @param area a {@code BoundingBox} represents the current area
     * @param osmUserId a {@code Long} specifies the user's OSM identifier; if not null return only the photos that were
     * uploaded by the logged in user
     * @param zoom represents the current zoom level
     * @return a list of {@code Segment}s
     * @throws ServiceException if the operation fails
     */
    public List<Segment> listMatchedTracks(final BoundingBox area, final Long osmUserId, final int zoom)
            throws ServiceException {
        final ListResponse<Segment> listSegmentResponse =
                listMatchedTacks(area, osmUserId, zoom, Paging.TRACKS_DEFAULT);
        final Set<Segment> segments = new HashSet<>();
        if (listSegmentResponse != null) {
            segments.addAll(listSegmentResponse.getCurrentPageItems());
            if (listSegmentResponse.getTotalItems() > Config.getInstance().getTracksMaxItems()) {
                final int pages = listSegmentResponse.getTotalItems() > Config.getInstance().getTracksMaxItems()
                        ? (listSegmentResponse.getTotalItems() / Config.getInstance().getTracksMaxItems()) + 1
                                : SECOND_PAGE;
                final ExecutorService executor = Executors.newFixedThreadPool(pages);
                final List<Future<ListResponse<Segment>>> futures = new ArrayList<>();
                for (int i = SECOND_PAGE; i <= pages; i++) {
                    final Paging paging = new Paging(i, Config.getInstance().getTracksMaxItems());
                    final Callable<ListResponse<Segment>> callable =
                            () -> listMatchedTacks(area, osmUserId, zoom, paging);
                            futures.add(executor.submit(callable));
                }
                segments.addAll(readResult(futures));
                executor.shutdown();
            }
        }
        return new ArrayList<>(segments);
    }

    private ListResponse<Segment> listMatchedTacks(final BoundingBox area, final Long osmUserId, final int zoom,
            final Paging paging) throws ServiceException {
        final Map<String, String> arguments = new HttpContentBuilder(area, osmUserId, zoom, paging).getContent();
        final String url = new StringBuilder(OpenStreetCamServiceConfig.getInstance().getServiceBaseUrl())
                .append(RequestConstants.LIST_MATCHED_TRACKS).toString();
        final ListResponse<Segment> listSegmentResponse =
                executePost(url, arguments, new TypeToken<ListResponse<Segment>>() {}.getType());
        verifyResponseStatus(listSegmentResponse);
        return listSegmentResponse;
    }
}