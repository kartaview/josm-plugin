/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service;


/**
 * Exception to be thrown by the service component.
 *
 * @author Beata
 * @version $Revision$
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = -7049128049723081647L;

    /**
     * Builds a new object with the given message.
     *
     * @param msg a {@code String}
     */
    public ServiceException(final String msg) {
        super(msg);
    }

    /**
     * Builds a new object with the given cause.
     *
     * @param cause a {@code Throwable}
     */
    public ServiceException(final Throwable cause) {
        super(cause);
    }

    /**
     * Builds a new object with the given message and cause.
     *
     * @param msg a {@code String}
     * @param cause a {@code Throwable}
     */
    public ServiceException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}