/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.photo.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


/**
 * Utility class, reads different object types from {@code JsonReader}
 *
 * @author beataj
 * @version $Revision$
 */
final class ReaderUtil {

    private ReaderUtil() {}

    static Double readDouble(final JsonReader reader) throws IOException {
        Double value = null;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            value = reader.nextDouble();
        }
        return value;
    }

    static Long readLong(final JsonReader reader) throws IOException {
        Long value = null;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            value = reader.nextLong();
        }
        return value;
    }

    static Integer readInt(final JsonReader reader) throws IOException {
        Integer value = null;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            value = reader.nextInt();
        }
        return value;
    }

    static String readString(final JsonReader reader) throws IOException {
        String value = null;
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            value = reader.nextString();
        }
        return value;
    }

    /**
     * Reads a geometry that has the following format: [[lat1,lon1], [lat2,lon2],...[latn,lonn]].
     *
     * @param reader a {@code JsonReader} object
     * @return a list of {@code LatLon} objects
     * @throws IOException if the read operation failed
     */
    static List<LatLon> readGeometry(final JsonReader reader) throws IOException {
        final List<LatLon> geometry = new ArrayList<>();
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
        } else {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginArray();
                final Double lat = reader.nextDouble();
                final Double lon = reader.nextDouble();
                geometry.add(new LatLon(lat, lon));
                reader.endArray();
            }
            reader.endArray();
        }
        return geometry;
    }
}