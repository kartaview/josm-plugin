/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import static org.openstreetmap.josm.plugins.openstreetcam.gui.layer.Constants.RENDERING_MAP;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sequence;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;


/**
 * Defines the OpenStreetCam layer functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetCamLayer extends AbtractLayer {

    private final PaintHandler paintHandler = new PaintHandler();
    private List<Photo> photos;
    private Photo selectedPhoto;
    private Sequence selectedSequence;


    @Override
    public void paint(final Graphics2D graphics, final MapView mapView, final Bounds bounds) {
        mapView.setDoubleBuffered(true);
        graphics.setRenderingHints(RENDERING_MAP);
        if (photos != null) {
            final Composite originalComposite = graphics.getComposite();
            final Stroke originalStorke = graphics.getStroke();
            paintHandler.drawPhotos(graphics, mapView, photos, selectedPhoto, selectedSequence);
            graphics.setComposite(originalComposite);
            graphics.setStroke(originalStorke);
        }
    }

    /**
     * Sets the currently displayed photo list.
     *
     * @param photos a list of {@code Photo}s
     * @param checkSelectedPhoto is true, verifies if the selected photo is present or not in the given photo list. The
     * selected photo is set to null, if it is not present in the given list.
     */
    public void setPhotos(final List<Photo> photos, final boolean checkSelectedPhoto) {
        this.photos = photos;
        if ((checkSelectedPhoto && this.selectedPhoto != null)
                && (this.photos == null || !this.photos.contains(selectedPhoto))) {
            this.selectedPhoto = null;
        }
    }

    /**
     * Returns the photo that is located near to the given point. The method returns null if there is no nearby item.
     *
     * @param point a {@code Point} represents location where the user had clicked
     * @return a {@code Photo}
     */
    public Photo nearbyPhoto(final Point point) {
        Photo photo = (selectedSequence != null && selectedSequence.getPhotos() != null)
                ? Util.nearbyPhoto(selectedSequence.getPhotos(), point) : null;
                photo = photo == null && photos != null ? Util.nearbyPhoto(photos, point) : photo;
                return photo;
    }

    /**
     * Returns the photos that are either previous/next or close to the selected photo.
     *
     * @param prevNextCount the number of previous/next photos to be returned
     * @param nearbyCount the number of nearby photos to be returned
     * @return a sey of {@code Photo}s
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
            result.addAll(Util.nearbyPhotos(photos, selectedPhoto, nearbyCount));
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
        if (selectedSequence != null && (selectedSequence.getPhotos() != null)) {
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
        if (selectedSequence != null) {
            for (final Photo elem : selectedSequence.getPhotos()) {
                if (elem.getSequenceIndex().equals(index)) {
                    photo = elem;
                    // API issue: does not return username for sequence photos
                    photo.setUsername(selectedPhoto.getUsername());
                    break;
                }
            }
        } else if (photos != null) {
            for (final Photo elem : photos) {
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

    /**
     * Returns the currently selected photo.
     *
     * @return a {@code Photo}
     */
    public Photo getSelectedPhoto() {
        return selectedPhoto;
    }

    /**
     * Returns the currently selected sequence.
     *
     * @return a {@code Sequence}
     */
    public Sequence getSelectedSequence() {
        return selectedSequence;
    }

    /**
     * Sets the currently selected photo.
     *
     * @param selectedPhoto a {@code Photo} a selected photo
     */
    public void setSelectedPhoto(final Photo selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    /**
     * Sets the currently selected sequence.
     *
     * @param selectedSequence a {@code Sequence}
     */
    public void setSelectedSequence(final Sequence selectedSequence) {
        this.selectedSequence = selectedSequence;
    }
}