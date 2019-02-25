package org.openstreetmap.josm.plugins.openstreetcam.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * Entity that matched an OSM relation element to the corresponding downloaded element.
 * This type of element has no  osm_id or from, to nodes and is represented by WAY_SECTION members.
 *
 * @author laurad
 */
public class DownloadedRelation extends OsmElement {

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

}