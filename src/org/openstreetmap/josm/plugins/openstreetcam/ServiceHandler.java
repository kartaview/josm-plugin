/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2016, Telenav, Inc. All Rights Reserved
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
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.JosmUserIdentityManager;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.service.Service;
import org.openstreetmap.josm.plugins.openstreetcam.service.ServiceException;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Executes the service operations corresponding to user actions. If an operation fails, a corresponding message is
 * displayed to the user.
 *
 * @author Beata
 * @version $Revision$
 */
final class ServiceHandler {

    private static final ServiceHandler INSTANCE = new ServiceHandler();
    private final Service service;

    private ServiceHandler() {
        service = new Service();
    }


    static ServiceHandler getInstance() {
        return INSTANCE;
    }


    /**
     * Lists the photos from the current area based on the given filters.
     *
     * @param areas a list of {@code Circle}s representing the search areas. If the OsmDataLayer is active, there might
     * be several bounds.
     * @param filter a {@code Filter} represents the user's search filters. Null values are ignored.
     * @return a list of {@code Photo}s
     */
    List<Photo> listNearbyPhotos(final List<Circle> areas, final ListFilter filter) {
        final Long osmUserId = osmUserId(filter);
        final Date date = filter != null ? filter.getDate() : null;
        List<Photo> finalResult = new ArrayList<>();
        try {
            if (areas.size() > 1) {
                // special case: there are several different areas visible in the OSM data layer
                final ExecutorService executor = Executors.newFixedThreadPool(areas.size());
                final List<Future<List<Photo>>> futures = new ArrayList<>();
                for (final Circle circle : areas) {
                    final Callable<List<Photo>> callable = () -> service.listNearbyPhotos(circle, date, osmUserId);
                    futures.add(executor.submit(callable));
                }
                finalResult.addAll(readResult(futures));
                executor.shutdown();
            } else {
                finalResult = service.listNearbyPhotos(areas.get(0), date, osmUserId);
            }
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final int val = JOptionPane.showOptionDialog(Main.map.mapView,
                        GuiConfig.getInstance().getErrorPhotoListTxt(), GuiConfig.getInstance().getErrorTitle(),
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                final boolean flag = val == JOptionPane.YES_OPTION;
                PreferenceManager.getInstance().savePhotosErrorSuppressFlag(flag);
            }
        }
        return finalResult;
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
    List<Segment> listMatchedTracks(final List<BoundingBox> areas, final ListFilter filter, final int zoom) {
        List<Segment> finalResult = new ArrayList<>();
        final Long osmUserId = osmUserId(filter);
        try {
            if (areas.size() > 1) {
                // special case: there are several different areas visible in the OSM data layer
                final ExecutorService executor = Executors.newFixedThreadPool(areas.size());
                final List<Future<List<Segment>>> futures = new ArrayList<>();
                for (final BoundingBox bbox : areas) {
                    final Callable<List<Segment>> callable = () -> service.listMatchedTracks(bbox, osmUserId, zoom);
                    futures.add(executor.submit(callable));
                }
                finalResult.addAll(readResult(futures));
                executor.shutdown();
            } else {
                finalResult = service.listMatchedTracks(areas.get(0), osmUserId, zoom);
            }
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadPhotosErrorSuppressFlag()) {
                final int val = JOptionPane.showOptionDialog(Main.map.mapView,
                        GuiConfig.getInstance().getErrorPhotoListTxt(), GuiConfig.getInstance().getErrorTitle(),
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                final boolean flag = val == JOptionPane.YES_OPTION;
                PreferenceManager.getInstance().savePhotosErrorSuppressFlag(flag);
            }
        }
        return finalResult;
    }


    private Long osmUserId(final ListFilter filter) {
        Long osmUserId = null;
        if (filter != null && filter.isOnlyUserFlag() && JosmUserIdentityManager.getInstance().isFullyIdentified()
                && JosmUserIdentityManager.getInstance().asUser().getId() > 0) {
            osmUserId = JosmUserIdentityManager.getInstance().asUser().getId();
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
     * Retries the sequence corresponding to the given identifier
     *
     * @param id a sequence identifier
     * @return a {@code Sequence}
     */
    Sequence retrieveSequence(final Long id) {
        Sequence sequence = null;
        try {
            sequence = service.retrieveSequence(id);
        } catch (final ServiceException e) {
            if (!PreferenceManager.getInstance().loadSequenceErrorSuppressFlag()) {
                final int val = JOptionPane.showOptionDialog(Main.map.mapView,
                        GuiConfig.getInstance().getErrorSequenceTxt(), GuiConfig.getInstance().getErrorTitle(),
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
        return service.retrievePhoto(photoName);
    }
}