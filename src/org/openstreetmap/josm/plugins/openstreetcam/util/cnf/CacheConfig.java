/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Loads cache configuration properties.
 *
 * @author beataj
 * @version $Revision$
 */
public final class CacheConfig extends BaseConfig {

    private static final String CONFIG_FILE = "openstreetcam_cache.properties";
    private static final int DEFAULT_MEMORY_COUNT = 50;
    private static final int DEFAULT_DISK_COUNT = 5000;
    private static final int DEFAULT_PREV_NEXT_COUNT = 5;
    private static final int DEFAULT_NEARBY_COUNT = 10;
    private static final CacheConfig INSTANCE = new CacheConfig();

    /* default values to use if there is no user specific cache settings */
    private final int defaultMemoryCount;
    private final int defaultDiskCount;
    private final int defaultPrevNextCount;
    private final int defaultNearbyCount;

    /* maximum values for cache settings */
    private final int maxMemoryCount;
    private final int maxDiskCount;
    private final int maxPrevNextCount;
    private final int maxNearbyCount;


    private CacheConfig() {
        super(CONFIG_FILE);

        defaultMemoryCount = readIntegerProperty("default.memory.count", DEFAULT_MEMORY_COUNT);
        defaultDiskCount = readIntegerProperty("default.disk.count", DEFAULT_DISK_COUNT);
        defaultPrevNextCount = readIntegerProperty("default.prevNext.count", DEFAULT_PREV_NEXT_COUNT);
        defaultNearbyCount = readIntegerProperty("default.nearby.count", DEFAULT_NEARBY_COUNT);

        maxMemoryCount = readIntegerProperty("max.memory.count", DEFAULT_MEMORY_COUNT);
        maxDiskCount = readIntegerProperty("max.disk.count", DEFAULT_DISK_COUNT);
        maxPrevNextCount = readIntegerProperty("max.prevNext.count", DEFAULT_PREV_NEXT_COUNT);
        maxNearbyCount = readIntegerProperty("max.nearby.count", DEFAULT_NEARBY_COUNT);
    }


    public static CacheConfig getInstance() {
        return INSTANCE;
    }


    public int getDefaultMemoryCount() {
        return defaultMemoryCount;
    }

    public int getDefaultDiskCount() {
        return defaultDiskCount;
    }

    public int getDefaultPrevNextCount() {
        return defaultPrevNextCount;
    }

    public int getDefaultNearbyCount() {
        return defaultNearbyCount;
    }

    public int getMaxMemoryCount() {
        return maxMemoryCount;
    }

    public int getMaxDiskCount() {
        return maxDiskCount;
    }

    public int getMaxPrevNextCount() {
        return maxPrevNextCount;
    }

    public int getMaxNearbyCount() {
        return maxNearbyCount;
    }
}