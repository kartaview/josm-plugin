/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.handler;


/**
 * Exception thrown by the photo handler.
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoHandlerException extends Exception {

    private static final long serialVersionUID = -6666879046927057243L;

    /**
     * Builds a new exception with the given message.
     *
     * @param msg the exception message
     */
    public PhotoHandlerException(final String msg) {
        super(msg);
    }

    /**
     * Builds a new exception with the given cause.
     *
     * @param cause the exception cause
     */
    public PhotoHandlerException(final Throwable cause) {
        super(cause);
    }

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