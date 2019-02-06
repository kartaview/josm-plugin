package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Sign;


/**
 * This class creates the component containing the detection type lists.
 *
 * @author laurad
 */
class DetectionTypeList extends JPanel {

    private static final long serialVersionUID = 212486274590615046L;
    private final List<DetectionTypeListItem> listItems;
    private final Map<String, List<Sign>> allSigns;

    DetectionTypeList() {
        listItems = new ArrayList<>();
        allSigns = DetectionTypeContent.getInstance().getContent();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBackground(Color.WHITE);
    }

    void populateDetectionList(final List<String> selectedSignTypes, final List<Sign> selectedSpecificSigns,
            final String region) {
        listItems.clear();
        if (allSigns != null) {
            for (final String key : allSigns.keySet()) {
                final List<Sign> signsToDisplay = region == null || region.isEmpty() ? allSigns.get(key) :
                        filterRegionSigns(allSigns.get(key), region);
                if (signsToDisplay != null && !signsToDisplay.isEmpty()) {
                    boolean typeSelected = false;
                    if (selectedSignTypes != null && !selectedSignTypes.isEmpty()) {
                        typeSelected = selectedSignTypes.contains(key);
                    }
                    List<Sign> selectedSigns = null;
                    if (selectedSpecificSigns != null && !selectedSpecificSigns.isEmpty()) {
                        selectedSigns = new ArrayList<>(signsToDisplay);
                        selectedSigns.retainAll(selectedSpecificSigns);
                    }
                    final DetectionTypeListItem listItem =
                            new DetectionTypeListItem(key, typeSelected, signsToDisplay, selectedSigns);
                    listItems.add(listItem);
                }
            }
        }
        removeAll();
        listItems.forEach(this::add);
        revalidate();
    }

    private List<Sign> filterRegionSigns(final List<Sign> signs, final String region) {
        return signs.parallelStream().filter(sign -> sign.getRegion().equals(region)).collect(Collectors.toList());
    }

    @Override
    public void setEnabled(final boolean enabled) {
        for (final DetectionTypeListItem detectionItem : listItems) {
            detectionItem.setEnabled(enabled);
        }
    }

    void clearSelection() {
        for (final DetectionTypeListItem detectionItem : listItems) {
            detectionItem.clearSelection();
        }
    }

    void selectAll() {
        for (final DetectionTypeListItem detectionItem : listItems) {
            detectionItem.selectAll();
        }
    }

    List<String> getSelectedTypes() {
        final List<String> selectedTypes = new ArrayList<>();
        for (final DetectionTypeListItem detectionItem : listItems) {
            if (detectionItem.isTypeSelected()) {
                selectedTypes.add(detectionItem.getTypeName());
            }
        }
        return selectedTypes.isEmpty() ? null : selectedTypes;
    }

    List<Sign> getSelectedValues() {
        final List<Sign> selectedValues = new ArrayList<>();
        for (final DetectionTypeListItem detectionItem : listItems) {
            if (!detectionItem.isTypeSelected()) {
                selectedValues.addAll(detectionItem.getSignList());
            }
        }
        return selectedValues.isEmpty() ? null : selectedValues;
    }

}