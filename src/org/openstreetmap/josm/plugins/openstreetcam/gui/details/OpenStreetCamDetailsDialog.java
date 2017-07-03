/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.openstreetcam.PhotoHandler;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.PhotoType;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClosestPhotoObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DataTypeChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.TrackAutoplayObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.gui.builder.ContainerBuilder;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Defines the logic of the left side "OpenStreetCamDetails" panel.
 *
 * @author Beata
 * @version $Revision$
 */
public final class OpenStreetCamDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = -8089399825436744652L;

    /** preferred size */
    private static final Dimension DIM = new Dimension(150, 150);

    /** dialog default height */
    private static final int DLG_HEIGHT = 150;

    private static OpenStreetCamDetailsDialog instance = new OpenStreetCamDetailsDialog();

    /* dialog components */
    private final DetailsPanel pnlDetails;
    private final PhotoPanel pnlPhoto;
    private final ButtonPanel pnlBtn;

    /** flag that indicates if the dialog window was already destroyed or not */
    private boolean destroyed = false;

    /** the dimension of the dialog window, it is used to detect if the user had maximized or not the dialog window */
    private Dimension size;

    /** the currently selected element */
    private Pair<Photo, PhotoType> selectedElement;


    private OpenStreetCamDetailsDialog() {
        super(GuiConfig.getInstance().getPluginShortName(), IconConfig.getInstance().getDialogShortcutName(),
                GuiConfig.getInstance().getPluginShortcutLongText(),
                ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getPluginShortcutText()), DLG_HEIGHT,
                true, PreferenceEditor.class);
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
     * @return a {@code OpenStreetCamDetailsDialog}
     */
    public static OpenStreetCamDetailsDialog getInstance() {
        if (instance == null) {
            instance = new OpenStreetCamDetailsDialog();
        }
        return instance;
    }

    /**
     * Destroys the instance of the dialog.
     */
    public static void destroyInstance() {
        instance = null;
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        if (selectedElement != null && selectedElement.getSecond().equals(PhotoType.THUMBNAIL) && isPanelMaximized()) {
            loadPhoto(selectedElement.getFirst(), PhotoType.LARGE_THUMBNAIL);
            size = getSize();
        }
        super.paintComponent(graphics);
    }

    private boolean isPanelMaximized() {
        return !size.equals(getSize())
                && (size.getHeight() < getSize().getHeight() || size.getWidth() < getSize().getWidth());
    }

    @Override
    public void destroy() {
        if (!destroyed) {
            super.destroy();
            destroyed = true;
        }
    }

    /**
     * Updates the details dialog with the details of the given photo.
     *
     * @param photo the currently selected {@code Photo}
     * @param photoType the type of photo to be loaded
     * @param displayLoadingMessage specifies if the loading message is displayed or not. The loading message is
     * displayed until the photo is loaded.
     */
    public void updateUI(final Photo photo, final PhotoType photoType, final boolean displayLoadingMessage) {
        if (photo != null) {
            // display loading text
            if (displayLoadingMessage) {
                pnlPhoto.displayLoadingMessage();
            }
            pnlBtn.updateUI(photo);
            repaint();

            // load image
            ThreadPool.getInstance().execute(() -> loadPhoto(photo, photoType));
        } else {
            pnlDetails.updateUI(null, false);
            pnlDetails.setToolTipText("");
            pnlPhoto.updateUI(null);
            pnlBtn.updateUI(null);
            repaint();
        }
    }

    private void loadPhoto(final Photo photo, final PhotoType photoType) {
        final PhotoType finalPhotoType = photoType == null ? PhotoType.LARGE_THUMBNAIL : photoType;
        try {
            final Pair<BufferedImage, PhotoType> imageResult =
                    PhotoHandler.getInstance().loadPhoto(photo, finalPhotoType);
            selectedElement = new Pair<>(photo, imageResult.getSecond());
            if (imageResult.getFirst() != null) {
                if (PreferenceManager.getInstance().loadPhotoSettings().isHighQualityFlag()
                        && !imageResult.getSecond().equals(PhotoType.HIGH_QUALITY)) {
                    pnlDetails.updateUI(photo, true);
                    pnlDetails.setToolTipText(GuiConfig.getInstance().getWarningHighQualityPhoto());
                } else {
                    pnlDetails.updateUI(photo, false);
                    pnlDetails.setToolTipText(null);
                }
                pnlPhoto.updateUI(imageResult.getFirst());
            }
        } catch (final Exception e) {
            pnlPhoto.displayErrorMessage();
        }
        repaint();
    }

    /**
     * Registers the observers to the button panel.
     *
     * @param closestPhotoObserver the {@code ClosestPhotoObserver} listens for the closest button's action
     * @param dataTypeChangeObserver the {@code DataTypeChangeObserver} listens for the data switch button's action
     * @param locationObserver the {@code LocationObserver} listens for the location button's action
     * @param sequenceObserver the {@code SequenceObserver} listens for the next/previous button's action
     * @param trackAutoplayObserver the {@code TrackAutoplayObserver} listens for the play/stop button's action
     */
    public void registerObservers(final ClosestPhotoObserver closestPhotoObserver,
            final DataTypeChangeObserver dataTypeChangeObserver, final LocationObserver locationObserver,
            final SequenceObserver sequenceObserver, final TrackAutoplayObserver trackAutoplayObserver) {
        pnlBtn.registerObserver(closestPhotoObserver);
        pnlBtn.registerObserver(dataTypeChangeObserver);
        pnlBtn.registerObserver(locationObserver);
        pnlBtn.registerObserver(sequenceObserver);
        pnlBtn.registerObserver(trackAutoplayObserver);
    }

    /**
     * Enables/disables the sequence related actions.
     *
     * @param isPrevious if true/false the previous photo action button is enabled
     * @param isNext is true the next photo action button is enabled
     */
    public void enableSequenceActions(final boolean isPrevious, final boolean isNext) {
        pnlBtn.enableSequenceActions(isPrevious, isNext);
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

    /**
     * Updates the properties of the data switch button. Null properties are ignored.
     *
     * @param dataType a {@code DataType} specifies the currently displayed data type
     * @param isEnabled enables/disables the data switch button
     * @param isVisible specifies the button visibility
     */
    public void updateDataSwitchButton(final DataType dataType, final Boolean isEnabled, final Boolean isVisible) {
        if (dataType != null) {
            pnlBtn.updateDataSwitchButton(dataType);
        }
        if (isEnabled != null) {
            pnlBtn.enableDataSwitchButton(isEnabled);
        }
        if (isVisible != null) {
            pnlBtn.setDataSwitchButtonVisibiliy(isVisible);
        }
        pnlBtn.revalidate();
        repaint();
    }

    /**
     * Checks if a photo is selected or not.
     *
     * @return true if a photo is selected, false otherwise
     */
    public boolean isPhotoSelected() {
        return pnlBtn.isPhotoSelected();
    }
}