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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.List;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.IconConfig;


/**
 * Defines custom drawing methods.
 *
 * @author Beata
 * @version $Revision$
 */
class PaintHandler {

    /**
     * Draws a list of photos to the map. A photo is represented by an icon on the map.
     *
     * @param graphics a {@code Graphics2D} used to draw elements to the map
     * @param mapView a {@code MapView} represents the current map view
     * @param photos a list of {@code Photo}s
     * @param selectedPhoto the currently selected {@code Photo}
     */
    void drawPhotos(final Graphics2D graphics, final MapView mapView, final List<Photo> photos,
            final Photo selectedPhoto) {
        for (final Photo photo : photos) {
            if (!photo.equals(selectedPhoto)) {
                final Point point = mapView.getPoint(photo.getLocation());
                if (mapView.contains(point)) {
                    drawIcon(graphics, IconConfig.getInstance().getPhotoIcon(), point);
                }
            }
        }
        if (selectedPhoto != null) {
            final Point point = mapView.getPoint(selectedPhoto.getLocation());
            if (mapView.contains(point)) {
                drawIcon(graphics, IconConfig.getInstance().getPhotoSelectedIcon(), point);
            }
        }
    }

    private static void drawIcon(final Graphics2D g2D, final ImageIcon icon, final Point p) {
        g2D.drawImage(icon.getImage(), p.x - (icon.getIconWidth() / 2), p.y - (icon.getIconHeight() / 2),
                new ImageObserver() {

            @Override
            public boolean imageUpdate(final Image img, final int infoflags, final int x, final int y,
                    final int width, final int height) {
                return false;
            }
        });
    }
}