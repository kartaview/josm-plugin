package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DetectionTypeList extends JPanel {

    private List<DetectionTypeListItem> listItems;

    DetectionTypeList() {
        if (listItems == null) {
            listItems = new ArrayList<>();
            Map<String, List<Sign>> allSigns = DetectionTypeContent.getInstance().getContent();
            allSigns.keySet().forEach(key -> listItems.add(new DetectionTypeListItem(key, allSigns.get(key))));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            listItems.forEach(this::add);
        }
    }

    public void setEnabled(final boolean enabled) {
        for (DetectionTypeListItem detectionItem : listItems) {
            detectionItem.setEnabled(enabled);
        }
    }

    void clearSelection() {
        for (DetectionTypeListItem detectionItem : listItems) {
            detectionItem.setSelected(false);
            detectionItem.clearSelection();
        }
    }

    void selectAll() {
        for (DetectionTypeListItem detectionItem : listItems) {
            detectionItem.setSelected(true);
        }
    }

    List<String> getSelectedTypes() {
        List<String> selectedTypes = new ArrayList<>();
        for (DetectionTypeListItem detectionItem : listItems) {
            if (detectionItem.isTypeSelected()) {
                selectedTypes.add(detectionItem.getTypeName());
            }
        }
        return selectedTypes.isEmpty() ? null : selectedTypes;
    }

    List<Sign> getSelectedValues() {
        List<Sign> selectedValues = new ArrayList<>();
        for (DetectionTypeListItem detectionItem : listItems) {
            if (!detectionItem.isTypeSelected()) {
                selectedValues.addAll(detectionItem.getSignList());
            }
        }
        return selectedValues.isEmpty() ? null : selectedValues;
    }

}