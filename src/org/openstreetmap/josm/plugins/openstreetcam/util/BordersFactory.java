package org.openstreetmap.josm.plugins.openstreetcam.util;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.ZonesConfig;
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
    private static final String ZONE_1 = "PH";
    private static final String ZONE_2 = "SG";
    private final Map<String, List<Node>> borders;

    private static final BordersFactory INSTANCE = new BordersFactory();

    private BordersFactory() {
        borders = new HashMap<>();
        borders.put(ZONE_1, buildBorder(ZonesConfig.getInstance().getBorderZone1()));
        borders.put(ZONE_2, buildBorder(ZonesConfig.getInstance().getBorderZone2()));
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

    public List<Node> getBorder(final String zone) {
        return borders.get(zone);
    }
}