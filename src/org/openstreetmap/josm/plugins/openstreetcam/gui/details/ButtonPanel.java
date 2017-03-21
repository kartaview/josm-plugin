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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.argument.DataType;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.observer.*;
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
class ButtonPanel extends JPanel implements LocationObservable, SequenceObservable, ClosestPhotoObservable {

    private static final long serialVersionUID = -2909078640977666884L;

    private static final String NEXT_PHOTO = "next photo";
    private static final String PREVIOUS_PHOTO = "previous photo";

    private static final Dimension DIM = new Dimension(200, 24);
    private static final int ROWS = 1;
    private static final int COLS = 5;

    /* the panel's components */
    private final JButton btnManualSwitch;
    private final JButton btnPrevious;
    private final JButton btnNext;
    private final JButton btnLocation;
    private final JButton btnWebPage;
    private final JButton btnClosestPhoto;

    /* notifies the plugin main class */
    private LocationObserver locationObserver;
    private SequenceObserver sequenceObserver;
    private ClosestPhotoObserver closestPhotoObserver;


    /* the currently selected photo */
    private Photo photo;


    ButtonPanel() {
        super(new GridLayout(ROWS, COLS));

        final GuiConfig guiConfig = GuiConfig.getInstance();
        final IconConfig iconConfig = IconConfig.getInstance();

        final String tlt = PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()
                ? guiConfig.getBtnManualImageSwitchTlt() : guiConfig.getBtnManualSwitchTlt();
        btnManualSwitch =
                GuiBuilder.buildButton(new ManualDataSwitchAction(), iconConfig.getManualSwitchImageIcon(), tlt, false);
        btnManualSwitch.setActionCommand(DataType.PHOTO.toString());
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

        add(btnManualSwitch);
        add(btnPrevious);
        add(btnNext);
        add(btnLocation);
        add(btnWebPage);
        add(btnClosestPhoto);

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
    void enableManualSwitchButton(final boolean enabled) {
        btnManualSwitch.setEnabled(enabled);
    }

    void updateManualSwitchButton(final DataType dataType) {
        if (dataType.equals(DataType.PHOTO)) {
            btnManualSwitch.setIcon(IconConfig.getInstance().getManualSwitchSegmentIcon());
            final String tlt = PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()
                    ? GuiConfig.getInstance().getBtnManualSegmentSwitchTlt()
                    : GuiConfig.getInstance().getBtnManualSwitchTlt();
            btnManualSwitch.setToolTipText(tlt);
            btnManualSwitch.setActionCommand(DataType.SEGMENT.toString());
        } else {
            btnManualSwitch.setIcon(IconConfig.getInstance().getManualSwitchImageIcon());
            final String tlt = PreferenceManager.getInstance().loadMapViewSettings().isManualSwitchFlag()
                    ? GuiConfig.getInstance().getBtnManualImageSwitchTlt()
                    : GuiConfig.getInstance().getBtnManualSwitchTlt();
            btnManualSwitch.setToolTipText(tlt);
            btnManualSwitch.setActionCommand(DataType.PHOTO.toString());
        }
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


    private final class ManualDataSwitchAction extends AbstractAction {

        private static final long serialVersionUID = -6266140137863469921L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (event.getActionCommand().equals(DataType.PHOTO.toString())) {
                // request segments
                PreferenceManager.getInstance().saveManualSwitchDataType(DataType.PHOTO);
                btnManualSwitch.setIcon(IconConfig.getInstance().getManualSwitchSegmentIcon());
                btnManualSwitch.setToolTipText(GuiConfig.getInstance().getBtnManualSegmentSwitchTlt());
                btnManualSwitch.setActionCommand(DataType.SEGMENT.toString());
            } else {
                // request images
                PreferenceManager.getInstance().saveManualSwitchDataType(DataType.SEGMENT);
                btnManualSwitch.setIcon(IconConfig.getInstance().getManualSwitchImageIcon());
                btnManualSwitch.setToolTipText(GuiConfig.getInstance().getBtnManualImageSwitchTlt());
                btnManualSwitch.setActionCommand(DataType.PHOTO.toString());

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