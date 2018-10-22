/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.telenav.josm.common.gui.builder.ButtonBuilder;
import com.telenav.josm.common.gui.builder.CheckBoxBuilder;
import com.telenav.josm.common.gui.builder.ContainerBuilder;
import com.telenav.josm.common.gui.builder.DatePickerBuilder;
import com.telenav.josm.common.gui.builder.LabelBuilder;
import com.telenav.josm.common.gui.builder.TableBuilder;
import org.jdesktop.swingx.JXDatePicker;
import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.DetectionFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.formatter.DateFormatter;
import com.telenav.josm.common.gui.verifier.AbstractDateVerifier;


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

    private List<DetectionTypeListItem> detectionTypeComponent;
    private JButton btnSelectSignTypes;
    private JButton btnClearSignTypes;
    private final boolean isHighZoomLevel;


    FilterPanel(final boolean isHighZoomLevel) {
        super(new GridBagLayout());
        this.isHighZoomLevel = isHighZoomLevel;
        final SearchFilter filter = PreferenceManager.getInstance().loadSearchFilter();
        final DetectionTypeContent detectionTypeContent = DetectionTypeContent.getInstance();
        if (isHighZoomLevel) {
            addDataTypeFilter(filter.getDataTypes());
            addUserFilter(filter.isOlnyUserData());
            addDateFilter(filter.getDate());
            add(new JSeparator(JSeparator.HORIZONTAL), Constraints.SEPARATOR);
            add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDetectionLbl(), Font.BOLD),
                    Constraints.LBL_DETECTION);
            addModeFilter(filter.getDetectionFilter().getModes());
            addEditStatusFilter(filter.getDetectionFilter().getEditStatuses());
            addOsmComparisonFilter(filter.getDetectionFilter().getOsmComparisons());
            //TODO change detection type filter based on selected type - detections get signs
            addDetectionTypeFilter(detectionTypeContent);
            enableDetectionFilters(filter.getDataTypes());
        } else {
            addUserFilter(filter.isOlnyUserData());
            addDateFilter(filter.getDate());
        }
    }

    private void addDataTypeFilter(final List<DataType> types) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeLbl(), Font.BOLD),
                Constraints.LBL_DATA_TYPE);
        cbbPhotos = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeImageTxt(), Font.PLAIN, null,
                types != null && types.contains(DataType.PHOTO));
        add(cbbPhotos, Constraints.CBB_PHOTOS);
        cbbDetections = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeDetectionsTxt(),
                new DataTypeSelectionListener(), Font.PLAIN, types != null && types.contains(DataType.DETECTION), true);
        add(cbbDetections, Constraints.CBB_DETECTIONS);
        cbbCluster = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterDataTypeAggregatedDetectionsTxt(),
                new DataTypeSelectionListener(), Font.PLAIN, types != null && types.contains(DataType.CLUSTER), true);
        add(cbbCluster, Constraints.CBB_AGGREGATED_DETECTIONS);
    }

    private void addUserFilter(final boolean isSelected) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterUserLbl(), Font.BOLD),
                Constraints.getLblUser(isHighZoomLevel));
        final JLabel lblLoginWarning =
                LabelBuilder.build(GuiConfig.getInstance().getDlgFilterLoginWarningLbl(), Font.ITALIC);
        final boolean enabled = true;
        if (UserIdentityManager.getInstance().asUser().getId() <= 0) {
            lblLoginWarning.setForeground(Color.red);
            add(lblLoginWarning, Constraints.geLblLoginWarning(isHighZoomLevel));
        }
        cbbUser = CheckBoxBuilder.build(Font.PLAIN, isSelected, enabled);
        add(cbbUser, Constraints.getCbbUser(isHighZoomLevel));
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
        cbbAutomaticMode = CheckBoxBuilder.build(DetectionMode.AUTOMATIC.toString(), Font.PLAIN, null,
                modes != null && modes.contains(DetectionMode.AUTOMATIC));
        add(cbbAutomaticMode, Constraints.CBB_AUTOMATIC_MODE);
        cbbManualMode = CheckBoxBuilder.build(DetectionMode.MANUAL.toString(), Font.PLAIN, null,
                modes != null && modes.contains(DetectionMode.MANUAL));
        add(cbbManualMode, Constraints.CBB_MANUAL_MODE);
    }

    private void addEditStatusFilter(final List<EditStatus> editStatuses) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterEditStatusLbl(), Font.BOLD),
                Constraints.LBL_EDIT_STATUS);

        cbbOpenEditStatus = CheckBoxBuilder.build(EditStatus.OPEN.toString(), Font.PLAIN, null,
                editStatuses != null && editStatuses.contains(EditStatus.OPEN));
        add(cbbOpenEditStatus, Constraints.CBB_OPEN_EDIT_STATUS);
        cbbMappedEditStatus = CheckBoxBuilder.build(EditStatus.MAPPED.toString(), Font.PLAIN, null,
                editStatuses != null && editStatuses.contains(EditStatus.MAPPED));
        add(cbbMappedEditStatus, Constraints.CBB_MAPPED_EDIT_STATUS);
        cbbBadSignEditStatus = CheckBoxBuilder.build(EditStatus.BAD_SIGN.toString(), Font.PLAIN, null,
                editStatuses != null && editStatuses.contains(EditStatus.BAD_SIGN));
        add(cbbBadSignEditStatus, Constraints.CBB_BAD_SIGN_EDIT_STATUS);
        cbbOtherEditStatus = CheckBoxBuilder.build(EditStatus.OTHER.toString(), Font.PLAIN, null,
                editStatuses != null && editStatuses.contains(EditStatus.OTHER));
        add(cbbOtherEditStatus, Constraints.CBB_OTHER_EDIT_STATUS);
    }

    private void addOsmComparisonFilter(final List<OsmComparison> osmComparisons) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterOsmComparisonLbl(), Font.BOLD),
                Constraints.LBL_OSM_COMPARISON);

        cbbNewOsmComparison = CheckBoxBuilder.build(OsmComparison.NEW.toString(), Font.PLAIN, null,
                osmComparisons != null && osmComparisons.contains(OsmComparison.NEW));
        add(cbbNewOsmComparison, Constraints.CBB_NEW_OSM_COMPARISON);
        cbbChangedOsmComparison = CheckBoxBuilder.build(OsmComparison.CHANGED.toString(), Font.PLAIN, null,
                osmComparisons != null && osmComparisons.contains(OsmComparison.CHANGED));
        add(cbbChangedOsmComparison, Constraints.CBB_CHANGED_OSM_COMPARISON);
        cbbUnknownOsmComparison = CheckBoxBuilder.build(OsmComparison.UNKNOWN.toString(), Font.PLAIN, null,
                osmComparisons != null && osmComparisons.contains(OsmComparison.UNKNOWN));
        add(cbbUnknownOsmComparison, Constraints.CBB_UNKNOWN_OSM_COMPARISON);
        cbbSameOsmComparison = CheckBoxBuilder.build(OsmComparison.SAME.toString(), Font.PLAIN, null,
                osmComparisons != null && osmComparisons.contains(OsmComparison.SAME));
        add(cbbSameOsmComparison, Constraints.CBB_SAME_OSM_COMPARISON);
    }
//TODO make a service call to listSigns and keep them in a hash map. Make sure to only call once. Use the map to populate the JList or JTable accordingly. Ignore BLURRING Type
    private void addDetectionTypeFilter(final DetectionTypeContent detectionTypeContent) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDetectionTypeLbl(), Font.BOLD),
                Constraints.LBL_SIGN_TYPE);
        detectionTypeComponent = new ArrayList<>();
        Map<String, List<Sign>> allSigns = detectionTypeContent.getContent();
        allSigns.keySet().forEach(key -> detectionTypeComponent.add(new DetectionTypeListItem(key, allSigns.get(key))));
        final JPanel detectionTypes = new JPanel();
        detectionTypes.setLayout(new BoxLayout(detectionTypes, BoxLayout.Y_AXIS));
        detectionTypes.setAlignmentX(Component.LEFT_ALIGNMENT);
        detectionTypeComponent.forEach(detectionTypes::add);
        add(ContainerBuilder.buildScrollPane(detectionTypes, getBackground()), Constraints.CBB_SIGN_TYPE);
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
        if (dataTypes != null) {
            enableCommonFilters = dataTypes.contains(DataType.CLUSTER) || dataTypes.contains(DataType.DETECTION);
            enableDetectionFilters = dataTypes.contains(DataType.DETECTION);
        }

        // common filters
        cbbNewOsmComparison.setEnabled(enableCommonFilters);
        cbbChangedOsmComparison.setEnabled(enableCommonFilters);
        cbbUnknownOsmComparison.setEnabled(enableCommonFilters);
        cbbSameOsmComparison.setEnabled(enableCommonFilters);
        btnSelectSignTypes.setEnabled(enableCommonFilters);
        btnClearSignTypes.setEnabled(enableCommonFilters);
        for (DetectionTypeListItem detectionItem : detectionTypeComponent) {
            detectionItem.setEnabled(enableCommonFilters);
        }

        // detection only filters
        cbbAutomaticMode.setEnabled(enableDetectionFilters);
        cbbManualMode.setEnabled(enableDetectionFilters);
        cbbOpenEditStatus.setEnabled(enableDetectionFilters);
        cbbMappedEditStatus.setEnabled(enableDetectionFilters);
        cbbBadSignEditStatus.setEnabled(enableDetectionFilters);
        cbbOtherEditStatus.setEnabled(enableDetectionFilters);

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
            List<EditStatus> editStatuses = selectedEditStatuses();
            List<DetectionMode> detectionModes = selectedModes();
            List<String> signInternalNames = null;
            List<String> signTypes = new ArrayList<>();//listSignType != null ? listSignType.getSelectedValuesList() : null;
            //TODO listSignType might be in a different form for the new case. Take it into account how this will be handled
            searchFilter = new SearchFilter(date, cbbUser.isSelected(), dataTypes, new DetectionFilter(
                    selectedOsmComparisons(), editStatuses, signTypes, null, detectionModes));
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
            if (SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons() != null) {
                newOsmComparisonSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons().contains(OsmComparison.NEW);
                changedOsmComparisonSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons().contains(OsmComparison.CHANGED);
                unknownOsmComparisonSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons().contains(OsmComparison.UNKNOWN);
                sameOsmComparisonSelected =
                        SearchFilter.DEFAULT.getDetectionFilter().getOsmComparisons().contains(OsmComparison.SAME);
            }
            cbbNewOsmComparison.setSelected(newOsmComparisonSelected);
            cbbChangedOsmComparison.setSelected(changedOsmComparisonSelected);
            cbbUnknownOsmComparison.setSelected(unknownOsmComparisonSelected);
            cbbSameOsmComparison.setSelected(sameOsmComparisonSelected);

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
            for (DetectionTypeListItem detectionType : detectionTypeComponent) {
                detectionType.clearSelection();
            }
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
            for (DetectionTypeListItem detectionType : detectionTypeComponent) {
                detectionType.setSelected(true);
            }
        }
    }

    private final class SignTypesClearAction extends AbstractAction {

        private static final long serialVersionUID = -8589369992232950474L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            for (DetectionTypeListItem detectionType : detectionTypeComponent) {
                detectionType.setSelected(false);
            }
        }
    }
}