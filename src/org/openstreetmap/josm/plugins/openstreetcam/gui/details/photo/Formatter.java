/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;


/**
 * Utility class, formats custom objects.
 *
 * @author Beata
 * @version $Revision$
 */
final class Formatter {

    private static final String EMPTY_DATE = "0000-00-00 00:00:00";

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
        if (photo.getShotDate() != null && !EMPTY_DATE.equals(photo.getShotDate())) {
            sb.append(" on ").append(photo.getShotDate());
        }
        if (photo.getUsername() != null && !photo.getUsername().isEmpty()) {
            sb.append(" by ").append("<a href='' target='_blank'>");
            sb.append(photo.getUsername()).append("</a>");
        }
        if (photo.getDetections() != null && !photo.getDetections().isEmpty()) {
            final int detectionsNr = photo.getDetections().size();
            if(detectionsNr == 1) {
                sb.append(" (1 detection).");
            } else {
                sb.append(" (").append(detectionsNr).append(" detections).");
            }
        }
        sb.append("</html>");
        return sb.toString();
    }
}