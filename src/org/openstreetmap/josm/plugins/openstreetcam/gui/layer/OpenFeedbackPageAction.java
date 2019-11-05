/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer;

import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.gui.ShortcutFactory;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.OpenStreetCamServiceConfig;
import org.openstreetmap.josm.tools.OpenBrowser;


/**
 * Open the feedback web page using the default browser.
 *
 * @author beataj
 * @version $Revision$
 */
class OpenFeedbackPageAction extends JosmAction {

    private static final long serialVersionUID = 4196639030623647016L;

    OpenFeedbackPageAction() {
        super(GuiConfig.getInstance().getLayerFeedbackMenuItemLbl(), IconConfig.getInstance().getFeedbackIconName(),
                GuiConfig.getInstance().getLayerFeedbackMenuItemLbl(),
                ShortcutFactory.getInstance().getShotrcut(GuiConfig.getInstance().getLayerFeedbackShortcutText()),
                true);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        try {
            OpenBrowser.displayUrl(new URI(OpenStreetCamServiceConfig.getInstance().getFeedbackUrl()));
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(MainApplication.getMainFrame(),
                    GuiConfig.getInstance().getErrorFeedbackPageText(), GuiConfig.getInstance().getErrorTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}