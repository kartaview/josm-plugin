/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the user configurable cache settings attributes.
 *
 * @author beataj
 * @version $Revision$
 */
public class CacheSettings {

    private final Integer memoryCount;
    private final Integer diskCount;
    private final Integer prevNextCount;
    private final Integer nearbyCount;


    /**
     * Builds a new object with the given arguments.
     *
     * @param memoryCount the number objects to keep in memory
     * @param diskCount the number of objects to keep in disk
     * @param prevNextCount the number of next and previous objects to cache
     * @param nearbyCount the number of nearby objects to cache
     */
    public CacheSettings(final Integer memoryCount, final Integer diskCount, final Integer prevNextCount,
            final Integer nearbyCount) {
        this.memoryCount = memoryCount;
        this.diskCount = diskCount;
        this.prevNextCount = prevNextCount;
        this.nearbyCount = nearbyCount;
    }


    public Integer getMemoryCount() {
        return memoryCount;
    }

    public Integer getDiskCount() {
        return diskCount;
    }

    public Integer getPrevNextCount() {
        return prevNextCount;
    }

    public Integer getNearbyCount() {
        return nearbyCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(diskCount);
        result = prime * result + EntityUtil.hashCode(memoryCount);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final CacheSettings other = (CacheSettings) obj;
            result = EntityUtil.bothNullOrEqual(diskCount, other.getDiskCount());
            result = result && EntityUtil.bothNullOrEqual(memoryCount, other.getMemoryCount());
        }
        return result;
    }
}