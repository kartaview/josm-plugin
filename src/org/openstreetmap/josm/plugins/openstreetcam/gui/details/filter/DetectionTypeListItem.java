package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import com.telenav.josm.common.gui.builder.CheckBoxBuilder;

import com.telenav.josm.common.gui.builder.ListBuilder;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.util.List;


/**
 * This class maps an element from the detection type component.
 *
 * @author laurad
 */
class DetectionTypeListItem extends JPanel {

    private final JCheckBox signType;
    private final JList signList;
    private boolean ignoreCheckboxSelection = false;

    DetectionTypeListItem(String labelName, final boolean typeSelected, List<Sign> signs, List<Sign> selectedSigns) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        String prettyName = labelName.replace("_", " ");
        signType = CheckBoxBuilder.build(prettyName, Font.PLAIN, Color.WHITE, typeSelected);
        signType.setAlignmentX(Component.LEFT_ALIGNMENT);
        signType.setName(labelName);
        signType.addItemListener((final ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectAll();
            } else {
                if (ignoreCheckboxSelection) {
                    ignoreCheckboxSelection = false;
                } else {
                    clearSelection();
                }
            }
        });
        add(signType);

        signList = ListBuilder.build(signs, selectedSigns, new DetectionTypeListRenderer(), Font.PLAIN);
        signList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        signList.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (typeSelected) {
            selectAll();
        }
        int size = signs.size() % 8 == 0 ? signs.size() / 8 : signs.size() / 8 + 1; //TODO calculate size based on width
        signList.setVisibleRowCount(size);
        ListSelectionModel listSelectionModel = signList.getSelectionModel();
        listSelectionModel.addListSelectionListener((final ListSelectionEvent e) -> {
            if (getSignList().size() != signs.size()) {
                ignoreCheckboxSelection = true;
                signType.setSelected(false);
            }

        });
        add(signList);
        setBackground(Color.WHITE);
    }

    private void selectAll() {
        final int end = signList.getModel().getSize() - 1;
        signList.setSelectionInterval(0, end);
    }

    void clearSelection() {
        signList.clearSelection();
    }

    public void setEnabled(final boolean enabled) {
        signType.setEnabled(enabled);
        signList.setEnabled(enabled);
    }

    public void setSelected(final boolean selected) {
        signType.setSelected(selected);
    }

    boolean isTypeSelected() {
        return signType.isSelected();
    }

    String getTypeName() {
        return signType.getName();
    }

    List getSignList() {
        return signList.getSelectedValuesList();
    }

}


class DetectionTypeListRenderer extends DefaultListCellRenderer {

    private final Dimension SIZE = new Dimension(40, 40);

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        final Sign type = (Sign) value;
        label.setIcon(DetectionIconFactory.INSTANCE.getIcon(type, false));
        label.setText("");
        label.setPreferredSize(SIZE);
        label.setToolTipText(type.getName());
        return label;
    }
}