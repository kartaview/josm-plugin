/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.ConfidenceLevelCategory;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import com.grab.josm.common.entity.Pair;


/**
 * Used to return the appropriate ImageIcon background for clusters.
 * @author laurad
 */
public enum ClusterBackgroundIconFactory {

    INSTANCE;

    private final Map<ConfidenceLevelCategory, Pair<ImageIcon, ImageIcon>> coloredBorders;
    private final Map<ConfidenceLevelCategory, Pair<ImageIcon, ImageIcon>> edgeColoredBorders;
    private final Pair<ImageIcon, ImageIcon> colorlessBorders;
    private final Pair<ImageIcon, ImageIcon> edgeColorlessBorders;

    ClusterBackgroundIconFactory() {
        coloredBorders = new HashMap<>();
        edgeColoredBorders = new HashMap<>();
        for (final ConfidenceLevelCategory confidenceLevelCategory : ConfidenceLevelCategory.values()) {
            coloredBorders.put(confidenceLevelCategory, new Pair<>(
                    IconConfig.getInstance().getUnselectedClusterBordersColored().get(confidenceLevelCategory),
                    IconConfig.getInstance().getSelectedClusterBordersColored().get(confidenceLevelCategory)));
            edgeColoredBorders.put(confidenceLevelCategory, new Pair<>(
                    IconConfig.getInstance().getUnselectedEdgeClusterBordersColored().get(confidenceLevelCategory),
                    IconConfig.getInstance().getSelectedEdgeClusterBordersColored().get(confidenceLevelCategory)));
        }
        colorlessBorders = new Pair<>(IconConfig.getInstance().getClusterBackgroundUnselectedIconColorless(),
                IconConfig.getInstance().getClusterBackgroundSelectedIconColorless());
        edgeColorlessBorders = new Pair<>(IconConfig.getInstance().getEdgeClusterBackgroundUnselectedIconColorless(),
                IconConfig.getInstance().getEdgeClusterBackgroundSelectedIconColorless());
    }

    // TODO: refactor this method into 2 metods
    public ImageIcon getClusterBackground(final Cluster cluster, final boolean isSelected, final boolean isColorCoded,
            final boolean isEdgeCluster) {
        ImageIcon backgroundIcon;
        final Map<ConfidenceLevelCategory, Pair<ImageIcon, ImageIcon>> confidenceBorders =
                isEdgeCluster ? edgeColoredBorders : coloredBorders;
        final Pair<ImageIcon, ImageIcon> plainBorders = isEdgeCluster ? edgeColorlessBorders : colorlessBorders;
        if (isColorCoded) {
            backgroundIcon =
                    isSelected ? confidenceBorders.get(cluster.getConfidenceLevel().getConfidenceCategory()).getSecond()
                            : confidenceBorders.get(cluster.getConfidenceLevel().getConfidenceCategory()).getFirst();

            if (backgroundIcon == null) {
                backgroundIcon = isSelected ? plainBorders.getSecond() : plainBorders.getFirst();
            }
        } else {
            backgroundIcon = isSelected ? plainBorders.getSecond() : plainBorders.getFirst();
        }
        return backgroundIcon;
    }
}