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

import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.data.osm.SimplePrimitiveId;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.plugins.openstreetcam.argument.CacheSettings;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Segment;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmElement;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmElementType;
import org.openstreetmap.josm.plugins.openstreetcam.handler.OsmDataHandler;
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

    /** the segments from the current map view; available only for small zoom levels */
    private List<Segment> segments = new ArrayList<>();

    /** the photos from the current map view; available only for high zoom levels */
    private PhotoDataSet photoDataSet = new PhotoDataSet();

    /** the detections from the current map view; available only for high zoom levels */
    private List<Detection> detections = new ArrayList<>();

    /** the clusters from the current map view; available only for high zoom levels */
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

    /** the currently downloaded OSM matched data */
    private List<OsmElement> matchedData;

    /** true if the currently selected elements are a result of a remote selection action */
    private boolean isRemoteSelection;

    private DataSet() {}

    public static DataSet getInstance() {
        return INSTANCE;
    }

    /**
     * Clears the current data set.
     *
     * @param clearSelection if true also the previously selected data is removed.
     */
    public synchronized void clear(final boolean clearSelection) {
        this.segments = new ArrayList<>();
        this.detections = new ArrayList<>();
        this.photoDataSet = new PhotoDataSet();
        this.clusters = new ArrayList<>();
        this.matchedData = new ArrayList<>();
        if (clearSelection) {
            clearSelection();
        }
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
        this.matchedData = null;
        setRemoteSelection(false);
    }

    /**
     * Clears the high zoom level data (photo locations and detections) including selected items.
     */
    public synchronized void cleaHighZoomLevelData() {
        this.detections = new ArrayList<>();
        this.photoDataSet = new PhotoDataSet();
        this.clusters = new ArrayList<>();
        this.matchedData = new ArrayList<>();
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
     * @param updateSelection if true - then the currently selected detection is removed if not present in the new list
     * of data
     */
    public synchronized void updateHighZoomLevelDetectionData(final List<Detection> detections,
            final boolean updateSelection) {
        this.detections = detections;
        if (updateSelection && selectedDetection != null && !selectedDetectionBelongsToSelectedCluster()) {
            selectedDetection = detections != null ? detections.stream()
                    .filter(detection -> detection.equals(selectedDetection)).findFirst().orElse(null) : null;
        }
    }

    /**
     * Updates the cluster data with a new list of clusters.
     *
     * @param clusters a list of {@code Cluster}s
     * @param updateSelection if true - then the currently selected cluster is removed if not present in the new list of
     * data
     */
    public synchronized void updateHighZoomLevelClusterData(final List<Cluster> clusters,
            final boolean updateSelection) {
        this.clusters = clusters;
        if (updateSelection && selectedCluster != null) {
            selectedCluster = (clusters == null || !clusters.contains(selectedCluster)) ? null : selectedCluster;
        }
    }

    /**
     * Updates the photo location data with a new list of photos.
     *
     * @param photoDataSet a {@code PhotoDataSet} containing a new list of {@code Photo}s
     * @param updateSelection if true - then the currently selected photo is removed if not present in the new data set
     */
    public synchronized void updateHighZoomLevelPhotoData(final PhotoDataSet photoDataSet,
            final boolean updateSelection) {
        this.photoDataSet = photoDataSet;
        if (updateSelection && hasSelectedPhoto() && !selectedPhotoBelongsToSelectedCluster()) {
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
     * Returns the photo that is located near to the given point. The method returns null if there is no nearby item.
     *
     * @param point a {@code Point} represents location where the user had clicked
     * @return a {@code Photo}
     */
    public Photo nearbyPhoto(final Point point) {
        Photo photo = null;
        if (selectedCluster != null && selectedCluster.hasPhotos()) {
            photo = Util.nearbyPhoto(selectedCluster.getPhotos(), point);
        }
        if (photo == null && selectedSequence != null && selectedSequence.hasPhotos()) {
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
     * Returns the photos that are either previous/next or close to the selected photo.
     *
     * @param prevNextCount the number of previous/next photos to be returned
     * @param nearbyCount the number of nearby photos to be returned
     * @return a set of {@code Photo}s
     */
    public synchronized Set<Photo> nearbyPhotos(final int prevNextCount, final int nearbyCount) {
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
     * Returns the detection that is located near to the given point. The method returns null if there is no nearby
     * item.
     *
     * @param point a {@code Point} represents location where the user had clicked
     * @return a {@code Detection}
     */
    public Detection nearbyDetection(final Point point) {
        Detection detection = null;
        if (selectedCluster != null && selectedCluster.hasDetections()) {
            detection = Util.nearbyDetection(selectedCluster.getDetections(), point);
        }
        if (detection != null && selectedSequence != null && selectedSequence.hasDetections()) {
            detection = Util.nearbyDetection(selectedSequence.getDetections(), point);
        }
        if (detection == null && detections != null) {
            detection = Util.nearbyDetection(detections, point);
        }
        return detection;
    }

    /**
     * Returns the cluster that is located near to the given point. The method returns null if there is no nearby item.
     *
     * @param point a {@code Point} represents the location on the screen where the user had clicked
     * @return a {@code Cluster}
     */
    public Cluster nearbyCluster(final Point point) {
        return clusters != null ? Util.nearbyCluster(clusters, point) : null;
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
    public synchronized Photo sequencePhoto(final int index) {
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
     * Returns the next or previous detection belonging to the selected cluster that is relative to the currently
     * selected cluster detection.
     *
     * @param isNext specifies if the next or previous detection is returned
     * @return a {@code Detection} object
     */
    public Detection clusterDetection(final boolean isNext) {
        Detection detection = null;
        if (selectedDetection != null) {
            int selectedIndex = 0;
            for (int i = 0; i < selectedCluster.getDetections().size(); i++) {
                if (selectedDetection.equals(selectedCluster.getDetections().get(i))) {
                    selectedIndex = i;
                    break;
                }
            }
            int index = isNext ? ++selectedIndex : --selectedIndex;
            index = index > selectedCluster.getDetections().size() - 1 ? 0
                    : index < 0 ? selectedCluster.getDetections().size() - 1 : index;
                    detection = selectedCluster.getDetections().get(index);
        }
        return detection;
    }

    /**
     * Returns the detection of a cluster associated with the given sequence id and sequence index.
     *
     * @param sequenceId the identifier of the sequence
     * @param sequenceIndex the index of the photo from the sequence to which the detection belongs
     * @return an {@code Optional} containing the corresponding detection, if there is no corresponding detection the
     * method returns empty
     */
    public Optional<Detection> selectedClusterDetection(final Long sequenceId, final Integer sequenceIndex) {
        return selectedCluster != null ? selectedCluster.getDetections().stream()
                .filter(d -> d.getSequenceId().equals(sequenceId) && d.getSequenceIndex().equals(sequenceIndex))
                .findFirst() : Optional.empty();
    }

    /**
     * Returns the photo of the selected cluster associated with the given sequence identifier and index.
     *
     * @param sequenceId the identifier of the sequence
     * @param sequenceIndex the index of the photo from the sequence to which the detection belongs
     * @return an {@code Optional} containing the corresponding photo, if there is no corresponding photo the method
     * returns empty
     */
    public Optional<Photo> selectedClusterPhoto(final Long sequenceId, final Integer sequenceIndex) {
        return clusterPhoto(selectedCluster, sequenceId, sequenceIndex);
    }

    /**
     * Returns the photo of the cluster associated with the sequence identifier and index.
     *
     * @param cluster a {@code Cluster} object
     * @param sequenceId the identifier of the sequence
     * @param sequenceIndex the index of the photo from the sequence to which the detection belongs
     * @return an {@code Optional} containing the corresponding photo, if there is no corresponding photo the method
     * returns empty
     */
    public Optional<Photo> clusterPhoto(final Cluster cluster, final Long sequenceId, final Integer sequenceIndex) {
        return cluster != null && cluster.getPhotos() != null ? cluster.getPhotos().stream()
                .filter(d -> d.getSequenceId().equals(sequenceId) && d.getSequenceIndex().equals(sequenceIndex))
                .findFirst() : Optional.empty();
    }

    /**
     * Sets a start photo from witch a possible closest image action should start.
     *
     * @param photo a {@code Photo}, representing the photo for which the nearby photos is computed
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
        if (nearbyPhotos != null && !nearbyPhotos.isEmpty()) {
            result = nearbyPhotos.iterator().next();
            nearbyPhotos.remove(result);
        }
        // recalculate closest photos when latest closest photo is returned
        if (nearbyPhotos != null && nearbyPhotos.isEmpty() && nearyPhotosStartPhoto != null) {
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
     * @return an {@code Optional} containing the corresponding photo, if there is no corresponding photo the method
     * returns empty
     */
    public Optional<Photo> detectionPhoto(final Long sequenceId, final Integer sequenceIndex) {
        final List<Photo> photos = hasSelectedSequence() && selectedSequence.hasPhotos() ? selectedSequence.getPhotos()
                : hasPhotos() ? photoDataSet.getPhotos() : null;
                Optional<Photo> result = Optional.empty();
                if (photos != null) {
                    result = photos.stream()
                            .filter(p -> p.getSequenceId().equals(sequenceId) && p.getSequenceIndex().equals(sequenceIndex))
                            .findFirst();
                }
                return result;
    }

    /**
     * Checks if the sequence has a previous photo in the active area.
     *
     * @return true/false
     */
    public boolean enablePreviousPhotoAction() {
        boolean result = false;
        if (selectedSequence != null && selectedPhoto != null && selectedSequence.hasPhotos()) {
            final int selectedIndex = selectedSequence.getPhotos().indexOf(selectedPhoto);
            result = !selectedSequence.getPhotos().get(0).getSequenceIndex().equals(selectedPhoto.getSequenceIndex())
                    && selectedIndex != -1
                    && Util.isPointInActiveArea(selectedSequence.getPhotos().get(selectedIndex - 1).getPoint());
        }
        return result;
    }

    /**
     * Checks if the sequence has a next photo in the active area.
     *
     * @return true/false
     */
    public boolean enableNextPhotoAction() {
        boolean result = false;
        if (selectedSequence != null && selectedPhoto != null && selectedSequence.hasPhotos()) {
            final int selectedIndex = selectedSequence.getPhotos().indexOf(selectedPhoto);
            result = !selectedSequence.getPhotos().get(selectedSequence.getPhotos().size() - 1).getSequenceIndex()
                    .equals(selectedPhoto.getSequenceIndex()) && selectedIndex != -1
                    && Util.isPointInActiveArea(selectedSequence.getPhotos().get(selectedIndex + 1).getPoint());
        }
        return result;
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

    /**
     * Removes the specified detection from the list of detections.
     *
     * @param detection a {@code Detection}s to be removed
     */
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

        // workaround for the case when the cluster photo is selected and object is not complete
        // (avoiding to load more than once the same photo from OpenStreetCam API
        if (selectedPhoto != null && selectedCluster != null && selectedCluster.getPhotos() != null
                && selectedCluster.getPhotos().contains(selectedPhoto)) {
            selectedCluster.getPhotos().remove(selectedPhoto);
            selectedCluster.getPhotos().add(selectedPhoto);
        }
    }

    /**
     * Sets the selected detection.
     *
     * @param selectedDetection a {@code Detection}
     */
    public void setSelectedDetection(final Detection selectedDetection) {
        this.selectedDetection = selectedDetection;
    }

    /**
     * Sets the selected cluster.
     *
     * @param selectedCluster a {@code Cluster}
     *
     */
    public void setSelectedCluster(final Cluster selectedCluster) {
        this.selectedCluster = selectedCluster;
    }

    /**
     * Updates the selected detection with a newer version of the detection. The method removes the old detection from
     * the data store (selected sequence and detections list) and adds the new version. If the new detection is null
     * then the selected detection will be removed.
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
     * Sets the downloaded OSM matched data for the selected detection.
     *
     * @param matchedData - List of downloaded osm elements.
     */
    public void setMatchedData(final List<OsmElement> matchedData) {
        this.matchedData = matchedData;
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

    /**
     * Returns the list of clusters.
     *
     * @return a list of {@code Cluster}
     */
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

    /**
     * Returns the selected cluster.
     *
     * @return a {@code Cluster}
     */
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

    public List<OsmElement> getMatchedData() {
        return matchedData;
    }

    /**
     * Verifies if the data set has items or not.
     *
     * @return boolean
     */
    public boolean hasItems() {
        return hasSegments() || hasDetections() || hasPhotos() || hasClusters() || isRemoteSelection;
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

    /**
     * Verifies if the data-set has clusters or not.
     *
     * @return boolean
     */
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

    public boolean hasSelectedCluster() {
        return selectedCluster != null;
    }

    public boolean hasActiveSelection() {
        return selectedCluster != null || selectedDetection != null || selectedPhoto != null
                || selectedSequence != null;
    }

    public boolean hasMatchedData() {
        return matchedData != null && !matchedData.isEmpty();
    }

    /**
     * Checks if the currently selected photo belongs or not to the selected cluster.
     *
     * @return boolean
     */
    public boolean selectedPhotoBelongsToSelectedCluster() {
        return photoBelongsToSelectedCluster(selectedPhoto);
    }

    private boolean selectedDetectionBelongsToSelectedCluster() {
        return detectionBelongsToSelectedCluster(selectedDetection);
    }

    /**
     * Checks if the given detection belongs to the selected cluster.
     *
     * @param detection a {@code Detection} object
     * @return boolean
     */
    public boolean detectionBelongsToSelectedCluster(final Detection detection) {
        return selectedCluster != null && detection != null && selectedCluster.getDetections() != null
                && selectedCluster.getDetections().contains(detection);
    }

    /**
     * Checks if the given photo belongs to the selected cluster or not.
     *
     * @param photo a {@code Photo} object
     * @return boolean
     */
    public boolean photoBelongsToSelectedCluster(final Photo photo) {
        return selectedCluster != null && selectedCluster.getPhotos() != null
                && selectedCluster.getPhotos().contains(photo);
    }

    /**
     * Checks if the current selected detection has osm elements.
     *
     * @return {@code boolean}
     */
    public boolean selectedDetectionHasOsmElements() {
        return selectedDetection != null && selectedDetection.getOsmElements() != null &&
                !selectedDetection.getOsmElements().isEmpty();
    }

    public boolean selectedDetectionHasValidOsmElements() {
        boolean validOsmElement = true;
        List<OsmElement> selectedDetectionOsmElements = (List) selectedDetection.getOsmElements();
        if (selectedDetectionOsmElements != null) {
            final Optional<org.openstreetmap.josm.data.osm.DataSet> result =
                    OsmDataHandler.retrieveServerObjects(selectedDetectionOsmElements);
            for (int i = 0; i < selectedDetectionOsmElements.size(); ++i) {
                OsmElementType element = selectedDetectionOsmElements.get(0).getType();
                if (element.equals(OsmElementType.WAY_SECTION)) {
                    final Way downloadedWay = (Way) result.get().getPrimitiveById(
                            new SimplePrimitiveId(selectedDetectionOsmElements.get(0).getOsmId(),
                                    OsmPrimitiveType.WAY));
                    if (downloadedWay.getNodesCount() <= 0) {
                        validOsmElement = false;
                    }
                }
            }
        }
        return selectedDetection != null && selectedDetection.getOsmElements() != null &&
                !selectedDetection.getOsmElements().isEmpty() && validOsmElement;
    }

    /**
     * Checks if the current selected cluster has osm elements.
     *
     * @return {@code boolean}
     */
    public boolean selectedClusterHasOsmElements() {
        return selectedCluster != null && selectedCluster.getOsmElements() != null &&
                !selectedCluster.getOsmElements().isEmpty();
    }

    public boolean selectedClusterHasValidOsmElements() {
        boolean validOsmElement = true;
        List<OsmElement> selectedClusterOsmElements = (List) selectedCluster.getOsmElements();
        if (selectedClusterOsmElements != null) {
            final Optional<org.openstreetmap.josm.data.osm.DataSet> result =
                    OsmDataHandler.retrieveServerObjects(selectedClusterOsmElements);
            for (int i = 0; i < selectedClusterOsmElements.size(); ++i) {
                OsmElementType element = selectedClusterOsmElements.get(0).getType();
                if (element.equals(OsmElementType.WAY_SECTION)) {
                    final Way downloadedWay = (Way) result.get().getPrimitiveById(
                            new SimplePrimitiveId(selectedClusterOsmElements.get(0).getOsmId(), OsmPrimitiveType.WAY));
                    if (downloadedWay.getNodesCount() <= 0) {
                        validOsmElement = false;
                    }
                }
            }
        }
        return selectedCluster != null && selectedCluster.getOsmElements() != null &&
                !selectedCluster.getOsmElements().isEmpty() && validOsmElement;
    }

    /**
     * Returns the 'isRemoteSelection' flag.
     *
     * @return a {@code boolean} value
     */
    public boolean isRemoteSelection() {
        return isRemoteSelection;
    }

    /**
     * Sets the 'isRemoteSelection' flag. This flag indicates if the selected items are selected as a result of a remote
     * item selection.
     *
     * @param isRemoteSelection boolean value
     */
    public void setRemoteSelection(final boolean isRemoteSelection) {
        this.isRemoteSelection = isRemoteSelection;
    }
}