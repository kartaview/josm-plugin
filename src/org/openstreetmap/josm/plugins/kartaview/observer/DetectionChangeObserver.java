/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.observer;

import org.openstreetmap.josm.plugins.kartaview.entity.EditStatus;


/**
 *
 * @author ioanao
 * @version $Revision$
 */
public interface DetectionChangeObserver {

    void editDetection(final EditStatus editStatus, final String text);
}