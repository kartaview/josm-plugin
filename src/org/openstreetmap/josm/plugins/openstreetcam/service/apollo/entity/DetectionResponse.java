/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo.entity;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import com.telenav.josm.common.entity.Status;


/**
 *
 * @author laurad
 * @version $Revision$
 */
public class DetectionResponse extends Response {

    private Detection detection;

    public DetectionResponse(final Status status) {
        super(status);
    }

    public Detection getDetection() {
        return detection;
    }
}
