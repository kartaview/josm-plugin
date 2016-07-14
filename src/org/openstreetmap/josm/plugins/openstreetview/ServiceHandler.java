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
import java.util.List;
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


    static ServiceHandler getInstance() {
        return INSTANCE;
    }

    List<Photo> listNearbyPhotos(final Circle circle, final ListFilter filter) {
        List<Photo> result = new ArrayList<>();
        final Long userId = filter != null && filter.isOnlyUserFlag()
                ? JosmUserIdentityManager.getInstance().asUser().getId() : null;
        final Date date = filter != null ? filter.getDate() : null;
        try {
            result = service.listNearbyPhotos(circle, date, userId, Paging.DEFAULT);
        } catch (final OpenStreetViewServiceException e) {
            JOptionPane.showMessageDialog(Main.parent, e.getMessage(), GuiConfig.getInstance().getPhotoErrorTxt(),
                    JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }
}