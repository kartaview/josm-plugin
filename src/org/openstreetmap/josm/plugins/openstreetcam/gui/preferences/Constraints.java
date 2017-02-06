/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license. 
 *  https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved             
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.preferences;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * 
 * @author beataj
 * @version $Revision$
 */
final class Constraints {

    private Constraints() {}

    static final GridBagConstraints LBL_IMAGE = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints CB_HIGHG_QUALITY = new GridBagConstraints(0, 1, 1, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);

    static final GridBagConstraints CB_TRACK_LOADING = new GridBagConstraints(0, 2, 1, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);

}