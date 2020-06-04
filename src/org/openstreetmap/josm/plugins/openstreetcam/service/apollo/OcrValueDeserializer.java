/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OcrValue;
import java.lang.reflect.Type;


/**
 * Custom deserializer for the {@code OcrValue} object.
 *
 * @author nicoleta.viregan
 */
public class OcrValueDeserializer implements JsonDeserializer<OcrValue> {

    private static final String VALUE = "value";
    private static final String LANGUAGE = "language";
    private static final String CHARACTER_SET = "characterSet";

    @Override
    public OcrValue deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final String value;
        String language = null;
        String characterSet = null;

        if (jsonElement instanceof JsonObject) {
            final JsonObject obj = (JsonObject) jsonElement;
            value = obj.get(VALUE).getAsString();
            if (obj.has(LANGUAGE)) {
                language = obj.get(LANGUAGE).getAsString();
            }
            if (obj.has(CHARACTER_SET)) {
                characterSet = obj.get(CHARACTER_SET).getAsString();
            }
        } else {
            final JsonPrimitive obj = (JsonPrimitive) jsonElement;
            value = obj.getAsString();

        }
        return value != null || language != null || characterSet != null ? new OcrValue(value, language, characterSet) :
                null;
    }
}