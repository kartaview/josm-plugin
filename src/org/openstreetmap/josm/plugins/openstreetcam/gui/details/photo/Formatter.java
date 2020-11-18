/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo;

import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
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
            int detectionsNr = 0;
            if (DataSet.getInstance().shouldDisplayFrontFacing()) {
                detectionsNr = photo.getDetections().size();
            } else {
                for (Detection detection : photo.getDetections()) {
                    if (detection.getShapeOnPhoto() != null
                            && detection.getShapeOnPhoto().getEquirectangularPolygon() != null && !detection
                            .getShapeOnPhoto().getEquirectangularPolygon().isEmpty()) {
                        ++detectionsNr;
                    }
                }
            }
            if (detectionsNr == 1) {
                sb.append(" (1 detection).");
            } else if (detectionsNr > 1) {
                sb.append(" (").append(detectionsNr).append(" detections).");
            }
        }
        sb.append("</html>");
        return sb.toString();
    }
}