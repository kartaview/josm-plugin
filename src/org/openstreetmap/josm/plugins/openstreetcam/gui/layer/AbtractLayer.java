/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
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
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


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
        return SearchFilter.DEFAULT.equals(PreferenceManager.getInstance().loadSearchFilter())
                ? IconConfig.getInstance().getLayerIcon() : IconConfig.getInstance().getLayerIconFiltered();
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
        if (addSequenceMenuItem()) {
            actions.add(saveSequenceAction);
            actions.add(SeparatorLayerAction.INSTANCE);
        } else {
            actions.add(displayFilterAction);
            actions.add(SeparatorLayerAction.INSTANCE);
        }
        if (addPhotoDataSetMenuItems()) {
            actions.add(downloadPreviousPhotosAction);
            actions.add(downloadNextPhotosAction);
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

    void enablePhotoDataSetDownloadActions(final boolean downloadPrevious, final boolean downloadNext) {
        downloadPreviousPhotosAction.setEnabled(downloadPrevious);
        downloadNextPhotosAction.setEnabled(downloadNext);
    }
}