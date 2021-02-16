/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import java.awt.Color;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.layer.ImageryLayer;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.kartaview.entity.Segment;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.BING_LAYER_NAME;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.MAPBOX_LAYER_NAME;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.OPAQUE_ALPHA;
import static org.openstreetmap.josm.plugins.kartaview.gui.layer.Constants.SEGMENT_TRANSPARENCY;


/**
 * Utility class, holds methods used for defining specific UI settings such as color, transparency.
 *
 * @author beataj
 * @version $Revision$
 */
final class PaintUtil {

    private PaintUtil() {}

    static SortedMap<Integer, Float> generateSegmentTransparencyMap(final List<Segment> segments) {
        final SortedMap<Integer, Float> map = new TreeMap<>();
        if (!segments.isEmpty()) {
            final SortedSet<Integer> coverages = new TreeSet<>();
            for (final Segment segment : segments) {
                coverages.add(segment.getCoverage());
            }
            map.put(coverages.first(), SEGMENT_TRANSPARENCY[0]);
            map.put(coverages.last(), SEGMENT_TRANSPARENCY[SEGMENT_TRANSPARENCY.length - 1]);

            final Integer[] list = coverages.toArray(new Integer[0]);
            final int count = coverages.size() / SEGMENT_TRANSPARENCY.length;
            int index = 0;
            for (int i = 0; i < SEGMENT_TRANSPARENCY.length - 1; i++) {
                index += count;
                map.put(list[index], SEGMENT_TRANSPARENCY[i]);

            }
        }
        return map;
    }

    static float segmentTransparency(final SortedMap<Integer, Float> map, final Integer coverage,
            final float originalTransparency) {
        float transparency = SEGMENT_TRANSPARENCY[0];
        if (map.size() > 1) {
            for (final Entry<Integer, Float> entry : map.entrySet()) {
                if (coverage <= entry.getKey()) {
                    transparency = entry.getValue();
                    break;
                }
            }

        } else {
            transparency = map.get(coverage);
        }
        if (originalTransparency < OPAQUE_ALPHA) {
            // take into account global JOSM transparency setting
            transparency =
                    OPAQUE_ALPHA.equals(transparency) ? originalTransparency : (originalTransparency * transparency);
        }
        return transparency;
    }

    static Color lineColor(final MapView mapView, final Color color) {
        String mapLayerName = "";
        if (mapView.getLayerManager().getActiveLayer() instanceof ImageryLayer) {
            mapLayerName = ((ImageryLayer) mapView.getLayerManager().getActiveLayer()).getInfo().getName();
        } else {
            for (final Layer layer : mapView.getLayerManager().getLayers()) {
                if (layer.isVisible() && layer instanceof ImageryLayer) {
                    mapLayerName = ((ImageryLayer) layer).getInfo().getName();
                    break;
                }
            }
        }
        return mapLayerName.equals(BING_LAYER_NAME) || mapLayerName.equals(MAPBOX_LAYER_NAME) ? color.brighter()
                : color.darker();
    }
}