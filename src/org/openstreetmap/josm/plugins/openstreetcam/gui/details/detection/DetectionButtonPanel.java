/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c) 2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionChangeObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.telenav.josm.common.gui.builder.ButtonBuilder;


/**
 * Defines a button panel with detection specific actions that an user can perform. If a detection is selected an user
 * can: mark the detection as "mapped", "bad detection" or add a new comment.
 *
 * @author ioanao
 * @version $Revision$
 */
class DetectionButtonPanel extends BaseButtonPanel implements DetectionChangeObservable {

    private static final long serialVersionUID = -6885598017144429682L;

    private DetectionChangeObserver detectionChangeObserver;
    private JButton btnMapped;
    private JButton btnBadDetection;
    private JButton btnComment;


    DetectionButtonPanel() {
        super();
    }

    @Override
    void createComponents() {
        addMappedButton();
        addBadDetectionButton();
        addCommentButton();
    }

    private void addMappedButton() {
        final JosmAction action =
                new EditAction(GuiConfig.getInstance().getBtnFixDetectionShortcutText(), EditStatus.MAPPED);
        btnMapped = ButtonBuilder.build(action, GuiConfig.getInstance().getDetectionEditStatusMappedText());
        btnMapped.setToolTipText(
                GuiConfig.getInstance().getBtnFixDetectionTlt().replace(SHORTCUT, action.getShortcut().getKeyText()));
        add(btnMapped);
    }

    private void addBadDetectionButton() {
        final EditAction badAction =
                new EditAction(GuiConfig.getInstance().getBtnBadDetectionShortcutText(), EditStatus.BAD_SIGN);
        btnBadDetection = ButtonBuilder.build(badAction, GuiConfig.getInstance().getBtnBadDetection());
        btnBadDetection.setToolTipText(GuiConfig.getInstance().getBtnBadDetectionTlt().replace(SHORTCUT,
                badAction.getShortcut().getKeyText()));
        add(btnBadDetection);
    }

    private void addCommentButton() {
        final DisplayEditDialogAction otherAction =
                new DisplayEditDialogAction(GuiConfig.getInstance().getDialogAddCommentText(),
                        GuiConfig.getInstance().getBtnOtherActionOnDetectionShortcutText());
        btnComment = ButtonBuilder.build(otherAction, GuiConfig.getInstance().getBtnOtherActionOnDetection());
        btnMapped.setToolTipText(GuiConfig.getInstance().getBtnOtherActionOnDetectionTlt().replace(SHORTCUT,
                otherAction.getShortcut().getKeyText()));
        add(btnComment);
    }

    void enablePanelActions(final EditStatus editStatus) {
        switch (editStatus) {
            case OPEN:
                enablePanelActions(true, true, true);
                break;
            case BAD_SIGN:
                enablePanelActions(true, false, true);
                break;
            case MAPPED:
                enablePanelActions(false, true, true);
                break;
            default:
                // OTHER
                enablePanelActions(true, true, false);
                break;
        }
    }

    private void enablePanelActions(final boolean mappedFlag, final boolean badDetectionFlag, final boolean otherFlag) {
        btnMapped.setEnabled(mappedFlag);
        btnBadDetection.setEnabled(badDetectionFlag);
        btnComment.setEnabled(otherFlag);
    }

    @Override
    public void registerObserver(final DetectionChangeObserver observer) {
        detectionChangeObserver = observer;
        ((DisplayEditDialogAction) btnComment.getAction()).registerObserver(observer);

    }

    @Override
    public void notifyDetectionChangeObserver(final EditStatus status, final String text) {
        detectionChangeObserver.editDetection(status, text);
    }


    private final class EditAction extends JosmAction {

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

    private final class DisplayEditDialogAction extends JosmAction {

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