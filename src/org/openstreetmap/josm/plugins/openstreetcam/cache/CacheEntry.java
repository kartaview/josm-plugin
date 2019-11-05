/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.cache;

import org.openstreetmap.josm.data.cache.BufferedImageCacheEntry;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the entry that is used in order to save images in the cache.
 *
 * @author beataj
 * @version $Revision$
 */
public class CacheEntry extends BufferedImageCacheEntry {

    private static final long serialVersionUID = -4038546512805668129L;

    /**
     * Flag that indicates if there was any issue during image loading.
     */
    private final boolean warning;


    /**
     * Builds a new object with the given arguments.
     *
     * @param image the image content in byte array format
     * @param warning specifies if there was or not any warning durring the image loading
     */
    public CacheEntry(final byte[] image, final boolean warning) {
        super(image);
        this.warning = warning;
    }


    public boolean isWarning() {
        return warning;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + EntityUtil.hashCode(warning);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final CacheEntry other = (CacheEntry) obj;
            result = EntityUtil.bothNullOrEqual(warning, other.isWarning());
        }
        return result;
    }
}