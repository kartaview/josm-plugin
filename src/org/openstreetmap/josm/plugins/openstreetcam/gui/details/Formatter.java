/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;


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
        final StringBuilder sb = new StringBuilder("<html>");
        sb.append("Created");
        sb.append(" on ").append(photo.getShotDate());
        if (photo.getUsername() != null && !photo.getUsername().isEmpty()) {
            sb.append(" by ").append("<a href='' target='_blank'>");
            sb.append(photo.getUsername()).append("</a>");
        }
        sb.append("</html>");
        return sb.toString();
    }
}