package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Detection;
import org.openstreetmap.josm.plugins.openstreetcam.observer.RowSelectionObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.RowSelectionObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import com.telenav.josm.common.formatter.DateFormatter;
import com.telenav.josm.common.formatter.DecimalPattern;
import com.telenav.josm.common.formatter.EntityFormatter;


/**
 * Builds a table with information about the selected cluster's detections.
 *
 * @author nicoletav
 */
class DetectionTable extends JTable implements RowSelectionObservable {

    private static final long serialVersionUID = 1L;
    private static final int ROW_HEIGHT = 15;
    private static final int TABLE_COLUMNS_EXTRA_WIDTH = 12;
    private static final int ID_COLUMN = 0;

    private Cluster cluster;
    private int tableWidth = 0;
    RowSelectionObserver observer;

    /**
     * @param cluster represents the selected cluster from the map
     */
    public DetectionTable(final Cluster cluster) {
        super();
        this.cluster = cluster;

        final List<Detection> detections = cluster.getDetections();
        final int detectionsNr = detections.size();
        final String headerComponents = GuiConfig.getInstance().getClusterTableHeader();
        final List<String> header = Arrays.asList(headerComponents.split("/"));
        final int columnsNr = header.size();

        List<String[]> model = new ArrayList<String[]>();

        for (int i = 0; i < detectionsNr; ++i) {
            final String id = detections.get(i).getId().toString();
            final String creation = DateFormatter.formatTimestamp(detections.get(i).getCreationTimestamp());
            final String update = DateFormatter.formatTimestamp(detections.get(i).getLatestChangeTimestamp());
            final String validationStatus = detections.get(i).getValidationStatus().toString();
            final String editStatus = detections.get(i).getEditStatus().toString();
            String confLevel = "0";
            if (detections.get(i).getConfidenceLevel() != 0.0) {
                confLevel = EntityFormatter.formatDouble(detections.get(i).getConfidenceLevel(), false,
                        DecimalPattern.SHORT);
            }
            final String facing =
                    EntityFormatter.formatDouble(detections.get(i).getFacing(), false, DecimalPattern.SHORT);
            String distance = "0";
            if (detections.get(i).getDistance() != 0.0) {
                distance = EntityFormatter.formatDouble((double) detections.get(i).getDistance(), false,
                        DecimalPattern.SHORT);
            }
            final String angleFromCenter = EntityFormatter.formatDouble((double) detections.get(i).getAngleFromCenter(),
                    false, DecimalPattern.SHORT);
            String orientation = "0";
            if (detections.get(i).getOrientation() != 0.0) {
                orientation = EntityFormatter.formatDouble((double) detections.get(i).getOrientation(), false,
                        DecimalPattern.SHORT);
            }

            model.add(new String[] { id, creation, update, validationStatus, editStatus, confLevel, facing, distance,
                    angleFromCenter, orientation });
        }
        TableModel tableModel = new DefaultTableModel(model.toArray(new Object[][] {}), header.toArray()) {

            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        this.setModel(tableModel);
        final TableColumnModel columnModel = this.getColumnModel();
        for (int columnIndex = 0; columnIndex < columnsNr; ++columnIndex) {
            String currentHeader = header.get(columnIndex);
            int maxColumnWidth = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(currentHeader.toString());
            for (int detectionIndex = 0; detectionIndex < detectionsNr; ++detectionIndex) {
                String[] currentRow = model.get(detectionIndex);
                final int widthValue =
                        getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(currentRow[columnIndex]);
                if (widthValue > maxColumnWidth) {
                    maxColumnWidth = widthValue;
                }
            }
            maxColumnWidth += TABLE_COLUMNS_EXTRA_WIDTH;
            columnModel.getColumn(columnIndex).setPreferredWidth(maxColumnWidth);
            tableWidth += maxColumnWidth;
        }
        this.setRowHeight(ROW_HEIGHT);
        this.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                Detection selectedDetection = getSelectedDetection();
                notifyRowSelectionObserver(selectedDetection);
            }
        });
    }

    private Detection getSelectedDetection() {
        Detection selectedDetection = null;
        final Long selectedDetectionId = Long.valueOf(this.getValueAt(this.getSelectedRow(), ID_COLUMN).toString());
        final List<Detection> detectionsList = cluster.getDetections();
        final int detectionsNr = detectionsList.size();
        int index = 0;
        while (index < detectionsNr) {
            long currentDetectionId = detectionsList.get(index).getId();
            if (currentDetectionId == selectedDetectionId) {
                selectedDetection = detectionsList.get(index);
            }
            ++index;
        }

        return selectedDetection;
    }

    public Cluster getCluster() {
        return cluster;
    }


    public int getTableWidth() {
        return tableWidth;
    }


    @Override
    public void registerObserver(final RowSelectionObserver observer) {
        this.observer = observer;

    }

    @Override
    public void notifyRowSelectionObserver(final Detection detection) {
        observer.selectDetectionFromTable(detection);
    }
}