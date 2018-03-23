/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXDatePicker;
import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ImageDataType;
import org.openstreetmap.josm.plugins.openstreetcam.argument.SearchFilter;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DetectionMode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.SignType;
import org.openstreetmap.josm.plugins.openstreetcam.service.apollo.DetectionFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.formatter.DateFormatter;
import com.telenav.josm.common.gui.builder.CheckBoxBuilder;
import com.telenav.josm.common.gui.builder.ContainerBuilder;
import com.telenav.josm.common.gui.builder.DatePickerBuilder;
import com.telenav.josm.common.gui.builder.LabelBuilder;
import com.telenav.josm.common.gui.builder.ListBuilder;
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
    private JCheckBox cbbDetections;
    private JCheckBox cbbPhotos;
    private JCheckBox cbbAutomaticMode;
    private JCheckBox cbbManualMode;

    private JList<OsmComparison> listOsmComparison;
    private JList<EditStatus> listEditStatus;
    private JList<SignType> listSignType;

    private final boolean isHighLevelZoom;


    FilterPanel(final boolean isHighLevelZoom) {
        super(new GridBagLayout());
        this.isHighLevelZoom = isHighLevelZoom;
        final SearchFilter filter = PreferenceManager.getInstance().loadSearchFilter();
        addDateFitler(filter.getDate());
        addUserFilter(filter.isOlnyUserData());
        if (isHighLevelZoom) {
            addDataTypeFilter(filter.getDataTypes());
            add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDetectionLbl(), Font.BOLD),
                    Constraints.LBL_DETECTION);
            addOsmComparisonFilter(filter.getDetectionFilter().getOsmComparisons());
            addEditStatusFilter(filter.getDetectionFilter().getEditStatuses());
            addDetectionTypeFilter(filter.getDetectionFilter().getSignTypes());
            addModeFilter(filter.getDetectionFilter().getModes());

            final boolean enableFilters =
                    filter.getDataTypes() != null && filter.getDataTypes().contains(ImageDataType.DETECTIONS);
            enableDetectionFilters(enableFilters);
        }
    }

    private void addDateFitler(final Date date) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDateLbl(), Font.BOLD), Constraints.LBL_DATE);
        pickerDate = DatePickerBuilder.build(date, Calendar.getInstance().getTime(), PICKER_SIZE);
        pickerDate.getEditor().addKeyListener(new DateVerifier(pickerDate));
        if (Util.zoom(MainApplication.getMap().mapView.getRealBounds()) < Config.getInstance().getMapPhotoZoom()) {
            pickerDate.setEnabled(false);
        }
        add(pickerDate, Constraints.PICKER_DATE);
    }

    private void enableDetectionFilters(final boolean enable) {
        listOsmComparison.setEnabled(enable);
        listEditStatus.setEnabled(enable);
        listSignType.setEnabled(enable);
        cbbAutomaticMode.setEnabled(enable);
        cbbManualMode.setEnabled(enable);
    }

    private void addUserFilter(final boolean isSelected) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterUserLbl(), Font.BOLD), Constraints.LBL_USER);

        final JLabel lblLoginWarning =
                LabelBuilder.build(GuiConfig.getInstance().getDlgFilterLoginWarningLbl(), Font.ITALIC);
        final boolean enabled = true;
        if (UserIdentityManager.getInstance().asUser().getId() <= 0) {
            lblLoginWarning.setForeground(Color.red);
            add(lblLoginWarning, Constraints.LBL_LOGIN_WARNING);
        }
        cbbUser = CheckBoxBuilder.build(Font.PLAIN, isSelected, enabled);
        add(cbbUser, Constraints.CBB_USER);
    }

    private void addDataTypeFilter(final List<ImageDataType> types) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterPhotoTypeLbl(), Font.BOLD),
                Constraints.LBL_PHOTO_TYPE);

        cbbPhotos = CheckBoxBuilder.build(GuiConfig.getInstance().getDataTypeImageText(), Font.PLAIN, null,
                types != null && types.contains(ImageDataType.PHOTOS));
        add(cbbPhotos, Constraints.CBB_PHOTOS);

        cbbDetections = CheckBoxBuilder.build(GuiConfig.getInstance().getDataTypeDetectionText(), Font.PLAIN, null,
                types != null && types.contains(ImageDataType.DETECTIONS));
        add(cbbDetections, Constraints.CBB_DETECTIONS);
        cbbDetections.addItemListener(new DetectionFilterSelectionListener());
    }

    private void addOsmComparisonFilter(final List<OsmComparison> osmComparisons) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterOsmComparisonLbl(), Font.BOLD),
                Constraints.LBL_OSM_COMPARISON);
        listOsmComparison = ListBuilder.build(Arrays.asList(OsmComparison.values()), osmComparisons,
                new DefaultListCellRenderer(), Font.PLAIN);
        listOsmComparison.setLayoutOrientation(JList.VERTICAL);

        add(ContainerBuilder.buildScrollPane(listOsmComparison, getBackground()), Constraints.CBB_OSM_COMPARISON);
    }

    private void addEditStatusFilter(final List<EditStatus> editStatuses) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterEditStatusLbl(), Font.BOLD),
                Constraints.LBL_EDIT_STATUS);
        listEditStatus = ListBuilder.build(Arrays.asList(EditStatus.values()), editStatuses,
                new DefaultListCellRenderer(), Font.PLAIN);
        listEditStatus.setLayoutOrientation(JList.VERTICAL);
        add(ContainerBuilder.buildScrollPane(listEditStatus, getBackground()), Constraints.CBB_EDIT_STATUS);
    }

    private void addDetectionTypeFilter(final List<SignType> signTypes) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterDetectionTypeLbl(), Font.BOLD),
                Constraints.LBL_SIGN_TYPE);
        listSignType = ListBuilder.build(Arrays.asList(SignType.values()), signTypes, new DefaultListCellRenderer(),
                Font.PLAIN);
        listSignType.setLayoutOrientation(JList.VERTICAL);
        add(ContainerBuilder.buildScrollPane(listSignType, getBackground()), Constraints.CBB_SIGN_TYPE);
    }

    private void addModeFilter(final List<DetectionMode> modes) {
        add(LabelBuilder.build(GuiConfig.getInstance().getDlgFilterModeLbl(), Font.BOLD), Constraints.LBL_MODE);
        cbbAutomaticMode = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterModeAutomaticTxt(), Font.PLAIN,
                null, modes != null && modes.contains(DetectionMode.AUTOMATIC));
        add(cbbAutomaticMode, Constraints.CBB_AUTOMATIC_MODE);
        cbbManualMode = CheckBoxBuilder.build(GuiConfig.getInstance().getDlgFilterModeManualTxt(), Font.PLAIN, null,
                modes != null && modes.contains(DetectionMode.MANUAL));
        add(cbbManualMode, Constraints.CBB_MANUAL_MODE);
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
        if (isHighLevelZoom) {
            searchFilter = new SearchFilter(date, cbbUser.isSelected(), getSelectedDataTypes(),
                    new DetectionFilter(listOsmComparison.getSelectedValuesList(),
                            listEditStatus.getSelectedValuesList(), listSignType.getSelectedValuesList(),
                            getSelectedModes()));
        } else {
            searchFilter = new SearchFilter(date, cbbUser.isSelected());
        }
        return searchFilter;
    }

    private List<ImageDataType> getSelectedDataTypes() {
        final List<ImageDataType> selected = new ArrayList<>();
        if (cbbPhotos.isSelected()) {
            selected.add(ImageDataType.PHOTOS);
        }
        if (cbbDetections.isSelected()) {
            selected.add(ImageDataType.DETECTIONS);
        }
        return selected;
    }

    private List<DetectionMode> getSelectedModes() {
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
    void clearFilters() {
        pickerDate.getEditor().setText("");
        pickerDate.setDate(null);
        cbbUser.setSelected(false);
        if (isHighLevelZoom) {
            cbbPhotos.setSelected(false);
            cbbDetections.setSelected(false);
            listOsmComparison.clearSelection();
            listEditStatus.clearSelection();
            listSignType.clearSelection();
            cbbAutomaticMode.setSelected(false);
            cbbManualMode.setSelected(false);
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

    private final class DetectionFilterSelectionListener implements ItemListener {

        @Override
        public void itemStateChanged(final ItemEvent event) {
            final boolean filtersEnabled = event.getStateChange() == ItemEvent.SELECTED;
            enableDetectionFilters(filtersEnabled);
        }

    }


    /* Holds UI constraints */
    private static final class Constraints {

        private static final GridBagConstraints LBL_DATE = new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 3, 5), 0, 0);
        private static final GridBagConstraints PICKER_DATE = new GridBagConstraints(1, 0, 2, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 3, 10), 0, 0);

        private static final GridBagConstraints LBL_USER = new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 5), 0, 0);
        private static final GridBagConstraints CBB_USER = new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 10), 0, 0);
        private static final GridBagConstraints LBL_LOGIN_WARNING = new GridBagConstraints(2, 1, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 10), 0, 0);

        private static final GridBagConstraints LBL_PHOTO_TYPE = new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 3, 0), 0, 0);
        private static final GridBagConstraints CBB_DETECTIONS = new GridBagConstraints(1, 2, 1, 1, 0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 0), 0, 0);
        private static final GridBagConstraints CBB_PHOTOS = new GridBagConstraints(2, 2, 1, 1, 0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 0), 0, 0);

        private static final GridBagConstraints LBL_DETECTION = new GridBagConstraints(0, 4, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);
        private static final GridBagConstraints LBL_OSM_COMPARISON = new GridBagConstraints(0, 5, 2, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 15, 3, 5), 0, 0);
        private static final GridBagConstraints CBB_OSM_COMPARISON = new GridBagConstraints(1, 5, 3, 2, 1, 4,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 10), 0, 70);

        private static final GridBagConstraints LBL_EDIT_STATUS = new GridBagConstraints(0, 7, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 15, 3, 5), 0, 0);
        private static final GridBagConstraints CBB_EDIT_STATUS = new GridBagConstraints(1, 7, 3, 2, 1, 4,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 10), 0, 90);

        private static final GridBagConstraints LBL_SIGN_TYPE = new GridBagConstraints(0, 9, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 15, 3, 5), 0, 0);
        private static final GridBagConstraints CBB_SIGN_TYPE = new GridBagConstraints(1, 9, 3, 1, 1, 4,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 10), 0, 90);

        private static final GridBagConstraints LBL_MODE = new GridBagConstraints(0, 10, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 15, 3, 5), 0, 0);
        private static final GridBagConstraints CBB_AUTOMATIC_MODE = new GridBagConstraints(1, 10, 1, 1, 0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 0), 0, 0);
        private static final GridBagConstraints CBB_MANUAL_MODE = new GridBagConstraints(2, 10, 1, 1, 0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 10), 0, 0);

        private Constraints() {}
    }
}