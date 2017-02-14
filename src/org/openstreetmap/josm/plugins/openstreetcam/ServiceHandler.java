/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2016, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
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
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.JosmUserIdentityManager;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Paging;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.service.Service;
import org.openstreetmap.josm.plugins.openstreetcam.service.ServiceException;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.ServiceConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


/**
 * Executes the service operations corresponding to user actions. If an operation fails, a corresponding message is
 * displayed to the user.
 *
 * @author Beata
 * @version $Revision$
 */
public final class ServiceHandler {

    private static final ServiceHandler INSTANCE = new ServiceHandler();
    private final Service service;


    private ServiceHandler() {
        service = new Service();
    }


    public static ServiceHandler getInstance() {
        return INSTANCE;
    }


    /**
     * Lists the photos from the current area based on the given filters.
     *
     * @param areas a list of {@code Circle}s representing the search areas. If the OsmDataLayer is active, there might
     * be several bounds.
     * @param filter a {@code Filter} represents the user's search filters. Null values are ignored
     * @return a list of {@code Photo}s
     */
    public List<Photo> listNearbyPhotos(final List<Circle> areas, final ListFilter filter) {
        Long osmUserId =
                filter != null && filter.isOnlyUserFlag() ? (JosmUserIdentityManager.getInstance().asUser().getId() > 0
                        ? JosmUserIdentityManager.getInstance().asUser().getId() : null) : null;
        final Date date = filter != null ? filter.getDate() : null;
        List<Photo> finalResult = new ArrayList<>();
        try {
            if (areas.size() > 1) {
                final ExecutorService executor = Executors.newFixedThreadPool(areas.size());
                final List<Future<List<Photo>>> futures = new ArrayList<>();
                for (final Circle circle : areas) {
                    final Callable<List<Photo>> callable = () -> {
                        return service.listNearbyPhotos(circle, date, osmUserId, Paging.DEFAULT);
                    };
                    futures.add(executor.submit(callable));
                }
                finalResult.addAll(readResult(futures));
                executor.shutdown();
            } else {
                finalResult = service.listNearbyPhotos(areas.get(0), date, osmUserId, Paging.DEFAULT);
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


    private Set<Photo> readResult(final List<Future<List<Photo>>> futures) throws ServiceException {
        final Set<Photo> result = new HashSet<>();
        for (final Future<List<Photo>> future : futures) {
            try {
                result.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new ServiceException(e);
            }
        }
        return result;
    }

    public Sequence retrieveSequence(final Long id) {
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

    public BufferedImage loadImage(final String photoName) throws IOException {
        final StringBuilder link = new StringBuilder(ServiceConfig.getInstance().getBaseUrl());
        link.append(photoName);
        ImageIO.setUseCache(false);
        return ImageIO.read(new BufferedInputStream(new URL(link.toString()).openStream()));
    }
}