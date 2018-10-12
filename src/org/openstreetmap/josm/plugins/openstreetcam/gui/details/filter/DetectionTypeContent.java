package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.handler.ServiceHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class DetectionTypeContent {

    private Map<String, List<Sign>> allSigns;
    private static final String BLURRING_TYPE = "BLURRING";
    private static final DetectionTypeContent INSTANCE = new DetectionTypeContent();

    private DetectionTypeContent() {
        if (allSigns == null) {
            List<Sign> signs = ServiceHandler.getInstance().listSigns();
            allSigns = signs.stream().collect(Collectors.groupingBy(Sign::getType));
            allSigns.remove(BLURRING_TYPE);
        }
    }

    public static DetectionTypeContent getInstance() {
        return INSTANCE;
    }

    public List<String> getTypes() {
        return new ArrayList<>(allSigns.keySet());
    }

    Map<String, List<Sign>> getContent() {
        return allSigns;
    }

}