/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c) 2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionChangeObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.telenav.josm.common.gui.builder.ButtonBuilder;
import com.telenav.josm.common.gui.builder.MenuBuilder;


/**
 *
 * @author ioanao
 * @version $Revision$
 */
class ButtonPanel extends JPanel implements DetectionChangeObservable {

    private static final long serialVersionUID = -6885598017144429682L;

    private static final String SHORTCUT = "sc";
    private static final int COLS = 3;
    private static final int ROWS = 1;
    private static final Dimension DIM = new Dimension(200, 24);

    private DetectionChangeObserver detectionChangeObserver;
    private JButton btnAlreadyFixed;
    private JButton btnFix;
    private JMenuItem badDetectionMenuItem;
    private JMenuItem commentMenuItem;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));
        addFixButton();
        addAlreadyFixedButton();
        addCouldntFixButton();
        setPreferredSize(DIM);
    }

    void enablePanelActions(final EditStatus editStatus) {
        switch (editStatus) {
            case OPEN:
                enablePanelActions(true, true, true, true);
                break;
            case FIXED:
                enablePanelActions(true, false, true, true);
                break;
            case ALREADY_FIXED:
                enablePanelActions(false, true, true, true);
                break;
            case BAD_SIGN:
                enablePanelActions(true, true, false, true);
                break;
            default:
                // OTHER
                enablePanelActions(true, true, true, false);
                break;
        }
    }

    private void enablePanelActions(final boolean alreadyFixedFlag, final boolean fixFlag,
            final boolean badDetectionFlag, final boolean otherFlag) {
        btnAlreadyFixed.setEnabled(alreadyFixedFlag);
        btnFix.setEnabled(fixFlag);
        badDetectionMenuItem.setEnabled(badDetectionFlag);
        commentMenuItem.setEnabled(otherFlag);

    }

    @Override
    public void registerObserver(final DetectionChangeObserver observer) {
        detectionChangeObserver = observer;
        ((DisplayEditDialogAction) commentMenuItem.getAction()).registerObserver(observer);
    }

    @Override
    public void notifyDetectionChangeObserver(final EditStatus status, final String text) {
        detectionChangeObserver.editDetection(status, text);
    }

    private void addFixButton() {
        final JosmAction action =
                new EditAction(GuiConfig.getInstance().getBtnFixDetectionShortcutText(), EditStatus.FIXED);
        btnFix = ButtonBuilder.build(action, GuiConfig.getInstance().getBtnFixDetection());
        btnFix.setToolTipText(
                GuiConfig.getInstance().getBtnFixDetectionTlt().replace(SHORTCUT, action.getShortcut().getKeyText()));
        add(btnFix);
    }

    private void addAlreadyFixedButton() {
        final JosmAction action = new EditAction(GuiConfig.getInstance().getBtnAlreadyFixedDetectionShortcutText(),
                EditStatus.ALREADY_FIXED);
        btnAlreadyFixed = ButtonBuilder.build(action, GuiConfig.getInstance().getBtnAlreadyFixedDetection());
        btnAlreadyFixed.setToolTipText(GuiConfig.getInstance().getBtnAlreadyFixedDetectionTlt().replace(SHORTCUT,
                action.getShortcut().getKeyText()));
        add(btnAlreadyFixed);
    }

    private void addCouldntFixButton() {
        final EditAction badAction =
                new EditAction(GuiConfig.getInstance().getBtnBadDetectionShortcutText(), EditStatus.BAD_SIGN);
        badDetectionMenuItem = MenuBuilder.build(badAction, GuiConfig.getInstance().getBtnBadDetection());
        badDetectionMenuItem.setToolTipText(GuiConfig.getInstance().getBtnBadDetectionTlt().replace(SHORTCUT,
                badAction.getShortcut().getKeyText()));


        final DisplayEditDialogAction otherAction =
                new DisplayEditDialogAction(GuiConfig.getInstance().getAddCommentDialogText(),
                        GuiConfig.getInstance().getBtnOtherActionOnDetectionShortcutText());
        commentMenuItem = MenuBuilder.build(otherAction, GuiConfig.getInstance().getBtnOtherActionOnDetection());
        commentMenuItem.setToolTipText(GuiConfig.getInstance().getBtnOtherActionOnDetectionTlt().replace(SHORTCUT,
                otherAction.getShortcut().getKeyText()));

        final JButton btnOther = ButtonBuilder.build(new CouldntFixAction(badDetectionMenuItem, commentMenuItem),
                GuiConfig.getInstance().getBtnCouldntFixDetection());
        add(btnOther);
    }


    private class EditAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        private final EditStatus editStatus;

        private EditAction(final String shortcutText, final EditStatus status) {
            super(null, null, null, ShortcutFactory.getInstance().getShotrcut(shortcutText), true);
            this.editStatus = status;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            notifyDetectionChangeObserver(editStatus, null);
        }
    }


    private class CouldntFixAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;
        private static final int Y_COORD = 4;

        private final JPopupMenu menu;


        private CouldntFixAction(final JMenuItem item1, final JMenuItem item2) {
            menu = new JPopupMenu();
            menu.add(item1);
            menu.add(item2);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            final JButton cmpParent = (JButton) event.getSource();
            final Point point = cmpParent.getMousePosition();
            int x = 0;
            int y = 0;
            if (cmpParent.getMousePosition() != null) {
                x = point.x;
                y = point.y - cmpParent.getWidth() / Y_COORD;
            }
            menu.show(cmpParent, x, y);
        }
    }


    private class DisplayEditDialogAction extends JosmAction {

        private static final long serialVersionUID = 7465727160123599818L;
        private final EditDialog dialog;


        private DisplayEditDialogAction(final String title, final String shortcutText) {
            super(null, null, null, ShortcutFactory.getInstance().getShotrcut(shortcutText), true);
            dialog = new EditDialog(title);

        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            dialog.setVisible(true);
        }

        private void registerObserver(final DetectionChangeObserver observer) {
            dialog.registerObserver(observer);
        }
    }
}