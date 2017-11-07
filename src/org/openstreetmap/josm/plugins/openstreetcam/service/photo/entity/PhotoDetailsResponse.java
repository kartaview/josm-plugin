/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.photo.entity;

import org.openstreetmap.josm.plugins.openstreetcam.service.entity.Response;
import com.telenav.josm.common.entity.Status;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoDetailsResponse extends Response {

    private final OSV osv;


    public PhotoDetailsResponse(final Status status, final OSV osv) {
        super(status);
        this.osv = osv;
    }


    public OSV getOsv() {
        return osv;
    }
}