/*
 * Copyright 2020 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 */
package org.openstreetmap.josm.plugins.kartaview.entity;

import java.util.List;


/**
 * Represents the result set for the EdgeLayer.
 *
 * @author beata.tautan
 * @version $Revision$
 */
public class EdgeDataResultSet {

    private List<EdgeDetection> edgeDetections;
    private List<Cluster> edgeClusters;


    public EdgeDataResultSet() {
    }

    public EdgeDataResultSet(List<EdgeDetection> edgeDetections, List<Cluster> edgeClusters) {
        this.edgeDetections = edgeDetections;
        this.edgeClusters = edgeClusters;
    }

    public List<EdgeDetection> getEdgeDetections() {
        return edgeDetections;
    }

    public List<Cluster> getEdgeClusters() {
        return edgeClusters;
    }
}