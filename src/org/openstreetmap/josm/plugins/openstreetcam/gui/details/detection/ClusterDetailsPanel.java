/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Dimension;
import java.awt.Font;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.telenav.josm.common.formatter.DateFormatter;


/**
 * Displays the information of a selected {@code Cluster}.
 *
 * @author beataj
 * @version $Revision$
 */
class ClusterDetailsPanel extends BaseDetailsPanel<Cluster> {

    private static final long serialVersionUID = -5861164183509625676L;

    @Override
    protected void createComponents(final Cluster cluster) {
        final int widthLbl = getMaxWidth(getFontMetrics(getFont().deriveFont(Font.BOLD)),
                GuiConfig.getInstance().getDetectedDetectionText(), GuiConfig.getInstance().getDetectionOnOsmText(),
                GuiConfig.getInstance().getDetectionCreatedDate(), GuiConfig.getInstance().getClusterDetectionsLbl(),
                GuiConfig.getInstance().getDetectionIdLbl(), GuiConfig.getInstance().getClusterComponentValueLbl());
        addSignType(GuiConfig.getInstance().getDetectedDetectionText(), cluster.getSign(), widthLbl);
        addInformation(GuiConfig.getInstance().getDetectionOnOsmText(), cluster.getOsmComparison(), widthLbl);
        if (cluster.getLatestChangeTimestamp() != null) {
            addInformation(GuiConfig.getInstance().getDetectionCreatedDate(),
                    DateFormatter.formatTimestamp(cluster.getLatestChangeTimestamp()), widthLbl);
        }
        final int detections = cluster.getDetectionIds() != null ? cluster.getDetectionIds().size() : 0;
        addInformation(GuiConfig.getInstance().getClusterDetectionsLbl(), detections, widthLbl);
        addInformation(GuiConfig.getInstance().getDetectionIdLbl(), cluster.getId(), widthLbl);
        addInformation(GuiConfig.getInstance().getClusterComponentValueLbl(), cluster.getComponentValue(), widthLbl);
        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, pnlHeight));
    }
}