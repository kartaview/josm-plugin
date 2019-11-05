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
import org.openstreetmap.josm.data.coor.LatLon;


/**
 * Entity that matched an OSM relation element to the corresponding downloaded element. This type of element has no
 * osm_id or from, to nodes and is represented by WAY_SECTION members.
 *
 * @author laurad
 */
public class DownloadedRelation extends OsmElement {

    private static final float TRANSLATION = 0.00001f;
    private final List<DownloadedWay> downloadedMembers;

    public DownloadedRelation(final OsmElement element) {
        super(element.getOsmId(), element.getType(), element.getMembers(), element.getFromId(), element.getOsmId(),
                element.getTag());
        downloadedMembers = new ArrayList<>();

    }

    public void addMember(final DownloadedWay member) {
        downloadedMembers.add(member);
    }

    public List<DownloadedWay> getDownloadedMembers() {
        return downloadedMembers;
    }

    /**
     * To avoid overlapping ways, this method translates one of the ways to the right.
     */
    public void translateIdenticalMembers() {
        for (int i = 1; i < downloadedMembers.size(); i++) {
            if (downloadedMembers.get(i).getMatchedFromNode()
                    .hasEqualSemanticAttributes(downloadedMembers.get(i - 1).getMatchedFromNode()) && downloadedMembers
                    .get(i).getMatchedToNode()
                    .hasEqualSemanticAttributes(downloadedMembers.get(i - 1).getMatchedToNode())) {
                downloadedMembers.get(i).setMatchedFromCoordinates(
                        new LatLon(downloadedMembers.get(i).getMatchedFromNode().lat(),
                                downloadedMembers.get(i).getMatchedFromNode().lon() + TRANSLATION));
                downloadedMembers.get(i).setMatchedToCoordinates(
                        new LatLon(downloadedMembers.get(i).getMatchedToNode().lat(),
                                downloadedMembers.get(i).getMatchedToNode().lon() + TRANSLATION));
                downloadedMembers.get(i).getDownloadedNodes().parallelStream()
                        .forEach(node -> node.setCoor(new LatLon(node.lat(), node.lon() + TRANSLATION)));
            }
        }
    }
}