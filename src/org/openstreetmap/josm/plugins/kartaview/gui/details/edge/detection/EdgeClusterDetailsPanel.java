/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.edge.detection;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Objects;
import javax.swing.ListSelectionModel;
import org.openstreetmap.josm.plugins.kartaview.entity.Cluster;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.gui.details.common.BaseClusterDetailsPanel;
import org.openstreetmap.josm.plugins.kartaview.observer.EdgeDetectionRowSelectionObserver;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import com.grab.josm.common.formatter.DateFormatter;


/**
 * Displays the information of an Edge cluster.
 *
 * @author maria.mitisor
 */
class EdgeClusterDetailsPanel extends BaseClusterDetailsPanel {

    private static final long serialVersionUID = -1290825247193950651L;

    private EdgeDetectionsTable edgeDetectionsTable;
    private EdgeDetectionRowSelectionObserver rowSelectionObserver;


    @Override
    public void createComponents(final Cluster edgeCluster) {
        setCluster(edgeCluster);
        final int widthLbl = getMaxWidth(getFontMetrics(getFont().deriveFont(Font.BOLD)),
                GuiConfig.getInstance().getEdgeDetectionIdLbl(), GuiConfig.getInstance().getEdgeClusterCreatedAtLbl(),
                GuiConfig.getInstance().getEdgeDetectionSignLbl(), GuiConfig.getInstance().getEdgeClusterOSMLbl(),
                GuiConfig.getInstance().getEdgeDetectionAlgorithmLbl(),
                GuiConfig.getInstance().getEdgeDetectionConfidenceLevelLbl(),
                GuiConfig.getInstance().getDetectionFacingText(), GuiConfig.getInstance().getClusterDetectionsLbl());

        addInformation(GuiConfig.getInstance().getEdgeDetectionIdLbl(), edgeCluster.getId(), widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeClusterCreatedAtLbl(),
                DateFormatter.formatTimestamp(edgeCluster.getLatestChangeTimestamp()), widthLbl);
        addSignIcon(GuiConfig.getInstance().getEdgeDetectionSignLbl(), edgeCluster.getSign(), widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeClusterOSMLbl(), edgeCluster.getOsmComparison().name(), widthLbl);

        if (Objects.nonNull(edgeCluster.getAlgorithm())) {
            addInformation(GuiConfig.getInstance().getEdgeDetectionAlgorithmLbl(),
                    edgeCluster.getAlgorithm().toString(), widthLbl);
        }
        if (Objects.nonNull(edgeCluster.getConfidenceLevel())) {
            addInformation(GuiConfig.getInstance().getEdgeDetectionConfidenceLevelLbl(),
                    edgeCluster.getConfidenceLevel().toString(), widthLbl);
        }
        addInformation(GuiConfig.getInstance().getDetectionFacingText(), edgeCluster.getFacing(), widthLbl);

        if (Objects.nonNull(edgeCluster.getEdgeDetections()) && !edgeCluster.getEdgeDetections().isEmpty()) {
            addInformation(GuiConfig.getInstance().getClusterDetectionsLbl(), EMPTY_STRING, widthLbl);
            addDetectionsTable(edgeCluster);
            addSelectedEdgeDetectionToTable(edgeCluster.getEdgeDetections().get(0));
            edgeDetectionsTable.requestFocusInWindow();
        }
        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, pnlHeight));
    }

    @Override
    protected void addDetectionsTable(final Cluster edgeCluster) {
        edgeDetectionsTable = new EdgeDetectionsTable(edgeCluster);
        edgeDetectionsTable.registerObserver(rowSelectionObserver);
        final int tableWidth = edgeDetectionsTable.getTableWidth();

        edgeDetectionsTable.getTableHeader().setBounds(new Rectangle(0, getPnlY() + INFO_TO_TABLE_EXTRA_HEIGHT,
                Math.max(tableWidth, getPnlWidth()), LINE_HEIGHT));

        final int heightTableContent = edgeCluster.getEdgeDetections().size() * ROW_HEIGHT;
        edgeDetectionsTable.setBounds(new Rectangle(0, getPnlY() + HEADER_TO_CONTENT_EXTRA_HEIGHT + LINE_HEIGHT,
                Math.max(tableWidth, getPnlWidth()), heightTableContent));
        edgeDetectionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(edgeDetectionsTable.getTableHeader());
        add(edgeDetectionsTable);
        setPnlY(getPnlY() + heightTableContent + TABLE_END_EXTRA_HEIGHT);
        setPnlWidth(tableWidth);

        addSelectedEdgeDetectionToTable(edgeCluster.getEdgeDetections().get(0));
    }

    void addSelectedEdgeDetectionToTable(final EdgeDetection edgeDetection) {
        if (Objects.nonNull(edgeDetection) && Objects.nonNull(edgeDetectionsTable)
                && Objects.nonNull(edgeDetectionsTable.getEdgeCluster())) {
            int detectionRow = 0;
            for (int rowIndex = 0; rowIndex < edgeDetectionsTable.getRowCount(); ++rowIndex) {
                if (Objects.nonNull(edgeDetectionsTable.getValueAt(rowIndex, ID_COLUMN))) {
                    final long currentRowId =
                            Long.parseLong(edgeDetectionsTable.getValueAt(rowIndex, ID_COLUMN).toString());
                    if (currentRowId == edgeDetection.getId()) {
                        detectionRow = rowIndex;
                    }
                }
            }
            if (edgeDetectionsTable.getRowCount() > 0) {
                edgeDetectionsTable.setRowSelectionInterval(detectionRow, detectionRow);
                edgeDetectionsTable.notifyRowSelectionObserver(edgeDetection);
            }
        }
    }

    void registerObserver(final EdgeDetectionRowSelectionObserver rowSelectionObserver) {
        this.rowSelectionObserver = rowSelectionObserver;
    }
}