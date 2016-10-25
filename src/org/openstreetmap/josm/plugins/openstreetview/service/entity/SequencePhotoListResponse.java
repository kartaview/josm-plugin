/*
 * Copyright ©2016, Telenav, Inc. All Rights Reserved
 *
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/ *legalcode.
 *
 */
package org.openstreetmap.josm.plugins.openstreetview.service.entity;

import org.openstreetmap.josm.plugins.openstreetview.entity.Sequence;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class SequencePhotoListResponse extends Response {

    // setters are not required,since GSON sets the fields directly using reflection.
    private Sequence osv;


    public Sequence getOsv() {
        return osv;
    }
}