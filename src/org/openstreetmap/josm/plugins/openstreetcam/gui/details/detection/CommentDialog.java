/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionChangeObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DetectionChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.tools.GuiSizesHelper;
import com.grab.josm.common.gui.CancelAction;
import com.grab.josm.common.gui.ModalDialog;
import com.grab.josm.common.gui.builder.ButtonBuilder;
import com.grab.josm.common.gui.builder.ContainerBuilder;
import com.grab.josm.common.gui.builder.TextComponentBuilder;


/**
 *
 * @author ioanao
 * @version $Revision$
 */
class CommentDialog extends ModalDialog implements DetectionChangeObservable {

    private static final long serialVersionUID = -6452018453545899786L;
    private static final Dimension DIM = new Dimension(300, 200);
    private static final Border BORDER = new EmptyBorder(5, 5, 2, 5);
    private static final int SCROLL_BAR_UNIT = 100;

    private JTextArea txtComment;
    private transient DetectionChangeObserver observer;


    CommentDialog(final String title) {
        super(title, null, GuiSizesHelper.getDimensionDpiAdjusted(DIM));
        setLocationRelativeTo(MainApplication.getMap().mapView);
        createComponents();
    }

    @Override
    public void createComponents() {
        txtComment = TextComponentBuilder.buildTextArea(new EditDocument(), null, Color.white, Font.PLAIN, true);
        final JPanel pnlComment = ContainerBuilder.buildBorderLayoutPanel(null, ContainerBuilder
                .buildScrollPane(txtComment, null, Color.white, Color.gray, SCROLL_BAR_UNIT, true, null), null, BORDER);
        pnlComment.setVerifyInputWhenFocusTarget(true);
        add(pnlComment, BorderLayout.CENTER);

        final JButton btnOk = ButtonBuilder.build(new OkAction(), GuiConfig.getInstance().getBtnOkLbl());
        final JPanel pnlBtn = ContainerBuilder.buildFlowLayoutPanel(FlowLayout.TRAILING, btnOk,
                ButtonBuilder.build(new CancelAction(this), GuiConfig.getInstance().getBtnCancelLbl()));
        final JPanel pnlSouth = ContainerBuilder.buildBorderLayoutPanel(null, pnlBtn, null);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    @Override
    public void registerObserver(final DetectionChangeObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyDetectionChangeObserver(final EditStatus status, final String text) {
        observer.editDetection(status, text);
    }


    private class OkAction extends AbstractAction {

        private static final long serialVersionUID = 1132011303908316806L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            notifyDetectionChangeObserver(EditStatus.OTHER, txtComment.getText());
            txtComment.setText(null);
            dispose();
        }
    }


    private final class EditDocument extends PlainDocument {

        private static final long serialVersionUID = -6861902595242696120L;
        private static final int MAX_LENGTH = 100;

        @Override
        public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
            if (str != null && txtComment.getText().length() <= MAX_LENGTH) {
                super.insertString(offs, str, a);
            }
        }
    }
}