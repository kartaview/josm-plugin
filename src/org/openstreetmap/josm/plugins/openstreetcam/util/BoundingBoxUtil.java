/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util;

import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import com.telenav.josm.common.argument.BoundingBox;
import org.openstreetmap.josm.plugins.openstreetcam.argument.MapViewSettings;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


/**
 * Utility class, contains methods for obtaining the current bounding box.
 *
 * @author beataj
 * @version $Revision$
 */
public final class BoundingBoxUtil {

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
                bounds = MainApplication.getMap().mapView.getRealBounds();
            } else {
                bounds = osmDataLayerBounds.get(0);
            }
        } else {
            bounds = MainApplication.getMap().mapView.getRealBounds();
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
    private static List<BoundingBox> currentBoundingBoxes() {
        final List<BoundingBox> result = new ArrayList<>();
        final List<Bounds> osmDataLayerBounds = editLayerDataBounds();
        if (osmDataLayerBounds != null && !osmDataLayerBounds.isEmpty()) {
            for (final Bounds osmBounds : osmDataLayerBounds) {
                if (MainApplication.getMap().mapView.getRealBounds().intersects(osmBounds)) {
                    result.add(new BoundingBox(osmBounds.getMax().lat(), osmBounds.getMin().lat(),
                            osmBounds.getMax().lon(), osmBounds.getMin().lon()));
                }
            }
        } else {
            result.add(mapViewBounds());
        }
        return result;
    }

    /**
     * Based on the load data preference that tells if the users wishes for data to be loaded outside the active area
     * or not, the appropriate list of active areas is returned.
     * @param mapViewDataLoad - if true, only the active areas are retured. If false, the whole mapView is returned.
     * @return List of {@code BoundingBox} containing all active areas
     */
    public static List<BoundingBox> currentActiveAreas(final boolean mapViewDataLoad) {
        final List<BoundingBox> result = new ArrayList<>();
        if (!mapViewDataLoad) {
            result.add(mapViewBounds());
        } else {
            result.addAll(currentBoundingBoxes());
        }
        return result;
    }

    /**
     * Returns a list of Bounds objects, representing the current active area. If no area is active, The whole map is
     * returned.
     *
     * @return a list of {@code Bounds}
     */
    public static List<Bounds> currentBounds() {
        final MapViewSettings mapViewSettings = PreferenceManager.getInstance().loadMapViewSettings();
        List<Bounds> result = new ArrayList<>();
        final List<Bounds> osmDataLayerBounds = editLayerDataBounds();
        if (!mapViewSettings.isDataLoadFlag() || osmDataLayerBounds == null || osmDataLayerBounds.isEmpty()) {
            result.add(new Bounds(-90, -180, 90, 180));
        } else {
            result = osmDataLayerBounds;
        }
        return result;
    }

    private static BoundingBox mapViewBounds() {
        final Bounds bounds = MainApplication.getMap().mapView.getRealBounds();
        return new BoundingBox(bounds.getMax().lat(), bounds.getMin().lat(), bounds.getMax().lon(),
                bounds.getMin().lon());
    }

    private static List<Bounds> editLayerDataBounds() {
        List<Bounds> osmDataLayerBounds = null;
        if (MainApplication.getLayerManager().getEditLayer() != null
                && (MainApplication.getLayerManager().getActiveLayer() instanceof OsmDataLayer)
                && MainApplication.getLayerManager().getActiveLayer().isVisible()) {
            osmDataLayerBounds = MainApplication.getLayerManager().getEditLayer().data.getDataSourceBounds();
        }
        return osmDataLayerBounds;
    }
}