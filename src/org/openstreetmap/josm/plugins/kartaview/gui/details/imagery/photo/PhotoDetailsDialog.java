/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.photo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.kartaview.argument.PhotoSize;
import org.openstreetmap.josm.plugins.kartaview.entity.Detection;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.kartaview.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.kartaview.handler.PhotoHandler;
import org.openstreetmap.josm.plugins.kartaview.observer.DetectionSelectionObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.LocationObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.NearbyPhotoObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.SequenceAutoplayObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.SwitchPhotoFormatObserver;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import com.grab.josm.common.entity.Pair;
import com.grab.josm.common.gui.builder.ContainerBuilder;
import com.grab.josm.common.thread.ThreadPool;


/**
 * Defines the logic of the left side "KartaViewDetails" panel.
 *
 * @author Beata
 * @version $Revision$
 */
public final class PhotoDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = -8089399825436744652L;

    /** preferred size */
    private static final Dimension DIM = new Dimension(150, 150);

    /** dialog default height */
    private static final int DLG_HEIGHT = 150;

    private static PhotoDetailsDialog instance = new PhotoDetailsDialog();

    /* dialog components */
    private DetailsPanel pnlDetails;
    private PhotoPanel pnlPhoto;
    private ButtonPanel pnlBtn;

    /** the dimension of the dialog window, it is used to detect if the user had maximized or not the dialog window */
    private Dimension size;

    /** the currently selected element */
    private transient Pair<Photo, PhotoSize> selectedElement;
    private boolean isDetached = false;


    private PhotoDetailsDialog() {
        super(GuiConfig.getInstance().getPluginShortName(), IconConfig.getInstance().getDialogShortcutName(), GuiConfig
                .getInstance().getPluginShortcutLongText(), ShortcutFactory.getInstance().getShortcut(GuiConfig
                        .getInstance().getPluginShortcutText()), DLG_HEIGHT, true, PreferenceEditor.class);
        pnlDetails = new DetailsPanel(getBackground());
        pnlDetails.setBackground(getBackground());
        pnlPhoto = new PhotoPanel();
        pnlBtn = new ButtonPanel();
        final JPanel pnlMain = ContainerBuilder.buildBorderLayoutPanel(pnlDetails, pnlPhoto, pnlBtn, null);
        add(createLayout(pnlMain, false, null));
        setPreferredSize(DIM);
        pnlPhoto.setSize(getPreferredSize());
        size = DIM;
    }

    /**
     * Returns the unique instance of the details dialog window.
     *
     * @return a {@code KartaViewDetailsDialog}
     */
    public static synchronized PhotoDetailsDialog getInstance() {
        if (Objects.isNull(instance)) {
            instance = new PhotoDetailsDialog();
        }
        return instance;
    }

    /**
     * Destroys the instance of the dialog.
     */
    public static synchronized void destroyInstance() {
        if (Objects.nonNull(instance)) {
            instance.pnlDetails = null;
            instance.pnlPhoto = null;
            instance.pnlBtn = null;
            instance.selectedElement = null;
            instance = null;
        }
    }

    @Override
    public void destroy() {
        destroyInstance();
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        if (selectedElement != null && selectedElement.getSecond().equals(PhotoSize.THUMBNAIL) && isPanelMaximized()) {
            loadPhoto(selectedElement.getFirst(), PhotoSize.LARGE_THUMBNAIL);
            size = getSize();
        }
        super.paintComponent(graphics);
    }

    private boolean isPanelMaximized() {
        return !size.equals(getSize()) && (size.getHeight() < getSize().getHeight() || size.getWidth() < getSize()
                .getWidth());
    }

    /**
     * It is called when the detached dialog is opened.
     */
    @Override
    protected void detach() {
        if (!isDetached) {
            pnlPhoto.initializeCurrentImageView();
            super.detach();
            isDetached = true;
        }
    }

    /**
     * It is called when the detached dialog is closed.
     */
    @Override
    protected void dock() {
        pnlPhoto.initializeCurrentImageView();
        super.dock();
        isDetached = false;
    }

    @Override
    public void expand() {
        if (isCollapsed) {
            super.expand();
        }
    }

    @Override
    public void hideDialog() {
        super.hideDialog();
        isDetached = false;
    }

    /**
     * Updates the details dialog with the details of the given photo.
     *
     * @param photo the currently selected {@code Photo}
     * @param photoType the type of photo to be loaded
     * @param displayLoadingMessage specifies if the loading message is displayed or not. The loading message is
     * displayed until the photo is loaded.
     */
    public void updateUI(final Photo photo, final PhotoSize photoType, final boolean displayLoadingMessage) {
        if (photo != null) {
            // display loading text
            if (displayLoadingMessage) {
                pnlDetails.updateUI(null, false);
                pnlPhoto.displayLoadingMessage();
            }
            pnlBtn.updateUI(photo);
            repaint();

            // load image
            ThreadPool.getInstance().execute(() -> loadPhoto(photo, photoType));
        } else {
            pnlDetails.setToolTipText("");
            pnlDetails.updateUI(null, false);
            pnlPhoto.updateUI(null, null);
            pnlBtn.updateUI(null);
            repaint();
        }
    }

    private void loadPhoto(final Photo photo, final PhotoSize photoType) {
        final PhotoSize finalPhotoType = photoType == null ? PhotoSize.LARGE_THUMBNAIL : photoType;
        try {
            final Pair<BufferedImage, PhotoSize> imageResult = PhotoHandler.getInstance().loadPhoto(photo,
                    finalPhotoType);
            selectedElement = new Pair<>(photo, imageResult.getSecond());
            SwingUtilities.invokeLater(() -> {
                if (imageResult.getFirst() != null && DataSet.getInstance().getSelectedPhoto() != null && DataSet
                        .getInstance().getSelectedPhoto().equals(photo)) {
                    if (PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag() && !imageResult
                            .getSecond().equals(PhotoSize.HIGH_QUALITY)) {
                        pnlDetails.updateUI(photo, true);
                        pnlDetails.setToolTipText(GuiConfig.getInstance().getWarningHighQualityPhoto());
                    } else {
                        pnlDetails.updateUI(photo, false);
                        pnlDetails.setToolTipText(null);
                    }
                    pnlPhoto.updateUI(imageResult.getFirst(), photo.getDetections());
                }
            });
        } catch (final Exception e) {
            pnlPhoto.displayErrorMessage();
        }
        repaint();
    }

    /**
     * Removes the given detection from the list of displayed detections.
     *
     * @param detection a {@code Detection} to be removed
     */
    public void removePhotoDetection(final Detection detection) {
        pnlPhoto.removeDetection(detection);
        repaint();
    }

    /**
     * Updates the list of displayed detections. If the list is null, then no detection is displayed.
     *
     * @param detections a list of {@code Detection}s to be displayed.
     */
    public void updatePhotoDetections(final List<Detection> detections) {
        pnlPhoto.updateDetections(detections);
        repaint();
    }

    /**
     * Registers the observers to the panels.
     *
     * @param closestPhotoObserver the {@code ClosestPhotoObserver} listens for the closest button's action
     * @param locationObserver the {@code LocationObserver} listens for the location button's action
     * @param sequenceObserver the {@code SequenceObserver} listens for the next/previous button's action
     * @param trackAutoplayObserver the {@code TrackAutoplayObserver} listens for the play/stop button's action
     * @param switchPhotoFormatObserver the {@code SwitchPhotoFormatObserver} listens for switch image format button's
     * action
     * @param detectionSelectionObserver the {@code DetectionSelectionObserver} listens for detection selection action
     */
    public void registerObservers(final NearbyPhotoObserver closestPhotoObserver,
            final LocationObserver locationObserver, final SequenceObserver sequenceObserver,
            final SequenceAutoplayObserver trackAutoplayObserver,
            final SwitchPhotoFormatObserver switchPhotoFormatObserver,
            final DetectionSelectionObserver detectionSelectionObserver) {
        pnlBtn.registerObserver(closestPhotoObserver);
        pnlBtn.registerObserver(locationObserver);
        pnlBtn.registerObserver(sequenceObserver);
        pnlBtn.registerObserver(trackAutoplayObserver);
        pnlBtn.registerObserver(switchPhotoFormatObserver);
        pnlPhoto.registerObserver(detectionSelectionObserver);
    }

    /**
     * Enables/disables the sequence related actions.
     *
     * @param isPrevious if true/false the previous photo action button is enabled
     * @param isNext is true the next photo action button is enabled
     * @param action represents the track play current situation
     */
    public void enableSequenceActions(final boolean isPrevious, final boolean isNext, final AutoplayAction action) {
        pnlBtn.enableSequenceActions(isPrevious, isNext, action);
        pnlBtn.revalidate();
        repaint();
    }

    /**
     * Enables/disables the closest photo button.
     *
     * @param enabled enables/disables the closest photo button
     */
    public void enableClosestPhotoButton(final boolean enabled) {
        pnlBtn.enableClosestPhotoButton(enabled);
        pnlBtn.revalidate();
        repaint();
    }

    public void updateSwitchImageFormatButton(final boolean enabled, final boolean isCroppedInPanel) {
        pnlBtn.enableSwitchImageFormatButton(enabled, isCroppedInPanel);
    }

    /**
     * Checks if a photo is selected or not.
     *
     * @return true if a photo is selected, false otherwise
     */
    public boolean isPhotoSelected() {
        return pnlBtn.isPhotoSelected();
    }

    public List<Detection> getDisplayedPhotoDetections() {
        return pnlPhoto.getDetections();
    }
}