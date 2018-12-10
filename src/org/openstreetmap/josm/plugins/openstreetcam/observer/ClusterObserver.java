/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;


/**
 * Observes the "Next Photo/Previous Photo" user click action. If the user clicks on the next/previous photo action the
 * next/previous photo belonging to a selected cluster relative to the selected photo is selected.
 *
 * @author beataj
 * @version $Revision$
 */
public interface ClusterObserver {

    /**
     * Selects a photo belonging to a selected cluster. The photo to be selected is computed relative to the currently
     * selected photo.
     *
     * @param isNext specifies if the next or previous photo relative to the selected photo is selected
     */
    void selectPhoto(boolean isNext);
}