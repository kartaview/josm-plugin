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
package org.openstreetmap.josm.plugins.openstreetview.argument;

import java.util.Date;
import com.telenav.josm.common.util.EntityUtil;


/**
 * Defines the filters that can be applied to the list methods.
 *
 * @author Beata
 * @version $Revision$
 */
public class ListFilter {

    private final Date date;
    private final String osmUserId;

    public ListFilter(final Date date, final String osmUserId) {
        this.date = date;
        this.osmUserId = osmUserId;
    }

    public Date getDate() {
        return date;
    }

    public String getOsmUserId() {
        return osmUserId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(date);
        result = prime * result + EntityUtil.hashCode(osmUserId);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj instanceof ListFilter) {
            final ListFilter other = (ListFilter) obj;
            result = EntityUtil.bothNullOrEqual(date, other.getDate());
            result = result && EntityUtil.bothNullOrEqual(osmUserId, other.getOsmUserId());
        }
        return result;
    }
}