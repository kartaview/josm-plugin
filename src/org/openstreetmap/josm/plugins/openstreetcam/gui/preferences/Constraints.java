/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 *  https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.preferences;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * Holds {@code GridBagConstraints} for the preference panel.
 *
 * @author beataj
 * @version $Revision$
 */
final class Constraints {

    static final GridBagConstraints LBL_MAP_VIEW = new GridBagConstraints(0, 0, 4, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);
    static final GridBagConstraints LBL_PHOTO_ZOOM = new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 9, 3, 0), 0, 0);
    static final GridBagConstraints SP_PHOTO_ZOOM = new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints CB_MANUAL_SWITCH = new GridBagConstraints(0, 2, 4, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints CB_DATA_LOAD = new GridBagConstraints(0, 3, 4, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints LBL_IMAGE = new GridBagConstraints(0, 4, 4, 1, 1, 1, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(10, 0, 3, 0), 0, 0);
    static final GridBagConstraints CB_HIGHG_QUALITY = new GridBagConstraints(0, 5, 4, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints CB_MOUSE_HOVER = new GridBagConstraints(0, 7, 4, 1, 1, 1, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints LBL_MOUSE_HOVER_DELAY = new GridBagConstraints(0, 8, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 9, 3, 0), 0, 0);
    static final GridBagConstraints SP_MOUSE_HOVER_DELAY = new GridBagConstraints(1, 8, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints LBL_AGGREGATED = new GridBagConstraints(0, 9, 4, 1, 1, 1, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(10, 0, 3, 0), 0, 0);
    static final GridBagConstraints CB_DISPLAY_IMAGE = new GridBagConstraints(0, 10, 4, 1, 1, 1, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints CB_DISPLAY_DETECTION = new GridBagConstraints(0, 12, 4, 1, 1, 1, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints LBL_TRACK = new GridBagConstraints(0, 13, 4, 1, 1, 1, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(10, 0, 3, 0), 0, 0);
    static final GridBagConstraints CB_TRACK_LOADING = new GridBagConstraints(0, 15, 4, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints LBL_AUTOPLAY = new GridBagConstraints(0, 16, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 9, 3, 0), 0, 0);
    static final GridBagConstraints LBL_AUTOPLAY_LENGTH = new GridBagConstraints(0, 17, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 15, 3, 0), 0, 0);
    static final GridBagConstraints TXT_AUTOPLAY_LENGTH = new GridBagConstraints(1, 17, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);
    static final GridBagConstraints LBL_AUTOPLAY_DELAY = new GridBagConstraints(0, 18, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 15, 3, 0), 0, 0);
    static final GridBagConstraints SP_AUTOPLAY_DELAY = new GridBagConstraints(1, 18, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);
    static final GridBagConstraints LBL_CACHE = new GridBagConstraints(0, 19, 4, 1, 1, 1, GridBagConstraints.LINE_START,
            GridBagConstraints.HORIZONTAL, new Insets(10, 0, 3, 0), 0, 0);
    static final GridBagConstraints LBL_MEMORY_COUNT = new GridBagConstraints(0, 20, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 9, 3, 0), 0, 0);
    static final GridBagConstraints SP_MEMORY_COUNT = new GridBagConstraints(1, 20, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints LBL_DISK_COUNT = new GridBagConstraints(0, 21, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 9, 3, 0), 0, 0);
    static final GridBagConstraints SP_DISK_COUNT = new GridBagConstraints(1, 21, 1, 1, 0, 0, GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints LBL_PREV_NEXT_COUNT = new GridBagConstraints(0, 22, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 9, 3, 0), 0, 0);
    static final GridBagConstraints SP_PREV_NEXT_COUNT = new GridBagConstraints(1, 22, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
    static final GridBagConstraints LBL_NEARBY_COUNT = new GridBagConstraints(0, 23, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(2, 9, 3, 0), 0, 0);
    static final GridBagConstraints SP_NEARBY_COUNT = new GridBagConstraints(1, 23, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);


    private Constraints() {}
}