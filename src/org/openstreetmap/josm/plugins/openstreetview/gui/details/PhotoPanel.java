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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
import org.openstreetmap.josm.tools.Pair;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Displays the selected image.
 *
 * @author Beata
 * @version $Revision$
 */
class PhotoPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

    private static final long serialVersionUID = -1550900781158007580L;
    private static final int MAX_ZOOM = 5;

    /** the current image */
    private BufferedImage image;

    /** a rectangle where the image is incorporated related to the outside panel */
    private Rectangle frame;

    /** a part of the image that is currently displayed */
    private Rectangle currentView;

    /** the image coordinate where the mouse dragging was started */
    private Point startPoint;

    PhotoPanel() {
        super(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
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
                add(GuiBuilder.buildLabel(GuiConfig.getInstance().getErrorPhotoLoadingTxt(),
                        getFont().deriveFont(Font.BOLD, 12), Color.white), BorderLayout.CENTER);
            }
        } else {
            image = null;
            currentView = null;
            frame = null;
        }
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (image != null) {
            final Point point = getPointOnImage(e.getPoint());
            zoom(point.x, point.y, e.getWheelRotation());
            repaint();
        }
    }

    private void zoom(final int x, final int y, final int wheelRotation) {
        final Pair<Integer, Integer> vertical;
        final Pair<Integer, Integer> horizontal;

        if (this.getWidth() > this.getHeight()) {
            vertical = getImageFixedDimension(y, currentView.y, currentView.height, image.getHeight(), wheelRotation);

            final int newWidth = (vertical.b - vertical.a) * this.getWidth() / this.getHeight();
            horizontal = getImageRelativeDimension(x, currentView.x, currentView.width, newWidth, image.getWidth(),
                    wheelRotation);
        } else {
            horizontal = getImageFixedDimension(x, currentView.x, currentView.width, image.getWidth(), wheelRotation);

            final int newHeight = (horizontal.b - horizontal.a) * this.getHeight() / this.getWidth();
            vertical = getImageRelativeDimension(y, currentView.y, currentView.height, newHeight, image.getHeight(),
                    wheelRotation);
        }

        if ((horizontal.b - horizontal.a > image.getWidth() / MAX_ZOOM)
                && (vertical.b - vertical.a > image.getHeight() / MAX_ZOOM)) {
            currentView = new Rectangle(horizontal.a, vertical.a, horizontal.b - horizontal.a, vertical.b - vertical.a);
        }
    }

    private Pair<Integer, Integer> getImageFixedDimension(final int ref, final int currentViewMinCoord,
            final int currentViewDim, final int imgDim, final int wheelRotation) {
        Pair<Integer, Integer> pair;
        if (wheelRotation < 0) {   // zoom in case
            pair = getImagePart(ref, currentViewMinCoord, currentViewMinCoord + currentViewDim,
                    (currentViewDim * 4 / 5) / 2);
        } else {
            if (currentViewDim != imgDim) {
                final int dif = (currentViewDim * 5 / 4) / 2;
                int minRef = currentViewMinCoord - dif;
                int maxRef = currentViewMinCoord + currentViewDim + dif;

                minRef = (minRef < 0) ? 0 : minRef;
                maxRef = (maxRef > imgDim) ? imgDim : maxRef;

                pair = getImagePart(ref, minRef, maxRef, dif);
            } else {
                pair = new Pair<>(0, imgDim);
            }
        }

        return pair;
    }

    private Pair<Integer, Integer> getImageRelativeDimension(final int ref, final int currentViewMinCoord,
            final int currentViewDim, final int newDim, final int imgDim, final int wheelRotation) {
        Pair<Integer, Integer> pair;
        int dimension;
        if (wheelRotation < 0) {  // zoom in
            dimension = (newDim > currentViewDim) ? currentViewDim : newDim;
            pair = getImagePart(ref, currentViewMinCoord, currentViewMinCoord + currentViewDim, dimension / 2);
        } else {                  // zoom out
            if (currentViewDim != imgDim) {
                dimension = (newDim > imgDim) ? imgDim : newDim;

                int minRef = currentViewMinCoord - (dimension / 2 - currentViewDim / 2);
                int maxRef = currentViewMinCoord + currentViewDim + (dimension / 2 - currentViewDim / 2);

                minRef = (minRef < 0) ? 0 : minRef;
                maxRef = (maxRef > imgDim) ? imgDim : maxRef;

                pair = getImagePart(ref, minRef, maxRef, dimension / 2);
            } else {
                pair = new Pair<>(0, imgDim);
            }
        }
        return pair;
    }

    private Pair<Integer, Integer> getImagePart(final int ref, final int firstRef, final int secondRef, final int cut) {

        final int minCoord = ref - cut;
        final int maxCoord = ref + cut;

        final Pair<Integer, Integer> result = new Pair<>(minCoord, maxCoord);
        result.a = minCoord;
        result.b = maxCoord;

        if (minCoord < firstRef) {
            result.a = firstRef;
            result.b = result.b - minCoord + firstRef;
        }

        if (maxCoord > secondRef) {
            result.a = result.a - maxCoord + secondRef;
            result.b = secondRef;
        }

        result.a = (result.a < firstRef) ? firstRef : result.a;
        result.b = (result.b > secondRef) ? secondRef : result.b;

        return result;
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        if (image != null) {
            startPoint = getPointOnImage(e.getPoint());
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (image != null) {
            final Point endPoint = getPointOnImage(e.getPoint());
            moveCurrentView(startPoint.x - endPoint.x, startPoint.y - endPoint.y);
            repaint();
        }
    }

    private void moveCurrentView(final int xDif, final int yDif) {
        currentView.x = currentView.x + xDif;
        currentView.y = currentView.y + yDif;

        if (currentView.x < 0) {
            currentView.x = 0;
        }

        if (currentView.y < 0) {
            currentView.y = 0;
        }

        if (currentView.x + currentView.width > image.getWidth()) {
            currentView.x = image.getWidth() - currentView.width;
        }

        if (currentView.y + currentView.height > image.getHeight()) {
            currentView.y = image.getHeight() - currentView.height;
        }
    }

    /** Get the mouse position on the image (avoid the "empty" space around image) */
    private Point getPointOnImage(final Point point) {
        final int x = currentView.x + (point.x - frame.x) * currentView.width / frame.width;
        final int y = currentView.y + (point.y - frame.y) * currentView.height / frame.height;
        return new Point(x, y);
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (image != null) {
            // clean the panel
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            // draw the image
            matchImageOnPanel(g);
        }
    }

    /** The method match the current view of the picture in the panel. */
    private void matchImageOnPanel(final Graphics g) {
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

    @Override
    public void mouseMoved(final MouseEvent e) {}

    @Override
    public void mouseClicked(final MouseEvent e) {}

    @Override
    public void mouseReleased(final MouseEvent e) {}

    @Override
    public void mouseEntered(final MouseEvent e) {}

    @Override
    public void mouseExited(final MouseEvent e) {}

}