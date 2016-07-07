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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.openstreetview.util.cnf.ServiceConfig;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Displays the selected image.
 *
 * @author Beata
 * @version $Revision$
 */
class PhotoPanel extends JPanel {

    private static final long serialVersionUID = -1550900781158007580L;
    private ImageIcon image;

    PhotoPanel() {
        super(new BorderLayout());
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.gray));
    }

    void updateUI(final String photoName) {
        if (photoName != null) {
            removeAll();
            final StringBuilder link = new StringBuilder(ServiceConfig.getInstance().getBaseUrl());
            link.append(photoName);
            try {
                image = new ImageIcon(ImageIO.read(new URL(link.toString())));
                final JLabel lbl = new JLabel(new ImageIcon(
                        image.getImage().getScaledInstance(getWidth(), getHeight(), java.awt.Image.SCALE_SMOOTH)));
                add(lbl, BorderLayout.CENTER);
            } catch (final IOException e) {
                add(GuiBuilder.buildLabel(GuiConfig.getInstance().getPhotoErrorTxt(),
                        getFont().deriveFont(Font.BOLD, 12), Color.white), BorderLayout.CENTER);
            }
        }
    }
}