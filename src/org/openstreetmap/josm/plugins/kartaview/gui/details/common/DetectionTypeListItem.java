/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openstreetmap.josm.plugins.kartaview.entity.Sign;
import org.openstreetmap.josm.plugins.kartaview.gui.DetectionIconFactory;
import com.grab.josm.common.gui.builder.CheckBoxBuilder;
import com.grab.josm.common.gui.builder.ListBuilder;


/**
 * This class maps an element from the detection type component.
 *
 * @author laurad
 */
class DetectionTypeListItem extends JPanel {

    private static final long serialVersionUID = -7115447482264760072L;
    private static final int MAXIMUM_SIGNS_DISPLAYED_ON_ROW = 12;
    private final JCheckBox signType;
    private final JList<Sign> signList;
    private final int listSize;

    DetectionTypeListItem(final String labelName, final boolean typeSelected, final List<Sign> signs,
            final List<Sign> selectedSigns) {
        this.listSize = signs.size();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBackground(Color.WHITE);

        final String prettyName = labelName.replace("_", " ");
        signType = CheckBoxBuilder.build(prettyName, labelName, new DetectionTypeCBoxActionListener(), Font.PLAIN,
                Component.LEFT_ALIGNMENT, Color.WHITE, typeSelected);
        add(signType);

        final int visibleRows =
                signs.size() % MAXIMUM_SIGNS_DISPLAYED_ON_ROW == 0 ? signs.size() / MAXIMUM_SIGNS_DISPLAYED_ON_ROW :
                        signs.size() / MAXIMUM_SIGNS_DISPLAYED_ON_ROW + 1; //TODO calculate size based on width
        final List<Sign> selection = typeSelected ? signs : selectedSigns;
        signList = ListBuilder
                .build(signs, selection, new DetectionTypeListRenderer(), new DetectionListSelectionListener(),
                        Font.PLAIN, JList.HORIZONTAL_WRAP, Component.LEFT_ALIGNMENT, visibleRows);
        add(signList);
    }

    void selectAll() {
        final int end = signList.getModel().getSize() - 1;
        signList.setSelectionInterval(0, end);
    }

    void clearSelection() {
        signType.setSelected(false);
        signList.clearSelection();
    }

    void clearTypeSelection() {
        signType.setSelected(false);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        signType.setEnabled(enabled);
        signList.setEnabled(enabled);
    }

    boolean isTypeSelected() {
        return signType.isSelected();
    }

    String getTypeName() {
        return signType.getName();
    }

    List<Sign> getSignList() {
        return signList.getSelectedValuesList();
    }

    private class DetectionTypeCBoxActionListener implements ActionListener{

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (signType.isSelected()) {
                selectAll();
            } else {
                clearSelection();
            }
        }
    }

    private class DetectionListSelectionListener implements ListSelectionListener{

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            if (getSignList().size() != listSize) {
                signType.setSelected(false);
            }else{
                signType.setSelected(true);
            }
        }
    }

    private static class DetectionTypeListRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 7022769009126337948L;

        @Override
        public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
                final boolean isSelected, final boolean cellHasFocus) {
            final JLabel label =
                    (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            final Sign type = (Sign) value;
            label.setIcon(DetectionIconFactory.INSTANCE.getIcon(type, true));
            label.setText("");
            label.setToolTipText(type.getName());
            return label;
        }
    }
}