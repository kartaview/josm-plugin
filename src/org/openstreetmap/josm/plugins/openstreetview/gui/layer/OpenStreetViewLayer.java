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
package org.openstreetmap.josm.plugins.openstreetview.gui.layer;

import static org.openstreetmap.josm.plugins.openstreetview.gui.layer.Constants.RENDERING_MAP;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetview.util.Util;


/**
 * Defines the OpenStreetView layer functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetViewLayer extends AbtractLayer {

    private final PaintHandler paintHandler = new PaintHandler();
    private List<Photo> photos;
    private Photo selectedPhoto;


    @Override
    public void paint(final Graphics2D graphics, final MapView mapView, final Bounds bounds) {
        mapView.setDoubleBuffered(true);
        graphics.setRenderingHints(RENDERING_MAP);
        if (photos != null) {
            paintHandler.drawPhotos(graphics, mapView, photos, selectedPhoto);
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
     * @param point a {@code Point} represents the location where the user had clicked
     * @return a {@code Photo}
     */
    public Photo nearbyPhoto(final Point point) {
        return photos != null ? Util.nearbyPhoto(photos, point) : null;
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
     * Sets the currently selected photo.
     *
     * @param selectedPhoto a {@code Photo} a selected photo
     */
    public void setSelectedPhoto(final Photo selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }
}