/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.photo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.kartaview.gui.details.common.DownloadMatchedOsmElement;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.KartaViewServiceConfig;
import org.openstreetmap.josm.plugins.kartaview.util.pref.PreferenceManager;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.data.osm.SimplePrimitiveId;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.kartaview.DataSet;
import org.openstreetmap.josm.plugins.kartaview.argument.AutoplayAction;
import org.openstreetmap.josm.plugins.kartaview.argument.Projection;
import org.openstreetmap.josm.plugins.kartaview.entity.Photo;
import org.openstreetmap.josm.plugins.kartaview.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.kartaview.observer.LocationObservable;
import org.openstreetmap.josm.plugins.kartaview.observer.LocationObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.NearbyPhotoObservable;
import org.openstreetmap.josm.plugins.kartaview.observer.NearbyPhotoObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.SequenceAutoplayObservable;
import org.openstreetmap.josm.plugins.kartaview.observer.SequenceAutoplayObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.SequenceObservable;
import org.openstreetmap.josm.plugins.kartaview.observer.SequenceObserver;
import org.openstreetmap.josm.plugins.kartaview.observer.SwitchPhotoFormatObservable;
import org.openstreetmap.josm.plugins.kartaview.observer.SwitchPhotoFormatObserver;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.grab.josm.common.gui.builder.ButtonBuilder;
import com.grab.josm.common.thread.ThreadPool;


/**
 * Defines the {@code KartaViewDetailsDialog} action panel. The user can perform the following actions: filter, jump
 * to photo location and open photo web page.
 *
 * @author Beata
 * @version $Revision$
 */
class ButtonPanel extends JPanel
        implements NearbyPhotoObservable, LocationObservable, SequenceObservable, SequenceAutoplayObservable,
        SwitchPhotoFormatObservable {

    private static final long serialVersionUID = -2909078640977666884L;

    private static final String SHORTCUT = "sc";
    private static final Dimension DIM = new Dimension(200, 24);
    private static final int ROWS = 1;
    private static final int COLS = 5;

    /* the panel's components */
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnAutoplay;
    private JButton btnLocation;
    private JButton btnSwitchImageFormat;
    private JButton btnWebPage;
    private JButton btnClosestPhoto;
    private JButton btnMatchedWay;

    /* notifies the plugin main class */
    private transient NearbyPhotoObserver nearbyPhotoObserver;
    private transient LocationObserver locationObserver;
    private transient SequenceObserver sequenceObserver;
    private transient SequenceAutoplayObserver sequenceAutoplayObserver;
    private transient SwitchPhotoFormatObserver imageFormatObserver;

    /* the currently selected photo */
    private transient Photo photo;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));
        addPreviousButton();
        addNextButton();
        addAutoplayButton();
        addClosestPhotoButton();
        addSwitchPhotoFormatButton();
        addMatchedWayButton();
        addLocationButton();
        addWebPageButton();
        setPreferredSize(DIM);
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

    private void addSwitchPhotoFormatButton() {
        final JosmAction action = new SwitchImageFormat();
        if (PreferenceManager.getInstance().loadPhotoSettings().isDisplayFrontFacingFlag()) {
            final String tooltip = GuiConfig.getInstance().getBtnSwitchPhotoFormatToWrappedTlt();
            btnSwitchImageFormat =
                    ButtonBuilder.build(action, IconConfig.getInstance().getWrappedImageFormatIcon(), tooltip, false);
        } else {
            final String tooltip = GuiConfig.getInstance().getBtnSwitchPhotoFormatToFrontFacingTlt();
            btnSwitchImageFormat = ButtonBuilder
                    .build(action, IconConfig.getInstance().getFrontFacingImageFormatIcon(), tooltip, false);
        }
        add(btnSwitchImageFormat);
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
            if (!autoplayStartedFlag) {
                updateAutoplayButton(AutoplayAction.START);
            }
            final boolean matchedWayEnabled = photo.getWayId() != null;
            btnMatchedWay.setEnabled(matchedWayEnabled);
            if (photo.getProjectionType().equals(Projection.SPHERE)) {
                enableSwitchImageFormatButton(true, DataSet.getInstance().isFrontFacingDisplayed());
            } else {
                enableSwitchImageFormatButton(false,
                        PreferenceManager.getInstance().loadPhotoSettings().isDisplayFrontFacingFlag());
            }
        } else {
            enableSequenceActions(false, false, null);
            btnWebPage.setEnabled(false);
            btnClosestPhoto.setEnabled(false);
            btnLocation.setEnabled(false);
            btnMatchedWay.setEnabled(false);
            enableSwitchImageFormatButton(false,
                    PreferenceManager.getInstance().loadPhotoSettings().isDisplayFrontFacingFlag());
        }
    }

    /**
     * Enables or disables the "KartaView Sequence" related action buttons based on the given arguments. The play
     * button is disabled if the last photo of the track was selected (isNext is false).
     *
     * @param isPrevious if true then the "Previous" button is enabled; if false then the button is disabled
     * @param isNext if true then the "Next" button is enabled; if false then the button is disabled
     */
    void enableSequenceActions(final boolean isPrevious, final boolean isNext, final AutoplayAction action) {
        btnPrevious.setEnabled(isPrevious);
        btnNext.setEnabled(isNext);
        btnAutoplay.setEnabled(isNext);
        if (action != null) {
            updateAutoplayButton(action);
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
     * Updates the switch image format button icon, tool-tip and action command.
     *
     * @param isFrontFacingDisplayed  true if in the photo panel is cropped format; false otherwise
     */
    void updateSwitchImageFormatButton(final boolean isFrontFacingDisplayed) {
        if (isFrontFacingDisplayed) {
            btnSwitchImageFormat.setIcon(IconConfig.getInstance().getWrappedImageFormatIcon());
            final String tooltip = GuiConfig.getInstance().getBtnSwitchPhotoFormatToWrappedTlt();
            btnSwitchImageFormat.setToolTipText(tooltip);
        } else {
            btnSwitchImageFormat.setIcon(IconConfig.getInstance().getFrontFacingImageFormatIcon());
            final String tooltip = GuiConfig.getInstance().getBtnSwitchPhotoFormatToFrontFacingTlt();
            btnSwitchImageFormat.setToolTipText(tooltip);
        }
        revalidate();
        repaint();
    }

    /**
     * Enables or disables the switch format image photo button.
     *
     * @param enabled if true then the button is enabled; if false then the button is disabled
     */
    void enableSwitchImageFormatButton(final boolean enabled, final boolean isCroppedInPanel) {
        btnSwitchImageFormat.setEnabled(enabled);
        updateSwitchImageFormatButton(isCroppedInPanel);
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
    public void registerObserver(final SequenceAutoplayObserver sequenceAutoplayObserver) {
        this.sequenceAutoplayObserver = sequenceAutoplayObserver;
    }

    @Override
    public void notifyObserver(final AutoplayAction action) {
        sequenceAutoplayObserver.play(action);
    }

    @Override
    public void notifySwitchPhotoFormatObserver() {
        imageFormatObserver.switchPhotoFormat();
    }

    @Override
    public void registerObserver(final SwitchPhotoFormatObserver switchPhotoFormatObserver) {
        this.imageFormatObserver = switchPhotoFormatObserver;
    }

    boolean isPhotoSelected() {
        return photo != null;
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
                enableSequenceActions(false, false, null);
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
     *  Changes the image load in the panel according to the content of the panel.
     */
    private final class SwitchImageFormat extends JosmAction {

        private static final long serialVersionUID = -2907119623783298953L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final boolean frontFacingIsDisplayed = DataSet.getInstance().isFrontFacingDisplayed();
            updateSwitchImageFormatButton(frontFacingIsDisplayed);
            DataSet.getInstance().setFrontFacingDisplayed(!frontFacingIsDisplayed);
            notifySwitchPhotoFormatObserver();
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
                        new StringBuilder(KartaViewServiceConfig.getInstance().getPhotoDetailsUrl());
                link.append(photo.getSequenceId()).append("/").append(photo.getSequenceIndex());
                try {
                    OpenBrowser.displayUrl(new URI(link.toString()));
                } catch (final Exception e) {
                    JOptionPane.showMessageDialog(MainApplication.getMainFrame(),
                            GuiConfig.getInstance().getErrorPhotoPageText(), GuiConfig.getInstance().getErrorTitle(),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    private final class DownloadMatchedWayId extends DownloadMatchedOsmElement {

        private static final long serialVersionUID = 7626759776502632881L;

        private DownloadMatchedWayId() {
            super(GuiConfig.getInstance().getBtnMatchedWayTlt(), GuiConfig.getInstance().getBtnMatchedWayTlt());
        }

        @Override
        protected PrimitiveId getPrimitiveId() {
            final Long wayId = photo != null ? photo.getWayId() : null;
            return wayId != null ? new SimplePrimitiveId(wayId, OsmPrimitiveType.WAY) : null;
        }
    }
}