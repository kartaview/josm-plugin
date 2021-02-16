/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.handler;


/**
 * Exception thrown by the photo handler.
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoHandlerException extends Exception {

    private static final long serialVersionUID = -6666879046927057243L;

    /**
     * Builds a new exception with the given message and cause.
     *
     * @param msg the exception message
     * @param cause the exception cause
     */
    public PhotoHandlerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}