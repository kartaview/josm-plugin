/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Contribution;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class Request {

    private final Detection detection;
    private final Contribution contribution;


    public Request(final Detection detection, final Contribution contribution) {
        this.detection = detection;
        this.contribution = contribution;
    }

    public Detection getDetection() {
        return detection;
    }

    public Contribution getContribution() {
        return contribution;
    }
}