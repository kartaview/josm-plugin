/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetview.gui.details;

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
import javax.swing.text.DefaultFormatterFactory;
import org.jdesktop.swingx.JXDatePicker;
import org.openstreetmap.josm.gui.JosmUserIdentityManager;
import org.openstreetmap.josm.plugins.openstreetview.argument.ListFilter;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.pref.PreferenceManager;
import com.telenav.josm.common.formatter.DateFormatter;
import com.telenav.josm.common.gui.GuiBuilder;


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
    private JLabel lblLoginWarning;

    FilterPanel() {
        super(new GridBagLayout());
        final ListFilter filter = PreferenceManager.getInstance().loadListFilter();
        addDateFitler(filter.getDate());
        addUserFilter(filter.isOnlyUserFlag());
    }


    private void addDateFitler(final Date date) {
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getDlgFilterDateLbl(), getFont().deriveFont(Font.BOLD),
                getBackground()), Constraints.LBL_DATE);

        pickerDate = new JXDatePicker();
        pickerDate.setPreferredSize(PICKER_SIZE);
        pickerDate.getMonthView().setTodayBackground(Color.darkGray);
        pickerDate.getMonthView().setDayForeground(Calendar.SATURDAY, Color.red);
        pickerDate.getMonthView().setShowingLeadingDays(true);
        pickerDate.getMonthView().setShowingTrailingDays(true);
        pickerDate.getMonthView().setSelectionDate(date);
        pickerDate.getEditor().setFormatterFactory(new DefaultFormatterFactory(new DateFormatter()));
        pickerDate.getEditor().addKeyListener(new DateVerifier(pickerDate.getEditor()));
        add(pickerDate, Constraints.PICKER_DATE);
    }

    private void addUserFilter(final boolean isSelected) {
        add(GuiBuilder.buildLabel(GuiConfig.getInstance().getDlgFilterUserLbl(), getFont().deriveFont(Font.BOLD),
                getBackground()), Constraints.LBL_USER);
        cbbUser = GuiBuilder.buildCheckBox(null, getFont().deriveFont(Font.PLAIN));
        cbbUser.setSelected(isSelected);
        lblLoginWarning = GuiBuilder.buildLabel(GuiConfig.getInstance().getDlgFilterLoginWarning(),
                getFont().deriveFont(Font.ITALIC), getBackground());
        if (JosmUserIdentityManager.getInstance().asUser().getId() < 0) {
            cbbUser.setEnabled(false);
            lblLoginWarning.setForeground(Color.red);
            add(lblLoginWarning, Constraints.LBL_LOGIN_WARNING);
        }
        add(cbbUser, Constraints.CBB_USER);
    }

    /**
     * Returns the currently selected filters, considering also the uncommitted date case.
     *
     * @return a {@code ListFilter} object
     */
    ListFilter selectedFilters() {
        final String editorText = pickerDate.getEditor().getText();
        final DateFormatter formatter = new DateFormatter();

        Date uncommitteddDate;
        final Object uncommitteddDateObject = formatter.stringToValue(editorText);
        if (uncommitteddDateObject == null) {
            uncommitteddDate = null;
        } else {
            uncommitteddDate = (Date) formatter.stringToValue(editorText);
        }

        if (uncommitteddDate == null) {
            // the 'uncommitted' date is invalid
            if (!editorText.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, GuiConfig.getInstance().getIncorrectDateFilterTxt(),
                        GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // the 'uncommitted' date is empty
            if (!(pickerDate.getDate() == null)) {
                return getConfirmation(uncommitteddDate, editorText);
            }

            // the date from the date picker editor was already committed
            return new ListFilter(pickerDate.getDate(), cbbUser.isSelected());
        }

        // the 'uncommitted' date is valid
        // the date from the date picker editor was already committed
        if (uncommitteddDate.equals(pickerDate.getDate())) {
            // the 'uncommitted' date is greater than the accepted value
            if (!checkAcceptance(uncommitteddDate)) {
                return null;
            }

            // the 'uncommitted' date is committed and valid
            return new ListFilter(pickerDate.getDate(), cbbUser.isSelected());
        }

        // the date from the date picker editor was not committed
        if (!checkAcceptance(uncommitteddDate)) {
            return null;
        }

        // the uncommitted date need an extra confirmation
        return getConfirmation(uncommitteddDate, editorText);
    }

    private ListFilter getConfirmation(final Date date, final String dateAsText) {
        final int response =
                JOptionPane.showConfirmDialog(null, GuiConfig.getInstance().getConfirmDateFilterTxt() + dateAsText,
                        GuiConfig.getInstance().getConfirmDateFilterTitle(), JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            return new ListFilter(date, cbbUser.isSelected());
        }
        return null;
    }

    private boolean checkAcceptance(final Date date) {
        if (date.compareTo(
                (Date) (new DateFormatter().stringToValue(GuiConfig.getInstance().getMaxDateFilterTxt()))) > 0) {
            JOptionPane.showMessageDialog(null, GuiConfig.getInstance().getUnacceptedDateFilterTxt(),
                    GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Clears the filters.
     */
    void clearFilters() {
        pickerDate.getEditor().setText("");
        pickerDate.setDate(null);
        cbbUser.setSelected(false);
    }

    /* Holds UI constraints */
    private static final class Constraints {

        private Constraints() {}

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
    }
}