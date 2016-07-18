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
package org.openstreetmap.josm.plugins.openstreetview.gui.details;

import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import com.telenav.josm.common.formatter.EntityFormatter;


/**
 * Utility class, formats custom objects.
 *
 * @author Beata
 * @version $Revision$
 */
final class Formatter {

    private Formatter() {}

    /**
     * Returns a string containing the upload date and username of the user who had uploaded the photo.
     *
     * @param photo a {@code Photo} represents the currently selected photo
     * @return a {@code String}
     */
    static String formatPhotoDetails(final Photo photo) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Uploaded");
        if (photo.getUsername() != null && !photo.getUsername().isEmpty()) {
            sb.append(" by ").append(photo.getUsername()).append(" ");
        }
        sb.append(" on ").append(EntityFormatter.formatTimestamp(photo.getTimestamp()));
        return sb.toString();
    }
}