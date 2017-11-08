/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2016, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;

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
import org.openstreetmap.josm.plugins.openstreetcam.argument.Paging;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoTypeFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.service.FilterPack;
import org.openstreetmap.josm.plugins.openstreetcam.service.ServiceException;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.ApolloService;
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


    public Pair<PhotoDataSet, List<Detection>> searchHighZoomData(final BoundingBox area, final SearchFilter filter) {
        Pair<PhotoDataSet, List<Detection>> result;
        if (filter != null && filter.getPhotoType() != PhotoTypeFilter.WITHOUT_DETECTIONS) {
            final PhotoDataSet photoDataSet = listNearbyPhotos(area, filter, Paging.NEARBY_PHOTOS_DEAFULT);
            result = new Pair<>(photoDataSet, null);
        } else {

            final ExecutorService executorService = Executors.newFixedThreadPool(2);
            final Future<PhotoDataSet> future1 = executorService.submit(() -> {
                return listNearbyPhotos(area, filter, Paging.NEARBY_PHOTOS_DEAFULT);
            });
            final Future<List<Detection>> future2 = executorService.submit(() -> {
                return searchDetections(area, null);
            });

            PhotoDataSet photoDataSet = null;
            try {
                photoDataSet = future1.get();
            } catch (final Exception ex) {
                // handle ex
            }
            List<Detection> detections = null;
            try {
                detections = future2.get();
            } catch (final Exception ex) {
                // handle ex
            }
            executorService.shutdown();
            result = new Pair<>(photoDataSet, detections);
        }
        return result;
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
        final Long osmUserId = osmUserId(filter);
        final Date date = filter != null ? filter.getDate() : null;
        PhotoDataSet result = new PhotoDataSet();
        try {
            result = openStreetCamService.listNearbyPhotos(area, date, osmUserId, paging);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final int val = JOptionPane.showOptionDialog(MainApplication.getMap().mapView,
                        GuiConfig.getInstance().getErrorPhotoListText(), GuiConfig.getInstance().getErrorTitle(),
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                final boolean flag = val == JOptionPane.YES_OPTION;
                PreferenceManager.getInstance().savePhotosErrorSuppressFlag(flag);
            }
        }

        return result;
    }


    private List<Detection> searchDetections(final BoundingBox area, final SearchFilter searchFilter) {
        final Long osmUserId = osmUserId(searchFilter);
        final FilterPack filterPack = new FilterPack(osmUserId, searchFilter.getDate(),
                searchFilter.getOsmComparisons(), searchFilter.getEditStatuses(), searchFilter.getSignTypes());
        List<Detection> result = new ArrayList<>();
        try {
            result = apolloService.searchDetections(area, filterPack);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final int val = JOptionPane.showOptionDialog(MainApplication.getMap().mapView,
                        GuiConfig.getInstance().getErrorPhotoListText(), GuiConfig.getInstance().getErrorTitle(),
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                final boolean flag = val == JOptionPane.YES_OPTION;
                PreferenceManager.getInstance().savePhotosErrorSuppressFlag(flag);
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
        final Long osmUserId = osmUserId(filter);
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
                final int val = JOptionPane.showOptionDialog(MainApplication.getMap().mapView,
                        GuiConfig.getInstance().getErrorSegmentListText(), GuiConfig.getInstance().getErrorTitle(),
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                final boolean flag = val == JOptionPane.YES_OPTION;
                PreferenceManager.getInstance().saveSegmentsErrorSuppressFlag(flag);
            }
        }
        return finalResult;
    }


    private Long osmUserId(final SearchFilter filter) {
        Long osmUserId = null;
        if (filter != null && filter.isOnlyMineFlag() && UserIdentityManager.getInstance().isFullyIdentified()
                && UserIdentityManager.getInstance().asUser().getId() > 0) {
            osmUserId = UserIdentityManager.getInstance().asUser().getId();
        }
        return osmUserId;
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

    /**
     * Retries the sequence corresponding to the given identifier.
     *
     * @param id a sequence identifier
     * @return a {@code Sequence}
     */
    Sequence retrieveSequence(final Long id) {
        Sequence sequence = null;
        try {
            sequence = openStreetCamService.retrieveSequence(id);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final int val = JOptionPane.showOptionDialog(MainApplication.getMap().mapView,
                        GuiConfig.getInstance().getErrorSequenceText(), GuiConfig.getInstance().getErrorTitle(),
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                final boolean flag = val == JOptionPane.YES_OPTION;
                PreferenceManager.getInstance().saveSequenceErrorSuppressFlag(flag);
            }
        }
        return sequence;
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
}