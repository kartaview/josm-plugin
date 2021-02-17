/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.layer;

import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.KartaViewServiceConfig;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.kartaview.gui.ShortcutFactory;
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
            OpenBrowser.displayUrl(new URI(KartaViewServiceConfig.getInstance().getFeedbackUrl()));
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(MainApplication.getMainFrame(),
                    GuiConfig.getInstance().getErrorFeedbackPageText(), GuiConfig.getInstance().getErrorTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}