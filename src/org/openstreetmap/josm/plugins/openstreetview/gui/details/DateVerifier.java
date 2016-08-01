/*
 *  Copyright 2015 Telenav, Inc.
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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import com.telenav.josm.common.formatter.DateFormatter;


/**
 * Defines an input verifier for date text fields.
 *
 * @author ioanao
 * @version $Revision$
 */
class DateVerifier implements KeyListener {

    private static final String LETTER = "[0-9]";
    private static final String YEAR_PATTERN = LETTER + "{4}";
    private static final String DM_PATTERN = LETTER + "{2}";
    private static final String SEP = "-";

    private final JTextField textField;

    /**
     * Builds a new object based on the given arguments.
     *
     * @param component the {@code JComponent} that is validated
     * @param message a {@code String} to be displayed if the user input is invalid. This string is displayed as a
     * tool-tip.
     */
    DateVerifier(final JComponent component) {
        textField = ((JTextField) component);
        this.textField.addKeyListener(this);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        final String valueStr = textField.getText().trim();
        if ((e.getKeyCode() == KeyEvent.VK_ENTER) && !(valueStr.isEmpty())) {
            if (!validate()) {
                textField.setText("");
            }
        } else {
            autocompleteDateSeparator(valueStr, e.getKeyChar());
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        autocompleteDateSeparator(textField.getText().trim(), e.getKeyChar());
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        final String keyValue = String.valueOf(e.getKeyChar());
        if (!(keyValue.matches(LETTER))
                || (textField.getText().matches(YEAR_PATTERN + SEP + DM_PATTERN + SEP + DM_PATTERN))) {
            e.setKeyChar(Character.MIN_VALUE);
        }
    }

    private boolean validate() {
        final String valueStr = textField.getText().trim();
        if (!valueStr.isEmpty()) {
            final DateFormatter formatter = new DateFormatter();
            final Date newDate = (Date) formatter.stringToValue(valueStr);
            if (newDate == null) {
                JOptionPane.showMessageDialog(null, GuiConfig.getInstance().getErrorDateFilterTxt(),
                        GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void autocompleteDateSeparator(final String text, final char key) {
        if (key != '\b') {
            if (text.matches(YEAR_PATTERN) || (text.matches(YEAR_PATTERN + SEP + DM_PATTERN))) {
                textField.setText(text + SEP);
            }
        }
    }

}