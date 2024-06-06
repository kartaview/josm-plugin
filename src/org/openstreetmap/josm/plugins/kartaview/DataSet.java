/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.data.osm.SimplePrimitiveId;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.kartaview.argument.CacheSettings;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmElement;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmElementType;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.entity.PhotoDataSet;
import org.openstreetmap.josm.plugins.kartaview.entity.Segment;
import org.openstreetmap.josm.plugins.kartaview.entity.Sequence;
import org.openstreetmap.josm.plugins.kartaview.handler.PhotoHandler;
import org.openstreetmap.josm.plugins.kartaview.handler.imagery.OsmDataHandler;
import org.openstreetmap.josm.plugins.kartaview.util.Util;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import com.grab.josm.common.thread.ThreadPool;


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
    private List<EdgeDetection> edgeDetections = new ArrayList<>();
    private List<Cluster> clusters = new ArrayList<>();
    private List<Cluster> edgeClusters = new ArrayList<>();
    private Photo selectedPhoto;
    private Detection selectedDetection;
    private EdgeDetection selectedEdgeDetection;
    private Cluster selectedCluster;
    private Cluster selectedEdgeCluster;
    private Sequence selectedSequence;
    private Bounds selectedSequenceDetectionBounds;
    private Photo nearyPhotosStartPhoto;
    private Collection<Photo> nearbyPhotos;
    private List<OsmElement> matchedData;
    private boolean isRemoteSelection;
    private boolean isFrontFacingDisplayed;
    private boolean isSwitchPhotoFormatAction;

    private DataSet() {
    }

    public static DataSet getInstance() {
        return INSTANCE;
    }

    /**
     * Clears the current data set from the KartaView layer.
     *
     * @param clearSelection if true, the previously selected data is also removed.
     */
    public synchronized void clearKartaViewLayerData(final boolean clearSelection) {
        this.segments = new ArrayList<>();
        this.detections = new ArrayList<>();
        this.photoDataSet = new PhotoDataSet();
        this.clusters = new ArrayList<>();
        this.matchedData = new ArrayList<>();
        if (clearSelection) {
            clearKartaViewLayerSelection();
        }
    }

    /**
     * Clears the current data set from the Edge layer.
     *
     * @param clearSelection if true, the previously selected data is also removed.
     */
    public synchronized void clearEdgeLayerData(final boolean clearSelection) {
        this.edgeDetections = new ArrayList<>();
        this.edgeClusters = new ArrayList<>();
        if (clearSelection) {
            clearEdgeLayerSelection();
        }
    }


    public synchronized void clearKartaViewLayerSelection() {
        this.selectedDetection = null;
        this.selectedPhoto = null;
        this.nearbyPhotos = new ArrayList<>();
        this.nearyPhotosStartPhoto = null;
        this.selectedSequence = null;
        this.selectedSequenceDetectionBounds = null;
        this.selectedCluster = null;
        this.matchedData = null;
        this.isFrontFacingDisplayed = false;
        setRemoteSelection(false);
    }

    public synchronized void clearEdgeLayerSelection() {
        this.selectedEdgeDetection = null;
        this.selectedEdgeCluster = null;
    }

    public synchronized void cleaHighZoomLevelData() {
        this.detections = new ArrayList<>();
        this.photoDataSet = new PhotoDataSet();
        this.clusters = new ArrayList<>();
        this.matchedData = new ArrayList<>();
        clearKartaViewLayerSelection();
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
        if (updateSelection && selectedDetection != null && !selectedDetectionBelongsToSelectedCluster()
                && selectedCluster != null) {
            selectedDetection = detections != null ? detections.stream().filter(detection -> detection.equals(
                    selectedDetection)).findFirst().orElse(null) : null;
        }
    }

    /**
     * Updates the edge detection data with a new list of Edge detections.
     *
     * @param edgeDetections a new list of {@code EdgeDetection}s
     */
    public synchronized void updateHighZoomLevelEdgeDetectionData(final List<EdgeDetection> edgeDetections) {
        this.edgeDetections = edgeDetections;
    }

    public synchronized void updateHighZoomLevelEdgeClusterData(final List<Cluster> edgeClusters) {
        this.edgeClusters = edgeClusters;
    }

    /**
     * Updates the EDGE layer selected data, based on the current EDGE data set.
     */
    public void updateEdgeLayerSelection() {
        //check if selected edge cluster is present in the current data set
        if (Objects.nonNull(selectedEdgeCluster) && (Objects.isNull(edgeClusters) || !edgeClusters.contains(
                selectedEdgeCluster))) {
            setSelectedEdgeCluster(null);
            setSelectedEdgeDetection(null);
        }
        //check if selected edge detection is present in the current data set
        if (Objects.nonNull(selectedEdgeDetection) && !edgeDetectionBelongsToSelectedCluster(selectedEdgeDetection)
                && (Objects.isNull(edgeDetections) || !edgeDetections.contains(selectedEdgeDetection))) {
            setSelectedEdgeDetection(null);
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
     */
    public synchronized void updateHighZoomLevelPhotoData(final PhotoDataSet photoDataSet) {
        if (Objects.isNull(photoDataSet)) {
            this.photoDataSet.removeAllPhotos();
        } else {
            revalidatePhotosToBeDrawn(photoDataSet);
            if (hasSelectedPhoto() && hasNearbyPhotos()) {
                selectNearbyPhotos(getSelectedPhoto());
                if (Config.getInstance().isCacheEnabled()) {
                    ThreadPool.getInstance().execute(() -> {
                        final CacheSettings cacheSettings = PreferenceManager.getInstance().loadCacheSettings();
                        PhotoHandler.getInstance().loadPhotos(nearbyPhotos(cacheSettings.getPrevNextCount(),
                                cacheSettings.getNearbyCount()));
                    });
                }
            }
        }
    }

    /**
     * Eliminates the photos which are not in the active area and adds to the PhotoDataSet the photos which are not
     * represented in the map.
     *
     * @param currentPhotoDataSet - the photos to be added in the map after an action
     */
    private void revalidatePhotosToBeDrawn(final PhotoDataSet currentPhotoDataSet) {
        final PhotoDataSet oldDataSet = this.photoDataSet;
        this.photoDataSet = currentPhotoDataSet;
        final List<Photo> photosToBeDrawn = new ArrayList<>();
        if (oldDataSet != null && oldDataSet.getPhotos() != null) {
            for (final Photo photo : oldDataSet.getPhotos()) {
                if (!isPhotoDrawn(photo, photoDataSet)) {
                    photosToBeDrawn.add(photo);
                }
            }
        }
        this.photoDataSet.addPhotos(photosToBeDrawn);
    }

    private boolean isPhotoDrawn(final Photo photo, final PhotoDataSet previousPhotoDataSet) {
        boolean isDrawn = false;
        if (photo != null && previousPhotoDataSet != null && previousPhotoDataSet.hasItems()) {
            isDrawn = previousPhotoDataSet.getPhotos().contains(photo);
        }
        return isDrawn;
    }

    /**
     * Returns the photo that is located near the given point. The method returns null if there is no nearby item.
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
     * Returns the Edge detection that is nearest to the given point. The method returns null if there is no nearby
     * item.
     *
     * @param point a {@code Point} represents location where the user clicked
     * @return a {@code EdgeDetection}
     */
    public EdgeDetection nearbyEdgeDetection(final Point point) {
        EdgeDetection edgeDetection = null;
        if (selectedEdgeCluster != null && selectedEdgeCluster.hasEdgeDetections()) {
            edgeDetection = Util.nearbyEdgeDetection(selectedEdgeCluster.getEdgeDetections(), point);
        }
        if (edgeDetection == null && edgeDetections != null) {
            edgeDetection = Util.nearbyEdgeDetection(edgeDetections, point);
        }
        return edgeDetection;
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
     * Returns the Edge cluster that is closest to the given point. The method returns null if there is no nearby item.
     *
     * @param point a {@code Point} represents the location on the screen where the user clicked
     * @return a {@code Cluster}
     */
    public Cluster nearbyEdgeCluster(final Point point) {
        return Objects.nonNull(edgeClusters) ? Util.nearbyCluster(edgeClusters, point) : null;
    }

    /**
     * Checks if the given photo belongs to the selected sequence or not.
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
                    if (selectedPhoto != null) {
                        photo.setUsername(selectedPhoto.getUsername());
                    }
                    break;
                }
            }
        } else if (photoDataSet != null && photoDataSet.hasItems() && selectedPhoto != null) {
            for (final Photo elem : photoDataSet.getPhotos()) {
                if (elem.getSequenceIndex().equals(index) && elem.getSequenceId().equals(selectedPhoto
                        .getSequenceId())) {
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
            index = index > selectedCluster.getDetections().size() - 1 ? 0 : index < 0 ? selectedCluster.getDetections()
                    .size() - 1 : index;
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
        return selectedCluster != null ? selectedCluster.getDetections().stream().filter(d -> d.getSequenceId().equals(
                sequenceId) && d.getSequenceIndex().equals(sequenceIndex)).findFirst() : Optional.empty();
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
        Optional<Photo> clusterPhoto = Optional.empty();
        if (cluster != null) {
            final List<Photo> clusterPhotos = cluster.getPhotos() != null ? new ArrayList<>(cluster.getPhotos())
                    : new ArrayList<>();
            clusterPhoto = clusterPhotos.stream().filter(d -> d.getSequenceId().equals(sequenceId) && d
                    .getSequenceIndex().equals(sequenceIndex)).findFirst();
        }
        return clusterPhoto;
    }

    /**
     * Sets a start photo from witch a possible closest image action should start.
     *
     * @param photo a {@code Photo}, representing the photo for which the nearby photos is computed
     */
    public void selectNearbyPhotos(final Photo photo) {
        nearyPhotosStartPhoto = photo;
        if (photo != null && photoDataSet != null && photoDataSet.hasItems()) {
            nearbyPhotos = Util.nearbyPhotos(photoDataSet.getPhotos(), nearyPhotosStartPhoto, Config.getInstance()
                    .getClosestPhotosMaxItems());
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
        // recalculate the closest photos when latest closest photo is returned
        if (nearbyPhotos != null && nearbyPhotos.isEmpty() && nearyPhotosStartPhoto != null) {
            nearbyPhotos = Util.nearbyPhotos(photoDataSet.getPhotos(), nearyPhotosStartPhoto, Config.getInstance()
                    .getClosestPhotosMaxItems());
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
            result = photos.stream().filter(p -> p.getSequenceId().equals(sequenceId) && p.getSequenceIndex().equals(
                    sequenceIndex)).findFirst();
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
                    && selectedIndex != -1 && matchesBboxCriteria(selectedIndex - 1);
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
                    .equals(selectedPhoto.getSequenceIndex()) && selectedIndex != -1 && matchesBboxCriteria(
                            selectedIndex + 1);
        }
        return result;
    }

    /**
     * Method for validating if the photo matches the bbox criteria and the load data preferences.
     *
     * @param selectedIndex index of the photo in the sequence
     * @return boolean true if the photo matches the bbox criteria; false otherwise
     */
    private boolean matchesBboxCriteria(final int selectedIndex) {
        boolean matches = true;
        if (MainApplication.getLayerManager().getActiveLayer() instanceof OsmDataLayer && PreferenceManager
                .getInstance().loadMapViewSettings().isDataLoadFlag() && !Util.isPointInActiveArea(selectedSequence
                        .getPhotos().get(selectedIndex).getPoint())) {
            matches = false;
        }
        return matches;
    }

    /**
     * Returns the last photo of the selected sequence.
     *
     * @return a {@code Photo}
     */
    public Photo selectedSequenceLastPhoto() {
        final Photo lastPhoto;
        if (selectedSequence == null || selectedSequence.getPhotos() == null || selectedSequence.getPhotos()
                .isEmpty()) {
            lastPhoto = null;
        } else {
            final int index = selectedSequence.getPhotos().size();
            lastPhoto = selectedSequence.getPhotos().get(index - 1);
        }
        return lastPhoto;
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

    public void setSelectedPhoto(final Photo selectedPhoto) {
        this.selectedPhoto = selectedPhoto;

        // workaround for the case when the cluster photo is selected and object is not
        // complete
        // (avoiding to load more than once the same photo from KartaView API
        if (selectedPhoto != null && selectedCluster != null && selectedCluster.getPhotos() != null && selectedCluster
                .getPhotos().contains(selectedPhoto)) {
            selectedCluster.getPhotos().remove(selectedPhoto);
            selectedCluster.getPhotos().add(selectedPhoto);
        }
    }

    public void setSelectedDetection(final Detection selectedDetection) {
        this.selectedDetection = selectedDetection;
    }

    public void setSelectedEdgeDetection(final EdgeDetection selectedEdgeDetection) {
        this.selectedEdgeDetection = selectedEdgeDetection;
    }

    public EdgeDetection getSelectedEdgeDetection() {
        return selectedEdgeDetection;
    }

    public void setSelectedCluster(final Cluster selectedCluster) {
        this.selectedCluster = selectedCluster;
    }

    public void setSelectedEdgeCluster(final Cluster selectedEdgeCluster) {
        this.selectedEdgeCluster = selectedEdgeCluster;
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

    public synchronized void updateSelectedSequenceDetections(List<Detection> detections, final Bounds bounds) {
        if (hasSelectedSequence() && (Objects.nonNull(detections))) {
            selectedSequence.addDetections(detections);
            selectedSequenceDetectionBounds.extend(bounds);
        }
    }

    public boolean selectedSequenceHasDetectionsForBounds(final Bounds bounds) {
        return selectedSequenceDetectionBounds.contains(bounds);
    }

    /**
     * Sets the selected sequence within the bounds.
     *
     * @param sequence a {@code Sequence}
     * @param detectionBounds a {@code Bounds}
     */
    public void setSelectedSequence(final Sequence sequence, final Bounds detectionBounds) {
        this.selectedSequence = sequence;
        this.selectedSequenceDetectionBounds = detectionBounds;
    }

    /**
     * Sets the downloaded OSM matched data for the selected detection.
     *
     * @param matchedData - List of downloaded osm elements.
     */
    public void setMatchedData(final List<OsmElement> matchedData) {
        this.matchedData = matchedData;
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

    public List<EdgeDetection> getEdgeDetections() {
        return edgeDetections;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public List<Cluster> getEdgeClusters() {
        return edgeClusters;
    }

    public Photo getSelectedPhoto() {
        return selectedPhoto;
    }

    public Detection getSelectedDetection() {
        return selectedDetection;
    }

    public Cluster getSelectedCluster() {
        return selectedCluster;
    }

    public Cluster getSelectedEdgeCluster() {
        return selectedEdgeCluster;
    }

    public Sequence getSelectedSequence() {
        return selectedSequence;
    }

    public Collection<Photo> getNearbyPhotos() {
        return nearbyPhotos;
    }

    public List<OsmElement> getMatchedData() {
        return matchedData;
    }

    public boolean hasItems() {
        return hasSegments() || hasDetections() || hasPhotos() || hasClusters() || hasEdgeDetections()
                || hasEdgeClusters() || isRemoteSelection;
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

    public boolean hasEdgeDetections() {
        return edgeDetections != null && !edgeDetections.isEmpty();
    }

    public boolean hasClusters() {
        return clusters != null && !clusters.isEmpty();
    }

    public boolean hasEdgeClusters() {
        return Objects.nonNull(edgeClusters) && !edgeClusters.isEmpty();
    }

    public boolean hasNearbyPhotos() {
        return nearbyPhotos != null && !nearbyPhotos.isEmpty();
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

    public boolean hasSelectedEdgeDetection() {
        return Objects.nonNull(selectedEdgeDetection);
    }

    public boolean hasSelectedEdgeCluster() {
        return Objects.nonNull(selectedEdgeCluster);
    }

    public boolean hasSelectedCluster() {
        return selectedCluster != null;
    }

    public boolean hasActiveSelection() {
        return selectedCluster != null || selectedDetection != null || selectedPhoto != null || selectedSequence
                != null;
    }

    public boolean hasMatchedData() {
        return matchedData != null && !matchedData.isEmpty();
    }

    public boolean selectedPhotoBelongsToSelectedCluster() {
        return photoBelongsToSelectedCluster(selectedPhoto);
    }

    private boolean selectedDetectionBelongsToSelectedCluster() {
        return detectionBelongsToSelectedCluster(selectedDetection);
    }

    public boolean detectionBelongsToSelectedCluster(final Detection detection) {
        return selectedCluster != null && detection != null && selectedCluster.getDetections() != null
                && selectedCluster.getDetections().contains(detection);
    }

    public boolean edgeDetectionBelongsToSelectedCluster(final EdgeDetection edgeDetection) {
        return Objects.nonNull(selectedEdgeCluster) && Objects.nonNull(edgeDetection) && Objects.nonNull(
                selectedEdgeCluster.getEdgeDetections()) && selectedEdgeCluster.getEdgeDetections().contains(
                        edgeDetection);
    }

    public boolean photoBelongsToSelectedCluster(final Photo photo) {
        return selectedCluster != null && selectedCluster.getPhotos() != null && selectedCluster.getPhotos().contains(
                photo);
    }

    public boolean selectedDetectionHasOsmElements() {
        return selectedDetection != null && selectedDetection.getOsmElements() != null && !selectedDetection
                .getOsmElements().isEmpty();
    }

    public boolean selectedDetectionHasValidOsmElements() {
        boolean validOsmElement = false;
        if (selectedClusterHasOsmElements() && selectedDetection != null) {
            validOsmElement = hasValidOsmElements(selectedDetection.getOsmElements());
        }
        return validOsmElement;
    }

    public boolean selectedClusterHasOsmElements() {
        return selectedCluster != null && selectedCluster.getOsmElements() != null && !selectedCluster.getOsmElements()
                .isEmpty();
    }

    public boolean selectedClusterHasValidOsmElements() {
        boolean validOsmElement = false;
        if (selectedClusterHasOsmElements()) {
            validOsmElement = hasValidOsmElements(selectedCluster.getOsmElements());
        }
        return validOsmElement;
    }

    private boolean hasValidOsmElements(final Collection<OsmElement> osmElements) {
        boolean isValid = false;
        final Optional<org.openstreetmap.josm.data.osm.DataSet> result = OsmDataHandler.retrieveServerObjects(
                osmElements);
        if (result.isPresent()) {
            isValid = true;
            for (final OsmElement osmElement : osmElements) {
                final OsmElementType element = osmElement.getType();
                if (element.equals(OsmElementType.WAY_SECTION)) {
                    final Way downloadedWay = (Way) result.get().getPrimitiveById(new SimplePrimitiveId(osmElement
                            .getOsmId(), OsmPrimitiveType.WAY));
                    if (downloadedWay != null && downloadedWay.getNodesCount() <= 0) {
                        isValid = false;
                    }
                }
            }
        }
        return isValid;
    }

    public boolean isRemoteSelection() {
        return isRemoteSelection;
    }

    /**
     * Sets the 'isRemoteSelection' flag.
     *
     * @param isRemoteSelection boolean value that indicates if the selected items are selected as a result of a remote
     * item selection.
     */
    public void setRemoteSelection(final boolean isRemoteSelection) {
        this.isRemoteSelection = isRemoteSelection;
    }

    public boolean isFrontFacingDisplayed() {
        return isFrontFacingDisplayed;
    }

    /**
     * Sets the 'isFrontFacingDisplayed' flag.
     *
     * @param frontFacingDisplayed boolean value that indicates if the front facing version of the image is displayed
     * in the photo panel.
     */
    public void setFrontFacingDisplayed(final boolean frontFacingDisplayed) {
        isFrontFacingDisplayed = frontFacingDisplayed;
    }

    public boolean isSwitchPhotoFormatAction() {
        return isSwitchPhotoFormatAction;
    }

    /**
     * Sets the 'isSwitchPhotoFormatAction' flag.
     *
     * @param switchPhotoFormatAction boolean value that indicates if the switch photo format action triggered an
     * update in the panel.
     */
    public void setSwitchPhotoFormatAction(final boolean switchPhotoFormatAction) {
        isSwitchPhotoFormatAction = switchPhotoFormatAction;
    }
}