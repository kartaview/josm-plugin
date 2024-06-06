/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.imagery.filter;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * Holds grid bag constraints for the {@code FilterPanel}.
 *
 * @author beataj
 * @version $Revision$
 */
public final class Constraints {

    /* high zoom level UI constraints */
    public static final GridBagConstraints LBL_DATA_TYPE =
            new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 10, 3, 5), 0, 0);

    public static final GridBagConstraints DATA_TYPE_PANEL =
            new GridBagConstraints(1, 0, 4, 1, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(2, 3, 3, 0), 0, 0);

    public static final GridBagConstraints LBL_COMMON_FILTERS =
            new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 10, 3, 5), 0, 0);

    static final GridBagConstraints LBL_DATE =
            new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 30, 3, 10), 0, 0);

    static final GridBagConstraints PICKER_DATE =
            new GridBagConstraints(1, 2, 2, 1, 1, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 3, 10), 0, 0);

    static final GridBagConstraints SEPARATOR =
            new GridBagConstraints(0, 2, 5, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 3, 3, 0), 0, 0);

    static final GridBagConstraints LBL_CONFIDENCE_CATEGORY =
            new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 30, 3, 5), 0, 0);

    static final GridBagConstraints CONFIDENCE_CATEGORY_PANEL =
            new GridBagConstraints(1, 3, 2, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(2, 3, 3, 0), 0, 0);

    static final GridBagConstraints LBL_SIGN_FILTERS =
            new GridBagConstraints(0, 4, 2, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 30, 3, 5), 0, 0);

    static final GridBagConstraints LBL_SIGN_REGION =
            new GridBagConstraints(0, 5, 2, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 60, 3, 5), 0, 0);

    static final GridBagConstraints CB_SIGN_REGION =
            new GridBagConstraints(1, 5, 2, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints LBL_SEARCH_SIGN =
            new GridBagConstraints(0, 6, 2, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 60, 3, 5), 0, 0);

    static final GridBagConstraints TXT_SEARCH_SIGN =
            new GridBagConstraints(1, 6, 2, 1, 0, 0, GridBagConstraints.LAST_LINE_END, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints LBL_SEARCH_SIGN_EXPLANATION =
            new GridBagConstraints(3, 6, 2, 1, 0, 0, GridBagConstraints.LAST_LINE_END, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 60, 3, 5), 0, 0);

    static final GridBagConstraints LBL_SIGN_TYPE =
            new GridBagConstraints(0, 7, 2, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 60, 3, 5), 0, 0);

    static final GridBagConstraints CBB_SIGN_TYPE =
            new GridBagConstraints(1, 7, 4, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 10, 3, 10), 0, 160);

    static final GridBagConstraints PNL_BTN =
            new GridBagConstraints(2, 8, 3, 1, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 3, 3, 0), 0, 0);

    static final GridBagConstraints LBL_DETECTION =
            new GridBagConstraints(0, 9, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 10, 3, 5), 0, 0);

    static final GridBagConstraints LBL_MODE =
            new GridBagConstraints(0, 10, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 30, 3, 5), 0, 0);

    static final GridBagConstraints MODE_PANEL =
            new GridBagConstraints(1, 10, 4, 1, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(2, 3, 3, 0), 0, 0);

    static final GridBagConstraints LBL_EDIT_STATUS =
            new GridBagConstraints(0, 11, 1, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 30, 3, 5), 0, 0);

    static final GridBagConstraints EDIT_STATUS_PANEL =
            new GridBagConstraints(1, 11, 4, 1, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(2, 3, 3, 0), 0, 0);

    static final GridBagConstraints LBL_CLUSTER_FILTERS =
            new GridBagConstraints(0, 12, 2, 1, 1, 1, GridBagConstraints.FIRST_LINE_START,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 10, 3, 5), 0, 0);

    static final GridBagConstraints LBL_OSM_COMPARISON =
            new GridBagConstraints(0, 13, 1, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 30, 3, 5), 0, 0);

    static final GridBagConstraints OSM_COMPARISON_PANEL =
            new GridBagConstraints(1, 13, 4, 1, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(2, 3, 3, 0), 0, 0);

    private Constraints() {
    }
}