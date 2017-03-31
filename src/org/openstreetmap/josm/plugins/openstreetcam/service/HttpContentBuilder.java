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

import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.BBOX_BOTTOM_RIGHT;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.BBOX_TOP_LEFT;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.COORDINATE;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.EXTERNAL_USER_ID;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.ID;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.MY_TRACKS;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.MY_TRACKS_VAL;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.PAGE;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.PAGE_ITEMS;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.RADIUS;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.USER_TYPE;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.USER_TYPE_OSM;
import static org.openstreetmap.josm.plugins.openstreetcam.service.RequestConstants.ZOOM;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Paging;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Helper class, builds the content for the HTTP POST methods.
 *
 * @author Beata
 * @version $Revision$
 */
final class HttpContentBuilder {

    private static final String SEPARATOR = ",";
    private static final String DATE_FORMAT = "YYYY-MM-dd";

    private final Map<String, String> content = new HashMap<>();


    HttpContentBuilder(final Circle circle, final Date date, final Long osmUserId, final Paging paging) {
        content.put(COORDINATE, circle.getCenter().getY() + SEPARATOR + circle.getCenter().getX());
        content.put(RADIUS, Integer.toString((int) circle.getRadius()));
        if (date != null) {
            content.put(RequestConstants.DATE, new SimpleDateFormat(DATE_FORMAT).format(date));
        }

        if (osmUserId != null && osmUserId > 0) {
            content.put(EXTERNAL_USER_ID, Long.toString(osmUserId));
            content.put(USER_TYPE, USER_TYPE_OSM);
        }

        if (paging == null) {

            addPaging(Paging.NEARBY_PHOTOS_DEAFULT);
        } else {
            addPaging(paging);
        }
    }

    HttpContentBuilder(final BoundingBox area, final Long osmUserId, final int zoom, final Paging paging) {
        content.put(BBOX_TOP_LEFT, area.getNorth() + SEPARATOR + area.getWest());
        content.put(BBOX_BOTTOM_RIGHT, area.getSouth() + SEPARATOR + area.getEast());

        if (zoom >= Config.getInstance().getTracksMaxZoom()) {
            content.put(ZOOM, Integer.toString(Config.getInstance().getTracksMaxZoom()));
        } else {
            content.put(ZOOM, Integer.toString(zoom));
        }
        if (osmUserId != null && osmUserId > 0) {
            content.put(EXTERNAL_USER_ID, Long.toString(osmUserId));
            content.put(USER_TYPE, USER_TYPE_OSM);
            content.put(MY_TRACKS, MY_TRACKS_VAL);
        }
        if (paging == null) {
            addPaging(Paging.TRACKS_DEFAULT);
        } else {
            addPaging(paging);
        }
    }

    HttpContentBuilder(final Long id) {
        content.put(ID, id.toString());
    }

    Map<String, String> getContent() {
        return content;
    }

    private void addPaging(final Paging paging) {
        content.put(PAGE, Integer.toString(paging.getPage()));
        content.put(PAGE_ITEMS, Integer.toString(paging.getItemsPerPage()));
    }
}