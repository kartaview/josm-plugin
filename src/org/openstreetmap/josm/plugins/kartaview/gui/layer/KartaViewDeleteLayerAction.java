/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import java.awt.event.ActionEvent;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.layer.DeleteLayerAction;


/**
 * Deletes the KartaView layer.
 *
 * @author ioanao
 * @version $Revision$
 */
final class KartaViewDeleteLayerAction extends BaseDeleteLayerAction {

    private static final long serialVersionUID = 1569467764140753112L;
    private final DeleteLayerAction deleteAction = LayerListDialog.getInstance().createDeleteLayerAction();

    @Override
    public void actionPerformed(final ActionEvent e) {
        PreferenceManager.getInstance().saveKartaViewLayerOpenedFlag(false);
        deleteAction.actionPerformed(e);
    }
}