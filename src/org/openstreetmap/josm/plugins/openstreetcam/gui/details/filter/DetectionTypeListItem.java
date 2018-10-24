package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import com.telenav.josm.common.gui.builder.CheckBoxBuilder;

import com.telenav.josm.common.gui.builder.ListBuilder;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;


/**
 * This class maps an element from the detection type component.
 *
 * @author laurad
 */
class DetectionTypeListItem extends JPanel {

    private final JCheckBox signType;
    private final JList signList;

    DetectionTypeListItem(String labelName, final boolean typeSelected, List<Sign> signs, List<Sign> selectedSigns) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBackground(Color.WHITE);

        String prettyName = labelName.replace("_", " ");
        signType = CheckBoxBuilder.build(prettyName, Font.PLAIN, Color.WHITE, typeSelected);
        signType.setAlignmentX(Component.LEFT_ALIGNMENT);
        signType.setName(labelName);
        signType.addActionListener((final ActionEvent e) -> {
            if (signType.isSelected()) {
                selectAll();
            } else {
                clearSelection();
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
                signType.setSelected(false);
            }else{
                signType.setSelected(true);
            }

        });
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