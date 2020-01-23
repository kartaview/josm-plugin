/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.details.filter;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * Holds grid bag constraints for the {@code FilterPanel}.
 *
 * @author beataj
 * @version $Revision$
 */
final class Constraints {

    /* low zoom level UI constraints */
    private static final GridBagConstraints LOW_ZOOM_LBL_USER = new GridBagConstraints(0, 0, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 5), 0, 0);
    private static final GridBagConstraints LOW_ZOOM_CBB_USER = new GridBagConstraints(1, 0, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 10), 0, 0);
    private static final GridBagConstraints LOW_ZOOM_LBL_LOGIN_WARNING = new GridBagConstraints(2, 0, 1, 1, 1, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 10), 0, 0);
    private static final GridBagConstraints LOW_ZOOM_LBL_DATE = new GridBagConstraints(0, 1, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 5), 0, 0);
    private static final GridBagConstraints LOW_ZOOM_PICKER_DATE = new GridBagConstraints(1, 1, 2, 1, 1, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 10), 0, 0);

    /* high zoom level UI constraints */
    static final GridBagConstraints LBL_DATA_TYPE = new GridBagConstraints(0, 0, 1, 1, 1, 1,
            GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints CBB_PHOTOS = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 0), 0, 0);

    static final GridBagConstraints CBB_DETECTIONS = new GridBagConstraints(2, 0, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.NONE, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints CBB_AGGREGATED_DETECTIONS = new GridBagConstraints(3, 0, 3, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.NONE, new Insets(2, 0, 3, 10), 0, 0);

    private static final GridBagConstraints HIGH_ZOOM_LBL_USER = new GridBagConstraints(0, 1, 1, 1, 1, 1,
            GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    private static final GridBagConstraints HIGH_ZOOM_CBB_USER = new GridBagConstraints(1, 1, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 0), 0, 0);

    private static final GridBagConstraints HIGH_ZOOM_LBL_LOGIN_WARNING = new GridBagConstraints(2, 1, 3, 1, 1, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.NONE, new Insets(5, 3, 3, 0), 0, 0);

    private static final GridBagConstraints HIGH_ZOOM_LBL_DATE = new GridBagConstraints(0, 2, 1, 1, 1, 1,
            GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    private static final GridBagConstraints HIGH_ZOOM_PICKER_DATE = new GridBagConstraints(1, 2, 2, 1, 1, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 10), 0, 0);

    static final GridBagConstraints SEPARATOR = new GridBagConstraints(0, 3, 5, 1, 1, 1, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(5, 3, 3, 0), 0, 0);

    static final GridBagConstraints LBL_DETECTION = new GridBagConstraints(0, 4, 1, 1, 1, 1,
            GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 3, 5), 0, 0);

    static final GridBagConstraints LBL_MODE = new GridBagConstraints(0, 5, 1, 1, 1, 1,
            GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints CBB_AUTOMATIC_MODE = new GridBagConstraints(1, 5, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 0), 0, 0);

    static final GridBagConstraints CBB_MANUAL_MODE = new GridBagConstraints(2, 5, 3, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 10), 0, 0);

    static final GridBagConstraints LBL_EDIT_STATUS = new GridBagConstraints(0, 6, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints CBB_OPEN_EDIT_STATUS = new GridBagConstraints(1, 6, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 0), 0, 0);

    static final GridBagConstraints CBB_MAPPED_EDIT_STATUS = new GridBagConstraints(2, 6, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints CBB_BAD_SIGN_EDIT_STATUS = new GridBagConstraints(3, 6, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints CBB_OTHER_EDIT_STATUS = new GridBagConstraints(4, 6, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.NONE, new Insets(2, 0, 3, 10), 0, 0);

    static final GridBagConstraints LBL_OSM_COMPARISON = new GridBagConstraints(0, 7, 2, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints CBB_NEW_OSM_COMPARISON = new GridBagConstraints(1, 7, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 0), 0, 0);

    static final GridBagConstraints CBB_CHANGED_OSM_COMPARISON = new GridBagConstraints(2, 7, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints CBB_UNKNOWN_OSM_COMPARISON = new GridBagConstraints(3, 7, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints CBB_SAME_OSM_COMPARISON = new GridBagConstraints(4, 7, 1, 1, 0, 0,
            GridBagConstraints.PAGE_START, GridBagConstraints.NONE, new Insets(2, 0, 3, 10), 0, 0);

    static final GridBagConstraints CBB_IMPLIED_OSM_COMPARISON = new GridBagConstraints(1, 8, 2, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 10), 0, 0);

    static final GridBagConstraints LBL_CONFIDENCE_LEVEL = new GridBagConstraints(0, 9, 2, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 15), 0, 0);

    static final GridBagConstraints CONFIDENCE_LEVEL_PANEL = new GridBagConstraints(1, 9, 2, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 5), 0, 0);

    static final GridBagConstraints LBL_MIN_CONFIDENCE_LEVEL = new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints TXT_MIN_CONFIDENCE_LEVEL = new GridBagConstraints(1, 0, 1, 1, 1, 0,
            GridBagConstraints.LAST_LINE_END, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints LBL_MAX_CONFIDENCE_LEVEL = new GridBagConstraints(2, 0, 1, 1, 0, 0,
            GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints TXT_MAX_CONFIDENCE_LEVEL = new GridBagConstraints(3, 0, 1, 1, 1, 0,
            GridBagConstraints.LAST_LINE_END, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 0), 0, 0);

    static final GridBagConstraints LBL_SIGN_REGION = new GridBagConstraints(0, 10, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints CB_SIGN_REGION = new GridBagConstraints(1, 10, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints LBL_SEARCH_SIGN = new GridBagConstraints(0, 11, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints TXT_SEARCH_SIGN = new GridBagConstraints(1, 11, 2, 1, 0, 0,
            GridBagConstraints.LAST_LINE_END, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints LBL_SEARCH_SIGN_EXPLANATION = new GridBagConstraints(3, 11, 2, 1, 0, 0,
            GridBagConstraints.LAST_LINE_END, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints LBL_SIGN_TYPE = new GridBagConstraints(0, 12, 1, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints CBB_SIGN_TYPE = new GridBagConstraints(1, 12, 4, 1, 1, 1,
            GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 10), 0, 160);

    static final GridBagConstraints PNL_BTN = new GridBagConstraints(2, 13, 3, 1, 0, 0, GridBagConstraints.PAGE_START,
            GridBagConstraints.HORIZONTAL, new Insets(0, 3, 3, 0), 0, 0);

    private Constraints() {}


    static GridBagConstraints getLblUser(final boolean isHighZoomLevel) {
        return isHighZoomLevel ? HIGH_ZOOM_LBL_USER : LOW_ZOOM_LBL_USER;
    }

    static GridBagConstraints getCbbUser(final boolean isHighZoomLevel) {
        return isHighZoomLevel ? HIGH_ZOOM_CBB_USER : LOW_ZOOM_CBB_USER;
    }

    static GridBagConstraints geLblLoginWarning(final boolean isHighZoomLevel) {
        return isHighZoomLevel ? HIGH_ZOOM_LBL_LOGIN_WARNING : LOW_ZOOM_LBL_LOGIN_WARNING;
    }

    static GridBagConstraints getLblDate(final boolean isHighZoomLevel) {
        return isHighZoomLevel ? HIGH_ZOOM_LBL_DATE : LOW_ZOOM_LBL_DATE;
    }

    static GridBagConstraints getPickerDate(final boolean isHighZoomLevel) {
        return isHighZoomLevel ? HIGH_ZOOM_PICKER_DATE : LOW_ZOOM_PICKER_DATE;
    }
}