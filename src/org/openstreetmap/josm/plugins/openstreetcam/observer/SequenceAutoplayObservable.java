/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplayAction;


/**
 * The observable interface for the {@code TrackAutoplayObserver} object.
 *
 * @author beataj
 * @version $Revision$
 */
public interface SequenceAutoplayObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code TrackAutoplayObserver}
     */
    void registerObserver(SequenceAutoplayObserver observer);

    /**
     * Notifies the observes listening.
     *
     * @param action the auto-play action to be performed
     */
    void notifyObserver(AutoplayAction action);
}