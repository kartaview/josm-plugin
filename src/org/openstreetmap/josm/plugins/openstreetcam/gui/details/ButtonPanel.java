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
import java.net.URI;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClosestPhotoObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.ClosestPhotoObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DataTypeChangeObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DataTypeChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.TrackAutoplayObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.TrackAutoplayObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.telenav.josm.common.gui.builder.ButtonBuilder;
import com.telenav.josm.common.thread.ThreadPool;


/**
 * Defines the {@code OpenStreetCamDetailsDialog} action panel. The user can perform the following actions: filter, jump
 * to photo location and open photo web page.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel implements ClosestPhotoObservable, DataTypeChangeObservable, LocationObservable,
        SequenceObservable, TrackAutoplayObservable {

    private static final long serialVersionUID = -2909078640977666884L;

    private static final Dimension DIM = new Dimension(200, 24);
    private static final int ROWS = 1;
    private static final int COLS = 5;

    /* the panel's components */
    private JButton btnDataSwitch;
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnAutoplay;
    private JButton btnLocation;
    private JButton btnWebPage;
    private JButton btnClosestPhoto;

    /* notifies the plugin main class */
    private ClosestPhotoObserver closestPhotoObserver;
    private DataTypeChangeObserver dataUpdateObserver;
    private LocationObserver locationObserver;
    private SequenceObserver sequenceObserver;
    private TrackAutoplayObserver trackAutoplayObserver;


    /* the currently selected photo */
    private Photo photo;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));
        createComponents();
        setPreferredSize(DIM);
    }

    private void createComponents() {
        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();
        btnPrevious = ButtonBuilder.build(new SelectPhotoAction(guiConfig.getBtnPreviousShortcutText(), false),
                iconConfig.getPreviousIcon(), guiConfig.getBtnPreviousTlt(), false);
        btnNext = ButtonBuilder.build(new SelectPhotoAction(guiConfig.getBtnNextShortcutText(), true),
                iconConfig.getNextIcon(), guiConfig.getBtnNextTlt(), false);
        btnClosestPhoto = ButtonBuilder.build(new ClosestPhotoAction(), iconConfig.getClosestImageIcon(),
                guiConfig.getBtnClosestTlt(), false);

        btnAutoplay = ButtonBuilder.build(new TrackAutoplayAction(), iconConfig.getPlayIcon(),
                guiConfig.getBtnPlayTlt(), false);
        btnAutoplay.setActionCommand(AutoplayAction.START.name());
        btnLocation = ButtonBuilder.build(new JumpToLocationAction(), iconConfig.getLocationIcon(),
                guiConfig.getBtnLocationTlt(), false);
        btnWebPage = ButtonBuilder.build(new OpenWebPageAction(), iconConfig.getWebPageIcon(),
                guiConfig.getBtnWebPageTlt(), false);

        btnDataSwitch = ButtonBuilder.build(new ManualDataSwitchAction(), iconConfig.getManualSwitchImageIcon(),
                guiConfig.getBtnDataSwitchImageTlt(), false);
        btnDataSwitch.setActionCommand(DataType.PHOTO.toString());

        if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
            // add manual switch button
            add(btnDataSwitch);
        }

        add(btnPrevious);
        add(btnNext);
        add(btnAutoplay);
        add(btnClosestPhoto);
        add(btnLocation);
        add(btnWebPage);
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
            btnClosestPhoto.setEnabled(false);
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

    /**
     * Sets the data switch button visibility.
     *
     * @param isVisible if true/false the button is added to the button panel/removed from the button panel
     */
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
            btnDataSwitch = ButtonBuilder.build(new ManualDataSwitchAction(), icon, tlt, enabled);
            btnDataSwitch.setActionCommand(DataType.PHOTO.toString());
            add(btnDataSwitch, 0);

        } else {
            remove(btnDataSwitch);
        }
    }

    /**
     * Updates the data switch button icon, tool-tip and action command.
     *
     * @param dataType a {@code DataType} specifies the currently displayed data type
     */
    void updateDataSwitchButton(final DataType dataType) {
        if (dataType.equals(DataType.PHOTO)) {
            btnDataSwitch.setIcon(IconConfig.getInstance().getManualSwitchSegmentIcon());
            btnDataSwitch.setToolTipText(GuiConfig.getInstance().getBtnDataSwitchSegmentTlt());
            btnDataSwitch.setActionCommand(DataType.SEGMENT.toString());
        } else {
            btnDataSwitch.setIcon(IconConfig.getInstance().getManualSwitchImageIcon());
            btnDataSwitch.setToolTipText(GuiConfig.getInstance().getBtnDataSwitchImageTlt());
            btnDataSwitch.setActionCommand(DataType.PHOTO.toString());
        }
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

        // autoplay should be enabled if there are next photos
        btnAutoplay.setEnabled(isNext);
        if (!isNext && !isPrevious) {
            // reset initial state
            btnAutoplay.setIcon(IconConfig.getInstance().getPlayIcon());
            btnAutoplay.setToolTipText(GuiConfig.getInstance().getBtnPlayTlt());
            btnAutoplay.setActionCommand(AutoplayAction.START.name());
        }
    }

    /**
     * Enables or disables the closest photo button.
     *
     * @param enabled if true then the button is enabled; if false then the button is disabled
     */
    void enableClosestPhotoButton(final boolean enabled) {
        btnClosestPhoto.setEnabled(enabled);
    }

    void updateAutoplayButton(final AutoplayAction action) {
        if (action.equals(AutoplayAction.START)) {
            btnAutoplay.setIcon(IconConfig.getInstance().getPlayIcon());
            btnAutoplay.setToolTipText(GuiConfig.getInstance().getBtnPlayTlt());
            btnAutoplay.setActionCommand(AutoplayAction.STOP.name());
        } else {
            btnAutoplay.setIcon(IconConfig.getInstance().getStopIcon());
            btnAutoplay.setToolTipText(GuiConfig.getInstance().getBtnStopTlt());
            btnAutoplay.setActionCommand(AutoplayAction.START.name());
        }
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

    @Override
    public void registerObserver(final TrackAutoplayObserver trackAutoplayObserver) {
        this.trackAutoplayObserver = trackAutoplayObserver;

    }

    @Override
    public void notifyObserver(final AutoplayAction action) {
        trackAutoplayObserver.play(action);
    }

    /**
     * Defines the functionality of the manual data switch button.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class ManualDataSwitchAction extends JosmAction {

        private static final long serialVersionUID = -6266140137863469921L;


        private ManualDataSwitchAction() {
            super(GuiConfig.getInstance().getBtnDataSwitchShortcutTlt(), null,
                    GuiConfig.getInstance().getBtnDataSwitchShortcutTlt(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnDataSwitchShortcutTlt()),
                    true);
        }

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


    /**
     * Selects the previous/next photo from the displayed sequence
     *
     * @author beataj
     * @version $Revision$
     */
    private final class SelectPhotoAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;

        private final boolean isNext;

        private SelectPhotoAction(final String shortcutText, final boolean isNext) {
            super(shortcutText, null, shortcutText, ShortcutFactory.getInstance().getShotrcut(shortcutText), true);
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


    /**
     * Selects the closest photo of the selected photo.
     *
     * @author ioanao
     * @version $Revision$
     */
    private final class ClosestPhotoAction extends JosmAction {

        private static final long serialVersionUID = 191591505362305396L;


        private ClosestPhotoAction() {
            super(GuiConfig.getInstance().getBtnClosestShortcutText(), null,
                    GuiConfig.getInstance().getBtnClosestShortcutText(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnClosestShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (photo != null) {
                notifyClosestPhotoObserver();
            }
        }
    }

    /**
     * Starts/stops to auto-play the currently displayed track.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class TrackAutoplayAction extends JosmAction {

        private static final long serialVersionUID = -2733397455276087753L;


        private TrackAutoplayAction() {
            super(GuiConfig.getInstance().getBtnPlayShortcutText(), null,
                    GuiConfig.getInstance().getBtnPlayShortcutText(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnPlayShortcutText()), true);
        }


        @Override
        public void actionPerformed(final ActionEvent event) {
            if (photo != null) {
                final AutoplayAction action = AutoplayAction.valueOf(event.getActionCommand());
                updateAutoplayButton(AutoplayAction.valueOf(event.getActionCommand()));
                if (action.equals(AutoplayAction.START)) {
                    btnClosestPhoto.setEnabled(false);
                    btnPrevious.setEnabled(false);
                    btnNext.setEnabled(false);
                } else {
                    btnPrevious.setEnabled(true);
                    btnNext.setEnabled(true);
                }
                ThreadPool.getInstance().execute(() -> notifyObserver(action));

            }
        }
    }

    /**
     * Centers the map to the selected photo's location.
     *
     * @author beataj
     * @version $Revision$
     */
    private final class JumpToLocationAction extends JosmAction {

        private static final long serialVersionUID = 6824741346944799071L;

        private JumpToLocationAction() {
            super(GuiConfig.getInstance().getBtnLocationShortcutText(), null,
                    GuiConfig.getInstance().getBtnLocationShortcutText(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnLocationShortcutText()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (photo != null) {
                notifyObserver();
            }
        }
    }

    /**
     * Opens the selected photo's web page
     *
     * @author beataj
     * @version $Revision$
     */
    private final class OpenWebPageAction extends JosmAction {

        private static final long serialVersionUID = -1443190917019829709L;

        private OpenWebPageAction() {
            super(GuiConfig.getInstance().getBtnWebPageShortcutTlt(), null,
                    GuiConfig.getInstance().getBtnWebPageShortcutTlt(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnWebPageShortcutTlt()),
                    true);
        }

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
}