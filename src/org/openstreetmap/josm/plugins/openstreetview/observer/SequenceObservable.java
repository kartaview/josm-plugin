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
 * The observable interface for the {@code SequenceObserver} object.
 *
 * @author beataj
 * @version $Revision$
 */
public interface SequenceObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code SequenceObserver}
     */
    void registerObserver(SequenceObserver observer);

    /**
     * Notifies the observes listening.
     *
     * @param index the photo index
     */
    void notifyObserver(int index);
}