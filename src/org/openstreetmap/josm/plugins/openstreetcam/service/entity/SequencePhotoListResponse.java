/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.entity;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import com.telenav.josm.common.entity.Status;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class SequencePhotoListResponse extends Response {

    // setters are not required,since GSON sets the fields directly using reflection.
    private final Sequence osv;

    public SequencePhotoListResponse(final Status status, final Sequence osv) {
        super(status);
        this.osv = osv;
    }

    public Sequence getOsv() {
        return osv;
    }
}