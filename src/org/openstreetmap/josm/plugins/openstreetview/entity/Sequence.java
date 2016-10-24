/*
 *  Copyright ©2016, Telenav, Inc. All Rights Reserved
 *
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 *  https://creativecommons.org/licenses/by-sa/4.0/ *legalcode.
 *
 */
package org.openstreetmap.josm.plugins.openstreetview.entity;

import java.util.List;


/**
 * 
 * @author beataj
 * @version $Revision$
 */
public class Sequence {

    private Long id;
    private List<Photo> photos;


    public Sequence(Long id, List<Photo> photos) {
        this.id = id;
        this.photos = photos;
    }


    public Long getId() {
        return id;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}