/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import org.openstreetmap.josm.plugins.kartaview.entity.OcrValue;
import java.lang.reflect.Type;


/**
 * Custom deserializer for the {@code OcrValue} object.
 *
 * @author nicoleta.viregan
 */
public class OcrValueDeserializer implements JsonDeserializer<OcrValue> {

    private static final String TEXT = "text";
    private static final String LANGUAGE = "language";
    private static final String CHARACTER_SET = "characterSet";
    private static final String NAME = "name";

    @Override
    public OcrValue deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final String text;
        String language = null;
        String characterSet = null;

        if (jsonElement instanceof JsonObject) {
            final JsonObject obj = (JsonObject) jsonElement;
            text = obj.get(TEXT).getAsString();
            if (obj.has(LANGUAGE)) {
                final JsonObject languageObj = (JsonObject) obj.get(LANGUAGE);
                language = languageObj.get(NAME).getAsString();
            }
            if (obj.has(CHARACTER_SET)) {
                final JsonObject characterSetObj = (JsonObject) obj.get(CHARACTER_SET);
                characterSet = characterSetObj.get(NAME).getAsString();
            }
        } else {
            final JsonPrimitive obj = (JsonPrimitive) jsonElement;
            text = obj.getAsString();

        }
        return text != null || language != null || characterSet != null ? new OcrValue(text, language, characterSet) :
                null;
    }
}