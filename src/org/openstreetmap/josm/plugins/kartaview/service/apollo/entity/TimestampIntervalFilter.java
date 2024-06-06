/*
 * Copyright 2024 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.kartaview.service.apollo.entity;

import java.time.Instant;


/**
 * POJO class used as timestamp filtering criteria.
 *
 * @author nicoleta.viregan
 */
public class TimestampIntervalFilter {

    private final Instant toTimestamp;
    private final Instant fromTimestamp;

    public TimestampIntervalFilter(final Instant to, final Instant toTimestamp) {
        this.toTimestamp = to;
        this.fromTimestamp = toTimestamp;
    }

    public Instant getToTimestamp() {
        return toTimestamp;
    }

    public Instant getFromTimestamp() {
        return fromTimestamp;
    }
}