/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;


/**
 * Base handler for mouse selection.
 *
 * @author nicoleta.viregan
 */
public abstract class BaseMouseSelectionHandler extends MouseAdapter {

    /** Defines the number of mouse clicks that is considered to be an un-selection action. */
    private static final int UNSELECT_CLICK_COUNT = 2;

    @Override
    public void mouseClicked(final MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event) && selectionAllowed()) {
            SwingUtilities.invokeLater(() -> {
                if (event.getClickCount() == UNSELECT_CLICK_COUNT) {
                    handleMapDataUnselection();
                } else {
                    handleMapSelection(event.getPoint());
                }
            });
        }
    }

    /**
     * Handles the situation when map data is selected.
     */
    public abstract void handleMapSelection(final Point point);

    /**
     * Handles the situation when the previously selected map data is un-selected.
     */
    public abstract void handleMapDataUnselection();

    /**
     * Checks if the map selection is allowed.
     */
    public abstract boolean selectionAllowed();
}