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
import org.openstreetmap.josm.plugins.openstreetcam.DataSet;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Cluster;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
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


    ClusterButtonPanel() {
        super(3);
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
        final boolean enabled = DataSet.getInstance().selectedClusterHasOsmElements();
        final JosmAction action = new MatchedDataAction(GuiConfig.getInstance().getBtnMatchedDataShortcutText(), true);
        final String tooltip = GuiConfig.getInstance().getBtnMatchedDataTlt().replace(SHORTCUT,
                action.getShortcut().getKeyText());
        btnMatchedData = ButtonBuilder.build(action, IconConfig.getInstance().getMatchedWayIcon(), tooltip, enabled);
        add(btnMatchedData);
    }

    void updateUI(final Cluster cluster) {
        boolean enablePhotoButtons = false;
        boolean enableMatchedDataButton = false;
        if (cluster != null) {
            enableMatchedDataButton = DataSet.getInstance().selectedClusterHasOsmElements();
            enablePhotoButtons = cluster.getDetectionIds() != null && cluster.getDetectionIds().size() > 1;
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
}