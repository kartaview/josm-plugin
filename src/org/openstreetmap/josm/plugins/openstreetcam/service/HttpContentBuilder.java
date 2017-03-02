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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetcam.argument.Paging;
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
        content.put(RequestConstants.COORDINATE, circle.getCenter().getY() + SEPARATOR + circle.getCenter().getX());
        content.put(RequestConstants.RADIUS, Integer.toString((int) circle.getRadius()));
        if (date != null) {
            content.put(RequestConstants.DATE, new SimpleDateFormat(DATE_FORMAT).format(date));
        }
        addOsmUserId(osmUserId);
        addPaging(paging);
    }

    HttpContentBuilder(final BoundingBox area, final Long osmUserId, final int zoom, final Paging paging) {
        content.put(RequestConstants.BBOX_TOP_LEFT, area.getNorth() + SEPARATOR + area.getWest());
        content.put(RequestConstants.BBOX_BOTTOM_RIGHT, area.getSouth() + SEPARATOR + area.getEast());
        content.put(RequestConstants.ZOOM, Integer.toString(zoom));
        addOsmUserId(osmUserId);
        addPaging(paging);
    }

    HttpContentBuilder(final Long id) {
        content.put(RequestConstants.ID, id.toString());
    }

    Map<String, String> getContent() {
        return content;
    }

    private void addOsmUserId(final Long osmUserId) {
        if (osmUserId != null && osmUserId > 0) {
            content.put(RequestConstants.USER_ID, Long.toString(osmUserId));
        }
    }

    private void addPaging(final Paging paging) {
        if (paging == null) {
            content.put(RequestConstants.PAGE, Integer.toString(Paging.DEFAULT.getPage()));
            content.put(RequestConstants.PAGE_ITEMS, Integer.toString(Paging.DEFAULT.getItemsPerPage()));
        } else {
            content.put(RequestConstants.PAGE, Integer.toString(paging.getPage()));
            content.put(RequestConstants.PAGE_ITEMS, Integer.toString(paging.getItemsPerPage()));
        }
    }
}