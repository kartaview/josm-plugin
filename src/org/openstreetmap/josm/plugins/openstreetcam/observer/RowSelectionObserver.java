/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;

/**
 * Interface that describes the functionality of a class responsible for handling the selection of a row from the clusters's table.
 *
 * @author nicoletav
 */
public interface  RowSelectionObserver {

    /**
     * Handle the selection of row event.
     *
     * @param detection - the detection from the selected row
     */
    void selectDetectionFromTable(final Detection detection);
}