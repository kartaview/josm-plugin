/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import java.awt.event.ActionEvent;

import org.openstreetmap.josm.plugins.kartaview.gui.details.filter.DetectionTypeContent;
import org.openstreetmap.josm.plugins.kartaview.gui.details.filter.FilterDialog;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.PleaseWaitRunnable;
import org.openstreetmap.josm.gui.progress.swing.PleaseWaitProgressMonitor;
import org.openstreetmap.josm.plugins.kartaview.gui.ShortcutFactory;

import javax.swing.SwingUtilities;


/**
 * Displays the filter dialog window.
 *
 * @author beataj
 * @version $Revision$
 */
class DisplayFilterDialogAction extends JosmAction {

    private static final long serialVersionUID = 8325126526750975651L;


    DisplayFilterDialogAction() {
        super(GuiConfig.getInstance().getDlgFilterTitle(), IconConfig.getInstance().getFilterIconName(),
                GuiConfig.getInstance().getDlgFilterTitle(),
                ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getDlgFilterShortcutText()), true);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        MainApplication.worker.execute(new GenerateSigns());
    }

    private static class GenerateSigns extends PleaseWaitRunnable {

        /**
         * Flag indicating that the user asked to cancel this task
         */
        private boolean canceled;

        /**
         * Reference to the task currently running
         */
        private PleaseWaitRunnable currentTask;

        public GenerateSigns() {
            super(GuiConfig.getInstance().getDlgFilterTitle());
        }

        @Override
        protected void cancel() {
            synchronized (this) {
                canceled = true;
                if (currentTask != null) {
                    currentTask.operationCanceled();
                }
                ((PleaseWaitProgressMonitor) progressMonitor).close();
            }
        }

        @Override
        protected void realRun() {
            progressMonitor.indeterminateSubTask("Retrieving sign types and regions. This might take a moment.");
            DetectionTypeContent.generateSigns();
            DetectionTypeContent.generateRegions();
            currentTask = null;
            progressMonitor.finishTask();
        }

        @Override
        protected void finish() {
            synchronized (this) {
                if (canceled) {
                    return;
                }
            }
            SwingUtilities.invokeLater(() -> {
                final FilterDialog filterDialog = new FilterDialog(IconConfig.getInstance().getFilterIcon());
                filterDialog.setVisible(true);
            });
        }
    }
}