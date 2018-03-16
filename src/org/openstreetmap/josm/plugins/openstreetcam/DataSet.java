/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.handler.PhotoHandler;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.thread.ThreadPool;


/**
 *
 *
 * @author beataj
 * @version $Revision$
 */
public final class DataSet {

    private static final DataSet INSTANCE = new DataSet();


    private List<Segment> segments = new ArrayList<>();
    private PhotoDataSet photoDataSet = new PhotoDataSet();
    private List<Detection> detections = new ArrayList<>();

    /** the currently selected photo */
    private Photo selectedPhoto;

    /** the currently selected detection */
    private Detection selectedDetection;
    private Sequence selectedSequence;
    private Photo startPhoto;
    private Collection<Photo> closestPhotos;


    private DataSet() {}


    public static DataSet getInstance() {
        return INSTANCE;
    }

    public synchronized void clear() {
        this.segments = null;
        this.detections = null;
        this.photoDataSet = null;
        this.selectedDetection = null;
        this.selectedPhoto = null;
        this.closestPhotos = null;
        this.startPhoto = null;
    }

    public void updateLowZoomLevelData(final List<Segment> segments) {
        this.segments = segments;

        if (hasPhotos()) {
            this.photoDataSet = null;
        }
        if (hasDetections()) {
            this.detections = null;
        }
        if (hasSelectedDetection()) {
            this.selectedDetection = null;
        }
        if (hasSelectedPhoto()) {
            this.selectedPhoto = null;
        }

        if (hasClosestPhotos()) {
            this.closestPhotos = null;
            this.startPhoto = null;

        }

    }

    public void updateHighZoomLevelDetectionData(final List<Detection> detections, final boolean updateSelection) {
        this.detections = detections;
        if (updateSelection && selectedDetection != null) {
            selectedDetection = detections != null ? detections.stream()
                    .filter(detection -> detection.equals(selectedDetection)).findFirst().orElse(null) : null;
        }
        if (hasSegments()) {
            this.segments = null;
        }
    }

    public void updateHighZoomLevelPhotoData(final PhotoDataSet photoDataSet, final boolean updateSelection) {
        this.photoDataSet = photoDataSet;
        if (updateSelection) {
            if (selectedPhoto != null) {
                selectedPhoto = photoDataSet != null ? photoDataSet.getPhotos().stream()
                        .filter(photo -> photo.equals(selectedPhoto)).findFirst().orElse(null) : null;
            }
        }
        if (getSelectedPhoto() != null && getClosestPhotos() != null) {
            selectStartPhotoForClosestAction(getSelectedPhoto());
            ThreadPool.getInstance().execute(() -> {
                final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                PhotoHandler.getInstance()
                .loadPhotos(nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount()));
            });
        }
        if (hasSegments()) {
            this.segments = null;
        }
    }

    /**
     * Returns the photo that is located near to the given point. The method returns null if there is no nearby item.
     *
     * @param point a {@code Point} represents location where the user had clicked
     * @return a {@code Photo}
     */
    public Photo nearbyPhoto(final Point point) {
        Photo photo = null;
        if (selectedSequence != null && selectedSequence.hasPhotos()) {
            photo = Util.nearbyPhoto(selectedSequence.getPhotos(), point);
            // API issue: does not return username for sequence photos
            if (selectedPhoto != null && photo != null) {
                photo.setUsername(selectedPhoto.getUsername());
            }
        }
        if (photo == null && photoDataSet != null && photoDataSet.hasItems()) {
            photo = Util.nearbyPhoto(photoDataSet.getPhotos(), point);
        }
        return photo;
    }

    /**
     * Returns the photo that is located near to the given point. The method returns null if there is no nearby item.
     *
     * @param point a {@code Point} represents location where the user had clicked
     * @return a {@code Photo}
     */
    public Detection nearbyDetection(final Point point) {
        Detection detection = null;
        if (selectedSequence != null && selectedSequence.hasDetections()) {
            detection = Util.nearbyDetection(selectedSequence.getDetections(), point);
        }
        if (detection == null && detections != null) {
            detection = Util.nearbyDetection(detections, point);
        }
        return detection;
    }


    /**
     * Returns the photos that are either previous/next or close to the selected photo.
     *
     * @param prevNextCount the number of previous/next photos to be returned
     * @param nearbyCount the number of nearby photos to be returned
     * @return a set of {@code Photo}s
     */
    public Set<Photo> nearbyPhotos(final int prevNextCount, final int nearbyCount) {
        final Set<Photo> result = new HashSet<>();
        if (selectedPhoto != null) {
            for (int i = 1; i <= prevNextCount; i++) {
                final Photo nextPhoto = sequencePhoto(selectedPhoto.getSequenceIndex() + i);
                if (nextPhoto != null) {
                    result.add(nextPhoto);
                }
                final Photo prevPhoto = sequencePhoto(selectedPhoto.getSequenceIndex() - i);
                if (prevPhoto != null) {
                    result.add(prevPhoto);
                }
            }
            if (photoDataSet != null && photoDataSet.hasItems()) {
                result.addAll(Util.nearbyPhotos(photoDataSet.getPhotos(), selectedPhoto, nearbyCount));
            }
        }
        return result;
    }

    /**
     * Checks if the given photo belongs or not to the selected sequence.
     *
     * @param photo a {@code Photo}
     * @return boolean
     */
    public boolean isPhotoPartOfSequence(final Photo photo) {
        boolean contains = false;
        if (selectedSequence != null && selectedSequence.hasPhotos()) {
            for (final Photo elem : selectedSequence.getPhotos()) {
                if (elem.equals(photo)) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Returns the photo from the sequence located at the given position. The method returns null if there is no
     * corresponding element.
     *
     * @param index represents the location of a photo in the selected sequence
     * @return a {@code Photo}
     */
    public Photo sequencePhoto(final int index) {
        Photo photo = null;
        if (selectedSequence != null && selectedSequence.hasPhotos()) {
            for (final Photo elem : selectedSequence.getPhotos()) {
                if (elem.getSequenceIndex().equals(index)) {
                    photo = elem;
                    // API issue: does not return username for sequence photos
                    photo.setUsername(selectedPhoto.getUsername());
                    break;
                }
            }
        } else if (photoDataSet != null && photoDataSet.hasItems() && selectedPhoto != null) {
            for (final Photo elem : photoDataSet.getPhotos()) {
                if (elem.getSequenceIndex().equals(index)
                        && elem.getSequenceId().equals(selectedPhoto.getSequenceId())) {
                    photo = elem;
                    break;
                }
            }
        }
        return photo;
    }

    /**
     * Sets a start photo from witch a possible closest image action should start.
     *
     * @param photo a {@code Photo}
     */
    public void selectStartPhotoForClosestAction(final Photo photo) {
        startPhoto = photo;
        if (photo != null && photoDataSet != null && photoDataSet.hasItems()) {
            closestPhotos = Util.nearbyPhotos(photoDataSet.getPhotos(), startPhoto,
                    Config.getInstance().getClosestPhotosMaxItems());
        } else {
            closestPhotos = Collections.emptyList();
        }
    }


    /**
     * Retrieve the closest image of the currently selected image.
     *
     * @return a {@code Photo}
     */
    public Photo closestSelectedPhoto() {
        Photo closestPhoto = null;
        if (!closestPhotos.isEmpty()) {
            closestPhoto = closestPhotos.iterator().next();
            closestPhotos.remove(closestPhoto);
        }
        // recalculate closest photos when latest closest photo is returned
        if (closestPhotos.isEmpty()) {
            closestPhotos = Util.nearbyPhotos(photoDataSet.getPhotos(), startPhoto,
                    Config.getInstance().getClosestPhotosMaxItems());
        }
        return closestPhoto;
    }

    public Photo detectionPhoto(final Long sequenceId, final Integer sequenceIndex) {
        Photo result = null;
        final List<Photo> photos =
                selectedSequence != null && selectedSequence.hasPhotos() ? selectedSequence.getPhotos()
                        : (photoDataSet != null && photoDataSet.hasItems()) ? photoDataSet.getPhotos() : null;
                        if (photos != null) {
                            for (final Photo photo : photos) {
                                if (photo.getSequenceId().equals(sequenceId) && photo.getSequenceIndex().equals(sequenceIndex)) {
                                    result = photo;
                                    break;
                                }
                            }
                        }
                        return result;
    }


    /**
     * Checks if the selected photo is the first photo of the sequence.
     *
     * @return true/false
     */
    public boolean enablePreviousPhotoAction() {
        return selectedSequence != null && selectedPhoto != null
                && !selectedSequence.getPhotos().get(0).getSequenceIndex().equals(selectedPhoto.getSequenceIndex());
    }

    /**
     * Checks if the selected photo is the last photo of the sequence.
     *
     * @return true/false
     */
    public boolean enableNextPhotoAction() {
        return selectedSequence != null && selectedPhoto != null
                && !selectedSequence.getPhotos().get(selectedSequence.getPhotos().size() - 1).getSequenceIndex()
                .equals(selectedPhoto.getSequenceIndex());
    }


    public Photo selectedSequenceLastPhoto() {
        final int index = selectedSequence.getPhotos().size();
        return selectedSequence.getPhotos().get(index - 1);
    }

    public void setSelectedPhoto(final Photo selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    public void setSelectedDetection(final Detection selectedDetection) {
        this.selectedDetection = selectedDetection;
    }

    public void setSelectedSequence(final Sequence selectedSequence) {
        this.selectedSequence = selectedSequence;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public PhotoDataSet getPhotoDataSet() {
        return photoDataSet;
    }

    public List<Detection> getDetections() {
        return detections;
    }

    public Photo getSelectedPhoto() {
        return selectedPhoto;
    }

    public Detection getSelectedDetection() {
        return selectedDetection;
    }

    public Sequence getSelectedSequence() {
        return selectedSequence;
    }

    public Collection<Photo> getClosestPhotos() {
        return closestPhotos;
    }

    public boolean hasItems() {
        return hasSegments() || hasDetections() || hasPhotos();
    }

    public boolean hasPhotos() {
        return photoDataSet != null && photoDataSet.hasItems();
    }

    public boolean hasSegments() {
        return segments != null && !segments.isEmpty();
    }

    public boolean hasDetections() {
        return detections != null && !detections.isEmpty();
    }

    public boolean hasNearbyPhotos() {
        return closestPhotos != null && !closestPhotos.isEmpty();
    }

    public boolean hasSelectedSequence() {
        return selectedSequence != null;
    }

    public boolean hasSelectedPhoto() {
        return selectedPhoto != null;
    }

    public boolean hasSelectedDetection() {
        return selectedDetection != null;
    }

    public boolean hasClosestPhotos() {
        return closestPhotos != null && !closestPhotos.isEmpty();
    }

}