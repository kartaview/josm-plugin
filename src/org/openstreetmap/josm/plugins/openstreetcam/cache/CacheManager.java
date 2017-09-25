/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
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
            attr.setUseMemoryShrinker(true);
            this.cache.setCacheAttributes(attr);
            this.cache.clear();
        } catch (final IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Returns the unique instance of the cache manager.
     *
     * @return a {@code CacheManager} object
     */
    public static CacheManager getInstance() {
        return INSTANCE;
    }

    /**
     * Adds a photo to the cache.
     *
     * @param sequenceId the identifier of the sequence to which the photo belongs
     * @param photoName the name of the photo
     * @param content the photo content in byte format
     * @param warning a flag indicating if the photo loading was successful or not
     */
    public void putPhoto(final Long sequenceId, final String photoName, final byte[] content, final boolean warning) {
        cache.put(new Key(sequenceId, photoName), new CacheEntry(content, warning));
    }

    /**
     * Returns the photo corresponding to the given name. The method returns null if there is no corresponding photo.
     *
     * @param sequenceId the identifier of the sequence to which the photo belongs
     * @param photoName the name of the photo
     * @return a {@code CacheEntry} object
     */
    public CacheEntry getPhoto(final Long sequenceId, final String photoName) {
        return cache.get(new Key(sequenceId, photoName));
    }

    /**
     * Removes all the photos associated with the given sequence identifier.
     *
     * @param sequenceId the identifier of the sequence to which the photo belongs
     */
    public void removePhotos(final Long sequenceId) {
        final Set<Object> keySet = CompositeCacheManager.getInstance().getCache(CACHE_NAME).getKeySet();
        for (final Object key : keySet) {
            if (((Key) key).getSequenceId().equals(sequenceId)) {
                cache.remove((Key) key);
            }
        }
    }

    /**
     * Verifies if the cache contains the given photo.
     *
     * @param sequenceId the identifier of the sequence to which the photo belongs
     * @param photoName the name of the photo
     * @return true if the cache contains the photo, false otherwise
     */
    public boolean containsPhoto(final Long sequenceId, final String photoName) {
        return getPhoto(sequenceId, photoName) != null;
    }
}