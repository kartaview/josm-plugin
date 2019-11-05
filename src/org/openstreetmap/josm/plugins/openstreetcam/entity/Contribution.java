/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.entity;


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