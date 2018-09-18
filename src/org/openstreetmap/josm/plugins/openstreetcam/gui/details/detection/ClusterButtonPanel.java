/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.gui.details.common.DownloadMatchedOsmElement;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClusterObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClusterObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import com.telenav.josm.common.gui.builder.ButtonBuilder;


/**
 * Defines a button panel with cluster specific actions that an user can perform. If a cluster (aggregated detection)
 * is selected a user can: visualize the next/previous photo belonging to the cluster and download the corresponding
 * OSM element to which the cluster was matched.
 *
 * @author beataj
 * @version $Revision$
 */
class ClusterButtonPanel extends BaseButtonPanel implements ClusterObservable {

    private static final long serialVersionUID = 8371616785853793886L;

    private ClusterObserver clusterObserver;

    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnMatchedData;

    private Cluster cluster;


    ClusterButtonPanel() {
        super();
    }


    @Override
    void createComponents() {
        addPreviousButton();
        addNextButton();
        addMatchedDataButton();
    }

    private void addPreviousButton() {
        final JosmAction action =
                new SelectPhotoAction(GuiConfig.getInstance().getBtnClusterPreviousShortcutText(), false);
        final String tooltip =
                GuiConfig.getInstance().getBtnPreviousTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnPrevious = ButtonBuilder.build(action, IconConfig.getInstance().getPreviousIcon(), tooltip, false);
        add(btnPrevious);
    }

    private void addNextButton() {
        final JosmAction action = new SelectPhotoAction(GuiConfig.getInstance().getBtnClusterNextShortcutText(), true);
        final String tooltip =
                GuiConfig.getInstance().getBtnNextTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnNext = ButtonBuilder.build(action, IconConfig.getInstance().getNextIcon(), tooltip, false);
        add(btnNext);
    }

    private void addMatchedDataButton() {
        final JosmAction action = new DownloadMatchedData();
        final String tooltip = GuiConfig.getInstance().getBtnClusterMatchedDataTlt().replace(SHORTCUT,
                action.getShortcut().getKeyText());
        btnMatchedData = ButtonBuilder.build(action, IconConfig.getInstance().getMatchedWayIcon(), tooltip, false);
        add(btnMatchedData);
    }

    void updateUI(final Cluster cluster) {
        this.cluster = cluster;
        boolean enablePhotoButtons = false;
        boolean enableMatchedDataButton = false;
        if (cluster != null) {
            enableMatchedDataButton = cluster.getOsmElement() != null && cluster.getOsmElement().getOsmId() != null;
            enablePhotoButtons = cluster.getDetectionIds() != null && cluster.getDetectionIds().size() > 0;
        }
        btnNext.setEnabled(enablePhotoButtons);
        btnPrevious.setEnabled(enablePhotoButtons);
        btnMatchedData.setEnabled(enableMatchedDataButton);
    }

    @Override
    public void registerObserver(final ClusterObserver clusterObserver) {
        this.clusterObserver = clusterObserver;
    }

    @Override
    public void notifyObserver(final boolean isNext) {
        this.clusterObserver.selectPhoto(isNext);
    }

    /**
     * Selects the next or previous photo belonging to the cluster. The next/previous photo is computed related to the
     * currently selected photo.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class SelectPhotoAction extends JosmAction {

        private static final long serialVersionUID = 708072028661003225L;
        private final boolean isNext;

        private SelectPhotoAction(final String shortcutText, final boolean isNext) {
            super(shortcutText, null, shortcutText, ShortcutFactory.getInstance().getShotrcut(shortcutText), true);
            this.isNext = isNext;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            notifyObserver(isNext);
        }
    }

    /**
     * Downloads the OSM data to which the cluster is matched.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class DownloadMatchedData extends DownloadMatchedOsmElement {

        private static final long serialVersionUID = 7626759776502632881L;

        private DownloadMatchedData() {
            super(GuiConfig.getInstance().getBtnClusterMatchedDataTlt(),
                    GuiConfig.getInstance().getBtnClusterMatchedDataTlt());
        }

        @Override
        protected PrimitiveId getPrimitiveId() {
            return cluster.getOsmElement() != null ? cluster.getOsmElement().getPrimitiveId() : null;
        }
    }
}