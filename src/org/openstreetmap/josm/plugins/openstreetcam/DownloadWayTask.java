/*
 *  Copyright 2017 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetcam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.downloadtasks.DownloadReferrersTask;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.gui.ExceptionDialogUtil;
import org.openstreetmap.josm.gui.PleaseWaitRunnable;
import org.openstreetmap.josm.gui.io.DownloadPrimitivesTask;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.gui.progress.ProgressMonitor;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.io.OsmTransferException;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.xml.sax.SAXException;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class DownloadWayTask extends PleaseWaitRunnable {

    /** The identifier of the way that needs to be downloaded */
    private final PrimitiveId wayId;

    /** Temporary layer where downloaded primitives are put */
    private final OsmDataLayer tempLayer;

    /** Flag indicated that user ask for cancel this task */
    private boolean canceled;

    /** Reference to the task currently running */
    private PleaseWaitRunnable currentTask;


    /**
     * Creates a new download way task
     *
     * @param wayId the identifier of the way.
     */
    public DownloadWayTask(final PrimitiveId wayId) {
        super(GuiConfig.getInstance().getInfoMatchedWayTitle(), null, false);
        this.wayId = wayId;
        tempLayer = new OsmDataLayer(new DataSet(), OsmDataLayer.createNewName(), null);
    }


    @Override
    protected void cancel() {
        synchronized (this) {
            canceled = true;
            if (currentTask != null) {
                currentTask.operationCanceled();
            }
        }
    }

    @Override
    protected void realRun() throws SAXException, IOException, OsmTransferException {
        downloadWay();
        downloadWayRefferers();
        currentTask = null;
    }

    private void downloadWay() {
        final List<PrimitiveId> ids = new ArrayList<>();
        ids.add(wayId);
        final DownloadTask mainTask =
                new DownloadTask(tempLayer, ids, getProgressMonitor().createSubTaskMonitor(1, false));
        synchronized (this) {
            currentTask = mainTask;
            if (canceled) {
                currentTask = null;
                return;
            }
        }
        currentTask.run();
    }

    private void downloadWayRefferers() {
        synchronized (this) {
            if (canceled) {
                currentTask = null;
                return;
            }
            currentTask =
                    new DownloadReferrersTask(tempLayer, wayId, getProgressMonitor().createSubTaskMonitor(1, false));
        }
        currentTask.run();
    }

    @Override
    protected void finish() {
        synchronized (this) {
            if (canceled) {
                return;
            }
        }
        if (Main.getLayerManager().getEditLayer() == null) {
            Main.getLayerManager().addLayer(tempLayer);
        } else {
            Main.getLayerManager().getEditLayer().mergeFrom(tempLayer);
        }
        GuiHelper.runInEDT(() -> Main.getLayerManager().getEditDataSet().setSelected(wayId));
    }


    private final class DownloadTask extends DownloadPrimitivesTask {

        private DownloadTask(final OsmDataLayer layer, final List<PrimitiveId> ids,
                final ProgressMonitor progressMonitor) {
            super(layer, ids, true, progressMonitor);
        }

        @Override
        protected void finish() {
            if (canceled) {
                return;
            }
            if (lastException != null) {
                ExceptionDialogUtil.explainException(lastException);
                return;
            }
            GuiHelper.runInEDTAndWait(() -> {
                layer.mergeFrom(ds);
                layer.onPostDownloadFromServer();
            });
        }
    }
}