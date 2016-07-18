/*
 *  Copyright 2016 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.openstreetview.service;


/**
 * Exception to be thrown by the service component.
 *
 * @author Beata
 * @version $Revision$
 */
public class OpenStreetViewServiceException extends Exception {

    private static final long serialVersionUID = -7049128049723081647L;

    /**
     * Builds a new object with the given message.
     *
     * @param msg a {@code String}
     */
    public OpenStreetViewServiceException(final String msg) {
        super(msg);
    }

    /**
     * Builds a new object with the given cause.
     *
     * @param cause a {@code Throwable}
     */
    public OpenStreetViewServiceException(final Throwable cause) {
        super(cause);
    }

    /**
     * Builds a new object with the given message and cause.
     *
     * @param msg a {@code String}
     * @param cause a {@code Throwable}
     */
    public OpenStreetViewServiceException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}