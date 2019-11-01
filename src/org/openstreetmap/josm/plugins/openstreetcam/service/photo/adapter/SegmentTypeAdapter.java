/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.photo.adapter;

import static org.openstreetmap.josm.plugins.openstreetcam.service.photo.adapter.Constants.SEGMENT_COVERAGE;
import static org.openstreetmap.josm.plugins.openstreetcam.service.photo.adapter.Constants.SEGMENT_FROM;
import static org.openstreetmap.josm.plugins.openstreetcam.service.photo.adapter.Constants.SEGMENT_GEOMETRY;
import static org.openstreetmap.josm.plugins.openstreetcam.service.photo.adapter.Constants.SEGMENT_ID;
import static org.openstreetmap.josm.plugins.openstreetcam.service.photo.adapter.Constants.SEGMENT_TO;
import static org.openstreetmap.josm.plugins.openstreetcam.service.photo.adapter.Constants.WAY_ID;
import java.io.IOException;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.SegmentBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


/**
 * Custom type adapter for the {@code Segment} object.
 *
 * @author beataj
 * @version $Revision$
 */
public class SegmentTypeAdapter extends TypeAdapter<Segment> {

    @Override
    public Segment read(final JsonReader reader) throws IOException {
        final SegmentBuilder builder = new SegmentBuilder();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case SEGMENT_ID:
                    builder.id(ReaderUtil.readString(reader));
                    break;
                case SEGMENT_FROM:
                    builder.from(ReaderUtil.readLong(reader));
                    break;
                case SEGMENT_TO:
                    builder.to(ReaderUtil.readLong(reader));
                    break;
                case WAY_ID:
                    builder.wayId(ReaderUtil.readLong(reader));
                    break;
                case SEGMENT_COVERAGE:
                    builder.coverage(ReaderUtil.readInt(reader));
                    break;
                case SEGMENT_GEOMETRY:
                    builder.geometry(ReaderUtil.readGeometry(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return builder.build();
    }

    @Override
    public void write(final JsonWriter writer, final Segment object) throws IOException {
        writer.beginObject();
        writer.name(SEGMENT_ID).value(object.getId());
        writer.name(SEGMENT_FROM).value(object.getFrom());
        writer.name(SEGMENT_TO).value(object.getTo());
        writer.name(WAY_ID).value(object.getWayId());
        writer.name(SEGMENT_COVERAGE).value(object.getCoverage());
        writer.name(SEGMENT_GEOMETRY);
        writer.beginArray();
        for (final LatLon geom : object.getGeometry()) {
            writer.beginArray();
            writer.value(geom.getY());
            writer.value(geom.getX());
            writer.endArray();
        }
        writer.endArray();
        writer.endObject();
    }
}