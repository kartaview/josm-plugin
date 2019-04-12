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
import java.awt.Rectangle;
import javax.swing.ListSelectionModel;

import com.telenav.josm.common.formatter.DecimalPattern;
import com.telenav.josm.common.formatter.EntityFormatter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.observer.RowSelectionObserver;
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

    private static final int ROW_HEIGHT = 15;
    private static final int INFO_TO_TABLE_EXTRA_HEIGHT = 15;
    private static final int HEADER_TO_CONTENT_EXTRA_HEIGHT = 16;
    private static final int TABLE_END_EXTRA_HEIGHT = 50;
    private static final int LINE_HEIGHT = 25;
    private static final String EMPTY_STRING = "";
    private static final int ID_COLUMN = 0;
    private static final double ZERO_DOUBLE = 0;
    private static final String ZERO_STRING = "0";


    private DetectionTable table;
    private RowSelectionObserver rowSelectionObserver;

    private Cluster cluster;

    @Override
    protected void createComponents(final Cluster cluster) {
        this.cluster = cluster;
        final int widthLbl = getMaxWidth(getFontMetrics(getFont().deriveFont(Font.BOLD)),
                GuiConfig.getInstance().getDetectedDetectionText(), GuiConfig.getInstance().getDetectionOnOsmText(),
                GuiConfig.getInstance().getDetectionCreatedDate(), GuiConfig.getInstance().getClusterDetectionsLbl(),
                GuiConfig.getInstance().getDetectionIdLbl(), GuiConfig.getInstance().getClusterConfidenceLevelText(),
                GuiConfig.getInstance().getClusterComponentValueLbl(),
                GuiConfig.getInstance().getClusterLaneCountText());
        addSignType(GuiConfig.getInstance().getDetectedDetectionText(), cluster.getSign(), widthLbl);
        addInformation(GuiConfig.getInstance().getDetectionOnOsmText(), cluster.getOsmComparison(), widthLbl);
        if (cluster.getLatestChangeTimestamp() != null) {
            addInformation(GuiConfig.getInstance().getDetectionCreatedDate(),
                    DateFormatter.formatTimestamp(cluster.getLatestChangeTimestamp()), widthLbl);
        }
        addInformation(GuiConfig.getInstance().getDetectionIdLbl(), cluster.getId(), widthLbl);
        if (cluster.getConfidenceLevel() != null) {
            if (cluster.getConfidenceLevel() > ZERO_DOUBLE) {
                addInformation(GuiConfig.getInstance().getClusterConfidenceLevelText(),
                        EntityFormatter.formatDouble(cluster.getConfidenceLevel(), false, DecimalPattern.MEDIUM),
                        widthLbl);
            } else {
                addInformation(GuiConfig.getInstance().getClusterConfidenceLevelText(), ZERO_STRING, widthLbl);
            }
        }
        addInformation(GuiConfig.getInstance().getClusterComponentValueLbl(), cluster.getComponentValue(), widthLbl);
        addInformation(GuiConfig.getInstance().getClusterLaneCountText(), cluster.getLaneCount(), widthLbl);

        if (cluster.getDetections() != null && !cluster.getDetections().isEmpty()) {
            addInformation(GuiConfig.getInstance().getClusterDetectionsLbl(), EMPTY_STRING, widthLbl);
            addClusterTable(cluster);
            table.requestFocusInWindow();
        }

        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth(), pnlHeight));
    }

    void addClusterTable(final Cluster cluster) {
        if (cluster != null) {
            table = new DetectionTable(cluster);
            table.registerObserver(rowSelectionObserver);
            final int detectionsNr = cluster.getDetections().size();
            final int tableWidth = table.getTableWidth();

            table.getTableHeader()
            .setBounds(new Rectangle(0, getPnlY() + INFO_TO_TABLE_EXTRA_HEIGHT, tableWidth, LINE_HEIGHT));

            add(table.getTableHeader());
            final int heightTableContent = detectionsNr * ROW_HEIGHT;
            table.setBounds(new Rectangle(0, getPnlY() + HEADER_TO_CONTENT_EXTRA_HEIGHT + LINE_HEIGHT, tableWidth,
                    heightTableContent));
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            add(table);
            setPnlY(getPnlY() + heightTableContent + TABLE_END_EXTRA_HEIGHT);
            setPnlWidth(tableWidth);
        }
    }

    void addSelectedDetectionToTable(final Detection detection) {
        if (detection != null && table != null && table.getCluster() != null) {
            int detectionRow = 0;
            for (int rowIndex = 0; rowIndex < table.getRowCount(); ++rowIndex) {
                if (table.getValueAt(rowIndex, ID_COLUMN) != null) {
                    final int currentRowId = Integer.valueOf(table.getValueAt(rowIndex, ID_COLUMN).toString());
                    if (currentRowId == detection.getId()) {
                        detectionRow = rowIndex;
                    }
                }
            }
            if (table.getRowCount() > 0) {
                table.setRowSelectionInterval(detectionRow, detectionRow);
            }
        }
    }

    void registerObserver(final RowSelectionObserver rowSelectionObserver) {
        this.rowSelectionObserver = rowSelectionObserver;
    }


    Cluster getCluster() {
        return cluster;
    }

    void clearCluster() {
        this.cluster = null;
    }
}