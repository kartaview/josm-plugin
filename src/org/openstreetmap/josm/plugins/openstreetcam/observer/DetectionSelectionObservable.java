/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;


/**
 * Interface that describes the functionality of a class responsible with sending notifications related to detection
 * selection.
 *
 * @author ioanao
 * @version $Revision$
 */
public interface DetectionSelectionObservable {

    /**
     * Registers the observer responsible of handling the notification.
     *
     * @param observer a {@code DetectionSelectionObserver}
     */
    void registerObserver(final DetectionSelectionObserver observer);

    /**
     * Notifies the observers entity when a detection is selected.
     */
    void notifyDetectionSelectionObserver(final Detection detection);
}