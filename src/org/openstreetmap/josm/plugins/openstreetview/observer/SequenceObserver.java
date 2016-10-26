/*
 * Copyright ©2016, Telenav, Inc. All Rights Reserved
 *
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/ *legalcode.
 *
 */
package org.openstreetmap.josm.plugins.openstreetview.observer;


/**
 * Observes the 'Next Photo/Previous Photo' user click action. If the user clicks on the 'Next Photo/Previous Photo'
 * buttons then the previous/next photo needs to be displayed and the map needs to be centered around the selected
 * element.
 *
 * @author beataj
 * @version $Revision$
 */
public interface SequenceObserver {

    /**
     * Selects the photo that is located at the given index in the selected sequence.
     *
     * @param index the photo index
     */
    void selectSequencePhoto(int index);
}