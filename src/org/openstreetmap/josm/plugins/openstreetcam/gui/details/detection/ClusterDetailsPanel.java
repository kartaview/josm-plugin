/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import com.grab.josm.common.formatter.DateFormatter;
import com.grab.josm.common.formatter.DecimalPattern;
import com.grab.josm.common.formatter.EntityFormatter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.observer.RowSelectionObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.awt.Rectangle;


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


    private DetectionTable table;
    private transient RowSelectionObserver rowSelectionObserver;

    private transient Cluster cluster;

    @Override
    protected void createComponents(final Cluster cluster) {
        this.cluster = cluster;
        final int widthLbl = getMaxWidth(getFontMetrics(getFont().deriveFont(java.awt.Font.BOLD)),
                GuiConfig.getInstance().getDetectedDetectionText(), GuiConfig.getInstance().getDetectionOnOsmText(),
                GuiConfig.getInstance().getDetectionCreatedDate(), GuiConfig.getInstance().getClusterDetectionsLbl(),
                GuiConfig.getInstance().getDetectionIdLbl(), GuiConfig.getInstance().getClusterConfidenceLevelText(),
                GuiConfig.getInstance().getClusterOcrValueLbl(), GuiConfig.getInstance().getOcrConfidenceLbl(),
                GuiConfig.getInstance().getClusterLaneCountText());
        addSignType(GuiConfig.getInstance().getDetectedDetectionText(), cluster.getSign(), widthLbl);
        addInformation(GuiConfig.getInstance().getDetectionOnOsmText(), cluster.getOsmComparison(), widthLbl);
        if (cluster.getLatestChangeTimestamp() != null) {
            addInformation(GuiConfig.getInstance().getDetectionCreatedDate(),
                    DateFormatter.formatTimestamp(cluster.getLatestChangeTimestamp()), widthLbl);
        }
        addInformation(GuiConfig.getInstance().getDetectionIdLbl(), cluster.getId(), widthLbl);
        if (cluster.getConfidenceLevel() != null) {
            if (cluster.getConfidenceLevel().getOverallConfidence() != null) {
                addInformation(GuiConfig.getInstance().getClusterConfidenceLevelText(), EntityFormatter
                        .formatDouble(cluster.getConfidenceLevel().getOverallConfidence(), false,
                                DecimalPattern.MEDIUM), widthLbl);
            }
            if (cluster.getConfidenceLevel().getOcrConfidence() != null) {
                addInformation(GuiConfig.getInstance().getOcrConfidenceLbl(), EntityFormatter
                        .formatDouble(cluster.getConfidenceLevel().getOcrConfidence(), false, DecimalPattern.MEDIUM),
                        widthLbl);
            }
        }
        addInformation(GuiConfig.getInstance().getClusterLaneCountText(), cluster.getLaneCount(), widthLbl);
        addOcrValue(cluster.getOcrValue(), widthLbl);
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
                    final int currentRowId = Integer.parseInt(table.getValueAt(rowIndex, ID_COLUMN).toString());
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