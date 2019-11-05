/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */

package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.tools.Geometry;


/**
 * Entity that matched a WAY Osm Element to the downloaded data. There are two cases: 1. The OsmElement has toNode and
 * fromNode so only those 2 Nodes are downloaded. 2. The OsmElement only has the osmId, therefore the download returns a
 * list of Node elements.
 *
 * @author laurad
 */
public class DownloadedWay extends OsmElement {

    public static final int SIZE_OF_TWO_POINT_LIST = 2;
    private final Node matchedFromNode;
    private final Node matchedToNode;
    private final List<Node> downloadedNodes;
    private final Boolean isStraight;

    public DownloadedWay(final OsmElement element, final Node matchedFromNode, final Node matchedToNode,
            final Way downloadedWay) {
        super(element.getOsmId(), element.getType(), element.getMembers(), element.getFromId(), element.getOsmId(),
                element.getTag());
        this.matchedFromNode = new Node(matchedFromNode);
        this.matchedToNode = new Node(matchedToNode);
        this.downloadedNodes = copyNodes(downloadedWay.getNodes());
        isStraight = determineWayStraightness(downloadedWay, matchedFromNode, matchedToNode);
    }

    public DownloadedWay(final OsmElement element, final Way downloadedWay) {
        super(element.getOsmId(), element.getType(), element.getMembers(), element.getFromId(), element.getOsmId(),
                element.getTag());
        this.downloadedNodes = copyNodes(downloadedWay.getNodes());
        matchedToNode = null;
        matchedFromNode = null;
        isStraight = null;
    }

    public Node getMatchedFromNode() {
        return matchedFromNode;
    }

    public Node getMatchedToNode() {
        return matchedToNode;
    }

    public List<Node> getDownloadedNodes() {
        return downloadedNodes;
    }

    public boolean isStraight() {
        return isStraight;
    }

    void setMatchedFromCoordinates(final LatLon newCoordinates) {
        matchedFromNode.setCoor(newCoordinates);
    }

    void setMatchedToCoordinates(final LatLon newCoordinates) {
        matchedToNode.setCoor(newCoordinates);
    }

    private List<Node> copyNodes(final List<Node> nodes) {
        return nodes.stream().map(Node::new).collect(Collectors.toList());
    }

    private boolean determineWayStraightness(final Way way, final Node fromNode, final Node toNode) {
        boolean straight = true;
        final List<Node> waySection = new ArrayList<>(way.getNodes().subList( way.getNodes().indexOf(fromNode), way.getNodes().indexOf(toNode)));
        if(waySection.size() > SIZE_OF_TWO_POINT_LIST) {
            List<Double> angles = new ArrayList<>();
            for (int i = 1; i < waySection.size() - 1; i++) {
                angles.add(Geometry.getNormalizedAngleInDegrees(
                        Geometry.getCornerAngle(waySection.get(i - 1).getEastNorth(), waySection.get(i).getEastNorth(), waySection.get(i).getEastNorth())));
            }
            for (int i = 0; i < angles.size() - 2; i++) {
                if (Math.abs(angles.get(i) - angles.get(i + 1)) > 2) {
                    straight = false;
                }
            }
        }
        return straight;
    }
}