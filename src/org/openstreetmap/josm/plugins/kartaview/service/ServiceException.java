/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service;


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