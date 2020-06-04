/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;

/**
 * @author nicoleta.viregan
 */
public class OcrValue {

    private final String text;
    private final String language;
    private final String characterSet;

    public OcrValue(final String text, final String language, final String characterSet) {
        this.text = text;
        this.language = language;
        this.characterSet = characterSet;
    }

    public String getText() {
        return text;
    }

    public String getLanguage() {
        return language;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public boolean isNotNull() {
        return text != null || language != null || characterSet != null;
    }
}
