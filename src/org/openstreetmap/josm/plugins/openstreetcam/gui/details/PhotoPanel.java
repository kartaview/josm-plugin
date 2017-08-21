/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.gui.builder.LabelBuilder;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;


/**
 * Displays the selected image.
 *
 * @author Beata
 * @version $Revision$
 */
class PhotoPanel extends JPanel implements MouseWheelListener {

    private static final long serialVersionUID = -1550900781158007580L;
    private static final int HALF = 2;
    private static final int MAX_ZOOM = 5;

    private transient BufferedImage image;

    /**
     * a rectangle where the image is incorporated related to the outside panel
     */
    private Rectangle frame;

    /**
     * a part of the image that is currently displayed
     */
    private Rectangle currentView;

    /**
     * the image coordinate where the mouse dragging was started
     */
    private Point startPoint;

    private Dimension size;


    PhotoPanel() {
        super(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        addMouseWheelListener(this);
        addMouseListener(new MousePressedAdapter());
        addMouseMotionListener(new MouseDraggedAdapter());
    }


    void updateUI(final BufferedImage image) {
        removeAll();
        this.image = image;
        if (image != null) {
            currentView = new Rectangle(0, 0, image.getWidth(), image.getHeight());
            matchImageOnPanel();
        } else {
            currentView = null;
            frame = null;
        }
        revalidate();
        repaint();
    }

    void displayErrorMessage() {
        removeAll();
        setBackground(Color.white);
        image = null;
        currentView = null;
        add(LabelBuilder.build(GuiConfig.getInstance().getErrorPhotoLoadingText(), Font.BOLD, Color.white),
                BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    void displayLoadingMessage() {
        removeAll();
        setBackground(Color.white);
        image = null;
        currentView = null;
        add(LabelBuilder.build(GuiConfig.getInstance().getWarningLoadingPhoto(), Font.BOLD, Color.white),
                BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (image != null) {
            final Point point = getPointOnImage(e.getPoint());
            zoom(point.x, point.y, e.getWheelRotation());
            matchImageOnPanel();
            repaint();
        }
    }

    private void zoom(final int x, final int y, final int wheelRotation) {
        final Pair<Integer, Integer> vertical;
        final Pair<Integer, Integer> horizontal;

        if (getWidth() > getHeight()) {
            vertical = getImageFixedDimension(y, currentView.y, currentView.height, image.getHeight(), wheelRotation);
            if (vertical.getSecond() == image.getHeight() && vertical.getFirst() == 0) {
                horizontal = new Pair<>(0, image.getWidth());
            } else {
                final int newWidth = ((vertical.getSecond() - vertical.getFirst()) * getWidth()) / getHeight();
                horizontal = getImageRelativeDimension(x, currentView.x, currentView.width, newWidth, image.getWidth(),
                        wheelRotation);
            }
        } else {
            horizontal = getImageFixedDimension(x, currentView.x, currentView.width, image.getWidth(), wheelRotation);
            if (horizontal.getSecond() == image.getWidth() && horizontal.getFirst() == 0) {
                vertical = new Pair<>(0, image.getHeight());
            } else {
                final int newHeight = (horizontal.getSecond() - horizontal.getFirst()) * getHeight() / getWidth();
                vertical = getImageRelativeDimension(y, currentView.y, currentView.height, newHeight, image.getHeight(),
                        wheelRotation);
            }
        }

        if (wheelRotation > 0 || (wheelRotation <= 0
                && horizontal.getSecond() - horizontal.getFirst() > image.getWidth() / MAX_ZOOM
                && vertical.getSecond() - vertical.getFirst() > image.getHeight() / MAX_ZOOM)) {
            currentView = new Rectangle(horizontal.getFirst(), vertical.getFirst(),
                    horizontal.getSecond() - horizontal.getFirst(), vertical.getSecond() - vertical.getFirst());
        }
    }

    private Pair<Integer, Integer> getImageFixedDimension(final int ref, final int currentViewMinCoord,
            final int currentViewDim, final int imgDim, final int wheelRotation) {
        final Pair<Integer, Integer> pair;

        // zoom in case
        if (wheelRotation < 0) {
            pair = getImagePart(ref, currentViewMinCoord, currentViewMinCoord + currentViewDim,
                    (currentViewDim * 4 / 5) / HALF);
        } else {
            if (currentViewDim != imgDim) {
                final int dif = (currentViewDim * 5 / 4) / HALF;
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
     * @param ref                 is an integer representing the x or the y coordinate of the mouse position, which is taken into
     *                            account on the new image part computation
     * @param currentViewMinCoord is an integer representing the x or the y coordinate of current view left corner
     * @param currentViewDim      is an integer representing the width or the height of the image part which is currently
     *                            displayed on the screen
     * @param newDim              is an integer which should be the new image part dimension (width or height)
     * @param imgDim              is an integer representing the image dimension (on 0x axis or 0y axis)
     * @param wheelRotation       is an integer, its negative values signifying zoom in and positive values signifying zoom
     *                            out
     * @return a pair {@code Pair} containing the left corner x or y coordinate and the right corner x or y coordinate
     * of the new image part which will be displayed
     */
    private Pair<Integer, Integer> getImageRelativeDimension(final int ref, final int currentViewMinCoord,
            final int currentViewDim, final int newDim, final int imgDim, final int wheelRotation) {
        // zoom in case
        if (wheelRotation < 0) {
            final int dimension = (newDim > currentViewDim) ? currentViewDim : newDim;
            return getImagePart(ref, currentViewMinCoord, currentViewMinCoord + currentViewDim, dimension / HALF);
        }

        // zoom out case
        return normalizeDimension(ref, newDim, imgDim);
    }

    private Pair<Integer, Integer> normalizeDimension(final int ref, final int newDim, final int imgDim) {
        int minRef = ref - newDim / HALF;
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

    private Pair<Integer, Integer> getImagePart(final int ref, final int currentViewMinCoord,
            final int currentViewMaxCoord, final int cut) {
        final int minCoord = ref - cut;
        final int maxCoord = ref + cut;

        int modifiedFirstRef = minCoord;
        int modifiedSecondRef = maxCoord;

        if (minCoord < currentViewMinCoord) {
            modifiedFirstRef = currentViewMinCoord;
            modifiedSecondRef = modifiedSecondRef - minCoord + currentViewMinCoord;
        }

        if (maxCoord > currentViewMaxCoord) {
            modifiedFirstRef = modifiedFirstRef - maxCoord + currentViewMaxCoord;
            modifiedSecondRef = currentViewMaxCoord;
        }

        modifiedFirstRef = modifiedFirstRef < currentViewMinCoord ? currentViewMinCoord : modifiedFirstRef;
        modifiedSecondRef = (modifiedSecondRef > currentViewMaxCoord) ? currentViewMaxCoord : modifiedSecondRef;
        return new Pair<>(modifiedFirstRef, modifiedSecondRef);
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
        // clean the panel
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        if (image != null) {
            // draw the image
            if (!getSize().equals(size)) {
                panelDimensionsChanged();
            }
            g.drawImage(image, frame.x, frame.y, frame.x + frame.width, frame.y + frame.height, currentView.x,
                    currentView.y, currentView.x + currentView.width, currentView.y + currentView.height, null);
        }
        size = getSize();
    }

    /**
     * The method match on the panel the new part of the image.
     */
    private void matchImageOnPanel() {
        System.out.println(frame.y + " " + frame.x);
        int imageWidth = getWidth();
        int imageHeight = (getWidth() * currentView.height) / currentView.width;
        int marginLeft = 0;
        int marginTop = (getHeight() - imageHeight) / HALF;
        if (imageHeight > getHeight()) {
            imageHeight = getHeight();
            imageWidth = (getHeight() * currentView.width) / currentView.height;
            marginLeft = (getWidth() - imageWidth) / HALF;
            marginTop = 0;
        }
        frame = new Rectangle(marginLeft, marginTop, imageWidth, imageHeight);
    }

    void panelDimensionsChanged() {
        if ((currentView != null) && (currentView.width != image.getWidth() || currentView.height != image
                .getHeight())) {
            if (getWidth() != size.getWidth() && getHeight() != size.getHeight()) {
                Pair<Integer, Integer> newDimension = normalizeDimension(currentView.x + currentView.width / HALF,
                        currentView.height * getWidth() / frame.height, image.getWidth());
                currentView.x = newDimension.getFirst();
                currentView.width = newDimension.getSecond() - newDimension.getFirst();

                if (frame.y > 0 || currentView.width < image.getWidth()) {
                    frame.width = getWidth();
                }

                newDimension = normalizeDimension(currentView.y + currentView.height / HALF,
                        currentView.width * getHeight() / frame.width, image.getWidth());
                currentView.y = newDimension.getFirst();
                currentView.height = newDimension.getSecond() - newDimension.getFirst();

                if (frame.x > 0 || currentView.height < image.getHeight()) {
                    frame.height = getHeight();
                }

                if (!(frame.y > 0 || frame.x > 0) || currentView.height == image.getHeight()
                        || currentView.width == image.getWidth()) {
                    matchImageOnPanel();
                }
            } else {
                if (getWidth() != size.getWidth()) {
                    System.out.println("width changed");
                    final Pair<Integer, Integer> newDimension =
                            normalizeDimension(currentView.x + currentView.width / HALF,
                                    currentView.height * getWidth() / frame.height, image.getWidth());
                    currentView.x = newDimension.getFirst();
                    currentView.width = newDimension.getSecond() - newDimension.getSecond();
                    if (frame.y > 0) {
                        System.out.println("width changed with margin");
                        frame.width = getWidth();
                    }
                }
                if (getHeight() != size.getHeight()) {
                    System.out.println("changed height");
                    final Pair<Integer, Integer> newDimension =
                            normalizeDimension(currentView.y + currentView.height / HALF,
                                    currentView.width * getHeight() / frame.width, image.getHeight());
                    currentView.y = newDimension.getFirst();
                    currentView.height = newDimension.getSecond() - newDimension.getFirst();

                    if (frame.x > 0) {
                        System.out.println("heigth changed with margin");
                        frame.height = getHeight();
                    }
                }

                /*if (frame.y > 0 && getWidth() != size.getWidth()) {
                    frame.width = getWidth();
                }
                if (frame.x > 0 && getHeight() != size.getHeight()) {
                    frame.height = getHeight();
                }*/
                System.out.println(frame.y + " " + frame.x);

                if (!(((frame.y > 0) && (getWidth() != size.getWidth())) || ((frame.x > 0) && (getHeight() != size
                        .getHeight())))) {
                    System.out.println("match needed");
                    matchImageOnPanel();
                }
            }
        } else {
            matchImageOnPanel();
        }
    }

    private class MousePressedAdapter extends MouseAdapter {

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
                matchImageOnPanel();
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