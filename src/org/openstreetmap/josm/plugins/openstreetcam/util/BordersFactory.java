package org.openstreetmap.josm.plugins.openstreetcam.util;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoZones;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.ZonesConfig;
import org.openstreetmap.josm.tools.Geometry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author nicoleta.viregan
 */
public class BordersFactory {

    private static final String COMMA = ",";
    private static final String SPACE = " ";
    private static final String COORD_SEPARATOR = ", ";
    private static final int  LAT_INDEX = 1;
    private static final int LONG_INDEX = 0;
    private final Map<PhotoZones, List<Node>> borders;

    private static final BordersFactory INSTANCE = new BordersFactory();

    private BordersFactory() {
        borders = new HashMap<>();
        borders.put(PhotoZones.PH, buildBorder(ZonesConfig.getInstance().getBorderZone1()));
        borders.put(PhotoZones.SG, buildBorder(ZonesConfig.getInstance().getBorderZone2()));
    }

    private List<Node> buildBorder(final String borderSimplePolygonText) {
        final List<String> latLonPairsText =
                Arrays.asList(borderSimplePolygonText.replace(COORD_SEPARATOR, COMMA).split(COMMA));
        return latLonPairsText.stream().map(pair -> new Node(
                new LatLon(Double.parseDouble(pair.split(SPACE)[LAT_INDEX]),
                        Double.parseDouble(pair.split(SPACE)[LONG_INDEX])))).collect(Collectors.toList());
    }

    public static BordersFactory getInstance() {
        return INSTANCE;
    }

    public List<Node> getBorder(final PhotoZones zone) {
        return borders.get(zone);
    }

    public boolean isPhotoInAvailableZone(final LatLon photoCoordinates) {
        final Node correlatedNode = new Node(photoCoordinates);
        return Geometry.nodeInsidePolygon(correlatedNode, BordersFactory.getInstance().getBorder(PhotoZones.PH))
                || Geometry.nodeInsidePolygon(correlatedNode, BordersFactory.getInstance().getBorder(PhotoZones.SG));
    }
}