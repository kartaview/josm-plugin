/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.ImageProvider.ImageSizes;
import com.telenav.josm.common.entity.Pair;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public enum DetectionIconFactory {

    INSTANCE;

    private static final String EXT_PNG = ".png";
    private static final String EXT_SVG = ".svg";
    private static final String SIGN_POST_PREFIX = "regulatory--arrow";
    private static final String SIGN_POST_ICON_NAME = "information--highway-interchange--g1";
    private final Map<String, Pair<ImageIcon, ImageIcon>> iconsMap;


    private DetectionIconFactory() {
        iconsMap = new HashMap<>();
        for (final String file : new File(IconConfig.getInstance().getDetectionIconsLongPath()).list()) {
            final String fileName = file.replace(EXT_PNG, "").replace(EXT_SVG, "");
            final String filePath = IconConfig.getInstance().getDetectionIconsPath() + "/" + file;
            iconsMap.put(fileName, new Pair<>(ImageProvider.get(filePath, ImageSizes.CURSOR),
                    ImageProvider.get(filePath, ImageSizes.MAPMAX)));
        }
    }

    public ImageIcon getIcon(final String name, final boolean isSelected) {
        final ImageIcon icon;
        if (name.startsWith(SIGN_POST_PREFIX)) {
            icon = isSelected ? iconsMap.get(SIGN_POST_ICON_NAME).getSecond()
                    : iconsMap.get(SIGN_POST_ICON_NAME).getFirst();
        } else {
            icon = isSelected ? iconsMap.get(name).getSecond() : iconsMap.get(name).getFirst();
        }
        return icon;
    }
}