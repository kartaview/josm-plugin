/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;
import org.openstreetmap.josm.plugins.openstreetcam.handler.ServiceHandler;

import java.util.ArrayList;
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

    private static Map<String, List<Sign>> allSigns;
    private static List<String> regions;
    private static final String BLURRING_TYPE = "BLURRING";
    private static final String PHOTO_QUALITY_TYPE = "PHOTO_QUALITY";
    private final static DetectionTypeContent INSTANCE = new DetectionTypeContent();

    private DetectionTypeContent() {
        generateSigns();
    }

    public static DetectionTypeContent getInstance() {
        return INSTANCE;
    }

    public static void generateSigns() {
        if (allSigns == null || allSigns.isEmpty()) {
            final List<Sign> signs = ServiceHandler.getInstance().listSigns();
            if (signs != null) {
                allSigns = signs.stream().collect(Collectors.groupingBy(Sign::getType));
                allSigns.remove(BLURRING_TYPE);
                allSigns.remove(PHOTO_QUALITY_TYPE);
                //add all icons to hash so they do not cause delay on request while the plugin is running
                signs.stream().filter(sign -> !sign.getType().equals(BLURRING_TYPE) && !sign.getType()
                        .equals(PHOTO_QUALITY_TYPE)).forEach(sign -> DetectionIconFactory.INSTANCE.getIcon(sign, false));
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

    List<String> getRegions() {
        return regions == null ? new ArrayList<>() : new ArrayList<>(regions);
    }
}