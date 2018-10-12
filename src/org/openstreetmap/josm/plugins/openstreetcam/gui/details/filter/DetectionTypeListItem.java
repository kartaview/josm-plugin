package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import com.telenav.josm.common.gui.builder.CheckBoxBuilder;

import com.telenav.josm.common.gui.builder.ListBuilder;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;


class DetectionTypeListItem extends JPanel {

    private JCheckBox selectType;
    private JList signList;

    DetectionTypeListItem(String labelName, List<Sign> signs) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        selectType = CheckBoxBuilder.build(labelName, Font.PLAIN, null, false);
        add(selectType);
        signList = ListBuilder.build(signs, null, new DetectionTypeListRenderer(), Font.PLAIN);
        signList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        signList.setVisibleRowCount(2);
        add(signList);
        setAlignmentX(Component.LEFT_ALIGNMENT);
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