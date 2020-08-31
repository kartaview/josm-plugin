/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.swingx.JXDatePicker;
import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ConfidenceLevelFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.DetectionFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.grab.josm.common.formatter.DateFormatter;
import com.grab.josm.common.gui.builder.ButtonBuilder;
import com.grab.josm.common.gui.builder.CheckBoxBuilder;
import com.grab.josm.common.gui.builder.ContainerBuilder;
import com.grab.josm.common.gui.builder.DatePickerBuilder;
import com.grab.josm.common.gui.builder.LabelBuilder;
import com.grab.josm.common.gui.builder.TextComponentBuilder;
import com.grab.josm.common.gui.verifier.AbstractDateVerifier;


/**
 * Displays the possible data filters.
 *
 * @author Beata
 * @version $Revision$
 */
class FilterPanel extends JPanel {

    private static final long serialVersionUID = -4229411104270361299L;
    private static final Dimension PICKER_SIZE = new Dimension(120, 20);

    /* panel components */
    private JXDatePicker pickerDate;
    private JCheckBox cbbUser;

    private JCheckBox cbbPhotos;
    private JCheckBox cbbDetections;
    private JCheckBox cbbCluster;

    private JCheckBox cbbAutomaticMode;
    private JCheckBox cbbManualMode;

    /* edit status filter */
    private JCheckBox cbbOpenEditStatus;
    private JCheckBox cbbMappedEditStatus;
    private JCheckBox cbbBadSignEditStatus;
    private JCheckBox cbbOtherEditStatus;

    /* osm comparison filter */
    private JCheckBox cbbNewOsmComparison;
    private JCheckBox cbbChangedOsmComparison;
    private JCheckBox cbbUnknownOsmComparison;
    private JCheckBox cbbSameOsmComparison;
    private JCheckBox cbbImpliedOsmComparison;

    /* confidence filter */
    private JTextField minConfidenceLvl;
    private JTextField maxConfidenceLvl;

    private DetectionTypeList detectionTypeList;
    private JComboBox<String> detectionRegion;
    private JButton btnSelectSignTypes;
    private JButton btnClearSignTypes;
    private JTextField searchDataTextField;
    private final boolean isHighZoomLevel;


    FilterPanel(final boolean isHighZoomLevel) {
        super(new GridBagLayout());
        this.isHighZoomLevel = isHighZoomLevel;
        final SearchFilter filter = PreferenceManager.getInstance().loadSearchFilter();
        if (isHighZoomLevel) {
            addDataTypeFilter(filter.getDataTypes());
            addUserFilter(filter.isOlnyUserData(), filter.getDataTypes());
            addDateFilter(filter.getDate());
            add(new JSeparator(JSeparator.HORIZONTAL), Constraints.SEPARATOR);
            add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDetectionLbl(), Font.BOLD),
                    Constraints.LBL_DETECTION);
            addModeFilter(filter.getDetectionFilter().getModes());
            addEditStatusFilter(filter.getDetectionFilter().getEditStatuses());
            addOsmComparisonFilter(filter.getDetectionFilter().getOsmComparisons());
            addConfidenceLevelFilter(filter.getDetectionFilter().getConfidenceLevelFilter());
            addRegionFilter(filter.getDetectionFilter().getRegion());
            addSearchFilter();
            addDetectionTypeFilter(filter.getDetectionFilter().getSignTypes(),
                    filter.getDetectionFilter().getSpecificSigns(), filter.getDetectionFilter().getRegion(), "");
            enableDetectionFilters(filter.getDataTypes());
        } else {
            addUserFilter(filter.isOlnyUserData());
            addDateFilter(filter.getDate());
        }
        if (DataSet.getInstance().getSelectedSequence() != null) {
            pickerDate.setEnabled(false);
            cbbUser.setEnabled(false);
        }
    }

    private void addDataTypeFilter(final List<DataType> types) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeLbl(), Font.BOLD),
                Constraints.LBL_DATA_TYPE);
        final JPanel dataTypePanel = new JPanel(new GridLayout(1, 3));
        cbbPhotos = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeImageTxt(),
                new DataTypeSelectionListener(), Font.PLAIN, null, types != null && types.contains(DataType.PHOTO));
        dataTypePanel.add(cbbPhotos);
        cbbDetections = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeDetectionsTxt(),
                new DataTypeSelectionListener(), Font.PLAIN, types != null && types.contains(DataType.DETECTION), true);
        dataTypePanel.add(cbbDetections);
        cbbCluster = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeAggregatedDetectionsTxt(),
                new DataTypeSelectionListener(), Font.PLAIN, types != null && types.contains(DataType.CLUSTER), true);
        dataTypePanel.add(cbbCluster);
        add(dataTypePanel, Constraints.DATA_TYPE_PANEL);
    }

    private void addUserFilter(final boolean isSelected) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterUserLbl(), Font.BOLD),
                Constraints.getLblUser(isHighZoomLevel));
        final JLabel lblLoginWarning =
                LabelBuilder.build(GuiConfig.getInstance().getDlgFilterLoginWarningLbl(), Font.ITALIC);
        boolean enabled = true;
        if (UserIdentityManager.getInstance().asUser().getId() <= 0) {
            lblLoginWarning.setForeground(Color.red);
            add(lblLoginWarning, Constraints.geLblLoginWarning(isHighZoomLevel));
            enabled = false;
        }
        cbbUser = CheckBoxBuilder.build(Font.PLAIN, isSelected, enabled);
        add(cbbUser, Constraints.getCbbUser(isHighZoomLevel));
    }

    private void addUserFilter(final boolean isSelected, final List<DataType> dataTypes) {
        addUserFilter(isSelected);
        if (dataTypes != null && dataTypes.size() == 1 && dataTypes.get(0).equals(DataType.CLUSTER)) {
            cbbUser.setEnabled(false);
        }
    }

    private void addDateFilter(final Date date) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDateLbl(), Font.BOLD),
                Constraints.getLblDate(isHighZoomLevel));
        pickerDate = DatePickerBuilder.build(date, Calendar.getInstance().getTime(), PICKER_SIZE);
        pickerDate.getEditor().addKeyListener(new DateVerifier(pickerDate));
        if (Util.zoom(MainApplication.getMap().mapView.getRealBounds()) < Config.getInstance().getMapPhotoZoom()) {
            pickerDate.setEnabled(false);
        }
        add(pickerDate, Constraints.getPickerDate(isHighZoomLevel));
    }

    private void addModeFilter(final List<DetectionMode> modes) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterModeLbl(), Font.BOLD), Constraints.LBL_MODE);
        final JPanel modePanel = new JPanel(new GridLayout(0, 4));
        cbbAutomaticMode = CheckBoxBuilder.build(DetectionMode.AUTOMATIC.toString(), Font.PLAIN, null,
                modes != null && modes.contains(DetectionMode.AUTOMATIC));
        modePanel.add(cbbAutomaticMode);
        cbbManualMode = CheckBoxBuilder.build(DetectionMode.MANUAL.toString(), Font.PLAIN, null,
                modes != null && modes.contains(DetectionMode.MANUAL));
        modePanel.add(cbbManualMode);
        add(modePanel, Constraints.MODE_PANEL);
    }

    private void addEditStatusFilter(final List<EditStatus> editStatuses) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterEditStatusLbl(), Font.BOLD),
                Constraints.LBL_EDIT_STATUS);
        final JPanel editStatusPanel = new JPanel(new GridLayout(1, 4));
        cbbOpenEditStatus = CheckBoxBuilder.build(EditStatus.OPEN.toString(), Font.PLAIN, null,
                editStatuses != null && editStatuses.contains(EditStatus.OPEN));
        editStatusPanel.add(cbbOpenEditStatus);
        cbbMappedEditStatus = CheckBoxBuilder.build(EditStatus.MAPPED.toString(), Font.PLAIN, null,
                editStatuses != null && editStatuses.contains(EditStatus.MAPPED));
        editStatusPanel.add(cbbMappedEditStatus);
        cbbBadSignEditStatus = CheckBoxBuilder.build(EditStatus.BAD_SIGN.toString(), Font.PLAIN, null,
                editStatuses != null && editStatuses.contains(EditStatus.BAD_SIGN));
        editStatusPanel.add(cbbBadSignEditStatus);
        cbbOtherEditStatus = CheckBoxBuilder.build(EditStatus.OTHER.toString(), Font.PLAIN, null,
                editStatuses != null && editStatuses.contains(EditStatus.OTHER));
        editStatusPanel.add(cbbOtherEditStatus);
        add(editStatusPanel, Constraints.EDIT_STATUS_PANEL);
    }

    private void addOsmComparisonFilter(final List<OsmComparison> osmComparisons) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterOsmComparisonLbl(), Font.BOLD),
                Constraints.LBL_OSM_COMPARISON);
        final JPanel osmComparisonPanel = new JPanel(new GridLayout(0, 4));
        cbbNewOsmComparison = CheckBoxBuilder.build(OsmComparison.NEW.toString(), Font.PLAIN, null,
                osmComparisons != null && osmComparisons.contains(OsmComparison.NEW));
        osmComparisonPanel.add(cbbNewOsmComparison);
        cbbChangedOsmComparison = CheckBoxBuilder.build(OsmComparison.CHANGED.toString(), Font.PLAIN, null,
                osmComparisons != null && osmComparisons.contains(OsmComparison.CHANGED));
        osmComparisonPanel.add(cbbChangedOsmComparison);
        cbbUnknownOsmComparison = CheckBoxBuilder.build(OsmComparison.UNKNOWN.toString(), Font.PLAIN, null,
                osmComparisons != null && osmComparisons.contains(OsmComparison.UNKNOWN));
        osmComparisonPanel.add(cbbUnknownOsmComparison);
        cbbSameOsmComparison = CheckBoxBuilder.build(OsmComparison.SAME.toString(), Font.PLAIN, null,
                osmComparisons != null && osmComparisons.contains(OsmComparison.SAME));
        osmComparisonPanel.add(cbbSameOsmComparison);
        cbbImpliedOsmComparison = CheckBoxBuilder.build(OsmComparison.IMPLIED.toString(), Font.PLAIN, null,
                osmComparisons != null && osmComparisons.contains(OsmComparison.IMPLIED));
        osmComparisonPanel.add(cbbImpliedOsmComparison);
        add(osmComparisonPanel, Constraints.OSM_COMPARISON_PANEL);
    }

    private void addConfidenceLevelFilter(final ConfidenceLevelFilter confidenceLevelFilter) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterConfidenceLbl(), Font.BOLD),
                Constraints.LBL_CONFIDENCE_LEVEL);
        final JPanel confidencePanel = new JPanel(new GridBagLayout());
        confidencePanel.add(
                LabelBuilder.build(GuiConfig.getInstance().getDlgFilterConfidenceMinLbl(), Font.PLAIN, Color.RED),
                Constraints.LBL_MIN_CONFIDENCE_LEVEL);
        final String min = confidenceLevelFilter.getMinConfidenceLevel() != null
                ? confidenceLevelFilter.getMinConfidenceLevel().toString() : "";
        minConfidenceLvl = TextComponentBuilder.buildTextField(min, Font.PLAIN, Color.WHITE);
        minConfidenceLvl.getDocument().addDocumentListener(new ConfidenceTextFieldListener(minConfidenceLvl));
        confidencePanel.add(minConfidenceLvl, Constraints.TXT_MIN_CONFIDENCE_LEVEL);
        confidencePanel.add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterConfidenceMaxLbl(), Font.PLAIN),
                Constraints.LBL_MAX_CONFIDENCE_LEVEL);
        final String max = confidenceLevelFilter.getMaxConfidenceLevel() != null
                ? confidenceLevelFilter.getMaxConfidenceLevel().toString() : "";
        maxConfidenceLvl = TextComponentBuilder.buildTextField(max, Font.PLAIN, Color.WHITE);
        maxConfidenceLvl.getDocument().addDocumentListener(new ConfidenceTextFieldListener(maxConfidenceLvl));
        confidencePanel.add(maxConfidenceLvl, Constraints.TXT_MAX_CONFIDENCE_LEVEL);
        add(confidencePanel, Constraints.CONFIDENCE_LEVEL_PANEL);
    }

    private void addRegionFilter(final String region) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDataRegionLbl(), Font.BOLD),
                Constraints.LBL_SIGN_REGION);
        final List<String> regions = DetectionTypeContent.getInstance().getRegions();
        regions.add(0, "");
        detectionRegion = new JComboBox<>(regions.toArray(new String[0]));
        if (region != null) {
            detectionRegion.setSelectedItem(region);
        }
        detectionRegion.addActionListener(new RegionSelectionListener());
        add(detectionRegion, Constraints.CB_SIGN_REGION);
    }

    private void addSearchFilter() {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterSearchDataExplicationLbl(), Font.ITALIC),
                Constraints.LBL_SEARCH_SIGN_EXPLANATION);
        searchDataTextField = TextComponentBuilder.buildTextField("", Font.PLAIN, Color.WHITE);
        searchDataTextField.addKeyListener(new SearchSignListener());
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterSearchDataLbl(), Font.BOLD),
                Constraints.LBL_SEARCH_SIGN);
        add(searchDataTextField, Constraints.TXT_SEARCH_SIGN);
    }

    private void addDetectionTypeFilter(final List<String> signTypes, final List<Sign> specificSigns,
            final String region, final String inputText) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDetectionTypeLbl(), Font.BOLD),
                Constraints.LBL_SIGN_TYPE);
        detectionTypeList = new DetectionTypeList();
        detectionTypeList.populateDetectionList(signTypes, specificSigns, region, inputText);
        add(ContainerBuilder.buildScrollPane(detectionTypeList, getBackground()), Constraints.CBB_SIGN_TYPE);
        btnSelectSignTypes =
                ButtonBuilder.build(new SignTypesSelectAction(), GuiConfig.getInstance().getBtnSelectLbl());
        btnClearSignTypes = ButtonBuilder.build(new SignTypesClearAction(), GuiConfig.getInstance().getBtnClearLbl());
        final JPanel pnlButton =
                ContainerBuilder.buildFlowLayoutPanel(FlowLayout.RIGHT, btnSelectSignTypes, btnClearSignTypes);
        add(pnlButton, Constraints.PNL_BTN);
    }

    private void enableDetectionFilters(final List<DataType> dataTypes) {
        boolean enableCommonFilters = false;
        boolean enableDetectionFilters = false;
        boolean enableClusterFilters = false;
        boolean enableUserFilter = false;
        if (dataTypes != null) {
            enableCommonFilters = dataTypes.contains(DataType.CLUSTER) || dataTypes.contains(DataType.DETECTION);
            enableDetectionFilters = dataTypes.contains(DataType.DETECTION);
            enableClusterFilters = dataTypes.contains(DataType.CLUSTER);
            enableUserFilter = !(dataTypes.contains(DataType.CLUSTER) && (dataTypes.size() == 1));
        }

        // common filters
        cbbNewOsmComparison.setEnabled(enableCommonFilters);
        cbbChangedOsmComparison.setEnabled(enableCommonFilters);
        cbbUnknownOsmComparison.setEnabled(enableCommonFilters);
        cbbSameOsmComparison.setEnabled(enableCommonFilters);
        cbbImpliedOsmComparison.setEnabled(enableCommonFilters);
        detectionTypeList.setEnabled(enableCommonFilters);
        btnSelectSignTypes.setEnabled(enableCommonFilters);
        btnClearSignTypes.setEnabled(enableCommonFilters);
        detectionRegion.setEnabled(enableCommonFilters);
        searchDataTextField.setEnabled(enableCommonFilters);

        //user filter
        cbbUser.setEnabled(enableUserFilter);

        // detection only filters
        cbbAutomaticMode.setEnabled(enableDetectionFilters);
        cbbManualMode.setEnabled(enableDetectionFilters);
        cbbOpenEditStatus.setEnabled(enableDetectionFilters);
        cbbMappedEditStatus.setEnabled(enableDetectionFilters);
        cbbBadSignEditStatus.setEnabled(enableDetectionFilters);
        cbbOtherEditStatus.setEnabled(enableDetectionFilters);

        // cluster only filters
        minConfidenceLvl.setEnabled(enableClusterFilters);
        maxConfidenceLvl.setEnabled(enableClusterFilters);
    }


    /**
     * Returns the currently selected filters, considering also the uncommitted date case.
     *
     * @return a {@code ListFilter} object
     */
    SearchFilter selectedFilters() {
        final String dateValue = pickerDate.getEditor().getText();
        Date date;
        if (dateValue == null || dateValue.trim().isEmpty()) {
            date = null;
        } else {
            date = DateFormatter.parseDay(dateValue.trim());
            if (date == null) {
                // date value was invalid
                JOptionPane.showMessageDialog(this, GuiConfig.getInstance().getIncorrectDateFilterText(),
                        GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
            } else if (date.compareTo(Calendar.getInstance().getTime()) > 0) {
                // date is to big
                JOptionPane.showMessageDialog(this, GuiConfig.getInstance().getUnacceptedDateFilterText(),
                        GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }
        SearchFilter searchFilter;
        if (isHighZoomLevel) {
            final List<DataType> dataTypes = selectedDataTypes();
            final List<EditStatus> editStatuses = selectedEditStatuses();
            final List<DetectionMode> detectionModes = selectedModes();
            final List<String> signTypes = detectionTypeList.getSelectedTypes(searchDataTextField.getText());
            final List<Sign> signValues = detectionTypeList.getSelectedValues();
            final String region = selectedRegion();
            final ConfidenceLevelFilter confidenceLevelFilter = selectedConfidenceLevel();
            searchFilter = new SearchFilter(date, cbbUser.isSelected(), dataTypes,
                    new DetectionFilter(selectedOsmComparisons(), editStatuses, signTypes, signValues, detectionModes,
                            region, confidenceLevelFilter, null, null));
        } else {
            searchFilter = new SearchFilter(date, cbbUser.isSelected());
        }
        return searchFilter;
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

    private List<EditStatus> selectedEditStatuses() {
        final List<EditStatus> editStatuses = new ArrayList<>();
        if (cbbOpenEditStatus.isSelected()) {
            editStatuses.add(EditStatus.OPEN);
        }
        if (cbbMappedEditStatus.isSelected()) {
            editStatuses.add(EditStatus.MAPPED);
        }
        if (cbbBadSignEditStatus.isSelected()) {
            editStatuses.add(EditStatus.BAD_SIGN);
        }
        if (cbbOtherEditStatus.isSelected()) {
            editStatuses.add(EditStatus.OTHER);
        }
        return editStatuses;
    }

    private List<DataType> selectedDataTypes() {
        final List<DataType> selected = new ArrayList<>();
        if (cbbPhotos.isSelected()) {
            selected.add(DataType.PHOTO);
        }
        if (cbbDetections.isSelected()) {
            selected.add(DataType.DETECTION);
        }
        if (cbbCluster.isSelected()) {
            selected.add(DataType.CLUSTER);
        }
        return selected;
    }

    private List<DetectionMode> selectedModes() {
        final List<DetectionMode> selectedModes = new ArrayList<>();
        if (cbbAutomaticMode.isSelected()) {
            selectedModes.add(DetectionMode.AUTOMATIC);
        }
        if (cbbManualMode.isSelected()) {
            selectedModes.add(DetectionMode.MANUAL);
        }
        return selectedModes;
    }

    private String selectedRegion() {
        String region = null;
        if (detectionRegion.getSelectedItem() != null) {
            region = detectionRegion.getSelectedItem().toString();
        }
        return region;
    }

    private ConfidenceLevelFilter selectedConfidenceLevel() {
        ConfidenceLevelFilter result;
        Double minConfidence = null;
        Double maxConfidence = null;

        if (minConfidenceLvl.getText() != null && !minConfidenceLvl.getText().isEmpty()) {
            minConfidence = Double.parseDouble(minConfidenceLvl.getText());
        }
        if (maxConfidenceLvl.getText() != null && !maxConfidenceLvl.getText().isEmpty()) {
            maxConfidence = Double.parseDouble(maxConfidenceLvl.getText());
        }
        if (minConfidence != null && maxConfidence != null && minConfidence > maxConfidence) {
            result = new ConfidenceLevelFilter(maxConfidence, minConfidence);
        } else {
            result = new ConfidenceLevelFilter(minConfidence, maxConfidence);
        }
        return result;
    }

    /**
     * Clears the filters.
     */
    void resetFilters() {
        pickerDate.getEditor().setText("");
        pickerDate.setDate(SearchFilter.DEFAULT.getDate());
        cbbUser.setSelected(SearchFilter.DEFAULT.isOlnyUserData());
        if (isHighZoomLevel) {
            cbbPhotos.setSelected(SearchFilter.DEFAULT.getDataTypes().contains(DataType.PHOTO));
            cbbDetections.setSelected(SearchFilter.DEFAULT.getDataTypes().contains(DataType.DETECTION));
            cbbCluster.setSelected(SearchFilter.DEFAULT.getDataTypes().contains(DataType.CLUSTER));
            boolean newOsmComparisonSelected = false;
            boolean changedOsmComparisonSelected = false;
            boolean unknownOsmComparisonSelected = false;
            boolean sameOsmComparisonSelected = false;
            boolean impliedOsmComparisonSelected = false;
            if (SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons() != null) {
                newOsmComparisonSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons().contains(OsmComparison.NEW);
                changedOsmComparisonSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons().contains(OsmComparison.CHANGED);
                unknownOsmComparisonSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons().contains(OsmComparison.UNKNOWN);
                sameOsmComparisonSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons().contains(OsmComparison.SAME);
                impliedOsmComparisonSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons().contains(OsmComparison.IMPLIED);
            }
            cbbNewOsmComparison.setSelected(newOsmComparisonSelected);
            cbbChangedOsmComparison.setSelected(changedOsmComparisonSelected);
            cbbUnknownOsmComparison.setSelected(unknownOsmComparisonSelected);
            cbbSameOsmComparison.setSelected(sameOsmComparisonSelected);
            cbbImpliedOsmComparison.setSelected(impliedOsmComparisonSelected);
            minConfidenceLvl.setText("");
            maxConfidenceLvl.setText("");

            boolean openEditStatusSelected = false;
            boolean mappedEditStatusSelected = false;
            boolean badEditStatusSelected = false;
            boolean otherEditStatusSelected = false;
            if (SearchFilter.DEFAULT.getDetectionFilter().getEditStatuses() != null) {
                openEditStatusSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getEditStatuses().contains(EditStatus.OPEN);
                mappedEditStatusSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getEditStatuses().contains(EditStatus.MAPPED);
                badEditStatusSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getEditStatuses().contains(EditStatus.BAD_SIGN);
                otherEditStatusSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getEditStatuses().contains(EditStatus.OTHER);
            }
            cbbOpenEditStatus.setSelected(openEditStatusSelected);
            cbbMappedEditStatus.setSelected(mappedEditStatusSelected);
            cbbBadSignEditStatus.setSelected(badEditStatusSelected);
            cbbOtherEditStatus.setSelected(otherEditStatusSelected);
            detectionRegion.setSelectedIndex(0);
            detectionTypeList.clearSelection();
            cbbAutomaticMode.setSelected(false);
            cbbManualMode.setSelected(false);
            enableDetectionFilters(SearchFilter.DEFAULT.getDataTypes());
        }
    }

    private final class DateVerifier extends AbstractDateVerifier {

        private DateVerifier(final JXDatePicker component) {
            super(component);
        }

        @Override
        public boolean validate() {
            boolean isValid = true;
            final String valueStr = getTextFieldValue();
            if (!valueStr.isEmpty()) {
                final Date newDate = DateFormatter.parseDay(valueStr);
                if (newDate == null) {
                    JOptionPane.showMessageDialog(FilterPanel.this,
                            GuiConfig.getInstance().getIncorrectDateFilterText(),
                            GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
                    isValid = false;
                } else if (newDate.compareTo(Calendar.getInstance().getTime()) > 0) {
                    JOptionPane.showMessageDialog(FilterPanel.this,
                            GuiConfig.getInstance().getUnacceptedDateFilterText(),
                            GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
                    isValid = false;
                }
            }
            return isValid;
        }
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

    private final class ConfidenceTextFieldListener implements DocumentListener {

        private final JTextField confidenceLevel;

        ConfidenceTextFieldListener(final JTextField confidenceLevel) {
            super();
            this.confidenceLevel = confidenceLevel;
        }

        @Override
        public void insertUpdate(final DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(final DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(final DocumentEvent e) {
            update();
        }

        private void update() {
            if (confidenceLevel.getText() != null && !confidenceLevel.getText().isEmpty()) {
                try {
                    final Double confidenceLvl = Double.parseDouble(confidenceLevel.getText());
                    if (confidenceLvl < 0 || confidenceLvl > 1) {
                        SwingUtilities.invokeLater(() -> {
                            confidenceLevel.setText("");
                            JOptionPane.showMessageDialog(confidenceLevel.getParent(),
                                    GuiConfig.getInstance().getUnacceptedConfidenceFilterText(),
                                    GuiConfig.getInstance().getWarningTitle(), JOptionPane.WARNING_MESSAGE);
                        });
                    }
                } catch (final NumberFormatException ex) {
                    SwingUtilities.invokeLater(() -> {
                        confidenceLevel.setText("");
                        JOptionPane.showMessageDialog(confidenceLevel.getParent(),
                                GuiConfig.getInstance().getIncorrectConfidenceFilterText(),
                                GuiConfig.getInstance().getWarningTitle(), JOptionPane.WARNING_MESSAGE);
                    });
                }
            }
        }
    }
}