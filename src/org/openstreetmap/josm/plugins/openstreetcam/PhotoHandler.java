/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoSize;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheEntry;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.service.ServiceException;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.Logging;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Handles image loading related logic.
 *
 * @author beataj
 * @version $Revision$
 */
public final class PhotoHandler {

    private final CacheManager cacheManager = CacheManager.getInstance();
    private static final PhotoHandler INSTANCE = new PhotoHandler();


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
     * @return a pair of ({@code BufferedImage}, {@code Boolean}) representing the corresponding image and boolean flag.
     * The flag is true if the user requested the high quality image and for some reason the image could not be
     * retrieved and instead the large thumbnail is retrieved.
     * @throws PhotoHandlerException if the photo could not be loaded or if the photo content could not be read
     */
    public Pair<BufferedImage, PhotoSize> loadPhoto(final Photo photo, final PhotoSize type)
            throws PhotoHandlerException {
        Pair<BufferedImage, PhotoSize> result;
        ImageIO.setUseCache(false);
        try {
            if (type.equals(PhotoSize.THUMBNAIL)) {
                result = loadThumbnailPhoto(photo);
            } else if (type.equals(PhotoSize.HIGH_QUALITY)) {
                result = loadHighQualityPhoto(photo);
            } else {
                result = loadPhoto(photo.getSequenceId(), photo.getLargeThumbnailName(), PhotoSize.LARGE_THUMBNAIL,
                        true);
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
        final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photo.getThumbnailName());
        return new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(byteImage))),
                PhotoSize.THUMBNAIL);
    }

    private Pair<BufferedImage, PhotoSize> loadHighQualityPhoto(final Photo photo)
            throws ServiceException, IOException {
        Pair<BufferedImage, PhotoSize> result;
        try {
            result = loadPhoto(photo.getSequenceId(), photo.getName(), PhotoSize.HIGH_QUALITY, false);
        } catch (final Exception e) {
            // try to load large thumbnail image
            result = loadPhoto(photo.getSequenceId(), photo.getLargeThumbnailName(), PhotoSize.LARGE_THUMBNAIL, true);
        }
        return result;
    }

    private Pair<BufferedImage, PhotoSize> loadPhoto(final Long sequenceId, final String photoName,
            final PhotoSize photoType, final boolean isWarning) throws ServiceException, IOException {
        final Pair<BufferedImage, PhotoSize> result;
        final CacheEntry image = cacheManager.getPhoto(sequenceId, photoName);
        if (image == null) {
            // load image from server
            final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photoName);
            cacheManager.putPhoto(sequenceId, photoName, byteImage, isWarning);
            result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(byteImage))), photoType);
        } else {
            result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(image.getContent()))),
                    photoType);
        }
        return result;
    }

    /**
     * Loads the photos corresponding to the given set of elements and saves the loaded data to the cache.
     *
     * @param photos a set of {@code Photo}s
     */
    public void loadPhotos(final Set<Photo> photos) {
        final boolean highQualityFlag = PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag();
        for (final Photo photo : photos) {
            ThreadPool.getInstance().execute(() -> loadPhotoToCache(photo, highQualityFlag));
        }
    }

    private void loadPhotoToCache(final Photo photo, final boolean highQualityFlag) {
        if (highQualityFlag) {
            // retrieve and save high quality image
            try {
                loadPhotoToCache(photo.getSequenceId(), photo.getName(), false);
            } catch (final Exception e) {
                // try to load large thumbnail
                try {
                    loadPhotoToCache(photo.getSequenceId(), photo.getLargeThumbnailName(), true);
                } catch (final Exception e2) {
                    Logging.warn("Error loading image:" + photo.getLargeThumbnailName(), e2);
                }
            }
        } else {
            // retrieve and save large thumbnail
            try {
                loadPhotoToCache(photo.getSequenceId(), photo.getLargeThumbnailName(), false);
            } catch (final Exception e2) {
                Logging.warn("Error loading image:" + photo.getLargeThumbnailName(), e2);
            }
        }
    }

    private void loadPhotoToCache(final Long sequenceId, final String photoName, final boolean isWarning)
            throws ServiceException {
        if (!cacheManager.containsPhoto(sequenceId, photoName)) {
            final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photoName);
            cacheManager.putPhoto(sequenceId, photoName, byteImage, isWarning);
        }
    }
}