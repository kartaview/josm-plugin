/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.cache;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.engine.CompositeCacheAttributes;
import org.apache.commons.jcs.engine.behavior.ICompositeCacheAttributes.DiskUsagePattern;
import org.apache.commons.jcs.engine.control.CompositeCacheManager;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.cache.JCSCacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


/**
 * Handles cache related operations.
 *
 * @author beataj
 * @version $Revision$
 */
public final class CacheManager {

    private static final String CACHE_NAME = "openstreetcam";
    private static final String CACHE_LOCATION = "/cache/";
    private static final String MEMORY_MANAGER = "org.apache.commons.jcs.engine.memory.fifo.FIFOMemoryCache";
    private final CacheAccess<Key, CacheEntry> cache;

    private static final CacheManager INSTANCE = new CacheManager();


    private CacheManager() {
        final String pluginLocation =
                new File(Main.pref.getPluginsDirectory(), GuiConfig.getInstance().getPluginShortName()).getPath();
        final CacheSettings settings = PreferenceManager.getInstance().loadPreferenceSettings().getCacheSettings();
        try {
            this.cache = JCSCacheManager.getCache(CACHE_NAME, settings.getMemoryCount(), settings.getDiskCount(),
                    pluginLocation + CACHE_LOCATION);
            final CompositeCacheAttributes attr = (CompositeCacheAttributes) this.cache.getCacheAttributes();
            attr.setDiskUsagePattern(DiskUsagePattern.SWAP);
            attr.setMemoryCacheName(MEMORY_MANAGER);
            this.cache.setCacheAttributes(attr);
            this.cache.clear();
        } catch (final IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    public static CacheManager getInstance() {
        return INSTANCE;
    }

    /**
     * Adds an image to the cache.
     *
     * @param key the key of the image
     * @param image the image in byte format
     * @param warning a flag indicating if the image loading was successful or not
     */
    public void putPhoto(final Long sequenceId, final String imageName, final byte[] image, final boolean warning) {
        cache.put(new Key(sequenceId, imageName), new CacheEntry(image, warning));
    }

    /**
     * Returns the image corresponding to the given name. The method returns null if there is no corresponding image.
     *
     * @param imageName the name of the image
     * @return {@code BufferedImage}
     */
    public CacheEntry getPhoto(final Long sequenceId, final String imageName) {
        return cache.get(new Key(sequenceId, imageName));
    }

    /**
     * Checks if the there is any image associated with the given key in the cache.
     *
     * @param imageName
     * @return
     */
    public boolean containsPhoto(final Long sequenceId, final String imageName) {
        final Set<Object> keySet = CompositeCacheManager.getInstance().getCache(CACHE_NAME).getKeySet();
        return keySet.contains(new Key(sequenceId, imageName));
    }

    /**
     * Removes all the images associated with the given prefix.
     *
     * @param prefix
     */
    public void removePhotos(final Long sequenceId) {
        final Set<Object> keySet = CompositeCacheManager.getInstance().getCache(CACHE_NAME).getKeySet();
        for (final Object key : keySet) {
            if (((Key) key).getSequenceId().equals(sequenceId)) {
                cache.remove((Key) key);
            }
        }
    }
}