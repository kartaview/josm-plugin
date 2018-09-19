/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;


/**
 * The observable interface for the {@code ClusterObserver} object.
 *
 * @author beataj
 * @version $Revision$
 */
public interface ClusterObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code ClusterObserver} to be registered
     */
    void registerObserver(ClusterObserver observer);

    /**
     * Notifies the observer listening.
     *
     * @param isNext specifies if the next or previous photo is selected
     */
    void notifyObserver(boolean isNext);
}