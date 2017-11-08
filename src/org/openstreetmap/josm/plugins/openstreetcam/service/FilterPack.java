/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service;

import java.util.Date;
import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.SignType;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class FilterPack {

    private final Long externalId;
    private final Date date;
    private final List<OsmComparison> osmComparisons;
    private final List<EditStatus> editStatuses;
    final List<SignType> signTypes;


    public FilterPack(final Long externalId, final Date date, final List<OsmComparison> osmComparisons,
            final List<EditStatus> editStatuses, final List<SignType> signTypes) {
        this.externalId = externalId;
        this.date = date;
        this.osmComparisons = osmComparisons;
        this.editStatuses = editStatuses;
        this.signTypes = signTypes;
    }

    public Long getExternalId() {
        return externalId;
    }

    public Date getDate() {
        return date;
    }

    public List<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    public List<EditStatus> getEditStatuses() {
        return editStatuses;
    }


    public List<SignType> getSignTypes() {
        return signTypes;
    }
}