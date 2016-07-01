/*
 *  Copyright 2016 Telenav, Inc.
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
package org.openstreetmap.josm.plugins.openstreetview.gui.layer;

import java.awt.Graphics2D;
import javax.swing.Action;
import javax.swing.Icon;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.IconConfig;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetViewLayer extends Layer {

    public OpenStreetViewLayer() {
        super(GuiConfig.getInstance().getPluginShortName());
    }

    @Override
    public void paint(final Graphics2D arg0, final MapView arg1, final Bounds arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public Icon getIcon() {
        return IconConfig.getInstance().getLayerIcon();
    }

    @Override
    public Object getInfoComponent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Action[] getMenuEntries() {
        final LayerListDialog layerListDialog = LayerListDialog.getInstance();
        return new Action[] { layerListDialog.createActivateLayerAction(this),
                layerListDialog.createShowHideLayerAction(), layerListDialog.createDeleteLayerAction(),
                SeparatorLayerAction.INSTANCE, SeparatorLayerAction.INSTANCE, new LayerListPopup.InfoAction(this) };
    }

    @Override
    public String getToolTipText() {
        return GuiConfig.getInstance().getPluginLongName();
    }

    @Override
    public boolean isMergable(final Layer arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void mergeFrom(final Layer layer) {
        // this operation is not supported
    }

    @Override
    public void visitBoundingBox(final BoundingXYVisitor visitor) {
        // this operation is not supported
    }
}