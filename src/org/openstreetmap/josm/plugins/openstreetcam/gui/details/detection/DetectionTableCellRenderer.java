package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ValidationStatus;
import com.telenav.josm.common.formatter.DateFormatter;
import com.telenav.josm.common.formatter.DecimalPattern;
import com.telenav.josm.common.formatter.EntityFormatter;


/**
 * Defines a custom table cell renderer for {@code Detection} objects.
 *
 * @author nicoletav
 */
class DetectionTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    private static final String ZERO = "0";
    private static final double APROXIMATED_ZERO_DOUBLE = 0.0;
    private static final float APROXIMATED_ZERO_FLOAT = 0.0f;
    private static final Color HEADER_GRAY = new Color(235, 237, 239);
    private static final long  MIN_DATE_VALUE= 1000000000;

    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setFont(MainApplication.getMap().getFont().deriveFont(Font.PLAIN));
        if (value != null) {
            String txt = "-";
            if (value instanceof Long) {
                if ((long) value >= MIN_DATE_VALUE) {
                    txt = DateFormatter.formatTimestamp((Long) value);
                } else {
                    txt = value.toString();
                }
            } else if (value instanceof ValidationStatus || value instanceof EditStatus) {
                txt = value.toString();
            } else if (value instanceof Double) {
                if ((double) value != APROXIMATED_ZERO_DOUBLE) {
                    txt = EntityFormatter.formatDouble((double) value, false, DecimalPattern.SHORT);
                } else {
                    txt = ZERO;
                }
            } else if (value instanceof Float) {
                if ((float) value != APROXIMATED_ZERO_FLOAT) {
                    final double convertedValue = (float) value;
                    txt = EntityFormatter.formatDouble(convertedValue, false, DecimalPattern.SHORT);
                } else {
                    txt = ZERO;
                }
            } else if (value instanceof String) {
                txt = value.toString();
                setBackground(HEADER_GRAY);
                setBorder(new MatteBorder(0, 0, 1, 1, Color.gray));
            }
            setText(txt);
        }
        return this;
    }
}