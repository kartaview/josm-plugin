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
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import static org.openstreetmap.josm.tools.I18n.tr;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.gui.dialogs.layer.DeleteLayerAction;
import org.openstreetmap.josm.gui.help.HelpUtil;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.DisplayFilterDialogAction;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.ImageProvider;


/**
 * Defines JOSM layer related functionality.
 *
 * @author Beata
 * @version $Revision$
 */
abstract class AbtractLayer extends Layer {

    AbtractLayer() {
        super(GuiConfig.getInstance().getPluginShortName());
    }


    @Override
    public Icon getIcon() {
        return IconConfig.getInstance().getLayerIcon();
    }

    @Override
    public Object getInfoComponent() {
        return GuiConfig.getInstance().getPluginTlt();
    }

    @Override
    public Action[] getMenuEntries() {
        final LayerListDialog layerListDialog = LayerListDialog.getInstance();
        return new Action[] { layerListDialog.createActivateLayerAction(this),
                layerListDialog.createShowHideLayerAction(), new DeleteLayerAction2(),
                SeparatorLayerAction.INSTANCE, new DisplayFilterDialogAction(), SeparatorLayerAction.INSTANCE,
                new LayerListPopup.InfoAction(this) };
    }

    @Override
    public String getToolTipText() {
        return GuiConfig.getInstance().getPluginLongName();
    }

    @Override
    public boolean isMergable(final Layer layer) {
        return false;
    }

    @Override
    public void mergeFrom(final Layer layer) {
        // this operation is not supported
    }

    @Override
    public void visitBoundingBox(final BoundingXYVisitor visitor) {
        // no logic to add here
    }

    private final class DeleteLayerAction2 extends AbstractAction {

        private static final long serialVersionUID = 1569467764140753112L;
        private final DeleteLayerAction deleteAction = LayerListDialog.getInstance().createDeleteLayerAction();

        public DeleteLayerAction2() {
            new ImageProvider("dialogs", "delete").getResource().attachImageIcon(this, true);
            putValue(SHORT_DESCRIPTION, tr("Delete the selected layers."));
            putValue(NAME, tr("Delete"));
            putValue("help", HelpUtil.ht("/Dialog/LayerList#DeleteLayer"));
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            System.err.println("deleting layer...");
            deleteAction.actionPerformed(e);
        }

    }
}