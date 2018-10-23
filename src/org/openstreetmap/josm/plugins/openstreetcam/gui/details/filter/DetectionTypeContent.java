package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;
import org.openstreetmap.josm.plugins.openstreetcam.handler.ServiceHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * This class retrieves all the signs from the server and stores them in a map using their type as key.
 * The BLURRING type is ignored.
 *
 * @author laurad
 */
public class DetectionTypeContent {

    private Map<String, List<Sign>> allSigns;
    private static final String BLURRING_TYPE = "BLURRING";
    private static final DetectionTypeContent INSTANCE = new DetectionTypeContent();

    private DetectionTypeContent() {
        if (allSigns == null) {
            List<Sign> signs = ServiceHandler.getInstance().listSigns();
            allSigns = signs.stream().collect(Collectors.groupingBy(Sign::getType));
            allSigns.remove(BLURRING_TYPE);
            //add all icons to hash so they do not cause delay on request while the plugin is running
            signs.forEach(sign -> DetectionIconFactory.INSTANCE.getIcon(sign,false));
        }
    }

    public static DetectionTypeContent getInstance() {
        return INSTANCE;
    }

    Map<String, List<Sign>> getContent() {
        return allSigns;
    }
}