package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This class creates the component containing the detection type lists.
 *
 * @author laurad
 */
public class DetectionTypeList extends JPanel {

    private List<DetectionTypeListItem> listItems;

    DetectionTypeList(final List<String> selectedSignTypes, final List<Sign> selectedSpecificSigns) {
        if (listItems == null) {
            listItems = new ArrayList<>();
            Map<String, List<Sign>> allSigns = DetectionTypeContent.getInstance().getContent();
            for (String key : allSigns.keySet()) {
                boolean typeSelected = false;
                if(selectedSignTypes != null && !selectedSignTypes.isEmpty()){
                    typeSelected = selectedSignTypes.contains(key);
                }
                List<Sign> selectedSigns = null;
                if (selectedSpecificSigns != null && !selectedSpecificSigns.isEmpty()) {
                    selectedSigns = new ArrayList<>(allSigns.get(key));
                    selectedSigns.retainAll(selectedSpecificSigns);
                }
                listItems.add(new DetectionTypeListItem(key, typeSelected, allSigns.get(key), selectedSigns));
            }
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