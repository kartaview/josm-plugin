/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import java.awt.event.ActionEvent;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.kartaview.DownloadPhotosTask;


/**
 *
 * @author beataj
 * @version $Revision$
 */
class DownloadPhotosAction extends JosmAction {

    private static final long serialVersionUID = 9151442277348821560L;

    private final String taskTitle;
    private final boolean loadNextResults;

    DownloadPhotosAction(final String name, final String taskTitle, final boolean loadNextResults) {
        super(name, IconConfig.getInstance().getDownloadIconName(), name, null, true);
        this.taskTitle = taskTitle;
        this.loadNextResults = loadNextResults;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        MainApplication.worker.execute(new DownloadPhotosTask(taskTitle, loadNextResults));
    }
}