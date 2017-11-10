/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.openstreetcam.service.apollo;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.openstreetcam.service.BaseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 *
 * @author beataj
 * @version $Revision$
 */
public class ApolloService extends BaseService {

    @Override
    public Gson createGson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LatLon.class, new LatLonDeserializer());
        return builder.create();
    }
}