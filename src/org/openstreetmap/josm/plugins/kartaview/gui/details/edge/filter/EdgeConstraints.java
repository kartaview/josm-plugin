/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.gui.details.edge.filter;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * Holds grid bag constraints for the {@code EdgeFilterPanel}.
 *
 * @author adina.misaras
 */
class EdgeConstraints {

    static final GridBagConstraints LBL_EDGE_SIGN_FILTERS =
            new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 30, 3, 5), 0, 0);

    static final GridBagConstraints LBL_EDGE_SIGN_REGION =
            new GridBagConstraints(0, 3, 2, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 60, 3, 5), 0, 0);

    static final GridBagConstraints CB_EDGE_SIGN_REGION =
            new GridBagConstraints(1, 3, 2, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints LBL_EDGE_SEARCH_SIGN =
            new GridBagConstraints(0, 4, 2, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 60, 3, 5), 0, 0);

    static final GridBagConstraints TXT_EDGE_SEARCH_SIGN =
            new GridBagConstraints(1, 4, 2, 1, 0, 0, GridBagConstraints.LAST_LINE_END, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 3, 5), 0, 0);

    static final GridBagConstraints LBL_EDGE_SEARCH_SIGN_EXPLANATION =
            new GridBagConstraints(3, 4, 2, 1, 0, 0, GridBagConstraints.LAST_LINE_END, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 60, 3, 5), 0, 0);

    static final GridBagConstraints LBL_EDGE_SIGN_TYPE =
            new GridBagConstraints(0, 5, 2, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 60, 3, 5), 0, 0);

    static final GridBagConstraints CBB_EDGE_SIGN_TYPE =
            new GridBagConstraints(1, 5, 4, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 10, 3, 10), 0, 160);

    static final GridBagConstraints EDGE_PNL_BTN =
            new GridBagConstraints(2, 6, 3, 1, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 3, 3, 0), 0, 0);

    static final GridBagConstraints LBL_EDGE_CLUSTER_FILTERS =
            new GridBagConstraints(0, 7, 2, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 10, 3, 5), 0, 0);

    static final GridBagConstraints LBL_EDGE_CONFIDENCE_CATEGORY =
            new GridBagConstraints(0, 8, 1, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 30, 3, 5), 0, 0);

    static final GridBagConstraints EDGE_CONFIDENCE_CATEGORY_CB_C1 =
            new GridBagConstraints(1, 8, 4, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE,
                    new Insets(2, 4, 3, 0), 0, 0);

    static final GridBagConstraints EDGE_CONFIDENCE_CATEGORY_CB_C2 =
            new GridBagConstraints(1, 8, 4, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE,
                    new Insets(2, 94, 3, 0), 0, 0);

    static final GridBagConstraints EDGE_CONFIDENCE_CATEGORY_CB_C3 =
            new GridBagConstraints(1, 8, 4, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE,
                    new Insets(2, 185, 3, 0), 0, 0);

    static final GridBagConstraints LBL_EDGE_OSM_COMPARISON =
            new GridBagConstraints(0, 9, 1, 1, 1, 1, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 30, 3, 5), 0, 0);

    static final GridBagConstraints EDGE_OSM_COMPARISON_PANEL =
            new GridBagConstraints(1, 9, 4, 1, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                    new Insets(2, 3, 3, 0), 0, 0);

    private EdgeConstraints() {
    }
}