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
package org.openstreetmap.josm.plugins.openstreetview.observer;

/**
 * The observable interface for the {@code LocationObserver} object.
 *
 * @author Beata
 * @version $Revision$
 */
public interface LocationObservable {

    /**
     * Registers the given observer.
     *
     * @param observer a {@code LocationObserver}
     */
    void registerObserver(LocationObserver observer);

    /**
     * Notifies the observers listening.
     */
    void notifyObserver();
}