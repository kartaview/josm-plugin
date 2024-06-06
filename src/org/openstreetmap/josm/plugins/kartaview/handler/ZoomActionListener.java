/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 */
package org.openstreetmap.josm.plugins.kartaview.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.openstreetmap.josm.plugins.kartaview.handler.edge.EdgeDataUpdateHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.imagery.KartaViewDataUpdateHandler;

import com.grab.josm.common.thread.ThreadPool;


/**
 * Listener for zoom changes. When the zoom changes the data for the KartaView and Edge layers is updated according to
 * the zoom level and current area.
 *
 * @author beata.tautan
 * @version $Revision$
 */
public class ZoomActionListener implements ActionListener {

    @Override
    public void actionPerformed(final ActionEvent event) {
        ThreadPool.getInstance().execute(() -> new KartaViewDataUpdateHandler().updateData(false));
        ThreadPool.getInstance().execute(() -> new EdgeDataUpdateHandler().updateData(false));
    }
}