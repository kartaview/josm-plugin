package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import com.telenav.josm.common.gui.builder.CheckBoxBuilder;

import com.telenav.josm.common.gui.builder.ListBuilder;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;

import javax.swing.*;
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

    private JCheckBox signType;
    private JList signList;

    DetectionTypeListItem(String labelName, final boolean typeSelected, List<Sign> signs, List<Sign> selectedSigns) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        signType = CheckBoxBuilder.build(labelName, Font.PLAIN, null, typeSelected);
        signType.setAlignmentX(Component.LEFT_ALIGNMENT);
        signType.addItemListener((final ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectAll();
            } else {
                clearSelection();
            }
        });
        add(signType);

        signList = ListBuilder.build(signs, selectedSigns, new DetectionTypeListRenderer(), Font.PLAIN);
        signList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        signList.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(typeSelected){
            selectAll();
        }
        int size = signs.size() % 8 == 0 ? signs.size() / 8 : signs.size() / 8 + 1;
        signList.setVisibleRowCount(size);
        add(signList);
    }

    private void selectAll() {
        final int end = signList.getModel().getSize() - 1;
        signList.setSelectionInterval(0, end);
    }

    void clearSelection() {
        signList.clearSelection();
    }

    public void setEnabled(final boolean enabled){
        signType.setEnabled(enabled);
        signList.setEnabled(enabled);
    }

    public void setSelected(final boolean selected){
        signType.setSelected(selected);
    }

    boolean isTypeSelected(){
        return signType.isSelected();
    }

    String getTypeName(){
        return signType.getText();
    }

    List getSignList(){
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