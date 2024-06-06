/*
 * Copyright 2022 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 */
package org.openstreetmap.josm.plugins.kartaview.entity;


/**
 * Defines the cluster confidence level confidence category entity.
 *
 * @author adina.misaras
 * @version $Revision$
 */
public enum ConfidenceLevelCategory {

    C1, C2, C3;

    public static ConfidenceLevelCategory getConfidenceLevelCategory(final String value) {
        return value != null ? valueOf(value) : null;
    }
}