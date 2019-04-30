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
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionChangeObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
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
    private static final int BUTTON_FONT_SIZE = 13;
    private static final int COLUMNS = 4;

    private transient DetectionChangeObserver detectionChangeObserver;
    private JButton btnMapped;
    private JButton btnBadDetection;
    private JButton btnComment;
    private JButton btnMatchedData;


    DetectionButtonPanel() {
        super(COLUMNS);
    }

    @Override
    void createComponents() {
        addMappedButton();
        addBadDetectionButton();
        addCommentButton();
        addMatchedDataButton();
    }

    private void addMappedButton() {
        final JosmAction action =
                new EditAction(GuiConfig.getInstance().getBtnFixDetectionShortcutText(), EditStatus.MAPPED);
        btnMapped = ButtonBuilder.build(action, IconConfig.getInstance().getMappedIcon(),
                GuiConfig.getInstance().getDetectionEditStatusMappedText(), BUTTON_FONT_SIZE,
                GuiConfig.getInstance().getBtnFixDetectionTlt().replace(SHORTCUT, action.getShortcut().getKeyText()));
        add(btnMapped);
    }

    private void addBadDetectionButton() {
        final EditAction badAction =
                new EditAction(GuiConfig.getInstance().getBtnBadDetectionShortcutText(), EditStatus.BAD_SIGN);
        btnBadDetection = ButtonBuilder.build(badAction, IconConfig.getInstance().getBadDetectionIcon(),
                GuiConfig.getInstance().getBtnBadDetection(), BUTTON_FONT_SIZE, GuiConfig.getInstance()
                .getBtnBadDetectionTlt().replace(SHORTCUT, badAction.getShortcut().getKeyText()));
        add(btnBadDetection);
    }

    private void addCommentButton() {
        final DisplayCommentDialogAction otherAction =
                new DisplayCommentDialogAction(GuiConfig.getInstance().getDialogAddCommentText(),
                        GuiConfig.getInstance().getBtnOtherActionOnDetectionShortcutText());
        btnComment = ButtonBuilder.build(otherAction, IconConfig.getInstance().getOtherIcon(),
                GuiConfig.getInstance().getBtnOtherActionOnDetection(), BUTTON_FONT_SIZE, GuiConfig.getInstance()
                .getBtnOtherActionOnDetectionTlt().replace(SHORTCUT, otherAction.getShortcut().getKeyText()));
        add(btnComment);
    }

    private void addMatchedDataButton() {
        final boolean enabled = DataSet.getInstance().selectedDetectionHasOsmElements();
        final MatchedDataAction matchedDataAction =
                new MatchedDataAction(GuiConfig.getInstance().getBtnMatchedDataShortcutText(), false);
        btnMatchedData = ButtonBuilder.build(matchedDataAction, IconConfig.getInstance().getMatchedWayIcon(),
                GuiConfig.getInstance().getBtnMatchedData(), BUTTON_FONT_SIZE, GuiConfig.getInstance()
                .getBtnMatchedDataTlt().replace(SHORTCUT, matchedDataAction.getShortcut().getKeyText()));
        btnMatchedData.setEnabled(enabled);
        add(btnMatchedData);
    }

    void enablePanelActions(final EditStatus editStatus) {
        final boolean matchedDataFlag = DataSet.getInstance().selectedDetectionHasOsmElements();
        switch (editStatus) {
            case OPEN:
                enablePanelActions(true, true, true, matchedDataFlag);
                break;
            case BAD_SIGN:
                enablePanelActions(true, false, true, matchedDataFlag);
                break;
            case MAPPED:
                enablePanelActions(false, true, true, matchedDataFlag);
                break;
            default:
                // OTHER
                enablePanelActions(true, true, false, matchedDataFlag);
                break;
        }
    }

    private void enablePanelActions(final boolean mappedFlag, final boolean badDetectionFlag, final boolean otherFlag,
            final boolean matchedDataFlag) {
        btnMapped.setEnabled(mappedFlag);
        btnBadDetection.setEnabled(badDetectionFlag);
        btnComment.setEnabled(otherFlag);
        btnMatchedData.setEnabled(matchedDataFlag);
    }

    @Override
    public void registerObserver(final DetectionChangeObserver observer) {
        detectionChangeObserver = observer;
        ((DisplayCommentDialogAction) btnComment.getAction()).registerObserver(observer);

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

    private final class DisplayCommentDialogAction extends JosmAction {

        private static final long serialVersionUID = 7465727160123599818L;
        private final CommentDialog dialog;


        private DisplayCommentDialogAction(final String title, final String shortcutText) {
            super(null, null, null, ShortcutFactory.getInstance().getShotrcut(shortcutText), true);
            dialog = new CommentDialog(title);
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