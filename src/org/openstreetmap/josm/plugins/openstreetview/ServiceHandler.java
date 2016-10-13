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
package org.openstreetmap.josm.plugins.openstreetview;

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
import org.openstreetmap.josm.plugins.openstreetview.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetview.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetview.argument.Paging;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetview.service.OpenStreetViewService;
import org.openstreetmap.josm.plugins.openstreetview.service.OpenStreetViewServiceException;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.pref.PreferenceManager;


/**
 * Executes the service operations corresponding to user actions. If an operation fails, a corresponding message is
 * displayed to the user.
 *
 * @author Beata
 * @version $Revision$
 */
final class ServiceHandler {

    private static final ServiceHandler INSTANCE = new ServiceHandler();
    private final OpenStreetViewService service;


    private ServiceHandler() {
        service = new OpenStreetViewService();
    }

    /**
     * 
     * @return
     */
    static ServiceHandler getInstance() {
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
        final Long osmUserId = filter != null && filter.isOnlyUserFlag()
                ? JosmUserIdentityManager.getInstance().asUser().getId() : null;
        final Date date = filter != null ? filter.getDate() : null;
        List<Photo> finalResult = new ArrayList<>();
        try {
            if (areas.size() > 1) {
                Set<Photo> result = new HashSet<>();
                final ExecutorService executor = Executors.newFixedThreadPool(areas.size());
                final List<Future<List<Photo>>> futures = new ArrayList<>();
                for (Circle circle : areas) {
                    futures.add(executor.submit(new Callable<List<Photo>>() {
                        @Override
                        public List<Photo> call() throws OpenStreetViewServiceException {
                            return service.listNearbyPhotos(circle, date, osmUserId, Paging.DEFAULT);
                        }
                    }));
                }
                for (Future<List<Photo>> future : futures) {
                    try {
                        result.addAll(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        throw new OpenStreetViewServiceException(e);
                    }
                }
                finalResult.addAll(result);
                executor.shutdown();
            } else {
                finalResult = service.listNearbyPhotos(areas.get(0), date, osmUserId, Paging.DEFAULT);
            }
        } catch (final OpenStreetViewServiceException e) {
            if (!PreferenceManager.getInstance().loadErrorSuppressFlag()) {
                JOptionPane.showMessageDialog(Main.parent, e.getMessage(),
                        GuiConfig.getInstance().getErrorPhotoListTxt(), JOptionPane.ERROR_MESSAGE);
            }
        }
        return finalResult;
    }
}