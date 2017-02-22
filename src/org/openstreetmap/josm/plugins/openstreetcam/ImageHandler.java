/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheEntry;
import org.openstreetmap.josm.plugins.openstreetcam.cache.CacheManager;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.service.ServiceException;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.Pair;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Handles image loading related logic.
 *
 * @author beataj
 * @version $Revision$
 */
public final class ImageHandler {

    private final CacheManager cacheManager = CacheManager.getInstance();
    private static final ImageHandler INSTANCE = new ImageHandler();


    public static ImageHandler getInstance() {
        return INSTANCE;
    }


    /**
     * Loads the photo with the specified properties.
     *
     * @param photo a {@code Photo} represents the currently selected object
     * @return a pair of ({@code BufferedImage}, {@code Boolean}) representing the corresponding image and boolean flag.
     * The flag is true if the user requested the high quality image and for some reason the image could not be
     * retrieved and instead the large thumbnail is retrieved.
     * @throws IOException
     * @throws ServiceException
     */
    public Pair<BufferedImage, Boolean> loadImage(final Photo photo) throws IOException, ServiceException {
        Pair<BufferedImage, Boolean> result;
        ImageIO.setUseCache(false);
        if (PreferenceManager.getInstance().loadPreferenceSettings().getImageSettings().isHighQualityFlag()) {
            // load high quality image
            final CacheEntry image = cacheManager.getImage(photo.getSequenceId(), photo.getName());
            if (image == null) {
                try {
                    final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photo.getName());
                    cacheManager.putImage(photo.getSequenceId(), photo.getName(), byteImage, false);
                    result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(byteImage))),
                            false);
                } catch (final ServiceException e) {
                    // load large thumbnail
                    final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photo.getLargeThumbnailName());
                    cacheManager.putImage(photo.getSequenceId(), photo.getLargeThumbnailName(), byteImage, true);
                    result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(byteImage))),
                            true);
                }
            } else {
                result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(image.getContent()))),
                        image.isWarning());
            }
        } else {
            // load large thumbnail
            final CacheEntry image = cacheManager.getImage(photo.getSequenceId(), photo.getLargeThumbnailName());
            if (image == null) {
                final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photo.getLargeThumbnailName());
                cacheManager.putImage(photo.getSequenceId(), photo.getLargeThumbnailName(), byteImage, false);
                result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(byteImage))), false);
            } else {
                result = new Pair<>(ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(image.getContent()))),
                        image.isWarning());
            }
        }
        return result;
    }

    /**
     * Loads the photos corresponding to the given set of elements and saves the loaded data to the cache.
     *
     * @param photos a set of {@code Photo}s
     */
    public void loadImages(final Set<Photo> photos) {
        final boolean highQualityFlag = PreferenceManager.getInstance().loadImageSettings().isHighQualityFlag();
        for (final Photo photo : photos) {
            ThreadPool.getInstance().execute(() -> loadImageToCache(photo, highQualityFlag));
        }

    }

    private void loadImageToCache(final Photo photo, final boolean highQualityFlag) {
        if (highQualityFlag) {
            // retrieve and save high quality image
            try {
                final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photo.getName());
                cacheManager.putImage(photo.getSequenceId(), photo.getName(), byteImage, false);
            } catch (final Exception e) {
                // try to load large thumbnail
                try {
                    final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photo.getLargeThumbnailName());
                    cacheManager.putImage(photo.getSequenceId(), photo.getLargeThumbnailName(), byteImage, true);
                } catch (final Exception e2) {
                    Main.warn(e2, "Error loading image:" + photo.getLargeThumbnailName());
                }
            }
        } else {
            // retrieve and save large thumbnail
            try {
                final byte[] byteImage = ServiceHandler.getInstance().retrievePhoto(photo.getLargeThumbnailName());
                cacheManager.putImage(photo.getSequenceId(), photo.getLargeThumbnailName(), byteImage, false);
            } catch (final Exception e2) {
                Main.warn(e2, "Error loading image:" + photo.getLargeThumbnailName());
            }
        }
    }
}