/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.entity.PixelPoint;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionSelectionObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionSelectionObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.grab.josm.common.entity.Pair;
import com.grab.josm.common.gui.builder.LabelBuilder;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


/**
 * Displays the selected image.
 *
 * @author Beata
 * @version $Revision$
 */
class PhotoPanel extends JPanel implements MouseWheelListener, DetectionSelectionObservable {

    private static final long serialVersionUID = -1550900781158007580L;
    private static final int HALF = 2;
    private static final int MAX_ZOOM = 5;
    private static final float BORDER_SIZE = 3;
    private static final Color SELECTED_SIGN_COLOR = new Color(0, 191, 255);
    private static final Color UNSELECTED_SIGN_COLOR = new Color(255, 0, 0);

    private transient BufferedImage image;

    /** a rectangle where the image is incorporated related to the outside panel */
    private Rectangle frame;

    /** a part of the image that is currently displayed */
    private Rectangle currentView;

    /** the image coordinate where the mouse dragging was started */
    private Point startPoint;

    /** the dimension of the panel, it is used to detect if the user had maximized or not the panel */
    private Dimension size;

    /** detection related entities */
    private transient DetectionSelectionObserver detectionSelectionObserver;
    private transient List<Detection> detections;


    PhotoPanel() {
        super(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        addMouseWheelListener(this);
        addMouseListener(new MousePressedAdapter());
        addMouseMotionListener(new MouseDraggedAdapter());
    }

    void updateUI(final BufferedImage image, final List<Detection> detections) {
        removeAll();
        this.image = image;
        this.detections = detections;
        initializeCurrentImageView();
        revalidate();
        repaint();
    }

    void updateDetections(final List<Detection> detections) {
        this.detections = detections;
        revalidate();
        repaint();
    }

    void removeDetection(final Detection detection) {
        if (detection != null && detections != null) {
            detections.remove(detection);
            revalidate();
            repaint();
        }
    }

    /**
     * Initialize the current view of the image with the image itself.
     */
    void initializeCurrentImageView() {
        if (image != null) {
            currentView = new Rectangle(0, 0, image.getWidth(), image.getHeight());
        } else {
            currentView = null;
            frame = null;
        }
    }

    void displayErrorMessage() {
        removeAll();
        setBackground(Color.white);
        image = null;
        currentView = null;
        add(LabelBuilder.build(GuiConfig.getInstance().getErrorLoadingPhotoPanelText(), Font.BOLD, Color.white),
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

        if (wheelRotation > 0 || (horizontal.getSecond() - horizontal.getFirst() > image.getWidth() / MAX_ZOOM
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
            return getImagePart(ref, currentViewMinCoord, currentViewMinCoord + currentViewDim, dimension / HALF);
        }

        // zoom out case
        return getImagePart(ref, 0, imgDim, newDim / HALF);
    }

    private Pair<Integer, Integer> getImagePart(final int ref, final int firstRef, final int secondRef, final int cut) {
        final int minCoord = ref - cut;
        final int maxCoord = ref + cut;

        int modifiedFirstRef = minCoord;
        int modifiedSecondRef = maxCoord;

        if (minCoord < firstRef) {
            modifiedFirstRef = firstRef;
            modifiedSecondRef = modifiedSecondRef - minCoord + firstRef;
        }

        if (maxCoord > secondRef) {
            modifiedFirstRef = modifiedFirstRef - maxCoord + secondRef;
            modifiedSecondRef = secondRef;
        }
        modifiedFirstRef = Math.max(modifiedFirstRef, firstRef);
        modifiedSecondRef = Math.min(modifiedSecondRef, secondRef);
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
            } else {
                matchImageOnPanel();
            }
            g.drawImage(image, frame.x, frame.y, frame.x + frame.width, frame.y + frame.height, currentView.x,
                    currentView.y, currentView.x + currentView.width, currentView.y + currentView.height, null);
            drawDetections((Graphics2D) g);
        }
        size = getSize();
    }

    private void drawDetections(final Graphics2D graphics) {
        if (detections != null && !detections.isEmpty()) {
            final Detection selectedDetection = DataSet.getInstance().getSelectedDetection();
            final Photo displayedPhoto = DataSet.getInstance().getSelectedPhoto();
            for (final Detection detection : detections) {
                setRepresentationProperties(graphics, detection.equals(selectedDetection));
                if (DataSet.getInstance().isFrontFacingDisplayed()) {
                    drawOnFrontFacingImage(graphics, detection, selectedDetection);
                } else {
                    drawOnWrappedImage(graphics, detection, displayedPhoto);
                }
            }
        }
    }

    private void setRepresentationProperties(final Graphics2D graphics, final boolean isSelected) {
        if (isSelected) {
            graphics.setColor(SELECTED_SIGN_COLOR);
            graphics.setStroke(new BasicStroke(BORDER_SIZE));
        } else {
            graphics.setColor(UNSELECTED_SIGN_COLOR);
        }
    }

    private void drawOnFrontFacingImage(final Graphics2D graphics, final Detection detection,
            final Detection selectedDetection) {
        if (detection.getLocationOnPhoto() != null) {
            final double x = frame.x
                    + (detection.getLocationOnPhoto().getX() * image.getWidth() - currentView.x) * frame.getWidth()
                    / currentView.getWidth();
            final double y = frame.y
                    + (detection.getLocationOnPhoto().getY() * image.getHeight() - currentView.y) * frame.getHeight()
                    / currentView.getHeight();

            final double width =
                    detection.getLocationOnPhoto().getWidth() * image.getWidth() * frame.getWidth() / currentView
                            .getWidth();
            final double height =
                    detection.getLocationOnPhoto().getHeight() * image.getHeight() * frame.getHeight() / currentView
                            .getHeight();

            if (detection.equals(selectedDetection)) {
                final Stroke oldStroke = graphics.getStroke();
                graphics.draw(new Rectangle2D.Double(x - BORDER_SIZE, y - BORDER_SIZE, width + 2 * BORDER_SIZE,
                        height + 2 * BORDER_SIZE));
                graphics.setStroke(oldStroke);
            } else {
                graphics.draw(new Rectangle2D.Double(x, y, width, height));
            }
        }
    }

    private void drawOnWrappedImage(final Graphics2D graphics, final Detection detection, final Photo displayedPhoto) {
        if (detection.getShapeOnPhoto() != null && detection.getShapeOnPhoto().getEquirectangularPolygon() != null
                && displayedPhoto.getRealSize() != null && displayedPhoto.getRealSize().isNotNull()) {
            List<PixelPoint> equirectangularPolygon =
                    (List<PixelPoint>) detection.getShapeOnPhoto().getEquirectangularPolygon();
            if (!equirectangularPolygon.isEmpty()) {
                for (int i = 0; equirectangularPolygon != null && i < equirectangularPolygon.size() - 1; ++i) {
                    final PixelPoint point1 = equirectangularPolygon.get(i);
                    final PixelPoint point2 = equirectangularPolygon.get(i + 1);
                    final double x1 = frame.x
                            + (point1.getX() * image.getWidth() - currentView.x) * frame.getWidth() / currentView
                            .getWidth();
                    final double y1 = frame.y
                            + (point1.getY() * image.getHeight() - currentView.y) * frame.getHeight() / currentView
                            .getHeight();
                    final double x2 = frame.x
                            + (point2.getX() * image.getWidth() - currentView.x) * frame.getWidth() / currentView
                            .getWidth();
                    final double y2 = frame.y
                            + (point2.getY() * image.getHeight() - currentView.y) * frame.getHeight() / currentView
                            .getHeight();
                    graphics.draw(new Line2D.Double(x1, y1, x2, y2));
                }
            }
        }
    }

    /**
     * The method match on the panel the new part of the image.
     */
    private void matchImageOnPanel() {
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

    private void panelDimensionsChanged() {
        if (currentView != null) {
            if (currentView.width != image.getWidth() || currentView.height != image.getHeight()) {
                if (getWidth() != size.width) {
                    final Pair<Integer, Integer> newDimension = getImagePart(currentView.x + currentView.width / HALF,
                            0, image.getWidth(), (currentView.height * getWidth() / frame.height) / HALF);
                    currentView.x = newDimension.getFirst();
                    currentView.width = newDimension.getSecond() - newDimension.getFirst();

                    if (currentView.height == image.getHeight() || currentView.width < image.getWidth()) {
                        frame.x = 0;
                        frame.width = getWidth();
                    } else {
                        matchImageOnPanel();
                    }
                }
                if (getHeight() != size.height) {
                    final Pair<Integer, Integer> newDimension = getImagePart(currentView.y + currentView.height / HALF,
                            0, image.getHeight(), (currentView.width * getHeight() / frame.width) / HALF);
                    currentView.y = newDimension.getFirst();
                    currentView.height = newDimension.getSecond() - newDimension.getFirst();

                    if (currentView.width == image.getWidth() || currentView.height < image.getHeight()) {
                        frame.y = 0;
                        frame.height = getHeight();
                    } else {
                        matchImageOnPanel();
                    }
                }
            } else {
                matchImageOnPanel();
            }
        }
    }

    @Override
    public void registerObserver(final DetectionSelectionObserver detectionSelectionObserver) {
        this.detectionSelectionObserver = detectionSelectionObserver;
    }

    @Override
    public void notifyDetectionSelectionObserver(final Detection detection) {
        detectionSelectionObserver.selectPhotoDetection(detection);
    }


    List<Detection> getDetections() {
        return detections;
    }

    private class MousePressedAdapter extends MouseAdapter {

        @Override
        public void mousePressed(final MouseEvent e) {
            if (image != null) {
                startPoint = getPointOnImage(e.getPoint());
            }
        }

        @Override
        public void mouseClicked(final MouseEvent e) {
            if (detections != null && image != null) {
                final Point clickedPoint = getPointOnImage(e.getPoint());
                final Point2D translatedPoint = new Point2D.Double(clickedPoint.getX() / image.getWidth(),
                        clickedPoint.getY() / image.getHeight());
               Detection selectedDetection;
                if (PreferenceManager.getInstance().loadPhotoSettings().isDisplayFrontFacingFlag()) {
                    selectedDetection = detections.stream()
                            .filter(detection -> detection.getLocationOnPhoto() != null && detection
                                    .getLocationOnPhoto().contains(translatedPoint))
                            .sorted(Comparator.comparingDouble(d -> d.getLocationOnPhoto().surface())).findFirst()
                            .orElse(null);
                } else {
                    selectedDetection = detections.stream().filter(detection -> detection.getShapeOnPhoto()
                            .isPointInEquirectangularPolygon(translatedPoint)).findFirst().orElse(null);
                }
                notifyDetectionSelectionObserver(selectedDetection);
                repaint();
            }
        }
    }


    private class MouseDraggedAdapter extends MouseMotionAdapter {

        @Override
        public void mouseDragged(final MouseEvent e) {
            if (image != null) {
                final Point endPoint = getPointOnImage(e.getPoint());
                if (startPoint != null && endPoint != null) {
                    moveCurrentView(startPoint.x - endPoint.x, startPoint.y - endPoint.y);
                    repaint();
                }
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