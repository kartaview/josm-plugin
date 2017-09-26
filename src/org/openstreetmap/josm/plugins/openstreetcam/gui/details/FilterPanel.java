/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXDatePicker;
import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import com.telenav.josm.common.formatter.DateFormatter;
import com.telenav.josm.common.gui.builder.CheckBoxBuilder;
import com.telenav.josm.common.gui.builder.DatePickerBuilder;
import com.telenav.josm.common.gui.builder.LabelBuilder;
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


    FilterPanel() {
        super(new GridBagLayout());
        final ListFilter filter = PreferenceManager.getInstance().loadListFilter();
        addDateFitler(filter.getDate());
        addUserFilter(filter.isOnlyUserFlag());
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

    /**
     * Returns the currently selected filters, considering also the uncommitted date case.
     *
     * @return a {@code ListFilter} object
     */
    ListFilter selectedFilters() {
        ListFilter filter = null;
        final String dateValue = pickerDate.getEditor().getText();
        if (dateValue == null || dateValue.trim().isEmpty()) {
            filter = new ListFilter(null, cbbUser.isSelected());
        } else {
            final Date date = DateFormatter.parseDay(dateValue.trim());
            if (date == null) {
                // date value was invalid
                JOptionPane.showMessageDialog(this, GuiConfig.getInstance().getIncorrectDateFilterText(),
                        GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
            } else if (date.compareTo(Calendar.getInstance().getTime()) > 0) {
                // date is to big
                JOptionPane.showMessageDialog(this, GuiConfig.getInstance().getUnacceptedDateFilterText(),
                        GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
            } else {
                filter = new ListFilter(date, cbbUser.isSelected());
            }
        }
        return filter;
    }

    /**
     * Clears the filters.
     */
    void clearFilters() {
        pickerDate.getEditor().setText("");
        pickerDate.setDate(null);
        cbbUser.setSelected(false);
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

        private Constraints() {}
    }
}