/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.adapter;

import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_HEADING;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_ID;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_LATITUDE;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_LONGITUDE;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_LTH_NAME;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_NAME;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_SEQUENCE_ID;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_SEQUENCE_IDX;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_TH_NAME;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_TIMESTAMP;
import java.io.IOException;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import com.google.gson.stream.JsonWriter;


/**
 *
 * @author beataj
 * @version $Revision$
 */
final class PhotoWriter {

    public void write(final JsonWriter writer, final Photo photo) throws IOException {
        writer.beginObject();
        writer.name(PHOTO_ID).value(photo.getId());
        writer.name(PHOTO_SEQUENCE_ID).value(photo.getSequenceId());
        writer.name(PHOTO_SEQUENCE_IDX).value(photo.getSequenceIndex());
        if (photo.getLocation() != null) {
            writer.name(PHOTO_LATITUDE).value(photo.getLocation().getY());
            writer.name(PHOTO_LONGITUDE).value(photo.getLocation().getX());
        }
        writer.name(PHOTO_NAME).value(photo.getName());
        writer.name(PHOTO_LTH_NAME).value(photo.getLargeThumbnailName());
        writer.name(PHOTO_TH_NAME).value(photo.getThumbnailName());
        writer.name(PHOTO_TIMESTAMP).value(photo.getTimestamp());
        writer.name(PHOTO_HEADING).value(photo.getHeading());
        writer.endObject();
    }
}