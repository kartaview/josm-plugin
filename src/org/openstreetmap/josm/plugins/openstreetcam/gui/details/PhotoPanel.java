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
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.tools.Pair;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Displays the selected image.
 *
 * @author Beata
 * @version $Revision$
 */
class PhotoPanel extends JPanel implements MouseWheelListener {

    private static final long serialVersionUID = -1550900781158007580L;
    private static final int MAX_ZOOM = 5;

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
        addMouseWheelListener(this);
        addMouseListener(new MousePressedAdapater());
        addMouseMotionListener(new MouseDraggedAdapter());
    }


    void updateUI(final BufferedImage image) {
        removeAll();
        this.image = image;
        if (image != null) {
            currentView = new Rectangle(0, 0, image.getWidth(), image.getHeight());
        } else {
            currentView = null;
            frame = null;
        }
        repaint();
    }

    void displayErrorMessage() {
        removeAll();
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getErrorPhotoLoadingTxt(),
                getFont().deriveFont(Font.BOLD, GuiBuilder.FONT_SIZE_12), Color.white), BorderLayout.CENTER);
        repaint();
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

        if (getWidth() > getHeight()) {
            vertical = getImageFixedDimension(y, currentView.y, currentView.height, image.getHeight(), wheelRotation);
            if (vertical.b == image.getHeight() && vertical.a == 0) {
                horizontal = new Pair<>(0, image.getWidth());
            } else {
                final int newWidth = ((vertical.b - vertical.a) * getWidth()) / getHeight();
                horizontal = getImageRelativeDimension(x, currentView.x, currentView.width, newWidth, image.getWidth(),
                        wheelRotation);
            }
        } else {
            horizontal = getImageFixedDimension(x, currentView.x, currentView.width, image.getWidth(), wheelRotation);
            if (horizontal.b == image.getWidth() && horizontal.a == 0) {
                vertical = new Pair<>(0, image.getHeight());
            } else {
                final int newHeight = (horizontal.b - horizontal.a) * getHeight() / getWidth();
                vertical = getImageRelativeDimension(y, currentView.y, currentView.height, newHeight, image.getHeight(),
                        wheelRotation);
            }
        }

        if ((horizontal.b - horizontal.a > image.getWidth() / MAX_ZOOM)
                && (vertical.b - vertical.a > image.getHeight() / MAX_ZOOM)) {
            currentView = new Rectangle(horizontal.a, vertical.a, horizontal.b - horizontal.a, vertical.b - vertical.a);
        }
    }

    private Pair<Integer, Integer> getImageFixedDimension(final int ref, final int currentViewMinCoord,
            final int currentViewDim, final int imgDim, final int wheelRotation) {
        Pair<Integer, Integer> pair;

        // zoom in case
        if (wheelRotation < 0) {
            pair = getImagePart(ref, currentViewMinCoord, currentViewMinCoord + currentViewDim,
                    (currentViewDim * 4 / 5) / 2);
        } else {
            if (currentViewDim != imgDim) {
                final int dif = (currentViewDim * 5 / 4) / 2;
                int minRef = currentViewMinCoord - dif;
                int maxRef = currentViewMinCoord + currentViewDim + dif;

                minRef = (minRef < 0) ? 0 : minRef;
                maxRef = (maxRef > imgDim) ? imgDim : maxRef;

                pair = getImagePart(ref, minRef, maxRef, dif + 1);
            } else {
                pair = new Pair<>(0, imgDim);
            }
        }
        return pair;
    }

    /**
     * @param ref is an integer representing the x or the y coordinate of the mouse position, which is taken into
     * account on the new image part computation
     * @param currentViewMinCoord is an integer representing the x or the y coordinate of current view left corner
     * @param currentViewDim is an integer representing the width or the height of the image part which is currently
     * displayed on the screen
     * @param newDim is an integer which should be the new image part dimension (width or height)
     * @param imgDim is an integer representing the image dimension (on 0x axis or 0y axis)
     * @param wheelRotation is an integer, its negative values signifying zoom in and positive values signifying zoom
     * out
     * @return a pair {@code Pair} containing the left corner x or y coordinate and the right corner x or y coordinate
     * of the new image part which will be displayed
     */
    private Pair<Integer, Integer> getImageRelativeDimension(final int ref, final int currentViewMinCoord,
            final int currentViewDim, final int newDim, final int imgDim, final int wheelRotation) {
        // zoom in case
        if (wheelRotation < 0) {
            final int dimension = (newDim > currentViewDim) ? currentViewDim : newDim;
            return getImagePart(ref, currentViewMinCoord, currentViewMinCoord + currentViewDim, dimension / 2);
        }

        // zoom out case
        int minRef = ref - (newDim / 2);
        int maxRef = minRef + newDim;
        if (minRef < 0) {
            maxRef -= minRef;
            minRef = 0;
        }
        if (maxRef > imgDim) {
            minRef -= maxRef - imgDim;
            minRef = (minRef < 0) ? 0 : minRef;
            maxRef = imgDim;
        }

        return new Pair<>(minRef, maxRef);
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

    /**
     * Translate a panel coordinate to an image coordinate (avoid the "empty" space around image).
     *
     * @param point is a coordinate from the panel
     * @return a new point {@code Point} from the image coordinate system
     */
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
            g.fillRect(0, 0, getWidth(), getHeight());

            // draw the image
            matchImageOnPanel(g);
        }
    }

    /**
     * The method match on the panel the new part of the image.
     *
     * @param graphics a graphic object {@code Graphics} on which the current view will be drawn out
     */
    private void matchImageOnPanel(final Graphics graphics) {
        int imageWidth = getWidth();
        int imageHeight = (getWidth() * currentView.height) / currentView.width;
        int marginLeft = 0;
        int marginTop = (getHeight() - imageHeight) / 2;
        if (imageHeight > getHeight()) {
            imageHeight = getHeight();
            imageWidth = (getHeight() * currentView.width) / currentView.height;
            marginLeft = (getWidth() - imageWidth) / 2;
            marginTop = 0;
        }

        frame = new Rectangle(marginLeft, marginTop, imageWidth, imageHeight);
        graphics.drawImage(image, marginLeft, marginTop, marginLeft + imageWidth, marginTop + imageHeight,
                currentView.x, currentView.y, currentView.x + currentView.width, currentView.y + currentView.height,
                null);
    }

    private class MousePressedAdapater extends MouseAdapter {

        @Override
        public void mousePressed(final MouseEvent e) {
            if (image != null) {
                startPoint = getPointOnImage(e.getPoint());
            }
        }
    }

    private class MouseDraggedAdapter extends MouseMotionAdapter {

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
    }

}