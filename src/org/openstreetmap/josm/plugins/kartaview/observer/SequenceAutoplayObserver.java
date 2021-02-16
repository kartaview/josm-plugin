/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.argument.AutoplayAction;


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