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
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetview.observer.LocationObservable;
import org.openstreetmap.josm.plugins.openstreetview.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.ServiceConfig;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Defines the {@code OpenStreetViewDetailsDialog} action panel. The user can perform the following actions: filter,
 * jump to photo location and open photo web page.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel implements LocationObservable {

    private static final long serialVersionUID = -2909078640977666884L;
    private static final int ROWS = 1;
    private static final int COLS = 5;
    private static final Dimension DIM = new Dimension(200, 23);

    private LocationObserver observer;

    private final JButton btnFilter;
    private final JButton btnLocation;
    private final JButton btnWebPage;

    private Photo photo;

    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();
        btnFilter = GuiBuilder.buildButton(new DisplayFilterDialogAction(), iconConfig.getFilterIcon(),
                guiConfig.getBtnFilterTlt(), true);
        btnLocation = GuiBuilder.buildButton(new JumpToLocationAction(), iconConfig.getLocationIcon(),
                guiConfig.getBtnLocationTlt(), false);
        btnWebPage = GuiBuilder.buildButton(new OpenWebPageAction(), iconConfig.getWebPageIcon(),
                guiConfig.getBtnWebPageTlt(), false);

        add(btnFilter);
        add(btnLocation);
        add(btnWebPage);
        setPreferredSize(DIM);
    }


    void enablePanelActions(final Photo photo) {
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

    private final class DisplayFilterDialogAction extends AbstractAction {

        private static final long serialVersionUID = 3384826283452064630L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            final FilterDialog filterDialog = new FilterDialog();
            filterDialog.setVisible(true);
        }

    }

    private final class JumpToLocationAction extends AbstractAction {

        private static final long serialVersionUID = 6824741346944799071L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (photo != null) {
                notifyObserver();
            }
        }

    }

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
                    JOptionPane.showMessageDialog(Main.parent, "Could not open image page", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

}