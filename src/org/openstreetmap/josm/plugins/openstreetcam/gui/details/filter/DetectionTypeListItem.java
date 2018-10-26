package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;
import org.openstreetmap.josm.plugins.openstreetcam.gui.DetectionIconFactory;
import com.telenav.josm.common.gui.builder.CheckBoxBuilder;
import com.telenav.josm.common.gui.builder.ListBuilder;


/**
 * This class maps an element from the detection type component.
 *
 * @author laurad
 */
class DetectionTypeListItem extends JPanel {

    private static final long serialVersionUID = -7115447482264760072L;
    private final JCheckBox signType;
    private final JList<Sign> signList;

    DetectionTypeListItem(final String labelName, final boolean typeSelected, final List<Sign> signs, final List<Sign> selectedSigns) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBackground(Color.WHITE);

        final String prettyName = labelName.replace("_", " ");
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
        final int size = signs.size() % 8 == 0 ? signs.size() / 8 : signs.size() / 8 + 1; //TODO calculate size based on width
        signList.setVisibleRowCount(size);
        final ListSelectionModel listSelectionModel = signList.getSelectionModel();
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

    private class DetectionTypeListRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 7022769009126337948L;
        private final Dimension SIZE = new Dimension(40, 40);

        @Override
        public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
                final boolean isSelected, final boolean cellHasFocus) {
            final JLabel label =
                    (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            final Sign type = (Sign) value;
            label.setIcon(DetectionIconFactory.INSTANCE.getIcon(type, false));
            label.setText("");
            label.setPreferredSize(SIZE);
            label.setToolTipText(type.getName());
            return label;
        }
    }
}