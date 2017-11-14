/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.gui.preferences.PreferenceEditor;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import com.telenav.josm.common.gui.builder.ContainerBuilder;


/**
 * Defines the logic of the left side "OpenStreetCam Detection Details" panel.
 *
 * @author ioanao
 * @version $Revision$
 */
public final class DetectionDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = -3824929254682268496L;

    /** preferred size */
    private static final Dimension DIM = new Dimension(150, 100);

    /** dialog default height */
    private static final int DLG_HEIGHT = 100;


    private static final int SCROLL_BAR_UNIT = 100;

    private static DetectionDetailsDialog instance = new DetectionDetailsDialog();

    /** dialog components */
    private final DetectionDetailsPanel pnlDetails;
    private final DetectionButtonPanel pnlButtons;


    private DetectionDetailsDialog() {
        super(GuiConfig.getInstance().getDetectionDialogTitleName(),
                IconConfig.getInstance().getDetectionDialogShortcutName(),
                GuiConfig.getInstance().getPluginDetectionShortcutLongText(),
                ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getPluginDetectionShortcutText()),
                DLG_HEIGHT,
                true, PreferenceEditor.class);

        pnlDetails = new DetectionDetailsPanel();
        final JScrollPane scrollablePanel = ContainerBuilder.buildScrollPane(
                ContainerBuilder.buildEmptyPanel(Color.WHITE), null, Color.white, null, SCROLL_BAR_UNIT, false, DIM);
        scrollablePanel.setViewportView(pnlDetails);

        pnlButtons = new DetectionButtonPanel();

        add(createLayout(ContainerBuilder.buildBorderLayoutPanel(null, scrollablePanel, pnlButtons, null), false,
                null));
        setPreferredSize(DIM);
        pnlDetails.setSize(getPreferredSize());
        updateDetectionDetails(null);
    }

    /**
     * Returns the unique instance of the detection details dialog window.
     *
     * @return a {@code DetectionDetailsDialog}
     */
    public static DetectionDetailsDialog getInstance() {
        if (instance == null) {
            instance = new DetectionDetailsDialog();
        }
        return instance;
    }

    public void updateDetectionDetails(final Detection detection) {
        pnlDetails.updateData(detection);
        repaint();
    }
}