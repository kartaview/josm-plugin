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

import java.util.ArrayList;
import java.util.List;
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
    private final JosmAction saveSequenceAction;

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
        saveSequenceAction = new SaveTrackAction();
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
        final List<Action> actions = new ArrayList<>();
        actions.add(layerListDialog.createActivateLayerAction(this));
        actions.add(layerListDialog.createShowHideLayerAction());
        actions.add(deleteLayerAction);
        actions.add(SeparatorLayerAction.INSTANCE);
        actions.add(displayFilterAction);
        actions.add(SeparatorLayerAction.INSTANCE);
        if (addPhotoDataSetMenuItems()) {
            actions.add(downloadPreviousPhotosAction);
            actions.add(downloadNextPhotosAction);
            actions.add(SeparatorLayerAction.INSTANCE);
        }
        if (addSequenceMenuItem()) {
            actions.add(saveSequenceAction);
            actions.add(SeparatorLayerAction.INSTANCE);
        }
        actions.add(openFeedbackAction);
        actions.add(SeparatorLayerAction.INSTANCE);

        actions.add(openPreferencesAction);
        actions.add(SeparatorLayerAction.INSTANCE);
        actions.add(new LayerListPopup.InfoAction(this));
        return actions.toArray(new Action[0]);
    }

    abstract boolean addPhotoDataSetMenuItems();

    abstract boolean addSequenceMenuItem();

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