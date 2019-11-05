/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.RENDERING_MAP;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


/**
 * Defines the OpenStreetCam layer functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public final class OpenStreetCamLayer extends AbtractLayer {

    private final PaintHandler paintHandler = new PaintHandler();
    private static OpenStreetCamLayer instance;

    private OpenStreetCamLayer() {
        super();
    }

    /**
     * Returns the unique instance of the layer.
     *
     * @return a {@code OpenStreetCamLayer} object
     */
    public static OpenStreetCamLayer getInstance() {
        if (instance == null) {
            instance = new OpenStreetCamLayer();
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
    public void paint(final Graphics2D graphics, final MapView mapView, final Bounds bounds) {
        mapView.setDoubleBuffered(true);
        graphics.setRenderingHints(RENDERING_MAP);
        final DataSet dataSet = DataSet.getInstance();
        if (dataSet.hasItems()) {
            final Composite originalComposite = graphics.getComposite();
            final Stroke originalStorke = graphics.getStroke();
            if (dataSet.hasSegments()) {
                paintHandler.drawSegments(graphics, mapView, dataSet.getSegments());
            } else {
                // draw photos
                final boolean isTransparent =
                        dataSet.getSelectedSequence() != null || dataSet.getSelectedCluster() != null;
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
            }

            // draw sequence if any
            if (dataSet.getSelectedSequence() != null && dataSet.getSelectedSequence().hasData()) {
                paintHandler.drawSequence(graphics, mapView, dataSet.getSelectedSequence(), dataSet.getSelectedPhoto(),
                        dataSet.getSelectedDetection());
            }

            // draw downloaded matched data
            if (dataSet.hasMatchedData()) {
                paintHandler.drawMatchedData(graphics, mapView, dataSet.getMatchedData());
            }

            graphics.setComposite(originalComposite);
            graphics.setStroke(originalStorke);
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
        super.enablePhotoDataSetDownloadActions(enablePrevious, enableNext);
    }


    @Override
    boolean addPhotoDataSetMenuItems() {
        return DataSet.getInstance().getPhotoDataSet() != null;
    }

    @Override
    boolean addSequenceMenuItem() {
        return DataSet.getInstance().getSelectedSequence() != null;
    }
}