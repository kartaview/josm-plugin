/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.entity;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class Contribution {

    private final Author author;
    private final String comment;


    public Contribution(final Author author, final String comment) {
        this.author = author;
        this.comment = comment;
    }


    public Author getAuthor() {
        return author;
    }


    public String getComment() {
        return comment;
    }
}