/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.data.osm.SimplePrimitiveId;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.plugins.openstreetcam.DownloadWayTask;
import org.openstreetmap.josm.plugins.openstreetcam.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DataTypeChangeObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.DataTypeChangeObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.LocationObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.NearbyPhotoObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.NearbyPhotoObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceAutoplayObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceAutoplayObserver;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObservable;
import org.openstreetmap.josm.plugins.openstreetcam.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.openstreetcam.util.Util;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.OpenStreetCamServiceConfig;
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
class ButtonPanel extends JPanel implements NearbyPhotoObservable, DataTypeChangeObservable, LocationObservable,
SequenceObservable, SequenceAutoplayObservable {

    private static final long serialVersionUID = -2909078640977666884L;

    private static final String SHORTCUT = "sc";
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
    private JButton btnMatchedWay;

    /* notifies the plugin main class */
    private NearbyPhotoObserver nearbyPhotoObserver;
    private DataTypeChangeObserver dataUpdateObserver;
    private LocationObserver locationObserver;
    private SequenceObserver sequenceObserver;
    private SequenceAutoplayObserver sequenceAutoplayObserver;

    /* the currently selected photo */
    private Photo photo;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));
        addDataSwitchButton();
        addPreviousButton();
        addNextButton();
        addAutoplayButton();
        addClosestPhotoButton();
        addMatchedWayButton();
        addLocationButton();
        addWebPageButton();
        setPreferredSize(DIM);
    }

    private void addDataSwitchButton() {
        final JosmAction action = new ManualDataSwitchAction();
        final String tooltip =
                GuiConfig.getInstance().getBtnDataSwitchImageTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnDataSwitch =
                ButtonBuilder.build(action, IconConfig.getInstance().getManualSwitchImageIcon(), tooltip, false);
        btnDataSwitch.setActionCommand(DataType.PHOTO.toString());
        if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
            add(btnDataSwitch);
        }
    }

    private void addPreviousButton() {
        final JosmAction action = new SelectPhotoAction(GuiConfig.getInstance().getBtnPreviousShortcutText(), false);
        final String tooltip =
                GuiConfig.getInstance().getBtnPreviousTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnPrevious = ButtonBuilder.build(action, IconConfig.getInstance().getPreviousIcon(), tooltip, false);
        add(btnPrevious);
    }

    private void addNextButton() {
        final JosmAction action = new SelectPhotoAction(GuiConfig.getInstance().getBtnNextShortcutText(), true);
        final String tooltip =
                GuiConfig.getInstance().getBtnNextTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnNext = ButtonBuilder.build(new SelectPhotoAction(GuiConfig.getInstance().getBtnNextShortcutText(), true),
                IconConfig.getInstance().getNextIcon(), tooltip, false);
        add(btnNext);
    }

    private void addClosestPhotoButton() {
        final JosmAction action = new ClosestPhotoAction();
        final String tooltip =
                GuiConfig.getInstance().getBtnClosestTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnClosestPhoto = ButtonBuilder.build(action, IconConfig.getInstance().getClosestImageIcon(), tooltip, false);
        add(btnClosestPhoto);
    }

    private void addMatchedWayButton() {
        final JosmAction action = new DownloadMatchedWayId();
        final String tooltip =
                GuiConfig.getInstance().getBtnMatchedWayTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnMatchedWay = ButtonBuilder.build(new DownloadMatchedWayId(), IconConfig.getInstance().getMatchedWayIcon(),
                tooltip, false);
        add(btnMatchedWay);
    }

    private void addAutoplayButton() {
        final JosmAction action = new TrackAutoplayAction();
        final String tooltip =
                GuiConfig.getInstance().getBtnPlayTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnAutoplay = ButtonBuilder.build(action, IconConfig.getInstance().getPlayIcon(), tooltip, false);
        btnAutoplay.setActionCommand(AutoplayAction.START.name());
        add(btnAutoplay);
    }

    private void addLocationButton() {
        final JosmAction action = new JumpToLocationAction();
        final String tooltip =
                GuiConfig.getInstance().getBtnLocationTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnLocation = ButtonBuilder.build(action, IconConfig.getInstance().getLocationIcon(), tooltip, false);
        add(btnLocation);
    }

    private void addWebPageButton() {
        final JosmAction action = new OpenWebPageAction();
        final String tooltip =
                GuiConfig.getInstance().getBtnWebPageTlt().replace(SHORTCUT, action.getShortcut().getKeyText());
        btnWebPage = ButtonBuilder.build(action, IconConfig.getInstance().getWebPageIcon(), tooltip, false);
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
            btnWebPage.setEnabled(true);
            final boolean autoplayStartedFlag = PreferenceManager.getInstance().loadAutoplayStartedFlag();
            btnLocation.setEnabled(!autoplayStartedFlag);
            if (photo.getWayId() != null) {
                btnMatchedWay.setEnabled(!autoplayStartedFlag);
            }
            if (!autoplayStartedFlag) {
                updateAutoplayButton(AutoplayAction.START);
            }
        } else {
            enableSequenceActions(false, false);
            btnWebPage.setEnabled(false);
            btnClosestPhoto.setEnabled(false);
            btnLocation.setEnabled(false);
            btnMatchedWay.setEnabled(false);
            if (PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()) {
                enableDataSwitchButton(true);
            }
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
            final String tooltip = GuiConfig.getInstance().getBtnDataSwitchSegmentTlt().replaceAll(SHORTCUT,
                    ((JosmAction) btnDataSwitch.getAction()).getShortcut().getKeyText());
            btnDataSwitch.setToolTipText(tooltip);
            btnDataSwitch.setActionCommand(DataType.SEGMENT.toString());
        } else {
            btnDataSwitch.setIcon(IconConfig.getInstance().getManualSwitchImageIcon());
            final String tooltip = GuiConfig.getInstance().getBtnDataSwitchImageTlt().replaceAll(SHORTCUT,
                    ((JosmAction) btnDataSwitch.getAction()).getShortcut().getKeyText());
            btnDataSwitch.setToolTipText(tooltip);
            btnDataSwitch.setActionCommand(DataType.PHOTO.toString());
        }
        revalidate();
        repaint();
    }

    /**
     * Enables or disables the "OpenStreetCam Sequence" related action buttons based on the given arguments. The play
     * button is disabled if the last photo of the track was selected (isNext is false).
     *
     * @param isPrevious if true then the "Previous" button is enabled; if false then the button is disabled
     * @param isNext if true then the "Next" button is enabled; if false then the button is disabled
     */
    void enableSequenceActions(final boolean isPrevious, final boolean isNext) {
        btnPrevious.setEnabled(isPrevious);
        btnNext.setEnabled(isNext);
        btnAutoplay.setEnabled(isNext);
        if (!isNext) {
            updateAutoplayButton(AutoplayAction.START);
        }
    }

    private void updateAutoplayButton(final AutoplayAction action) {
        if (action.equals(AutoplayAction.START)) {
            btnAutoplay.setIcon(IconConfig.getInstance().getPlayIcon());
            final String tooltip = GuiConfig.getInstance().getBtnPlayTlt().replaceAll(SHORTCUT,
                    ((JosmAction) btnAutoplay.getAction()).getShortcut().getKeyText());
            btnAutoplay.setToolTipText(tooltip);
            btnAutoplay.setActionCommand(AutoplayAction.START.name());
            btnLocation.setEnabled(true);
            btnMatchedWay.setEnabled(true);
        } else {
            btnAutoplay.setIcon(IconConfig.getInstance().getStopIcon());
            final String tooltip = GuiConfig.getInstance().getBtnStopTlt().replaceAll(SHORTCUT,
                    ((JosmAction) btnAutoplay.getAction()).getShortcut().getKeyText());
            btnAutoplay.setToolTipText(tooltip);
            btnAutoplay.setActionCommand(AutoplayAction.STOP.name());
            btnLocation.setEnabled(false);
            btnMatchedWay.setEnabled(false);
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
    public void registerObserver(final NearbyPhotoObserver nearbyPhotoObserver) {
        this.nearbyPhotoObserver = nearbyPhotoObserver;
    }

    @Override
    public void notifyNearbyPhotoObserver() {
        nearbyPhotoObserver.selectNearbyPhoto();
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
    public void registerObserver(final SequenceAutoplayObserver sequenceAutoplayObserver) {
        this.sequenceAutoplayObserver = sequenceAutoplayObserver;
    }

    @Override
    public void notifyObserver(final AutoplayAction action) {
        sequenceAutoplayObserver.play(action);
    }

    boolean isPhotoSelected() {
        return photo != null;
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
            DataType dataType = DataType.getDataType(event.getActionCommand());
            dataType = dataType == null ? DataType.getDataType(btnDataSwitch.getActionCommand()) : dataType;
            notifyDataUpdateObserver(dataType);
            updateDataSwitchButton(dataType);
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
                notifyNearbyPhotoObserver();
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
                final AutoplayAction eventAction = AutoplayAction.getAutoplayAction(event.getActionCommand());
                final AutoplayAction autoplayAction = eventAction != null ? eventAction
                        : AutoplayAction.getAutoplayAction(btnAutoplay.getActionCommand());
                if (autoplayAction != null && autoplayAction.equals(AutoplayAction.START)) {
                    updateAutoplayButton(AutoplayAction.STOP);
                    btnClosestPhoto.setEnabled(false);
                    btnPrevious.setEnabled(false);
                    btnNext.setEnabled(false);
                } else {
                    updateAutoplayButton(AutoplayAction.START);
                    btnPrevious.setEnabled(true);
                    btnNext.setEnabled(true);
                }
                ThreadPool.getInstance().execute(() -> notifyObserver(autoplayAction));
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
                final StringBuilder link =
                        new StringBuilder(OpenStreetCamServiceConfig.getInstance().getPhotoDetailsUrl());
                link.append(photo.getSequenceId()).append("/").append(photo.getSequenceIndex());
                try {
                    OpenBrowser.displayUrl(new URI(link.toString()));
                } catch (final Exception e) {
                    JOptionPane.showMessageDialog(Main.parent, GuiConfig.getInstance().getErrorPhotoPageText(),
                            GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    private final class DownloadMatchedWayId extends JosmAction {

        private static final long serialVersionUID = 7626759776502632881L;

        private DownloadMatchedWayId() {
            super(GuiConfig.getInstance().getBtnMatchedWayTlt(), null, GuiConfig.getInstance().getBtnMatchedWayTlt(),
                    ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getBtnMatchedWayShortcutTlt()),
                    true);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (photo != null && photo.getWayId() != null) {
                final PrimitiveId wayId = new SimplePrimitiveId(photo.getWayId(), OsmPrimitiveType.WAY);
                final boolean downloaded = Util.editLayerContainsWay(wayId);
                if (downloaded) {
                    GuiHelper.runInEDT(() -> MainApplication.getLayerManager().getEditDataSet().setSelected(wayId));
                } else {
                    final DownloadWayTask task = new DownloadWayTask(wayId);
                    MainApplication.worker.submit(task);
                }
            }
        }
    }
}