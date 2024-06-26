/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.argument.PhotoSize;
import org.openstreetmap.josm.plugins.kartaview.cache.CacheEntry;
import org.openstreetmap.josm.plugins.kartaview.cache.CacheManager;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.service.ClientLogger;
import org.openstreetmap.josm.plugins.kartaview.service.ServiceException;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;
import com.grab.josm.common.entity.Pair;


/**
 * Handles image loading related logic.
 *
 * @author beataj
 * @version $Revision$
 */
public final class PhotoHandler {

    private static final String STORAGE = "storage";
    private final CacheManager cacheManager = CacheManager.getInstance();
    private static final PhotoHandler INSTANCE = new PhotoHandler();
    private static final ClientLogger LOGGER = new ClientLogger("error");


    /**
     * Returns the unique instance of the image handler.
     *
     * @return {@code ImageHandler} instance
     */
    public static PhotoHandler getInstance() {
        return INSTANCE;
    }


    /**
     * Loads the photo with the specified properties.
     *
     * @param photo a {@code Photo} represents the currently selected object
     * @param type a {@code PhotoType} represents the type of photo to load
     * @return a pair of ({@code BufferedImage}, {@code Boolean}) representing the corresponding image and
     * boolean flag. The flag is true if the user requested the high quality image and for some reason the
     * image could not be retrieved and instead the large thumbnail is retrieved.
     * @throws PhotoHandlerException if the photo could not be loaded or if the photo content could not be
     * read
     */
    public Pair<BufferedImage, PhotoSize> loadPhoto(final Photo photo, final PhotoSize type)
            throws PhotoHandlerException {
        Pair<BufferedImage, PhotoSize> result = null;
        ImageIO.setUseCache(false);
        try {
            if (type.equals(PhotoSize.THUMBNAIL)) {
                result = loadThumbnailPhoto(photo);
            } else if (type.equals(PhotoSize.HIGH_QUALITY)) {
                if (DataSet.getInstance().isFrontFacingDisplayed()) {
                    result = loadHighQualityPhoto(photo);
                } else {
                    result = loadPhoto(photo.getSequenceId(), photo.getLargeThumbnailWrappedName(),
                            PhotoSize.LARGE_THUMBNAIL, false);
                }
            } else {
                if (DataSet.getInstance().isFrontFacingDisplayed()) {
                    result = loadPhoto(photo.getSequenceId(), photo.getLargeThumbnailName(), PhotoSize.LARGE_THUMBNAIL,
                            true);
                } else {
                    result = loadPhoto(photo.getSequenceId(), photo.getLargeThumbnailWrappedName(),
                            PhotoSize.LARGE_THUMBNAIL, true);
                }
            }
        } catch (final ServiceException e) {
            throw new PhotoHandlerException("Could not load photo from server.", e);
        } catch (final IOException e) {
            throw new PhotoHandlerException("Could not read photo content.", e);
        }
        return result;
    }

    private Pair<BufferedImage, PhotoSize> loadThumbnailPhoto(final Photo photo) throws ServiceException, IOException {
        // special case, we don't save small thumbnails to cache
        byte[] byteImage = null;

        if (PreferenceManager.getInstance().loadPhotoSettings().isDisplayFrontFacingFlag()) {
            byteImage = ServiceHandler.getInstance().retrievePhoto(photo.getThumbnailName());
        } else {
            byteImage = ServiceHandler.getInstance().retrievePhoto(photo.getWrappedName());
        }

        return new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(byteImage))),
                PhotoSize.THUMBNAIL);
    }

    private Pair<BufferedImage, PhotoSize> loadHighQualityPhoto(final Photo photo)
            throws ServiceException, IOException {
        Pair<BufferedImage, PhotoSize> result;
        final String name = photo.getName().contains(STORAGE) ? photo.getName() : photo.getOriName();
        try {
            result = loadPhoto(photo.getSequenceId(), name, PhotoSize.HIGH_QUALITY, false);
        } catch (final Exception e) {
            LOGGER.log("Error loading high quality photo: ", e);
            // try to load large thumbnail image
            result = loadPhoto(photo.getSequenceId(), photo.getLargeThumbnailName(), PhotoSize.LARGE_THUMBNAIL, true);
        }
        return result;
    }

    private Pair<BufferedImage, PhotoSize> loadPhoto(final Long sequenceId, final String photoName,
            final PhotoSize photoType, final boolean isWarning) throws ServiceException, IOException {
        final Pair<BufferedImage, PhotoSize> result;
        if (Config.getInstance().isCacheEnabled()) {
            final CacheEntry image = cacheManager.getPhoto(sequenceId, photoName);
            if (image == null) {
                // load image from server
                final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photoName);
                cacheManager.putPhoto(sequenceId, photoName, byteImage, isWarning);
                result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(byteImage))),
                        photoType);
            } else {
                result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(image.getContent()))),
                        photoType);
            }
        } else {
            final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photoName);
            result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(byteImage))), photoType);
        }
        return result;
    }

    /**
     * Loads the photos corresponding to the given set of elements and saves the loaded data to the cache.
     *
     * @param photos a set of {@code Photo}s
     */
    public void loadPhotos(final Set<Photo> photos) {
        if (Config.getInstance().isCacheEnabled()) {
            final boolean highQualityFlag = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag();
            if (photos != null && !photos.isEmpty()) {
                final ExecutorService executorService = Executors.newFixedThreadPool(photos.size());
                for (final Photo photo : photos) {
                    executorService.execute(() -> loadPhotoToCache(photo, highQualityFlag));
                }
                executorService.shutdown();
            }
        }
    }

    private void loadPhotoToCache(final Photo photo, final boolean highQualityFlag) {
        if (Config.getInstance().isCacheEnabled()) {
            if (highQualityFlag) {
                // retrieve and save high quality image
                try {
                    loadPhotoToCache(photo.getSequenceId(), photo.getName(), false);
                } catch (final Exception e) {
                    LOGGER.log("Error retrieving and save high quality image: ", e);
                    // try to load large thumbnail
                    try {
                        loadPhotoToCache(photo.getSequenceId(), photo.getLargeThumbnailName(), true);
                    } catch (final Exception e2) {
                        LOGGER.log("Error loading large thumbnail: ", e2);
                    }
                }
            } else {
                // retrieve and save large thumbnail
                try {
                    loadPhotoToCache(photo.getSequenceId(), photo.getLargeThumbnailName(), false);
                } catch (final Exception e2) {
                    LOGGER.log("Error retrieving and save large thumbnail: ", e2);
                }
            }
        }
    }

    private void loadPhotoToCache(final Long sequenceId, final String photoName, final boolean isWarning)
            throws ServiceException {
        if (Config.getInstance().isCacheEnabled()) {
            if (!cacheManager.containsPhoto(sequenceId, photoName)) {
                final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photoName);
                cacheManager.putPhoto(sequenceId, photoName, byteImage, isWarning);
            }
        }
    }
}