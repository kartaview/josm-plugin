/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.ImageProvider.ImageSizes;
import org.openstreetmap.josm.tools.JosmRuntimeException;
import com.telenav.josm.common.entity.Pair;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public enum DetectionIconFactory {

    INSTANCE;

    private static final String DELIMITER = "/";
    private static final String UNKNOWN_ICON_NAME = "unknown.svg";
    private static final String SIGN_POST_TYPE = "SIGN_POST";
    private static final String SIGN_POST_ICON_NAME = "information--highway-interchange--g1.svg";
    private final Map<String, Pair<ImageIcon, ImageIcon>> iconsMap;


    private DetectionIconFactory() {
        iconsMap = new ConcurrentHashMap<>();
    }

    public ImageIcon getIcon(final Sign sign, final boolean isSelected) {
        String iconName = sign.getType().equals(SIGN_POST_TYPE) ? SIGN_POST_ICON_NAME : sign.getIconName();
        iconName = iconName == null ? IconConfig.getInstance().getDetectionIconsPath() + DELIMITER + UNKNOWN_ICON_NAME
                : iconName;
        final Pair<ImageIcon, ImageIcon> iconPair = iconsMap.computeIfAbsent(iconName,
                n -> new Pair<>(loadIcon(n, ImageSizes.LARGEICON), loadIcon(n, ImageSizes.CURSOR)));
        return isSelected ? iconPair.getSecond() : iconPair.getFirst();
    }

    private ImageIcon loadIcon(final String name, final ImageSizes size) {
        final String iconPath = IconConfig.getInstance().getDetectionIconsPath() + DELIMITER + name;
        ImageIcon icon = null;
        try {
            icon = ImageProvider.get(iconPath, size);
        } catch (final JosmRuntimeException ex) {
            final String defaultIcon = IconConfig.getInstance().getDetectionIconsPath() + DELIMITER + UNKNOWN_ICON_NAME;
            icon = ImageProvider.get(defaultIcon, size);
        }
        return icon;
    }
}