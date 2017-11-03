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

    private static final int COLS = 4;
    private static final int ROWS = 1;
    private static final Dimension DIM = new Dimension(200, 24);

    /*
     * private final JButton btnReopen; private JButton btnFix; private JButton btnInvalidate; private JButton btnOther;
     */


    DetectionButtonPanel() {
        super(new GridLayout(ROWS, COLS));
        add(ButtonBuilder.build(new ReopenAction(), GuiConfig.getInstance().getBtnReopenDetection()));
        add(ButtonBuilder.build(new FixAction(), GuiConfig.getInstance().getBtnFixDetection()));
        add(ButtonBuilder.build(new InvalidateAction(), GuiConfig.getInstance().getBtnInvalidateDetection()));
        add(ButtonBuilder.build(new OtherAction(), GuiConfig.getInstance().getBtnOtherDetection()));
        setPreferredSize(DIM);
    }

    private final class ReopenAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        private ReopenAction() {
            super(GuiConfig.getInstance().getBtnClosestShortcutText(), null,
                    GuiConfig.getInstance().getBtnClosestShortcutText(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnClosestShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {

        }
    }

    private final class FixAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        private FixAction() {
            super(GuiConfig.getInstance().getBtnClosestShortcutText(), null,
                    GuiConfig.getInstance().getBtnClosestShortcutText(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnClosestShortcutText()),
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
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnClosestShortcutText()),
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
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnClosestShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {

        }
    }
}