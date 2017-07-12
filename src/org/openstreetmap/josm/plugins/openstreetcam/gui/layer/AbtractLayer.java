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

import javax.swing.Action;
import javax.swing.Icon;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;


/**
 * Defines JOSM layer related functionality.
 *
 * @author Beata
 * @version $Revision$
 */
abstract class AbtractLayer extends Layer {

    private final JosmAction displayFilterAction;
    private final JosmAction openFeedbackAction;
    private final JosmAction deleteLayerAction;
    private final JosmAction downloadPreviousPhotosAction;
    private final JosmAction downloadNextPhotosAction;
    private final JosmAction openPreferencesAction;

    AbtractLayer() {
        super(GuiConfig.getInstance().getPluginShortName());
        displayFilterAction = new DisplayFilterDialogAction();
        openFeedbackAction = new OpenFeedbackPageAction();
        deleteLayerAction = new OpenStreetCamDeleteLayerAction();
        downloadPreviousPhotosAction = new DownloadPhotosAction(GuiConfig.getInstance().getLayerPreviousMenuItemLbl(),
                GuiConfig.getInstance().getInfoDownloadPreviousPhotosTitle(), false);
        downloadNextPhotosAction = new DownloadPhotosAction(GuiConfig.getInstance().getLayerNextMenuItemLbl(),
                GuiConfig.getInstance().getInfoDownloadNextPhotosTitle(), true);
        openPreferencesAction = new OpenPreferenceDialogAction();
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
        Action[] actions;
        if (addPhotoDataSetMenuItems()) {
            actions = new Action[] { layerListDialog.createActivateLayerAction(this),
                    layerListDialog.createShowHideLayerAction(), deleteLayerAction, SeparatorLayerAction.INSTANCE,
                    displayFilterAction, SeparatorLayerAction.INSTANCE, downloadPreviousPhotosAction,
                    downloadNextPhotosAction, SeparatorLayerAction.INSTANCE, openFeedbackAction,
                    SeparatorLayerAction.INSTANCE, openPreferencesAction, SeparatorLayerAction.INSTANCE,
                    new LayerListPopup.InfoAction(this) };
        } else {
            actions = new Action[] { layerListDialog.createActivateLayerAction(this),
                    layerListDialog.createShowHideLayerAction(), deleteLayerAction, SeparatorLayerAction.INSTANCE,
                    displayFilterAction, SeparatorLayerAction.INSTANCE, openFeedbackAction,
                    SeparatorLayerAction.INSTANCE, openPreferencesAction, SeparatorLayerAction.INSTANCE,
                    new LayerListPopup.InfoAction(this) };
        }
        return actions;
    }

    abstract boolean addPhotoDataSetMenuItems();

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

    public void enableDownloadPreviousPhotoAction(final boolean enabled) {
        downloadPreviousPhotosAction.setEnabled(enabled);
    }

    public void enabledDownloadNextPhotosAction(final boolean enabled) {
        downloadNextPhotosAction.setEnabled(enabled);
    }
}