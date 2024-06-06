/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.RENDERING_MAP;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.Action;
import javax.swing.Icon;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.argument.DataType;
import org.openstreetmap.josm.plugins.kartaview.argument.SearchFilter;
import org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.filter.FilterDialog;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;


/**
 * Defines the KartaView layer functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public final class KartaViewLayer extends BaseAbstractLayer {

    private final JosmAction downloadPreviousPhotosAction;
    private final JosmAction downloadNextPhotosAction;
    private final JosmAction saveSequenceAction;
    private static KartaViewLayer instance;

    private KartaViewLayer() {
        super(GuiConfig.getInstance().getPluginShortName(), new KartaViewDeleteLayerAction(),
                new DisplayFilterDialogAction(new FilterDialog(IconConfig.getInstance().getFilterIcon())));
        downloadPreviousPhotosAction = new DownloadPhotosAction(GuiConfig.getInstance().getLayerPreviousMenuItemLbl(),
                GuiConfig.getInstance().getInfoDownloadPreviousPhotosTitle(), false);
        downloadNextPhotosAction = new DownloadPhotosAction(GuiConfig.getInstance().getLayerNextMenuItemLbl(),
                GuiConfig.getInstance().getInfoDownloadNextPhotosTitle(), true);
        saveSequenceAction = new SaveTrackAction();
    }

    /**
     * Returns the unique instance of the layer.
     *
     * @return a {@code KartaViewLayer} object
     */
    public static KartaViewLayer getInstance() {
        if (instance == null) {
            instance = new KartaViewLayer();
        }
        return instance;
    }

    /**
     * Destroys the instance of the layer.
     */
    public static void destroyInstance() {
        instance = null;
    }

    @Override
    public Icon getIcon() {
        return SearchFilter.DEFAULT.equals(PreferenceManager.getInstance().loadSearchFilter())
                ? IconConfig.getInstance().getKartaViewLayerIcon() : IconConfig.getInstance().getLayerIconFiltered();
    }

    @Override
    public void paint(final Graphics2D graphics, final MapView mapView, final Bounds bounds) {
        mapView.setDoubleBuffered(true);
        graphics.setRenderingHints(RENDERING_MAP);
        final DataSet dataSet = DataSet.getInstance();
        if (dataSet.hasItems()) {
            final Composite originalComposite = graphics.getComposite();
            final Stroke originalStroke = graphics.getStroke();
            if (dataSet.hasSegments()) {
                paintHandler.drawSegments(graphics, mapView, dataSet.getSegments());
            } else {
                // draw photos
                final boolean isTransparent = Objects.nonNull(dataSet.getSelectedSequence());
                final List<DataType> dataTypes = PreferenceManager.getInstance().loadSearchFilter().getDataTypes();
                if (dataSet.hasPhotos() && (dataTypes.contains(DataType.PHOTO))) {
                    paintHandler.drawPhotos(graphics, mapView, dataSet.getPhotoDataSet().getPhotos(),
                            dataSet.getSelectedPhoto(), isTransparent);
                }

                // draw detections
                if (dataSet.getDetections() != null && dataTypes.contains(DataType.DETECTION)) {
                    paintHandler.drawDetections(graphics, mapView, dataSet.getDetections(),
                            dataSet.getSelectedDetection(), isTransparent);
                }

                // draw clusters
                if ((dataSet.getClusters() != null && dataTypes.contains(DataType.CLUSTER))
                        || dataSet.isRemoteSelection()) {
                    paintHandler.drawClusters(graphics, mapView, dataSet.getClusters(), dataSet.getSelectedCluster(),
                            dataSet.getSelectedPhoto(), dataSet.getSelectedDetection());
                }

                // draw sequence if any
                if (dataSet.getSelectedSequence() != null && dataSet.getSelectedSequence().hasData()) {
                    final boolean isClusterSelected = Objects.nonNull(dataSet.getSelectedCluster());
                    paintHandler.drawSequence(graphics, mapView, dataSet.getSelectedSequence(),
                            dataSet.getSelectedPhoto(), dataSet.getSelectedDetection(), isClusterSelected);
                }

                // draw downloaded matched data
                if (dataSet.hasMatchedData()) {
                    paintHandler.drawMatchedData(graphics, mapView, dataSet.getMatchedData());
                }
            }

            graphics.setComposite(originalComposite);
            graphics.setStroke(originalStroke);
        }
    }

    public void enablePhotoDataSetDownloadActions() {
        final DataSet dataSet = DataSet.getInstance();
        boolean enablePrevious = false;
        boolean enableNext = false;
        if (!dataSet.hasSelectedSequence() && dataSet.hasPhotos()) {
            enablePrevious = dataSet.getPhotoDataSet().hasPreviousItems();
            enableNext = dataSet.getPhotoDataSet().hasNextItems();
        }
        enablePhotoDataSetDownloadActions(enablePrevious, enableNext);
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
        actions.add(getDeleteLayerAction());
        actions.add(SeparatorLayerAction.INSTANCE);
        if (addSequenceMenuItem()) {
            actions.add(saveSequenceAction);
            actions.add(SeparatorLayerAction.INSTANCE);
        }
        actions.add(getDisplayFilterAction());
        actions.add(SeparatorLayerAction.INSTANCE);
        if (addPhotoDataSetMenuItems()) {
            actions.add(downloadPreviousPhotosAction);
            actions.add(downloadNextPhotosAction);
            actions.add(SeparatorLayerAction.INSTANCE);
        }
        actions.add(getOpenFeedbackAction());
        actions.add(SeparatorLayerAction.INSTANCE);

        actions.add(getOpenPreferencesAction());
        actions.add(SeparatorLayerAction.INSTANCE);
        actions.add(new LayerListPopup.InfoAction(this));
        return actions.toArray(new Action[0]);
    }

    @Override
    public String getToolTipText() {
        return GuiConfig.getInstance().getPluginLongName();
    }

    boolean addPhotoDataSetMenuItems() {
        return DataSet.getInstance().getPhotoDataSet() != null;
    }

    boolean addSequenceMenuItem() {
        return DataSet.getInstance().getSelectedSequence() != null;
    }

    void enablePhotoDataSetDownloadActions(final boolean downloadPrevious, final boolean downloadNext) {
        downloadPreviousPhotosAction.setEnabled(downloadPrevious);
        downloadNextPhotosAction.setEnabled(downloadNext);
    }
}