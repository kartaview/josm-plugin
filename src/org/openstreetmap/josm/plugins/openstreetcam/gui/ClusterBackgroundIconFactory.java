/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import com.grab.josm.common.entity.Pair;


/**
 * Used to return the appropriate ImageIcon background for clusters.
 * @author laurad
 */
public enum ClusterBackgroundIconFactory {

    INSTANCE;

    private final Map<Integer, Pair<ImageIcon, ImageIcon>> coloredBorders;
    private final Pair<ImageIcon, ImageIcon> colorlessBorders;
    static final double CLUSTER_CONFIDENCE_COLOR_RANGE_STEP = 0.1;

    ClusterBackgroundIconFactory() {
        coloredBorders = new HashMap<>();
        for (int i = 0; i < IconConfig.getInstance().getClusterBordersColored().size(); i++) {
            coloredBorders.put(i, new Pair<>(IconConfig.getInstance().getClusterBordersColored().get(i),
                    IconConfig.getInstance().getSelectedClusterBordersColored().get(i)));
        }
        colorlessBorders = new Pair<>(IconConfig.getInstance().getClusterBackgroundIconColorless(),
                IconConfig.getInstance().getClusterBackgroundSelectedIconColorless());
    }

    public ImageIcon getClusterBackground(final Cluster cluster, final boolean isSelected, final boolean isColorCoded) {
        ImageIcon backgroundIcon = null;
        if (isColorCoded) {
            int i = 0;
            while (i < coloredBorders.size() && backgroundIcon == null) {
                final double minRange = CLUSTER_CONFIDENCE_COLOR_RANGE_STEP * i;
                final double maxRange = CLUSTER_CONFIDENCE_COLOR_RANGE_STEP * (i + 1);
                if (cluster.getConfidenceLevel().getOverallConfidence() >= minRange
                        && cluster.getConfidenceLevel().getOverallConfidence() <= maxRange) {
                    backgroundIcon = isSelected ? coloredBorders.get(i).getSecond() : coloredBorders.get(i).getFirst();
                }
                i++;
            }
            if (backgroundIcon == null) {
                backgroundIcon = isSelected ? colorlessBorders.getSecond() : colorlessBorders.getFirst();
            }
        } else {
            backgroundIcon = isSelected ? colorlessBorders.getSecond() : colorlessBorders.getFirst();
        }
        return backgroundIcon;
    }
}