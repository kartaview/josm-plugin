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
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

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
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClosestPhotoObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClosestPhotoObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DataTypeChangeObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DataTypeChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Defines the {@code OpenStreetCamDetailsDialog} action panel. The user can perform the following actions: filter, jump
 * to photo location and open photo web page.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel
        implements LocationObservable, SequenceObservable, ClosestPhotoObservable, DataTypeChangeObservable {

    private static final long serialVersionUID = -2909078640977666884L;

    private static final String NEXT_PHOTO = "next photo";
    private static final String PREVIOUS_PHOTO = "previous photo";
    private static final String CLOSEST_PHOTO = "closest photo";

    private static final Dimension DIM = new Dimension(200, 24);
    private static final int ROWS = 1;
    private static final int COLS = 5;

    /* the panel's components */
    private JButton btnDataSwitch;
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnLocation;
    private JButton btnWebPage;
    private JButton btnClosestPhoto;

    /* notifies the plugin main class */
    private LocationObserver locationObserver;
    private SequenceObserver sequenceObserver;
    private ClosestPhotoObserver closestPhotoObserver;
    private DataTypeChangeObserver dataUpdateObserver;


    /* the currently selected photo */
    private Photo photo;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));
        createComponents();
        registerShortcuts();
        setPreferredSize(DIM);

    }

    private void createComponents() {
        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();

        btnPrevious = GuiBuilder.buildButton(new SelectPhotoAction(false), iconConfig.getPreviousIcon(),
                guiConfig.getBtnPreviousTlt(), false);
        btnNext = GuiBuilder.buildButton(new SelectPhotoAction(true), iconConfig.getNextIcon(),
                guiConfig.getBtnNextTlt(), false);
        btnLocation = GuiBuilder.buildButton(new JumpToLocationAction(), iconConfig.getLocationIcon(),
                guiConfig.getBtnLocationTlt(), false);
        btnWebPage = GuiBuilder.buildButton(new OpenWebPageAction(), iconConfig.getWebPageIcon(),
                guiConfig.getBtnWebPageTlt(), false);
        btnClosestPhoto = GuiBuilder.buildButton(new ClosestPhotoAction(), iconConfig.getClosestImageIcon(),
                guiConfig.getBtnClosestImageTlt(), false);
        btnDataSwitch = GuiBuilder.buildButton(new ManualDataSwitchAction(), iconConfig.getManualSwitchImageIcon(),
                guiConfig.getBtnDataSwitchImageTlt(), false);
        btnDataSwitch.setActionCommand(DataType.PHOTO.toString());

        if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
            // add manual switch button
            add(btnDataSwitch);
        }

        add(btnPrevious);
        add(btnNext);
        add(btnClosestPhoto);
        add(btnLocation);
        add(btnWebPage);
    }

    private void registerShortcuts() {
        Main.map.mapView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_DOWN_MASK), PREVIOUS_PHOTO);
        Main.map.mapView.getActionMap().put(PREVIOUS_PHOTO, new SelectPhotoAction(false));
        Main.map.mapView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK), NEXT_PHOTO);
        Main.map.mapView.getActionMap().put(NEXT_PHOTO, new SelectPhotoAction(true));
        Main.map.mapView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.ALT_DOWN_MASK), CLOSEST_PHOTO);
        Main.map.mapView.getActionMap().put(CLOSEST_PHOTO, new ClosestPhotoAction());
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_DOWN_MASK), PREVIOUS_PHOTO);
        getActionMap().put(PREVIOUS_PHOTO, new SelectPhotoAction(false));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK), NEXT_PHOTO);
        getActionMap().put(NEXT_PHOTO, new SelectPhotoAction(true));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.ALT_DOWN_MASK), CLOSEST_PHOTO);
        getActionMap().put(CLOSEST_PHOTO, new ClosestPhotoAction());

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
     * Enables or disables the manual data switch button.
     *
     * @param enabled is true/false
     */
    void enableDataSwitchButton(final boolean enabled) {
        btnDataSwitch.setEnabled(enabled);
    }

    void setDataSwitchButtonVisibiliy(final boolean isVisible) {
        if (isVisible) {
            final GuiConfig guiConfig = GuiConfig.getInstance();
            final IconConfig iconConfig = IconConfig.getInstance();
            final boolean enabled =
                    Util.zoom(Main.map.mapView.getRealBounds()) >= Config.getInstance().getMapPhotoZoom();
            final Icon icon = Util.zoom(Main.map.mapView.getRealBounds()) >= PreferenceManager.getInstance()
                    .loadMapViewSettings().getPhotoZoom() ? iconConfig.getManualSwitchSegmentIcon()
                            : iconConfig.getManualSwitchImageIcon();
            final String tlt = PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()
                    ? guiConfig.getBtnDataSwitchImageTlt() : guiConfig.getBtnDataSwitchSegmentTlt();
            btnDataSwitch = GuiBuilder.buildButton(new ManualDataSwitchAction(), icon, tlt, enabled);
            btnDataSwitch.setActionCommand(DataType.PHOTO.toString());
            add(btnDataSwitch, 0);

        } else {
            remove(btnDataSwitch);
        }
    }

    void updateDataSwitchButton(final DataType dataType) {
        Icon icon;
        String tlt;
        String actionCommand;

        if (dataType.equals(DataType.PHOTO)) {
            icon = IconConfig.getInstance().getManualSwitchSegmentIcon();
            tlt = GuiConfig.getInstance().getBtnDataSwitchSegmentTlt();
            actionCommand = DataType.SEGMENT.toString();
        } else {
            icon = IconConfig.getInstance().getManualSwitchImageIcon();
            tlt = GuiConfig.getInstance().getBtnDataSwitchImageTlt();
            actionCommand = DataType.PHOTO.toString();
        }
        btnDataSwitch.setIcon(icon);
        btnDataSwitch.setToolTipText(tlt);
        btnDataSwitch.setActionCommand(actionCommand);
        revalidate();
        repaint();
    }


    /**
     * Enables or disables the "OpenStreetCam Sequence" related action buttons.
     *
     * @param isPrevious if true then the "Previous" button is enabled; if false then the button is disabled
     * @param isNext if true then the "Next" button is enabled; if false then the button is disabled
     */
    void enableSequenceActions(final boolean isPrevious, final boolean isNext) {
        btnPrevious.setEnabled(isPrevious);
        btnNext.setEnabled(isNext);
    }

    void enableClosestPhotoButton(final boolean enabled) {
        btnClosestPhoto.setEnabled(enabled);
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
    public void registerObserver(final SequenceObserver sequenceObserver) {
        this.sequenceObserver = sequenceObserver;
    }

    @Override
    public void notifyObserver(final int index) {
        this.sequenceObserver.selectSequencePhoto(index);
    }

    @Override
    public void registerObserver(final ClosestPhotoObserver closestOhotoObserver) {
        this.closestPhotoObserver = closestOhotoObserver;
    }

    @Override
    public void notifyClosestPhotoObserver() {
        closestPhotoObserver.selectClosestPhoto();
    }


    @Override
    public void registerObserver(final DataTypeChangeObserver dataUpdateObserver) {
        this.dataUpdateObserver = dataUpdateObserver;
    }


    @Override
    public void notifyDataUpdateObserver(final DataType dataType) {
        dataUpdateObserver.update(dataType);
    }

    private final class ManualDataSwitchAction extends AbstractAction {

        private static final long serialVersionUID = -6266140137863469921L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getActionCommand().equals(DataType.PHOTO.toString())) {
                // request segments
                notifyDataUpdateObserver(DataType.PHOTO);
                btnDataSwitch.setIcon(IconConfig.getInstance().getManualSwitchSegmentIcon());
                btnDataSwitch.setToolTipText(GuiConfig.getInstance().getBtnDataSwitchSegmentTlt());
                btnDataSwitch.setActionCommand(DataType.SEGMENT.toString());
            } else {
                // request images
                notifyDataUpdateObserver(DataType.SEGMENT);
                btnDataSwitch.setIcon(IconConfig.getInstance().getManualSwitchImageIcon());
                btnDataSwitch.setToolTipText(GuiConfig.getInstance().getBtnDataSwitchImageTlt());
                btnDataSwitch.setActionCommand(DataType.PHOTO.toString());
            }

        }
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
                enableSequenceActions(false, false);
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
                final StringBuilder link = new StringBuilder(Config.getInstance().getPhotoDetailsUrl());
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


    /* selects the closest photo of the selected photo */

    private final class ClosestPhotoAction extends AbstractAction {

        private static final long serialVersionUID = 191591505362305396L;


        @Override
        public void actionPerformed(final ActionEvent event) {
            if (photo != null) {
                notifyClosestPhotoObserver();
            }
        }
    }

}