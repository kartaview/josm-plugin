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
package org.openstreetmap.josm.plugins.openstreetview.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.openstreetmap.josm.plugins.openstreetview.argument.Circle;
import org.openstreetmap.josm.plugins.openstreetview.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetview.argument.Paging;


/**
 * Helper class, builds the content for the HTTP POST methods.
 *
 * @author Beata
 * @version $Revision$
 */
final class HttpContentBuilder {

    private static final String SEPARATOR = ",";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-DD");

    private final Map<String, String> content = new HashMap<>();


    HttpContentBuilder(final Circle circle, final ListFilter filter, final Paging paging) {
        content.put(RequestConstants.COORDINATE, circle.getCenter().getY() + SEPARATOR + circle.getCenter().getX());
        final String radius = "" + (int) circle.getRadius();
        content.put(RequestConstants.RADIUS, radius);
        if (filter != null) {
            if (filter.getDate() != null) {
                content.put(RequestConstants.DATE, DATE_FORMAT.format(filter.getDate()));
            }
            if (filter.getOsmUserId() != null) {
                content.put(RequestConstants.USER_ID, filter.getOsmUserId());
            }
        }
        if (paging == null) {
            content.put(RequestConstants.PAGE, "" + Paging.DEFAULT.getPage());
            content.put(RequestConstants.PAGE_ITEMS, "" + Paging.DEFAULT.getItemsPerPage());
        }
    }


    Map<String, String> getContent() {
        return content;
    }
}