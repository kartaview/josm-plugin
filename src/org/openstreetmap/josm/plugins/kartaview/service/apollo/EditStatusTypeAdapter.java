/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo;

import java.lang.reflect.Type;
import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class EditStatusTypeAdapter implements JsonSerializer<EditStatus>, JsonDeserializer<EditStatus> {

    private static final String FIXED = "FIXED";
    private static final String ALREADY_FIXED = "ALREADY_FIXED";

    @Override
    public EditStatus deserialize(final JsonElement jsonElement, final Type type,
            final JsonDeserializationContext context) {
        final String editStatusValue = jsonElement.getAsString();
        EditStatus editStatus = null;
        if (editStatusValue != null && !editStatusValue.isEmpty()) {
            editStatus = FIXED.equals(editStatusValue) || ALREADY_FIXED.equals(editStatusValue) ? EditStatus.MAPPED
                    : EditStatus.valueOf(editStatusValue);
        }
        return editStatus;
    }

    @Override
    public JsonElement serialize(final EditStatus editStatus, final Type type, final JsonSerializationContext context) {
        final String editStatusValue =
                editStatus != null ? editStatus.equals(EditStatus.MAPPED) ? FIXED : editStatus.name() : null;
                return editStatusValue != null ? new JsonPrimitive(editStatusValue) : null;
    }
}