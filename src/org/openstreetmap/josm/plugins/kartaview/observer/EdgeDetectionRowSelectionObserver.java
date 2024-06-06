/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;


/**
 * Interface that describes how row selection is handled in the Edge detections table.
 *
 * @author maria.mitisor
 */
public interface EdgeDetectionRowSelectionObserver {

    /**
     * Handles the Edge detection row selection event.
     *
     * @param edgeDetection - the Edge detection from the selected row
     */
    void selectEdgeDetectionFromTable(final EdgeDetection edgeDetection);
}