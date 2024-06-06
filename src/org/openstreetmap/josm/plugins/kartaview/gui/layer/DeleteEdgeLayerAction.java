/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.layer.DeleteLayerAction;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import java.awt.event.ActionEvent;


/**
 * Deletes the Edge detections layer.
 *
 * @author maria.mitisor
 */
final class DeleteEdgeLayerAction extends BaseDeleteLayerAction {

    private static final long serialVersionUID = 2760481552277325069L;
    private final DeleteLayerAction deleteAction = LayerListDialog.getInstance().createDeleteLayerAction();

    @Override
    public void actionPerformed(final ActionEvent e) {
        PreferenceManager.getInstance().saveEdgeLayerOpenedFlag(false);
        deleteAction.actionPerformed(e);
    }
}