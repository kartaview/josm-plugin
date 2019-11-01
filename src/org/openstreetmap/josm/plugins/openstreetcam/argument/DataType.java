/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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