/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
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
        return new StringBuilder("JOSM/").append(josmVersion).append(", OpenStreetCam/").append(openStreetCamVersion)
                .append(", User Info/").append(osmUserInfo).toString();
    }
}