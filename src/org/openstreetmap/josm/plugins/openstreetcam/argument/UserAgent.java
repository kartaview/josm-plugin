/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.openstreetcam.argument;

import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.data.Version;
import org.openstreetmap.josm.data.osm.User;
import org.openstreetmap.josm.plugins.openstreetcam.util.pref.PreferenceManager;


/**
 * Defines the user agent HTTP request argument.
 *
 * @author beataj
 * @version $Revision$
 */
public class UserAgent {

    private final String josmVersion;
    private final String openStreetCamVersion;
    private final String osmUserInfo;


    public UserAgent() {
        josmVersion = Version.getInstance().getVersionString();
        openStreetCamVersion = PreferenceManager.getInstance().loadPluginLocalVersion();
        final User user = UserIdentityManager.getInstance().asUser();
        osmUserInfo = user.getId() > 0 ? Long.toString(user.getId()) : user.getName();
    }


    public String getJosmVersion() {
        return josmVersion;
    }

    public String getOpenStreetCamVersion() {
        return openStreetCamVersion;
    }

    public String getOsmUserInfo() {
        return osmUserInfo;
    }

    @Override
    public String toString() {
        return "JOSM/" + josmVersion + ", OpenStreetCam/" + openStreetCamVersion + ", User Info/" + osmUserInfo;
    }
}