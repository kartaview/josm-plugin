/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.photo;

import static org.openstreetmap.josm.plugins.kartaview.service.photo.RequestConstants.BBOX_BOTTOM_RIGHT;
import static org.openstreetmap.josm.plugins.kartaview.service.photo.RequestConstants.BBOX_TOP_LEFT;
import static org.openstreetmap.josm.plugins.kartaview.service.photo.RequestConstants.PAGE;
import static org.openstreetmap.josm.plugins.kartaview.service.photo.RequestConstants.PAGE_ITEMS;
import static org.openstreetmap.josm.plugins.kartaview.service.photo.RequestConstants.SEQUENCE_ID;
import static org.openstreetmap.josm.plugins.kartaview.service.photo.RequestConstants.SEQUENCE_INDEX;
import static org.openstreetmap.josm.plugins.kartaview.service.photo.RequestConstants.ZOOM;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.openstreetmap.josm.plugins.kartaview.service.Paging;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import com.grab.josm.common.argument.BoundingBox;
import com.grab.josm.common.formatter.DateFormatter;


/**
 * Helper class, builds the content for the HTTP POST methods.
 *
 * @author Beata
 * @version $Revision$
 */
final class HttpContentBuilder {

    private static final String SEPARATOR = ",";
    private final Map<String, String> content = new HashMap<>();


    HttpContentBuilder(final BoundingBox area, final Date date, final Paging paging) {
        content.put(BBOX_TOP_LEFT, area.getNorth() + SEPARATOR + area.getWest());
        content.put(BBOX_BOTTOM_RIGHT, area.getSouth() + SEPARATOR + area.getEast());
        if (date != null) {
            content.put(RequestConstants.DATE, DateFormatter.formatDay(date));
        }

        if (paging == null) {

            addPaging(Paging.NEARBY_PHOTOS_DEFAULT);
        } else {
            addPaging(paging);
        }
    }

    HttpContentBuilder(final BoundingBox area, final int zoom, final Paging paging) {
        content.put(BBOX_TOP_LEFT, area.getNorth() + SEPARATOR + area.getWest());
        content.put(BBOX_BOTTOM_RIGHT, area.getSouth() + SEPARATOR + area.getEast());

        if (zoom >= Config.getInstance().getTracksMaxZoom()) {
            content.put(ZOOM, Integer.toString(Config.getInstance().getTracksMaxZoom()));
        } else {
            content.put(ZOOM, Integer.toString(zoom));
        }

        if (paging == null) {
            addPaging(Paging.TRACKS_DEFAULT);
        } else {
            addPaging(paging);
        }
    }

    HttpContentBuilder(final Long id) {
        content.put(SEQUENCE_ID, id.toString());
    }

    HttpContentBuilder(final Long sequenceId, final Integer sequenceIndex) {
        content.put(SEQUENCE_ID, Long.toString(sequenceId));
        content.put(SEQUENCE_INDEX, Integer.toString(sequenceIndex));
    }

    Map<String, String> getContent() {
        return content;
    }

    private void addPaging(final Paging paging) {
        content.put(PAGE, Integer.toString(paging.getPage()));
        content.put(PAGE_ITEMS, Integer.toString(paging.getItems()));
    }
}