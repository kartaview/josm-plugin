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
    private Rectangle rectangle;

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
            final int xMouseCoord = currentView.x + (e.getX() - rectangle.x) * currentView.width / rectangle.width;
            final int yMouseCoord = currentView.y + (e.getY() - rectangle.y) * currentView.height / rectangle.height;
            zoom(xMouseCoord, yMouseCoord, e.getWheelRotation());
            PhotoPanel.this.repaint();

        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        // if (current != currentImageView.getWidth() || buttom - top !=
        // currentImageView.getHeight()) {
        // currentView = currentImageView.getSubimage(left, top, right - left,
        // buttom - top);
        // }
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
        }
    }

    /** The method match the current view of the picture in the panel. */
    private void matchDisplayAreaToOutsideComponent(final Graphics g) {
        final int frameWidth = this.getWidth();
        final int frameHeight = this.getHeight();
        int imageWidth = frameWidth;
        int imageHeight = (frameWidth * currentView.height) / currentView.width;
        int marginLeft = 0;
        int marginTop = (frameHeight - imageHeight) / 2;

        if (imageHeight > frameHeight) {
            imageHeight = frameHeight;
            imageWidth = (frameHeight * currentView.width) / currentView.height;
            marginLeft = (frameWidth - imageWidth) / 2;
            marginTop = 0;
        }
        rectangle = new Rectangle(marginLeft, marginTop, imageWidth, imageHeight);
        g.drawImage(image, marginLeft, marginTop, marginLeft + imageWidth, marginTop + imageHeight, currentView.x,
                currentView.y, currentView.x + currentView.width, currentView.y + currentView.height, null);
    }

    private void zoom(final int xMouseCoord, final int yMouseCoord, final int wheelRotation) {
        final int frameWidth = this.getWidth();
        final int frameHeight = this.getHeight();

        // get a part of the image, considering xMouseCoord and yMouseCoord

        // if the outside panel has a landscape format
        if (frameWidth > frameHeight) {
            // then the new image will have as height, 4/5 from the current displayed image
            Pair vertical;

            if (wheelRotation < 0) {
                vertical = getPart(yMouseCoord, currentView.y, currentView.y + currentView.height,
                        (currentView.height * 4 / 5) / 2);
            } else {
                if (currentView.getHeight() != image.getHeight()) {
                    final int dif = (currentView.height * 5 / 4) / 2;
                    int firstReference = currentView.y - dif;
                    int secondReference = currentView.y + currentView.height + dif;
                    if (firstReference < 0) {
                        firstReference = 0;
                    }
                    if (secondReference > image.getHeight()) {
                        secondReference = image.getHeight();
                    }
                    vertical = getPart(yMouseCoord, firstReference, secondReference, dif);
                } else {
                    vertical = new Pair();
                    vertical.first = 0;
                    vertical.second = image.getHeight();
                }
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
                if (currentView.getWidth() != image.getWidth()) {
                    if (newWidth > image.getWidth()) {
                        newWidth = image.getWidth();
                    }
                    int firstReference = currentView.x - (newWidth / 2 - currentView.width / 2);
                    int secondReference = currentView.x + currentView.width + (newWidth / 2 - currentView.width / 2);
                    if (firstReference < 0) {
                        firstReference = 0;
                    }
                    if (secondReference > image.getWidth()) {
                        secondReference = image.getWidth();
                    }
                    horizontal = getPart(xMouseCoord, firstReference, secondReference, newWidth / 2);
                } else {
                    horizontal = new Pair();
                    horizontal.first = 0;
                    horizontal.second = image.getWidth();
                }
            }

            // if (vertical.second - vertical.first != image.getHeight()
            // || (horizontal.second - horizontal.first != image.getWidth())) {
            currentView = new Rectangle(horizontal.first, vertical.first, horizontal.second - horizontal.first,
                    vertical.second - vertical.first);
        } else {
            /*
             * final Pair rightLeftResult = getPart(xMouseCoord, 0, currentImageView.getWidth(),
             * (currentImageView.getWidth() * 4 / 5) / 2); left = rightLeftResult.first; right = rightLeftResult.second;
             * int newHeight = (right - left) * frameHeight / frameWidth; if (newHeight > image.getHeight()) { newHeight
             * = image.getHeight(); } final Pair topButtomResult = getPart(yMouseCoord, 0, currentImageView.getHeight(),
             * newHeight / 2); top = topButtomResult.first; buttom = topButtomResult.second;
             */
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

        return result;
    }
}