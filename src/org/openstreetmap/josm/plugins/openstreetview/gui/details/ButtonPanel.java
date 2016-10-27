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
import java.awt.event.KeyEvent;
import java.net.URI;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Preferences.PreferenceChangeEvent;
import org.openstreetmap.josm.data.Preferences.PreferenceChangedListener;
import org.openstreetmap.josm.plugins.openstreetview.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetview.observer.LocationObservable;
import org.openstreetmap.josm.plugins.openstreetview.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetview.observer.SequenceObservable;
import org.openstreetmap.josm.plugins.openstreetview.observer.SequenceObserver;
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
class ButtonPanel extends JPanel implements LocationObservable, SequenceObservable, PreferenceChangedListener {

    private static final long serialVersionUID = -2909078640977666884L;

    private static final String NEXT_PHOTO = "next photo";
    private static final String PREVIOUS_PHOTO = "previous photo";

    private static final Dimension DIM = new Dimension(200, 24);
    private static final int ROWS = 1;
    private static final int COLS = 5;

    /* the panel's components */
    private final JButton btnFilter;
    private final JButton btnPrevious;
    private final JButton btnNext;
    private final JButton btnLocation;
    private final JButton btnWebPage;
    private final JButton btnFeedbackPage;

    /* notifies the plugin main class, if the user click's on the location button */
    private LocationObserver locationObserver;
    private SequenceObserver sequenceObserver;


    /* the currently selected photo */
    private Photo photo;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();
        final Icon icon = PreferenceManager.getInstance().loadListFilter().isDefaultFilter()
                ? iconConfig.getFilterIcon() : iconConfig.getFilterSelectedIcon();
        btnFilter = GuiBuilder.buildButton(new DisplayFilterDialogAction(), icon, guiConfig.getBtnFilterTlt(), true);
        btnPrevious = GuiBuilder.buildButton(new SelectPhotoAction(false), iconConfig.getPreviousIcon(),
                guiConfig.getBtnPreviousTlt(), false);
        btnNext = GuiBuilder.buildButton(new SelectPhotoAction(true), iconConfig.getNextIcon(),
                guiConfig.getBtnNextTlt(), false);
        btnLocation = GuiBuilder.buildButton(new JumpToLocationAction(), iconConfig.getLocationIcon(),
                guiConfig.getBtnLocationTlt(), false);
        btnWebPage = GuiBuilder.buildButton(new OpenWebPageAction(), iconConfig.getWebPageIcon(),
                guiConfig.getBtnWebPageTlt(), false);
        btnFeedbackPage = GuiBuilder.buildButton(new OpenFeedbackPageAction(), iconConfig.getFeedbackIcon(),
                guiConfig.getBtnFeedbackTlt(), true);

        add(btnFilter);
        add(btnPrevious);
        add(btnNext);
        add(btnLocation);
        add(btnWebPage);
        add(btnFeedbackPage);

        Main.map.mapView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_DOWN_MASK), PREVIOUS_PHOTO);
        Main.map.mapView.getActionMap().put(PREVIOUS_PHOTO, new SelectPhotoAction(false));
        Main.map.mapView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK), NEXT_PHOTO);
        Main.map.mapView.getActionMap().put(NEXT_PHOTO, new SelectPhotoAction(true));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_DOWN_MASK), PREVIOUS_PHOTO);
        getActionMap().put(PREVIOUS_PHOTO, new SelectPhotoAction(false));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK), NEXT_PHOTO);
        getActionMap().put(NEXT_PHOTO, new SelectPhotoAction(true));

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
            enableSequenceActions(false, false);
            btnLocation.setEnabled(false);
            btnWebPage.setEnabled(false);
        }
    }

    /**
     * Enables or disables the "OpenStreetView Sequence" related action buttons.
     *
     * @param isPrevious if true then the "Previous" button is enabled; if false then the button is disabled
     * @param isNext if true then the "Next" button is enabled; if false then the button is disabled
     */
    void enableSequenceActions(final boolean isPrevious, final boolean isNext) {
        btnPrevious.setEnabled(isPrevious);
        btnNext.setEnabled(isNext);
    }

    @Override
    public void registerObserver(final LocationObserver locationObserver) {
        this.locationObserver = locationObserver;
    }

    @Override
    public void notifyObserver() {
        this.locationObserver.zoomToSelectedPhoto();
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

    @Override
    public void registerObserver(final SequenceObserver sequenceObserver) {
        this.sequenceObserver = sequenceObserver;
    }

    @Override
    public void notifyObserver(final int index) {
        this.sequenceObserver.selectSequencePhoto(index);
    }


    /* selects the previous/next photo from the displayed sequence */
    private final class SelectPhotoAction extends AbstractAction {

        private static final long serialVersionUID = 191591505362305396L;

        private final boolean isNext;

        private SelectPhotoAction(final boolean isNext) {
            this.isNext = isNext;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (photo != null) {
                final int index = isNext ? photo.getSequenceIndex() + 1 : photo.getSequenceIndex() - 1;
                notifyObserver(index);
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