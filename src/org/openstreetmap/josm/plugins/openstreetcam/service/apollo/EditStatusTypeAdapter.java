/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;

import java.lang.reflect.Type;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
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