/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util;

import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Utility class, contains methods for obtaining the current bounding box.
 *
 * @author beataj
 * @version $Revision$
 */
public final class BoundingBoxUtil {

    private static final int EXTENSION_DISTANCE = 100;

    private BoundingBoxUtil() {}

    /**
     * Returns a bounding box that represents the current search area. The method takes into consideration also the edit
     * layer bounds. If there are more edit layer bounds the method returns the bounds of the current MapView. Use this
     * method for photo locations.
     *
     * @return list of {@code Circle}s
     */
    public static BoundingBox currentBoundingBox() {
        final List<Bounds> osmDataLayerBounds = editLayerDataBounds();
        Bounds bounds;
        if (osmDataLayerBounds != null && !osmDataLayerBounds.isEmpty()) {
            if (osmDataLayerBounds.size() > 1) {
                bounds = Main.map.mapView.getRealBounds();
            } else {
                bounds = osmDataLayerBounds.get(0);
            }
        } else {
            bounds = Main.map.mapView.getRealBounds();
        }
        return new BoundingBox(bounds.getMax().lat(), bounds.getMin().lat(), bounds.getMax().lon(),
                bounds.getMin().lon());
    }

    /**
     * Returns a list of bounding boxes, representing the current search area. The method takes into consideration also
     * the edit layer bounds. Use this method for segments.
     *
     * @return a list of {@code BoundingBox}
     */
    public static List<BoundingBox> currentBoundingBoxes() {
        final List<BoundingBox> result = new ArrayList<>();
        final List<Bounds> osmDataLayerBounds = editLayerDataBounds();
        if (osmDataLayerBounds != null && !osmDataLayerBounds.isEmpty()) {
            for (final Bounds osmBounds : osmDataLayerBounds) {
                if (Main.map.mapView.getRealBounds().intersects(osmBounds)) {
                    result.add(new BoundingBox(osmBounds.getMax().lat(), osmBounds.getMin().lat(),
                            osmBounds.getMax().lon(), osmBounds.getMin().lon()));
                }
            }
        } else {
            final Bounds bounds = Main.map.mapView.getRealBounds();
            final BoundingBox bbox = new BoundingBox(bounds.getMax().lat(), bounds.getMin().lat(),
                    bounds.getMax().lon(), bounds.getMin().lon());
            final double distance = EXTENSION_DISTANCE * Main.map.mapView.getScale();
            result.add(bbox.extendBoundingBox((int) distance));
        }
        return result;
    }


    private static List<Bounds> editLayerDataBounds() {
        List<Bounds> osmDataLayerBounds = null;
        if (Main.getLayerManager().getEditLayer() != null
                && (Main.getLayerManager().getActiveLayer() instanceof OsmDataLayer)) {
            osmDataLayerBounds = Main.getLayerManager().getEditLayer().data.getDataSourceBounds();
        }
        return osmDataLayerBounds;
    }
}