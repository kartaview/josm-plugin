/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import com.grab.josm.common.entity.EntityUtil;


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