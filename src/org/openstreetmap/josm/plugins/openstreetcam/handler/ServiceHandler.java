/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2016, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ImageDataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Paging;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Author;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Contribution;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.service.ServiceException;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.ApolloService;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.DetectionFilter;
import org.openstreetmap.josm.plugins.openstreetcam.service.photo.OpenStreetCamService;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.argument.BoundingBox;
import com.telenav.josm.common.entity.Pair;


/**
 * Executes the service operations corresponding to user actions. If an operation fails, a corresponding message is
 * displayed to the user.
 *
 * @author Beata
 * @version $Revision$
 */
public final class ServiceHandler {

    private static final int MAX_DATA_TYPES = 2;

    private static final ServiceHandler INSTANCE = new ServiceHandler();
    private final OpenStreetCamService openStreetCamService;
    private final ApolloService apolloService;


    private ServiceHandler() {
        openStreetCamService = new OpenStreetCamService();
        apolloService = new ApolloService();
    }

    public static ServiceHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Searches for photos and detections from the given area based on the given filters.
     *
     * @param area a {@code Circle} representing the search area.
     * @param filter a {@code SearchFilter} represents the user's search filters. Null values are ignored.
     * @return a {@code Pair} containing a {@code PhotoDataSet} and a list of {@code Detection}s
     */
    public Pair<PhotoDataSet, List<Detection>> searchHighZoomData(final BoundingBox area, final SearchFilter filter) {
        Pair<PhotoDataSet, List<Detection>> result = new Pair<>(null, null);
        if (filter.getDataTypes() != null) {
            if (filter.getDataTypes().size() == MAX_DATA_TYPES) {
                // load photos & detections
                result = loadPhotosAndDetections(area, filter);
            } else if (filter.getDataTypes().contains(ImageDataType.DETECTIONS)) {
                result = new Pair<>(null, searchDetections(area, filter));
            } else {
                result = new Pair<>(listNearbyPhotos(area, filter, Paging.NEARBY_PHOTOS_DEAFULT), null);
            }
        }
        return result;
    }

    private Pair<PhotoDataSet, List<Detection>> loadPhotosAndDetections(final BoundingBox area,
            final SearchFilter filter) {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final Future<PhotoDataSet> future1 =
                executorService.submit(() -> listNearbyPhotos(area, filter, Paging.NEARBY_PHOTOS_DEAFULT));
        final Future<List<Detection>> future2 = executorService.submit(() -> searchDetections(area, filter));

        PhotoDataSet photoDataSet = null;
        try {
            photoDataSet = future1.get();
        } catch (final Exception ex) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoListText());
                PreferenceManager.getInstance().savePhotosErrorSuppressFlag(flag);
            }
        }
        List<Detection> detections = null;
        try {
            detections = future2.get();
        } catch (final Exception ex) {
            if (!PreferenceManager.getInstance().loadDetectionSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionRetrieveText());
                PreferenceManager.getInstance().saveDetectionSearchErrorSuppressFlag(flag);
            }
        }
        executorService.shutdown();
        return new Pair<>(photoDataSet, detections);
    }

    /**
     * Lists the photos from the current area based on the given filters.
     *
     * @param area a {@code Circle} representing the search areas.
     * @param filter a {@code Filter} represents the user's search filters. Null values are ignored.
     * @param paging a {@code Paging} representing the pagination
     * @return a list of {@code Photo}s
     */
    public PhotoDataSet listNearbyPhotos(final BoundingBox area, final SearchFilter filter, final Paging paging) {
        Long osmUserId = null;
        Date date = null;
        if (filter != null) {
            osmUserId = filter.getOsmUserId();
            date = filter.getDate();
        }
        PhotoDataSet result = new PhotoDataSet();
        try {
            result = openStreetCamService.listNearbyPhotos(area, date, osmUserId, paging);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoListText());
                PreferenceManager.getInstance().savePhotosErrorSuppressFlag(flag);
            }
        }
        return result;
    }

    private List<Detection> searchDetections(final BoundingBox area, final SearchFilter filter) {
        Long osmUserId = null;
        Date date = null;
        DetectionFilter detectionFilter = null;
        if (filter != null) {
            osmUserId = filter.getOsmUserId();
            date = filter.getDate();
            detectionFilter = filter.getDetectionFilter();
        }
        List<Detection> result = null;
        try {
            result = apolloService.searchDetections(area, date, osmUserId, detectionFilter);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadDetectionSearchErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoListText());
                PreferenceManager.getInstance().saveDetectionSearchErrorSuppressFlag(flag);
            }
        }
        return result;
    }

    /**
     * Retrieves details of the given sequence based on the given filters.
     *
     * @param sequenceId the identifier of the sequence
     * @return a {code Pair} of {@code Sequence} and {@code Detection}s list
     */
    public Sequence retrieveSequence(final Long sequenceId) {
        Sequence result;
        final List<ImageDataType> dataTypesPreferences =
                PreferenceManager.getInstance().loadSearchFilter().getDataTypes();
        if (dataTypesPreferences != null && dataTypesPreferences.contains(ImageDataType.PHOTOS)
                && !dataTypesPreferences.contains(ImageDataType.DETECTIONS)) {
            result = null;
        } else {
            final ExecutorService executorService = Executors.newFixedThreadPool(2);
            final Future<Sequence> sequenceFuture = executorService.submit(() -> retrieveSequencePhotos(sequenceId));
            final Future<List<Detection>> detectionsFuture =
                    executorService.submit(() -> retrieveSequenceDetection(sequenceId));
            Sequence sequence = null;
            try {
                sequence = sequenceFuture.get();
            } catch (final Exception ex) {
                if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                    final boolean flag = handleException(GuiConfig.getInstance().getErrorSequenceText());
                    PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
                }
            }
            List<Detection> detections = null;
            try {
                detections = detectionsFuture.get();
            } catch (final Exception ex) {
                if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                    final boolean flag = handleException(GuiConfig.getInstance().getErrorSequenceText());
                    PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
                }
            }
            executorService.shutdown();
            result = new Sequence(sequenceId, sequence != null ? sequence.getPhotos() : null, detections);
        }
        return result;
    }

    private Sequence retrieveSequencePhotos(final Long id) {
        Sequence sequence = null;
        try {
            sequence = openStreetCamService.retrieveSequence(id);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorSequenceText());
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        return sequence;
    }

    private List<Detection> retrieveSequenceDetection(final Long id) {
        List<Detection> result = null;
        try {
            result = apolloService.retrieveSequenceDetections(id);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSequenceDetectionsErrorFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionRetrieveText());
                PreferenceManager.getInstance().saveSequenceDetectionsErrorFlag(flag);
            }
        }
        return result;
    }

    public List<Detection> retrievePhotoDetections(final Long sequenceId, final Integer sequenceIndex) {
        List<Detection> result = null;
        try {
            result = apolloService.retrievePhotoDetections(sequenceId, sequenceIndex);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSequenceDetectionsErrorFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionRetrieveText());
                PreferenceManager.getInstance().saveSequenceDetectionsErrorFlag(flag);
            }
        }
        return result;
    }

    /**
     * Lists the segments that have OpenStreetCam coverage from the given area(s) corresponding to the specified zoom
     * level.
     *
     * @param areas a list of {@code BoundingBox}s representing the search areas. If the OsmDataLayer is active, there
     * might be several bounding boxes.
     * @param filter a {@code Filter} represents the user's search filters. Null values are ignored.
     * @param zoom the current zoom level
     * @return a list of {@code Segment}s
     */
    public List<Segment> listMatchedTracks(final List<BoundingBox> areas, final SearchFilter filter, final int zoom) {
        List<Segment> finalResult = new ArrayList<>();
        final Long osmUserId = filter != null ? filter.getOsmUserId() : null;
        try {
            if (areas.size() > 1) {
                // special case: there are several different areas visible in the OSM data layer
                final ExecutorService executor = Executors.newFixedThreadPool(areas.size());
                final List<Future<List<Segment>>> futures = new ArrayList<>();
                for (final BoundingBox bbox : areas) {
                    final Callable<List<Segment>> callable =
                            () -> openStreetCamService.listMatchedTracks(bbox, osmUserId, zoom);
                    futures.add(executor.submit(callable));
                }
                finalResult.addAll(readResult(futures));
                executor.shutdown();
            } else {
                finalResult = openStreetCamService.listMatchedTracks(areas.get(0), osmUserId, zoom);
            }
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSegmentsErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorSegmentListText());
                PreferenceManager.getInstance().saveSegmentsErrorSuppressFlag(flag);
            }
        }
        return finalResult;
    }

    /**
     * Retrieves the photo with the given name.
     *
     * @param photoName the name of a photo
     * @return the photo content in byte array format
     * @throws ServiceException if the download operation fails
     */
    byte[] retrievePhoto(final String photoName) throws ServiceException {
        return openStreetCamService.retrievePhoto(photoName);
    }

    public Photo retrievePhotoDetails(final Long sequenceId, final Integer sequenceIndex) {
        Photo result = null;
        try {
            result = openStreetCamService.retrievePhotoDetails(sequenceId, sequenceIndex);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoLoadingText());
                PreferenceManager.getInstance().savePhotoDetectionsErrorFlag(flag);
            }
        }
        return result;
    }

    /**
     * Updates a detection.
     *
     * @param detectionId the identifier of the detection that need to be updated
     * @param editStatus a new edit status
     * @param comment a descriptive comment for the update.
     */
    public void updateDetection(final Long detectionId, final EditStatus editStatus, final String comment) {
        final Long userId = UserIdentityManager.getInstance().isFullyIdentified()
                && UserIdentityManager.getInstance().asUser().getId() > 0
                        ? UserIdentityManager.getInstance().asUser().getId() : null;
        final String userName = UserIdentityManager.getInstance().getUserName();
        if (userId == null) {
            JOptionPane.showMessageDialog(MainApplication.getMap().mapView,
                    GuiConfig.getInstance().getAuthenticationNeededErrorMessage(),
                    GuiConfig.getInstance().getWarningTitle(), JOptionPane.WARNING_MESSAGE, null);
        } else {
            final Author author = new Author(userId, userName);
            try {
                apolloService.updateDetection(new Detection(detectionId, editStatus),
                        new Contribution(author, comment));
            } catch (final ServiceException e) {
                if (!PreferenceManager.getInstance().loadDetectionUpdateErrorSuppressFlag()) {
                    final boolean flag = handleException(GuiConfig.getInstance().getErrorDetectionUpdateText());
                    PreferenceManager.getInstance().saveDetectionUpdateErrorSuppressFlag(flag);
                }
            }
        }
    }


    public Detection retrieveDetection(final Long detectionId) {
        Detection result = null;
        try {
            result = apolloService.retrieveDetection(detectionId);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final boolean flag = handleException(GuiConfig.getInstance().getErrorPhotoLoadingText());
                PreferenceManager.getInstance().savePhotoDetectionsErrorFlag(flag);
            }
        }
        return result;
    }


    private <T> Set<T> readResult(final List<Future<List<T>>> futures) throws ServiceException {
        final Set<T> result = new HashSet<>();
        for (final Future<List<T>> future : futures) {
            try {
                result.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new ServiceException(e);
            }
        }
        return result;
    }

    private boolean handleException(final String message) {
        final int val = JOptionPane.showOptionDialog(MainApplication.getMap().mapView, message,
                GuiConfig.getInstance().getErrorTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                null, null);
        return val == JOptionPane.YES_OPTION;
    }
}