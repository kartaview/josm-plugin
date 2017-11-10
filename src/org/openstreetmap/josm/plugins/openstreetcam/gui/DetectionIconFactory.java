/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui;

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
    }

    public ImageIcon getIcon(final String name, final boolean isSelected) {
        final String iconName = name.startsWith(SIGN_POST_PREFIX) ? SIGN_POST_ICON_NAME : name;
        Pair<ImageIcon, ImageIcon> iconPair = iconsMap.get(iconName);
        if (iconPair == null) {
            iconPair = new Pair<>(loadIcon(iconName, ImageSizes.LARGEICON), loadIcon(iconName, ImageSizes.CURSOR));
            iconsMap.put(name, iconPair);
        }
        return isSelected ? iconPair.getSecond() : iconPair.getFirst();
    }

    private ImageIcon loadIcon(final String name, final ImageSizes size) {
        ImageIcon icon;
        try {
            final String iconPath = IconConfig.getInstance().getDetectionIconsPath() + "/" + name + EXT_SVG;
            icon = ImageProvider.get(iconPath, size);
        } catch (final Exception ex) {
            // try to load png
            final String iconPath = IconConfig.getInstance().getDetectionIconsPath() + "/" + name + EXT_PNG;
            icon = ImageProvider.get(iconPath, size);
        }
        return icon;
    }
}