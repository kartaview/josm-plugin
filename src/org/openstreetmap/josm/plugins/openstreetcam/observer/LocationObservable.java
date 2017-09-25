/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

/**
 * The observable interface for the {@code LocationObserver} object.
 *
 * @author Beata
 * @version $Revision$
 */
public interface LocationObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code LocationObserver}
     */
    void registerObserver(LocationObserver observer);

    /**
     * Notifies the observers listening.
     */
    void notifyObserver();
}