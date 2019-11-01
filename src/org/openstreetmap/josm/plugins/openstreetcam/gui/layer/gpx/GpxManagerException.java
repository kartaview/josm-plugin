/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.gui.layer.gpx;


/**
 * Custom exception used by GPX manager.
 *
 * @author beataj
 * @version $Revision$
 */
public class GpxManagerException extends Exception {

    private static final long serialVersionUID = -5216934606681093149L;

    /**
     * Builds a new exception with the given cause.
     *
     * @param cause the exception cause
     */
    public GpxManagerException(final Throwable cause) {
        super(cause);
    }
}