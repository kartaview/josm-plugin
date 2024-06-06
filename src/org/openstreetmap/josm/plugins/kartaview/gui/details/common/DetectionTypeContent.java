/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.gui.DetectionIconFactory;
import org.openstreetmap.josm.plugins.kartaview.handler.ServiceHandler;


/**
 * This class retrieves all the signs from the server and stores them in a map using their type as key.
 * The BLURRING type is ignored.
 *
 * @author laurad
 */
public class DetectionTypeContent {

    private static Map<String, List<Sign>> allSigns;
    private static List<String> regions;
    private static final String BLURRING_TYPE = "BLURRING";
    private static final String PHOTO_QUALITY_TYPE = "PHOTO_QUALITY";
    private static final String POI = "POI";
    private final static DetectionTypeContent INSTANCE = new DetectionTypeContent();

    private DetectionTypeContent() {
        generateSigns();
        generateRegions();
    }

    public static DetectionTypeContent getInstance() {
        return INSTANCE;
    }

    public static void generateSigns() {
        if (allSigns == null || allSigns.isEmpty()) {
            final List<Sign> signs = ServiceHandler.getInstance().listSigns();
            if (signs != null) {
                allSigns =
                        signs.stream().collect(Collectors.groupingBy(Sign::getType, TreeMap::new, Collectors.toList()));
                allSigns.remove(BLURRING_TYPE);
                allSigns.remove(PHOTO_QUALITY_TYPE);
                allSigns.remove(POI);
                //add all icons to hash so they do not cause delay on request while the plugin is running
                signs.stream().filter(sign -> !sign.getType().equals(BLURRING_TYPE) && !sign.getType()
                        .equals(PHOTO_QUALITY_TYPE) && !sign.getType().equals(POI))
                .forEach(sign -> DetectionIconFactory.INSTANCE.getIcon(sign, false));
            }
        }
    }

    public static void generateRegions() {
        if(regions == null || regions.isEmpty()){
            regions = ServiceHandler.getInstance().listRegions();
        }
    }

    Map<String, List<Sign>> getContent() {
        return allSigns;
    }

    public List<String> getRegions() {
        return regions == null ? new ArrayList<>() : new ArrayList<>(regions);
    }
}