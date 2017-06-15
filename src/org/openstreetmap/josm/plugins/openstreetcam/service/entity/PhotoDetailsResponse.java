/*
 *  Copyright 2017 Telenav, Inc.
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
package org.openstreetmap.josm.plugins.openstreetcam.service.entity;

import com.telenav.josm.common.entity.Status;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class PhotoDetailsResponse extends Response {

    private final OSV osv;


    public PhotoDetailsResponse(final Status status, final OSV osv) {
        super(status);
        this.osv = osv;
    }


    public OSV getOsv() {
        return osv;
    }
}