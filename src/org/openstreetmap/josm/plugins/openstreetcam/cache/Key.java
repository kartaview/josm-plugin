/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.cache;

import java.io.Serializable;
import com.telenav.josm.common.entity.EntityUtil;


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