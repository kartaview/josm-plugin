/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.edge.filter;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.openstreetmap.josm.plugins.kartaview.argument.DataType;
import org.openstreetmap.josm.plugins.kartaview.argument.EdgeSearchFilter;
import org.openstreetmap.josm.plugins.kartaview.entity.ConfidenceLevelCategory;
import org.openstreetmap.josm.plugins.kartaview.entity.OsmComparison;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.gui.details.common.DetectionTypeContent;
import org.openstreetmap.josm.plugins.kartaview.gui.details.common.DetectionTypeList;
import org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.filter.Constraints;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;

import com.grab.josm.common.gui.builder.ButtonBuilder;
import com.grab.josm.common.gui.builder.CheckBoxBuilder;
import com.grab.josm.common.gui.builder.ContainerBuilder;
import com.grab.josm.common.gui.builder.LabelBuilder;
import com.grab.josm.common.gui.builder.TextComponentBuilder;


/**
 * Displays the possible data filters for the Edge detections layer.
 *
 * @author maria.mitisor
 */
class EdgeFilterPanel extends JPanel {

    private static final long serialVersionUID = -3476244540432846177L;
    private static final Color FILTERS_LBL_COLOR = new Color(0, 153, 0);
    private static final Color SIGN_FILTERS_LBL_COLOR = new Color(204, 102, 0);
    private static final String EMPTY = "";

    /* data types components */
    private JCheckBox cbbDetections;
    private JCheckBox cbbCluster;

    /* confidence filter */
    private JCheckBox cbbC1ConfidenceCategory;
    private JCheckBox cbbC2ConfidenceCategory;
    private JCheckBox cbbC3ConfidenceCategory;

    /* sign filters */
    private JComboBox<String> detectionRegion;
    private JTextField searchDataTextField;
    private DetectionTypeList detectionTypeList;

    /* osm comparison filters */
    private JCheckBox cbbNewOsmComparison;
    private JCheckBox cbbChangedOsmComparison;
    private JCheckBox cbbUnknownOsmComparison;
    private JCheckBox cbbSameOsmComparison;
    private JCheckBox cbbImpliedOsmComparison;

    /* buttons */
    private JButton btnSelectSignTypes;
    private JButton btnClearSignTypes;

    EdgeFilterPanel() {
        super(new GridBagLayout());
        final EdgeSearchFilter edgeSearchFilter = PreferenceManager.getInstance().loadEdgeSearchFilter();
        addDataTypeFilter(edgeSearchFilter.getDataTypes());
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterCommonLbl(), Font.BOLD, FILTERS_LBL_COLOR, null),
                Constraints.LBL_COMMON_FILTERS);
        addConfidenceCategoryFilter(edgeSearchFilter.getConfidenceCategories());
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterSignLbl(), Font.BOLD, SIGN_FILTERS_LBL_COLOR, null),
                EdgeConstraints.LBL_EDGE_SIGN_FILTERS);
        addRegionFilter(edgeSearchFilter.getRegion());
        addEdgeSearchFilter();
        addDetectionTypeFilter(edgeSearchFilter.getSignTypes(), edgeSearchFilter.getSpecificSigns(), edgeSearchFilter
                .getRegion(), EMPTY);
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterClusterLbl(), Font.BOLD, FILTERS_LBL_COLOR, null),
                EdgeConstraints.LBL_EDGE_CLUSTER_FILTERS);
        addOsmComparisonFilter(edgeSearchFilter.getOsmComparisons());
        enableDetectionFilters(edgeSearchFilter.getDataTypes());
    }

    private void addDataTypeFilter(final List<DataType> dataTypes) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeLbl(), Font.BOLD),
                Constraints.LBL_DATA_TYPE);
        final JPanel dataTypePanel = new JPanel(new GridLayout(1, 2));
        cbbDetections = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeDetectionsTxt(),
                new DataTypeSelectionListener(), Font.PLAIN, Objects.nonNull(dataTypes) && dataTypes.contains(
                        DataType.DETECTION), true);
        dataTypePanel.add(cbbDetections);
        cbbCluster = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeClusterTxt(),
                new DataTypeSelectionListener(), Font.PLAIN, Objects.nonNull(dataTypes) && dataTypes.contains(
                        DataType.CLUSTER), true);
        dataTypePanel.add(cbbCluster);
        add(dataTypePanel, Constraints.DATA_TYPE_PANEL);
    }

    private void addConfidenceCategoryFilter(final List<ConfidenceLevelCategory> confidenceCategories) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterConfidenceCategoryLbl(), Font.BOLD),
                EdgeConstraints.LBL_EDGE_CONFIDENCE_CATEGORY);
        cbbC1ConfidenceCategory = CheckBoxBuilder.build(ConfidenceLevelCategory.C1.toString(), Font.PLAIN, null,
                Objects.nonNull(confidenceCategories) && confidenceCategories.contains(ConfidenceLevelCategory.C1));
        add(cbbC1ConfidenceCategory, EdgeConstraints.EDGE_CONFIDENCE_CATEGORY_CB_C1);
        cbbC2ConfidenceCategory = CheckBoxBuilder.build(ConfidenceLevelCategory.C2.toString(), Font.PLAIN, null,
                Objects.nonNull(confidenceCategories) && confidenceCategories.contains(ConfidenceLevelCategory.C2));
        add(cbbC2ConfidenceCategory, EdgeConstraints.EDGE_CONFIDENCE_CATEGORY_CB_C2);
        cbbC3ConfidenceCategory = CheckBoxBuilder.build(ConfidenceLevelCategory.C3.toString(), Font.PLAIN, null,
                Objects.nonNull(confidenceCategories) && confidenceCategories.contains(ConfidenceLevelCategory.C3));
        add(cbbC3ConfidenceCategory, EdgeConstraints.EDGE_CONFIDENCE_CATEGORY_CB_C3);
    }

    private void addRegionFilter(final String region) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDataRegionLbl(), Font.BOLD),
                EdgeConstraints.LBL_EDGE_SIGN_REGION);
        final List<String> regions = DetectionTypeContent.getInstance().getRegions();
        regions.add(0, EMPTY);
        detectionRegion = new JComboBox<>(regions.toArray(new String[0]));
        if (Objects.nonNull(region)) {
            detectionRegion.setSelectedItem(region);
        }
        detectionRegion.addActionListener(new EdgeFilterPanel.RegionSelectionListener());
        add(detectionRegion, EdgeConstraints.CB_EDGE_SIGN_REGION);
    }

    private void addEdgeSearchFilter() {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterSearchDataExplicationLbl(), Font.ITALIC),
                EdgeConstraints.LBL_EDGE_SEARCH_SIGN_EXPLANATION);
        searchDataTextField = TextComponentBuilder.buildTextField(EMPTY, Font.PLAIN, Color.WHITE);
        searchDataTextField.addKeyListener(new EdgeFilterPanel.SearchSignListener());
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterSearchDataLbl(), Font.BOLD),
                EdgeConstraints.LBL_EDGE_SEARCH_SIGN);
        add(searchDataTextField, EdgeConstraints.TXT_EDGE_SEARCH_SIGN);
    }

    private void addDetectionTypeFilter(final List<String> signTypes, final List<Sign> specificSigns,
            final String region, final String inputText) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDetectionTypeLbl(), Font.BOLD),
                EdgeConstraints.LBL_EDGE_SIGN_TYPE);
        detectionTypeList = new DetectionTypeList();
        detectionTypeList.populateDetectionList(signTypes, specificSigns, region, inputText);
        add(ContainerBuilder.buildScrollPane(detectionTypeList, getBackground()), EdgeConstraints.CBB_EDGE_SIGN_TYPE);
        btnSelectSignTypes = ButtonBuilder.build(new EdgeFilterPanel.SignTypesSelectAction(), GuiConfig.getInstance()
                .getBtnSelectLbl());
        btnClearSignTypes = ButtonBuilder.build(new EdgeFilterPanel.SignTypesClearAction(), GuiConfig.getInstance()
                .getBtnClearLbl());
        final JPanel pnlButton = ContainerBuilder.buildFlowLayoutPanel(FlowLayout.RIGHT, btnSelectSignTypes,
                btnClearSignTypes);
        add(pnlButton, EdgeConstraints.EDGE_PNL_BTN);
    }

    private void addOsmComparisonFilter(final List<OsmComparison> osmComparisons) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterOsmComparisonLbl(), Font.BOLD),
                EdgeConstraints.LBL_EDGE_OSM_COMPARISON);
        final JPanel osmComparisonPanel = new JPanel(new GridLayout(0, 5));
        cbbNewOsmComparison = CheckBoxBuilder.build(OsmComparison.NEW.toString(), Font.PLAIN, null, Objects.nonNull(
                osmComparisons) && osmComparisons.contains(OsmComparison.NEW));
        osmComparisonPanel.add(cbbNewOsmComparison);
        cbbChangedOsmComparison = CheckBoxBuilder.build(OsmComparison.CHANGED.toString(), Font.PLAIN, null, Objects
                .nonNull(osmComparisons) && osmComparisons.contains(OsmComparison.CHANGED));
        osmComparisonPanel.add(cbbChangedOsmComparison);
        cbbUnknownOsmComparison = CheckBoxBuilder.build(OsmComparison.UNKNOWN.toString(), Font.PLAIN, null, Objects
                .nonNull(osmComparisons) && osmComparisons.contains(OsmComparison.UNKNOWN));
        osmComparisonPanel.add(cbbUnknownOsmComparison);
        cbbSameOsmComparison = CheckBoxBuilder.build(OsmComparison.SAME.toString(), Font.PLAIN, null, Objects.nonNull(
                osmComparisons) && osmComparisons.contains(OsmComparison.SAME));
        osmComparisonPanel.add(cbbSameOsmComparison);
        cbbImpliedOsmComparison = CheckBoxBuilder.build(OsmComparison.IMPLIED.toString(), Font.PLAIN, null, Objects
                .nonNull(osmComparisons) && osmComparisons.contains(OsmComparison.IMPLIED));
        osmComparisonPanel.add(cbbImpliedOsmComparison);
        add(osmComparisonPanel, EdgeConstraints.EDGE_OSM_COMPARISON_PANEL);
    }

    private void enableDetectionFilters(final List<DataType> dataTypes) {
        boolean edgeFilters = false;
        boolean edgeClusterFilters = false;

        if (Objects.nonNull(dataTypes)) {
            edgeFilters = dataTypes.contains(DataType.CLUSTER) || dataTypes.contains(DataType.DETECTION);
            edgeClusterFilters = dataTypes.contains(DataType.CLUSTER);
        }

        detectionRegion.setEnabled(edgeFilters);
        searchDataTextField.setEnabled(edgeFilters);
        detectionTypeList.setEnabled(edgeFilters);

        // cluster only filters
        cbbC1ConfidenceCategory.setEnabled(edgeClusterFilters);
        cbbC2ConfidenceCategory.setEnabled(edgeClusterFilters);
        cbbC3ConfidenceCategory.setEnabled(edgeClusterFilters);
        cbbNewOsmComparison.setEnabled(edgeClusterFilters);
        cbbChangedOsmComparison.setEnabled(edgeClusterFilters);
        cbbUnknownOsmComparison.setEnabled(edgeClusterFilters);
        cbbSameOsmComparison.setEnabled(edgeClusterFilters);
        cbbImpliedOsmComparison.setEnabled(edgeClusterFilters);

        btnSelectSignTypes.setEnabled(edgeFilters);
        btnClearSignTypes.setEnabled(edgeFilters);
    }

    /**
     * Returns the currently selected edge filters.
     *
     * @return a {@code EdgeSearchFilter} object
     */
    EdgeSearchFilter selectedEdgeFilters() {
        final List<DataType> dataTypes = selectedDataTypes();
        final List<String> signTypes = detectionTypeList.getSelectedTypes(searchDataTextField.getText());
        final List<Sign> signValues = detectionTypeList.getSelectedValues();
        final String region = selectedRegion();
        return new EdgeSearchFilter(dataTypes, selectedOsmComparisons(), selectedConfidenceCategories(), region,
                signTypes, signValues);
    }

    private List<DataType> selectedDataTypes() {
        final List<DataType> selected = new ArrayList<>();
        if (cbbDetections.isSelected()) {
            selected.add(DataType.DETECTION);
        }
        if (cbbCluster.isSelected()) {
            selected.add(DataType.CLUSTER);
        }
        return selected;
    }

    private String selectedRegion() {
        return Objects.nonNull(detectionRegion.getSelectedItem()) ? detectionRegion.getSelectedItem().toString() : null;
    }

    private List<ConfidenceLevelCategory> selectedConfidenceCategories() {
        final List<ConfidenceLevelCategory> confidenceCategories = new ArrayList<>();
        if (cbbC1ConfidenceCategory.isSelected()) {
            confidenceCategories.add(ConfidenceLevelCategory.C1);
        }
        if (cbbC2ConfidenceCategory.isSelected()) {
            confidenceCategories.add(ConfidenceLevelCategory.C2);
        }
        if (cbbC3ConfidenceCategory.isSelected()) {
            confidenceCategories.add(ConfidenceLevelCategory.C3);
        }
        return confidenceCategories;
    }

    private List<OsmComparison> selectedOsmComparisons() {
        final List<OsmComparison> osmComparisons = new ArrayList<>();
        if (cbbNewOsmComparison.isSelected()) {
            osmComparisons.add(OsmComparison.NEW);
        }
        if (cbbChangedOsmComparison.isSelected()) {
            osmComparisons.add(OsmComparison.CHANGED);
        }
        if (cbbUnknownOsmComparison.isSelected()) {
            osmComparisons.add(OsmComparison.UNKNOWN);
        }
        if (cbbSameOsmComparison.isSelected()) {
            osmComparisons.add(OsmComparison.SAME);
        }
        if (cbbImpliedOsmComparison.isSelected()) {
            osmComparisons.add(OsmComparison.IMPLIED);
        }
        return osmComparisons;
    }

    /**
     * Clears the filters.
     */
    void resetFilters() {
        cbbDetections.setSelected(EdgeSearchFilter.DEFAULT.getDataTypes().contains(DataType.DETECTION));
        cbbCluster.setSelected(EdgeSearchFilter.DEFAULT.getDataTypes().contains(DataType.CLUSTER));

        boolean c1ConfidenceCategorySelected = false;
        boolean c2ConfidenceCategorySelected = false;
        boolean c3ConfidenceCategorySelected = false;

        boolean newOsmComparisonSelected = false;
        boolean changedOsmComparisonSelected = false;
        boolean unknownOsmComparisonSelected = false;
        boolean sameOsmComparisonSelected = false;
        boolean impliedOsmComparisonSelected = false;

        if (Objects.nonNull(EdgeSearchFilter.DEFAULT.getConfidenceCategories())) {
            c1ConfidenceCategorySelected = EdgeSearchFilter.DEFAULT.getConfidenceCategories()
                    .contains(ConfidenceLevelCategory.C1);
            c2ConfidenceCategorySelected = EdgeSearchFilter.DEFAULT.getConfidenceCategories()
                    .contains(ConfidenceLevelCategory.C2);
            c3ConfidenceCategorySelected = EdgeSearchFilter.DEFAULT.getConfidenceCategories()
                    .contains(ConfidenceLevelCategory.C3);
        }

        cbbC1ConfidenceCategory.setSelected(c1ConfidenceCategorySelected);
        cbbC2ConfidenceCategory.setSelected(c2ConfidenceCategorySelected);
        cbbC3ConfidenceCategory.setSelected(c3ConfidenceCategorySelected);

        detectionRegion.setSelectedIndex(0);
        detectionTypeList.clearSelection();

        if (Objects.nonNull(EdgeSearchFilter.DEFAULT.getOsmComparisons())) {
            newOsmComparisonSelected = EdgeSearchFilter.DEFAULT.getOsmComparisons().contains(
                    OsmComparison.NEW);
            changedOsmComparisonSelected = EdgeSearchFilter.DEFAULT.getOsmComparisons().contains(
                    OsmComparison.CHANGED);
            unknownOsmComparisonSelected = EdgeSearchFilter.DEFAULT.getOsmComparisons().contains(
                    OsmComparison.UNKNOWN);
            sameOsmComparisonSelected = EdgeSearchFilter.DEFAULT.getOsmComparisons().contains(
                    OsmComparison.SAME);
            impliedOsmComparisonSelected = EdgeSearchFilter.DEFAULT.getOsmComparisons().contains(
                    OsmComparison.IMPLIED);
        }
        cbbNewOsmComparison.setSelected(newOsmComparisonSelected);
        cbbChangedOsmComparison.setSelected(changedOsmComparisonSelected);
        cbbUnknownOsmComparison.setSelected(unknownOsmComparisonSelected);
        cbbSameOsmComparison.setSelected(sameOsmComparisonSelected);
        cbbImpliedOsmComparison.setSelected(impliedOsmComparisonSelected);

        enableDetectionFilters(EdgeSearchFilter.DEFAULT.getDataTypes());
    }

    private final class DataTypeSelectionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            final List<DataType> dataTypes = selectedDataTypes();
            enableDetectionFilters(dataTypes);
        }
    }


    private final class SignTypesSelectAction extends AbstractAction {

        private static final long serialVersionUID = -7171771571524168530L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            detectionTypeList.selectAll();
        }
    }


    private final class SignTypesClearAction extends AbstractAction {

        private static final long serialVersionUID = -8589369992232950474L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            detectionTypeList.clearSelection();
        }
    }


    private final class RegionSelectionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            detectionTypeList.populateDetectionList(null, null, detectionRegion.getSelectedItem().toString(),
                    searchDataTextField.getText());
        }
    }


    private final class SearchSignListener implements KeyListener {

        @Override
        public void keyTyped(final KeyEvent e) {
        }

        @Override
        public void keyPressed(final KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                detectionTypeList.populateDetectionList(null, null, detectionRegion.getSelectedItem().toString(),
                        searchDataTextField.getText());
            }
        }

        @Override
        public void keyReleased(final KeyEvent e) {
        }
    }
}