/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.observer;

import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplayAction;


/**
 * Observes the 'Play/Stop'user click action. If the user clicks the 'Play' button then the track will start playing
 * automatically. If the user clicks the 'Stop' button then the auto-play will stop.
 *
 * @author beataj
 * @version $Revision$
 */
public interface SequenceAutoplayObserver {

    /**
     * Starts to auto-play the track or stop an already playing track.
     *
     * @param action a {@code AutoplayAction} specifies the auto-play action
     */
    void play(AutoplayAction action);
}