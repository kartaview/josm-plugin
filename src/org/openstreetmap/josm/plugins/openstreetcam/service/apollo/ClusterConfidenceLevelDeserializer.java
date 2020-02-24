/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;

import java.lang.reflect.Type;

import org.openstreetmap.josm.plugins.openstreetcam.entity.ClusterConfidenceLevel;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * @author beata.tautan
 *
 */
class ClusterConfidenceLevelDeserializer implements JsonDeserializer<ClusterConfidenceLevel> {

	private static final String OVERALL_CONFIDENCE = "overallConfidence";
	private static final String OCR_CONFIDENCE = "ocrConfidence";

	@Override
	public ClusterConfidenceLevel deserialize(final JsonElement jsonElement, final Type type,
			final JsonDeserializationContext context) {
		Double overallConfidence;
		Double ocrConfidence = null;
		if (jsonElement instanceof JsonObject) {
			final JsonObject obj = (JsonObject) jsonElement;
			overallConfidence = obj.get(OVERALL_CONFIDENCE).getAsDouble();
			ocrConfidence = obj.get(OCR_CONFIDENCE).getAsDouble();
		} else {
			final JsonPrimitive obj = (JsonPrimitive) jsonElement;
			overallConfidence = obj.getAsDouble();
		}
		return overallConfidence != null || ocrConfidence != null
				? new ClusterConfidenceLevel(overallConfidence, ocrConfidence)
				: null;
	}
}