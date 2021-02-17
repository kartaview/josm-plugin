/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.cache;

import java.io.Serializable;
import com.grab.josm.common.entity.EntityUtil;


/**
 * Defines the attributes of the cache key.
 *
 * @author beataj
 * @version $Revision$
 */
class Key implements Serializable {

    private static final long serialVersionUID = -5022787421996324345L;
    private final Long sequenceId;
    private final String imageName;


    Key(final Long sequenceId, final String imageName) {
        this.sequenceId = sequenceId;
        this.imageName = imageName;
    }

    Long getSequenceId() {
        return sequenceId;
    }

    String getImageName() {
        return imageName;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(imageName);
        result = prime * result + EntityUtil.hashCode(sequenceId);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final Key other = (Key) obj;
            result = EntityUtil.bothNullOrEqual(sequenceId, other.getSequenceId());
            result = result && EntityUtil.bothNullOrEqual(imageName, other.getImageName());
        }
        return result;
    }

    @Override
    public String toString() {
        return sequenceId + " " + imageName;
    }
}