/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;


/**
 * Serializer for {@code Instant}s.
 *
 * @author maria.mitisor
 */
public class InstantSerializer implements JsonSerializer<Instant> {

    @Override
    public JsonElement serialize(final Instant src, final Type typeOfSrc, final JsonSerializationContext context) {
        return new JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(src));
    }
}