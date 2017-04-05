/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam;


/**
 * Exception thrown by the image handler.
 *
 * @author beataj
 * @version $Revision$
 */
public class ImageHandlerException extends Exception {

    private static final long serialVersionUID = -6666879046927057243L;

    /**
     *
     * @param msg
     */
    public ImageHandlerException(final String msg) {
        super(msg);
    }

    /**
     *
     * @param cause
     */
    public ImageHandlerException(final Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param msg
     * @param cause
     */
    public ImageHandlerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}