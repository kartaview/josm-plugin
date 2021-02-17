/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;


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