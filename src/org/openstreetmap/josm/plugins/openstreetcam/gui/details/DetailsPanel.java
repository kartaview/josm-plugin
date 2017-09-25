/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details;

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
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.openstreetcam.entity.Photo;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.Config;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.telenav.josm.common.gui.builder.TextComponentBuilder;


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
            final StringBuilder link = new StringBuilder(Config.getInstance().getUserPageUrl());
            link.append(username);
            try {
                OpenBrowser.displayUrl(new URI(link.toString()));
            } catch (final Exception e) {
                JOptionPane.showMessageDialog(Main.parent, GuiConfig.getInstance().getErrorUserPageText(),
                        GuiConfig.getInstance().getErrorTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}