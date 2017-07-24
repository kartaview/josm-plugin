/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplayAction;


/**
 * The observable interface for the {@code TrackAutoplayObserver} object.
 *
 * @author beataj
 * @version $Revision$
 */
public interface TrackAutoplayObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code TrackAutoplayObserver}
     */
    void registerObserver(TrackAutoplayObserver observer);

    /**
     * Notifies the observes listening.
     *
     * @param action the auto-play action to be performed
     */
    void notifyObserver(AutoplayAction action);
}