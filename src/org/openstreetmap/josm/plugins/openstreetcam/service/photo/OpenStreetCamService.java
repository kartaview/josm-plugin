/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.photo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.service.BaseService;
import org.openstreetmap.josm.plugins.openstreetcam.service.ClientLogger;
import org.openstreetmap.josm.plugins.openstreetcam.service.ServiceException;
import org.openstreetmap.josm.plugins.openstreetcam.service.photo.adapter.PhotoTypeAdapter;
import org.openstreetmap.josm.plugins.openstreetcam.service.photo.adapter.SegmentTypeAdapter;
import org.openstreetmap.josm.plugins.openstreetcam.service.photo.entity.ListResponse;
import org.openstreetmap.josm.plugins.openstreetcam.service.photo.entity.PhotoDetailsResponse;
import org.openstreetmap.josm.plugins.openstreetcam.service.photo.entity.SequencePhotoListResponse;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.OpenStreetCamServiceConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.grab.josm.common.argument.BoundingBox;
import com.grab.josm.common.http.HttpConnector;
import com.grab.josm.common.http.HttpConnectorException;


/**
 * Executes the operations of the OpenStreetCam service.
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetCamService extends BaseService {

    private static final int SECOND_PAGE = 2;
    private static final ClientLogger logger = new ClientLogger("oscApi");

    @Override
    public Gson createGson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        builder.registerTypeAdapter(Photo.class, new PhotoTypeAdapter());
        builder.registerTypeAdapter(Segment.class, new SegmentTypeAdapter());
        return builder.create();
    }


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
        final String url =
                OpenStreetCamServiceConfig.getInstance().getServiceUrl().concat(RequestConstants.LIST_NEARBY_PHOTOS);
        final ListResponse<Photo> listPhotoResponse = executePost(url, arguments,
                new TypeToken<ListResponse<Photo>>() {}.getType(), logger, RequestConstants.LIST_NEARBY_PHOTOS);
        verifyResponseStatus(listPhotoResponse);
        int size = 0;
        PhotoDataSet dataSet = new PhotoDataSet();
        if (listPhotoResponse != null) {
            size = listPhotoResponse.getTotalItems();
            dataSet = new PhotoDataSet(listPhotoResponse.getCurrentPageItems(), paging.getPage(),
                    listPhotoResponse.getTotalItems());
        }
        logResponseSize(logger, RequestConstants.LIST_NEARBY_PHOTOS, size);
        return dataSet;
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
        final String url =
                OpenStreetCamServiceConfig.getInstance().getServiceUrl().concat(RequestConstants.SEQUENCE_PHOTO_LIST);
        final SequencePhotoListResponse detailsResponse = executePost(url, arguments, SequencePhotoListResponse.class,
                logger, RequestConstants.SEQUENCE_PHOTO_LIST);
        verifyResponseStatus(detailsResponse);

        Sequence sequence = null;
        if (detailsResponse != null) {
            sequence = detailsResponse.getOsv();

            // order photos by sequence index
            sequence.getPhotos().sort(Comparator.comparing(Photo::getSequenceIndex));
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
        final String url = OpenStreetCamServiceConfig.getInstance().getServiceBaseUrl().concat(photoName);
        byte[] image;
        try {
            final HttpConnector connector = new HttpConnector(url, getHeaders());
            final Instant startTime = Instant.now();
            image = connector.getBytes();
            final Instant endTime = Instant.now();
            logger.log(
                    "retrievePhoto " + url + " responded in " + Duration.between(startTime, endTime).toMillis() + "ms",
                    null);
        } catch (final HttpConnectorException e) {
            logger.log("Error calling " + url, e);
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
            if (listSegmentResponse.getTotalItems() > OpenStreetCamServiceConfig.getInstance().getTracksMaxItems()) {
                final int pages = listSegmentResponse.getTotalItems() > OpenStreetCamServiceConfig.getInstance()
                        .getTracksMaxItems()
                                ? (listSegmentResponse.getTotalItems()
                                        / OpenStreetCamServiceConfig.getInstance().getTracksMaxItems()) + 1
                                : SECOND_PAGE;
                final ExecutorService executor = Executors.newFixedThreadPool(pages);
                final List<Future<ListResponse<Segment>>> futures = new ArrayList<>();
                for (int i = SECOND_PAGE; i <= pages; i++) {
                    final Paging paging = new Paging(i, OpenStreetCamServiceConfig.getInstance().getTracksMaxItems());
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
        final String url = OpenStreetCamServiceConfig.getInstance().getServiceBaseUrl()
                .concat(RequestConstants.LIST_MATCHED_TRACKS);
        final ListResponse<Segment> listSegmentResponse = executePost(url, arguments,
                new TypeToken<ListResponse<Segment>>() {}.getType(), logger, RequestConstants.LIST_MATCHED_TRACKS);
        verifyResponseStatus(listSegmentResponse);
        logResponseSize(logger, RequestConstants.LIST_MATCHED_TRACKS, listSegmentResponse.getTotalItems());
        return listSegmentResponse;
    }

    public Photo retrievePhotoDetails(final Long sequenceId, final Integer sequenceIndex) throws ServiceException {
        final Map<String, String> arguments = new HttpContentBuilder(sequenceId, sequenceIndex).getContent();
        final String url =
                OpenStreetCamServiceConfig.getInstance().getServiceUrl().concat(RequestConstants.PHOTO_DETAILS);
        final PhotoDetailsResponse result =
                executePost(url, arguments, PhotoDetailsResponse.class, logger, RequestConstants.PHOTO_DETAILS);
        verifyResponseStatus(result);
        return result != null && result.getOsv() != null ? result.getOsv().getPhotoObject() : null;
    }
}