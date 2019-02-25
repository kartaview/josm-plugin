package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;


import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.data.osm.SimplePrimitiveId;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.progress.NullProgressMonitor;
import org.openstreetmap.josm.io.MultiFetchServerObjectReader;
import org.openstreetmap.josm.io.OsmTransferException;
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DownloadedNode;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DownloadedRelation;
import org.openstreetmap.josm.plugins.openstreetcam.entity.DownloadedWay;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmElement;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.gui.layer.OpenStreetCamLayer;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * The class handles the download matched OSM element action.
 *
 * @author laurad
 */

final class MatchedDataAction extends JosmAction {

    MatchedDataAction(final String shortcutText) {
        super(null, null, null, ShortcutFactory.getInstance().getShotrcut(shortcutText), true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Collection<OsmElement> osmElements = DataSet.getInstance().getSelectedDetection().getOsmElements();

        MultiFetchServerObjectReader reader = Util.retrieveServerObjectReader(osmElements);
        org.openstreetmap.josm.data.osm.DataSet result = null;
        try {
            result = reader.parseOsm(NullProgressMonitor.INSTANCE);
        } catch (OsmTransferException e1) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(MainApplication.getMainPanel(),
                    "Error retrieving OSM members from the OSM service", GuiConfig.getInstance().getWarningTitle(),
                    JOptionPane.WARNING_MESSAGE));
        }

        List<OsmElement> downloadedData = new ArrayList<>();

        for (OsmElement element : osmElements) {
            switch (element.getType()) {
                case NODE:
                    handleNode(result, downloadedData, element);
                    break;
                case WAY:
                    handleWay(result, downloadedData, element);
                    break;
                case WAY_SECTION:
                    handleWay(result, downloadedData, element);
                    break;
                case RELATION:
                    handleRelation(result, downloadedData, element);
                    break;
            }
        }

        if (!downloadedData.isEmpty()) {
            DataSet.getInstance().setMatchedData(downloadedData);
            OpenStreetCamLayer.getInstance().invalidate();
        }
    }

    /**
     * Matches an OSM relation to the retrieved information and create an appropriate {@code DownloadedRelation}
     * containing the structural Node elements.
     *
     * @param result - josm DataSet containing the service response
     * @param downloadedData - the List of OsmElements where the {@code DownloadedRelation} is added
     * @param element - the OsmElement of type RELATION to be matched in the service result
     */
    private void handleRelation(final org.openstreetmap.josm.data.osm.DataSet result,
            final List<OsmElement> downloadedData, final OsmElement element) {
        final DownloadedRelation downloadedRelation = new DownloadedRelation(element);
        final List<OsmElement> downloadedWays = new ArrayList<>();
        if (element.getMembers() != null) {
            for (OsmElement member : element.getMembers()) {
                handleWay(result, downloadedWays, member);
            }
            if (element.getMembers().size() == downloadedWays.size()) {
                for (OsmElement downloadedWay : downloadedWays) {
                    downloadedRelation.addMember((DownloadedWay) downloadedWay);
                }
                downloadedData.add(downloadedRelation);
            }
        }
    }

    /**
     * Matches an OSM way to the retrieved information and create an appropriate {@code DownloadedWay}
     * containing the structural Node elements.
     *
     * @param result - josm DataSet containing the service response
     * @param downloadedData - the List of OsmElements where the {@code DownloadedWay} is added
     * @param element - the OsmElement of type WAY to be matched in the service result
     */
    private void handleWay(final org.openstreetmap.josm.data.osm.DataSet result, final List<OsmElement> downloadedData,
            final OsmElement element) {
        if (element.getFromId() != null && element.getToId() != null) {
            Node fromPrimitive =
                    (Node) result.getPrimitiveById(new SimplePrimitiveId(element.getFromId(), OsmPrimitiveType.NODE));
            Node toPrimitive =
                    (Node) result.getPrimitiveById(new SimplePrimitiveId(element.getToId(), OsmPrimitiveType.NODE));
            if (fromPrimitive == null || toPrimitive == null) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(MainApplication.getMainPanel(),
                        "Way " + element.getOsmId() + " was not found in the map.",
                        GuiConfig.getInstance().getWarningTitle(), JOptionPane.WARNING_MESSAGE));
            } else {
                downloadedData.add(new DownloadedWay(element, fromPrimitive, toPrimitive));
            }
        } else if (element.getOsmId() != null) {
            Way downloadedWay =
                    (Way) result.getPrimitiveById(new SimplePrimitiveId(element.getOsmId(), OsmPrimitiveType.WAY));
            if (downloadedWay == null) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(MainApplication.getMainPanel(),
                        "Way " + element.getOsmId() + " was not found in the map.",
                        GuiConfig.getInstance().getWarningTitle(), JOptionPane.WARNING_MESSAGE));
            } else {
                downloadedData.add(new DownloadedWay(element, downloadedWay.getNodes()));
            }
        }
    }

    /**
     * Matches an OSM Node to the retrieved information and create an appropriate {@code DownloadedNode}
     * containing the node location.
     *
     * @param result - josm DataSet containing the service response
     * @param downloadedData - the List of OsmElements where the {@code DownloadedNode} is added
     * @param element - the OsmElement of type NODE to be matched in the service result
     */
    private void handleNode(final org.openstreetmap.josm.data.osm.DataSet result, final List<OsmElement> downloadedData,
            final OsmElement element) {
        if (element.getOsmId() != null) {
            Node downloadedNode =
                    (Node) result.getPrimitiveById(new SimplePrimitiveId(element.getOsmId(), OsmPrimitiveType.NODE));
            if (downloadedNode == null) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(MainApplication.getMainPanel(),
                        " Node " + element.getOsmId() + " was not found in the map.",
                        GuiConfig.getInstance().getWarningTitle(), JOptionPane.WARNING_MESSAGE));
            } else {
                downloadedData.add(new DownloadedNode(element, downloadedNode));
            }
        }
    }
}