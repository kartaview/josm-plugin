/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.entity.Detection;


/**
 * The observable interface for the {@code RowSelectionObserver}.
 *
 * @author nicoletav
 */
public interface  RowSelectionObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code RowSelectionObserver}
     */
    void registerObserver(final RowSelectionObserver observer);

    /**
     * Notifies the observers listening.
     *
     * @param detection corresponding to the selected row
     */
    void notifyRowSelectionObserver(final Detection detection);
}