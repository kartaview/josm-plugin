/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import java.awt.event.ActionEvent;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.DownloadPhotosTask;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class DownloadPhotosAction extends JosmAction {

    private static final long serialVersionUID = 9151442277348821560L;

    private final String taskTitle;
    private final boolean loadNextResults;

    public DownloadPhotosAction(final String name, final String taskTitle, final boolean loadNextResults) {
        super(name, IconConfig.getInstance().getDownloadIconName(), name, null, true);
        this.taskTitle = taskTitle;
        this.loadNextResults = loadNextResults;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        MainApplication.worker.execute(new DownloadPhotosTask(taskTitle, loadNextResults));
    }
}