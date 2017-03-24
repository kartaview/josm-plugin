/*
 * Copyright ©2016, Telenav, Inc. All Rights Reserved
 *
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/ *legalcode.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.entity;


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