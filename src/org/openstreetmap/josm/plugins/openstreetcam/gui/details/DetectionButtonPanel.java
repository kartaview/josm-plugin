/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.telenav.josm.common.gui.builder.ButtonBuilder;


/**
*
* @author ioanao
* @version $Revision$
*/
public class DetectionButtonPanel extends JPanel {

    private static final long serialVersionUID = -6885598017144429682L;

    private static final String SHORTCUT = "sc";
    private static final int COLS = 4;
    private static final int ROWS = 1;
    private static final Dimension DIM = new Dimension(200, 24);

    private JButton btnReopen;
    private JButton btnFix;
    private JButton btnInvalidate;
    private JButton btnOther;


    DetectionButtonPanel() {
        super(new GridLayout(ROWS, COLS));
        addReopenButton();
        addFixButton();
        addInvalidateButton();
        addOtherButton();
        setPreferredSize(DIM);
    }

    private void addReopenButton() {
        final JosmAction action = new ReopenAction();
        btnReopen = ButtonBuilder.build(action, GuiConfig.getInstance().getBtnReopenDetection());
        btnReopen.setToolTipText(GuiConfig.getInstance().getBtnReopenDetectionTlt().replace(SHORTCUT,
                action.getShortcut().getKeyText()));
        add(btnReopen);
    }

    private void addFixButton() {
        final JosmAction action = new FixAction();
        btnFix = ButtonBuilder.build(action, GuiConfig.getInstance().getBtnFixDetection());
        btnFix.setToolTipText(
                GuiConfig.getInstance().getBtnFixDetectionTlt().replace(SHORTCUT, action.getShortcut().getKeyText()));
        add(btnFix);
    }

    private void addInvalidateButton() {
        final JosmAction action = new InvalidateAction();
        btnInvalidate = ButtonBuilder.build(action, GuiConfig.getInstance().getBtnInvalidateDetection());
        btnInvalidate.setToolTipText(GuiConfig.getInstance().getBtnInvalidateDetectionTlt().replace(SHORTCUT,
                action.getShortcut().getKeyText()));
        add(btnInvalidate);
    }

    private void addOtherButton() {
        final JosmAction action = new OtherAction();
        btnOther = ButtonBuilder.build(action, GuiConfig.getInstance().getBtnOtherDetection());
        btnOther.setToolTipText(GuiConfig.getInstance().getBtnOtherActionOnDetectionTlt().replace(SHORTCUT,
                action.getShortcut().getKeyText()));
        add(btnOther);
    }


    private final class ReopenAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        private ReopenAction() {
            super(null, null, null, ShortcutFactory.getInstance()
                    .getShotrcut(GuiConfig.getInstance().getBtnReopenDetectionShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {

        }
    }

    private final class FixAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        private FixAction() {
            super(null, null, null,
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnFixDetectionShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {

        }
    }

    private final class InvalidateAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        private InvalidateAction() {
            super(GuiConfig.getInstance().getBtnClosestShortcutText(), null,
                    GuiConfig.getInstance().getBtnClosestShortcutText(),
                    ShortcutFactory.getInstance()
                            .getShotrcut(GuiConfig.getInstance().getBtnInvalidateDetectionShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {

        }
    }

    private final class OtherAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        private OtherAction() {
            super(GuiConfig.getInstance().getBtnClosestShortcutText(), null,
                    GuiConfig.getInstance().getBtnClosestShortcutText(),
                    ShortcutFactory.getInstance()
                            .getShotrcut(GuiConfig.getInstance().getBtnOtherActionOnDetectionShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {

        }
    }
}