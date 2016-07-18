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

import java.io.IOException;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


/**
 * Custom type adapter for the {@code Photo} object.
 *
 * @author Beata
 * @version $Revision$
 */
class PhotoTypeAdapter extends TypeAdapter<Photo> {

    private static final String ID = "id";
    private static final String SEQUENCE_ID = "sequence_id";
    private static final String SEQUENCE_IDX = "sequence_index";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lng";
    private static final String NAME = "name";
    private static final String LTH_NAME = "lth_name";
    private static final String TH_NAME = "th_name";
    private static final String TIMESTAMP = "timestamp";
    private static final String HEADING = "heading";
    private static final String USERNAME = "username";


    @Override
    public Photo read(final JsonReader reader) throws IOException {
        Long id = null;
        Long sequenceId = null;
        Long sequenceIdx = null;
        Double latitude = null;
        Double longitude = null;
        String name = null;
        String largeThumbnailName = null;
        String thumbnailName = null;
        Long timestamp = null;
        String heading = null;
        String username = null;
        reader.beginObject();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case ID:
                    id = readLong(reader);
                    break;
                case SEQUENCE_ID:
                    sequenceId = readLong(reader);
                    break;
                case SEQUENCE_IDX:
                    sequenceIdx = readLong(reader);
                    break;
                case LATITUDE:
                    latitude = readDouble(reader);
                    break;
                case LONGITUDE:
                    longitude = readDouble(reader);
                    break;
                case NAME:
                    name = readString(reader);
                    break;
                case LTH_NAME:
                    largeThumbnailName = readString(reader);
                    break;
                case TH_NAME:
                    thumbnailName = readString(reader);
                    break;
                case TIMESTAMP:
                    timestamp = readLong(reader);
                    break;
                case HEADING:
                    heading = readString(reader);
                    break;
                case USERNAME:
                    username = readString(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
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

    private String readString(final JsonReader reader) throws IOException {
        String value = null;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            value = reader.nextString();
        }
        return value;
    }


    @Override
    public void write(final JsonWriter writer, final Photo photo) throws IOException {
        writer.beginObject();
        writer.name(ID).value(photo.getId());
        writer.name(SEQUENCE_ID).value(photo.getSequenceId());
        writer.name(SEQUENCE_IDX).value(photo.getSequenceIndex());
        if (photo.getLocation() != null) {
            writer.name(LATITUDE).value(photo.getLocation().getY());
            writer.name(LONGITUDE).value(photo.getLocation().getX());
        }
        writer.name(NAME).value(photo.getName());
        writer.name(LTH_NAME).value(photo.getLargeThumbnailName());
        writer.name(TH_NAME).value(photo.getThumbnailName());
        writer.name(TIMESTAMP).value(photo.getTimestamp());
        writer.name(HEADING).value(photo.getHeading());
        writer.endObject();
    }
}