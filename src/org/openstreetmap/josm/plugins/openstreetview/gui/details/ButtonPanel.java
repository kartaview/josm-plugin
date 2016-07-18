/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetview.gui.details;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Preferences.PreferenceChangeEvent;
import org.openstreetmap.josm.data.Preferences.PreferenceChangedListener;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetview.observer.LocationObservable;
import org.openstreetmap.josm.plugins.openstreetview.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.ServiceConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Defines the {@code OpenStreetViewDetailsDialog} action panel. The user can perform the following actions: filter,
 * jump to photo location and open photo web page.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel implements LocationObservable, PreferenceChangedListener {

    private static final long serialVersionUID = -2909078640977666884L;

    private static final Dimension DIM = new Dimension(200, 23);
    private static final int ROWS = 1;
    private static final int COLS = 5;

    /* the panel's components */
    private final JButton btnFilter;
    private final JButton btnLocation;
    private final JButton btnWebPage;
    private final JButton btnFeedbackPage;

    /* notifies the plugin main class, if the user click's on the location button */
    private LocationObserver observer;

    /* the currently selected photo */
    private Photo photo;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();
        final Icon icon = PreferenceManager.getInstance().loadListFilter().isDefaultFilter()
                ? iconConfig.getFilterIcon() : iconConfig.getFilterSelectedIcon();
        btnFilter = GuiBuilder.buildButton(new DisplayFilterDialogAction(), icon, guiConfig.getBtnFilterTlt(), true);
        btnLocation = GuiBuilder.buildButton(new JumpToLocationAction(), iconConfig.getLocationIcon(),
                guiConfig.getBtnLocationTlt(), false);
        btnWebPage = GuiBuilder.buildButton(new OpenWebPageAction(), iconConfig.getWebPageIcon(),
                guiConfig.getBtnWebPageTlt(), false);
        btnFeedbackPage = GuiBuilder.buildButton(new OpenFeedbackPageAction(), iconConfig.getFeedbackIcon(),
                guiConfig.getBtnFeedbackTlt(), true);

        add(btnFilter);
        add(btnLocation);
        add(btnWebPage);
        add(btnFeedbackPage);
        setPreferredSize(DIM);
        Main.pref.addPreferenceChangeListener(this);
    }

    /**
     * Updates the UI components according to the selected photo. If the photo is not null then the location and web
     * page buttons becomes enabled; otherwise disabled.
     *
     * @param photo the currently selected photo
     */
    void updateUI(final Photo photo) {
        this.photo = photo;
        if (photo != null) {
            btnLocation.setEnabled(true);
            btnWebPage.setEnabled(true);
        } else {
            btnLocation.setEnabled(false);
            btnWebPage.setEnabled(false);
        }
    }

    @Override
    public void registerObserver(final LocationObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyObserver() {
        this.observer.zoomToSelectedPhoto();
    }

    @Override
    public void preferenceChanged(final PreferenceChangeEvent event) {
        if (event != null && (event.getNewValue() != null && !event.getNewValue().equals(event.getOldValue()))) {
            if (event.getKey().equals(PreferenceManager.getInstance().getFiltersChangedFlagKey())) {
                final Icon icon = PreferenceManager.getInstance().loadListFilter().isDefaultFilter()
                        ? IconConfig.getInstance().getFilterIcon() : IconConfig.getInstance().getFilterSelectedIcon();
                btnFilter.setIcon(icon);
            }
        }
    }

    /* centers the map to the selected photo's location */
    private final class JumpToLocationAction extends AbstractAction {

        private static final long serialVersionUID = 6824741346944799071L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (photo != null) {
                notifyObserver();
            }
        }
    }

    /* opens the selected photo's web page */
    private final class OpenWebPageAction extends AbstractAction {

        private static final long serialVersionUID = -1443190917019829709L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (photo != null) {
                final StringBuilder link = new StringBuilder(ServiceConfig.getInstance().getPhotoDetailsUrl());
                link.append(photo.getSequenceId()).append("/").append(photo.getSequenceIndex());
                try {
                    OpenBrowser.displayUrl(new URI(link.toString()));
                } catch (final Exception e) {
                    JOptionPane.showMessageDialog(Main.parent, GuiConfig.getInstance().getErrorPhotoPageTxt(),
                            GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /* opens the feedback web page */
    private final class OpenFeedbackPageAction extends AbstractAction {

        private static final long serialVersionUID = 4196639030623647016L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            try {
                OpenBrowser.displayUrl(new URI(ServiceConfig.getInstance().getFeedbackUrl()));
            } catch (final Exception e) {
                JOptionPane.showMessageDialog(Main.parent, GuiConfig.getInstance().getErrorFeedbackPageTxt(),
                        GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}