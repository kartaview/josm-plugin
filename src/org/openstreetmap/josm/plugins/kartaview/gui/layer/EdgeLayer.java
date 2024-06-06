/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
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

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.argument.DataType;
import org.openstreetmap.josm.plugins.kartaview.gui.details.edge.filter.EdgeFilterDialog;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;


/**
 * Defines the Edge Detections layer functionality.
 *
 * @author nicoleta.viregan
 */
public class EdgeLayer extends BaseAbstractLayer {

    private static EdgeLayer instance;

    private EdgeLayer() {
        super(GuiConfig.getInstance().getEdgeDataName(), new DeleteEdgeLayerAction(), new DisplayFilterDialogAction(
                new EdgeFilterDialog(IconConfig.getInstance().getFilterIcon())));
    }

    /**
     * Returns the unique instance of the layer.
     *
     * @return a {@code EdgeLayer} object
     */
    public static EdgeLayer getInstance() {
        if (Objects.isNull(instance)) {
            instance = new EdgeLayer();
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
        return IconConfig.getInstance().getEdgeLayerIcon();
    }

    @Override
    public String getToolTipText() {
        return null;
    }

    @Override
    public Object getInfoComponent() {
        return null;
    }

    @Override
    public Action[] getMenuEntries() {
        final LayerListDialog layerListDialog = LayerListDialog.getInstance();
        final List<Action> actions = new ArrayList<>();
        actions.add(layerListDialog.createActivateLayerAction(this));
        actions.add(layerListDialog.createShowHideLayerAction());
        actions.add(getDeleteLayerAction());
        actions.add(SeparatorLayerAction.INSTANCE);

        actions.add(getDisplayFilterAction());
        actions.add(SeparatorLayerAction.INSTANCE);
        actions.add(getOpenPreferencesAction());
        actions.add(SeparatorLayerAction.INSTANCE);
        actions.add(new LayerListPopup.InfoAction(this));
        return actions.toArray(new Action[0]);
    }

    @Override
    public void paint(final Graphics2D graphics, final MapView mapView, final Bounds bounds) {
        mapView.setDoubleBuffered(true);
        graphics.setRenderingHints(RENDERING_MAP);
        final DataSet dataSet = DataSet.getInstance();
        if (dataSet.hasItems()) {
            final Composite originalComposite = graphics.getComposite();
            final Stroke originalStroke = graphics.getStroke();
            final boolean isTransparent = Objects.nonNull(dataSet.getSelectedEdgeCluster());
            final List<DataType> dataTypes = PreferenceManager.getInstance().loadEdgeSearchFilter().getDataTypes();
            if (dataSet.hasEdgeDetections() && dataTypes.contains(DataType.DETECTION)) {
                paintHandler.drawEdgeDetections(graphics, mapView, dataSet.getEdgeDetections(), dataSet
                        .getSelectedEdgeDetection(), isTransparent);
            }
            if (dataSet.hasEdgeClusters() && dataTypes.contains(DataType.CLUSTER)) {
                paintHandler.drawEdgeClusters(graphics, mapView, dataSet.getEdgeClusters(), dataSet
                        .getSelectedEdgeCluster(), dataSet.getSelectedEdgeDetection());
            }
            graphics.setComposite(originalComposite);
            graphics.setStroke(originalStroke);
        }
    }
}