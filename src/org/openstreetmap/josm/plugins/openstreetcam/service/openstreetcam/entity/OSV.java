/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.openstreetcam.entity;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class OSV {

    private final Photo photoObject;


    public OSV(final Photo photoObject) {
        this.photoObject = photoObject;
    }


    public Photo getPhotoObject() {
        return photoObject;
    }
}