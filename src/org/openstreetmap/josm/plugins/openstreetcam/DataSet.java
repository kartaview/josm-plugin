/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
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
 * Holds the plugin's data.
 *
 * @author beataj
 * @version $Revision$
 */
public final class DataSet {

    private static final DataSet INSTANCE = new DataSet();

    private List<Segment> segments = new ArrayList<>();
    private PhotoDataSet photoDataSet = new PhotoDataSet();
    private List<Detection> detections = new ArrayList<>();
    private List<Cluster> clusters = new ArrayList<>();

    /** the currently selected photo */
    private Photo selectedPhoto;

    /** the currently selected detection */
    private Detection selectedDetection;

    /** the currently selected cluster */
    private Cluster selectedCluster;

    /** the currently selected sequence */
    private Sequence selectedSequence;

    /** the currently selected nearby photos */
    private Photo nearyPhotosStartPhoto;
    private Collection<Photo> nearbyPhotos;

    private DataSet() {}

    public static DataSet getInstance() {
        return INSTANCE;
    }

    /**
     * Clears the current data set, including the selections.
     */
    public synchronized void clear() {
        this.segments = new ArrayList<>();
        this.detections = new ArrayList<>();
        this.photoDataSet = new PhotoDataSet();
        this.clusters = new ArrayList<>();
        clearSelection();
    }

    /**
     * Clears the currently selected items.
     */
    public synchronized void clearSelection() {
        this.selectedDetection = null;
        this.selectedPhoto = null;
        this.nearbyPhotos = new ArrayList<>();
        this.nearyPhotosStartPhoto = null;
        this.selectedSequence = null;
        this.selectedCluster = null;
    }

    /**
     * Clears the high zoom level data (photo locations and detections) including selected items.
     */
    public synchronized void cleaHighZoomLevelData() {
        this.detections = new ArrayList<>();
        this.photoDataSet = new PhotoDataSet();
        this.clusters = new ArrayList<>();
        clearSelection();
    }

    /**
     * Updates the low zoom level data with a new list of {@code Segment}s.
     *
     * @param segments a new list of {@code Segment}s
     */
    public synchronized void updateLowZoomLevelData(final List<Segment> segments) {
        this.segments = segments;
    }

    /**
     * Updates the detection data with a new list of detections.
     *
     * @param detections a new list of {@code Detection}s
     * @param updateSelection if true - then the currently selected detection is removed if not present in
     * the new list of data
     */
    public synchronized void updateHighZoomLevelDetectionData(final List<Detection> detections,
            final boolean updateSelection) {
        this.detections = detections;
        if (updateSelection && selectedDetection != null) {
            selectedDetection = detections != null ? detections.stream()
                    .filter(detection -> detection.equals(selectedDetection)).findFirst().orElse(null) : null;
        }
    }

    /**
     * Updates the cluster data with a new list of clusters.
     *
     * @param clusters a list of {@code Cluster}s
     * @param updateSelection if true - then the currently selected cluster is removed if not present in the
     * new list of data
     */
    public synchronized void updateHighZoomLevelClusterData(final List<Cluster> clusters,
            final boolean updateSelection) {
        this.clusters = clusters;
        if (updateSelection && selectedCluster != null) {
            selectedCluster = clusters != null
                    ? clusters.stream().filter(cluster -> cluster.equals(selectedCluster)).findFirst().orElse(null)
                            : null;
        }
    }

    /**
     * Updates the photo location data with a new list of photos.
     *
     * @param photoDataSet a {@code PhotoDataSet} containing a new list of {@code Photo}s
     * @param updateSelection if true - then the currently selected photo is removed if not present in the
     * new data set
     */
    public synchronized void updateHighZoomLevelPhotoData(final PhotoDataSet photoDataSet,
            final boolean updateSelection) {
        this.photoDataSet = photoDataSet;
        if (updateSelection && hasSelectedPhoto()) {
            selectedPhoto = photoDataSet != null ? photoDataSet.getPhotos().stream()
                    .filter(photo -> photo.equals(selectedPhoto)).findFirst().orElse(null) : null;
        }
        if (hasSelectedPhoto() && hasNearbyPhotos()) {
            selectNearbyPhotos(getSelectedPhoto());
            ThreadPool.getInstance().execute(() -> {
                final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                PhotoHandler.getInstance()
                .loadPhotos(nearbyPhotos(cacheSettings.getPrevNextCount(), cacheSettings.getNearbyCount()));
            });
        }
    }

    /**
     * Returns the photo that is located near to the given point. The method returns null if there is no
     * nearby item.
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
     * Returns the detection that is located near to the given point. The method returns null if there is
     * no nearby item.
     *
     * @param point a {@code Point} represents location where the user had clicked
     * @return a {@code Detection}
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
     * Returns the cluster that is located near to the given point. The method returns null if there is no
     * nearby item.
     *
     * @param point a {@code Point} represents the location on the screen where the user had clicked
     * @return a {@code Cluster}
     */
    public Cluster nearbyCluster(final Point point) {
        return clusters != null ? Util.nearbyCluster(clusters, point) : null;
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
     * Returns the photo from the sequence located at the given position. The method returns null if there
     * is no corresponding element.
     *
     * @param index represents the location of a photo in the selected sequence
     * @return a {@code Photo}
     */
    synchronized public Photo sequencePhoto(final int index) {
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
                    // todo add case
                    photo = elem;
                    break;
                }
            }
        }
        return photo;
    }

    public Photo clusterPhoto(final boolean isNext) {
        Photo photo = null;
        int index;
        if (selectedPhoto != null) {
            int selectedIndex = 0;
            for (int i = 0; i < selectedCluster.getPhotos().size(); i++) {
                if (selectedPhoto.equals(selectedCluster.getPhotos().get(i))) {
                    selectedIndex = i;
                    break;
                }
            }

            index = isNext ? ++selectedIndex : --selectedIndex;
            if (index > selectedCluster.getPhotos().size() - 1) {
                index = 0;
            }
            if (index < 0) {
                index = selectedCluster.getPhotos().size() - 1;
            }
            photo = selectedCluster.getPhotos().get(index);
        }
        return photo;
    }

    /**
     * Sets a start photo from witch a possible closest image action should start.
     *
     * @param photo
     * a {@code Photo}
     */
    public void selectNearbyPhotos(final Photo photo) {
        nearyPhotosStartPhoto = photo;
        if (photo != null && photoDataSet != null && photoDataSet.hasItems()) {
            nearbyPhotos = Util.nearbyPhotos(photoDataSet.getPhotos(), nearyPhotosStartPhoto,
                    Config.getInstance().getClosestPhotosMaxItems());
        } else {
            nearbyPhotos = Collections.emptyList();
        }
    }

    /**
     * Retrieve the closest image of the currently selected image.
     *
     * @return a {@code Photo}
     */
    public Photo nearbyPhoto() {
        Photo result = null;
        if (!nearbyPhotos.isEmpty()) {
            result = nearbyPhotos.iterator().next();
            nearbyPhotos.remove(result);
        }
        // recalculate closest photos when latest closest photo is returned
        if (nearbyPhotos.isEmpty()) {
            nearbyPhotos = Util.nearbyPhotos(photoDataSet.getPhotos(), nearyPhotosStartPhoto,
                    Config.getInstance().getClosestPhotosMaxItems());
        }
        return result;
    }

    /**
     * Retrieve the photo identified by the given (sequenceId, sequenceIndex) pair.
     *
     * @param sequenceId the identifier of the sequence
     * @param sequenceIndex the photo index in the given sequence
     * @return a {@code Photo}
     */
    public Photo getPhoto(final Long sequenceId, final Integer sequenceIndex) {
        Photo result = null;
        final List<Photo> photos = hasSelectedSequence() && selectedSequence.hasPhotos() ? selectedSequence.getPhotos()
                : hasPhotos() ? photoDataSet.getPhotos() : null;
                if (photos != null) {
                    final Optional<Photo> photo = photos.stream()
                            .filter(p -> p.getSequenceId().equals(sequenceId) && p.getSequenceIndex().equals(sequenceIndex))
                            .findFirst();
                    result = photo.isPresent() ? photo.get() : null;
                }
                return result;
    }

    /**
     * Checks if the selected photo is the first photo of the sequence.
     *
     * @return true/false
     */
    public boolean enablePreviousPhotoAction() {
        return selectedSequence != null && selectedPhoto != null && selectedSequence.hasPhotos()
                && !selectedSequence.getPhotos().get(0).getSequenceIndex().equals(selectedPhoto.getSequenceIndex());
    }

    /**
     * Checks if the selected photo is the last photo of the sequence.
     *
     * @return true/false
     */
    public boolean enableNextPhotoAction() {
        return selectedSequence != null && selectedPhoto != null && selectedSequence.hasPhotos()
                && !selectedSequence.getPhotos().get(selectedSequence.getPhotos().size() - 1).getSequenceIndex()
                .equals(selectedPhoto.getSequenceIndex());
    }

    /**
     * Returns the selected sequence last photo.
     *
     * @return a {@code Photo}
     */
    public Photo selectedSequenceLastPhoto() {
        final int index = selectedSequence.getPhotos().size();
        return selectedSequence.getPhotos().get(index - 1);
    }

    public void removeDetection(final Detection detection) {
        if (hasDetections()) {
            detections.remove(detection);
        }
    }

    /**
     * Sets the selected photo.
     *
     * @param selectedPhoto a {@code Photo}
     */
    public void setSelectedPhoto(final Photo selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    /**
     * Sets the selected detection.
     *
     * @param selectedDetection a {@code Detection}
     */
    public void setSelectedDetection(final Detection selectedDetection) {
        this.selectedDetection = selectedDetection;
    }

    public void setSelectedCluster(final Cluster selectedCluster) {
        this.selectedCluster = selectedCluster;
    }

    /**
     * Updates the selected detection with a newer version of the detection. The method removes the old
     * detection from the data store (selected sequence and detections list) and adds the new version.
     * If the new detection is null then the selected detection will be removed.
     *
     * @param detection a {@code Detection} object
     */
    public synchronized void updateSelectedDetection(final Detection detection) {
        final Detection oldDetection = getSelectedDetection();
        setSelectedDetection(detection);
        if (hasSelectedSequence()) {
            selectedSequence.getDetections().remove(oldDetection);
            if (detection != null) {
                selectedSequence.getDetections().add(detection);
            }
        }
        if (hasDetections()) {
            detections.remove(oldDetection);
            if (detection != null) {
                detections.add(detection);
            }
        }
    }

    /**
     * Sets the selected sequence.
     *
     * @param selectedSequence a {@code Sequence}
     */
    public void setSelectedSequence(final Sequence selectedSequence) {
        this.selectedSequence = selectedSequence;
    }

    /**
     * Returns the list of segments.
     *
     * @return a list of {@code Segment}s
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Returns the photo data set.
     *
     * @return a {@code PhotoDataSet}
     */
    public PhotoDataSet getPhotoDataSet() {
        return photoDataSet;
    }

    /**
     * Returns the list of detections.
     *
     * @return a list of {@code Detection}
     */
    public List<Detection> getDetections() {
        return detections;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    /**
     * Returns the selected photo.
     *
     * @return a {@code Photo}
     */
    public Photo getSelectedPhoto() {
        return selectedPhoto;
    }

    /**
     * Returns the selected detection.
     *
     * @return a {@code Detection}
     */
    public Detection getSelectedDetection() {
        return selectedDetection;
    }

    public Cluster getSelectedCluster() {
        return selectedCluster;
    }

    /**
     * Returns the selected sequence.
     *
     * @return a {@code Sequence}
     */
    public Sequence getSelectedSequence() {
        return selectedSequence;
    }

    /**
     * Returns the list of nearby photos.
     *
     * @return a list of {@code Photo}s
     */
    public Collection<Photo> getNearbyPhotos() {
        return nearbyPhotos;
    }

    /**
     * Verifies if the data set has items or not.
     *
     * @return boolean
     */
    public boolean hasItems() {
        return hasSegments() || hasDetections() || hasPhotos() || hasClusters();
    }

    /**
     * Verifies if the data-set has photos or not.
     *
     * @return boolean
     */
    public boolean hasPhotos() {
        return photoDataSet != null && photoDataSet.hasItems();
    }

    /**
     * Verifies if the data-set has segments or not.
     *
     * @return boolean
     */
    public boolean hasSegments() {
        return segments != null && !segments.isEmpty();
    }

    /**
     * Verifies if the data-set has detections or not.
     *
     * @return boolean
     */
    public boolean hasDetections() {
        return detections != null && !detections.isEmpty();
    }

    public boolean hasClusters() {
        return clusters != null && !clusters.isEmpty();
    }

    /**
     * Checks is the data-set has nearby photos or not.
     *
     * @return boolean
     */
    public boolean hasNearbyPhotos() {
        return nearbyPhotos != null && !nearbyPhotos.isEmpty();
    }

    /**
     * Checks if the data-set has a selected sequence or not.
     *
     * @return boolean
     */
    public boolean hasSelectedSequence() {
        return selectedSequence != null;
    }

    /**
     * Checks if the data-set has selected photo or not.
     *
     * @return boolean
     */
    public boolean hasSelectedPhoto() {
        return selectedPhoto != null;
    }

    /**
     * Checks if the data set has detection selected or not.
     *
     * @return boolean
     */
    public boolean hasSelectedDetection() {
        return selectedDetection != null;
    }
}