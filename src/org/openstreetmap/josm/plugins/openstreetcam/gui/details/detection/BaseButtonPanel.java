/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.detection;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;


/**
 * Defines common functionality used by the detection and cluster button panels.
 *
 * @author beataj
 * @version $Revision$
 */
abstract class BaseButtonPanel extends JPanel {

    private static final long serialVersionUID = -331431553297529288L;

    protected static final String SHORTCUT = "sc";
    private static final int ROWS = 1;
    private static final Dimension DIM = new Dimension(200, 24);

    BaseButtonPanel(final int cols) {
        super(new GridLayout(ROWS, cols));
        createComponents();
        setPreferredSize(DIM);
    }

    abstract void createComponents();
}