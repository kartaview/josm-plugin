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
package org.openstreetmap.josm.plugins.openstreetview.service.entity;

import java.util.List;


/**
 * Defines a general response that is returned by the list methods.
 *
 * @author Beata
 * @version $Revision$
 */
class ListResponse extends Response {

    // setters are not required,since GSON sets the fields directly using reflection.
    private List<Integer> totalFilteredItems;



    public List<Integer> getTotalFilteredItems() {
        return totalFilteredItems;
    }
}