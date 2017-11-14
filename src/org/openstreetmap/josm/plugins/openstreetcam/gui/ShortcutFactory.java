/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.tools.Shortcut;


/**
 * Factory for the shortcuts associated with the plugin.
 *
 * @author beataj
 * @version $Revision$
 */
public final class ShortcutFactory {

    private static final Map<String, Shortcut> defaultShortcutMap = initializeDefaultShortcuts();
    private final Map<String, Shortcut> shortcutMap;

    private static final ShortcutFactory INSTANCE = new ShortcutFactory();


    private ShortcutFactory() {
        shortcutMap = new HashMap<>();
        Shortcut.listAll().forEach(item -> {
            if (item.getShortText().startsWith(GuiConfig.getInstance().getPluginShortName())) {
                shortcutMap.put(item.getShortText(), item);
            }
        });
    }

    private static Map<String, Shortcut> initializeDefaultShortcuts() {
        final GuiConfig guiConfig = GuiConfig.getInstance();
        final Map<String, Shortcut> map = new HashMap<>();
        map.put(guiConfig.getPluginShortcutText(), Shortcut.registerShortcut(guiConfig.getPluginShortcutText(),
                guiConfig.getPluginShortcutText(), KeyEvent.VK_1, Shortcut.ALT_CTRL));
        map.put(guiConfig.getPluginShortcutText(), Shortcut.registerShortcut(guiConfig.getPluginDetectionShortcutText(),
                guiConfig.getPluginDetectionShortcutText(), KeyEvent.VK_2, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnPreviousShortcutText(),
                Shortcut.registerShortcut(guiConfig.getBtnPreviousShortcutText(),
                        guiConfig.getBtnPreviousShortcutText(), KeyEvent.VK_LEFT, Shortcut.ALT));
        map.put(guiConfig.getBtnNextShortcutText(), Shortcut.registerShortcut(guiConfig.getBtnNextShortcutText(),
                guiConfig.getBtnNextShortcutText(), KeyEvent.VK_RIGHT, Shortcut.ALT));
        map.put(guiConfig.getBtnClosestShortcutText(), Shortcut.registerShortcut(guiConfig.getBtnClosestShortcutText(),
                guiConfig.getBtnClosestShortcutText(), KeyEvent.VK_2, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnPlayShortcutText(), Shortcut.registerShortcut(guiConfig.getBtnPlayShortcutText(),
                guiConfig.getBtnPlayShortcutText(), KeyEvent.VK_3, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnLocationShortcutText(),
                Shortcut.registerShortcut(guiConfig.getBtnLocationShortcutText(),
                        guiConfig.getBtnLocationShortcutText(), KeyEvent.VK_4, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnWebPageShortcutTlt(), Shortcut.registerShortcut(guiConfig.getBtnWebPageShortcutTlt(),
                guiConfig.getBtnWebPageShortcutTlt(), KeyEvent.VK_5, Shortcut.ALT_CTRL));
        map.put(guiConfig.getDlgFilterShortcutText(), Shortcut.registerShortcut(guiConfig.getDlgFilterShortcutText(),
                guiConfig.getDlgFilterShortcutText(), KeyEvent.VK_6, Shortcut.ALT_CTRL));
        map.put(guiConfig.getLayerFeedbackShortcutText(),
                Shortcut.registerShortcut(guiConfig.getLayerFeedbackShortcutText(),
                        guiConfig.getLayerFeedbackShortcutText(), KeyEvent.VK_7, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnDataSwitchShortcutTlt(),
                Shortcut.registerShortcut(guiConfig.getBtnDataSwitchShortcutTlt(),
                        guiConfig.getBtnDataSwitchShortcutTlt(), KeyEvent.VK_8, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnMatchedWayShortcutTlt(),
                Shortcut.registerShortcut(guiConfig.getBtnMatchedWayShortcutTlt(),
                        guiConfig.getBtnMatchedWayShortcutTlt(), KeyEvent.VK_9, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnFixDetectionShortcutText(),
                Shortcut.registerShortcut(guiConfig.getBtnFixDetectionShortcutText(),
                        guiConfig.getBtnFixDetectionShortcutText(), KeyEvent.VK_F, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnAlreadyFixedDetectionShortcutText(),
                Shortcut.registerShortcut(guiConfig.getBtnAlreadyFixedDetectionShortcutText(),
                        guiConfig.getBtnAlreadyFixedDetectionShortcutText(), KeyEvent.VK_A, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnOtherActionOnDetectionShortcutText(),
                Shortcut.registerShortcut(guiConfig.getBtnOtherActionOnDetectionShortcutText(),
                        guiConfig.getBtnOtherActionOnDetectionShortcutText(), KeyEvent.VK_O, Shortcut.ALT_CTRL));
        map.put(guiConfig.getBtnBadDetectionShortcutText(),
                Shortcut.registerShortcut(guiConfig.getBtnBadDetectionShortcutText(),
                        guiConfig.getBtnBadDetectionShortcutText(), KeyEvent.VK_B, Shortcut.ALT_CTRL));
        map.put(guiConfig.getLayerDeleteMenuItemLbl(), Shortcut.registerShortcut(guiConfig.getLayerDeleteMenuItemLbl(),
                guiConfig.getLayerDeleteMenuItemTlt(), KeyEvent.VK_DELETE, 0));
        return map;
    }

    /**
     * Returns the unique instance of the shortcut factory.
     *
     * @return a {@code ShortcutFactory}
     */
    public static ShortcutFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the shortcut associated with the given key. The method returns the default shortcut associated with the
     * key, if the shortcut was not registered in JOSM.
     *
     * @param key a {@code String} representing a shortcut key
     * @return a {@code Shortcut}
     */
    public Shortcut getShotrcut(final String key) {
        final Shortcut shortcut = shortcutMap.get(key);
        return shortcut != null ? shortcut : defaultShortcutMap.get(key);
    }
}