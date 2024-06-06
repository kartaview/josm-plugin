/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.common;

import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;


/**
 * Defines common functionality used by the {@code Cluster} details panel.
 *
 * @author beata.tautan
 * @version $Revision$
 */
public abstract class BaseClusterDetailsPanel extends BaseDetailsPanel<Cluster> {

    private static final long serialVersionUID = 5552298497676355176L;

    // constants commonly used for building the panel
    protected static final int INFO_TO_TABLE_EXTRA_HEIGHT = 5;
    protected static final int HEADER_TO_CONTENT_EXTRA_HEIGHT = 5;
    protected static final int TABLE_END_EXTRA_HEIGHT = 50;
    protected static final int LINE_HEIGHT = 25;
    protected static final int ROW_HEIGHT = 15;
    protected static final int ID_COLUMN = 0;

    private Cluster cluster;

    /**
     * Adds a {@code JTable} displaying the list of detections that belongs to the cluster.
     *
     * @param cluster the {@code Cluster} displayed in the panel
     */
    protected abstract void addDetectionsTable(final Cluster cluster);

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Cluster getCluster() {
        return cluster;
    }
}