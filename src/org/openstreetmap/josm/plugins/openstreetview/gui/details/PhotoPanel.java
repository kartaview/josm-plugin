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
package org.openstreetmap.josm.plugins.openstreetview.gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.ServiceConfig;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Displays the selected image.
 *
 * @author Beata
 * @version $Revision$
 */
class PhotoPanel extends JPanel {

    private static final long serialVersionUID = -1550900781158007580L;

    /** the current image */
    private BufferedImage image;

    /** a rectangle where the image is incorporated related to the outside panel */
    private Rectangle frame;

    /** a part of the image that is currently displayed */
    private Rectangle currentView;

    PhotoPanel() {
        super(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        this.addMouseWheelListener(new ImageMouseListener());
    }

    private class Pair {

        int first, second;
    }

    private class ImageMouseListener implements MouseWheelListener {

        @Override
        public synchronized void mouseWheelMoved(final MouseWheelEvent e) {
            // get the mouse position on the image (avoid the empty space around image)
            final int xMouseCoord = currentView.x + (e.getX() - frame.x) * currentView.width / frame.width;
            final int yMouseCoord = currentView.y + (e.getY() - frame.y) * currentView.height / frame.height;
            zoom(xMouseCoord, yMouseCoord, e.getWheelRotation());
            PhotoPanel.this.repaint();

        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (image != null) {
            // clean the panel
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            // draw the image
            matchDisplayAreaToOutsideComponent(g);
        }
    }

    void updateUI(final String photoName) {
        removeAll();
        if (photoName != null) {
            final StringBuilder link = new StringBuilder(ServiceConfig.getInstance().getBaseUrl());
            link.append(photoName);
            try {
                image = ImageIO.read(new URL(link.toString()));
                currentView = new Rectangle(0, 0, image.getWidth(), image.getHeight());
            } catch (final IOException e) {
                add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPhotoErrorTxt(),
                        getFont().deriveFont(Font.BOLD, 12), Color.white), BorderLayout.CENTER);
            }
        } else {
            image = null;
        }
    }

    /** The method match the current view of the picture in the panel. */
    private void matchDisplayAreaToOutsideComponent(final Graphics g) {
        int imageWidth = this.getWidth();
        int imageHeight = (this.getWidth() * currentView.height) / currentView.width;
        int marginLeft = 0;
        int marginTop = (this.getHeight() - imageHeight) / 2;

        if (imageHeight > this.getHeight()) {
            imageHeight = this.getHeight();
            imageWidth = (this.getHeight() * currentView.width) / currentView.height;
            marginLeft = (this.getWidth() - imageWidth) / 2;
            marginTop = 0;
        }
        frame = new Rectangle(marginLeft, marginTop, imageWidth, imageHeight);
        g.drawImage(image, marginLeft, marginTop, marginLeft + imageWidth, marginTop + imageHeight, currentView.x,
                currentView.y, currentView.x + currentView.width, currentView.y + currentView.height, null);
    }

    private void zoom(final int xMouseCoord, final int yMouseCoord, final int wheelRotation) {
        final int frameWidth = this.getWidth();
        final int frameHeight = this.getHeight();

        // get a part of the image, considering xMouseCoord and yMouseCoord

        // if the outside panel has a landscape format
        if (frameWidth > frameHeight) {
            Pair vertical;
            if (wheelRotation < 0) {
                // then the new image will have as height, 4/5 of the current view
                vertical = getPart(yMouseCoord, currentView.y, currentView.y + currentView.height,
                        (currentView.height * 4 / 5) / 2);
            } else {
                vertical = getCurrentViewNewFixedDimension(yMouseCoord, currentView.y, currentView.height,
                        image.getHeight());
            }

            // and as width, as much as possible from the current displayed image
            int newWidth = (vertical.second - vertical.first) * frameWidth / frameHeight;
            final Pair horizontal;
            if (wheelRotation < 0) {  // zoom in
                if (newWidth > currentView.width) {
                    newWidth = currentView.width;
                }
                horizontal = getPart(xMouseCoord, currentView.x, currentView.x + currentView.width, newWidth / 2);
            } else {                  // zoom out
                horizontal = getCurrentViewNewRelativeDimension(xMouseCoord, currentView.x, currentView.width, newWidth,
                        image.getWidth());
            }

            currentView = new Rectangle(horizontal.first, vertical.first, horizontal.second - horizontal.first,
                    vertical.second - vertical.first);
        } else {
            Pair horizontal;
            if (wheelRotation < 0) {
                // then the new image will have as width, 4/5 of the current view
                horizontal = getPart(xMouseCoord, currentView.x, currentView.x + currentView.width,
                        (currentView.width * 4 / 5) / 2);
            } else {
                horizontal = getCurrentViewNewFixedDimension(xMouseCoord, currentView.x, currentView.width,
                        image.getWidth());
            }

            // and as height, as much as possible from the current displayed image
            int newHeight = (horizontal.second - horizontal.first) * frameHeight / frameWidth;
            final Pair vertical;
            if (wheelRotation < 0) {  // zoom in
                if (newHeight > currentView.height) {
                    newHeight = currentView.height;
                }
                vertical = getPart(yMouseCoord, currentView.y, currentView.y + currentView.height, newHeight / 2);
            } else {                  // zoom out
                vertical = getCurrentViewNewRelativeDimension(yMouseCoord, currentView.y, currentView.height, newHeight,
                        image.getHeight());
            }

            currentView = new Rectangle(horizontal.first, vertical.first, horizontal.second - horizontal.first,
                    vertical.second - vertical.first);
        }
    }

    private Pair getPart(final int mouseCoord, final int firstReference, final int secondReference,
            final int croppedSize) {

        final Pair result = new Pair();

        final int firstPartMargin = mouseCoord - croppedSize;
        final int secondPartMargin = mouseCoord + croppedSize;

        result.first = firstPartMargin;
        result.second = secondPartMargin;

        if (firstPartMargin < firstReference) {
            result.first = firstReference;
            result.second = result.second - firstPartMargin + firstReference;
        }

        if (secondPartMargin > secondReference) {
            result.first = result.first - secondPartMargin + secondReference;
            result.second = secondReference;
        }

        if (result.first < firstReference) {
            result.first = firstReference;
        }
        if (result.second > secondReference) {
            result.second = secondReference;
        }
        return result;
    }

    Pair getCurrentViewNewFixedDimension(final int mouseCoord, final int currentViewMinCoord,
            final int currentViewDimension, final int imageDimension) {
        final Pair pair;
        if (currentViewDimension != imageDimension) {
            final int dif = (currentViewDimension * 5 / 4) / 2;
            int firstReference = currentViewMinCoord - dif;
            int secondReference = currentViewMinCoord + currentViewDimension + dif;
            if (firstReference < 0) {
                firstReference = 0;
            }
            if (secondReference > imageDimension) {
                secondReference = imageDimension;
            }
            pair = getPart(mouseCoord, firstReference, secondReference, dif);
        } else {
            pair = new Pair();
            pair.first = 0;
            pair.second = imageDimension;
        }
        return pair;
    }

    Pair getCurrentViewNewRelativeDimension(final int mouseCoord, final int currentViewMinCoord,
            final int currentViewDimension, int newDimension, final int imageDimension) {
        final Pair pair;
        if (currentViewDimension != imageDimension) {
            if (newDimension > imageDimension) {
                newDimension = imageDimension;
            }
            int firstReference = currentViewMinCoord - (newDimension / 2 - currentViewDimension / 2);
            int secondReference =
                    currentViewMinCoord + currentViewDimension + (newDimension / 2 - currentViewDimension / 2);
            if (firstReference < 0) {
                firstReference = 0;
            }
            if (secondReference > imageDimension) {
                secondReference = imageDimension;
            }
            pair = getPart(mouseCoord, firstReference, secondReference, newDimension / 2);
        } else {
            pair = new Pair();
            pair.first = 0;
            pair.second = imageDimension;
        }
        return pair;
    }
}