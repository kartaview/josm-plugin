/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.photo;

import java.awt.Color;
import java.awt.FlowLayout;
import java.net.URI;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.OpenStreetCamServiceConfig;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.grab.josm.common.gui.builder.TextComponentBuilder;


/**
 * Displays information related to the selected photo.
 *
 * @author beataj
 * @version $Revision$
 */
class DetailsPanel extends JPanel implements HyperlinkListener {

    private static final long serialVersionUID = -1875934866697053509L;

    private final JEditorPane txtDetails;
    private final JLabel lblWarning;
    private String username;


    DetailsPanel(final Color backgroundColor) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        txtDetails = TextComponentBuilder.buildEditorPane(null, this, false);
        txtDetails.setBackground(backgroundColor);
        lblWarning = new JLabel();
        lblWarning.setIcon(IconConfig.getInstance().getWarningIcon());
        lblWarning.setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder());
    }


    void updateUI(final Photo photo, final boolean isWarning) {
        if (photo != null) {
            this.username = photo.getUsername();
            txtDetails.setText(Formatter.formatPhotoDetails(photo));
            add(txtDetails);
            if (isWarning) {
                add(lblWarning);
            }
        } else {
            removeAll();
        }
        revalidate();
    }

    @Override
    public void hyperlinkUpdate(final HyperlinkEvent event) {
        if (txtDetails.getText() != null && event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            final StringBuilder link = new StringBuilder(OpenStreetCamServiceConfig.getInstance().getUserPageUrl());
            link.append(username);
            try {
                OpenBrowser.displayUrl(new URI(link.toString()));
            } catch (final Exception e) {
                JOptionPane.showMessageDialog(MainApplication.getMainFrame(),
                        GuiConfig.getInstance().getErrorUserPageText(), GuiConfig.getInstance().getErrorTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}