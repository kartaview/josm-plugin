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
    private static final int COLS = 3;
    private static final int ROWS = 1;
    private static final Dimension DIM = new Dimension(200, 24);

    private JButton btnFix;
    private JButton btnInvalidate;
    private JButton btnOther;


    DetectionButtonPanel() {
        super(new GridLayout(ROWS, COLS));
        addFixButton();
        addAlreadyFixedButton();
        addCouldntFixButton();
        setPreferredSize(DIM);
    }

    private void addFixButton() {
        final JosmAction action = new FixAction();
        btnFix = ButtonBuilder.build(action, GuiConfig.getInstance().getBtnFixDetection());
        btnFix.setToolTipText(
                GuiConfig.getInstance().getBtnFixDetectionTlt().replace(SHORTCUT, action.getShortcut().getKeyText()));
        add(btnFix);
    }

    private void addAlreadyFixedButton() {
        final JosmAction action = new AlreadyFixedAction();
        btnInvalidate = ButtonBuilder.build(action, GuiConfig.getInstance().getBtnAlreadyFixedDetection());
        btnInvalidate.setToolTipText(GuiConfig.getInstance().getBtnAlreadyFixedDetectionTlt().replace(SHORTCUT,
                action.getShortcut().getKeyText()));
        add(btnInvalidate);
    }

    private void addCouldntFixButton() {
        final JosmAction action = new CouldntFixAction();
        btnOther = ButtonBuilder.build(action, GuiConfig.getInstance().getBtnCouldntFixDetection());
        add(btnOther);
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

    private final class AlreadyFixedAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        private AlreadyFixedAction() {
            super(null, null, null, ShortcutFactory.getInstance()
                    .getShotrcut(GuiConfig.getInstance().getBtnAlreadyFixedDetectionShortcutText()), true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {

        }
    }

    private final class CouldntFixAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        @Override
        public void actionPerformed(final ActionEvent event) {

        }
    }
}