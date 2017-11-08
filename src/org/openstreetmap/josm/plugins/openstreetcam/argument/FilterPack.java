/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import java.util.Date;
import java.util.Set;
import org.openstreetmap.josm.plugins.openstreetcam.entity.EditStatus;
import org.openstreetmap.josm.plugins.openstreetcam.entity.OsmComparison;
import org.openstreetmap.josm.plugins.openstreetcam.entity.ValidationStatus;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class FilterPack {

    private final String externalId;
    private final Date date;
    private final Set<OsmComparison> osmComparisons;
    private final Set<EditStatus> editStatuses;
    final Set<ValidationStatus> validationStatuses;
    final Set<String> types;


    public FilterPack(final String externalId, final Date date, final Set<OsmComparison> osmComparisons,
            final Set<EditStatus> editStatuses, final Set<ValidationStatus> validationStatuses,
            final Set<String> types) {
        this.externalId = externalId;
        this.date = date;
        this.osmComparisons = osmComparisons;
        this.editStatuses = editStatuses;
        this.validationStatuses = validationStatuses;
        this.types = types;
    }

    public String getExternalId() {
        return externalId;
    }

    public Date getDate() {
        return date;
    }

    public Set<OsmComparison> getOsmComparisons() {
        return osmComparisons;
    }

    public Set<EditStatus> getEditStatuses() {
        return editStatuses;
    }

    public Set<ValidationStatus> getValidationStatuses() {
        return validationStatuses;
    }

    public Set<String> getTypes() {
        return types;
    }
}