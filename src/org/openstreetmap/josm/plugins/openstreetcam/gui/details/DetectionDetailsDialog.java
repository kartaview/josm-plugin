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
import javax.swing.JPanel;
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
public class DetectionDetailsDialog extends ToggleDialog {

    private static final long serialVersionUID = -3824929254682268496L;

    /** preferred size */
    private static final Dimension DIM = new Dimension(150, 150);

    /** dialog default height */
    private static final int DLG_HEIGHT = 150;

    private static DetectionDetailsDialog instance = new DetectionDetailsDialog();

    /** dialog components */
    private final PhotoPanel pnlDetails;
    private final DetectionButtonPanel pnlBtn;

    private DetectionDetailsDialog() {
        super(GuiConfig.getInstance().getPluginShortName(), IconConfig.getInstance().getDialogShortcutName(),
                GuiConfig.getInstance().getPluginShortcutLongText(),
                ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getPluginShortcutText()), DLG_HEIGHT,
                true, PreferenceEditor.class);
        pnlDetails = new PhotoPanel();
        pnlBtn = new DetectionButtonPanel();
        pnlDetails.setBackground(getBackground());
        final JPanel pnlMain = ContainerBuilder.buildBorderLayoutPanel(null, pnlDetails, pnlBtn, null);
        add(createLayout(pnlMain, false, null));
        setPreferredSize(DIM);
        pnlDetails.setSize(getPreferredSize());

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

    }

}