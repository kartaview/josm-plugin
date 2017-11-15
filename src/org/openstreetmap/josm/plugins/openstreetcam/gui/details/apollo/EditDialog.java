/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.apollo;

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
import com.telenav.josm.common.gui.CancelAction;
import com.telenav.josm.common.gui.ModalDialog;
import com.telenav.josm.common.gui.builder.ButtonBuilder;
import com.telenav.josm.common.gui.builder.ContainerBuilder;
import com.telenav.josm.common.gui.builder.TextComponentBuilder;


/**
 *
 * @author ioanao
 * @version $Revision$
 */
// TODO: make class package private
public class EditDialog extends ModalDialog implements DetectionChangeObservable {

    private static final long serialVersionUID = -6452018453545899786L;
    private static final Dimension DIM = new Dimension(300, 200);
    private static final Border BORDER = new EmptyBorder(5, 5, 2, 5);

    private final EditStatus status;
    private JTextArea txtComment;
    private DetectionChangeObserver observer;


    EditDialog(final String title, final EditStatus status) {
        super(title, null, GuiSizesHelper.getDimensionDpiAdjusted(DIM));
        setLocationRelativeTo(MainApplication.getMap().mapView);
        createComponents();
        this.status = status;
    }

    @Override
    public void createComponents() {
        txtComment = TextComponentBuilder.buildTextArea(new EditDocument(), null, Color.white, Font.PLAIN, true);

        // TODO: create a well named constant for 100
        final JPanel pnlComment = ContainerBuilder.buildBorderLayoutPanel(null,
                ContainerBuilder.buildScrollPane(txtComment, null, Color.white, Color.gray, 100, true, null), null,
                BORDER);
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
            notifyDetectionChangeObserver(status, txtComment.getText());
            dispose();
        }
    }


    private final class EditDocument extends PlainDocument {

        private static final long serialVersionUID = -6861902595242696120L;

        @Override
        public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
            if (str != null && txtComment.getText().length() <= 100) {
                super.insertString(offs, str, a);
            }
        }
    }
}