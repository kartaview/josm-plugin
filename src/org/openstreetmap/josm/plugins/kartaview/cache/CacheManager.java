/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.cache;

import java.io.File;
import java.util.Set;
import org.apache.commons.jcs3.access.CacheAccess;
import org.apache.commons.jcs3.engine.CompositeCacheAttributes;
import org.apache.commons.jcs3.engine.behavior.ICompositeCacheAttributes.DiskUsagePattern;
import org.apache.commons.jcs3.engine.control.CompositeCacheManager;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.cache.JCSCacheManager;
import org.openstreetmap.josm.plugins.kartaview.argument.CacheSettings;


/**
 * Handles cache related operations.
 *
 * @author beataj
 * @version $Revision$
 */
public final class CacheManager {

    private static final String CACHE_NAME = "openstreetcam";
    private static final String CACHE_LOCATION = "/cache/";
    private static final String MEMORY_MANAGER = "org.apache.commons.jcs3.engine.memory.fifo.FIFOMemoryCache";
    private final CacheAccess<Key, CacheEntry> cache;

    private static final CacheManager INSTANCE = new CacheManager();


    private CacheManager() {
        final String pluginLocation =
                new File(Preferences.main().getPluginsDirectory(), GuiConfig.getInstance().getPluginShortName())
                        .getPath();
        final CacheSettings settings = PreferenceManager.getInstance().loadPreferenceSettings().getCacheSettings();

        this.cache = JCSCacheManager.getCache(CACHE_NAME, settings.getMemoryCount(), settings.getDiskCount(),
                pluginLocation + CACHE_LOCATION);
        final CompositeCacheAttributes attr = (CompositeCacheAttributes) this.cache.getCacheAttributes();
        attr.setDiskUsagePattern(DiskUsagePattern.SWAP);
        attr.setMemoryCacheName(MEMORY_MANAGER);
        attr.setUseMemoryShrinker(true);
        this.cache.setCacheAttributes(attr);
        this.cache.clear();
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