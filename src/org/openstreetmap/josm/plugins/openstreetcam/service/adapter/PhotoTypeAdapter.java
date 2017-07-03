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
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_SHOT_DATE;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_TH_NAME;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_TIMESTAMP;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_USERNAME;
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.WAY_ID;
import java.io.IOException;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


/**
 * Custom type adapter for the {@code Photo} object.
 *
 * @author Beata
 * @version $Revision$
 */
public class PhotoTypeAdapter extends TypeAdapter<Photo> {

    @Override
    public Photo read(final JsonReader reader) throws IOException {
        Double latitude = null;
        Double longitude = null;
        final PhotoBuilder builder = new PhotoBuilder();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case PHOTO_ID:
                    builder.id(ReaderUtil.readLong(reader));
                    break;
                case PHOTO_SEQUENCE_ID:
                    builder.sequenceId(ReaderUtil.readLong(reader));
                    break;
                case PHOTO_SEQUENCE_IDX:
                    builder.sequenceIndex(ReaderUtil.readInt(reader));
                    break;
                case PHOTO_LATITUDE:
                    latitude = ReaderUtil.readDouble(reader);
                    break;
                case PHOTO_LONGITUDE:
                    longitude = ReaderUtil.readDouble(reader);
                    break;
                case PHOTO_NAME:
                    builder.name(ReaderUtil.readString(reader));
                    break;
                case PHOTO_LTH_NAME:
                    builder.largeThumbnailName(ReaderUtil.readString(reader));
                    break;
                case PHOTO_TH_NAME:
                    builder.thumbnailName(ReaderUtil.readString(reader));
                    break;
                case PHOTO_TIMESTAMP:
                    builder.timestamp(ReaderUtil.readLong(reader));
                    break;
                case PHOTO_HEADING:
                    final String value = ReaderUtil.readString(reader);
                    final Double heading = value != null && !value.isEmpty() ? Double.parseDouble(value) : null;
                    builder.heading(heading);
                    break;
                case PHOTO_USERNAME:
                    builder.username(ReaderUtil.readString(reader));
                    break;
                case WAY_ID:
                    final String wayIdValue = ReaderUtil.readString(reader);
                    final Long wayId = wayIdValue != null && !wayIdValue.isEmpty() ? Long.parseLong(wayIdValue) : null;
                    builder.wayId(wayId);
                    break;
                case PHOTO_SHOT_DATE:
                    builder.shotDate(ReaderUtil.readString(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        builder.location(latitude, longitude);
        reader.endObject();
        return builder.build();
    }

    @Override
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
        writer.name(PHOTO_USERNAME).value(photo.getUsername());
        writer.name(WAY_ID).value(photo.getWayId());
        writer.name(PHOTO_SHOT_DATE).value(photo.getShotDate());
        writer.endObject();
    }
}