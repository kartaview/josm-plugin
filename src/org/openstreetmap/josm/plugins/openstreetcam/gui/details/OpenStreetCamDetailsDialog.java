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
import java.io.BufferedInputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.ServiceConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.Shortcut;
import com.telenav.josm.common.gui.GuiBuilder;


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
    // private final JPanel pnlDetails;
    private final PhotoPanel pnlPhoto;
    private final ButtonPanel pnlBtn;


    public OpenStreetCamDetailsDialog() {
        super(GuiConfig.getInstance().getPluginShortName(), IconConfig.getInstance().getDialogShortcutName(),
                GuiConfig.getInstance().getPluginLongName(), shortcut, DLG_HEIGHT, true, PreferenceEditor.class);

        pnlPhoto = new PhotoPanel();
        pnlBtn = new ButtonPanel();
        // pnlDetails = new JPanel(new FlowLayout());
        lblDetails = GuiBuilder.buildLabel(null, getFont().deriveFont(GuiBuilder.FONT_SIZE_12), Color.white);
        // pnlDetails.add(lblDetails, FlowLayout.LEFT);
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
            // load image
            BufferedImage image = null;
            String detailsTxt = Formatter.formatPhotoDetails(photo);
            if (PreferenceManager.getInstance().loadHighQualityPhotoFlag()) {
                // load high quality image
                try {
                    image = loadImage(photo.getName());
                    updateUI(image, detailsTxt, false);
                } catch (Exception e) {
                    // try to load large thumbnail
                    try {
                        image = loadImage(photo.getLargeThumbnailName());
                        updateUI(image, detailsTxt, true);
                    } catch (Exception ex) {
                        pnlPhoto.displayErrorMessage();
                    }
                }
            } else {
                // load large thumbnail
                try {
                    image = loadImage(photo.getLargeThumbnailName());
                    updateUI(image, detailsTxt, false);
                } catch (Exception ex) {
                    pnlPhoto.displayErrorMessage();
                }
            }
        } else {
            lblDetails.setText("");
            lblDetails.setToolTipText(null);
            lblDetails.setIcon(null);
            pnlPhoto.updateUI(null);
        }
        pnlBtn.updateUI(photo);
        lblDetails.revalidate();
        repaint();
    }


    private BufferedImage loadImage(final String photoName) throws Exception {
        final StringBuilder link = new StringBuilder(ServiceConfig.getInstance().getBaseUrl());
        link.append(photoName);
        ImageIO.setUseCache(false);
        return ImageIO.read(new BufferedInputStream(new URL(link.toString()).openStream()));
    }

    private void updateUI(BufferedImage image, String detailsTxt, boolean showWarning) {
        lblDetails.setText(detailsTxt);
        if (showWarning) {
            lblDetails.setIcon(IconConfig.getInstance().getWarningIcon());
            lblDetails.setToolTipText(GuiConfig.getInstance().getWarningHighQualityPhoto());
        } else {
            lblDetails.setToolTipText(null);
            lblDetails.setIcon(null);
        }
        pnlPhoto.updateUI(image);
    }

    /**
     * Registers the observers to the button panel.
     *
     * @param locationObserver the {@code LocationObserver} listens for location button action
     * @param sequenceObserver the {@code SequenceObserver} listens for next/previous action
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
}