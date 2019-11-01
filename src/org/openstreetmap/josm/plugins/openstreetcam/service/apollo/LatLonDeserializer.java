/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;

import java.lang.reflect.Type;
import org.openstreetmap.josm.data.coor.LatLon;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * Custom deserializer for the {@code LatLon} object.
 *
 * @author Beata
 * @version $Revision$
 */
class LatLonDeserializer implements JsonDeserializer<LatLon> {

    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";


    @Override
    public LatLon deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext context) {
        final JsonObject obj = (JsonObject) jsonElement;
        final double lat = obj.get(LATITUDE).getAsDouble();
        final double lon = obj.get(LONGITUDE).getAsDouble();
        return new LatLon(lat, lon);
    }
}