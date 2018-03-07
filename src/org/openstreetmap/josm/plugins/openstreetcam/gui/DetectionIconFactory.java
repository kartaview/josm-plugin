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
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
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

    private static final String SIGN_POST_TYPE = "SIGN_POST";
    private static final String SIGN_POST_ICON_NAME = "information--highway-interchange--g1.svg";
    private final Map<String, Pair<ImageIcon, ImageIcon>> iconsMap;


    private DetectionIconFactory() {
        iconsMap = new HashMap<>();
    }

    public ImageIcon getIcon(final Sign sign, final boolean isSelected) {
        final String iconName = sign.getType().equals(SIGN_POST_TYPE) ? SIGN_POST_ICON_NAME : sign.getIconName();
        Pair<ImageIcon, ImageIcon> iconPair = iconsMap.get(iconName);
        if (iconPair == null) {
            iconPair = new Pair<>(loadIcon(iconName, ImageSizes.LARGEICON), loadIcon(iconName, ImageSizes.CURSOR));
            iconsMap.put(iconName, iconPair);
        }
        return isSelected ? iconPair.getSecond() : iconPair.getFirst();
    }

    private ImageIcon loadIcon(final String name, final ImageSizes size) {
        final String iconPath = IconConfig.getInstance().getDetectionIconsPath() + "/" + name;
        return ImageProvider.get(iconPath, size);
    }
}