/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2019, Telenav, Inc. All Rights Reserved
 */

package org.openstreetmap.josm.plugins.openstreetcam.entity;

import org.openstreetmap.josm.data.osm.Node;


/**
 * Entity that matches a downloaded Node Element to the OsmElement.
 *
 * @author laurad
 */
public class DownloadedNode extends OsmElement {

    private Node matchedNode;

    public DownloadedNode(final OsmElement element, final Node matchedNode) {
        super(element.getOsmId(), element.getType(), element.getMembers(), element.getFromId(), element.getOsmId(),
                element.getTag());
        this.matchedNode = matchedNode;
    }

    public Node getMatchedNode() {
        return matchedNode;
    }
}