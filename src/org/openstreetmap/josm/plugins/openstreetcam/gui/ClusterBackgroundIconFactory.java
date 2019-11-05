/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */

package org.openstreetmap.josm.plugins.openstreetcam.gui;

import com.telenav.josm.common.entity.Pair;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;

import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;


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
                double minRange = CLUSTER_CONFIDENCE_COLOR_RANGE_STEP * i;
                double maxRange = CLUSTER_CONFIDENCE_COLOR_RANGE_STEP * (i + 1);
                if (cluster.getConfidenceLevel() >= minRange && cluster.getConfidenceLevel() <= maxRange) {
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