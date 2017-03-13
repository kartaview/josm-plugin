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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.openstreetcam.ImageHandler;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.Pair;
import org.openstreetmap.josm.tools.Shortcut;
import com.telenav.josm.common.gui.GuiBuilder;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Defines the logic of the left side "OpenStreetCamDetails" panel.
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetCamDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = -8089399825436744652L;

    /** preferred size */
    private static final Dimension DIM = new Dimension(150, 150);

    /** dialog default height */
    private static final int DLG_HEIGHT = 150;

    /** the dialog shortcut displayed on the left side slide menu */
    private static Shortcut shortcut = Shortcut.registerShortcut(GuiConfig.getInstance().getPluginShortName(),
            GuiConfig.getInstance().getPluginLongName(), KeyEvent.VK_F10, Shortcut.NONE);

    /* dialog components */
    private final JLabel lblDetails;
    private final PhotoPanel pnlPhoto;
    private final ButtonPanel pnlBtn;


    public OpenStreetCamDetailsDialog() {
        super(GuiConfig.getInstance().getPluginShortName(), IconConfig.getInstance().getDialogShortcutName(),
                GuiConfig.getInstance().getPluginLongName(), shortcut, DLG_HEIGHT, true, PreferenceEditor.class);

        pnlPhoto = new PhotoPanel();
        pnlBtn = new ButtonPanel();
        lblDetails = GuiBuilder.buildLabel(null, getFont().deriveFont(GuiBuilder.FONT_SIZE_12), Color.white);
        final JPanel pnlMain = GuiBuilder.buildBorderLayoutPanel(lblDetails, pnlPhoto, pnlBtn);
        add(createLayout(pnlMain, false, null));
        setPreferredSize(DIM);
        pnlPhoto.setSize(getPreferredSize());
    }


    /**
     * Updates the details dialog with the details of the given photo.
     *
     * @param photo the currently selected {@code Photo}
     */
    public void updateUI(final Photo photo) {
        if (photo != null) {

            // display loading text
            ThreadPool.getInstance().execute(() -> {
                pnlPhoto.displayLoadingMessage();
                pnlBtn.updateUI(photo);
                lblDetails.revalidate();
                repaint();
            });

            // load image
            ThreadPool.getInstance().execute(() -> loadPhoto(photo));
        } else {
            lblDetails.setText("");
            lblDetails.setToolTipText(null);
            lblDetails.setIcon(null);
            pnlPhoto.updateUI(null);
            pnlBtn.updateUI(null);
            repaint();
        }
    }

    private void loadPhoto(final Photo photo) {
        pnlPhoto.displayLoadingMessage();
        final String detailsTxt = Formatter.formatPhotoDetails(photo);
        try {
            final Pair<BufferedImage, Boolean> imageResult = ImageHandler.getInstance().loadPhoto(photo);
            if (imageResult != null) {
                lblDetails.setText(detailsTxt);
                if (imageResult.b) {
                    lblDetails.setIcon(IconConfig.getInstance().getWarningIcon());
                    lblDetails.setToolTipText(GuiConfig.getInstance().getWarningHighQualityPhoto());
                } else {
                    lblDetails.setToolTipText(null);
                    lblDetails.setIcon(null);
                }
                pnlPhoto.updateUI(imageResult.a);
            }
        } catch (final Exception e) {
            pnlPhoto.displayErrorMessage();
        }
        pnlBtn.updateUI(photo);
        lblDetails.revalidate();
        repaint();
    }

    /**
     * Registers the observers to the button panel.
     *
     * @param locationObserver the {@code LocationObserver} listens for the location button's action
     * @param sequenceObserver the {@code SequenceObserver} listens for the next/previous button's action
     */
    public void registerObservers(final LocationObserver locationObserver, final SequenceObserver sequenceObserver) {
        pnlBtn.registerObserver(locationObserver);
        pnlBtn.registerObserver(sequenceObserver);
    }

    /**
     * Enables/disables the sequence related actions.
     *
     * @param isPrevious if true/false the previous photo action button is enabled
     * @param isNext is true the next photo action button is enabled
     */
    public void enableSequenceActions(final boolean isPrevious, final boolean isNext) {
        pnlBtn.enableSequenceActions(isPrevious, isNext);
        pnlBtn.repaint();
    }

    public void updateManualSwitchButton(final DataType dataType, final int zoom) {
        pnlBtn.updateManualSwitchButton(dataType, zoom);
        pnlBtn.repaint();
    }

    public void enableManualSwitchButton(final boolean enabled) {
        pnlBtn.enableManualSwitchButton(enabled);
        pnlBtn.repaint();
    }
}