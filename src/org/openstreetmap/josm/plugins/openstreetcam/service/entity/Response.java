/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.entity;

import com.telenav.josm.common.entity.Status;

/**
 *
 * @author beataj
 * @version $Revision$
 */
public class Response {

    private final Status status;

    public Response(final Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}