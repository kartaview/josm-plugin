/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.ImageProvider.ImageSizes;
import com.grab.josm.common.entity.Pair;


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
    private static final String NULL_VALUE_ICON_NAME = "NULL";
    private static final String SIGN_POST_ICON_NAME = "information--highway-interchange--g1.svg";
    private final Map<String, Pair<ImageIcon, ImageIcon>> iconsMap;


    DetectionIconFactory() {
        iconsMap = new ConcurrentHashMap<>();
    }

    public ImageIcon getIcon(final Sign sign, final boolean isSelected) {
        String iconName = Objects.nonNull(sign.getType()) && sign.getType().equals(SIGN_POST_TYPE) ? SIGN_POST_ICON_NAME
                : sign.getIconName();
        iconName = iconName == null || iconName.isEmpty() || iconName.equals(NULL_VALUE_ICON_NAME) ? UNKNOWN_ICON_NAME
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
        } catch (final RuntimeException ex) {
            final String defaultIconPath =
                    IconConfig.getInstance().getDetectionIconsPath() + DELIMITER + UNKNOWN_ICON_NAME;
            icon = ImageProvider.get(defaultIconPath, size);
        }
        return icon;
    }

    public ImageIcon detectionBackgroundIcon(final boolean isSelected) {
        final ImageIcon backgroundIcon;
        final Pair<ImageIcon, ImageIcon> detectionHeadingIcon =
                new Pair<>(IconConfig.getInstance().getDetectionHeadingUnselectedIcon(),
                        IconConfig.getInstance().getDetectionHeadingSelectedIcon());
        backgroundIcon = isSelected ? detectionHeadingIcon.getSecond() : detectionHeadingIcon.getFirst();
        return backgroundIcon;
    }

    public ImageIcon edgeDetectionBackgroundIcon(final boolean isSelected) {
        final ImageIcon backgroundIcon;
        final Pair<ImageIcon, ImageIcon> edgeDetectionHeadingIcon =
                new Pair<>(IconConfig.getInstance().getEdgeDetectionHeadingUnselectedIcon(),
                        IconConfig.getInstance().getEdgeDetectionHeadingSelectedIcon());
        backgroundIcon = isSelected ? edgeDetectionHeadingIcon.getSecond() : edgeDetectionHeadingIcon.getFirst();
        return backgroundIcon;
    }
}