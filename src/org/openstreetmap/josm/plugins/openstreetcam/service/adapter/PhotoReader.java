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
import static org.openstreetmap.josm.plugins.openstreetcam.service.adapter.Constants.PHOTO_USERNAME;
import java.io.IOException;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


/**
 *
 * @author beataj
 * @version $Revision$
 */
final class PhotoReader {

    public Photo read(final JsonReader reader) throws IOException {
        Long id = null;
        Long sequenceId = null;
        Integer sequenceIdx = null;
        Double latitude = null;
        Double longitude = null;
        String name = null;
        String largeThumbnailName = null;
        String thumbnailName = null;
        Long timestamp = null;
        String headingVal = null;
        String username = null;
        reader.beginObject();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case PHOTO_ID:
                    id = readLong(reader);
                    break;
                case PHOTO_SEQUENCE_ID:
                    sequenceId = readLong(reader);
                    break;
                case PHOTO_SEQUENCE_IDX:
                    sequenceIdx = readInt(reader);
                    break;
                case PHOTO_LATITUDE:
                    latitude = readDouble(reader);
                    break;
                case PHOTO_LONGITUDE:
                    longitude = readDouble(reader);
                    break;
                case PHOTO_NAME:
                    name = readString(reader);
                    break;
                case PHOTO_LTH_NAME:
                    largeThumbnailName = readString(reader);
                    break;
                case PHOTO_TH_NAME:
                    thumbnailName = readString(reader);
                    break;
                case PHOTO_TIMESTAMP:
                    timestamp = readLong(reader);
                    break;
                case PHOTO_HEADING:
                    headingVal = readString(reader);
                    break;
                case PHOTO_USERNAME:
                    username = readString(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        final Double heading = headingVal != null && !headingVal.isEmpty() ? Double.parseDouble(headingVal) : null;
        return new Photo(id, sequenceId, sequenceIdx, latitude, longitude, name, largeThumbnailName, thumbnailName,
                timestamp, heading, username);
    }

    private Double readDouble(final JsonReader reader) throws IOException {
        Double value = null;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            value = reader.nextDouble();
        }
        return value;
    }

    private Long readLong(final JsonReader reader) throws IOException {
        Long value = null;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            value = reader.nextLong();
        }
        return value;
    }

    private Integer readInt(final JsonReader reader) throws IOException {
        Integer value = null;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            value = reader.nextInt();
        }
        return value;
    }

    private String readString(final JsonReader reader) throws IOException {
        String value = null;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            value = reader.nextString();
        }
        return value;
    }
}