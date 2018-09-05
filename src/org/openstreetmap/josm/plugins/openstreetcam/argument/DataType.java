/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c) 2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import java.util.Arrays;
import java.util.List;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public enum DataType {

    PHOTO, DETECTION, CLUSTER;


    public static DataType getDataType(final String value) {
        final List<DataType> values = Arrays.asList(DataType.values());
        DataType dataType = values.stream().filter(m -> m.toString().equals(value)).findAny().orElse(null);
        if (dataType == null) {
            if (GuiConfig.getInstance().getDlgFilterDataTypeImageTxt().equals(value)) {
                dataType = DataType.PHOTO;
            } else if (GuiConfig.getInstance().getDlgFilterDataTypeDetectionsTxt().equals(value)) {
                dataType = DataType.DETECTION;
            }
        }
        return dataType;
    }
}