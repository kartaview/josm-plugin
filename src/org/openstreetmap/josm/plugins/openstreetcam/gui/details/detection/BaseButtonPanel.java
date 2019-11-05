/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2018, Telenav, Inc. All Rights Reserved
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