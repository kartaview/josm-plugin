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
import java.util.List;
import java.util.Objects;
import org.openstreetmap.josm.plugins.kartaview.entity.Attribute;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgeDetection;
import org.openstreetmap.josm.plugins.kartaview.entity.EdgePhoto;
import org.openstreetmap.josm.plugins.kartaview.gui.details.common.BaseDetailsPanel;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import com.grab.josm.common.formatter.DateFormatter;


/**
 * Displays the information of an Edge detection.
 *
 * @author maria.mitisor
 */
class EdgeDetectionDetailsPanel extends BaseDetailsPanel<EdgeDetection> {

    private static final long serialVersionUID = 5842933383198993565L;

    private static final int INFO_TO_TABLE_EXTRA_HEIGHT = 5;
    private static final int HEADER_TO_CONTENT_EXTRA_HEIGHT = 5;
    private static final int TABLE_END_EXTRA_HEIGHT = 50;
    private static final int LINE_HEIGHT = 25;
    private static final int ROW_HEIGHT = 15;
    private static final int PHOTO_INFO_LBL_X = 15;

    @Override
    public void createComponents(final EdgeDetection edgeDetection) {
        final int widthLbl = getMaxWidth(getFontMetrics(getFont().deriveFont(Font.BOLD)),
                GuiConfig.getInstance().getEdgeDetectionIdLbl(),
                GuiConfig.getInstance().getEdgeDetectionGeneratedAtLbl(),
                GuiConfig.getInstance().getEdgeDetectionSavedAtLbl(), GuiConfig.getInstance().getEdgeDetectionSignLbl(),
                GuiConfig.getInstance().getEdgeDetectionAlgorithmLbl(),
                GuiConfig.getInstance().getEdgeDetectionConfidenceLevelLbl(),
                GuiConfig.getInstance().getEdgeDetectionTrackingIdLbl(),
                GuiConfig.getInstance().getEdgeDetectionExtendedIdLbl(),
                GuiConfig.getInstance().getDetectionFacingText(),
                GuiConfig.getInstance().getEdgeDetectionPhotoMetadataLbl(),
                GuiConfig.getInstance().getEdgeDetectionDeviceLbl(), GuiConfig.getInstance().getEdgeDetectionSizeLbl(),
                GuiConfig.getInstance().getEdgeDetectionSensorHeadingLbl(),
                GuiConfig.getInstance().getEdgeDetectionGpsAccuracyLbl(),
                GuiConfig.getInstance().getEdgeDetectionHorizontalFieldOfViewLbl(),
                GuiConfig.getInstance().getEdgeDetectionVerticalFieldOfViewLbl(),
                GuiConfig.getInstance().getEdgeDetectionProjectionTypeLbl(),
                GuiConfig.getInstance().getEdgeDetectionCustomAttributesLbl()) + PHOTO_INFO_LBL_X;

        addDetectionInformation(edgeDetection, widthLbl);
        addPhotoMetadataInformation(edgeDetection.getPhotoMetadata(), widthLbl);
        addCustomAttributesInformation(edgeDetection.getCustomAttributes(), widthLbl);

        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, pnlHeight));
    }

    private void addDetectionInformation(EdgeDetection edgeDetection, int widthLbl) {
        addInformation(GuiConfig.getInstance().getEdgeDetectionIdLbl(), edgeDetection.getId(), widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionGeneratedAtLbl(),
                DateFormatter.formatMillisecondTimestamp(edgeDetection.getGenerationTimestamp()), widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionSavedAtLbl(),
                DateFormatter.formatTimestamp(edgeDetection.getCreationTimestamp()), widthLbl);
        addSignIcon(GuiConfig.getInstance().getEdgeDetectionSignLbl(), edgeDetection.getSign(), widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionAlgorithmLbl(), edgeDetection.getAlgorithm(), widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionConfidenceLevelLbl(), edgeDetection.getConfidenceLevel(),
                widthLbl);
        addInformation(GuiConfig.getInstance().getDetectionFacingText(), edgeDetection.getFacing(), widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionTrackingIdLbl(), edgeDetection.getTrackingId(),
                widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionExtendedIdLbl(), edgeDetection.getExtendedId(),
                widthLbl);
    }

    private void addPhotoMetadataInformation(EdgePhoto edgePhoto, final int widthLbl) {
        addLabel(GuiConfig.getInstance().getEdgeDetectionPhotoMetadataLbl(), widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionDeviceLbl(), edgePhoto.getDevice(), PHOTO_INFO_LBL_X,
                widthLbl);

        if (Objects.nonNull(edgePhoto.getSize())) {
            addInformation(GuiConfig.getInstance().getEdgeDetectionSizeLbl(), edgePhoto.getSize().toString(),
                    PHOTO_INFO_LBL_X, widthLbl);
        }
        addInformation(GuiConfig.getInstance().getEdgeDetectionSensorHeadingLbl(), edgePhoto.getSensorHeading(),
                PHOTO_INFO_LBL_X, widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionGpsAccuracyLbl(), edgePhoto.getGpsAccuracy(),
                PHOTO_INFO_LBL_X, widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionHorizontalFieldOfViewLbl(),
                edgePhoto.getHorizontalFieldOfView(), PHOTO_INFO_LBL_X, widthLbl);
        addInformation(GuiConfig.getInstance().getEdgeDetectionVerticalFieldOfViewLbl(),
                edgePhoto.getVerticalFieldOfView(), PHOTO_INFO_LBL_X, widthLbl);
        if (Objects.nonNull(edgePhoto.getProjectionType())) {
            addInformation(GuiConfig.getInstance().getEdgeDetectionProjectionTypeLbl(), edgePhoto.getProjectionType(),
                    PHOTO_INFO_LBL_X, widthLbl);
        }
    }

    private void addCustomAttributesInformation(final List<Attribute> customAttributes, int widthLbl) {
        if (Objects.nonNull(customAttributes)) {
            addLabel(GuiConfig.getInstance().getEdgeDetectionCustomAttributesLbl(), widthLbl);
            final AttributeTable attributeTable = new AttributeTable(customAttributes);
            final int tableWidth = attributeTable.getTableWidth();

            attributeTable.getTableHeader().setBounds(new Rectangle(0, getPnlY() + INFO_TO_TABLE_EXTRA_HEIGHT,
                    Math.max(tableWidth, getPnlWidth()), LINE_HEIGHT));

            add(attributeTable.getTableHeader());
            final int heightTableContent = customAttributes.size() * ROW_HEIGHT;
            attributeTable.setBounds(new Rectangle(0, getPnlY() + HEADER_TO_CONTENT_EXTRA_HEIGHT + LINE_HEIGHT,
                    Math.max(tableWidth, getPnlWidth()), heightTableContent));
            add(attributeTable);
            setPnlY(getPnlY() + heightTableContent + TABLE_END_EXTRA_HEIGHT);
            setPnlWidth(tableWidth);
        }
    }
}