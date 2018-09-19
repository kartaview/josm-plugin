/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c) 2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;

/**
 * The observable interface for the {@code DetectionChangeObserver}.
 *
 * @author ioanao
 * @version $Revision$
 */
public interface DetectionChangeObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code DetectionChangeObserver}
     */
    void registerObserver(final DetectionChangeObserver observer);

    /**
     * Notifies the observers listening.
     *
     * @param status the new status to be set
     * @param text an addition comment to be added to a detection
     */
    void notifyDetectionChangeObserver(final EditStatus status, final String text);
}