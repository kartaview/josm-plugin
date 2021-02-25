/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.argument;


/**
 * Defines the user configurable map view settings attributes.
 *
 * @author beataj
 * @version $Revision$
 */
public class MapViewSettings {

    private final Integer photoZoom;
    private final boolean dataLoadFlag;


    /**
     * Builds a new object with the given arguments.
     *
     * @param photoZoom the zoom level from which photo locations are displayed
     * @param dataLoadFlag specifies if the OSC data layer should be loaded only inside the active OSM Data layer
     */
    public MapViewSettings(final Integer photoZoom, final boolean dataLoadFlag) {
        this.photoZoom = photoZoom;
        this.dataLoadFlag = dataLoadFlag;
    }

    public Integer getPhotoZoom() {
        return photoZoom;
    }

    public boolean isDataLoadFlag() {
        return dataLoadFlag;
    }
}