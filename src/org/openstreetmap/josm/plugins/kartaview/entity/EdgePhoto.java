/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import java.util.Collection;
import org.openstreetmap.josm.data.coor.LatLon;


/**
 * POJO class for storing edge photo information.
 *
 * @author nicoleta.viregan
 */
public class EdgePhoto {

    private Long id;
    private Long creationTimestamp;
    private Long latestChangeTimestamp;
    private Device device;
    private LatLon point;
    private PhotoSize size;
    private Integer sensorHeading;
    private Double gpsAccuracy;
    private Double horizontalFieldOfView;
    private Double verticalFieldOfView;
    private String projectionType;
    private Collection<Attribute> customAttributes;

    public Long getId() {
        return id;
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Long getLatestChangeTimestamp() {
        return latestChangeTimestamp;
    }

    public Device getDevice() {
        return device;
    }

    public LatLon getPoint() {
        return point;
    }

    public PhotoSize getSize() {
        return size;
    }

    public Integer getSensorHeading() {
        return sensorHeading;
    }

    public Double getGpsAccuracy() {
        return gpsAccuracy;
    }

    public Double getHorizontalFieldOfView() {
        return horizontalFieldOfView;
    }

    public String getProjectionType() {
        return projectionType;
    }

    public Double getVerticalFieldOfView() {
        return verticalFieldOfView;
    }

    public Collection<Attribute> getCustomAttributes() {
        return customAttributes;
    }
}