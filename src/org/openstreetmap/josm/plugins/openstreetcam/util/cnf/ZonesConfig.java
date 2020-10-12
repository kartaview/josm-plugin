package org.openstreetmap.josm.plugins.openstreetcam.util.cnf;

import com.grab.josm.common.cnf.BaseConfig;


/**
 * Load values related to zones' borders.
 *
 * @author nicoleta.viregan
 */
public final class ZonesConfig extends BaseConfig {

    private static final String CONFIG_FILE = "openstreetcam_zones.properties";
    private static final ZonesConfig INSTANCE = new ZonesConfig();

    String borderZone1;
    String borderZone2;

    private ZonesConfig() {
        super(CONFIG_FILE);
        borderZone1 = readProperty("border.zone.1");
        borderZone2 = readProperty("border.zone.2");
    }

    public static ZonesConfig getInstance() {
        return INSTANCE;
    }

    public String getBorderZone1() {
        return borderZone1;
    }

    public String getBorderZone2() {
        return borderZone2;
    }
}